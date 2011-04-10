package com.sebster.poker.holdem;

public class BasicHoldemPushFoldStrategyProfile implements HoldemPushFoldStrategyProfile {

	private final int numberOfPlayers;

	private final BasicHoldemPushFoldStrategy[] strategies;

	public BasicHoldemPushFoldStrategyProfile(final int numberOfPlayers) {
		this.numberOfPlayers = numberOfPlayers;
		strategies = new BasicHoldemPushFoldStrategy[numberOfPlayers];
		for (int player = 0; player < numberOfPlayers; player++) {
			strategies[player] = new BasicHoldemPushFoldStrategy(player);
		}
	}

	@Override
	public int getNumberOfPlayers() {
		return numberOfPlayers;
	}

	@Override
	public BasicHoldemPushFoldStrategy getStrategy(final int player) {
		return strategies[player];
	}

}
