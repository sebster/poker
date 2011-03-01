package com.sebster.poker.holdem.odds.generation;

import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sebster.poker.Card;
import com.sebster.poker.CardSet;
import com.sebster.poker.Combination;
import com.sebster.poker.Hole;
import com.sebster.poker.Holes;
import com.sebster.poker.holdem.odds.PreFlopOddsCalculator;
import com.sebster.poker.odds.BasicOdds;
import com.sebster.poker.odds.CompressedHandValueDB;
import com.sebster.poker.odds.Odds;
import com.sebster.util.ArrayUtils;
import com.sebster.util.arrays.ShortArrayWrapper;

public class PreFlopOddsDBGenerator {

	private static final Logger logger = LoggerFactory.getLogger(PreFlopOddsDBGenerator.class);

	private final PreFlopOddsCalculator calculator;

	private final int players;

	private final String outputFileName;

	public PreFlopOddsDBGenerator(final String compressedHandValueDBFileName, final String outputFileName, final int players) throws IOException {
		if (compressedHandValueDBFileName == null) {
			logger.info("not using compressed hand value database: THIS IS SLOW!");
			calculator = null;
		} else {
			logger.info("reading compressed hand value database from {}", compressedHandValueDBFileName);
			final InputStream in = new GZIPInputStream(new BufferedInputStream(new FileInputStream(compressedHandValueDBFileName)));
			final CompressedHandValueDB db = new CompressedHandValueDB(in);
			in.close();
			calculator = new PreFlopOddsCalculator(db);
		}

		this.players = players;
		this.outputFileName = outputFileName;
	}

	public void generate() throws FileNotFoundException, IOException {
		logger.info("generating {} player preflop odds database to output file {}", players, outputFileName);
		final long t1 = System.currentTimeMillis();
		final Set<ShortArrayWrapper> seen = new HashSet<ShortArrayWrapper>();
		final DataOutputStream dos = new DataOutputStream(new GZIPOutputStream(new FileOutputStream(outputFileName)));
		dos.writeByte(players);
		iterateHands(dos, seen, 0, new Hole[players]);
		dos.writeShort(-1);
		dos.close();
		final long t2 = System.currentTimeMillis();
		logger.info("generation complete: {} records, {} seconds", seen.size(), (t2 - t1) / 1000);
	}

	private void iterateHands(final DataOutputStream dos, final Set<ShortArrayWrapper> seen, final int index, final Hole[] holes) throws IOException {

		if (index == holes.length) {
			final Hole[] normalizedHoles = holes.clone();
			Holes.normalize(normalizedHoles);
			final short[] holeIndexes = new short[holes.length];
			for (int h = 0; h < holes.length; h++) {
				holeIndexes[h] = (short) normalizedHoles[h].getIndex();
			}
			final ShortArrayWrapper key = new ShortArrayWrapper(holeIndexes);
			if (!seen.contains(key)) {
				final Odds[] odds = calculator != null ? calculator.calculateOdds(normalizedHoles) : calculateOdds(normalizedHoles);
				seen.add(key);
				for (int h = 0; h < holes.length; h++) {
					dos.writeShort(holeIndexes[h]);
				}
				for (int h = 0; h < holes.length; h++) {
					for (int n = 0; n < holes.length; n++) {
						dos.writeInt(odds[h].getNWaySplits(n + 1));
					}
				}
				logger.info("{}: {}", Arrays.toString(normalizedHoles), Arrays.toString(odds));
			}
		} else {
			Hole hole = index == 0 ? Hole.firstHole() : holes[index - 1].next();
			while (hole != null) {
				boolean ok = true;
				for (int i = 0; ok && i < index; i++) {
					ok = ok && !hole.intersects(holes[i]);
				}
				if (ok) {
					holes[index] = hole;
					iterateHands(dos, seen, index + 1, holes);
				}
				hole = hole.next();
			}
		}

	}

	private Odds[] calculateOdds(final Hole[] holes) {
		final EnumSet<Card> deckSet = EnumSet.allOf(Card.class);
		for (final Hole hole : holes) {
			deckSet.remove(hole.get(0));
			deckSet.remove(hole.get(1));
		}
		final int deckSize = deckSet.size();
		final Card[] deck = deckSet.toArray(new Card[deckSize]);
		final Card[] cards = new Card[7];
		final int holeCount = holes.length;
		final int[] handValues = new int[holeCount];
		final int[][] nWaySplits = new int[holeCount][holeCount + 1];
		int total = 0;
		for (int i = 0; i < deckSize; i++) {
			cards[2] = deck[i];
			for (int j = i + 1; j < deckSize; j++) {
				cards[3] = deck[j];
				for (int k = j + 1; k < deckSize; k++) {
					cards[4] = deck[k];
					for (int l = k + 1; l < deckSize; l++) {
						cards[5] = deck[l];
						for (int m = l + 1; m < deckSize; m++) {
							cards[6] = deck[m];
							for (int h = 0; h < holeCount; h++) {
								final Hole hole = holes[h];
								cards[0] = hole.get(0);
								cards[1] = hole.get(1);
								handValues[h] = Combination.getHandValue(CardSet.fromCards(cards));
							}
							final int max = ArrayUtils.max(handValues);
							final int count = ArrayUtils.count(handValues, max);
							for (int h = 0; h < holeCount; h++) {
								if (handValues[h] < max) {
									nWaySplits[h][0]++;
								} else {
									nWaySplits[h][count]++;
								}
							}
							total++;
						}
					}
				}
			}
		}
		final Odds[] odds = new Odds[holeCount];
		for (int h = 0; h < holeCount; h++) {
			odds[h] = new BasicOdds(nWaySplits[h]);
		}
		return odds;
	}

	public static void main(final String[] args) throws IOException {
		if (args.length != 2 && args.length != 3) {
			System.err.println("usage: " + PreFlopOddsCalculator.class.getName() + " <players> <outpuFileName> [handValueDBFileName]");
		}
		final PreFlopOddsDBGenerator generator = new PreFlopOddsDBGenerator(args.length == 3 ? args[2] : null, args[1], Integer.parseInt(args[0]));
		generator.generate();
	}

}
