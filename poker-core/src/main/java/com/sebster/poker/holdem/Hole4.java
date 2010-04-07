package com.sebster.poker.holdem;

import java.util.Arrays;

import com.sebster.poker.Card;
import com.sebster.poker.Deck;
import com.sebster.util.LinearOrder;

public final class Hole4 implements LinearOrder<Hole4> {

	private static final int[][] twoCardHoleCombinations = { { 0, 1 }, { 0, 2 }, { 0, 3 }, { 1, 2 }, { 1, 3 }, { 2, 3 } };

	/**
	 * The cards in this hole.
	 */
	private final Card[] cards = new Card[4];

	public Hole4(final Card first, final Card second, final Card third, final Card fourth) {
		this(new Card[] { first, second, third, fourth });
	}

	public Hole4(final Card[] cards) {
		if (cards.length != 4) {
			throw new IllegalArgumentException("cards must have length 4");
		}
		System.arraycopy(cards, 0, this.cards, 0, 4);
		Arrays.sort(cards);
		for (int i = 0; i < 3; i++) {
			if (cards[i] == cards[i + 1]) {
				throw new IllegalArgumentException("hole cards must be different");
			}
		}
	}

	/**
	 * Get the first (and highest) card in this hole.
	 * 
	 * @return the first (and highest) card in this hole
	 */
	public Card getFirst() {
		return cards[0];
	}

	/**
	 * Get the second card in this hole.
	 * 
	 * @return the second card in this hole
	 */
	public Card getSecond() {
		return cards[1];
	}

	/**
	 * Get the third card in this hole.
	 * 
	 * @return the third card in this hole
	 */
	public Card getThird() {
		return cards[2];
	}

	/**
	 * Get the fourth (and lowest) card in this hole.
	 * 
	 * @return the fourth (and lowest) card in this hole
	 */
	public Card getFourth() {
		return cards[3];
	}

	/**
	 * Get the card with the specified index in this hole.
	 * 
	 * @param index
	 *            the index of the card (0 to 3)
	 * @return the card with the specified index
	 */
	public Card getCard(int index) {
		return cards[index];
	}

	// FIXME implement!
	// /**
	// * Return the index of this hole in the list of all holes.
	// *
	// * @return the index of this hole in the list of all holes
	// */
	// public int getIndex() {
	// final int a = first.ordinal(), b = second.ordinal();
	// return Constants.HOLE_COUNT - ((52 - a) * (51 - a)) / 2 + (b - a - 1);
	// }

	// FIXME implement!
	// /*
	// * To find the right first value, we must solve:
	// *
	// * <pre> a = max n : sum(k : 1..n : N - k) &lt; i </pre>
	// *
	// * When solved this leads to:
	// *
	// * <pre> a = floor(N - 1/2 - sqrt(N^2 - N - 1/4 - 2i)) </pre>
	// */
	// public static Hole fromIndex(int index) {
	// if (index < 0 || index >= Constants.HOLE_COUNT) {
	// throw new IllegalArgumentException("invalid hole index");
	// }
	// final int a = (int) Math.floor(51.5 - Math.sqrt(2652.25 - 2 * index));
	// final int b = index + (52 - a) * (51 - a) / 2 + a + 1 -
	// Constants.HOLE_COUNT;
	// return new Hole(Card.values()[a], Card.values()[b]);
	// }

	public static Hole4 first() {
		final Card[] cards = new Card[4];
		cards[0] = Card.first();
		for (int i = 1; i < 4; i++) {
			cards[i] = cards[i - 1].next();
		}
		return new Hole4(cards);
	}

	public static Hole4 last() {
		final Card[] cards = new Card[4];
		cards[3] = Card.last();
		for (int i = 2; i >= 0; i--) {
			cards[i] = cards[i + 1].prev();
		}
		return new Hole4(cards);
	}

	public Hole4 next() {
		// FIXME implement!
		// final Card[] cards2 = cards.clone();
		// for (int i = 3; i >= 0; i--) {
		// if ((cards[i] = cards[i].next()) != null) {
		// }
		// final Card secondNext = second.next();
		// if (secondNext != null) {
		// return new Hole(first, secondNext);
		// }
		// final Card firstNext = first.next();
		// if (firstNext != Card.last()) {
		// return new Hole(firstNext, firstNext.next());
		// }
		return null;
	}

	public Hole4 prev() {
		// FIXME implement!
		// final Card[] cards2 = cards.clone();
		// for (int i = 3; i > 0; i--) {
		// cards2[i] = cards2[i].prev();
		// if (cards2[i - 1] != cards2[i]) {
		// return new Hole(cards2);
		// }
		// cards2[i] = Card.last();
		// }
		// if ((cards2[0] = cards2[0].prev()) != null) {
		// return new Hole(cards2);
		// }
		return null;
	}

	public boolean contains(final Card card) {
		for (int i = 0; i < 4; i++) {
			if (cards[i] == card) {
				return true;
			}
		}
		return false;
	}

	public boolean intersects(final Hole4 hole) {
		for (int i = 0; i < 4; i++) {
			if (hole.contains(cards[i])) {
				return true;
			}
		}
		return false;
	}

	public Hole[] getAll2CardHoles() {
		final Hole[] holes = new Hole[6];
		for (int i = 0; i < 6; i++) {
			holes[i] = new Hole(cards[twoCardHoleCombinations[i][0]], cards[twoCardHoleCombinations[i][1]]);
		}
		return holes;
	}

	/**
	 * Return the string representation of the hole. This consists of the short
	 * names of the four hole cards separated by a comma, e.g., "Ac,Ts,Th,3s".
	 * 
	 * @return the string representation of the hole
	 */
	@Override
	public String toString() {
		final StringBuilder buffer = new StringBuilder(cards[0].getShortName());
		for (int i = 1; i < 4; i++) {
			buffer.append(',');
			buffer.append(cards[i].getShortName());
		}
		return buffer.toString();
	}

	/**
	 * Return a hole from its string representation. The string representation
	 * must consist of the short names of the four cards, separated by a comma,
	 * e.g. "Ac,Ts,Th,3s".
	 * 
	 * @param string
	 *            the string representation of the hole
	 * @return the corresponding hole
	 */
	public static Hole4 fromString(final String string) {
		if (string == null) {
			throw new NullPointerException("string");
		}
		if (string.length() != 11 || string.charAt(2) != ',' || string.charAt(5) != ',' || string.charAt(8) != ',') {
			throw new IllegalArgumentException("invalid hole: " + string);
		}
		return new Hole4(Card.byName(string.substring(0, 2)), Card.byName(string.substring(3, 5)), Card.byName(string.substring(6, 8)), Card.byName(string.substring(9, 11)));
	}

	/**
	 * Draw a hole from the given deck.
	 * 
	 * @param deck
	 *            the deck to draw from
	 * @return the hole drawn from the deck.
	 * @throws {@link IllegalStateException} if the deck does not have at least
	 *         four cards remaining
	 */
	public static Hole4 fromDeck(final Deck deck) {
		return new Hole4(deck.draw(), deck.draw(), deck.draw(), deck.draw());
	}

	@Override
	public int hashCode() {
		final int prime = 87251;
		int result = 1;
		for (int i = 0; i < 4; i++) {
			result = prime * result + cards[i].hashCode();
		}
		return result;
	}

	@Override
	public boolean equals(final Object object) {
		if (object instanceof Hole4) {
			final Hole4 other = (Hole4) object;
			return Arrays.equals(cards, other.cards);
		}
		return false;
	}

	@Override
	public int compareTo(final Hole4 other) {
		for (int i = 0; i < 4; i++) {
			int j = cards[i].compareTo(other.cards[i]);
			if (j != 0) {
				return j;
			}
		}
		return 0;
	}

}
