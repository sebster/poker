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
import com.sebster.poker.odds.Odds;
import com.sebster.util.IOUtil;

import edu.umd.cs.findbugs.annotations.SuppressWarnings;

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
		data = new int[records * (1 + PLAYERS * PLAYERS)];
		for (int k = 0; k < data.length; k++) {
			data[k] = IOUtil.readInt4(in);
		}
		final long t2 = System.currentTimeMillis();
		LOG.info("3 player db init in {} ms, {} records", t2 - t1, records);
	}

	public Odds[] getOdds(final Hole hole1, final Hole hole2, final Hole hole3) {
		final Hole[] normalizedHoles = new Hole[] { hole1, hole2, hole3 };
		final int[] indexes = Holes.normalize(normalizedHoles);
		long longMatchupIndex = 0;
		for (int i = 0; i < PLAYERS; i++) {
			longMatchupIndex = longMatchupIndex * Constants.HOLE_COUNT + normalizedHoles[i].getIndex();
		}
		final int matchupIndex = (int) (Integer.MIN_VALUE + longMatchupIndex);
		final int dataIndex = findDataIndex(matchupIndex);
		final int offset = dataIndex * (1 + PLAYERS * PLAYERS) + 1;
		final Odds[] odds = new Odds[3];
		for (int i = 0; i < PLAYERS; i++) {
			odds[indexes[i]] = new IndexedOdds(offset + i * PLAYERS);
		}
		return odds;
	}

	private int findDataIndex(final int handIndex) {
		int low = 0;
		int high = records - 1;

		while (low <= high) {
			final int mid = low + high >>> 1;
			final int midVal = data[mid * (1 + PLAYERS * PLAYERS)];

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
	private final class IndexedOdds extends Odds {

		private final int index;

		@SuppressWarnings(value = "JCIP_FIELD_ISNT_FINAL_IN_IMMUTABLE_CLASS", justification = "cached value")
		private int losses;

		public IndexedOdds(final int index) {
			this.index = index;
		}

		@Override
		public int getNWaySplits(final int n) {
			if (n > 0) {
				return data[index + n - 1];
			}
			if (losses == 0) {
				int losses = getTotal();
				for (int i = 0; i < PLAYERS; i++) {
					losses -= data[index + i];
				}
				this.losses = losses;
			}
			return losses;
		}

		@Override
		public int getMaxN() {
			return PLAYERS;
		}

		@Override
		public int getTotal() {
			return Constants.BOARD_COUNT_46;
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
			final Odds[] odds = database.getOdds(holes[0], holes[1], holes[2]);
			t += odds[0].getWins() + odds[1].getWins() + odds[2].getWins();
		}
		final long t2 = System.currentTimeMillis();
		LOG.info("calculated value (to avoid code removal by optimiser) t={}", t);
		LOG.info("ran {} rounds in {} seconds", rounds, (t2 - t1) / 1000.0);
	}

}
