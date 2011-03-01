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
import com.sebster.poker.IndexedHole;
import com.sebster.poker.odds.BasicOdds;
import com.sebster.poker.odds.Constants;
import com.sebster.poker.odds.Odds;
import com.sebster.util.arrays.ShortArrayWrapper;

public class PreFlopOddsDB {

	private static final Logger logger = LoggerFactory.getLogger(PreFlopOddsDB.class);

	public static final String DB_RESOURCE_NAME = "holdem_%sp_preflop_full.db";

	/*
	 * Initialize-on-demand holder class idiom.
	 */
	private static class TwoPlayerDBHolder {

		public static final PreFlopOddsDB db = getInstance(2);

	}

	private static final PreFlopOddsDB getInstance(final int players) {
		InputStream in = null;
		try {
			in = PreFlopOddsDB.class.getResourceAsStream(String.format(DB_RESOURCE_NAME, players));
			return new PreFlopOddsDB(in);
		} catch (final IOException e) {
			// Should not happen.
			throw new RuntimeException(e);
		} finally {
			IOUtils.closeQuietly(in);
		}
	}

	public static final PreFlopOddsDB getTwoPlayerDB() {
		return TwoPlayerDBHolder.db;
	}

	private final int players;

	private final Map<ShortArrayWrapper, Odds[]> oddsMap;

	public PreFlopOddsDB(final InputStream in) throws IOException {
		final long t1 = System.currentTimeMillis();
		final DataInputStream dis = new DataInputStream(new BufferedInputStream(new GZIPInputStream(in)));
		this.players = dis.readByte();
		oddsMap = new HashMap<ShortArrayWrapper, Odds[]>();
		while (true) {
			final short[] holes = new short[players];
			final Odds[] odds = new Odds[players];
			// Read first hole index.
			holes[0] = dis.readShort();
			if (holes[0] == -1) {
				// This means EOF.
				break;
			}
			for (int i = 1; i < players; i++) {
				// Read remaining holes.
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
		final IndexedHole[] indexedHoles = Holes.normalize(holes);
		final short[] holeIndexes = new short[holes.length];
		for (int h = 0; h < holes.length; h++) {
			holeIndexes[h] = (short) indexedHoles[h].getHole().getIndex();
		}
		final Odds[] indexedOdds = oddsMap.get(new ShortArrayWrapper(holeIndexes));
		final Odds[] odds = new Odds[holes.length];
		for (int h = 0; h < holes.length; h++) {
			odds[indexedHoles[h].getIndex()] = indexedOdds[h];
		}
		return odds;
	}

}
