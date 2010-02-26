package com.sebster.poker;

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
	private int remaining;

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
	public int getRemaining() {
		return remaining;
	}

	/**
	 * Return whether or not there are still cards remaining in the deck.
	 * 
	 * @return whether or not there are still cards remaining in the deck
	 */
	public boolean hasRemaining() {
		return remaining > 0;
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
			int i = random.nextInt(remaining);
			Card card = cards[i];
			cards[i] = cards[--remaining];
			cards[remaining] = card;
			return card;
		}
		throw new IllegalStateException("no cards remaining");
	}

	/**
	 * Shuffle the deck (after which the number of remaining cards is again 52).
	 */
	public void shuffle() {
		remaining = 52;
	}

}
