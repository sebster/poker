package com.sebster.poker.solver;

import java.util.EnumSet;

import org.junit.Assert;
import org.junit.Test;

import com.sebster.poker.holdem.HoleCategory;
import com.sebster.poker.holdem.HoleRange;
import com.sebster.poker.holdem.odds.TwoPlayerPreFlopHoleCategoryOddsDB;

public class EvolveNETest {

	public static double[] test = new double[] { 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 1, 1, 1, 1, 1, 0, 1, 1, 1, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 };

	public static final double[] toDouble(final EnumSet<HoleCategory> strategy) {
		double[] result = new double[169];
		for (final HoleCategory hc : strategy) {
			result[hc.ordinal()] = 1;
		}
		return result;
	}

	public static final EnumSet<HoleCategory> toEnumSet(final double[] strategy) {
		EnumSet<HoleCategory> result = EnumSet.noneOf(HoleCategory.class);
		for (int i = 0; i < 169; i++) {
			if (strategy[i] > 0.5) {
				result.add(HoleCategory.values()[i]);
			}
		}
		return result;
	}

	@Test
	public void testSolver() {
		EnumSet<HoleCategory> pushRange, callRange;
		TwoPlayerPreFlopHoleCategoryOddsDB.getInstance().getOdds(HoleCategory.pAA, HoleCategory.pAA);

		pushRange = HoleRange.fromString("66+,ATs+,AJo+");
		callRange = HoleRange.fromString("99+,AKo,AQs+");
		Assert.assertEquals(callRange, toEnumSet(EvolveNE.optimalBBStrategy(toDouble(pushRange), 1000, 100)));

		pushRange = HoleRange.fromString("44+,A7s+,A9o+,KJs+");
		callRange = HoleRange.fromString("66+,AJ+");
		Assert.assertEquals(callRange, toEnumSet(EvolveNE.optimalBBStrategy(toDouble(pushRange), 1000, 100)));

		pushRange = HoleRange.fromString("22+,A2s+,A3o+,KTs+,KJo+,QJs");
		callRange = HoleRange.fromString("33+,A8o+,A7s+,KQs");
		Assert.assertEquals(callRange, toEnumSet(EvolveNE.optimalBBStrategy(toDouble(pushRange), 1000, 100)));

		pushRange = HoleRange.fromString("22+,A2+,K2+,Q2s+,Q6o+,J7s+,J9o+,T8s+,98s");
		callRange = HoleRange.fromString("22+,A2+,K8o+,K5s+,QTo+,Q9s+,JTs");
		Assert.assertEquals(callRange, toEnumSet(EvolveNE.optimalBBStrategy(toDouble(pushRange), 1000, 100)));

		pushRange = HoleRange.fromString("*");
		callRange = HoleRange.fromString("22+,A2+,K2+,Q2+,J3o+,J2s+,T6o+,T3s+,97o+,95s+,87o,86s+,76s");
		Assert.assertEquals(callRange, toEnumSet(EvolveNE.optimalBBStrategy(toDouble(pushRange), 1000, 100)));

		pushRange = HoleRange.fromString("22+,A2+,K2+,Q3o+,Q2s+,J6o+,J3s+,T7o+,T6s+,98o,97s+,87s");
		callRange = HoleRange.fromString("*");
		Assert.assertEquals(pushRange, toEnumSet(EvolveNE.optimalSBStrategy(toDouble(callRange), 1000, 100)));
		
		pushRange = toEnumSet(test);
		System.out.println("pushRange=" + HoleRange.toString(pushRange));
		callRange = Solver.optimalBBStrategy(pushRange, 5000, 600);
		System.out.println("callRange=" + HoleRange.toString(callRange));
		EnumSet<HoleCategory> pushRange2 = Solver.optimalSBStrategy(callRange, 5000, 600), extraIn1, extraIn2;
		extraIn1 = EnumSet.copyOf(pushRange);
		extraIn1.removeAll(pushRange2);
		extraIn2 = EnumSet.copyOf(pushRange2);
		extraIn2.removeAll(pushRange);
		System.out.println("extra in 1: " + extraIn1);
		System.out.println("extra in 2: " + extraIn2);
		System.out.println("EV 1: " + EvolveNE.getEV(toDouble(pushRange), 5000, 600));
		System.out.println("EV 2: " + EvolveNE.getEV(toDouble(pushRange2), 5000, 600));
		Assert.assertEquals(pushRange2, EnumSet.allOf(HoleCategory.class));
		
	}

}
