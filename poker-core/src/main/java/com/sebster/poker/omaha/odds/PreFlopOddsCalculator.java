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
import com.sebster.poker.odds.CompressedHandValueDB;
import com.sebster.poker.odds.Constants;
import com.sebster.poker.odds.Odds;

public class PreFlopOddsCalculator {

	public static final String DB_FILENAME = "omaha_hand_value_db.lzfi.gz";

	/**
	 * The compressed hand value database.
	 */
	private final CompressedHandValueDB db;

	/**
	 * The uncompressed hand value arrays for up to 6 omaha hands, which is 36
	 * two card hands.
	 */
	private final int[][] udata;

	private int lastExpandTime;

	private int lastCompareTime;

	public PreFlopOddsCalculator(final CompressedHandValueDB db) {
		this(db, new int[36][Constants.BOARD_COUNT_52]);
	}

	public PreFlopOddsCalculator(final CompressedHandValueDB db, int[][] udata) {
		if (db == null) {
			throw new NullPointerException("db");
		}
		if (udata == null) {
			throw new NullPointerException("udata");
		}
		if (udata.length < 36) {
			throw new IllegalArgumentException("udata must have at least length 36");
		}
		for (int i = 0; i < 36; i++) {
			if (udata[i].length < Constants.BOARD_COUNT_52) {
				throw new IllegalArgumentException("udata[" + i + "] must have at least length " + Constants.BOARD_COUNT_52);
			}
		}
		this.db = db;
		this.udata = udata;
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
		for (int i = 0; i < num2Holes; i++) {
			db.expand(holeIndexes[i], udata[i]);
		}

		final long t2 = System.currentTimeMillis();

		// Compare.
		int[] max2 = new int[numHoles];
		nb: for (int i = 0; i < Constants.BOARD_COUNT_52; i++) {
			int max = -1, count = 0;
			l = 0;
			for (int j = 0; j < numHoles; j++) {
				int maxv = -1;
				for (int k = 0; k < 6; k++) {
					final int v = udata[l++][i];
					if (v < 0) {
						continue nb;
					}
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
				nWaySplits[j][max2[j] == max ? count : 0]++;
			}
		}
		final long t3 = System.currentTimeMillis();

		lastExpandTime = (int) (t2 - t1);
		lastCompareTime = (int) (t3 - t2);

		// Create return value.
		final Odds[] odds = new Odds[numHoles];
		for (int i = 0; i < numHoles; i++) {
			odds[i] = new BasicOdds(nWaySplits[i]);
		}
		return odds;
	}

	public int getLastExpandTime() {
		return lastExpandTime;
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
		final CompressedHandValueDB db = new CompressedHandValueDB(in);
		in.close();

		final PreFlopOddsCalculator calculator = new PreFlopOddsCalculator(db);

		final Random random = new Random();
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

		System.out.println("avg expand=" + (totalExpandTime / 100) + " avg compare=" + (totalCompareTime / 100) + " avg total=" + (totalTime / 100) + " ms");
	}
}
