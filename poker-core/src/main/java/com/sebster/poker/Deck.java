package com.sebster.poker;

import java.util.EnumSet;
import java.util.Random;

/**
 * A deck of cards which can be shuffled and from which cards can be drawn.
 * 
 * @author sebster
 */
public class Deck {

	/**
	 * The cards.
	 */
	private final Card[] cards = new Card[52];

	/**
	 * The random generator used to draw.
	 */
	private final Random random;

	/**
	 * The number of cards remaining in the deck.
	 */
	private int numberRemaining;

	/**
	 * Create a shuffled deck of cards.
	 */
	public Deck() {
		this(new Random());
	}

	/**
	 * Create a shuffled deck of cards with the specified random number
	 * generator.
	 * 
	 * @param random
	 *            the random generator used to shuffle the deck
	 */
	public Deck(final Random random) {
		System.arraycopy(Card.values(), 0, cards, 0, 52);
		this.random = random;
		shuffle();
	}

	/**
	 * Get the number of cards remaining in the deck.
	 * 
	 * @return the number of cards remaining in the deck
	 */
	public int getNumberRemaining() {
		return numberRemaining;
	}

	/**
	 * Return whether or not there are still cards remaining in the deck.
	 * 
	 * @return whether or not there are still cards remaining in the deck
	 */
	public boolean hasRemaining() {
		return numberRemaining > 0;
	}

	/**
	 * Get a set with all the remaining cards in the deck. The deck remains
	 * unchanged.
	 * 
	 * @return a set with all the cards remaining in the deck
	 */
	public EnumSet<Card> getRemaining() {
		final EnumSet<Card> result = EnumSet.noneOf(Card.class);
		for (int i = 0; i < numberRemaining; i++) {
			result.add(cards[i]);
		}
		return result;
	}

	/**
	 * Draw the top card from the deck. The number of cards remaining is reduced
	 * by one.
	 * 
	 * @return the top card from the deck
	 * @throws {@link IllegalStateException} if there are no cards remaining in
	 *         the deck
	 */
	public Card draw() {
		if (hasRemaining()) {
			int i = random.nextInt(numberRemaining);
			Card card = cards[i];
			cards[i] = cards[--numberRemaining];
			cards[numberRemaining] = card;
			return card;
		}
		throw new IllegalStateException("no cards remaining");
	}

	/**
	 * Shuffle the deck (after which the number of remaining cards is again 52).
	 */
	public void shuffle() {
		numberRemaining = 52;
	}

}
