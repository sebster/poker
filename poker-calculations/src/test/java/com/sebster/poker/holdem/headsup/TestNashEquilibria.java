package com.sebster.poker.holdem.headsup;

import junit.framework.Assert;

import org.junit.Test;

import com.sebster.math.rational.Rational;
import com.sebster.poker.holdem.AllinOrFoldStrategy;
import com.sebster.poker.solver.Solver;

public class TestNashEquilibria {

	@Test
	public void testNashEquilibria() {
		for (final Rational r : NashEquilibria.getRs()) {
			final AllinOrFoldStrategy sbStrategy = NashEquilibria.getSBNashEquilibrium(r);
			final AllinOrFoldStrategy bbStrategy = NashEquilibria.getBBNashEqulibrium(r);
			final AllinOrFoldStrategy optBBStrategy = Solver.optimalBBStrategy(sbStrategy, r);
			final AllinOrFoldStrategy optSBStrategy = Solver.optimalSBStrategy(bbStrategy, r);
			System.out.println("Effective stacks = " + r + ", big blind = " + 1 + ", R = " + r + " (" + r.doubleValue() + ")");
			final Rational nashEv = NashEquilibria.getSBNashEquilibriumEV(r);
			final Rational computedEv = Solver.computeSBEV(sbStrategy, bbStrategy, r);
			final Rational optSBEv = Solver.computeSBEV(sbStrategy, optBBStrategy, r);
			final Rational optBBEv = Solver.computeSBEV(optSBStrategy, bbStrategy, r);
			System.out.println("Specified EV      = " + nashEv + " (" + nashEv.doubleValue() + ")");
			System.out.println("Computed EV       = " + computedEv + " (" + computedEv.doubleValue() + ")");
			System.out.println("EV against opt BB = " + optSBEv + " (" + optSBEv.doubleValue() + ")");
			System.out.println("EV against opt SB = " + optBBEv + " (" + optBBEv.doubleValue() + ")");
			Assert.assertEquals(nashEv, computedEv); // Verify registered EV is correct.
			Assert.assertEquals(nashEv, optSBEv);    // Verify registered EV is optimal against any SB strategy.
			Assert.assertEquals(nashEv, optBBEv);    // Verify registered EV is optimal against any BB strategy.
		}
	}

}
