package com.sebster.poker.solver;

import java.util.EnumSet;

import org.junit.Assert;
import org.junit.Test;

import com.sebster.poker.holdem.HoleCategory;
import com.sebster.poker.holdem.HoleRange;
import com.sebster.poker.holdem.odds.TwoPlayerPreFlopHoleCategoryOddsDB;

public class SolverTest {

	@Test
	public void testSolver() {
		EnumSet<HoleCategory> pushRange, callRange;
		TwoPlayerPreFlopHoleCategoryOddsDB.getInstance().getOdds(HoleCategory.pAA, HoleCategory.pAA);

		pushRange = HoleRange.fromString("66+,ATs+,AJo+");
		callRange = HoleRange.fromString("99+,AKo,AQs+");
		Assert.assertEquals(callRange, Solver.optimalBBStrategy(pushRange, 1000, 100));

		pushRange = HoleRange.fromString("44+,A7s+,A9o+,KJs+");
		callRange = HoleRange.fromString("66+,AJ+");
		Assert.assertEquals(callRange, Solver.optimalBBStrategy(pushRange, 1000, 100));

		pushRange = HoleRange.fromString("22+,A2s+,A3o+,KTs+,KJo+,QJs");
		callRange = HoleRange.fromString("33+,A8o+,A7s+,KQs");
		Assert.assertEquals(callRange, Solver.optimalBBStrategy(pushRange, 1000, 100));

		pushRange = HoleRange.fromString("22+,A2+,K2+,Q2s+,Q6o+,J7s+,J9o+,T8s+,98s");
		callRange = HoleRange.fromString("22+,A2+,K8o+,K5s+,QTo+,Q9s+,JTs");
		Assert.assertEquals(callRange, Solver.optimalBBStrategy(pushRange, 1000, 100));

		pushRange = HoleRange.fromString("*");
		callRange = HoleRange.fromString("22+,A2+,K2+,Q2+,J3o+,J2s+,T6o+,T3s+,97o+,95s+,87o,86s+,76s");
		Assert.assertEquals(callRange, Solver.optimalBBStrategy(pushRange, 1000, 100));

		pushRange = HoleRange.fromString("22+,A2+,K2+,Q3o+,Q2s+,J6o+,J3s+,T7o+,T6s+,98o,97s+,87s");
		callRange = HoleRange.fromString("*");
		Assert.assertEquals(pushRange, Solver.optimalSBStrategy(callRange, 1000, 100));
	}

}
