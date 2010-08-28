package com.sebster.poker.solver;

import java.util.EnumSet;

import org.junit.Assert;
import org.junit.Test;

import com.sebster.math.rational.Rational;
import com.sebster.poker.HoleCategory;
import com.sebster.poker.HoleRange;
import com.sebster.poker.holdem.AllinOrFoldStrategy;
import com.sebster.poker.holdem.PureAllinOrFoldStrategy;
import com.sebster.poker.holdem.odds.TwoPlayerPreFlopHoleCategoryOddsDB;

public class SolverTest {

	@Test
	public void testEV() {
		AllinOrFoldStrategy emptyStrategy = new PureAllinOrFoldStrategy();
		AllinOrFoldStrategy fullStrategy = new PureAllinOrFoldStrategy(EnumSet.allOf(HoleCategory.class));
		Assert.assertEquals(new Rational(-50), Solver.computeSBEV(emptyStrategy, fullStrategy, 1000, 100));
		Assert.assertEquals(new Rational(100), Solver.computeSBEV(fullStrategy, emptyStrategy, 1000, 100));
	}
	
	@Test
	public void testSolver() {
		AllinOrFoldStrategy pushRange, callRange;
		TwoPlayerPreFlopHoleCategoryOddsDB.getInstance().getOdds(HoleCategory.pAA, HoleCategory.pAA);

		pushRange = new PureAllinOrFoldStrategy(HoleRange.fromString("AA"));
		callRange = new PureAllinOrFoldStrategy(HoleRange.fromString("AA"));
		Assert.assertEquals(callRange, Solver.optimalBBStrategy(pushRange, 1000, 100));
		
		pushRange = new PureAllinOrFoldStrategy(HoleRange.fromString("66+,ATs+,AJo+"));
		callRange = new PureAllinOrFoldStrategy(HoleRange.fromString("99+,AKo,AQs+"));
		Assert.assertEquals(callRange, Solver.optimalBBStrategy(pushRange, 1000, 100));

		pushRange = new PureAllinOrFoldStrategy(HoleRange.fromString("44+,A7s+,A9o+,KJs+"));
		callRange = new PureAllinOrFoldStrategy(HoleRange.fromString("66+,AJ+"));
		Assert.assertEquals(callRange, Solver.optimalBBStrategy(pushRange, 1000, 100));

		pushRange = new PureAllinOrFoldStrategy(HoleRange.fromString("22+,A2s+,A3o+,KTs+,KJo+,QJs"));
		callRange = new PureAllinOrFoldStrategy(HoleRange.fromString("33+,A8o+,A7s+,KQs"));
		Assert.assertEquals(callRange, Solver.optimalBBStrategy(pushRange, 1000, 100));

		pushRange = new PureAllinOrFoldStrategy(HoleRange.fromString("22+,A2+,K2+,Q2s+,Q6o+,J7s+,J9o+,T8s+,98s"));
		callRange = new PureAllinOrFoldStrategy(HoleRange.fromString("22+,A2+,K8o+,K5s+,QTo+,Q9s+,JTs"));
		Assert.assertEquals(callRange, Solver.optimalBBStrategy(pushRange, 1000, 100));

		pushRange = new PureAllinOrFoldStrategy(HoleRange.fromString("*"));
		callRange = new PureAllinOrFoldStrategy(HoleRange.fromString("22+,A2+,K2+,Q2+,J3o+,J2s+,T6o+,T3s+,97o+,95s+,87o,86s+,76s"));
		Assert.assertEquals(callRange, Solver.optimalBBStrategy(pushRange, 1000, 100));

		pushRange = new PureAllinOrFoldStrategy(HoleRange.fromString("22+,A2+,K2+,Q3o+,Q2s+,J6o+,J3s+,T7o+,T6s+,98o,97s+,87s"));
		callRange = new PureAllinOrFoldStrategy(HoleRange.fromString("*"));
		Assert.assertEquals(pushRange, Solver.optimalSBStrategy(callRange, 1000, 100));
	}

}
