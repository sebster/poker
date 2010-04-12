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

	/**
	 * Compare this card set to another card set. A linear order is defined
	 * among card sets of the same size.
	 * 
	 * @param other
	 * @return
	 */
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
			final Card next = cards[i].next();
			if (next != null) {
				cards[i] = next;
				return new CardSet(cards);
			}

		}
		// a b c d e
		// e.next != null ?
		// d.next == null ?
		// TODO Auto-generated method stub
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
	
	/**
	 * Return the string representation of the card set. This consists of the short
	 * names of the cards separated by a comma, e.g., "Ac,Td".
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
	 * Return a hole from its string representation. The string representation
	 * must consist of the short names of the two cards, separated by a comma,
	 * e.g. "Ac,Td".
	 * 
	 * @param string
	 *            the string representation of the hole
	 * @return the corresponding hole
	 */
	public static Hole fromString(final String string) {
		// FIXME fix
		if (string == null) {
			throw new NullPointerException("string");
		}
		if (string.length() != 5 || string.charAt(2) != ',') {
			throw new IllegalArgumentException("invalid hole: " + string);
		}
		return new Hole(Card.byName(string.substring(0, 2)), Card.byName(string.substring(3, 5)));
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
