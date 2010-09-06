package com.sebster.poker.holdem;

import com.sebster.math.rational.Rational;
import com.sebster.poker.HoleCategory;

public class ReadOnlyAllinOrFoldStrategy implements AllinOrFoldStrategy {

	private final AllinOrFoldStrategy strategy;
	
	public ReadOnlyAllinOrFoldStrategy(final AllinOrFoldStrategy strategy) {
		this.strategy = strategy;
	}
	
	@Override
	public Rational getAllinFrequency(final HoleCategory holeCategory) {
		return strategy.getAllinFrequency(holeCategory);
	}

}
