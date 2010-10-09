package com.sebster.poker.holdem.tournament.icm;

import com.sebster.math.rational.Rational;

public interface ChipModel {

	/**
	 * Return the winning probability of the specified player given the stack
	 * sizes and the total chip count. The total chip count is added as a
	 * parameter for performance reasons: it's a constant, and it would be a
	 * waste of CPU cycles to constantly calculate it from the stack sizes.
	 * 
	 * @param stacks
	 *            the stack sizes
	 * @param chips
	 *            the total chip count (the sum of all the stack sizes)
	 * @param player
	 *            the player
	 * @return the winning probability of the specified player
	 */
	Rational getWinProbability(Rational[] stacks, final Rational chips, int player);

}
