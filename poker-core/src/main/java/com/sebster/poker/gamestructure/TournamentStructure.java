package com.sebster.poker.gamestructure;

public class TournamentStructure extends GameStructure {

	private BettingStructure bettingStructure;

	private TournamentType tournamentType;

	private BlindSpeed blindSpeed;

	private StackDepth stackDepth;

	public BettingStructure getBettingStructure() {
		return bettingStructure;
	}

	public TournamentType getTournamentType() {
		return tournamentType;
	}

	public BlindSpeed getBlindSpeed() {
		return blindSpeed;
	}

	public StackDepth getStackDepth() {
		return stackDepth;
	}

	@Override
	public final GameType getGameType() {
		return GameType.TOURNAMENT;
	}

}
