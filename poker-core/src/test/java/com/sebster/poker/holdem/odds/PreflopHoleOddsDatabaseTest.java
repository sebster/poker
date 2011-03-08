package com.sebster.poker.holdem.odds;

import org.junit.Assert;
import org.junit.Test;

import com.sebster.poker.Hole;
import com.sebster.poker.odds.Odds;

public class PreflopHoleOddsDatabaseTest {

	@Test
	public void testAllHands() {
		Hole hole1 = Hole.firstHole();
		while (hole1 != null) {
			Hole hole2 = Hole.firstHole();
			while (hole2 != null) {
				if (!hole1.intersects(hole2)) {
					final Odds[] odds = PreflopHoleOddsDatabase.getTwoPlayerDatabase().getOdds(hole1, hole2);
					final Odds oldOdds1 = TwoPlayerPreFlopOddsDB.getInstance().getOdds(hole1, hole2);
					Assert.assertEquals(oldOdds1, odds[0]);
					final Odds oldOdds2 = TwoPlayerPreFlopOddsDB.getInstance().getOdds(hole2, hole1);
					Assert.assertEquals(oldOdds2, odds[1]);
				}
				hole2 = hole2.next();
			}
			hole1 = hole1.next();
		}
	}

}
