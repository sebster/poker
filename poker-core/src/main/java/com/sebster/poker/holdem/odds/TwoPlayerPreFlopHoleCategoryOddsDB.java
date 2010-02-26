package com.sebster.poker.holdem.odds;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sebster.poker.holdem.HoleCategory;

public class TwoPlayerPreFlopHoleCategoryOddsDB {

	private static final Logger logger = LoggerFactory.getLogger(TwoPlayerPreFlopHoleCategoryOddsDB.class);

	public static final String DB_RESOURCE_NAME = "preflop_odds_2p_cat.bin";

	/*
	 * Initialize-on-demand holder class idiom.
	 */
	private static class DBHolder {

		public static final TwoPlayerPreFlopHoleCategoryOddsDB db;

		static {
			InputStream in = null;
			try {
				in = TwoPlayerPreFlopHoleCategoryOddsDB.class.getResourceAsStream(DB_RESOURCE_NAME);
				db = new TwoPlayerPreFlopHoleCategoryOddsDB(in);
			} catch (final IOException e) {
				// Should not happen.
				throw new RuntimeException(e);
			} finally {
				IOUtils.closeQuietly(in);
			}
		}

	}

	public static final TwoPlayerPreFlopHoleCategoryOddsDB getInstance() {
		return DBHolder.db;
	}

	private final TwoPlayerOdds[][] odds = new TwoPlayerOdds[169][];

	public TwoPlayerPreFlopHoleCategoryOddsDB(final InputStream in) throws IOException {
		final long t1 = System.currentTimeMillis();
		final DataInputStream dis = new DataInputStream(new BufferedInputStream(in));
		for (int i = 0; i < 169; i++) {
			odds[i] = new TwoPlayerOdds[i + 1];
			for (int j = 0; j <= i; j++) {
				odds[i][j] = new TwoPlayerOdds(dis.readInt(), dis.readInt(), dis.readInt());
			}
		}
		final long t2 = System.currentTimeMillis();
		logger.debug("db init in {} ms", t2 - t1);
	}

	public TwoPlayerOdds getOdds(final HoleCategory holeCategory1, final HoleCategory holeCategory2) {
		final int i = holeCategory1.ordinal(), j = holeCategory2.ordinal();
		return i >= j ? odds[i][j] : odds[j][i].reverse();
	}
	
	public int getHandCombinations(final HoleCategory holeCategory1, final HoleCategory holeCategory2) {
		final int i = holeCategory1.ordinal(), j = holeCategory2.ordinal();
		return (i >= j ? odds[i][j] : odds[j][i]).getTotal() / Constants.BOARD_COUNT_2;

	}

	public double getProbability(final HoleCategory holeCategory1, final HoleCategory holeCategory2) {
		return ((double) getHandCombinations(holeCategory1, holeCategory2)) / (52 * 51 / 2) / (50 * 49 / 2);
	}

}
