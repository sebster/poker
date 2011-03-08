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

import com.sebster.poker.Hole;
import com.sebster.poker.Holes;
import com.sebster.poker.odds.BasicOdds;
import com.sebster.poker.odds.Constants;
import com.sebster.poker.odds.Odds;
import com.sebster.util.arrays.ShortArrayWrapper;

public class PreflopHoleOddsDatabase {

	private static final Logger logger = LoggerFactory.getLogger(PreflopHoleOddsDatabase.class);

	public static final String DB_RESOURCE_NAME = "holdem_%sp_preflop_full.db";

	private static class TwoPlayerDatabaseHolder {
		static final PreflopHoleOddsDatabase db = loadDatabase(2);
	}

	private static class ThreePlayerDatabaseHolder {
		static final PreflopHoleOddsDatabase db = loadDatabase(3);
	}

	private static final PreflopHoleOddsDatabase loadDatabase(final int players) {
		InputStream in = null;
		try {
			in = PreflopHoleOddsDatabase.class.getResourceAsStream(String.format(DB_RESOURCE_NAME, players));
			return new PreflopHoleOddsDatabase(in);
		} catch (final IOException e) {
			// Should not happen.
			throw new RuntimeException(e);
		} finally {
			IOUtils.closeQuietly(in);
		}
	}

	public static final PreflopHoleOddsDatabase getTwoPlayerDatabase() {
		return TwoPlayerDatabaseHolder.db;
	}

	public static final PreflopHoleOddsDatabase getThreePlayerDatabase() {
		return ThreePlayerDatabaseHolder.db;
	}

	public static final PreflopHoleOddsDatabase getDatabase(final int players) {
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

	private final Map<ShortArrayWrapper, Odds[]> oddsMap;

	public PreflopHoleOddsDatabase(final InputStream in) throws IOException {
		final long t1 = System.currentTimeMillis();
		final DataInputStream dis = new DataInputStream(new BufferedInputStream(new GZIPInputStream(in)));
		this.players = dis.readByte();
		final int records = dis.readInt();
		oddsMap = new HashMap<ShortArrayWrapper, Odds[]>();
		for (int k = 0; k < records; k++) {
			final short[] holes = new short[players];
			final Odds[] odds = new Odds[players];
			for (int i = 0; i < players; i++) {
				holes[i] = dis.readShort();
			}
			for (int i = 0; i < players; i++) {
				final int[] nWaySplits = new int[players + 1];
				nWaySplits[0] = Constants.getHole2BoardCount(players);
				for (int n = 0; n < players; n++) {
					nWaySplits[n + 1] = dis.readInt();
					nWaySplits[0] -= nWaySplits[n + 1];
				}
				odds[i] = new BasicOdds(nWaySplits);
			}
			oddsMap.put(new ShortArrayWrapper(holes), odds);
		}
		final long t2 = System.currentTimeMillis();
		logger.info("{} player db init in {} ms, {} records", new Object[] { players, t2 - t1, oddsMap.size() });
	}

	public Odds[] getOdds(final Hole... holes) {
		if (holes.length != players) {
			throw new IllegalArgumentException("invalid number of holes: " + holes.length);
		}
		final Hole[] normalizedHoles = holes.clone();
		final int[] indexes = Holes.normalize(normalizedHoles);
		final short[] holeIndexes = new short[holes.length];
		for (int h = 0; h < normalizedHoles.length; h++) {
			holeIndexes[h] = (short) normalizedHoles[h].getIndex();
		}
		final Odds[] indexedOdds = oddsMap.get(new ShortArrayWrapper(holeIndexes));
		final Odds[] odds = new Odds[holes.length];
		for (int h = 0; h < odds.length; h++) {
			odds[indexes[h]] = indexedOdds[h];
		}
		return odds;
	}

}
