package com.sebster.poker.solver;

import com.sebster.gametheory.nash.NashEquilibrium;
import com.sebster.math.rational.Rational;
import com.sebster.math.rational.matrix.Matrix;
import com.sebster.poker.HoleCategory;
import com.sebster.poker.holdem.AllinOrFoldStrategy;
import com.sebster.poker.holdem.MixedAllinOrFoldStrategy;
import com.sebster.poker.holdem.odds.TwoPlayerPreFlopHoleCategoryOddsDB;
import com.sebster.poker.odds.Odds;
import com.sebster.util.collections.Pair;

public class NashSolver {

	public static NashResult calculateNashEquilibrium(final int effectiveStack, final int smallBlind, final int bigBlind) {
		final Matrix<Rational> E = new Matrix<Rational>(1 + 169, 1 + 2 * 169, Rational.ZERO);
		E.set(0, 0, Rational.ONE);
		for (int i = 0; i < 169; i++) {
			E.set(i + 1, 0, Rational.ONE.negate());
			E.set(i + 1, 2 * i + 1, Rational.ONE);
			E.set(i + 1, 2 * i + 2, Rational.ONE);
		}
		
		final Matrix<Rational> A = new Matrix<Rational>(1 + 169 * 2, 1 + 169 * 2, Rational.ZERO);
		final TwoPlayerPreFlopHoleCategoryOddsDB db = TwoPlayerPreFlopHoleCategoryOddsDB.getInstance();
		for (final HoleCategory hc1 : HoleCategory.values()) {
			for (final HoleCategory hc2 : HoleCategory.values()) {
				final int p1PushRow = 1 + 2 * hc1.ordinal();
				final int p1FoldRow = p1PushRow + 1;
				final int p2CallCol = 1 + 2 * hc2.ordinal();
				final int p2FoldCol = p2CallCol + 1;
				final Odds odds = db.getOdds(hc1, hc2);
				
				A.set(p1PushRow, p2CallCol, odds.getWinProbability().subtract(odds.getLossProbability()).multiply(effectiveStack).simplify());
				A.set(p1PushRow, p2FoldCol, new Rational(bigBlind));
				A.set(p1FoldRow, 0, new Rational(-smallBlind));
			}
		}
		
		final Pair<Matrix<Rational>, Matrix<Rational>> p = NashEquilibrium.solve(E, E, A, A.uminus());
		final Matrix<Rational> x = p.getFirst(), y = p.getSecond();
		final MixedAllinOrFoldStrategy p1Strategy = new MixedAllinOrFoldStrategy();
		final MixedAllinOrFoldStrategy p2Strategy = new MixedAllinOrFoldStrategy();
		for (final HoleCategory hc : HoleCategory.values()) {
			p1Strategy.putHoleCategory(hc, x.get(1 + hc.ordinal() * 2, 0));
			p2Strategy.putHoleCategory(hc, y.get(1 + hc.ordinal() * 2, 0));
		}
		
		final Rational sbEv = x.transpose().times(A).times(y).get(0, 0);
		return new NashResult(p1Strategy, p2Strategy, sbEv);
	}

	public static void main(final String[] args) {
		final int stack = Integer.parseInt(args[0]);
		final int bb = Integer.parseInt(args[1]);
		System.out.println("effective stacks = " + stack + " big blind = " + bb);
		final NashResult result = calculateNashEquilibrium(stack, bb / 2, bb);
		System.out.println("sb = " + result.getSbStrategy());
		System.out.println("bb = " + result.getBbStrategy());
		System.out.println("sb ev = " + result.getSbEV());
	}

	public static final class NashResult {
		
		private final AllinOrFoldStrategy sbStrategy;
		
		private final AllinOrFoldStrategy bbStrategy;
		
		private final Rational sbEV;
		
		private NashResult(final AllinOrFoldStrategy sbStrategy, final AllinOrFoldStrategy bbStrategy, final Rational sbEV) {
			this.sbStrategy = sbStrategy;
			this.bbStrategy = bbStrategy;
			this.sbEV = sbEV;
		}
		
		public AllinOrFoldStrategy getSbStrategy() {
			return sbStrategy;
		}
		
		public AllinOrFoldStrategy getBbStrategy() {
			return bbStrategy;
		}
		
		public Rational getSbEV() {
			return sbEV;
		}
		
	}
	
}
