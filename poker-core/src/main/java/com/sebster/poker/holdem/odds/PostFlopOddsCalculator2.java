package com.sebster.poker.holdem.odds;

import java.util.EnumSet;

import com.sebster.poker.Card;
import com.sebster.poker.CardSet;
import com.sebster.poker.Combination;
import com.sebster.poker.Hole;
import com.sebster.poker.odds.BasicOdds;
import com.sebster.poker.odds.Constants;
import com.sebster.poker.odds.Odds;

public class PostFlopOddsCalculator2 {

	private static final PostFlopOddsCalculator2 INSTANCE = new PostFlopOddsCalculator2();

	private final int[] HAND_VALUES = new int[Constants.BOARD_COUNT_52];
	
	private final int[][] COMBINATIONS = {
			{ 2, 3, 4, 5, 6}, {1, 3, 4, 5, 6}, {1, 2, 4, 5, 6}, {1, 2, 3, 5, 6}, {1, 2, 3, 4, 6}, {1, 2, 3, 4, 5},
			{ 0, 3, 4, 5, 6}, {0, 2, 4, 5, 6}, {0, 2, 3, 5, 6}, {0, 2, 3, 4, 6}, {0, 2, 3, 4, 5},
			{ 0, 1, 4, 5, 6}, {0, 1, 3, 5, 6}, {0, 1, 3, 4, 6}, {0, 1, 3, 4, 5},
			{ 0, 1, 2, 5, 6}, {0, 1, 2, 4, 6}, {0, 1, 2, 4, 5},
			{ 0, 1, 2, 3, 6}, {0, 1, 2, 3, 5},
			{ 0, 1, 2, 3, 4},
	};
	
	public static PostFlopOddsCalculator2 getInstance() {
		return INSTANCE;
	}

	private PostFlopOddsCalculator2() {
		// Singleton.
		CardSet board = CardSet.firstSet(5);
		for (int i = 0; i < HAND_VALUES.length; i++) {
			HAND_VALUES[i] = Combination.getBestValue(board);
			board = board.next();
		}
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
			if (!deck.remove(holes[i].first())) {
				throw new IllegalArgumentException("duplicate card " + holes[i].first() + " in hole " + holes[i]);
			}
			if (!deck.remove(holes[i].last())) {
				throw new IllegalArgumentException("duplicate card " + holes[i].last() + " in hole " + holes[i]);
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
				cards[5] = holes[i].first();
				cards[6] = holes[i].last();
				handValues[i] = Combination.getBestValue(CardSet.fromCards(cards));
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

//	private int getHandValue(int[] hand) {
//		Arrays.sort(hand.clone());
//		for (int i = 0; i < 21; i++) {
//			for (int j = 0; j < 5; j++) {
//				
//			}
//		}
//	}
	
}
