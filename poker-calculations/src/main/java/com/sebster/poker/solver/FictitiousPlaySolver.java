package com.sebster.poker.solver;

import com.sebster.poker.HoleCategory;
import com.sebster.poker.holdem.odds.TwoPlayerPreFlopHoleCategoryOddsDB;
import com.sebster.poker.holdem.tournament.icm.EquityCalculator;
import com.sebster.poker.holdem.tournament.icm.IndependentChipModel;
import com.sebster.poker.odds.Odds;

public class FictitiousPlaySolver {

	private static class EquityDeltas {
		private double sbFold;
		private double sbPushFold;
		private double sbPushCallWin;
		private double sbPushCallLose;
		private double bbFold;
		private double bbPushFold;
		private double bbPushCallWin;
		private double bbPushCallLose;
	}

	public static class ExpectedValues {

		private double sbEv;
		private double bbEv;

		public ExpectedValues(double sbEv, double bbEv) {
			this.sbEv = sbEv;
			this.bbEv = bbEv;
		}

		public double getSBEv() {
			return sbEv;
		}

		public double getBBEv() {
			return bbEv;
		}
		
	}

	public static ExpectedValues computeExpectedValues(final double[] sbStrategy, final double[] bbStrategy, final EquityDeltas equityDeltas) {
		final TwoPlayerPreFlopHoleCategoryOddsDB db = TwoPlayerPreFlopHoleCategoryOddsDB.getInstance();
		double sbEv = 0, bbEv = 0;
		for (int i = 0; i < 169; i++) {
			final HoleCategory bbHoleCategory = HoleCategory.values()[i];
			for (int j = 0; j < 169; j++) {
				final HoleCategory sbHoleCategory = HoleCategory.values()[j];
				final double sbPushFreq = sbStrategy[j];
				final double bbCallFreq = bbStrategy[i];
				final Odds odds = db.getOdds(sbHoleCategory, bbHoleCategory);

				// sb fold EV: (1 - sbPushFreq) * -sb
				double sbEqDelta = (1 - sbPushFreq) * equityDeltas.sbFold;
				double bbEqDelta = (1 - sbPushFreq) * equityDeltas.bbFold;
				// sb push, bb fold EV: sbPushFreq * (1 - bbCallFreq) * +bb
				sbEqDelta += sbPushFreq * (1 - bbCallFreq) * equityDeltas.sbPushFold;
				bbEqDelta += sbPushFreq * (1 - bbCallFreq) * equityDeltas.bbPushFold;
				// sb push, bb call EV: sbPushFreq * bbCallFreq * (p_win -
				// p_loss) * effectiveStacks
				sbEqDelta += sbPushFreq * bbCallFreq * (odds.getWins() * equityDeltas.sbPushCallWin + odds.getLosses() * equityDeltas.sbPushCallLose) / odds.getTotal();
				bbEqDelta += sbPushFreq * bbCallFreq * (odds.getWins() * equityDeltas.bbPushCallWin + odds.getLosses() * equityDeltas.bbPushCallLose) / odds.getTotal();

				// Scale to probability of being dealt.
				final double hc1hc2Prob = db.getProbabilityAsDouble(sbHoleCategory, bbHoleCategory);
				sbEv += hc1hc2Prob * sbEqDelta;
				bbEv += hc1hc2Prob * bbEqDelta;
			}
		}
		return new ExpectedValues(sbEv, bbEv);
	}

	public static ExpectedValues getHeadsUpBigBlindBestResponse(final double[] bbBestResponse, final double[] sbStrategy, final EquityDeltas equityDeltas) {
		final TwoPlayerPreFlopHoleCategoryOddsDB db = TwoPlayerPreFlopHoleCategoryOddsDB.getInstance();
		double sbEv = 0, bbEv = 0;
		for (int j = 0; j < 169; j++) {
			final HoleCategory hc1 = HoleCategory.values()[j];
			final double hc1Prob = hc1.getProbabilityAsDouble();
			sbEv += hc1Prob * ((1 - sbStrategy[j]) * equityDeltas.sbFold + sbStrategy[j] * equityDeltas.sbPushFold);
			bbEv += hc1Prob * ((1 - sbStrategy[j]) * equityDeltas.bbFold + sbStrategy[j] * equityDeltas.bbPushFold);
		}
		for (int i = 0; i < 169; i++) {
			double sbEvDelta = 0, bbEvDelta = 0;
			final HoleCategory hc2 = HoleCategory.values()[i];
			for (int j = 0; j < 169; j++) {
				final HoleCategory hc1 = HoleCategory.values()[j];
				final Odds odds = db.getOdds(hc1, hc2);
				final double hc1hc2Prob = db.getProbabilityAsDouble(hc1, hc2);
				sbEvDelta += hc1hc2Prob * sbStrategy[j] * ((odds.getWins() * equityDeltas.sbPushCallWin + odds.getLosses() * equityDeltas.sbPushCallLose) / odds.getTotal() - equityDeltas.sbPushFold);
				bbEvDelta += hc1hc2Prob * sbStrategy[j] * ((odds.getWins() * equityDeltas.bbPushCallWin + odds.getLosses() * equityDeltas.bbPushCallLose) / odds.getTotal() - equityDeltas.bbPushFold);
			}
			if (bbEvDelta > 0) {
				bbBestResponse[i] = 1;
				sbEv += sbEvDelta;
				bbEv += bbEvDelta;
			} else {
				bbBestResponse[i] = 0;
			}
		}
		return new ExpectedValues(sbEv, bbEv);
	}

	public static ExpectedValues getHeadsUpSmallBlindBestResponse(final double[] sbBestResponse, final double[] bbStrategy, final EquityDeltas equityDeltas) {
		double sbEv = equityDeltas.sbFold, bbEv = equityDeltas.bbFold;
		final TwoPlayerPreFlopHoleCategoryOddsDB db = TwoPlayerPreFlopHoleCategoryOddsDB.getInstance();
		for (int i = 0; i < 169; i++) {
			final HoleCategory hc1 = HoleCategory.values()[i];
			double sbEvDelta = hc1.getProbabilityAsDouble() * (equityDeltas.sbPushFold - equityDeltas.sbFold);
			double bbEvDelta = hc1.getProbabilityAsDouble() * (equityDeltas.bbPushFold - equityDeltas.bbFold);
			for (int j = 0; j < 169; j++) {
				final HoleCategory hc2 = HoleCategory.values()[j];
				final double hc1hc2Prob = db.getProbabilityAsDouble(hc1, hc2);
				final Odds odds = db.getOdds(hc1, hc2);
				sbEvDelta += hc1hc2Prob * bbStrategy[j] * ((odds.getWins() * equityDeltas.sbPushCallWin + odds.getLosses() * equityDeltas.sbPushCallLose) / odds.getTotal() - equityDeltas.sbPushFold);
				bbEvDelta += hc1hc2Prob * bbStrategy[j] * ((odds.getWins() * equityDeltas.bbPushCallWin + odds.getLosses() * equityDeltas.bbPushCallLose) / odds.getTotal() - equityDeltas.bbPushFold);
			}
			if (sbEvDelta > 0) {
				sbBestResponse[i] = 1;
				sbEv += sbEvDelta;
				bbEv += bbEvDelta;
			} else {
				sbBestResponse[i] = 0;
			}
		}
		return new ExpectedValues(sbEv, bbEv);
	}

	public static String printStrategy(final double[] strategy) {
		final StringBuilder buffer = new StringBuilder("{");
		for (int i = 0; i < 169; i++) {
			if (strategy[i] > 0.01) {
				buffer.append(' ');
				buffer.append(HoleCategory.values()[i]);
				buffer.append('=');
				buffer.append(strategy[i] > 0.99 ? "1" : String.valueOf(strategy[i]));
				buffer.append(',');
			}
		}
		buffer.setLength(buffer.length() - 1);
		buffer.append(" }");
		return buffer.toString();
	}

	public static double[][] fictitiousPlay(int iterations, int halfTime, EquityDeltas equityDeltas) {

		int n = 1;
		final double[] bbStrategy = new double[169], sbStrategy = new double[169];
		final double[] bbBestResponse = new double[169], sbBestResponse = new double[169];

		// Start with push aces only strategy.
		sbStrategy[HoleCategory.pAA.ordinal()] = 1;

		final double c = -Math.log(2) / halfTime;
		double sbLowerBound = -Double.MAX_VALUE, sbUpperBound = Double.MAX_VALUE;
		double bbLowerBound = -Double.MAX_VALUE, bbUpperBound = Double.MAX_VALUE;
		double w;
		do {
//			w = Math.exp(c * n);
			w = 1f / n;
			if (n % 100 == 0) {
				System.out.println("sb=" + printStrategy(sbStrategy));
				System.out.println("bb=" + printStrategy(bbStrategy));
				System.out.println("n=" + n + " w=" + w);
				System.out.println("SB NE EV lower bound=" + sbLowerBound);
				System.out.println("SB NE EV upper bound=" + sbUpperBound);
				System.out.println("BB NE EV lower bound=" + bbLowerBound);
				System.out.println("BB NE EV upper bound=" + bbUpperBound);
			}

			// Find BB best response to SB strategy.
			ExpectedValues ev1 = getHeadsUpBigBlindBestResponse(bbBestResponse, sbStrategy, equityDeltas);
			// SBEV(sbStrategy, bbBestResponse) == SBEV(sbStrategy, BR(sbStrategy)) <= SBEV(NE)
			// BBEV(sbStrategy, bbBestResponse) == BBEV(sbStrategy, BR(sbStrategy)) >= BBEV(NE) ?? (only for zero-sum?)
			sbLowerBound = Math.max(sbLowerBound, ev1.sbEv);
			bbUpperBound = Math.min(bbUpperBound, ev1.bbEv);

			// mix bb best response with bb belief
			for (int i = 0; i < 169; i++) {
				bbStrategy[i] = bbStrategy[i] * (1 - w) + bbBestResponse[i] * w;
			}

			// find optimal sb strategy to bb belief of optimal strategy
			ExpectedValues ev2 = getHeadsUpSmallBlindBestResponse(sbBestResponse, bbStrategy, equityDeltas);
			// EV(sbStrategy, bbStrategy) >= EV(BR(bbStrategy), bbStrategy) >= NE
			sbUpperBound = Math.min(sbUpperBound, ev2.sbEv);
			bbLowerBound = Math.max(bbLowerBound, ev2.bbEv);

			// mix sb best response with sb belief
			for (int i = 0; i < 169; i++) {
				sbStrategy[i] = sbStrategy[i] * (1 - w) + sbBestResponse[i] * w;
			}

			n++;
		} while (n <= iterations);

		return new double[][] { sbStrategy, bbStrategy };
	}

	// FIXME there has to be a better way
	public static double[] getNewEquities(final double[] payouts, final int[] newStacks) {
		final int sbPos = newStacks.length - 2;
		final int bbPos = newStacks.length - 1;
		if (newStacks[bbPos] == 0) {
			final int[] stacks = new int[newStacks.length - 1];
			System.arraycopy(newStacks, 0, stacks, 0, newStacks.length - 1);
			double[] equities = EquityCalculator.calculateEquitiesAsDoubles(stacks, payouts, IndependentChipModel.INSTANCE);
			double[] newEquities = new double[newStacks.length];
			System.arraycopy(equities, 0, newEquities, 0, newStacks.length - 1);
			newEquities[bbPos] = payouts.length >= newStacks.length ? payouts[newStacks.length - 1] : 0;
			return newEquities;
		} else if (newStacks[sbPos] == 0) {
			final int[] stacks = new int[newStacks.length - 1];
			if (newStacks.length > 2)
				System.arraycopy(newStacks, 0, stacks, 0, newStacks.length - 2);
			stacks[sbPos] = newStacks[bbPos];
			double[] equities = EquityCalculator.calculateEquitiesAsDoubles(stacks, payouts, IndependentChipModel.INSTANCE);
			double[] newEquities = new double[newStacks.length];
			System.arraycopy(equities, 0, newEquities, 0, newStacks.length - 2);
			newEquities[bbPos] = equities[sbPos];
			newEquities[sbPos] = payouts.length >= newStacks.length ? payouts[newStacks.length - 1] : 0;
			return newEquities;
		} else {
			return EquityCalculator.calculateEquitiesAsDoubles(newStacks, payouts, IndependentChipModel.INSTANCE);
		}
	}

	public static void main(final String[] args) {

		if (args.length == 0) {
			System.err.println("usage: " + FictitiousPlaySolver.class.getSimpleName() + " <n> <payout_1> ... <payout_n> <m> <stack_1> ... <stack_n> <bb> <iterations> <halftime>");
			System.exit(0);
		}

		// Parse arguments.
		final int numPayouts = Integer.parseInt(args[0]);
		final double[] payouts = new double[numPayouts];
		for (int i = 0; i < numPayouts; i++) {
			payouts[i] = Double.parseDouble(args[1 + i]);
		}
		final int numPlayers = Integer.parseInt(args[1 + numPayouts]);
		final int[] stacks = new int[numPlayers];
		for (int i = 0; i < numPlayers; i++) {
			stacks[i] = Integer.parseInt(args[1 + numPayouts + 1 + i]);
		}
		final int bigBlind = Integer.parseInt(args[1 + numPayouts + 1 + numPlayers]);
		final int smallBlind = bigBlind / 2;
		final int iterations = Integer.parseInt(args[1 + numPayouts + 1 + numPlayers + 1]);
		final int halfTime = Integer.parseInt(args[1 + numPayouts + 1 + numPlayers + 2]);

		// Calculate equity deltas.
		final double[] equities = EquityCalculator.calculateEquitiesAsDoubles(stacks, payouts, IndependentChipModel.INSTANCE);
		final int[] newStacks = new int[stacks.length];
		final int sbPos = stacks.length - 2, bbPos = stacks.length - 1;
		final int effectiveStack = Math.min(stacks[sbPos], stacks[bbPos]);
		double[] newEquities;
		final EquityDeltas equityDeltas = new EquityDeltas();

		// 1) fold
		System.arraycopy(stacks, 0, newStacks, 0, stacks.length);
		newStacks[sbPos] -= smallBlind;
		newStacks[bbPos] += smallBlind;
		newEquities = getNewEquities(payouts, newStacks);
		equityDeltas.sbFold = newEquities[sbPos] - equities[sbPos];
		equityDeltas.bbFold = newEquities[bbPos] - equities[bbPos];

		// 2) push-fold
		System.arraycopy(stacks, 0, newStacks, 0, stacks.length);
		newStacks[sbPos] += bigBlind;
		newStacks[bbPos] -= bigBlind;
		newEquities = getNewEquities(payouts, newStacks);
		equityDeltas.sbPushFold = newEquities[sbPos] - equities[sbPos];
		equityDeltas.bbPushFold = newEquities[bbPos] - equities[bbPos];

		// 2) push-call-win
		System.arraycopy(stacks, 0, newStacks, 0, stacks.length);
		newStacks[sbPos] += effectiveStack;
		newStacks[bbPos] -= effectiveStack;
		newEquities = getNewEquities(payouts, newStacks);
		equityDeltas.sbPushCallWin = newEquities[sbPos] - equities[sbPos];
		equityDeltas.bbPushCallWin = newEquities[bbPos] - equities[bbPos];

		// 3) push-call-lose
		System.arraycopy(stacks, 0, newStacks, 0, stacks.length);
		newStacks[sbPos] -= effectiveStack;
		newStacks[bbPos] += effectiveStack;
		newEquities = getNewEquities(payouts, newStacks);
		equityDeltas.sbPushCallLose = newEquities[sbPos] - equities[sbPos];
		equityDeltas.bbPushCallLose = newEquities[bbPos] - equities[bbPos];

		long t1 = System.currentTimeMillis();
		double[][] equilibria = fictitiousPlay(iterations, halfTime, equityDeltas);

		final double[] sbStrategy = equilibria[0], bbStrategy = equilibria[1];
		long t2 = System.currentTimeMillis();
		System.out.println("nash equilibrium computed with " + iterations + " iterations in " + ((t2 - t1) / 1000d) + " seconds");
		System.out.println("sb=" + printStrategy(sbStrategy));
		System.out.println("bb=" + printStrategy(bbStrategy));
		final ExpectedValues ev1 = getHeadsUpBigBlindBestResponse(new double[169], sbStrategy, equityDeltas);
		final ExpectedValues ev2 = getHeadsUpSmallBlindBestResponse(new double[169], bbStrategy, equityDeltas);
		System.out.println("SB EV against BR=" + ev1.getSBEv());
		System.out.println("BB EV of BR=" + ev1.getBBEv());
		System.out.println("BB EV against BR=" + ev2.getBBEv());
		System.out.println("SB EV of BR=" + ev2.getSBEv());
		final ExpectedValues ev3 = computeExpectedValues(sbStrategy, bbStrategy, equityDeltas);
		System.out.println("found strategies SB EV=" + ev3.getSBEv());
		System.out.println("found strategies BB EV=" + ev3.getBBEv());
	}

}
