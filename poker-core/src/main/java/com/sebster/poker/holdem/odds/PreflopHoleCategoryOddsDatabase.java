package com.sebster.poker.holdem.odds;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.GZIPInputStream;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sebster.poker.HoleCategory;
import com.sebster.poker.odds.BasicOdds;
import com.sebster.poker.odds.Odds;
import com.sebster.util.ArrayUtils;
import com.sebster.util.arrays.ByteArrayWrapper;

public class PreflopHoleCategoryOddsDatabase {

	private static final Logger LOG = LoggerFactory.getLogger(PreflopHoleCategoryOddsDatabase.class);

	public static final String DB_RESOURCE_NAME = "holdem_%sp_preflop_cat.db";

	private static class TwoPlayerDatabaseHolder {
		static final PreflopHoleCategoryOddsDatabase db = loadDatabase(2);
	}

	private static class ThreePlayerDatabaseHolder {
		static final PreflopHoleCategoryOddsDatabase db = loadDatabase(3);
	}

	private static final PreflopHoleCategoryOddsDatabase loadDatabase(final int players) {
		InputStream in = null;
		try {
			in = PreflopHoleCategoryOddsDatabase.class.getResourceAsStream(String.format(DB_RESOURCE_NAME, players));
			return new PreflopHoleCategoryOddsDatabase(in);
		} catch (final IOException e) {
			// Should not happen.
			throw new RuntimeException(e);
		} finally {
			IOUtils.closeQuietly(in);
		}
	}

	public static final PreflopHoleCategoryOddsDatabase getTwoPlayerDatabase() {
		return TwoPlayerDatabaseHolder.db;
	}

	public static final PreflopHoleCategoryOddsDatabase getThreePlayerDatabase() {
		return ThreePlayerDatabaseHolder.db;
	}

	public static final PreflopHoleCategoryOddsDatabase getDatabase(final int players) {
		switch (players) {
		case 2:
			return getTwoPlayerDatabase();
		case 3:
			return getThreePlayerDatabase();
		default:
			throw new UnsupportedOperationException("no database available for " + players + " players");
		}
	}

	private final int players;

	private final Map<ByteArrayWrapper, Odds[]> oddsMap;

	public PreflopHoleCategoryOddsDatabase(final InputStream in) throws IOException {
		final long t1 = System.currentTimeMillis();
		final DataInputStream dis = new DataInputStream(new BufferedInputStream(new GZIPInputStream(in)));
		this.players = dis.readByte();
		final int records = dis.readInt();
		oddsMap = new HashMap<ByteArrayWrapper, Odds[]>();
		for (int k = 0; k < records; k++) {
			final byte[] holeCategories = new byte[players];
			final Odds[] odds = new Odds[players];
			for (int i = 0; i < players; i++) {
				holeCategories[i] = dis.readByte();
			}
			for (int i = 0; i < players; i++) {
				final int[] nWaySplits = new int[players + 1];
				for (int n = 0; n <= players; n++) {
					nWaySplits[n] = dis.readInt();
				}
				odds[i] = new BasicOdds(nWaySplits);
			}
			oddsMap.put(new ByteArrayWrapper(holeCategories), odds);
		}
		final long t2 = System.currentTimeMillis();
		LOG.info("{} player db init in {} ms, {} records", new Object[] { players, t2 - t1, oddsMap.size() });
	}

	public Odds[] getOdds(final HoleCategory... holeCategories) {
		if (holeCategories.length != players) {
			throw new IllegalArgumentException("invalid number of holes categories: " + holeCategories.length);
		}
		final HoleCategory[] normalizedHoleCategories = holeCategories.clone();
		final int[] indexes = ArrayUtils.trackedInsertionSort(normalizedHoleCategories);
		final byte[] holeCategoryIndexes = new byte[holeCategories.length];
		for (int h = 0; h < normalizedHoleCategories.length; h++) {
			holeCategoryIndexes[h] = (byte) normalizedHoleCategories[h].ordinal();
		}
		final Odds[] indexedOdds = oddsMap.get(new ByteArrayWrapper(holeCategoryIndexes));
		final Odds[] odds = new Odds[holeCategories.length];
		for (int h = 0; h < holeCategories.length; h++) {
			odds[indexes[h]] = indexedOdds[h];
		}
		return odds;
	}

}
