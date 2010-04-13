package com.sebster.poker;

import java.util.AbstractSet;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.SortedSet;

import net.jcip.annotations.Immutable;

import com.sebster.util.LinearOrder;

@Immutable
public final class CardSet extends AbstractSet<Card> implements LinearOrder<CardSet>, SortedSet<Card> {

	private final Card[] cards;

	/**
	 * Create a new card set from the specified cards. A defensive copy is made
	 * of the cards to ensure the card set is immutable.
	 * 
	 * @param cards
	 *            the cards to create the card set from
	 */
	public static CardSet fromCards(Card... cards) {

		// Validation.
		if (cards == null) {
			throw new NullPointerException("cards");
		}
		if (cards.length == 0) {
			throw new IllegalArgumentException("cards is empty");
		}
		for (int i = 0; i < cards.length; i++) {
			if (cards[i] == null) {
				throw new IllegalArgumentException("cards contains null at index " + i);
			}
		}

		// Defensive copy.
		cards = cards.clone();

		// Sort and check for duplicate cards.
		Arrays.sort(cards);
		for (int i = 1; i < cards.length; i++) {
			if (cards[i] == cards[i - 1]) {
				throw new IllegalArgumentException("cards contains duplicate card " + cards[i]);
			}
		}

		// Return new card set.
		return new CardSet(cards);
	}

	/**
	 * Create a new card set from the specified set of cards.
	 * 
	 * @param cards
	 *            the collection of cards to create the card set from
	 */
	public static CardSet fromCards(final Collection<Card> cards) {
		if (cards == null) {
			throw new NullPointerException("cards");
		}
		if (cards.isEmpty()) {
			throw new IllegalArgumentException("cards is empty");
		}
		return fromCards(cards.toArray(new Card[cards.size()]));
	}

	/**
	 * Return a card set from one of its string representations. The string
	 * representation must consist of the names of the cards, separated by a
	 * comma, e.g. "Ac,Td".
	 * 
	 * @param string
	 *            a string representation of the card set
	 * @return the corresponding hole
	 */
	public static CardSet fromString(String string) {
		if (string == null) {
			throw new NullPointerException("string");
		}
		string = string.trim();
		if (string.isEmpty()) {
			throw new IllegalArgumentException("string is empty");
		}
		final String[] cardNames = string.split(",");
		final Card[] cards = new Card[cardNames.length];
		for (int i = 0; i < cardNames.length; i++) {
			final String cardName = cardNames[i].trim();
			if (cardName.isEmpty()) {
				throw new IllegalArgumentException("string contains empty string at index " + i);
			}
			cards[i] = Card.byName(cardName);
		}
		return CardSet.fromCards(cards);
	}

	/**
	 * Return the string representation of the card set. This consists of the
	 * short names of the cards separated by a comma, e.g., "Ac,Td". This
	 * representation can be used as input to the
	 * {@link CardSet#fromString(String)} method.
	 * 
	 * @return the string representation of the card set
	 */
	@Override
	public String toString() {
		final StringBuilder buffer = new StringBuilder(cards[0].getShortName());
		for (int i = 1; i < cards.length; i++) {
			buffer.append(',');
			buffer.append(cards[i].getShortName());
		}
		return buffer.toString();
	}

	/**
	 * Return the first card set with the specified number of cards.
	 * 
	 * @param numCards
	 *            the number of cards
	 * @return the first card set with the specified number of cards
	 */
	public static CardSet first(final int numCards) {
		if (numCards <= 0) {
			throw new IllegalArgumentException("numCards <= 0");
		}
		if (numCards > 52) {
			throw new IllegalArgumentException("numCards > 52");
		}
		final Card[] cards = new Card[numCards];
		Card card = Card.first();
		for (int i = 0; i < numCards; i++) {
			cards[i] = card;
			card = card.next();
		}
		return new CardSet(cards);
	}

	/**
	 * Return the last card set with the specified number of cards.
	 * 
	 * @param numCards
	 *            the number of cards
	 * @return the last card set with the specified number of cards
	 */
	public static CardSet last(final int numCards) {
		if (numCards <= 0) {
			throw new IllegalArgumentException("numCards <= 0");
		}
		if (numCards > 52) {
			throw new IllegalArgumentException("numCards > 52");
		}
		final Card[] cards = new Card[numCards];
		Card card = Card.last();
		for (int i = numCards - 1; i >= 0; i--) {
			cards[i] = card;
			card = card.prev();
		}
		return new CardSet(cards);
	}

	/**
	 * Create a new card set for the specified card array. This constructor is
	 * only called internally with a valid array, so no validation is done.
	 * 
	 * @param cards
	 *            the cards
	 */
	private CardSet(final Card[] cards) {
		this.cards = cards;
	}

	/**
	 * Return the card at the specified index of the sorted card set.
	 * 
	 * @param index
	 *            the index of the card
	 * @return the card at the specified index
	 */
	public Card getCard(final int index) {
		return cards[index];
	}

	@Override
	public int compareTo(final CardSet other) {
		if (cards.length == other.cards.length) {
			for (int i = 0; i < cards.length; i++) {
				int c = cards[i].compareTo(other.cards[i]);
				if (c != 0) {
					return c;
				}
			}
			return 0;
		}
		return cards.length < other.cards.length ? -1 : 1;
	}

	@Override
	public CardSet next() {
		final Card[] cards = this.cards.clone();
		for (int i = cards.length - 1; i >= 0; i--) {
			if (cards[i].ordinal() < 51 - (cards.length - i)) {
				cards[i] = cards[i].next();
				for (int j = i + 1; j < cards.length; j++) {
					cards[j] = cards[j - 1].next();
				}
				return new CardSet(cards);
			}
		}
		return null;
	}

	@Override
	public CardSet prev() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int size() {
		return cards.length;
	}

	@Override
	public Card[] toArray() {
		// Defensive copy.
		return cards.clone();
	}

	@Override
	public Card first() {
		return cards[0];
	}

	@Override
	public Card last() {
		// TODO Auto-generated method stub
		return cards[cards.length - 1];
	}

	@Override
	public SortedSet<Card> headSet(final Card toElement) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SortedSet<Card> subSet(Card fromElement, Card toElement) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SortedSet<Card> tailSet(Card fromElement) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean contains(final Object object) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isEmpty() {
		// A card set is never empty.
		return false;
	}

	@Override
	public Iterator<Card> iterator() {
		return new CardsIterator();
	}

	private final class CardsIterator implements Iterator<Card> {

		private int i = 0;

		@Override
		public boolean hasNext() {
			return i < cards.length;
		}

		@Override
		public Card next() {
			return cards[i++];
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}

	}

	@Override
	public Comparator<? super Card> comparator() {
		// Natural order of the cards.
		return null;
	}

	@Override
	public boolean equals(final Object object) {
		if (object instanceof CardSet) {
			final CardSet other = (CardSet) object;
			return Arrays.equals(cards, other.cards);
		}
		return false;
	}

	@Override
	public int hashCode() {
		return 37 * super.hashCode() + Arrays.hashCode(cards);
	}

	@Override
	public boolean add(final Card card) {
		// Immutable.
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean addAll(final Collection<? extends Card> collection) {
		// Immutable.
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean remove(final Object object) {
		// Immutable.
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean removeAll(final Collection<?> c) {
		// Immutable.
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean retainAll(final Collection<?> c) {
		// Immutable.
		throw new UnsupportedOperationException();
	}

	@Override
	public void clear() {
		// Immutable.
		throw new UnsupportedOperationException();
	}

}
