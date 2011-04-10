package com.sebster.poker.holdem;

import com.sebster.poker.HoleCategory;
import com.sebster.poker.odds.Constants;

public class BasicHoldemPushFoldStrategy implements HoldemPushFoldStrategy {

	private final int position;

	private final double[][] strategies;

	public BasicHoldemPushFoldStrategy(final int position) {
		this.position = position;
		strategies = new double[(1 << position) - 1][Constants.HOLE_CATEGORY_COUNT];
	}

	@Override
	public int getPosition() {
		return position;
	}

	@Override
	public double getPushFrequency(final HoleCategory holeCategory, final int action) {
		return strategies[action][holeCategory.ordinal()];
	}

	public void setPushFrequency(final HoleCategory holeCategory, final int action, final double pushFrequency) {
		strategies[action][holeCategory.ordinal()] = pushFrequency;
	}

}
