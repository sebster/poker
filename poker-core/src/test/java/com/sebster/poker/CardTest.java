package com.sebster.poker;

import org.junit.Assert;
import org.junit.Test;

public class CardTest {

	@Test
	public void testNext() {
		final Card[] cards = Card.values();
		for (int i = 0; i < cards.length; i++) {
			Assert.assertEquals(i + 1 < cards.length ? cards[i + 1] : null, cards[i].next());
		}
	}

	@Test
	public void testPrev() {
		final Card[] cards = Card.values();
		for (int i = 0; i < cards.length; i++) {
			Assert.assertEquals(i > 0 ? cards[i - 1] : null, cards[i].prev());
		}
	}

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
