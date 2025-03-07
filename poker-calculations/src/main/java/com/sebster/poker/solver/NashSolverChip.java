package com.sebster.poker.solver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sebster.gametheory.nash.NashEquilibrium;
import com.sebster.math.matrix.MatrixImpl;
import com.sebster.math.rational.Rational;
import com.sebster.poker.HoleCategory;
import com.sebster.poker.holdem.AllinOrFoldStrategy;
import com.sebster.poker.holdem.MixedAllinOrFoldStrategy;
import com.sebster.poker.holdem.odds.TwoPlayerPreFlopHoleCategoryOddsDB;
import com.sebster.poker.odds.Odds;
import com.sebster.util.collections.Pair;

public class NashSolverChip {

	private static final Logger logger = LoggerFactory.getLogger(NashSolverChip.class);

	private final static int VERSION = 4;
	
	public static NashResult calculateNashEquilibrium(final int effectiveStack, final int bigBlind) {
		final MatrixImpl<Rational> E = new MatrixImpl<Rational>(1 + 169, 1 + 2 * 169, Rational.ZERO);
		E.set(0, 0, Rational.ONE);
		for (int i = 0; i < 169; i++) {
			E.set(i + 1, 0, Rational.ONE.opposite());
			E.set(i + 1, 2 * i + 1, Rational.ONE);
			E.set(i + 1, 2 * i + 2, Rational.ONE);
		}
		
		final MatrixImpl<Rational> A = new MatrixImpl<Rational>(1 + 169 * 2, 1 + 169 * 2, Rational.ZERO);
		final TwoPlayerPreFlopHoleCategoryOddsDB db = TwoPlayerPreFlopHoleCategoryOddsDB.getInstance();
		for (final HoleCategory hc1 : HoleCategory.values()) {
			for (final HoleCategory hc2 : HoleCategory.values()) {
				final int p1PushRow = 1 + 2 * hc1.ordinal();
				final int p1FoldRow = p1PushRow + 1;
				final int p2CallCol = 1 + 2 * hc2.ordinal();
				final int p2FoldCol = p2CallCol + 1;
				final Odds odds = db.getOdds(hc1, hc2);
				final Rational hc1Prob = hc1.getProbability();
				final Rational hc1hc2Prob = db.getProbability(hc1, hc2);
				
				A.set(p1PushRow, p2CallCol, hc1hc2Prob.times(odds.getWinProbability().minus(odds.getLossProbability()).times(effectiveStack)));
				A.set(p1PushRow, p2FoldCol, hc1hc2Prob.times(bigBlind));
				A.set(p1FoldRow, 0, hc1Prob.times(bigBlind).dividedBy(-2));
			}
		}
		
		final Pair<MatrixImpl<Rational>, MatrixImpl<Rational>> p = NashEquilibrium.solve(E, E, A, A.uminus());
		final MatrixImpl<Rational> x = p.getFirst(), y = p.getSecond();
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
		logger.info("running nash solver version {}", VERSION);
		logger.info("stacks={}", stack);
		logger.info("big blind={}", bb);
		final NashResult result = calculateNashEquilibrium(stack, bb);
		System.out.println("sb=" + result.getSbStrategy());
		System.out.println("bb=" + result.getBbStrategy());
		System.out.println("ev=" + result.getSbEV());
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
