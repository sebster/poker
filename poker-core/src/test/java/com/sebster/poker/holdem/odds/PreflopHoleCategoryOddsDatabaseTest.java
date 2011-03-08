package com.sebster.poker.holdem.odds;

import org.junit.Assert;
import org.junit.Test;

import com.sebster.poker.HoleCategory;
import com.sebster.poker.odds.Odds;

public class PreflopHoleCategoryOddsDatabaseTest {

	@Test
	public void testAllHands() {
		for (final HoleCategory holeCategory1 : HoleCategory.values()) {
			for (final HoleCategory holeCategory2 : HoleCategory.values()) {
				final Odds[] odds = PreflopHoleCategoryOddsDatabase.getTwoPlayerDatabase().getOdds(holeCategory1, holeCategory2);
				final Odds oldOdds1 = TwoPlayerPreFlopHoleCategoryOddsDB.getInstance().getOdds(holeCategory1, holeCategory2);
				Assert.assertEquals(holeCategory1 + " vs " + holeCategory2, oldOdds1, odds[0]);
				final Odds oldOdds2 = TwoPlayerPreFlopHoleCategoryOddsDB.getInstance().getOdds(holeCategory2, holeCategory1);
				Assert.assertEquals(holeCategory2 + " vs " + holeCategory1, oldOdds2, odds[1]);
			}
		}
	}

}
