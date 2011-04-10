package com.sebster.poker.holdem;

/**
 * A strategy profile for hold'em.
 * 
 * @author sebster
 */
public interface HoldemPushFoldStrategyProfile {

	/**
	 * Get the number of players this strategy profile is for.
	 * 
	 * @return the number of players this strategy profile is for
	 */
	int getNumberOfPlayers();

	/**
	 * Get the strategy for the specified player (position). The player number
	 * (position) is 0 based.
	 * 
	 * @param player
	 *            the player
	 * 
	 * @return the strategy for the specified player
	 */
	HoldemPushFoldStrategy getStrategy(int player);

}
