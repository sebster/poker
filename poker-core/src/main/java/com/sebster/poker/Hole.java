package com.sebster.poker;

import java.util.Collection;

import net.jcip.annotations.Immutable;
import edu.umd.cs.findbugs.annotations.SuppressWarnings;

@Immutable
public final class Hole extends CardSet {

	private static final long serialVersionUID = 6042681695574660408L;

	@SuppressWarnings(value = "JCIP_FIELD_ISNT_FINAL_IN_IMMUTABLE_CLASS", justification = "cached computation")
	private transient Hole next;

	@SuppressWarnings(value = "JCIP_FIELD_ISNT_FINAL_IN_IMMUTABLE_CLASS", justification = "cached computation")
	private transient Hole prev;

	@SuppressWarnings(value = "JCIP_FIELD_ISNT_FINAL_IN_IMMUTABLE_CLASS", justification = "cached computation")
	private transient int indexPlusOne;

	private Hole(final Card first, final Card second) {
		super(new Card[] { first, second });
	}

	public Card getFirst() {
		return cards[0];
	}

	public Card getSecond() {
		return cards[1];
	}

	@Override
	public int size() {
		return 2;
	}

	@Override
	public Hole next() {
		if (next == null) {
			next = (Hole) super.next();
		}
		return next;
	}

	@Override
	public Hole prev() {
		if (prev == null) {
			prev = (Hole) super.prev();
		}
		return prev;
	}

	@Override
	public int getIndex() {
		if (indexPlusOne == 0) {
			indexPlusOne = super.getIndex() + 1;
		}
		return indexPlusOne - 1;
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

	public static Hole fromCards(final Card... cards) {
		if (cards.length == 2) {
			return (Hole) CardSet.fromCards(cards);
		}
		throw new IllegalArgumentException("invalid number of cards: " + cards.length);
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
		} else if (holeCategory.isPair()) {
			final Hole[] holes = new Hole[6];
			int k = 0;
			for (int i = 0; i < 4; i++) {
				for (int j = i + 1; j < 4; j++) {
					holes[k++] = getInstance(Card.byRankAndSuit(high, Suit.byValue(i)), Card.byRankAndSuit(high, Suit.byValue(j)));
				}
			}
			return holes;
		} else {
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
	}

	/**
	 * Get the instance for the specified first and second cards.
	 * 
	 * @param first
	 *            the first card of the hole
	 * @param second
	 *            the second card of the hole
	 * @return the hole for the specified cards
	 * @throws NullPointerException
	 *             if either of the cards are null
	 * @throws ArrayIndexOutOfBoundsException
	 *             if the first card is equal to second card
	 */
	public static Hole getInstance(final Card first, final Card second) {
		// Return existing instance.
		final int i = first.ordinal(), j = second.ordinal();
		return i < j ? HOLES[i][j - i - 1] : HOLES[j][i - j - 1];
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
