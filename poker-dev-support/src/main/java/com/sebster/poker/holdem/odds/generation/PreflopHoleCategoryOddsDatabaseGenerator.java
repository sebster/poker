package com.sebster.poker.holdem.odds.generation;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sebster.poker.Hole;
import com.sebster.poker.HoleCategory;
import com.sebster.poker.holdem.odds.PreflopHoleOddsDatabase;
import com.sebster.poker.odds.MutableOdds;
import com.sebster.poker.odds.Odds;

public class PreflopHoleCategoryOddsDatabaseGenerator {

	private static final Logger logger = LoggerFactory.getLogger(PreflopHoleCategoryOddsDatabaseGenerator.class);

	private final PreflopHoleOddsDatabase oddsDatabase;

	private final int players;

	private final String outputFileName;

	public PreflopHoleCategoryOddsDatabaseGenerator(final String outputFileName, final int players) throws IOException {
		this.oddsDatabase = PreflopHoleOddsDatabase.getDatabase(players);
		this.players = players;
		this.outputFileName = outputFileName;
	}

	public void generate() throws FileNotFoundException, IOException {
		logger.info("generating {} player preflop hole category odds database to output file {}", players, outputFileName);
		final long t1 = System.currentTimeMillis();
		final RandomAccessFile raf = new RandomAccessFile(new File(outputFileName), "rw");
		raf.writeByte(players);
		raf.writeInt(0);
		final int records = iterateHoleCategories(raf, 0, new HoleCategory[players]);
		raf.seek(1);
		raf.writeInt(records);
		raf.close();
		final long t2 = System.currentTimeMillis();
		logger.info("generation complete: {} records, {} seconds", records, (t2 - t1) / 1000);
	}

	private int iterateHoleCategories(final RandomAccessFile raf, final int index, final HoleCategory[] holeCategories) throws IOException {
		if (index == players) {
			final Hole[] holes = new Hole[players];
			final MutableOdds[] odds = new MutableOdds[players];
			for (int i = 0; i < odds.length; i++) {
				odds[i] = new MutableOdds(players);
			}
			iterateHoles(odds, holeCategories, 0, holes);
			for (int h = 0; h < holes.length; h++) {
				raf.writeByte(holeCategories[h].ordinal());
			}
			for (int h = 0; h < holeCategories.length; h++) {
				for (int n = 0; n <= odds[h].getMaxN(); n++) {
					raf.writeInt(odds[h].getNWaySplits(n));
				}
			}
			logger.info("{}: {}", Arrays.toString(holeCategories), Arrays.toString(odds));
			return 1;
		} else {
			HoleCategory holeCategory = index == 0 ? HoleCategory.first() : holeCategories[index - 1];
			int count = 0;
			while (holeCategory != null) {
				holeCategories[index] = holeCategory;
				count += iterateHoleCategories(raf, index + 1, holeCategories);
				holeCategory = holeCategory.next();
			}
			return count;
		}
	}

	private void iterateHoles(final MutableOdds[] totalOdds, final HoleCategory[] holeCategories, final int index, final Hole[] holes) {
		if (index == holes.length) {
			final Odds[] odds = oddsDatabase.getOdds(holes);
			for (int i = 0; i < odds.length; i++) {
				totalOdds[i].addOdds(odds[i]);
			}
		} else {
			for (Hole hole : Hole.allFromHoleCategory(holeCategories[index])) {
				boolean ok = true;
				for (int i = 0; ok && i < index; i++) {
					ok = ok && !hole.intersects(holes[i]);
				}
				if (ok) {
					holes[index] = hole;
					iterateHoles(totalOdds, holeCategories, index + 1, holes);
				}
				hole = hole.next();
			}

		}
	}

	public static void main(final String[] args) throws IOException {
		if (args.length != 2) {
			System.err.println("usage: " + PreflopHoleCategoryOddsDatabaseGenerator.class.getName() + " <players> <outpuFileName>");
			System.exit(1);
		}
		final PreflopHoleCategoryOddsDatabaseGenerator generator = new PreflopHoleCategoryOddsDatabaseGenerator(args[1], Integer.parseInt(args[0]));
		generator.generate();
	}

}
