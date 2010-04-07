package com.sebster.poker.solver;

import java.util.EnumSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sebster.poker.holdem.HoleCategory;
import com.sebster.poker.holdem.odds.TwoPlayerPreFlopHoleCategoryOddsDB;
import com.sebster.poker.odds.Odds;

public class Solver {

	private static final Logger logger = LoggerFactory.getLogger(Solver.class);

	/**
	 * minimize:
	 * 
	 * sum_h2 { sum_h1 p(h1 & h2) * ([ p_w(h1,h2) - p_l(h1,h2) ] * ES) - BB) *
	 * f(h1) } g(h2)
	 */
	public static EnumSet<HoleCategory> optimalBBStrategy(EnumSet<HoleCategory> sbStrategy, int effectiveStacks, int bigBlind) {
		final TwoPlayerPreFlopHoleCategoryOddsDB db = TwoPlayerPreFlopHoleCategoryOddsDB.getInstance();
		final long t1 = System.currentTimeMillis();
		EnumSet<HoleCategory> bbStrategy = EnumSet.noneOf(HoleCategory.class);
		for (int i = 0; i < 169; i++) {
			double coefficient = 0;
			final HoleCategory hc2 = HoleCategory.values()[i];
			for (int j = 0; j < 169; j++) {
				final HoleCategory hc1 = HoleCategory.values()[j];
				if (sbStrategy.contains(hc1)) {
					final Odds odds = db.getOdds(hc1, hc2);
					final double prob = db.getProbability(hc1, hc2);
					coefficient += prob * ((odds.getWinProbability() - odds.getLossProbability()) * effectiveStacks - bigBlind);
				}
			}
			if (coefficient < 0) {
				bbStrategy.add(hc2);
			}
		}
		final long t2 = System.currentTimeMillis();
		logger.debug("solved BB strategy in {} ms", t2 - t1);
		return bbStrategy;
	}

	public static EnumSet<HoleCategory> optimalSBStrategy(EnumSet<HoleCategory> bbStrategy, int effectiveStacks, int bigBlind) {
		final TwoPlayerPreFlopHoleCategoryOddsDB db = TwoPlayerPreFlopHoleCategoryOddsDB.getInstance();
		final long t1 = System.currentTimeMillis();
		EnumSet<HoleCategory> sbStrategy = EnumSet.noneOf(HoleCategory.class);
		for (int i = 0; i < 169; i++) {
			final HoleCategory hc1 = HoleCategory.values()[i];
			double coefficient = -hc1.getProbability() * bigBlind / 2;
			for (int j = 0; j < 169; j++) {
				final HoleCategory hc2 = HoleCategory.values()[j];
				final double prob = db.getProbability(hc1, hc2);
				if (bbStrategy.contains(hc2)) {
					final Odds odds = db.getOdds(hc1, hc2);
					coefficient += prob * ((odds.getLossProbability() - odds.getWinProbability()) * effectiveStacks);
				} else {
					coefficient += prob * -bigBlind;
				}
			}
			if (coefficient < 0) {
				sbStrategy.add(hc1);
			}
		}
		final long t2 = System.currentTimeMillis();
		logger.debug("solved SB strategy in {} ms", t2 - t1);
		return sbStrategy;
	}

}
