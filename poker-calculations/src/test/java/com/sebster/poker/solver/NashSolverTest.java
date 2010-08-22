package com.sebster.poker.solver;

import java.util.EnumMap;

import org.junit.Test;

import com.sebster.math.rational.Rational;
import com.sebster.poker.HoleCategory;
import com.sebster.util.collections.Pair;

public class NashSolverTest {

	@Test
	public void testNashSolver() {
		final Pair<EnumMap<HoleCategory, Rational>, EnumMap<HoleCategory, Rational>> strategies = NashSolver.calculateNashEquilibrium(1000, 50, 100);
		System.out.println("Strategy for player 1: " + strategies.getFirst());
		System.out.println("Strategy for player 2: " + strategies.getSecond());
	}
	
}
