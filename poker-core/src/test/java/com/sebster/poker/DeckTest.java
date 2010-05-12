package com.sebster.poker;

import java.util.Arrays;
import java.util.Random;

import org.junit.Assert;
import org.junit.Test;

public class DeckTest {

	@Test
	public void testDeck() {
		final Deck deck = new Deck(new Random(0));
		for (int i = 0; i < 10; i++) {
			final Card[] cards = new Card[52];
			int j = 0;
			while (deck.hasRemaining()) {
				Assert.assertTrue(j < 52);
				cards[j++] = deck.draw();
				Assert.assertEquals(52 - j, deck.getNumberRemaining());
			}
			Assert.assertEquals(52, j);
			Arrays.sort(cards);
			Assert.assertArrayEquals(Card.values(), cards);
			deck.shuffle();
		}
	}

}
