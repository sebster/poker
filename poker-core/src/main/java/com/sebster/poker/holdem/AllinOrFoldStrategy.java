package com.sebster.poker.holdem;

import com.sebster.math.rational.Rational;
import com.sebster.poker.HoleCategory;

public interface AllinOrFoldStrategy {

	Rational getAllinFrequency(HoleCategory holeCategory);
	
}
