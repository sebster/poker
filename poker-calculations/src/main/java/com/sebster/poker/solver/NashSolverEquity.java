package com.sebster.poker.solver;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sebster.gametheory.nash.NashEquilibrium;
import com.sebster.math.matrix.MatrixImpl;
import com.sebster.math.rational.Rational;
import com.sebster.poker.HoleCategory;
import com.sebster.poker.holdem.AllinOrFoldStrategy;
import com.sebster.poker.holdem.MixedAllinOrFoldStrategy;
import com.sebster.poker.holdem.odds.TwoPlayerPreFlopHoleCategoryOddsDB;
import com.sebster.poker.holdem.tournament.icm.EquityCalculator;
import com.sebster.poker.holdem.tournament.icm.IndependentChipModel;
import com.sebster.poker.odds.Odds;
import com.sebster.util.collections.Pair;

public class NashSolverEquity {

	private static final Logger logger = LoggerFactory.getLogger(NashSolverEquity.class);

	private final static int VERSION = 10;
	
	public static NashResult calculateNashEquilibrium(final Rational[] payouts, final Rational[] stacks, final Rational bigBlind, final int threads) {
		
		final Rational[] equities = EquityCalculator.calculateEquities(stacks, payouts, IndependentChipModel.INSTANCE);
		
		final int sbPos = stacks.length - 2;
		final int bbPos = stacks.length - 1;
		final Rational effectiveStack = stacks[sbPos].min(stacks[bbPos]);
		final Rational smallBlind = bigBlind.dividedBy(2);
		
		final MatrixImpl<Rational> E = new MatrixImpl<Rational>(1 + 169, 1 + 2 * 169, Rational.ZERO);
		E.set(0, 0, Rational.ONE);
		for (int i = 0; i < 169; i++) {
			E.set(i + 1, 0, Rational.ONE.opposite());
			E.set(i + 1, 2 * i + 1, Rational.ONE);
			E.set(i + 1, 2 * i + 2, Rational.ONE);
		}
		
		// FIXME calculate equities only once outside loop.
		final MatrixImpl<Rational> A = new MatrixImpl<Rational>(1 + 169 * 2, 1 + 169 * 2, Rational.ZERO);
		final MatrixImpl<Rational> B = new MatrixImpl<Rational>(1 + 169 * 2, 1 + 169 * 2, Rational.ZERO);
		final Rational[] newStacks = stacks.clone();
		Rational[] newEquities;
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
		
				// Push-call.
				// FIXME is this correct?
				final Rational chipDelta = odds.getWinProbability().minus(odds.getLossProbability()).times(effectiveStack);
				newStacks[sbPos] = stacks[sbPos].plus(chipDelta);
				newStacks[bbPos] = stacks[bbPos].minus(chipDelta);
				newEquities = EquityCalculator.calculateEquities(newStacks, payouts, IndependentChipModel.INSTANCE);
				A.set(p1PushRow, p2CallCol, hc1hc2Prob.times(newEquities[sbPos].minus(equities[sbPos])));
				B.set(p1PushRow, p2CallCol, hc1hc2Prob.times(newEquities[bbPos].minus(equities[bbPos])));
				
				// Push-fold.
				newStacks[sbPos] = stacks[sbPos].plus(bigBlind);
				newStacks[bbPos] = stacks[bbPos].minus(bigBlind);
				newEquities = EquityCalculator.calculateEquities(newStacks, payouts, IndependentChipModel.INSTANCE);
				A.set(p1PushRow, p2FoldCol, hc1hc2Prob.times(newEquities[sbPos].minus(equities[sbPos])));
				B.set(p1PushRow, p2FoldCol, hc1hc2Prob.times(newEquities[bbPos].minus(equities[bbPos])));

				// Fold.
				newStacks[sbPos] = stacks[sbPos].minus(smallBlind);
				newStacks[bbPos] = stacks[bbPos].plus(smallBlind);
				newEquities = EquityCalculator.calculateEquities(newStacks, payouts, IndependentChipModel.INSTANCE);
				A.set(p1FoldRow, 0, hc1Prob.times(newEquities[sbPos].minus(equities[sbPos])));
				B.set(p1FoldRow, 0, hc1Prob.times(newEquities[bbPos].minus(equities[bbPos])));
			}
		}
		
		final Pair<MatrixImpl<Rational>, MatrixImpl<Rational>> p = NashEquilibrium.solve(E, E, A, B, threads);
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
		final int numPayouts = Integer.parseInt(args[0]);
		final Rational[] payouts = new Rational[numPayouts];
		for (int i = 0; i < numPayouts; i++) {
			payouts[i] = Rational.fromString(args[1 + i]);
		}
		final int numPlayers = Integer.parseInt(args[1 + numPayouts]);
		final Rational[] stacks = new Rational[numPlayers];
		for (int i = 0; i < numPlayers; i++) {
			stacks[i] = Rational.fromString(args[1 + numPayouts + 1 + i]);
		}
		final Rational bigBlind = Rational.fromString(args[1 + numPayouts + 1 + numPlayers]);
		final int threads = Integer.parseInt(args[1 + numPayouts + 1 + numPlayers + 1]);
		
		logger.info("running nash solver version {}", VERSION);
		logger.info("payouts={}", Arrays.toString(payouts));
		logger.info("stacks={}", Arrays.toString(stacks));
		logger.info("big blind={}", bigBlind);
		logger.info("threads={}", threads);
		final NashResult result = calculateNashEquilibrium(payouts, stacks, bigBlind, threads);
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
