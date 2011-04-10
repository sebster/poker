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
import com.sebster.poker.holdem.odds.FastHoldemThreePlayerPreflopOddsCalculator;
import com.sebster.poker.odds.CompressedHandValueDatabase;
import com.sebster.poker.odds.Constants;

public class HoldemThreePlayerPreflopOddsDatabaseGenerator {

	private static final Logger logger = LoggerFactory.getLogger(HoldemThreePlayerPreflopOddsDatabaseGenerator.class);

	private static final int PLAYERS = 3;

	private static final int RECORD_SIZE = 4 * (1 + 13);

	private final FastHoldemThreePlayerPreflopOddsCalculator calculator;

	private final String outputFileName;

	public HoldemThreePlayerPreflopOddsDatabaseGenerator(final String compressedHandValueDBFileName, final String outputFileName) throws IOException {
		logger.info("reading compressed hand value database from {}", compressedHandValueDBFileName);
		final InputStream in = new GZIPInputStream(new BufferedInputStream(new FileInputStream(compressedHandValueDBFileName)));
		final CompressedHandValueDatabase db = new CompressedHandValueDatabase(in);
		in.close();
		calculator = new FastHoldemThreePlayerPreflopOddsCalculator(db);
		this.outputFileName = outputFileName;
	}

	public void generate() throws FileNotFoundException, IOException {
		logger.info("generating {} player preflop hole odds database to output file {}", PLAYERS, outputFileName);
		final long t1 = System.currentTimeMillis();
		final Set<Integer> seen = new HashSet<Integer>();
		final RandomAccessFile raf = new RandomAccessFile(new File(outputFileName), "rw");
		final long length = raf.length();
		raf.writeInt(0);
		iterateHands(raf, length, seen, 0, new Hole[PLAYERS]);
		raf.seek(0);
		raf.writeInt(seen.size());
		raf.close();
		final long t2 = System.currentTimeMillis();
		logger.info("generation complete: {} records, {} seconds", seen.size(), (t2 - t1) / 1000);
	}

	private void iterateHands(final RandomAccessFile raf, final long length, final Set<Integer> seen, final int index, final Hole[] holes) throws IOException {

		if (index == holes.length) {
			final Hole[] normalizedHoles = holes.clone();
			Holes.normalize(normalizedHoles);
			long longMatchupIndex = 0;
			for (final Hole normalizedHole : normalizedHoles) {
				longMatchupIndex = longMatchupIndex * Constants.HOLE_COUNT + normalizedHole.getIndex();
			}
			final int matchupIndex = (int) (Integer.MIN_VALUE + longMatchupIndex);
			if (!seen.contains(matchupIndex)) {
				seen.add(matchupIndex);
				final long pos = raf.getFilePointer();
				if (pos + RECORD_SIZE > length) {
					final int[] odds = calculator.calculateOdds(normalizedHoles);
					raf.writeInt(matchupIndex);
					for (final int odd : odds) {
						raf.writeInt(odd);
					}
					logger.info("{}: {}", Arrays.toString(normalizedHoles), Arrays.toString(odds));
				} else {
					raf.seek(pos + RECORD_SIZE);
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
		if (args.length != 2) {
			System.err.println("usage: " + HoldemThreePlayerPreflopOddsDatabaseGenerator.class.getName() + " <outpuFileName> <handValueDBFileName>");
			System.exit(1);
		}
		final HoldemThreePlayerPreflopOddsDatabaseGenerator generator = new HoldemThreePlayerPreflopOddsDatabaseGenerator(args[1], args[0]);
		generator.generate();
	}

}
