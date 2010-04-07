package com.sebster.poker.holdem.odds;

import java.util.EnumSet;

import com.sebster.poker.Card;
import com.sebster.poker.Combination;
import com.sebster.poker.holdem.Hole;
import com.sebster.poker.odds.BasicOdds;
import com.sebster.poker.odds.Odds;

public class PostFlopOddsCalculator {

	private static final PostFlopOddsCalculator INSTANCE = new PostFlopOddsCalculator();

	public static PostFlopOddsCalculator getInstance() {
		return INSTANCE;
	}

	private PostFlopOddsCalculator() {
		// Singleton.
	}

	public final Odds[] calculateOdds(final Hole[] holes, final Card[] board) {

		final int numHoles = holes.length;
		if (numHoles < 2 || numHoles > 10) {
			throw new IllegalArgumentException("number of holes must be between 2 and 10");
		}
		if (board.length < 3 || board.length > 5) {
			throw new IllegalArgumentException("board must consist of exactly 3-5 cards");
		}

		final EnumSet<Card> deck = EnumSet.allOf(Card.class);
		for (int i = 0; i < numHoles; i++) {
			if (!deck.remove(holes[i].getFirst())) {
				throw new IllegalArgumentException("duplicate card " + holes[i].getFirst() + " in hole " + holes[i]);
			}
			if (!deck.remove(holes[i].getSecond())) {
				throw new IllegalArgumentException("duplicate card " + holes[i].getSecond() + " in hole " + holes[i]);
			}
		}
		for (int i = 0; i < board.length; i++) {
			if (!deck.remove(board[i])) {
				throw new IllegalArgumentException("duplicate card " + board[i] + " the on board");
			}
		}

		final Card[] cards = new Card[7];
		System.arraycopy(board, 0, cards, 0, board.length);
		final int[][] result = new int[numHoles][numHoles + 1];
		calculateOdds(holes, cards, deck, board.length, result);
		final Odds[] odds = new Odds[numHoles];
		for (int i = 0; i < numHoles; i++) {
			odds[i] = new BasicOdds(result[i]);
		}
		return odds;
	}

	private void calculateOdds(final Hole[] holes, final Card[] cards, final EnumSet<Card> deck, final int length, final int[][] result) {
		final int numHoles = holes.length;
		if (length == 5) {
			int[] handValues = new int[numHoles];
			int max = -1;
			int count = 0;
			for (int i = 0; i < numHoles; i++) {
				cards[5] = holes[i].getFirst();
				cards[6] = holes[i].getSecond();
				handValues[i] = Combination.getHandValue(cards);
				if (handValues[i] > max) {
					max = handValues[i];
					count = 1;
				} else if (handValues[i] == max) {
					count++;
				}
			}
			for (int i = 0; i < numHoles; i++) {
				result[i][handValues[i] == max ? count : 0]++;
			}
			return;
		}

		for (final Card card : deck) {
			cards[length] = card;
			deck.remove(card);
			calculateOdds(holes, cards, deck, length + 1, result);
			deck.add(card);
		}
	}

}
