package com.sebster.poker.holdem.tournament.icm;

import com.sebster.math.rational.Rational;

public enum IndependentChipModel implements ChipModel {

	INSTANCE;
	
	@Override
	public Rational getWinProbability(final Rational[] stacks, final Rational chips, final int player) {
		return stacks[player].dividedBy(chips);
	}

	@Override
	public double getWinProbabilityAsDouble(final int[] stacks, final int chips, final int player) {
		return stacks[player] / (double) chips;
	}

}
