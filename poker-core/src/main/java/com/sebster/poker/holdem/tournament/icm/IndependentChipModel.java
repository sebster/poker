package com.sebster.poker.holdem.tournament.icm;

public class IndependentChipModel implements ChipModel {

	private static final IndependentChipModel INSTANCE = new IndependentChipModel();
	
	public static IndependentChipModel getInstance() {
		return INSTANCE;
	}
	private IndependentChipModel() {
		// Singleton.
	}
	
	@Override
	public double getWinProbability(final int[] stacks, final int chips, final int player) {
		return ((double) stacks[player]) / chips;
	}

}
