package com.sebster.poker.solver;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sebster.gametheory.nash.NashEquilibrium;
import com.sebster.math.rational.Rational;
import com.sebster.math.rational.matrix.Matrix;
import com.sebster.poker.HoleCategory;
import com.sebster.poker.holdem.AllinOrFoldStrategy;
import com.sebster.poker.holdem.MixedAllinOrFoldStrategy;
import com.sebster.poker.holdem.odds.TwoPlayerPreFlopHoleCategoryOddsDB;
import com.sebster.poker.holdem.tournament.icm.EquityCalculator;
import com.sebster.poker.holdem.tournament.icm.IndependentChipModel;
import com.sebster.poker.odds.Odds;
import com.sebster.util.collections.Pair;

public class NashSolver {

	private static final Logger logger = LoggerFactory.getLogger(NashSolver.class);

	private final static int VERSION = 6;
	
	public static NashResult calculateNashEquilibrium(final Rational[] payouts, final Rational[] stacks, final Rational bigBlind) {
		
		final Rational[] equities = EquityCalculator.calculateEquities(stacks, payouts, IndependentChipModel.INSTANCE);
		
		final int sbPos = stacks.length - 2;
		final int bbPos = stacks.length - 1;
		final Rational effectiveStack = stacks[sbPos].min(stacks[bbPos]);
		final Rational smallBlind = bigBlind.divide(2);
		
		final Matrix<Rational> E = new Matrix<Rational>(1 + 169, 1 + 2 * 169, Rational.ZERO);
		E.set(0, 0, Rational.ONE);
		for (int i = 0; i < 169; i++) {
			E.set(i + 1, 0, Rational.ONE.negate());
			E.set(i + 1, 2 * i + 1, Rational.ONE);
			E.set(i + 1, 2 * i + 2, Rational.ONE);
		}
		
		final Matrix<Rational> A = new Matrix<Rational>(1 + 169 * 2, 1 + 169 * 2, Rational.ZERO);
		final Matrix<Rational> B = new Matrix<Rational>(1 + 169 * 2, 1 + 169 * 2, Rational.ZERO);
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
				final Rational chipDelta = odds.getWinProbability().subtract(odds.getLossProbability()).multiply(effectiveStack);
				newStacks[sbPos] = stacks[sbPos].add(chipDelta);
				newStacks[bbPos] = stacks[bbPos].subtract(chipDelta);
				newEquities = EquityCalculator.calculateEquities(newStacks, payouts, IndependentChipModel.INSTANCE);
				A.set(p1PushRow, p2CallCol, hc1hc2Prob.multiply(newEquities[sbPos].subtract(equities[sbPos])).simplify());
				B.set(p1PushRow, p2CallCol, hc1hc2Prob.multiply(newEquities[bbPos].subtract(equities[bbPos])).simplify());
//				logger.debug("old stacks: " + Arrays.toString(stacks));
//				logger.debug("new stacks: " + Arrays.toString(newStacks));
//				logger.debug("chip delta: " + chipDelta);
//				logger.debug("push " + hc1 + " call " + hc2 + " deltas " + A.get(p1PushRow, p2CallCol) + ", "  + B.get(p1PushRow, p2CallCol));
//				logger.debug("old equities: " + Arrays.toString(newEquities));
//				logger.debug("new equities: " + Arrays.toString(newEquities));
				
				// Push-fold.
				newStacks[sbPos] = stacks[sbPos].add(bigBlind);
				newStacks[bbPos] = stacks[bbPos].subtract(bigBlind);
				newEquities = EquityCalculator.calculateEquities(newStacks, payouts, IndependentChipModel.INSTANCE);
				A.set(p1PushRow, p2FoldCol, hc1hc2Prob.multiply(newEquities[sbPos].subtract(equities[sbPos])).simplify());
				B.set(p1PushRow, p2FoldCol, hc1hc2Prob.multiply(newEquities[bbPos].subtract(equities[bbPos])).simplify());

				// Fold.
				newStacks[sbPos] = stacks[sbPos].subtract(smallBlind);
				newStacks[bbPos] = stacks[bbPos].add(smallBlind);
				newEquities = EquityCalculator.calculateEquities(newStacks, payouts, IndependentChipModel.INSTANCE);
				A.set(p1FoldRow, 0, hc1Prob.multiply(newEquities[sbPos].subtract(equities[sbPos])).simplify());
				B.set(p1FoldRow, 0, hc1Prob.multiply(newEquities[bbPos].subtract(equities[bbPos])).simplify());
			}
		}
		
		final Pair<Matrix<Rational>, Matrix<Rational>> p = NashEquilibrium.solve(E, E, A, B);
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
		
		logger.info("running nash solver version {}", VERSION);
		logger.info("payouts={}", Arrays.toString(payouts));
		logger.info("stacks={}", Arrays.toString(stacks));
		logger.info("big blind={}", bigBlind);
		final NashResult result = calculateNashEquilibrium(payouts, stacks, bigBlind);
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
