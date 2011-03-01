package com.sebster.poker.solver;

import java.util.Collections;
import java.util.NavigableMap;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sebster.math.rational.Rational;
import com.sebster.poker.HoleCategory;
import com.sebster.poker.holdem.AllinOrFoldStrategy;
import com.sebster.poker.holdem.PureAllinOrFoldStrategy;
import com.sebster.poker.holdem.odds.TwoPlayerPreFlopHoleCategoryOddsDB;
import com.sebster.poker.odds.Odds;

public class RationalBestResponseSolver {

	private static final Logger logger = LoggerFactory.getLogger(RationalBestResponseSolver.class);

	// BBEV = -SBEV
	public static Rational computeSBEV(final AllinOrFoldStrategy sbStrategy, final AllinOrFoldStrategy bbStrategy, final Rational r) {
		final TwoPlayerPreFlopHoleCategoryOddsDB db = TwoPlayerPreFlopHoleCategoryOddsDB.getInstance();
		Rational result = Rational.ZERO;
		for (int i = 0; i < 169; i++) {
			final HoleCategory bbHoleCategory = HoleCategory.values()[i];
			for (int j = 0; j < 169; j++) {
				final HoleCategory sbHoleCategory = HoleCategory.values()[j];
				final Rational sbPushFreq = sbStrategy.getAllinFrequency(sbHoleCategory);
				final Rational bbCallFreq = bbStrategy.getAllinFrequency(bbHoleCategory);
				final Odds odds = db.getOdds(sbHoleCategory, bbHoleCategory);

				// sb fold EV: (1 - sbPushFreq) * -sb
				Rational equity = Rational.ONE.minus(sbPushFreq).dividedBy(-2);
				// sb push, bb fold EV: sbPushFreq * (1 - bbCallFreq) * +bb
				equity = equity.plus(sbPushFreq.times(Rational.ONE.minus(bbCallFreq)));
				// sb push, bb call EV: sbPushFreq * bbCallFreq * (p_win - p_loss) * effectiveStacks
				equity = equity.plus(sbPushFreq.times(bbCallFreq).times(new Rational(odds.getWins() - odds.getLosses(), odds.getTotal()).times(r)));

				// Scale to probability of being dealt.
				final Rational gameProb = db.getProbability(sbHoleCategory, bbHoleCategory);
				result = result.plus(gameProb.times(equity));
			}
		}
		return result;
	}

	// TODO also return EV
	public static PureAllinOrFoldStrategy optimalBBStrategy(final AllinOrFoldStrategy sbStrategy, final Rational r) {
		final long t1 = System.currentTimeMillis();
		final TwoPlayerPreFlopHoleCategoryOddsDB db = TwoPlayerPreFlopHoleCategoryOddsDB.getInstance();
		final PureAllinOrFoldStrategy bbStrategy = new PureAllinOrFoldStrategy();
		for (int i = 0; i < 169; i++) {
			Rational coefficient = Rational.ZERO;
			final HoleCategory hc2 = HoleCategory.values()[i];
			for (int j = 0; j < 169; j++) {
				final HoleCategory hc1 = HoleCategory.values()[j];
				final Odds odds = db.getOdds(hc1, hc2);
				final Rational gameProb = db.getProbability(hc1, hc2);
				coefficient = coefficient.plus(gameProb.times(sbStrategy.getAllinFrequency(hc1)).times((new Rational(odds.getWins() - odds.getLosses(), odds.getTotal()).times(r /* effective stack */).minus(1 /* bb */))));
			}
			if (coefficient.signum() < 0) {
				bbStrategy.addHoleCategory(hc2);
			}
		}
		final long t2 = System.currentTimeMillis();
		logger.debug("solved BB strategy in {} ms", t2 - t1);
		return bbStrategy;
	}

	// TODO also return EV
	public static PureAllinOrFoldStrategy optimalSBStrategy(final AllinOrFoldStrategy bbStrategy, final Rational r) {
		final long t1 = System.currentTimeMillis();
		final TwoPlayerPreFlopHoleCategoryOddsDB db = TwoPlayerPreFlopHoleCategoryOddsDB.getInstance();
		final PureAllinOrFoldStrategy sbStrategy = new PureAllinOrFoldStrategy();
		for (int i = 0; i < 169; i++) {
			final HoleCategory hc1 = HoleCategory.values()[i];
			Rational coefficient = hc1.getProbability().times(new Rational(3, 2) /* bb + sb */);
			for (int j = 0; j < 169; j++) {
				final HoleCategory hc2 = HoleCategory.values()[j];
				final Rational gameProb = db.getProbability(hc1, hc2);
				final Odds odds = db.getOdds(hc1, hc2);
				coefficient = coefficient.plus(gameProb.times(bbStrategy.getAllinFrequency(hc2).times(new Rational(odds.getWins() - odds.getLosses(), odds.getTotal()).times(r /* effective stack */).minus(1 /* bb */))));
			}
			if (coefficient.signum() > 0) {
				sbStrategy.addHoleCategory(hc1);
			}
		}
		final long t2 = System.currentTimeMillis();
		logger.debug("solved SB strategy in {} ms", t2 - t1);
		return sbStrategy;
	}

	public static NavigableMap<HoleCategory, Rational> computeBBStrategyHandRanking(final AllinOrFoldStrategy sbStrategy, final AllinOrFoldStrategy bbStrategy, final Rational r) {
		final NavigableMap<HoleCategory, Rational> handRanking = new TreeMap<HoleCategory, Rational>(Collections.reverseOrder());
		final TwoPlayerPreFlopHoleCategoryOddsDB db = TwoPlayerPreFlopHoleCategoryOddsDB.getInstance();
		for (int i = 0; i < 169; i++) {
			Rational coefficient = Rational.ZERO;
			final HoleCategory hc2 = HoleCategory.values()[i];
			for (int j = 0; j < 169; j++) {
				final HoleCategory hc1 = HoleCategory.values()[j];
				final Odds odds = db.getOdds(hc1, hc2);
				final Rational gameProb = db.getProbability(hc1, hc2);
				coefficient = coefficient.plus(gameProb.times(sbStrategy.getAllinFrequency(hc1)).times((new Rational(odds.getWins() - odds.getLosses(), odds.getTotal()).times(r /* effective stack */).minus(1 /* bb */))));
			}
			handRanking.put(hc2, coefficient.opposite());
		}
		return handRanking;
	}

	public static NavigableMap<HoleCategory, Rational> computeSBStrategyHandRanking(final AllinOrFoldStrategy sbStrategy, final AllinOrFoldStrategy bbStrategy, final Rational r) {
		final NavigableMap<HoleCategory, Rational> handRanking = new TreeMap<HoleCategory, Rational>(Collections.reverseOrder());
		final TwoPlayerPreFlopHoleCategoryOddsDB db = TwoPlayerPreFlopHoleCategoryOddsDB.getInstance();
		for (int i = 0; i < 169; i++) {
			final HoleCategory hc1 = HoleCategory.values()[i];
			Rational coefficient = hc1.getProbability().times(new Rational(3, 2));
			for (int j = 0; j < 169; j++) {
				final HoleCategory hc2 = HoleCategory.values()[j];
				final Rational gameProb = Rational.ONE; // db.getProbability(hc1, hc2);
				final Odds odds = db.getOdds(hc1, hc2);
				coefficient = coefficient.plus(gameProb.times(bbStrategy.getAllinFrequency(hc2).times(new Rational(odds.getWins() - odds.getLosses(), odds.getTotal()).times(r /* effective stack */).minus(1 /* bb */))));
			}
			handRanking.put(hc1, coefficient);
		}
		return handRanking;
	}

}
