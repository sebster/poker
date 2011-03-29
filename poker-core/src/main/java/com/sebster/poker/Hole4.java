package com.sebster.poker;

import java.util.Collection;

public final class Hole4 extends CardSet {

	private static final long serialVersionUID = 277473471732391645L;

	private static final int[][] TWO_CARD_COMBINATIONS = { { 0, 1 }, { 0, 2 }, { 0, 3 }, { 1, 2 }, { 1, 3 }, { 2, 3 } };

	private Hole4(final Card cards[]) {
		super(cards);
	}

	@Override
	public int size() {
		return 4;
	}

	@Override
	public Hole4 next() {
		return (Hole4) super.next();
	}

	@Override
	public Hole4 prev() {
		return (Hole4) super.prev();
	}

	public Hole[] getAll2CardHoles() {
		final Hole[] holes = new Hole[6];
		for (int i = 0; i < 6; i++) {
			holes[i] = Hole.fromCards(cards[TWO_CARD_COMBINATIONS[i][0]], cards[TWO_CARD_COMBINATIONS[i][1]]);
		}
		return holes;
	}

	public static Hole4 firstHole4() {
		return (Hole4) CardSet.firstSet(4);
	}

	public static Hole4 lastHole4() {
		return (Hole4) CardSet.lastSet(4);
	}

	public static Hole4 fromCards(final Card first, final Card second, final Card third, final Card fourth) {
		return (Hole4) CardSet.fromCards(first, second, third, fourth);
	}

	public static Hole fromCards(final Collection<Card> cards) {
		final CardSet cardSet = CardSet.fromCards(cards);
		if (cardSet.size() == 2) {
			return (Hole) cardSet;
		}
		throw new IllegalArgumentException("invalid number of cards: " + cards.size());
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
		final CardSet cardSet = CardSet.fromString(string);
		if (cardSet.size() == 4) {
			return (Hole4) cardSet;
		}
		throw new IllegalArgumentException("invalid number of cards: " + cardSet.size());
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
		return (Hole4) CardSet.fromDeck(deck, 4);
	}

	public static Hole4 fromIndex(final int index) {
		return (Hole4) CardSet.fromIndex(index, 4);
	}

	/**
	 * Get the instance for the specified cards.
	 * 
	 * @param cards
	 *            the cards for this hole4
	 */
	static Hole4 getInstance(final Card[] cards) {
		return new Hole4(cards);
	}

}
