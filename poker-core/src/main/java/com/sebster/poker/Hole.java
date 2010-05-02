package com.sebster.poker;

import java.util.Collection;

import net.jcip.annotations.Immutable;

@Immutable
public final class Hole extends CardSet {

	private Hole(final Card first, final Card second) {
		super(new Card[] { first, second });
	}

	@Override
	public int size() {
		return 2;
	}
	
	@Override
	public Hole next() {
		// TODO more efficient implementation
		return (Hole) super.next();
	}

	@Override
	public Hole prev() {
		// TODO more efficient implementation
		return (Hole) super.prev();
	}

	@Override
	public boolean contains(final Object card) {
		// More efficient than superclass.
		return cards[0] == card || cards[1] == card;
	}

	/**
	 * Return whether or not the hole cards are suited.
	 * 
	 * @return true if the hole cards have the same suit, false otherwise
	 */
	public boolean isSuited() {
		return cards[0].getSuit() == cards[1].getSuit();
	}

	/**
	 * Return whether or not the hole cards are a pair.
	 * 
	 * @return true if the hole cards have the same rank, false otherwise
	 */
	public boolean isPair() {
		return cards[0].getRank() == cards[1].getRank();
	}

	/**
	 * Get the hole category of the hole.
	 * 
	 * @return the hole category of the hole
	 */
	public HoleCategory getHoleCategory() {
		return HoleCategory.byDescription(cards[0].getRank(), cards[1].getRank(), isSuited());
	}

	@Override
	public boolean equals(final Object object) {
		// Only one instance of each hole.
		return this == object;
	}

	public static Hole firstHole() {
		return (Hole) CardSet.firstSet(2);
	}

	public static Hole lastHole() {
		return (Hole) CardSet.lastSet(2);
	}

	public static Hole fromCards(final Card first, final Card second) {
		return (Hole) CardSet.fromCards(first, second);
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
	 * must consist of the short names of the two cards, separated by a comma,
	 * e.g. "Ac,Td".
	 * 
	 * @param string
	 *            the string representation of the hole
	 * @return the corresponding hole
	 */
	public static Hole fromString(final String string) {
		final CardSet cardSet = CardSet.fromString(string);
		if (cardSet.size() == 2) {
			return (Hole) cardSet;
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
	 *         two cards remaining
	 */
	public static Hole fromDeck(final Deck deck) {
		return (Hole) CardSet.fromDeck(deck, 2);
	}

	public static Hole fromIndex(final int index) {
		return (Hole) CardSet.fromIndex(index, 2);
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
		return getInstance(Card.byRankAndSuit(holeCategory.getHighRank(), Suit.CLUBS), Card.byRankAndSuit(holeCategory.getLowRank(), holeCategory.isSuited() ? Suit.CLUBS : Suit.DIAMONDS));
	}

	public static Hole[] allFromHoleCategory(final HoleCategory holeCategory) {
		final Rank high = holeCategory.getHighRank(), low = holeCategory.getLowRank();
		if (holeCategory.isSuited()) {
			final Hole[] holes = new Hole[4];
			for (int i = 0; i < 4; i++) {
				holes[i] = getInstance(Card.byRankAndSuit(high, Suit.byValue(i)), Card.byRankAndSuit(low, Suit.byValue(i)));
			}
			return holes;
		}
		if (holeCategory.isPair()) {
			final Hole[] holes = new Hole[6];
			int k = 0;
			for (int i = 0; i < 4; i++) {
				for (int j = i + 1; j < 4; j++) {
					holes[k++] = getInstance(Card.byRankAndSuit(high, Suit.byValue(i)), Card.byRankAndSuit(high, Suit.byValue(j)));
				}
			}
			return holes;

		}
		final Hole[] holes = new Hole[12];
		int k = 0;
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				if (i != j) {
					holes[k++] = getInstance(Card.byRankAndSuit(high, Suit.byValue(i)), Card.byRankAndSuit(low, Suit.byValue(j)));
				}
			}
		}
		return holes;
	}

	/**
	 * Get the instance for the specified first and second cards.
	 * 
	 * @param first
	 *            the lowest card of the hole
	 * @param second
	 *            the highest card of the hole
	 * @return the hole for the specified cards
	 * @throws NullPointerException
	 *             if either of the cards are null
	 * @throws ArrayIndexOutOfBoundsException
	 *             if the first card is not smaller than; the second card
	 */
	static Hole getInstance(final Card first, final Card second) {
		// Return existing instance.
		final int i = first.ordinal(), j = second.ordinal() - i - 1;
		return HOLES[i][j];
	}

	/**
	 * The Hole class is instance controlled, we ensure that there is only one
	 * instance of each distinct Hole. The HOLES array contains all instances.
	 */
	private static final Hole[][] HOLES;

	static {
		// Generate all the 2 card holes.
		HOLES = new Hole[52][0];
		for (int i = 0; i < 52; i++) {
			HOLES[i] = new Hole[51 - i];
			for (int j = i + 1; j < 52; j++) {
				HOLES[i][j - i - 1] = new Hole(Card.values()[i], Card.values()[j]);
			}
		}
	}

}
