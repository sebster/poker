package com.sebster.poker.omaha.odds;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Random;
import java.util.zip.GZIPInputStream;

import com.sebster.poker.Deck;
import com.sebster.poker.Hole;
import com.sebster.poker.Hole4;
import com.sebster.poker.odds.BasicOdds;
import com.sebster.poker.odds.CompressedHandValueDatabase;
import com.sebster.poker.odds.Constants;
import com.sebster.poker.odds.Odds;
import com.sebster.util.ArrayUtils;

public class PreFlopOddsCalculator {

	public static final String DB_FILENAME = "omaha_hand_value_db.lzfi.gz";

	private final CompressedHandValueDatabase db;

	/**
	 * The uncompressed hand value arrays for up to 6 omaha hands, which is 36
	 * two card hands.
	 */
	private final int[][] udata = new int[36][Constants.BOARD_COUNT_52];

	/**
	 * The hole indexes of the uncompressed hand value arrays. This allows a
	 * check to see if a required hole index is already present, in which case
	 * it does not need to be uncompressed.
	 */
	private final int[] udataIndexes = new int[36];

	private int lastExpandTime;

	private int lastExpandCacheHits;

	private int lastCompareTime;

	public PreFlopOddsCalculator(final CompressedHandValueDatabase db) {
		if (db == null) {
			throw new NullPointerException("db");
		}
		this.db = db;
		Arrays.fill(udataIndexes, -1);
	}

	public final Odds[] calculateOdds(final Hole4[] holes) {

		final int numHoles = holes.length;
		if (numHoles < 2 || numHoles > 6) {
			throw new IllegalArgumentException("number of holes must be between 2 and 6");
		}

		// Check for duplicate cards and convert holes to indexes.
		final int num2Holes = numHoles * 6;
		final int[] holeIndexes = new int[num2Holes];
		int l = 0;
		for (int i = 0; i < numHoles; i++) {
			for (int j = i + 1; j < numHoles; j++) {
				if (holes[i].intersects(holes[j])) {
					throw new IllegalArgumentException("hole " + holes[i] + " and hole " + holes[j] + " contain common cards");
				}
			}
			final Hole[] twoCardHoles = holes[i].getAll2CardHoles();
			for (int j = 0; j < 6; j++) {
				holeIndexes[l++] = twoCardHoles[j].getIndex();
			}
		}

		final int[][] udata = this.udata;
		final int[][] nWaySplits = new int[numHoles][numHoles + 1];

		final long t1 = System.currentTimeMillis();

		// Decompress the hands.
		lastExpandCacheHits = 0;
		nh: for (int i = 0; i < num2Holes; i++) {
			final int holeIndex = holeIndexes[i];
			// Scan the current udata for the specified hole.
			for (int j = i; j < 36; j++) {
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
		final int[] max2 = new int[numHoles];
		nb: for (int i = 0; i < Constants.BOARD_COUNT_52; i++) {
			for (int j = 0; j < num2Holes; j++) {
				if (udata[j][i] < 0) {
					continue nb;
				}
			}
			int max = 0, count = 0;
			l = 0;
			for (int j = 0; j < numHoles; j++) {
				int maxv = 0;
				for (int k = 0; k < 6; k++) {
					final int v = udata[l++][i];
					if (v > maxv) {
						maxv = v;
					}
				}
				max2[j] = maxv;
				if (maxv < max) {
					// Losing hand.
				} else if (maxv == max) {
					// Split.
					count++;
				} else {
					// New winning hand.
					max = maxv;
					count = 1;
				}
			}
			for (int j = 0; j < numHoles; j++) {
				if (max2[j] == max) {
					nWaySplits[j][count]++;
				}
			}
		}
		final long t3 = System.currentTimeMillis();

		lastExpandTime = (int) (t2 - t1);
		lastCompareTime = (int) (t3 - t2);

		// Create return value.
		final Odds[] odds = new Odds[numHoles];
		for (int i = 0; i < numHoles; i++) {
			final int[] nWaySplitsI = nWaySplits[i];
			int k = Constants.getHole4BoardCount(numHoles);
			for (int j = 1; j <= numHoles; j++) {
				k -= nWaySplitsI[j];
			}
			nWaySplitsI[0] = k;
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

		final PreFlopOddsCalculator calculator = new PreFlopOddsCalculator(db);

		final Random random = new Random(0);
		final Deck deck = new Deck(random);
		for (int i = 0; i < 50; i++) {
			System.out.println("warmup round " + i);
			final Hole4[] holes = new Hole4[numHoles];
			for (int j = 0; j < numHoles; j++) {
				holes[j] = Hole4.fromDeck(deck);
			}
			calculator.calculateOdds(holes);
			deck.shuffle();
		}

		long totalTime = 0, totalExpandTime = 0, totalCompareTime = 0;

		for (int i = 0; i < 100; i++) {
			final Hole4[] holes = new Hole4[numHoles];
			for (int j = 0; j < numHoles; j++) {
				holes[j] = Hole4.fromDeck(deck);
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

			if (odds.getTotal() != Constants.getHole4BoardCount(numHoles)) {
				System.out.println("***** BOARD COUNT INCORRECT ***** (" + odds.getTotal() + " != " + Constants.getHole4BoardCount(numHoles) + ")");
			}
		}

		System.out.println("avg expand=" + totalExpandTime / 100 + " avg compare=" + totalCompareTime / 100 + " avg total=" + totalTime / 100 + " ms");
	}
}
