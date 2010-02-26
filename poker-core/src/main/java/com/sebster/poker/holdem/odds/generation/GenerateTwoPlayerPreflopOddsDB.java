package com.sebster.poker.holdem.odds.generation;

import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sebster.poker.Card;
import com.sebster.poker.Combination;
import com.sebster.poker.Suit;
import com.sebster.poker.holdem.odds.Constants;
import com.sebster.poker.holdem.odds.TwoPlayerPreFlopOddsDB;

public class GenerateTwoPlayerPreflopOddsDB {

	private static final Logger logger = LoggerFactory.getLogger(GenerateTwoPlayerPreflopOddsDB.class);

	public static void generate(final String filename) throws IOException {
		logger.info("generating 2 player preflop odds database");
		final List<int[]> results = new ArrayList<int[]>();
		final Card[] cards = new Card[4];
		for (int i = 0; i < 52; i++) {
			final Card card0 = Card.values()[i];
			final Suit suit0 = card0.getSuit();
			// We can always permute the suits so that the first card is clubs.
			if (suit0 != Suit.CLUBS) {
				// We can always permute the suits so that the first card is
				// clubs.
				continue;
			}
			cards[0] = card0;

			for (int j = i + 1; j < 52; j++) {
				final Card card1 = Card.values()[j];
				// We can always permute the suits so that the second card is a
				// club or diamond.
				final Suit suit1 = card1.getSuit();
				if (suit1 != Suit.CLUBS && suit1 != Suit.DIAMONDS) {
					// If it already is, do nothing, else permute...
					continue;
				}
				cards[1] = card1;
				/*
				 * We can always make sure the second hand comes
				 * lexicographically after the first hand. Since the first card
				 * cannot be equal in both hands this means that the first card
				 * of this hand comes after the first card of the other hand.
				 */
				for (int k = i + 1; k < 52; k++) {
					if (k == i || k == j) {
						// Can't have duplicate cards.
						continue;
					}
					final Card card2 = Card.values()[k];
					final Suit suit2 = card2.getSuit();
					cards[2] = card2;

					for (int l = k + 1; l < 52; l++) {
						if (l == i || l == j) {
							// Can't have duplicate cards.
							continue;
						}
						final Card card3 = Card.values()[l];
						final Suit suit3 = card3.getSuit();
						cards[3] = card3;

						/*
						 * If there is a heart AND a spade we can always permute
						 * the suits so that the heart is first.
						 */
						if (suit2 == Suit.SPADES && suit3 == Suit.HEARTS) {
							continue;
						}
						/*
						 * If there is no heart and a spade we can always
						 * permute the suits so that the spade becomes a heart.
						 */
						if (suit2 != Suit.HEARTS && suit3 != Suit.HEARTS && (suit2 == Suit.SPADES || suit3 == Suit.SPADES)) {
							continue;
						}
						/*
						 * If there are no diamonds in the first hand, we don't
						 * need them in the second hand (because it contains at
						 * most 2 suits different from the clubs and we already
						 * have hearts and spades).
						 */
						if (suit1 == Suit.CLUBS && (suit2 == Suit.DIAMONDS || suit3 == Suit.DIAMONDS)) {
							continue;
						}
						// If the second hand is suited, we don't need spades.
						if (suit2 == suit3 && suit2 == Suit.SPADES) {
							continue;
						}
						/*
						 * If the first hand is a pair, and the second hand has
						 * diamonds but no club, we can permute the pair and
						 * switch clubs and diamonds.
						 */
						if (card0.getRank() == card1.getRank() && (suit2 != Suit.CLUBS && suit3 != Suit.CLUBS && (suit2 == Suit.DIAMONDS || suit3 == Suit.DIAMONDS))) {
							continue;
						}

						final int[] odds = calculateHoleOdds(cards[0], cards[1], cards[2], cards[3]);
						logger.debug("{} {} {} {} {} {}", new Object[] { card0, card1, card2, card3, odds[0], odds[1] });
						results.add(new int[] { (i << 24) | (j << 16) | (k << 8) | l, odds[0], odds[1] });
					}
				}
			}
		}
		logger.info("saving 2 player preflop odds database");
		final DataOutputStream dos = new DataOutputStream(new GZIPOutputStream(new FileOutputStream(filename)));
		dos.writeInt(results.size());
		for (int[] result : results) {
			for (int i : result) {
				dos.writeInt(i);
			}
		}
		dos.close();
	}

	public static int[] calculateHoleOdds(Card card0, Card card1, Card card2, Card card3) {
		final Card[] deck = new Card[48];
		int c = 0;
		for (Card card : Card.values()) {
			if (card != card0 && card != card1 && card != card2 && card != card3) {
				deck[c++] = card;
			}
		}
		final Card[] cards = new Card[7];
		int win = 0, loss = 0, total = 0;
		for (int i = 0; i < 48; i++) {
			cards[2] = deck[i];
			for (int j = i + 1; j < 48; j++) {
				cards[3] = deck[j];
				for (int k = j + 1; k < 48; k++) {
					cards[4] = deck[k];
					for (int l = k + 1; l < 48; l++) {
						cards[5] = deck[l];
						for (int m = l + 1; m < 48; m++) {
							cards[6] = deck[m];
							cards[0] = card0;
							cards[1] = card1;
							final int handValue1 = Combination.getHandValue(cards);
							cards[0] = card2;
							cards[1] = card3;
							final int handValue2 = Combination.getHandValue(cards);
							if (handValue1 > handValue2) {
								win++;
							} else if (handValue2 > handValue1) {
								loss++;
							}
							total++;
						}
					}
				}
			}
		}
		assert (total == Constants.BOARD_COUNT_2);
		return new int[] { win, loss };
	}

	public static void main(final String[] args) throws IOException {
		generate(args.length > 0 ? args[0] : TwoPlayerPreFlopOddsDB.DB_RESOURCE_NAME);
	}

}
