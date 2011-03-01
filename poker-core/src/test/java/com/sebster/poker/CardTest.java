package com.sebster.poker;

import org.junit.Assert;
import org.junit.Test;

public class CardTest {

	@Test
	public void testGetByRankAndSuit() {
		for (final Rank rank : Rank.values()) {
			for (final Suit suit : Suit.values()) {
				Assert.assertEquals(rank, Card.byRankAndSuit(rank, suit).getRank());
				Assert.assertEquals(suit, Card.byRankAndSuit(rank, suit).getSuit());
			}
		}
	}
}
