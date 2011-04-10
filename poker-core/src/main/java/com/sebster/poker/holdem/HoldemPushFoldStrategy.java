package com.sebster.poker.holdem;

import com.sebster.poker.HoleCategory;

/**
 * A (mixed) push/fold strategy for hold'em.
 * 
 * @author sebster
 */
public interface HoldemPushFoldStrategy {

	/**
	 * Return the position of the player this strategy is for. The position is
	 * 0-based.
	 * 
	 * @return the position
	 */
	int getPosition();

	/**
	 * Return the push frequency for the specified hole category, given the
	 * action of the players in earlier positions. The action is interpreted as
	 * a list of bits indicating whether the player in the respective position
	 * pushed (1) or folded (0).
	 * 
	 * @param holeCategory
	 *            the hole category
	 * @param action
	 *            the previous action
	 * @return the push frequency of this mixed strategy
	 */
	double getPushFrequency(HoleCategory holeCategory, int action);

}
