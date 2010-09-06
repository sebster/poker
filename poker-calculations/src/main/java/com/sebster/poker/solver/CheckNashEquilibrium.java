package com.sebster.poker.solver;

import com.sebster.math.rational.Rational;
import com.sebster.poker.holdem.AllinOrFoldStrategy;
import com.sebster.poker.holdem.headsup.NashEquilibria;

public class CheckNashEquilibrium {

	public static void checkNashEquilibrium(final AllinOrFoldStrategy sbStrategy, final AllinOrFoldStrategy bbStrategy, final Rational r) {
		AllinOrFoldStrategy optBBStrategy = Solver.optimalBBStrategy(sbStrategy, r);
		AllinOrFoldStrategy optSBStrategy = Solver.optimalSBStrategy(bbStrategy, r);
		System.out.println("Effective stacks = " + r + ", big blind = " + 1 + ", R = " + r + " (" + r.decimalValue() + ")");
		final Rational nashEv = Solver.computeSBEV(sbStrategy, bbStrategy, r);
		final Rational sbEv = Solver.computeSBEV(sbStrategy, optBBStrategy, r);
		final Rational bbEv = Solver.computeSBEV(optSBStrategy, bbStrategy, r);
		System.out.println("Computed strategy EV = " + nashEv + " (" + nashEv.decimalValue() + ")");
		System.out.println("SB EV against opt BB = " + sbEv + " (" + sbEv.decimalValue() + ")");
		System.out.println("BB EV against opt SB = " + bbEv + " (" + bbEv.decimalValue() + ")");
	}
	
	public static void main(String[] args) {
		for (final Rational r : NashEquilibria.getRs()) {
			checkNashEquilibrium(NashEquilibria.getSBNashEquilibrium(r), NashEquilibria.getBBNashEqulibrium(r), r);
		}
	}

}
