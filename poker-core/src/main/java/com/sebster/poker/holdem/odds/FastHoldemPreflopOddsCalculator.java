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
import com.sebster.poker.odds.BasicOdds;
import com.sebster.poker.odds.CompressedHandValueDatabase;
import com.sebster.poker.odds.Constants;
import com.sebster.poker.odds.Odds;
import com.sebster.util.ArrayUtils;

@NotThreadSafe
public class FastHoldemPreflopOddsCalculator implements HoldemPreflopOddsCalculator {

	public static final String DB_FILENAME = "holdem_hand_value_db.lzfi.gz";

	private final CompressedHandValueDatabase db;

	/**
	 * The uncompressed hand value arrays for up to 10 hands.
	 */
	private final int[][] udata = new int[10][Constants.BOARD_COUNT_52];

	/**
	 * The hole indexes of the uncompressed hand value arrays. This allows a
	 * check to see if a required hole index is already present, in which case
	 * it does not need to be uncompressed.
	 */
	private final int[] udataIndexes = new int[10];

	private int lastExpandTime;

	private int lastExpandCacheHits;

	private int lastCompareTime;

	public FastHoldemPreflopOddsCalculator(final CompressedHandValueDatabase db) {
		if (db == null) {
			throw new NullPointerException("db");
		}
		this.db = db;
		Arrays.fill(udataIndexes, -1);
	}

	@Override
	public final Odds[] calculateOdds(final Hole... holes) {

		final int numHoles = holes.length;
		if (numHoles < 2 || numHoles > 10) {
			throw new IllegalArgumentException("number of holes must be between 2 and 10");
		}

		// Check for duplicate cards and convert holes to indexes.
		final int[] holeIndexes = new int[numHoles];
		for (int i = 0; i < numHoles; i++) {
			for (int j = i + 1; j < numHoles; j++) {
				if (holes[i].intersects(holes[j])) {
					throw new IllegalArgumentException("hole " + holes[i] + " and hole " + holes[j] + " contain common cards");
				}
			}
			holeIndexes[i] = holes[i].getIndex();
		}

		final int[][] udata = this.udata;
		final int[] udataIndexes = this.udataIndexes;
		final int[][] nWaySplits = new int[numHoles][numHoles + 1];

		final long t1 = System.currentTimeMillis();

		// Decompress the hands.
		lastExpandCacheHits = 0;
		nh: for (int i = 0; i < numHoles; i++) {
			final int holeIndex = holeIndexes[i];
			// Scan the current udata for the specified hole.
			for (int j = i; j < 10; j++) {
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
			for (int j = 0; j < numHoles; j++) {
				if (udata[j][i] < 0) {
					// Board intersects hole, skip board.
					continue nb;
				}
			}
			// Find the maximum hand value, and the number of times it occurs.
			int max = 0, count = 0;
			for (int j = 0; j < numHoles; j++) {
				final int v = udata[j][i];
				if (v < max) {
					// Losing hand.
				} else if (v > max) {
					// New winning hand.
					max = v;
					count = 1;
				} else if (v == max) {
					// Split with current winning hand.
					count++;
				}
			}
			// Count the win/split for the winning hands.
			for (int j = 0; j < numHoles; j++) {
				if (udata[j][i] == max) {
					// Count win/split.
					nWaySplits[j][count]++;
				}
			}
		}

		final long t3 = System.currentTimeMillis();

		// Record the expand and compare times.
		lastExpandTime = (int) (t2 - t1);
		lastCompareTime = (int) (t3 - t2);

		// Create the return value.
		final Odds[] odds = new Odds[numHoles];
		for (int i = 0; i < numHoles; i++) {
			final int[] nWaySplitsI = nWaySplits[i];
			// Compute losses because they were not counted.
			int k = Constants.getHole2BoardCount(numHoles);
			for (int j = 1; j <= numHoles; j++) {
				k -= nWaySplitsI[j];
			}
			nWaySplitsI[0] = k;
			// Initialize the odds for this hole.
			odds[i] = new BasicOdds(nWaySplitsI);
		}
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

		int numHoles = 2;
		if (args.length > 0) {
			numHoles = Integer.parseInt(args[0]);
		}

		String dbFilename = DB_FILENAME;
		if (args.length > 1) {
			dbFilename = args[1];
		}

		final InputStream in = new GZIPInputStream(new BufferedInputStream(new FileInputStream(dbFilename)));
		final CompressedHandValueDatabase db = new CompressedHandValueDatabase(in);
		in.close();

		final FastHoldemPreflopOddsCalculator calculator = new FastHoldemPreflopOddsCalculator(db);

		final Random random = new Random();
		final Deck deck = new Deck(random);
		for (int i = 0; i < 50; i++) {
			System.out.println("warmup round " + i);
			final Hole[] holes = new Hole[numHoles];
			for (int j = 0; j < numHoles; j++) {
				holes[j] = Hole.fromDeck(deck);
			}
			calculator.calculateOdds(holes);
			deck.shuffle();
		}

		long totalTime = 0, totalExpandTime = 0, totalCompareTime = 0;

		for (int i = 0; i < 100; i++) {
			final Hole[] holes = new Hole[numHoles];
			for (int j = 0; j < numHoles; j++) {
				holes[j] = Hole.fromDeck(deck);
			}
			deck.shuffle();

			System.out.println();
			System.out.println("round=" + i + " hands=" + Arrays.toString(holes));

			final Odds odds = calculator.calculateOdds(holes)[0];
			System.out.println("odds[0]=" + odds);

			final int expandTime = calculator.getLastExpandTime();
			final int compareTime = calculator.getLastCompareTime();
			final int total = expandTime + compareTime;
			System.out.println("expand=" + expandTime + " compare=" + compareTime + " total=" + total + " ms");
			totalTime += total;
			totalExpandTime += expandTime;
			totalCompareTime += compareTime;

			if (odds.getTotal() != Constants.getHole2BoardCount(numHoles)) {
				System.out.println("***** BOARD COUNT INCORRECT ***** (" + odds.getTotal() + " != " + Constants.getHole2BoardCount(numHoles) + ")");
			}
			if (numHoles == 2) {
				final Odds odds2 = TwoPlayerPreFlopOddsDB.getInstance().getOdds(holes[0], holes[1]);
				if (!(odds.getLosses() == odds2.getLosses() && odds.getWins() == odds2.getWins() && odds.getSplits() == odds2.getSplits())) {
					System.out.println("***** ODDS INCORRECT *****");
					System.out.println("should be " + odds2);
				}
			}

		}

		System.out.println("avg expand=" + totalExpandTime / 100 + " avg compare=" + totalCompareTime / 100 + " avg total=" + totalTime / 100 + " ms");
	}
}
