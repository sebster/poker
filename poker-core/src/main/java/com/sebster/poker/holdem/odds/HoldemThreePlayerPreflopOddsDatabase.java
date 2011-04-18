package com.sebster.poker.holdem.odds;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

import net.jcip.annotations.Immutable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sebster.poker.Deck;
import com.sebster.poker.Hole;
import com.sebster.poker.Holes;
import com.sebster.poker.odds.Constants;
import com.sebster.util.IOUtil;

public final class HoldemThreePlayerPreflopOddsDatabase {

	private static final Logger LOG = LoggerFactory.getLogger(HoldemThreePlayerPreflopOddsDatabase.class);

	private static final String DB_RESOURCE_NAME = "holdem_3p_preflop_full.db";

	private static final int PLAYERS = 3;

	private static final class HoldemThreePlayerPreflopOddsDatabaseHolder {

		private static final HoldemThreePlayerPreflopOddsDatabase database;

		static {
			InputStream in = null;
			try {
				in = HoldemThreePlayerPreflopOddsDatabase.class.getResourceAsStream(DB_RESOURCE_NAME);
				database = new HoldemThreePlayerPreflopOddsDatabase(new BufferedInputStream(in, 16 * 1024));
			} catch (final IOException e) {
				// Should not happen.
				throw new RuntimeException(e);
			} finally {
				IOUtil.closeQuietly(in);
			}
		}

	}

	public static HoldemThreePlayerPreflopOddsDatabase getInstance() {
		return HoldemThreePlayerPreflopOddsDatabaseHolder.database;
	}

	private final int records;

	private final int[] data;

	public HoldemThreePlayerPreflopOddsDatabase(final InputStream in) throws IOException {
		final long t1 = System.currentTimeMillis();
		records = IOUtil.readInt4(in);
		data = new int[records * 14];
		for (int k = 0; k < data.length; k++) {
			data[k] = IOUtil.readInt4(in);
		}
		final long t2 = System.currentTimeMillis();
		LOG.info("3 player db init in {} ms, {} records", t2 - t1, records);
	}

	public IndexedOdds getOdds(final Hole hole1, final Hole hole2, final Hole hole3) {
		final Hole[] normalizedHoles = new Hole[] { hole1, hole2, hole3 };
		final int[] indexes = Holes.normalize(normalizedHoles);
		long longMatchupIndex = 0;
		for (int i = 0; i < PLAYERS; i++) {
			longMatchupIndex = longMatchupIndex * Constants.HOLE_COUNT + normalizedHoles[i].getIndex();
		}
		final int matchupIndex = (int) (Integer.MIN_VALUE + longMatchupIndex);
		final int dataIndex = findDataIndex(matchupIndex);
		final int offset = dataIndex * 14 + 1;
		return new IndexedOdds(data, offset, indexes);
	}

	private int findDataIndex(final int handIndex) {
		int low = 0;
		int high = records - 1;

		while (low <= high) {
			final int mid = low + high >>> 1;
			final int midVal = data[mid * 14];

			if (midVal < handIndex) {
				low = mid + 1;
			} else if (midVal > handIndex) {
				high = mid - 1;
			} else {
				return mid;
			}
		}
		throw new IllegalArgumentException("invalid hand index: " + handIndex);
	}

	@Immutable
	public final static class IndexedOdds {

		public static final int RANK_111 = 0;
		public static final int RANK_112 = 1;
		public static final int RANK_121 = 2;
		public static final int RANK_122 = 3;
		public static final int RANK_123 = 4;
		public static final int RANK_132 = 5;
		public static final int RANK_211 = 6;
		public static final int RANK_212 = 7;
		public static final int RANK_213 = 8;
		public static final int RANK_221 = 9;
		public static final int RANK_231 = 10;
		public static final int RANK_312 = 11;
		public static final int RANK_321 = 12;

		private final int[] data;

		private final int offset;

		private final int[] indexes;

		public IndexedOdds(final int[] data, final int offset, final int[] indexes) {
			this.data = data;
			this.offset = offset;
			this.indexes = indexes;
		}

		public int get(final int n) {
			return data[offset + n];
		}

		public int getNWaySplits(final int player, final int n) {
			final int realPlayer = indexes[player];
			if (realPlayer == 0) {
				if (n == 0) { // losses of player 1
					return get(RANK_211) + get(RANK_212) + get(RANK_213) + get(RANK_221) + get(RANK_231) + get(RANK_312) + get(RANK_321);
				} else if (n == 1) { // wins of player 1
					return get(RANK_122) + get(RANK_123) + get(RANK_132);
				} else if (n == 2) { // 2-way splits in 1st place of player 1
					return get(RANK_112) + get(RANK_121);
				}
			} else if (realPlayer == 1) {
				if (n == 0) { // losses of player 2
					return get(RANK_121) + get(RANK_122) + get(RANK_123) + get(RANK_132) + get(RANK_221) + get(RANK_231) + get(RANK_321);
				} else if (n == 1) { // wins of player 2
					return get(RANK_212) + get(RANK_213) + get(RANK_312);
				} else if (n == 2) { // 2-way splits in 1st place of player 2
					return get(RANK_112) + get(RANK_211);
				}
			} else if (realPlayer == 2) {
				if (n == 0) { // losses op player 3
					return get(RANK_112) + get(RANK_122) + get(RANK_123) + get(RANK_132) + get(RANK_212) + get(RANK_213) + get(RANK_312);
				} else if (n == 1) { // wins of player 3
					return get(RANK_221) + get(RANK_231) + get(RANK_321);
				} else if (n == 2) { // 2-way splits in 1st place of player 3
					return get(RANK_121) + get(RANK_211);
				}
			}
			// 3-way split (of any player)
			return get(RANK_111);
		}

	}

	public static void main(final String[] args) {
		if (args.length < 1) {
			throw new IllegalArgumentException("usage: " + HoldemThreePlayerPreflopOddsDatabase.class.getSimpleName() + " <number of rounds>");
		}
		final int rounds = Integer.parseInt(args[0]);
		final HoldemThreePlayerPreflopOddsDatabase database = getInstance();
		LOG.info("running {} rounds", rounds);
		int t = 0;
		final long t1 = System.currentTimeMillis();
		for (int i = 0; i < rounds; i++) {
			final Deck deck = new Deck();
			final Hole[] holes = new Hole[PLAYERS];
			for (int j = 0; j < PLAYERS; j++) {
				holes[j] = Hole.fromCards(deck.draw(), deck.draw());
			}
			final IndexedOdds odds = database.getOdds(holes[0], holes[1], holes[2]);
			t += odds.get(0);
		}
		final long t2 = System.currentTimeMillis();
		LOG.info("calculated value (to avoid code removal by optimiser) t={}", t);
		LOG.info("ran {} rounds in {} seconds", rounds, (t2 - t1) / 1000.0);
	}

}
