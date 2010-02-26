package com.sebster.poker.holdem.tournament;

import java.math.BigDecimal;

public abstract class TournamentStructure {

	/** The tournament buy-in. */
	BigDecimal buyin;

	/** The tournament fee. */
	BigDecimal fee;

	int chips;

	int minPlayers;
	
	int maxPlayers;

	int seatsPerTable;

	BlindLevel[] blindLevels;

}
