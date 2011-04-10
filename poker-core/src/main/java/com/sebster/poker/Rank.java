package com.sebster.poker;

import java.util.HashMap;
import java.util.Map;

import com.sebster.util.LinearOrderElement;

/**
 * Enumeration of the ranks.
 * 
 * @author sebster
 */
public enum Rank implements LinearOrderElement<Rank> {

	/**
	 * The ranks.
	 */
	TWO("two", "deuces", "2"),
	THREE("three", "treys", "3"),
	FOUR("four", "fours", "4"),
	FIVE("five", "fives", "5"),
	SIX("six", "sixes", "6"),
	SEVEN("seven", "sevens", "7"),
	EIGHT("eight", "eights", "8"),
	NINE("nine", "nines", "9"),
	TEN("ten", "tens", "T"),
	JACK("jack", "jacks", "J"),
	QUEEN("queen", "queens", "Q"),
	KING("king", "kings", "K"),
	ACE("ace", "aces", "A");

	/**
	 * Compile time constant for the number of ranks.
	 */
	private static final int NUMBER_OF_RANKS = 13;

	/**
	 * Cached static copy of the values array.
	 */
	private static final Rank[] VALUES = values();

	/**
	 * The name of the rank, e.g. "two", "ten", "ace".
	 */
	private final String name;

	/**
	 * The plural name of the rank, e.g., "deuces", "tens", "aces".
	 */
	private final String pluralName;

	/**
	 * The single letter short name of the rank, e.g. "2", "T", "A".
	 */
	private final String shortName;

	/**
	 * A map of the ranks by their names.
	 */
	private static final Map<String, Rank> byName;

	static {
		byName = new HashMap<String, Rank>();
		for (final Rank rank : VALUES) {
			byName.put(rank.getName().toLowerCase(), rank);
			byName.put(rank.getPluralName().toLowerCase(), rank);
			byName.put(rank.getShortName().toLowerCase(), rank);
		}
	}

	private Rank(final String name, final String pluralName, final String shortName) {
		this.name = name;
		this.pluralName = pluralName;
		this.shortName = shortName;
	}

	/**
	 * Get the value of the rank. This is equivalent to the ordinal value + 2,
	 * i.e., <code>2 = TWO</code>, <code>3 = THREE</code>, ...,
	 * <code>12 = KING</code>, <code>13 = ACE</code>.
	 * 
	 * @return the value of the rank
	 */
	public int getValue() {
		return ordinal() + 2;
	}

	/**
	 * Get the name of the rank.
	 * 
	 * @return the name of the rank
	 */
	public String getName() {
		return name;
	}

	/**
	 * Get the plural name of the rank.
	 * 
	 * @return the plural name of the rank
	 */
	public String getPluralName() {
		return pluralName;
	}

	/**
	 * Get the single letter short name of the rank.
	 * 
	 * @return the single letter short name of the rank
	 */
	public String getShortName() {
		return shortName;
	}

	/**
	 * Returns the short name of the rank.
	 * 
	 * @return the short name of the rank
	 */
	@Override
	public String toString() {
		return getShortName();
	}

	/**
	 * Get the first rank, which is two.
	 * 
	 * @return the first rank
	 */
	public Rank first() {
		return VALUES[0];
	}

	/**
	 * Get the last rank, which is ace.
	 * 
	 * @return the last rank
	 */
	public Rank last() {
		return VALUES[NUMBER_OF_RANKS - 1];
	}

	/**
	 * Get the next rank, or null if this is the last rank.
	 * 
	 * @return the next rank or null if this is the last rank
	 */
	@Override
	public Rank next() {
		final int i = ordinal() + 1;
		if (i < NUMBER_OF_RANKS) {
			return VALUES[i];
		}
		return null;
	}

	/**
	 * Get the previous rank, or null if this is the previous rank.
	 * 
	 * @return the previous rank or null if this is the first rank
	 */
	@Override
	public Rank prev() {
		final int i = ordinal() - 1;
		if (i >= 0) {
			return VALUES[i];
		}
		return null;
	}

	/**
	 * Get a rank by its name. The name can be the single letter name, e.g. "2"
	 * or "A", the singular name, e.g., "two" or "ace", or the plural name,
	 * e.g., "deuces" or "aces". The name is case insensitive. If an invalid
	 * rank name is given an {@link IllegalArgumentException} is thrown. This
	 * method never returns <code>null</code>.
	 * 
	 * @param name
	 *            the name of the rank
	 * @return the corresponding rank
	 * @throws IllegalArgumentException
	 *             if the name is invalid
	 */
	public static Rank byName(final String name) {
		final Rank rank = byName.get(name.toLowerCase());
		if (rank == null) {
			throw new IllegalArgumentException("invalid rank name: " + name);
		}
		return rank;
	}

	/**
	 * Get a rank by its name. The name can be the single character name, e.g.
	 * '2' or 'A'. The name is case insensitive. If an invalid rank name is
	 * given an {@link IllegalArgumentException} is thrown. This method never
	 * returns <code>null</code>.
	 * 
	 * @param name
	 *            the name of the rank
	 * @return the corresponding rank
	 * @throws IllegalArgumentException
	 *             if the name is invalid
	 */
	public static Rank byName(final char name) {
		return byName(String.valueOf(name));
	}

	/**
	 * Get a rank by its value. The value is a number from 2 to 13, with the ace
	 * being 13. If an invalid rank is given an {@link IllegalArgumentException}
	 * is thrown. This method never returns <code>null</code>.
	 * 
	 * @param value
	 *            the value of the rank
	 * @return the corresponding rank
	 * @throws IllegalArgumentException
	 *             if the value is invalid
	 */
	public static Rank byValue(final int value) {
		if (value < 2 || value >= NUMBER_OF_RANKS + 2) {
			throw new IllegalArgumentException("invalid rank value: " + value);
		}
		return VALUES[value - 2];
	}

}
