package com.sebster.poker.holdem.odds;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Random;
import java.util.zip.GZIPInputStream;

import com.sebster.poker.Deck;
import com.sebster.poker.Hole;
import com.sebster.poker.holdem.odds.generation.CompressHandValueDB;
import com.sebster.poker.odds.BasicOdds;
import com.sebster.poker.odds.CompressedHandValueDB;
import com.sebster.poker.odds.Constants;
import com.sebster.poker.odds.Odds;

public class PreFlopOddsCalculator {

	/**
	 * The compressed hand value database.
	 */
	private final CompressedHandValueDB db;

	/**
	 * The uncompressed hand value arrays for up to 10 hands.
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
		if (udata.length < 10) {
			throw new IllegalArgumentException("udata must have at least length 36");
		}
		for (int i = 0; i < 10; i++) {
			if (udata[i].length < Constants.BOARD_COUNT_52){
				throw new IllegalArgumentException("udata[" + i + "] must have at least length " + Constants.BOARD_COUNT_52);
			}
		}
		this.db = db;
		this.udata = udata;
	}

	public final Odds[] calculateOdds(final Hole[] holes) {

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
		final int[][] nWaySplits = new int[numHoles][numHoles + 1];

		final long t1 = System.currentTimeMillis();

		// Decompress the hands.
		for (int i = 0; i < numHoles; i++) {
			db.expand(holeIndexes[i], udata[i]);
		}

		final long t2 = System.currentTimeMillis();

		// Compare.
		nb: for (int i = 0; i < Constants.BOARD_COUNT_52; i++) {
			int max = -1, count = 0;
			nh: for (int j = 0; j < numHoles; j++) {
				final int v = udata[j][i];
				if (v < 0) {
					continue nb;
				}
				if (v < max) {
					continue nh;
				}
				if (v == max) {
					count++;
				} else {
					max = v;
					count = 1;
				}
			}
			for (int j = 0; j < numHoles; j++) {
				nWaySplits[j][udata[j][i] == max ? count : 0]++;
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

		String dbFilename = CompressHandValueDB.FILENAME;
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
			final Hole[] holes = new Hole[numHoles];
			for (int j = 0; j < numHoles; j++) {
				holes[j] = new Hole(deck.draw(), deck.draw());
			}
			calculator.calculateOdds(holes);
			deck.shuffle();
		}

		long totalTime = 0, totalExpandTime = 0, totalCompareTime = 0;

		for (int i = 0; i < 100; i++) {
			final Hole[] holes = new Hole[numHoles];
			for (int j = 0; j < numHoles; j++) {
				holes[j] = new Hole(deck.draw(), deck.draw());
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

		System.out.println("avg expand=" + (totalExpandTime / 100) + " avg compare=" + (totalCompareTime / 100) + " avg total=" + (totalTime / 100) + " ms");
	}
}
