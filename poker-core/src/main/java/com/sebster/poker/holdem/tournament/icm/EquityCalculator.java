package com.sebster.poker.holdem.tournament.icm;

import java.util.Arrays;

import com.sebster.math.rational.Rational;

// FIXME duplicate logic for doubles and rationals
public class EquityCalculator {

	private EquityCalculator() {
		// Utility class.
	}

	/**
	 * Calculate the probabilities of placing first, second, third, etc., given
	 * the player stack sizes.
	 * 
	 * @param stacks
	 *            the stacks of the players
	 * @param places
	 *            the number of places to calculate the probabilities for
	 * @return the probabilities for each player to reach each place
	 */
	public static Rational[][] calculateProbabilities(final Rational[] stacks, final int places, final ChipModel chipModel) {
		for (int i = 0; i < stacks.length; i++) {
			if (stacks[i] == null || stacks[i].signum() <= 0) {
				throw new IllegalArgumentException("invalid stack for player " + i + ": " + stacks[i]);
			}
		}
		final Rational[] stacksCopy = stacks.clone();
		final int players = stacksCopy.length;
		final Rational[][] result = new Rational[players][places];
		Rational chips = Rational.ZERO;
		for (int i = 0; i < players; i++) {
			chips = chips.plus(stacksCopy[i]);
		}
		for (int place = 0; place < places; place++) {
			for (int player = 0; player < players; player++) {
				result[player][place] = calculateProbability(stacksCopy, chips, player, place, 0, chipModel, result);
			}
		}
		return result;
	}

	private static Rational calculateProbability(final Rational[] stacks, final Rational chips, final int player, final int targetPlace, final int currentPlace, final ChipModel chipModel, final Rational[][] result) {
		final int players = stacks.length;
		if (currentPlace == targetPlace) {
			return chipModel.getWinProbability(stacks, chips, player);
		}
		Rational probability = Rational.ZERO;
		for (int winningPlayer = 0; winningPlayer < players; winningPlayer++) {
			final Rational winningStack = stacks[winningPlayer];
			stacks[winningPlayer] = Rational.ZERO;
			probability = probability.plus(result[winningPlayer][currentPlace].times(calculateProbability(stacks, chips.minus(winningStack), player, targetPlace, currentPlace + 1, chipModel, result)));
			stacks[winningPlayer] = winningStack;
		}
		return probability;
	}

	/**
	 * Calculate the probabilities of placing first, second, third, etc., given
	 * the player stack sizes.
	 * 
	 * @param stacks
	 *            the stacks of the players
	 * @param places
	 *            the number of places to calculate the probabilities for
	 * @return the probabilities for each player to reach each place
	 */
	public static double[][] calculateProbabilitiesAsDoubles(final int[] stacks, final int places, final ChipModel chipModel) {
		for (int i = 0; i < stacks.length; i++) {
			if (stacks[i] <= 0) {
				throw new IllegalArgumentException("invalid stack for player " + i + ": " + stacks[i]);
			}
		}
		final int[] stacksCopy = stacks.clone();
		final int players = stacksCopy.length;
		final double[][] result = new double[players][places];
		int chips = 0;
		for (int i = 0; i < players; i++) {
			chips += stacksCopy[i];
		}
		for (int place = 0; place < places; place++) {
			for (int player = 0; player < players; player++) {
				result[player][place] = calculateProbabilityAsDouble(stacksCopy, chips, player, place, 0, chipModel, result);
			}
		}
		return result;
	}

	private static double calculateProbabilityAsDouble(final int[] stacks, final int chips, final int player, final int targetPlace, final int currentPlace, final ChipModel chipModel, final double[][] result) {
		final int players = stacks.length;
		if (currentPlace == targetPlace) {
			return chipModel.getWinProbabilityAsDouble(stacks, chips, player);
		}
		double probability = 0;
		for (int winningPlayer = 0; winningPlayer < players; winningPlayer++) {
			final int winningStack = stacks[winningPlayer];
			stacks[winningPlayer] = 0;
			probability += result[winningPlayer][currentPlace] * calculateProbabilityAsDouble(stacks, chips - winningStack, player, targetPlace, currentPlace + 1, chipModel, result);
			stacks[winningPlayer] = winningStack;
		}
		return probability;
	}

	public static Rational[] calculateEquities(final Rational[] stacks, final Rational[] payouts, final ChipModel chipModel) {
		final int players = stacks.length;
		final int places = Math.min(payouts.length, players);
		final Rational[] result = new Rational[players];
		final Rational[][] probabilities = calculateProbabilities(stacks, places, chipModel);
		for (int i = 0; i < players; i++) {
			result[i] = Rational.ZERO;
			for (int j = 0; j < places; j++) {
				result[i] = result[i].plus(probabilities[i][j].times(payouts[j]));
			}
		}
		return result;
	}

	public static double[] calculateEquitiesAsDoubles(final int[] stacks, final double[] payouts, final ChipModel chipModel) {
		final int players = stacks.length;
		final int places = Math.min(payouts.length, players);
		final double[] result = new double[players];
		final double[][] probabilities = calculateProbabilitiesAsDoubles(stacks, places, chipModel);
		for (int i = 0; i < players; i++) {
			for (int j = 0; j < places; j++) {
				result[i] += probabilities[i][j] * payouts[j];
			}
		}
		return result;
	}

	public static void main(final String[] args) {
		final Rational[] stacks = { new Rational(4500), new Rational(2000), new Rational(1500), new Rational(1500), new Rational(500) };
		final Rational[] payouts = new Rational[] { new Rational(50), new Rational(30), new Rational(20) };
		Rational[] equities = calculateEquities(stacks, payouts, IndependentChipModel.INSTANCE);
		System.out.println(Arrays.toString(equities));
		Rational[][] result = calculateProbabilities(stacks, 3, IndependentChipModel.INSTANCE);
		for (int i = 0; i < result.length; i++) {
			System.out.println(Arrays.toString(result[i]));
		}
	}

}
