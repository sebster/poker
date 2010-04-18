package com.sebster.poker;

import java.util.AbstractSet;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;

import net.jcip.annotations.Immutable;

import com.sebster.poker.util.Combinatorics;
import com.sebster.util.LinearOrder;

// TODO implement NavigableSet?
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
	 * Create a new card set from the specified deck.
	 * 
	 * @param deck
	 *            the deck to draw from
	 * @param size
	 *            the number of cards in the card set
	 * @return the card set drawn from the deck
	 * @throws {@link IllegalArgumentException} if the deck does not have enough
	 *         cards remaining
	 */
	public static CardSet fromDeck(final Deck deck, final int size) {
		if (size <= 0) {
			throw new IllegalArgumentException("size <= 0");
		}
		if (size > 52) {
			throw new IllegalArgumentException("size > 52");
		}
		final Card[] cards = new Card[size];
		for (int i = 0; i < size; i++) {
			cards[i] = deck.draw();
		}
		Arrays.sort(cards);
		return new CardSet(cards);
	}

	public static CardSet fromIndex(int index, final int size) {
		if (size <= 0) {
			throw new IllegalArgumentException("size <= 0");
		}
		if (size > 8) {
			throw new UnsupportedOperationException("card set too big to be indexed");
		}

		final Card[] cards = new Card[size];
		int offset = 0;
		for (int i = 0; i < cards.length; i++) {
			final int[] a = indexes[offset][cards.length - 1 - i];
			// Binary search.
			int low = 0, high = a.length;
			while (low + 1 != high) {
				final int mid = (low + high) >>> 1;
				if (a[mid] > index) {
					high = mid;
				} else {
					low = mid;
				}
			}
			index -= a[low];
			offset += low;
			cards[i] = Card.values()[offset++];
		}
		return new CardSet(cards);
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
	 * @param size
	 *            the number of cards
	 * @return the first card set with the specified number of cards
	 */
	public static CardSet first(final int size) {
		if (size <= 0) {
			throw new IllegalArgumentException("size <= 0");
		}
		if (size > 52) {
			throw new IllegalArgumentException("size > 52");
		}
		final Card[] cards = new Card[size];
		Card card = Card.first();
		for (int i = 0; i < size; i++) {
			cards[i] = card;
			card = card.next();
		}
		return new CardSet(cards);
	}

	/**
	 * Return the last card set with the specified number of cards.
	 * 
	 * @param size
	 *            the number of cards
	 * @return the last card set with the specified number of cards
	 */
	public static CardSet last(final int size) {
		if (size <= 0) {
			throw new IllegalArgumentException("size <= 0");
		}
		if (size > 52) {
			throw new IllegalArgumentException("size > 52");
		}
		final Card[] cards = new Card[size];
		Card card = Card.last();
		for (int i = size - 1; i >= 0; i--) {
			cards[i] = card;
			card = card.prev();
		}
		return new CardSet(cards);
	}

	/**
	 * Return the number different of card sets with the specified size
	 * 
	 * @param size
	 *            the size of the card sets
	 * @return the number of different card sets with the specified size
	 */
	public static int getCount(int size) {
		if (size <= 0) {
			throw new IllegalArgumentException("size <= 0");
		}
		if (size > 8) {
			throw new UnsupportedOperationException("size > 8");
		}
		return counts[size - 1];
	}

	/**
	 * Get the index of this cards set in the sorted list of all card sets with
	 * the same size.
	 * 
	 * @return the index of this card set
	 */
	public int getIndex() {
		if (cards.length > 8) {
			throw new UnsupportedOperationException("card set too big to be indexed");
		}
		int index = 0, offset = 0;
		for (int i = 0; i < cards.length; i++) {
			int first = cards[i].ordinal() - offset;
			index += indexes[offset][cards.length - 1 - i][first];
			offset += first + 1;
		}
		return index;
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
	public Card get(final int index) {
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
			if (cards[i].ordinal() < 52 - (cards.length - i)) {
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
		final Card[] cards = this.cards.clone();
		for (int i = cards.length - 1; i >= 0; i--) {
			if (cards[i].ordinal() > (i > 0 ? cards[i - 1].ordinal() + 1 : 0)) {
				cards[i] = cards[i].prev();
				if (i < cards.length - 1) {
					cards[cards.length - 1] = Card.last();
					for (int j = cards.length - 2; j > i; j--) {
						cards[j] = cards[j + 1].prev();
					}
				}
				return new CardSet(cards);
			}
		}
		return null;
	}

	/**
	 * Test if the card set contains common cards with the specified card set.
	 * 
	 * @param cardSet
	 *            the card set to test
	 * 
	 * @return <code>true</code> if this card set contains common cards with the
	 *         specified card set, <code>false</code> otherwise
	 */
	public boolean intersects(final CardSet cardSet) {
		int i = 0, j = 0;
		while (i < cards.length && j < cardSet.cards.length) {
			int k = cards[i].compareTo(cardSet.cards[j]);
			if (k == 0) {
				return true;
			}
			if (k < 0) {
				i++;
			} else {
				j++;
			}
		}
		return false;
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
		return cards[cards.length - 1];
	}

	@Override
	public SortedSet<Card> headSet(final Card toElement) {
		// TODO better implementation?
		return Collections.unmodifiableSortedSet(new TreeSet<Card>(this).headSet(toElement));
	}

	@Override
	public SortedSet<Card> subSet(final Card fromElement, final Card toElement) {
		// TODO better implementation?
		return Collections.unmodifiableSortedSet(new TreeSet<Card>(this).subSet(fromElement, toElement));
	}

	@Override
	public SortedSet<Card> tailSet(final Card fromElement) {
		// TODO better implementation?
		return Collections.unmodifiableSortedSet(new TreeSet<Card>(this).tailSet(fromElement));
	}

	@Override
	public boolean contains(final Object object) {
		if (object instanceof Card) {
			return Arrays.binarySearch(cards, (Card) object) >= 0;
		}
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

	public Iterator<Card> descendingIterator() {
		return new ReverseCardsIterator();
	}
	
	public Iterable<Card> descendingIterable() {
		return new Iterable<Card>() {
			@Override
			public Iterator<Card> iterator() {
				return descendingIterator();
			}
		};
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

	
	private final class ReverseCardsIterator implements Iterator<Card> {

		private int i = cards.length;

		@Override
		public boolean hasNext() {
			return i > 0;
		}

		@Override
		public Card next() {
			return cards[--i];
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
		return 31 * super.hashCode() + Arrays.hashCode(cards);
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
	public boolean removeAll(final Collection<?> collection) {
		// Immutable.
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean retainAll(final Collection<?> collection) {
		// Immutable.
		throw new UnsupportedOperationException();
	}

	@Override
	public void clear() {
		// Immutable.
		throw new UnsupportedOperationException();
	}

	private static final int[][][] indexes;

	private static final int[] counts;

	static {
		indexes = new int[52][][];
		for (int deckSize = 51; deckSize >= 0; deckSize--) {
			final int count = Math.min(8, deckSize + 2);
			indexes[51 - deckSize] = new int[count][];
			for (int length = 1; length <= count; length++) {
				indexes[51 - deckSize][length - 1] = new int[deckSize - length + 3];
				int i = 0;
				for (int first = 0; first <= deckSize - length + 1; first++) {
					i += Combinatorics.combinations(deckSize - first, length - 1);
					indexes[51 - deckSize][length - 1][first + 1] = i;
				}
			}
		}
		counts = new int[8];
		for (int i = 0; i < counts.length; i++) {
			counts[i] = Combinatorics.combinations(52, i + 1);
		}
	}

}