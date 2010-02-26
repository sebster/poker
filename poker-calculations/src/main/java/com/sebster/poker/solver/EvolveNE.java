package com.sebster.poker.solver;

import java.util.Arrays;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sebster.poker.holdem.HoleCategory;
import com.sebster.poker.holdem.odds.Odds;
import com.sebster.poker.holdem.odds.TwoPlayerPreFlopHoleCategoryOddsDB;

public class EvolveNE {

	private static final Logger logger = LoggerFactory.getLogger(EvolveNE.class);

	private static final int COUNT = 30;

	private static final int STACKS = 5000;

	private static final int BIGBLIND = 600;

	public static class Strategy implements Comparable<Strategy> {
		public double[] strategy;
		public double fitness;

		Strategy(final double[] strategy) {
			this.strategy = strategy;
			this.fitness = getEV(strategy, STACKS, BIGBLIND);
		}

		@Override
		public int compareTo(final Strategy other) {
			return Double.compare(fitness, other.fitness);
		}

		@Override
		public String toString() {
			StringBuffer buffer = new StringBuffer();
			for (int i = 0; i < 169; i++) {
				buffer.append(HoleCategory.values()[i].toString());
				buffer.append(" = ");
				buffer.append(String.valueOf(strategy[i]));
				buffer.append("\n");
			}
			buffer.append("expected value = " + fitness);
			buffer.append("\n");
			return buffer.toString();
		}
	}

	public static double getEV(final double[] sbStrategy, final int effectiveStacks, final int bigBlind) {
		final TwoPlayerPreFlopHoleCategoryOddsDB db = TwoPlayerPreFlopHoleCategoryOddsDB.getInstance();
		double result = 0;
		for (int i = 0; i < 169; i++) {
			final HoleCategory hc1 = HoleCategory.values()[i];
			result -= hc1.getProbability() * bigBlind / 2;
		}
		for (int i = 0; i < 169; i++) {
			double coefficient = 0;
			final HoleCategory hc2 = HoleCategory.values()[i];
			for (int j = 0; j < 169; j++) {
				final HoleCategory hc1 = HoleCategory.values()[j];
				final Odds odds = db.getOdds(hc1, hc2);
				final double prob = db.getProbability(hc1, hc2);
				result += sbStrategy[j] * prob * bigBlind;
				coefficient += sbStrategy[j] * prob * ((odds.getWinProbability() - odds.getLossProbability()) * effectiveStacks - bigBlind);
			}
			if (coefficient < 0) {
				result += coefficient;
			}
		}
		return result;
	}

	public static double[] optimalBBStrategy(final double[] sbStrategy, int effectiveStacks, int bigBlind) {
		final TwoPlayerPreFlopHoleCategoryOddsDB db = TwoPlayerPreFlopHoleCategoryOddsDB.getInstance();
		final double[] bbStrategy = new double[169];
		for (int i = 0; i < 169; i++) {
			double coefficient = 0;
			final HoleCategory hc2 = HoleCategory.values()[i];
			for (int j = 0; j < 169; j++) {
				final HoleCategory hc1 = HoleCategory.values()[j];
				final Odds odds = db.getOdds(hc1, hc2);
				final double prob = db.getProbability(hc1, hc2);
				coefficient += sbStrategy[j] * prob * ((odds.getWinProbability() - odds.getLossProbability()) * effectiveStacks - bigBlind);
			}
			if (coefficient < 0) {
				bbStrategy[i] = 1;
			}
		}
		return bbStrategy;
	}

	public static double[] optimalSBStrategy(final double[] bbStrategy, final int effectiveStacks, final int bigBlind) {
		final TwoPlayerPreFlopHoleCategoryOddsDB db = TwoPlayerPreFlopHoleCategoryOddsDB.getInstance();
		final double[] sbStrategy = new double[169];
		for (int i = 0; i < 169; i++) {
			final HoleCategory hc1 = HoleCategory.values()[i];
			double coefficient = hc1.getProbability() * bigBlind / 2;
			for (int j = 0; j < 169; j++) {
				final HoleCategory hc2 = HoleCategory.values()[j];
				final double prob = db.getProbability(hc1, hc2);
				final Odds odds = db.getOdds(hc1, hc2);
				coefficient += prob * (bigBlind + bbStrategy[j] * ((odds.getWinProbability() - odds.getLossProbability()) * effectiveStacks - bigBlind));
			}
			if (coefficient > 0) {
				sbStrategy[i] = 1;
			}
		}
		return sbStrategy;
	}

	public static double[] randomStrategy(final Random random) {
		final double[] strategy = new double[169];
		for (int i = 0; i < 169; i++) {
			strategy[i] = random.nextDouble();
		}
		return strategy;
	}

	public static Strategy crossover(final Random random, final Strategy strategy1, final Strategy strategy2) {
		final double[] strategy = new double[169];
		int i = random.nextInt(169);
		int j = random.nextInt(169);
		for (int k = 0; k < 169; k++) {
			strategy[k] = (i < j ? (i <= k && k < j) : (k < j || i <= k)) ? strategy1.strategy[k] : strategy2.strategy[k];
		}
		return new Strategy(strategy);
	}

	public static Strategy mutate(final Random random, final Strategy strategy1) {
		final double[] strategy = strategy1.strategy.clone();
		strategy[random.nextInt(169)] = random.nextDouble();
		return new Strategy(strategy);
	}

	public static void main(String[] args) {
		final Random random = new Random();
		Strategy[] population = new Strategy[COUNT * COUNT];
		logger.debug("initializing population");
		for (int i = 0; i < population.length; i++) {
			population[i] = new Strategy(randomStrategy(random));
		}
		double lastEv = 0;
		int mutations = COUNT / 2;
		while (true) {
			Arrays.sort(population);
			final double[] best = population[population.length - 1].strategy;
			final double ev = population[population.length - 1].fitness;
			System.out.println(population[population.length - 1]);
			if (lastEv == ev) {
				mutations = mutations * 2;
			} else {
				mutations = COUNT;
			}
			lastEv = ev;
			
			final double[] improved = optimalSBStrategy(optimalBBStrategy(best, STACKS, BIGBLIND), STACKS, BIGBLIND);
			boolean nash = true;
			for (int i = 0; i < 169; i++) {
				if (improved[i] != best[i]) {
					nash = false;
					break;
				}
			}
			if (nash) {
				logger.info("nash equilibrium found");
				System.exit(0);
			}
			int k = 0;
			logger.debug("doing crossover");
			for (int i = 0; i < COUNT; i++) {
				for (int j = 0; j < COUNT; j++) {
					population[k++] = crossover(random, population[population.length - 1 - i], population[population.length - 1 - 1]);
				}
			}
			logger.debug("doing mutations");
			for (int i = 0; i < mutations; i++) {
				int j = random.nextInt(population.length);
				population[j] = mutate(random, population[j]);
			}
			logger.debug("doing some squares");
			for (int i = 0; i < COUNT; i++) {
				int j = random.nextInt(population.length);
				double[] strategy = population[j].strategy;
				for (int l = 0; l < 169; l++) {
					strategy[l] = strategy[l] < 0.5 ? strategy[l] * strategy[l] : (1 - (1 - strategy[l]) * (1 - strategy[l]));
				}
				population[j] = new Strategy(strategy);
			}
		}
	}
}
