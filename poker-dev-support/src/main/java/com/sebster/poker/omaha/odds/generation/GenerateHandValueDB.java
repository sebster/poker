package com.sebster.poker.omaha.odds.generation;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.GZIPOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sebster.poker.Card;
import com.sebster.poker.CardSet;
import com.sebster.poker.Combination;
import com.sebster.poker.Hole;

public class GenerateHandValueDB {

	private static final Logger logger = LoggerFactory.getLogger(GenerateHandValueDB.class);

	/**
	 * The default hand value database filename.
	 */
	public static final String FILENAME = "omaha_hand_value_db.bin.gz";

	private static final int[][] COMBINATIONS = {
			{ 0, 1, 2 }, { 0, 1, 3 }, { 0, 1, 4 }, { 0, 2, 3 }, { 0, 2, 4 }, { 0, 3, 4 },
			{ 1, 2, 3 }, { 1, 2, 4 }, { 1, 3, 4 },
			{ 2, 3, 4 }
	};

	/**
	 * Generate the hand value database of for every hole (starting hand) for
	 * every possible board. All boards are considered, even boards that contain
	 * cards in the hole. For these invalid boards the hand value is set to -1.
	 * This will generate a DB of 1326 times 2598960 integer hand values, for a
	 * total of 12.8 GB of data. By default the DB will be saved to
	 * "omaha_hand_values.dat.gz", but if an argument is given, that will be
	 * used as the filename instead.
	 * 
	 * @param args
	 *            the output filename
	 * @throws IOException
	 *             if an I/O exception occurs
	 */
	public static void main(final String[] args) throws IOException {
		final String fileName = args.length == 0 ? FILENAME : args[0];
		final DataOutputStream dos = new DataOutputStream(new GZIPOutputStream(new BufferedOutputStream(new FileOutputStream(fileName))));
		final Card[] cards = new Card[5];
		final Card[] board = new Card[5];
		final long t0 = System.currentTimeMillis();
		for (int i = 0; i < 52; i++) { // hand 1
			cards[3] = Card.values()[i];
			for (int j = i + 1; j < 52; j++) { // hand 2
				cards[4] = Card.values()[j];
				final Hole hole = Hole.fromCards(cards[3], cards[4]);
				final long t1 = System.currentTimeMillis();
				logger.info("Hole: {}", hole);
				for (int k = 0; k < 52; k++) { // board 1
					board[0] = Card.values()[k];
					for (int l = k + 1; l < 52; l++) { // board 2
						board[1] = Card.values()[l];
						for (int m = l + 1; m < 52; m++) { // board 3
							board[2] = Card.values()[m];
							for (int n = m + 1; n < 52; n++) { // board 4
								board[3] = Card.values()[n];
								for (int o = n + 1; o < 52; o++) { // board 5
									board[4] = Card.values()[o];
									int maxValue = -1;
									for (final int[] c : COMBINATIONS) {
										cards[0] = board[c[0]];
										cards[1] = board[c[1]];
										cards[2] = board[c[2]];
										final int value = Combination.getBestValue(CardSet.fromCards(cards));
										if (value == -1) {
											// Invalid hand/board combination.
											maxValue = -1;
											break;
										}
										if (value > maxValue) {
											maxValue = value;
										}
									}
									dos.writeInt(maxValue);
								}
							}
						}
					}
				}
				final long t2 = System.currentTimeMillis();
				logger.info("Hole processing took {} seconds", (t2 - t1) / 1000.0);
			}
		}
		dos.close();
		final long t3 = System.currentTimeMillis();
		logger.info("Database generation took {} hours", (t3 - t0) / 3600.0 / 1000.0);
	}

}
