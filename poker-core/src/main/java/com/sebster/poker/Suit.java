package com.sebster.poker;

import java.util.HashMap;
import java.util.Map;

/**
 * Enumeration of the suits.
 * 
 * @author sebster
 */
public enum Suit {

	/**
	 * The suits.
	 */
	CLUBS, DIAMONDS, HEARTS, SPADES;

	/**
	 * The name of the suit, e.g. "spades", "hearts".
	 */
	private final String name;

	/**
	 * The singular name of the suit, e.g. "spade", "heart".
	 */
	private final String singularName;

	/**
	 * The single letter short name of the suit, e.g., "s", "h".
	 */
	private final String shortName;

	/**
	 * A map of the suits by their names.
	 */
	private static final Map<String, Suit> byName;

	/**
	 * Compile time constant for the number of suits.
	 */
	private static final int NUMBER_OF_SUITS = 4;

	/**
	 * Cached static copy of the values array.
	 */
	private static final Suit[] VALUES = values();

	static {
		byName = new HashMap<String, Suit>();
		for (final Suit suit : VALUES) {
			byName.put(suit.getName().toLowerCase(), suit);
			byName.put(suit.getShortName().toLowerCase(), suit);
			byName.put(suit.getSingularName().toLowerCase(), suit);
		}
	}

	private Suit() {
		this.name = name().toLowerCase();
		this.singularName = name.substring(0, name.length() - 1);
		this.shortName = name.substring(0, 1);
	}

	/**
	 * Get the value of the suit. This is equivalent to the ordinal value, i.e.,
	 * <code>0 = CLUBS</code>, <code>1 = DIAMONDS</code>,
	 * <code>2 = HEARTS</code>, <code>3 = SPADES</code>.
	 * 
	 * @return the value of the suit
	 */
	public int getValue() {
		return ordinal();
	}

	/**
	 * Get the name of the suit, e.g. "spades" or "hearts".
	 * 
	 * @return the name of the suit
	 */
	public String getName() {
		return name;
	}

	/**
	 * Get the singular name of the suit, e.g., "spade" or "heart".
	 * 
	 * @return the singular name of the suit
	 */
	public String getSingularName() {
		return singularName;
	}

	/**
	 * Get the single letter short name of the suit, e.g. "s" or "h".
	 * 
	 * @return the single letter short name of the suit
	 */
	public String getShortName() {
		return shortName;
	}

	/**
	 * Returns the short name of the suit.
	 * 
	 * @return the short name of the suit
	 */
	@Override
	public String toString() {
		return getShortName();
	}

	/**
	 * Get the first suit, which is clubs.
	 * 
	 * @return the first suit
	 */
	public static Suit first() {
		return VALUES[0];
	}

	/**
	 * Get the last suit, which is spades.
	 * 
	 * @return the last suit
	 */
	public static Suit last() {
		return VALUES[NUMBER_OF_SUITS - 1];
	}

	/**
	 * Get the next suit, or null if this is the last suit.
	 * 
	 * @return the next suit or null if this is the last suit
	 */
	public Suit next() {
		final int i = ordinal() + 1;
		if (i < NUMBER_OF_SUITS) {
			return VALUES[i];
		}
		return null;
	}

	/**
	 * Get the previous suit, or null if this is the previous suit.
	 * 
	 * @return the previous suit or null if this is the first suit
	 */
	public Suit prev() {
		final int i = ordinal() - 1;
		if (i >= 0) {
			return VALUES[i];
		}
		return null;
	}

	/**
	 * Get a suit by its name. The name can be the single letter short name,
	 * e.g. "s" or "h", the singular name, e.g., "spade" or "heart", or the
	 * plural name, e.g., "spades" or "hearts". The name is case insensitive. If
	 * an invalid suit name is given an {@link IllegalArgumentException} is
	 * thrown. This method never returns <code>null</code>.
	 * 
	 * @param name
	 *            the name of the suit
	 * @return the corresponding suit
	 * @throws IllegalArgumentException
	 *             if the name is invalid
	 */
	public static Suit byName(final String name) {
		final Suit suit = byName.get(name.toLowerCase());
		if (suit == null) {
			throw new IllegalArgumentException("invalid suit name: " + name);
		}
		return suit;
	}

	/**
	 * Get a suit by its single letter name, e.g. 's' or 'h'. The name is case
	 * insensitive. If an invalid suit name is given an
	 * {@link IllegalArgumentException} is thrown. This method never returns
	 * <code>null</code>.
	 * 
	 * @param name
	 *            the name of the suit
	 * @return the corresponding suit
	 * @throws IllegalArgumentException
	 *             if the name is invalid
	 */
	public static Suit byName(final char name) {
		return byName(String.valueOf(name));
	}

	/**
	 * Get a suit by its value. The value is a number from 0 to 3. If an invalid
	 * suit value is given an {@link IllegalArgumentException} is thrown. This
	 * method never returns <code>null</code>.
	 * 
	 * @param value
	 *            the value of the suit
	 * @return the corresponding suit
	 * @throws IllegalArgumentException
	 *             if the value is invalid
	 */
	public static Suit byValue(final int value) {
		if (value < 0 || value >= NUMBER_OF_SUITS) {
			throw new IllegalArgumentException("invalid suit value: " + value);
		}
		return VALUES[value];
	}

}
