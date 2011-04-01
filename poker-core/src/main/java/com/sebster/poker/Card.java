package com.sebster.poker;

import com.sebster.util.LinearOrder;

/**
 * Enumeration of the cards.
 * 
 * @author sebster
 */
public enum Card implements LinearOrder<Card> {

	TWO_CLUBS(Rank.TWO, Suit.CLUBS),
	TWO_DIAMONDS(Rank.TWO, Suit.DIAMONDS),
	TWO_HEARTS(Rank.TWO, Suit.HEARTS),
	TWO_SPADES(Rank.TWO, Suit.SPADES),
	THREE_CLUBS(Rank.THREE, Suit.CLUBS),
	THREE_DIAMONDS(Rank.THREE, Suit.DIAMONDS),
	THREE_HEARTS(Rank.THREE, Suit.HEARTS),
	THREE_SPADES(Rank.THREE, Suit.SPADES),
	FOUR_CLUBS(Rank.FOUR, Suit.CLUBS),
	FOUR_DIAMONDS(Rank.FOUR, Suit.DIAMONDS),
	FOUR_HEARTS(Rank.FOUR, Suit.HEARTS),
	FOUR_SPADES(Rank.FOUR, Suit.SPADES),
	FIVE_CLUBS(Rank.FIVE, Suit.CLUBS),
	FIVE_DIAMONDS(Rank.FIVE, Suit.DIAMONDS),
	FIVE_HEARTS(Rank.FIVE, Suit.HEARTS),
	FIVE_SPADES(Rank.FIVE, Suit.SPADES),
	SIX_CLUBS(Rank.SIX, Suit.CLUBS),
	SIX_DIAMONDS(Rank.SIX, Suit.DIAMONDS),
	SIX_HEARTS(Rank.SIX, Suit.HEARTS),
	SIX_SPADES(Rank.SIX, Suit.SPADES),
	SEVEN_CLUBS(Rank.SEVEN, Suit.CLUBS),
	SEVEN_DIAMONDS(Rank.SEVEN, Suit.DIAMONDS),
	SEVEN_HEARTS(Rank.SEVEN, Suit.HEARTS),
	SEVEN_SPADES(Rank.SEVEN, Suit.SPADES),
	EIGHT_CLUBS(Rank.EIGHT, Suit.CLUBS),
	EIGHT_DIAMONDS(Rank.EIGHT, Suit.DIAMONDS),
	EIGHT_HEARTS(Rank.EIGHT, Suit.HEARTS),
	EIGHT_SPADES(Rank.EIGHT, Suit.SPADES),
	NINE_CLUBS(Rank.NINE, Suit.CLUBS),
	NINE_DIAMONDS(Rank.NINE, Suit.DIAMONDS),
	NINE_HEARTS(Rank.NINE, Suit.HEARTS),
	NINE_SPADES(Rank.NINE, Suit.SPADES),
	TEN_CLUBS(Rank.TEN, Suit.CLUBS),
	TEN_DIAMONDS(Rank.TEN, Suit.DIAMONDS),
	TEN_HEARTS(Rank.TEN, Suit.HEARTS),
	TEN_SPADES(Rank.TEN, Suit.SPADES),
	JACK_CLUBS(Rank.JACK, Suit.CLUBS),
	JACK_DIAMONDS(Rank.JACK, Suit.DIAMONDS),
	JACK_HEARTS(Rank.JACK, Suit.HEARTS),
	JACK_SPADES(Rank.JACK, Suit.SPADES),
	QUEEN_CLUBS(Rank.QUEEN, Suit.CLUBS),
	QUEEN_DIAMONDS(Rank.QUEEN, Suit.DIAMONDS),
	QUEEN_HEARTS(Rank.QUEEN, Suit.HEARTS),
	QUEEN_SPADES(Rank.QUEEN, Suit.SPADES),
	KING_CLUBS(Rank.KING, Suit.CLUBS),
	KING_DIAMONDS(Rank.KING, Suit.DIAMONDS),
	KING_HEARTS(Rank.KING, Suit.HEARTS),
	KING_SPADES(Rank.KING, Suit.SPADES),
	ACE_CLUBS(Rank.ACE, Suit.CLUBS),
	ACE_DIAMONDS(Rank.ACE, Suit.DIAMONDS),
	ACE_HEARTS(Rank.ACE, Suit.HEARTS),
	ACE_SPADES(Rank.ACE, Suit.SPADES);

	/**
	 * Compile time constant for the number of cards.
	 */
	private static final int NUMBER_OF_CARDS = 13 * 4;

	/**
	 * Cached static copy of the values array.
	 */
	private static final Card[] VALUES = values();

	/**
	 * The rank of the card.
	 */
	private final Rank rank;

	/**
	 * The suit of the card.
	 */
	private final Suit suit;

	private Card(final Rank rank, final Suit suit) {
		this.rank = rank;
		this.suit = suit;
	}

	/**
	 * Get the rank of the card.
	 * 
	 * @return the rank of the card
	 */
	public Rank getRank() {
		return rank;
	}

	/**
	 * Get the suit of the card.
	 * 
	 * @return the suit of the card
	 */
	public Suit getSuit() {
		return suit;
	}

	/**
	 * Get the name of the card, e.g. "two of spades" or "ten of clubs".
	 * 
	 * @return the name of the card
	 */
	public String getName() {
		return rank.getName() + " of " + suit.getName();
	}

	/**
	 * Get the two letter short name of the card, e.g. "2s" or "Tc".
	 * 
	 * @return the two letter short name of the rank
	 */
	public String getShortName() {
		return rank.getShortName() + suit.getShortName();
	}

	/**
	 * Returns the short name of the card.
	 * 
	 * @return the short name of the card
	 */
	@Override
	public String toString() {
		return getShortName();
	}

	/**
	 * Get the first card, which is the two of clubs.
	 * 
	 * @return the first card
	 */
	public static Card first() {
		return VALUES[0];
	}

	/**
	 * Get the last cart, which is the ace of spades.
	 * 
	 * @return the last card
	 */
	public static Card last() {
		return VALUES[NUMBER_OF_CARDS - 1];
	}

	/**
	 * Get the next card, or null if this is the last card.
	 * 
	 * @return the card suit or null if this is the last card
	 */
	@Override
	public Card next() {
		final int i = ordinal() + 1;
		if (i < NUMBER_OF_CARDS) {
			return VALUES[i];
		}
		return null;
	}

	/**
	 * Get the previous card, or null if this is the previous card.
	 * 
	 * @return the previous card or null if this is the first card
	 */
	@Override
	public Card prev() {
		final int i = ordinal() - 1;
		if (i >= 0) {
			return VALUES[i];
		}
		return null;
	}

	/**
	 * Get a card by its rank and suit.
	 * 
	 * @param rank
	 *            the rank of the card
	 * @param suit
	 *            the suit of the card
	 * @return the card with the specified rank and suit
	 */
	public static Card byRankAndSuit(final Rank rank, final Suit suit) {
		return VALUES[(rank.ordinal() << 2) + suit.ordinal()];
	}

	/**
	 * Get a card by its name. The name can be the two letter short name, e.g.
	 * "2s" or "Tc", or the long name, e.g., "two of spades" or "ten of clubs".
	 * The name is case insensitive. If an invalid name is given an
	 * {@link IllegalArgumentException} is thrown. This method never returns
	 * <code>null</code>.
	 * 
	 * @param name
	 *            the name of the card
	 * @return the corresponding card
	 * @throws IllegalArgumentException
	 *             if the name is invalid
	 */
	public static Card byName(final String name) {
		String rankName = null, suitName = null;
		if (name.length() == 2) {
			rankName = name.substring(0, 1);
			suitName = name.substring(1, 2);
		} else {
			final String lowerName = name.toLowerCase();
			final int i = lowerName.indexOf(" of ");
			if (i >= 0) {
				rankName = lowerName.substring(0, i);
				suitName = lowerName.substring(i + 4);
			} else {
				throw new IllegalArgumentException("invalid card name: " + name);
			}
		}
		return byRankAndSuit(Rank.byName(rankName), Suit.byName(suitName));
	}

}
