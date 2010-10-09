package com.sebster.poker;

import java.util.AbstractSet;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.NavigableSet;

import net.jcip.annotations.Immutable;

import com.sebster.poker.util.Combinatorics;
import com.sebster.util.LinearOrder;

@Immutable
public class CardSet extends AbstractSet<Card> implements LinearOrder<CardSet>, NavigableSet<Card> {

	protected final Card[] cards;

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

		// Defensive copy.
		cards = cards.clone();

		// Check for null cards.
		for (int i = 0; i < cards.length; i++) {
			if (cards[i] == null) {
				throw new IllegalArgumentException("cards contains null at index " + i);
			}
		}

		// Sort and check for duplicate cards.
		Arrays.sort(cards);
		for (int i = 1; i < cards.length; i++) {
			if (cards[i] == cards[i - 1]) {
				throw new IllegalArgumentException("cards contains duplicate card " + cards[i]);
			}
		}

		// Return new card set.
		return getInstance(cards);
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
		return getInstance(cards);
	}

	/**
	 * Create a new card set from an index for the specified number of cards.
	 * 
	 * @param index
	 *            the index in the ordered list of card sets of the specified
	 *            size
	 * @param size
	 *            the size of the card set
	 * @return the card set at the specified index
	 * @throws IllegalArgumentException
	 *             if {@literal size <= 0}
	 * @throws UnsupportedOperationException
	 *             if {@literal size > 8}
	 */
	public static CardSet fromIndex(int index, final int size) {
		// FIXME test index!
		if (size <= 0) {
			throw new IllegalArgumentException("size <= 0");
		}
		if (size > 8) {
			throw new UnsupportedOperationException("card set too big to be indexed");
		}

		final Card[] cards = new Card[size];
		int offset = 0;
		for (int i = 0; i < cards.length; i++) {
			final int[] a = INDEXES[offset][cards.length - 1 - i];
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
		return getInstance(cards);
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
		final StringBuilder buffer = new StringBuilder(first().getShortName());
		final int size = cards.length;
		for (int i = 1; i < size; i++) {
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
	public static CardSet firstSet(final int size) {
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
		return getInstance(cards);
	}

	/**
	 * Return the last card set with the specified number of cards.
	 * 
	 * @param size
	 *            the number of cards
	 * @return the last card set with the specified number of cards
	 */
	public static CardSet lastSet(final int size) {
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
		return getInstance(cards);
	}

	/**
	 * Return the number different of card sets with the specified size.
	 * 
	 * @param size
	 *            the size of the card sets
	 * @return the number of different card sets with the specified size
	 */
	public static int numberOfSets(int size) {
		if (size <= 0) {
			throw new IllegalArgumentException("size <= 0");
		}
		if (size > 8) {
			throw new UnsupportedOperationException("size > 8");
		}
		return NUMBER_OF_SETS[size - 1];
	}

	/**
	 * Get the index of this cards set in the sorted list of all card sets with
	 * the same size.
	 * 
	 * @return the index of this card set
	 * @throws UnsupportedOperationException
	 *             if the size of this card set is greater than 8
	 */
	public int getIndex() {
		if (size() > 8) {
			throw new UnsupportedOperationException("card set too big to be indexed");
		}
		final int size = cards.length;
		int index = 0, offset = 0;
		for (int i = 0; i < size; i++) {
			int first = cards[i].ordinal() - offset;
			index += INDEXES[offset][size - 1 - i][first];
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
	CardSet(final Card[] cards) {
		this.cards = cards;
	}

	/**
	 * Get an instance for the specified cards. If the number of cards is 2,
	 * then a unique Hole instance for the specified cards is returned. If the
	 * number of cards is 4 then Hole4 instance is returned.
	 * 
	 * Precondition: The cards must be all different, not-null, and sorted in
	 * their natural order.
	 * 
	 * @param cards
	 *            the cards
	 * @return a CardSet instance for the specified cards
	 */
	static CardSet getInstance(final Card[] cards) {
		switch (cards.length) {
		case 2:
			// Delegate Hole instance control to Hole class.
			return Hole.getInstance(cards[0], cards[1]);
		case 4:
			// Delegate Hole4 instance control to Hole4 class.
			return Hole4.getInstance(cards);
		default:
			// For other sizes return a new CardSet instance.
			return new CardSet(cards);
		}
	}

	/**
	 * Return the card at the specified index of the sorted card set.
	 * 
	 * @param index
	 *            the index of the card
	 * @return the card at the specified index
	 * @throws IllegalArgumentException
	 *             if {@literal index < 0 or index >= size()}
	 */
	public Card get(final int index) {
		try {
			return cards[index];
		} catch (final ArrayIndexOutOfBoundsException e) {
			throw new IllegalArgumentException("index out of bounds: " + index, e);
		}
	}

	@Override
	public int size() {
		return cards.length;
	}

	@Override
	public int compareTo(final CardSet other) {
		final int size = cards.length;
		final int otherSize = other.cards.length;
		if (size == otherSize) {
			for (int i = 0; i < size; i++) {
				final int c = cards[i].compareTo(other.cards[i]);
				if (c != 0) {
					return c;
				}
			}
			return 0;
		}
		return size < otherSize ? -1 : 1;
	}

	@Override
	public Card[] toArray() {
		// Defensive copy.
		return cards.clone();
	}

	@Override
	public CardSet next() {
		final Card[] cards = toArray();
		for (int i = cards.length - 1; i >= 0; i--) {
			if (cards[i].ordinal() < 52 - (cards.length - i)) {
				cards[i] = cards[i].next();
				for (int j = i + 1; j < cards.length; j++) {
					cards[j] = cards[j - 1].next();
				}
				return getInstance(cards);
			}
		}
		return null;
	}

	@Override
	public CardSet prev() {
		final Card[] cards = toArray();
		for (int i = cards.length - 1; i >= 0; i--) {
			if (cards[i].ordinal() > (i > 0 ? cards[i - 1].ordinal() + 1 : 0)) {
				cards[i] = cards[i].prev();
				if (i < cards.length - 1) {
					cards[cards.length - 1] = Card.last();
					for (int j = cards.length - 2; j > i; j--) {
						cards[j] = cards[j + 1].prev();
					}
				}
				return getInstance(cards);
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
		final int size = cards.length, otherSize = cardSet.cards.length;
		int i = 0, j = 0;
		while (i < size && j < otherSize) {
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
	public Card first() {
		return cards[0];
	}

	@Override
	public Card last() {
		return cards[cards.length - 1];
	}

	@Override
	public NavigableSet<Card> headSet(final Card toElement) {
		return headSet(toElement, false);
	}

	@Override
	public NavigableSet<Card> headSet(final Card toElement, final boolean inclusive) {
		// FIXME implement
		throw new AssertionError("not implemented yet");
	}

	@Override
	public NavigableSet<Card> subSet(final Card fromElement, final Card toElement) {
		return subSet(toElement, true, fromElement, false);
	}

	@Override
	public NavigableSet<Card> subSet(final Card fromElement, final boolean fromInclusive, final Card toElement, final boolean toInclusive) {
		// FIXME implement
		throw new AssertionError("not implemented yet");
	}

	@Override
	public NavigableSet<Card> tailSet(final Card fromElement) {
		return tailSet(fromElement, true);
	}

	@Override
	public NavigableSet<Card> tailSet(final Card fromElement, final boolean inclusive) {
		// FIXME implement
		throw new AssertionError("not implemented yet");
	}

	@Override
	public NavigableSet<Card> descendingSet() {
		// FIXME implement
		throw new AssertionError("not implemented yet");
	}

	@Override
	public Card lower(final Card e) {
		int i = Arrays.binarySearch(cards, e);
		i = i < 0 ? -(i + 2) : i - 1;
		return i < 0 ? null : cards[i];
	}

	@Override
	public Card floor(final Card e) {
		int i = Arrays.binarySearch(cards, e);
		if (i < 0) {
			i = -(i + 2);
			return i < 0 ? null : cards[i];
		}
		return cards[i];
	}

	@Override
	public Card higher(final Card e) {
		int i = Arrays.binarySearch(cards, e);
		i = i < 0 ? -(i + 1) : i + 1;
		return i < cards.length ? cards[i] : null;
	}

	@Override
	public Card ceiling(final Card e) {
		int i = Arrays.binarySearch(cards, e);
		if (i < 0) {
			i = -(i + 1);
		}
		return i < cards.length ? cards[i] : null;
	}

	@Override
	public boolean contains(final Object object) {
		if (object instanceof Card) {
			return Arrays.binarySearch(cards, object) >= 0;
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

	@Override
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

	class CardsIterator implements Iterator<Card> {

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

	class ReverseCardsIterator implements Iterator<Card> {

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
		if (this == object) {
			return true;
		}
		if (!(object instanceof CardSet)) {
			return false;
		}
		final CardSet other = (CardSet) object;
		final int size = cards.length;
		if (size != other.cards.length) {
			return false;
		}
		for (int i = 0; i < size; i++) {
			if (cards[i] != other.cards[i]) {
				return false;
			}
		}
		return true;
	}

	@Override
	public int hashCode() {
		final int size = cards.length;
		int result = 1;
		for (int i = 0; i < size; i++) {
			result = result * 31 + cards[i].getRank().ordinal();
		}
		return result;
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

	@Override
	public Card pollFirst() {
		// Immutable.
		throw new UnsupportedOperationException();
	}

	@Override
	public Card pollLast() {
		// Immutable.
		throw new UnsupportedOperationException();
	}

	protected static final int[][][] INDEXES;

	protected static final int[] NUMBER_OF_SETS;

	static {
		INDEXES = new int[52][][];
		for (int deckSize = 51; deckSize >= 0; deckSize--) {
			final int count = Math.min(8, deckSize + 2);
			INDEXES[51 - deckSize] = new int[count][];
			for (int length = 1; length <= count; length++) {
				INDEXES[51 - deckSize][length - 1] = new int[deckSize - length + 3];
				int i = 0;
				for (int first = 0; first <= deckSize - length + 1; first++) {
					i += Combinatorics.combinations(deckSize - first, length - 1);
					INDEXES[51 - deckSize][length - 1][first + 1] = i;
				}
			}
		}
		NUMBER_OF_SETS = new int[8];
		for (int i = 0; i < NUMBER_OF_SETS.length; i++) {
			NUMBER_OF_SETS[i] = Combinatorics.combinations(52, i + 1);
		}
	}

}