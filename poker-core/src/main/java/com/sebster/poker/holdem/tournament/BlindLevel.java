package com.sebster.poker.holdem.tournament;

public class BlindLevel {

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
	 * Get the ante in chip.
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

}
