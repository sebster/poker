package com.sebster.poker.holdem.odds.generation;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import com.sebster.poker.Hole;
import com.sebster.poker.HoleCategory;
import com.sebster.poker.holdem.odds.TwoPlayerPreFlopHoleCategoryOddsDB;
import com.sebster.poker.holdem.odds.TwoPlayerPreFlopOddsDB;
import com.sebster.poker.odds.Constants;
import com.sebster.poker.odds.Odds;

public class GenerateTwoPlayerPreflopHoleCategoryOddsDB {

	public static void generate(final String filename, final TwoPlayerPreFlopOddsDB oddsDb) throws IOException {
		final DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(filename)));
		for (int i = 0; i < Constants.HOLE_CATEGORY_COUNT; i++) {
			final HoleCategory holeCategory1 = HoleCategory.values()[i];
			final Hole[] holes1 = Hole.allFromHoleCategory(holeCategory1);
			for (int j = 0; j <= i; j++) {
				final HoleCategory holeCategory2 = HoleCategory.values()[j];
				final Hole[] holes2 = Hole.allFromHoleCategory(holeCategory2);
				int wins = 0, losses = 0, splits = 0, count = 0;
				for (final Hole hole1 : holes1) {
					for (final Hole hole2 : holes2) {
						if (!hole1.intersects(hole2)) {
							final Odds odds = oddsDb.getOdds(hole1, hole2);
							wins += odds.getWins();
							losses += odds.getLosses();
							splits += odds.getSplits();
							count++;
						}
					}
				}
				dos.writeInt(wins);
				dos.writeInt(losses);
				dos.writeInt(splits);
				assert(wins + losses + splits == count * Constants.BOARD_COUNT_48);
			}
		}
		dos.close();
	}

	public static void main(final String[] args) throws IOException {
		String outputFilename = TwoPlayerPreFlopHoleCategoryOddsDB.DB_RESOURCE_NAME;
		if (args.length > 0) {
			outputFilename = args[0];
		}
		generate(outputFilename, TwoPlayerPreFlopOddsDB.getInstance());
	}

}
