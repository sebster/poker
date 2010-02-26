package com.sebster.poker.holdem.odds;

import org.junit.Assert;
import org.junit.Test;

import com.sebster.poker.Card;
import com.sebster.poker.holdem.Hole;

public class TwoPlayerPreflopOddsDBTest {

	@Test
	public void testAllHands() {
		for (int i = 0; i < 52; i++) {
			for (int j = 0; j < 52; j++) {
				if (i == j)
					continue;
				for (int k = 0; k < 52; k++) {
					if (i == k || j == k)
						continue;
					for (int l = 0; l < 52; l++) {
						if (i == l || j == l || k == l)
							continue;
						final Hole hole1 = new Hole(Card.values()[i], Card.values()[j]);
						final Hole hole2 = new Hole(Card.values()[k], Card.values()[l]);
						Odds odds = TwoPlayerPreFlopOddsDB.getInstance().getOdds(hole1, hole2);
						Assert.assertNotNull(odds);
					}
				}
			}
		}
	}

}
