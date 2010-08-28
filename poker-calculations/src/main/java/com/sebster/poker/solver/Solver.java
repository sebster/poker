package com.sebster.poker.solver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sebster.math.rational.Rational;
import com.sebster.poker.HoleCategory;
import com.sebster.poker.holdem.AllinOrFoldStrategy;
import com.sebster.poker.holdem.PureAllinOrFoldStrategy;
import com.sebster.poker.holdem.odds.TwoPlayerPreFlopHoleCategoryOddsDB;
import com.sebster.poker.odds.Odds;

public class Solver {

	private static final Logger logger = LoggerFactory.getLogger(Solver.class);

	public static Rational computeSBEV(final AllinOrFoldStrategy sbStrategy, final AllinOrFoldStrategy bbStrategy, final int effectiveStacks, final int bigBlind) {
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
				Rational equity = Rational.ONE.subtract(sbPushFreq).multiply(-bigBlind / 2);
				// sb push, bb fold EV: sbPushFreq * (1 - bbCallFreq) * +bb
				equity = equity.add(sbPushFreq.multiply(Rational.ONE.subtract(bbCallFreq)).multiply(bigBlind));
				// sb push, bb call EV: sbPushFreq * bbCallFreq * (p_win - p_loss) * effectiveStacks
				equity = equity.add(sbPushFreq.multiply(bbCallFreq).multiply(new Rational(odds.getWins() - odds.getLosses(), odds.getTotal()).multiply(effectiveStacks)));

				// Scale to probability of being dealt.
				final Rational gameProb = db.getProbability(sbHoleCategory, bbHoleCategory);
				result = result.add(gameProb.multiply(equity)).simplify();
			}
		}
		return result;
	}

	// TODO also return EV
	public static PureAllinOrFoldStrategy optimalBBStrategy(final AllinOrFoldStrategy sbStrategy, final int effectiveStacks, final int bigBlind) {
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
				coefficient = coefficient.add(gameProb.multiply(sbStrategy.getAllinFrequency(hc1)).multiply((new Rational(odds.getWins() - odds.getLosses(), odds.getTotal()).multiply(effectiveStacks).subtract(bigBlind)))).simplify();
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
	public static PureAllinOrFoldStrategy optimalSBStrategy(final AllinOrFoldStrategy bbStrategy, final int effectiveStacks, final int bigBlind) {
		final long t1 = System.currentTimeMillis();
		final TwoPlayerPreFlopHoleCategoryOddsDB db = TwoPlayerPreFlopHoleCategoryOddsDB.getInstance();
		final PureAllinOrFoldStrategy sbStrategy = new PureAllinOrFoldStrategy();
		for (int i = 0; i < 169; i++) {
			final HoleCategory hc1 = HoleCategory.values()[i];
			Rational coefficient = hc1.getProbability().multiply(bigBlind / 2 + bigBlind);
			for (int j = 0; j < 169; j++) {
				final HoleCategory hc2 = HoleCategory.values()[j];
				final Rational gameProb = db.getProbability(hc1, hc2);
				final Odds odds = db.getOdds(hc1, hc2);
				coefficient = coefficient.add(gameProb.multiply(bbStrategy.getAllinFrequency(hc2).multiply(new Rational(odds.getWins() - odds.getLosses(), odds.getTotal()).multiply(effectiveStacks).subtract(bigBlind)))).simplify();
			}
			if (coefficient.signum() > 0) {
				sbStrategy.addHoleCategory(hc1);
			}
		}
		final long t2 = System.currentTimeMillis();
		logger.debug("solved SB strategy in {} ms", t2 - t1);
		return sbStrategy;
	}

}
