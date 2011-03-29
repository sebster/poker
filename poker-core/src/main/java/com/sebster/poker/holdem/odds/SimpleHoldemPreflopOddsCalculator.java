package com.sebster.poker.holdem.odds;

import java.util.EnumSet;

import com.sebster.poker.Card;
import com.sebster.poker.CardSet;
import com.sebster.poker.Combination;
import com.sebster.poker.Hole;
import com.sebster.poker.odds.BasicOdds;
import com.sebster.poker.odds.Odds;
import com.sebster.util.ArrayUtils;

public class SimpleHoldemPreflopOddsCalculator implements HoldemPreflopOddsCalculator {

	@Override
	public Odds[] calculateOdds(final Hole... holes) {
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

}
