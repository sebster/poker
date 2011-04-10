package com.sebster.poker.gamestructure;

public abstract class GameStructure {

	private String name;

	private GameVariation gameVariation;

	private GameLimit gameLimit;

	private TableSize tableSize;

	private PokerSite pokerSite;

	public String getName() {
		return name;
	}

	public GameVariation getGameVariation() {
		return gameVariation;
	}

	public GameLimit getGameLimit() {
		return gameLimit;
	}

	public TableSize getTableSize() {
		return tableSize;
	}

	public PokerSite getPokerSite() {
		return pokerSite;
	}

	public abstract GameType getGameType();

}
