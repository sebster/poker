package com.sebster.poker.holdem.tournament.icm;

import java.util.Arrays;

import com.sebster.math.rational.Rational;

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
		final Rational[] stacksCopy = stacks.clone();
		final int players = stacksCopy.length;
		final Rational[][] result = new Rational[players][places];
		Rational chips = Rational.ZERO;
		for (int i = 0; i < players; i++) {
			chips = chips.add(stacksCopy[i]);
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
			probability = probability.add(result[winningPlayer][currentPlace].multiply(calculateProbability(stacks, chips.subtract(winningStack), player, targetPlace, currentPlace + 1, chipModel, result)));
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
				result[i] = result[i].add(probabilities[i][j].multiply(payouts[j]));
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
