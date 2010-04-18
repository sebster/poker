package com.sebster.poker.holdem.odds;

import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.TreeSet;

import com.sebster.poker.Card;
import com.sebster.poker.CardSet;
import com.sebster.poker.Combination;
import com.sebster.poker.Hole;

public class Nuts {

	private Nuts() {
		// Static utility class.
	}

	/**
	 * Get the possible hands, given the board and the available cards, mapped
	 * by the number of hands they lose to. So the set for the first key will be
	 * the nut hands, for the second key the second nut hands, etc.
	 * 
	 * @param board
	 * @param available
	 * @return
	 */
	public static SortedMap<Integer, Set<Hole>> getBestHands(final Card[] board, final Card[] available) {
		final long l1 = System.currentTimeMillis();
		final SortedMap<Integer, Set<Hole>> result = new TreeMap<Integer, Set<Hole>>(Collections.reverseOrder());
		final Card[] cards = new Card[board.length + 2];
		System.arraycopy(board, 0, cards, 2, board.length);
		for (int i = 0; i < available.length; i++) {
			cards[0] = available[i];
			for (int j = i + 1; j < available.length; j++) {
				cards[1] = available[j];
				int value1 = Combination.getHandValue(CardSet.fromCards(cards));
//				int count = value1;
//				int count = 0;
//				for (int k = 0; k < available.length; k++) {
//					if (k == i || k == j) {
//						continue;
//					}
//					cards[0] = available[k];
//					for (int l = k + 1; l < available.length; l++) {
//						if (l == i || l == j) {
//							continue;
//						}
//						cards[1] = available[l];
//						int value2 = Combination.getHandValue(cards);
//						if (value2 > value1) {
//							count++;
//						}
//					}
//				}
				Set<Hole> holes = result.get(value1);
				if (holes == null) {
					holes = new TreeSet<Hole>();
					result.put(value1, holes);
				}
				holes.add(new Hole(available[i], available[j]));
			}
		}
		long l2 = System.currentTimeMillis();
		System.out.println("Search took: " + (l2 - l1) + " ms");
		return result;
	}

	public static void main(String[] args) {
		// Deck deck = new Deck();
		Card[] board = new Card[5];
		board[0] = Card.byName("Jd");
		board[1] = Card.byName("3c");
		board[2] = Card.byName("Ks");
		board[3] = Card.byName("Jc");
		board[4] = Card.byName("5h");
		// for (int i = 0; i < 5; i++) {
		// board[i] = deck.draw();
		// }
		EnumSet<Card> cards = EnumSet.allOf(Card.class);
		for (int i = 0; i < 5; i++) {
			cards.remove(board[i]);
		}
		Card[] available = (Card[]) cards.toArray(new Card[0]);
		// Card[] available = new Card[deck.getRemaining()];
		// int i = 0;
		// while (deck.hasRemaining()) {
		// available[i++] = deck.draw();
		// }
		Arrays.sort(available);
		System.out.println(Arrays.toString(board));
		System.out.println(Arrays.toString(available));
		System.out.println(getBestHands(board, available));
	}
}
