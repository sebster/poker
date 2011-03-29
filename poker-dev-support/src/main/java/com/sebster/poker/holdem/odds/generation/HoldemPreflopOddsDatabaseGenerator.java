package com.sebster.poker.holdem.odds.generation;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.zip.GZIPInputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sebster.poker.Hole;
import com.sebster.poker.Holes;
import com.sebster.poker.holdem.odds.FastHoldemPreflopOddsCalculator;
import com.sebster.poker.holdem.odds.HoldemPreflopOddsCalculator;
import com.sebster.poker.holdem.odds.SimpleHoldemPreflopOddsCalculator;
import com.sebster.poker.odds.CompressedHandValueDatabase;
import com.sebster.poker.odds.Odds;
import com.sebster.util.arrays.ShortArrayWrapper;

public class HoldemPreflopOddsDatabaseGenerator {

	private static final Logger logger = LoggerFactory.getLogger(HoldemPreflopOddsDatabaseGenerator.class);

	private final HoldemPreflopOddsCalculator calculator;

	private final int players;

	private final String outputFileName;

	public HoldemPreflopOddsDatabaseGenerator(final String compressedHandValueDBFileName, final String outputFileName, final int players) throws IOException {
		if (compressedHandValueDBFileName == null) {
			logger.info("not using compressed hand value database: THIS IS SLOW!");
			calculator = new SimpleHoldemPreflopOddsCalculator();
		} else {
			logger.info("reading compressed hand value database from {}", compressedHandValueDBFileName);
			final InputStream in = new GZIPInputStream(new BufferedInputStream(new FileInputStream(compressedHandValueDBFileName)));
			final CompressedHandValueDatabase db = new CompressedHandValueDatabase(in);
			in.close();
			calculator = new FastHoldemPreflopOddsCalculator(db);
		}

		this.players = players;
		this.outputFileName = outputFileName;
	}

	public void generate() throws FileNotFoundException, IOException {
		logger.info("generating {} player preflop hole odds database to output file {}", players, outputFileName);
		final long t1 = System.currentTimeMillis();
		final Set<ShortArrayWrapper> seen = new HashSet<ShortArrayWrapper>();
		final RandomAccessFile raf = new RandomAccessFile(new File(outputFileName), "rw");
		final long length = raf.length();
		raf.writeInt(0);
		iterateHands(raf, length, seen, 0, new Hole[players]);
		raf.seek(0);
		raf.writeInt(seen.size());
		raf.close();
		final long t2 = System.currentTimeMillis();
		logger.info("generation complete: {} records, {} seconds", seen.size(), (t2 - t1) / 1000);
	}

	private void iterateHands(final RandomAccessFile raf, final long length, final Set<ShortArrayWrapper> seen, final int index, final Hole[] holes) throws IOException {

		if (index == holes.length) {
			final Hole[] normalizedHoles = holes.clone();
			Holes.normalize(normalizedHoles);
			final short[] holeIndexes = new short[holes.length];
			for (int h = 0; h < normalizedHoles.length; h++) {
				holeIndexes[h] = (short) normalizedHoles[h].getIndex();
			}
			final ShortArrayWrapper key = new ShortArrayWrapper(holeIndexes);
			if (!seen.contains(key)) {
				seen.add(key);
				final long pos = raf.getFilePointer();
				final long bytes = 2 * holes.length + 4 * holes.length * holes.length;
				if (pos + bytes > length) {
					final Odds[] odds = calculator.calculateOdds(normalizedHoles);
					for (int h = 0; h < holes.length; h++) {
						raf.writeShort(holeIndexes[h]);
					}
					for (int h = 0; h < holes.length; h++) {
						for (int n = 0; n < holes.length; n++) {
							raf.writeInt(odds[h].getNWaySplits(n + 1));
						}
					}
					logger.info("{}: {}", Arrays.toString(normalizedHoles), Arrays.toString(odds));
				} else {
					raf.seek(pos + bytes);
				}
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
					iterateHands(raf, length, seen, index + 1, holes);
				}
				hole = hole.next();
			}
		}

	}

	public static void main(final String[] args) throws IOException {
		if (args.length != 2 && args.length != 3) {
			System.err.println("usage: " + HoldemPreflopOddsDatabaseGenerator.class.getName() + " <players> <outpuFileName> [handValueDBFileName]");
			System.exit(1);
		}
		final HoldemPreflopOddsDatabaseGenerator generator = new HoldemPreflopOddsDatabaseGenerator(args.length == 3 ? args[2] : null, args[1], Integer.parseInt(args[0]));
		generator.generate();
	}

}
