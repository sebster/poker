package com.sebster.poker.util;

import com.sebster.poker.Card;
import com.sebster.poker.Suit;

public class Utils {

	private Utils() {
		// Utility class.
	}

	public static void permuteSuits(final Card[] cards, final Suit suit1, final Suit suit2) {
		for (int i = 0; i < cards.length; i++) {
			final Card card = cards[i];
			final Suit suit = card.getSuit();
			if (suit == suit1) {
				cards[i] = Card.byRankAndSuit(card.getRank(), suit2);
			} else if (suit == suit2) {
				cards[i] = Card.byRankAndSuit(card.getRank(), suit1);
			}
		}
	}

}
