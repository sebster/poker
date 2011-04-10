package com.sebster.poker.gamestructure;

import net.jcip.annotations.Immutable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * A single blind level, which consists of a small blind, a big blind, an ante
 * (which may be {@code 0}), the length of the level in seconds, and the length
 * of the break after the level in seconds (which may be {@code 0}).
 */
@Immutable
public final class BlindLevel {

	/** The small blind in chips. */
	private final int smallBlind;

	/** The big blind in chips. */
	private final int bigBlind;

	/** The ante in chips. */
	private final int ante;

	/** The length of the level in seconds. */
	private final int length;

	/** The length of the break after the level in seconds. */
	private final int breakLength;

	public BlindLevel(final int smallBlind, final int bigBlind, final int ante, final int length, final int breakLength) {
		this.smallBlind = smallBlind;
		this.bigBlind = bigBlind;
		this.ante = ante;
		this.length = length;
		this.breakLength = breakLength;
	}

	/**
	 * Get the small blind in chips.
	 * 
	 * @return the small blind in chips
	 */
	public int getSmallBlind() {
		return smallBlind;
	}

	/**
	 * Get the big blind in chips.
	 * 
	 * @return the big blind in chips
	 */
	public int getBigBlind() {
		return bigBlind;
	}

	/**
	 * Get the ante in chips
	 * 
	 * @return the ante in chips
	 */
	public int getAnte() {
		return ante;
	}

	/**
	 * Get the length of the level in seconds.
	 * 
	 * @return the length of the level in seconds
	 */
	public int getLength() {
		return length;
	}

	/**
	 * Get the length of the break after the level in seconds.
	 * 
	 * @return the length of the break after the level in seconds
	 */
	public int getBreakLength() {
		return breakLength;
	}

	@Override
	public boolean equals(final Object object) {
		if (this == object) {
			return true;
		}
		if (object instanceof BlindLevel) {
			final BlindLevel other = (BlindLevel) object;
			return new EqualsBuilder().append(smallBlind, other.smallBlind).append(bigBlind, other.bigBlind).append(ante, other.ante).append(length, other.length).append(breakLength, other.breakLength).isEquals();
		}
		return false;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(smallBlind).append(bigBlind).append(ante).append(length).append(breakLength).toHashCode();
	}

}
