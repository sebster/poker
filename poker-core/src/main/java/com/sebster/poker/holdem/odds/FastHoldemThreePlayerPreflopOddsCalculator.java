package com.sebster.poker.holdem.odds;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Random;
import java.util.zip.GZIPInputStream;

import net.jcip.annotations.NotThreadSafe;

import com.sebster.poker.Deck;
import com.sebster.poker.Hole;
import com.sebster.poker.odds.CompressedHandValueDatabase;
import com.sebster.poker.odds.Constants;
import com.sebster.util.ArrayUtils;

@NotThreadSafe
public class FastHoldemThreePlayerPreflopOddsCalculator {

	private static final String DB_FILENAME = "holdem_hand_value_db.lzfi.gz";

	private static final int NUM_HOLES = 3;

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

	private final CompressedHandValueDatabase db;

	/**
	 * The uncompressed hand value arrays for up to 3 hands.
	 */
	private final int[][] udata = new int[NUM_HOLES][Constants.BOARD_COUNT_52];

	/**
	 * The hole indexes of the uncompressed hand value arrays. This allows a
	 * check to see if a required hole index is already present, in which case
	 * it does not need to be uncompressed.
	 */
	private final int[] udataIndexes = new int[NUM_HOLES];

	private int lastExpandTime;

	private int lastExpandCacheHits;

	private int lastCompareTime;

	public FastHoldemThreePlayerPreflopOddsCalculator(final CompressedHandValueDatabase db) {
		if (db == null) {
			throw new NullPointerException("db");
		}
		this.db = db;
		Arrays.fill(udataIndexes, -1);
	}

	public final int[] calculateOdds(final Hole... holes) {

		if (holes.length != NUM_HOLES) {
			throw new IllegalArgumentException("number of holes must be between 2 and 10");
		}

		// Check for duplicate cards and convert holes to indexes.
		final int[] holeIndexes = new int[NUM_HOLES];
		for (int i = 0; i < NUM_HOLES; i++) {
			for (int j = i + 1; j < NUM_HOLES; j++) {
				if (holes[i].intersects(holes[j])) {
					throw new IllegalArgumentException("hole " + holes[i] + " and hole " + holes[j] + " contain common cards");
				}
			}
			holeIndexes[i] = holes[i].getIndex();
		}

		final int[][] udata = this.udata;
		final int[] udataIndexes = this.udataIndexes;
		final int[] odds = new int[13];

		final long t1 = System.currentTimeMillis();

		// Decompress the hands.
		lastExpandCacheHits = 0;
		nh: for (int i = 0; i < NUM_HOLES; i++) {
			final int holeIndex = holeIndexes[i];
			// Scan the current udata for the specified hole.
			for (int j = i; j < NUM_HOLES; j++) {
				if (udataIndexes[j] == holeIndex) {
					// Found cached decomressed hand.
					lastExpandCacheHits++;
					if (i != j) {
						// Make sure it's in the correct place.
						ArrayUtils.swap(udata, i, j);
						ArrayUtils.swap(udataIndexes, i, j);
					}
					continue nh;
				}
			}
			// No cached hand, decompress.
			db.expand(holeIndex, udata[i]);
			udataIndexes[i] = holeIndex;
		}

		final long t2 = System.currentTimeMillis();

		// Compare.
		nb: for (int i = 0; i < Constants.BOARD_COUNT_52; i++) {
			// First check if the board intersects any of the holes.
			for (int j = 0; j < NUM_HOLES; j++) {
				if (udata[j][i] < 0) {
					// Board intersects hole, skip board.
					continue nb;
				}
			}
			// Find the number of times each different outcome occurs.
			final int v0 = udata[0][i], v1 = udata[1][i], v2 = udata[2][i];
			if (v0 < v1) {
				if (v2 < v0) {
					odds[RANK_213]++;
				} else if (v2 > v0) {
					if (v1 < v2) {
						odds[RANK_321]++;
					} else if (v2 > v1) {
						odds[RANK_312]++;
					} else { // v1 == v2
						odds[RANK_211]++;
					}
				} else { // v2 == v0
					odds[RANK_212]++;
				}
			} else if (v0 > v1) {
				if (v2 > v0) {
					odds[RANK_231]++;
				} else if (v2 < v0) {
					if (v1 < v2) {
						odds[RANK_132]++;
					} else if (v2 > v1) {
						odds[RANK_123]++;
					} else { // v1 == v2
						odds[RANK_122]++;
					}
				} else { // v2 == v0
					odds[RANK_121]++;
				}
			} else { // v0 == v1
				if (v0 < v2) {
					odds[RANK_221]++;
				} else if (v0 > v2) {
					odds[RANK_112]++;
				} else { // v0 == v2
					odds[RANK_111]++;
				}
			}
		}

		final long t3 = System.currentTimeMillis();

		// Record the expand and compare times.
		lastExpandTime = (int) (t2 - t1);
		lastCompareTime = (int) (t3 - t2);

		// Return the odds.
		return odds;
	}

	public int getLastExpandTime() {
		return lastExpandTime;
	}

	public int getLastExpandCacheHits() {
		return lastExpandCacheHits;
	}

	public int getLastCompareTime() {
		return lastCompareTime;
	}

	public static void main(final String[] args) throws IOException {

		String dbFilename = DB_FILENAME;
		if (args.length > 0) {
			dbFilename = args[0];
		}

		final InputStream in = new GZIPInputStream(new BufferedInputStream(new FileInputStream(dbFilename)));
		final CompressedHandValueDatabase db = new CompressedHandValueDatabase(in);
		in.close();

		final FastHoldemThreePlayerPreflopOddsCalculator calculator = new FastHoldemThreePlayerPreflopOddsCalculator(db);

		final Random random = new Random();
		final Deck deck = new Deck(random);
		for (int i = 0; i < 50; i++) {
			System.out.println("warmup round " + i);
			final Hole[] holes = new Hole[NUM_HOLES];
			for (int j = 0; j < NUM_HOLES; j++) {
				holes[j] = Hole.fromDeck(deck);
			}
			calculator.calculateOdds(holes);
			deck.shuffle();
		}

		long totalTime = 0, totalExpandTime = 0, totalCompareTime = 0;

		final int rounds = 1000;
		for (int i = 0; i < rounds; i++) {
			final Hole[] holes = new Hole[NUM_HOLES];
			for (int j = 0; j < NUM_HOLES; j++) {
				holes[j] = Hole.fromDeck(deck);
			}
			deck.shuffle();

			System.out.println();
			System.out.println("round=" + i + " hands=" + Arrays.toString(holes));

			final int[] odds = calculator.calculateOdds(holes);
			int sum = 0;
			for (final int odd : odds) {
				sum += odd;
			}
			System.out.println("odds=" + Arrays.toString(odds) + " sum=" + sum);

			final int expandTime = calculator.getLastExpandTime();
			final int compareTime = calculator.getLastCompareTime();
			final int total = expandTime + compareTime;
			System.out.println("expand=" + expandTime + " compare=" + compareTime + " total=" + total + " ms");
			totalTime += total;
			totalExpandTime += expandTime;
			totalCompareTime += compareTime;

			if (sum != Constants.getHole2BoardCount(NUM_HOLES)) {
				System.out.println("***** BOARD COUNT INCORRECT ***** (" + sum + " != " + Constants.getHole2BoardCount(NUM_HOLES) + ")");
			}
			// if (NUM_HOLES == 2) {
			// final Odds odds2 =
			// TwoPlayerPreFlopOddsDB.getInstance().getOdds(holes[0], holes[1]);
			// if (!(odds.getLosses() == odds2.getLosses() && odds.getWins() ==
			// odds2.getWins() && odds.getSplits() == odds2.getSplits())) {
			// System.out.println("***** ODDS INCORRECT *****");
			// System.out.println("should be " + odds2);
			// }
			// }

		}

		System.out.println("avg expand=" + totalExpandTime / rounds + " avg compare=" + totalCompareTime / rounds + " avg total=" + totalTime / rounds + " ms");
	}
}
