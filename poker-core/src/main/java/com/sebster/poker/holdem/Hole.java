package com.sebster.poker.holdem;

import com.sebster.poker.Card;
import com.sebster.poker.Deck;
import com.sebster.poker.Rank;
import com.sebster.poker.Suit;
import com.sebster.poker.odds.Constants;
import com.sebster.util.LinearOrder;

public final class Hole implements LinearOrder<Hole> {

	/**
	 * The first (and highest) card in this hole.
	 */
	private final Card first;

	/**
	 * The second (and lowest) card in this hole.
	 */
	private final Card second;

	public Hole(final Card first, final Card second) {
		if (first == second) {
			throw new IllegalArgumentException("hole cards must be different");
		}
		if (first.compareTo(second) < 0) {
			this.first = first;
			this.second = second;
		} else {
			this.first = second;
			this.second = first;
		}
	}

	/**
	 * Get the first (and highest) card in this hole.
	 * 
	 * @return the first (and highest) card in this hole
	 */
	public Card getFirst() {
		return first;
	}

	/**
	 * Get the second (and lowest) card in this hole.
	 * 
	 * @return the second (and lowest) card in this hole
	 */
	public Card getSecond() {
		return second;
	}

	public Card getCard(int index) {
		switch (index) {
		case 0: return first;
		case 1: return second;
		}
		throw new IllegalArgumentException("index must be 0 or 1");
	}
	
	/**
	 * Return the index of this hole in the list of all holes.
	 * 
	 * @return the index of this hole in the list of all holes
	 */
	public int getIndex() {
		final int a = first.ordinal(), b = second.ordinal();
		return Constants.HOLE_COUNT - ((52 - a) * (51 - a)) / 2 + (b - a - 1);
	}

	/*
	 * To find the right first value, we must solve:
	 * 
	 * <pre> a = max n : sum(k : 1..n : N - k) &lt; i </pre>
	 * 
	 * When solved this leads to:
	 * 
	 * <pre> a = floor(N - 1/2 - sqrt(N^2 - N - 1/4 - 2i)) </pre>
	 */
	public static Hole fromIndex(int index) {
		if (index < 0 || index >= Constants.HOLE_COUNT) {
			throw new IllegalArgumentException("invalid hole index");
		}
		final int a = (int) Math.floor(51.5 - Math.sqrt(2652.25 - 2 * index));
		final int b = index + (52 - a) * (51 - a) / 2 + a + 1 - Constants.HOLE_COUNT;
		return new Hole(Card.values()[a], Card.values()[b]);
	}

	public static Hole first() {
		return new Hole(Card.first(), Card.first().next());
	}

	public static Hole last() {
		return new Hole(Card.last().prev(), Card.last());
	}

	public Hole next() {
		final Card secondNext = second.next();
		if (secondNext != null) {
			return new Hole(first, secondNext);
		}
		final Card firstNext = first.next();
		if (firstNext != Card.last()) {
			return new Hole(firstNext, firstNext.next());
		}
		return null;
	}

	public Hole prev() {
		final Card secondPrev = second.prev();
		if (secondPrev != first) {
			return new Hole(first, secondPrev);
		}
		final Card firstPrev = first.prev();
		if (firstPrev != null) {
			return new Hole(firstPrev, Card.last());
		}
		return null;
	}

	/**
	 * Return whether or not the hole cards are suited.
	 * 
	 * @return true if the hole cards have the same suit, false otherwise
	 */
	public boolean isSuited() {
		return first.getSuit() == second.getSuit();
	}

	/**
	 * Return whether or not the hole cards are a pair.
	 * 
	 * @return true if the hole cards have the same rank, false otherwise
	 */
	public boolean isPair() {
		return first.getRank() == second.getRank();
	}

	public boolean contains(final Card card) {
		return first == card || second == card;
	}

	public boolean intersects(final Hole hole) {
		return hole.contains(first) || hole.contains(second);
	}

	/**
	 * Get the hole category of the hole.
	 * 
	 * @return the hole category of the hole
	 */
	public HoleCategory getHoleCategory() {
		return HoleCategory.byDescription(first.getRank(), second.getRank(), isSuited());
	}

	/**
	 * Return the string representation of the hole. This consists of the short
	 * names of the two hole cards separated by a comma, e.g., "Ac,Td".
	 * 
	 * @return the string representation of the hole
	 */
	@Override
	public String toString() {
		return first.getShortName() + "," + second.getShortName();
	}

	/**
	 * Return a hole from its string representation. The string representation
	 * must consist of the short names of the two cards, separated by a comma,
	 * e.g. "Ac,Td".
	 * 
	 * @param string
	 *            the string representation of the hole
	 * @return the corresponding hole
	 */
	public static Hole fromString(final String string) {
		if (string == null) {
			throw new NullPointerException("string");
		}
		if (string.length() != 5 || string.charAt(2) != ',') {
			throw new IllegalArgumentException("invalid hole: " + string);
		}
		return new Hole(Card.byName(string.substring(0, 2)), Card.byName(string.substring(3, 5)));
	}

	/**
	 * Get an example hole by its category. The first hole card will be a club,
	 * the second will a club or diamond.
	 * 
	 * @param holeCategory
	 *            the hole category
	 * 
	 * @return the example hole for the specified hole category
	 */
	public static Hole fromHoleCategory(final HoleCategory holeCategory) {
		return new Hole(Card.byRankAndSuit(holeCategory.getHighRank(), Suit.CLUBS), Card.byRankAndSuit(holeCategory.getLowRank(), holeCategory.isSuited() ? Suit.CLUBS : Suit.DIAMONDS));
	}

	public static Hole[] allFromHoleCategory(final HoleCategory holeCategory) {
		final Rank high = holeCategory.getHighRank(), low = holeCategory.getLowRank();
		if (holeCategory.isSuited()) {
			final Hole[] holes = new Hole[4];
			for (int i = 0; i < 4; i++) {
				holes[i] = new Hole(Card.byRankAndSuit(high, Suit.byValue(i)), Card.byRankAndSuit(low, Suit.byValue(i)));
			}
			return holes;
		}
		if (holeCategory.isPair()) {
			final Hole[] holes = new Hole[6];
			int k = 0;
			for (int i = 0; i < 4; i++) {
				for (int j = i + 1; j < 4; j++) {
					holes[k++] = new Hole(Card.byRankAndSuit(high, Suit.byValue(i)), Card.byRankAndSuit(high, Suit.byValue(j)));
				}
			}
			return holes;

		}
		final Hole[] holes = new Hole[12];
		int k = 0;
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				if (i != j) {
					holes[k++] = new Hole(Card.byRankAndSuit(high, Suit.byValue(i)), Card.byRankAndSuit(low, Suit.byValue(j)));
				}
			}
		}
		return holes;
	}

	/**
	 * Draw a hole from the given deck.
	 * 
	 * @param deck
	 *            the deck to draw from
	 * @return the hole drawn from the deck.
	 * @throws {@link IllegalStateException} if the deck does not have at least
	 *         two cards remaining
	 */
	public static Hole fromDeck(final Deck deck) {
		return new Hole(deck.draw(), deck.draw());
	}

	@Override
	public int hashCode() {
		final int prime = 87251;
		int result = 1;
		result = prime * result + first.hashCode();
		result = prime * result + second.hashCode();
		return result;
	}

	@Override
	public boolean equals(final Object object) {
		if (object instanceof Hole) {
			final Hole other = (Hole) object;
			return first == other.first && second == other.second;
		}
		return false;
	}

	@Override
	public int compareTo(final Hole other) {
		final int i = first.compareTo(other.first);
		if (i != 0) {
			return i;
		}
		return second.compareTo(other.second);
	}

}
