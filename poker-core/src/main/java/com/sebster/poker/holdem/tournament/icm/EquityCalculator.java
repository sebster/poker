package com.sebster.poker.holdem.tournament.icm;

import java.util.Arrays;

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
	public static double[][] calculateProbabilities(final int[] stacks, final int places, final ChipModel chipModel) {
		final int[] stacksCopy = stacks.clone();
		final int players = stacksCopy.length;
		final double[][] result = new double[players][places];
		int chips = 0;
		for (int i = 0; i < players; i++) {
			chips += stacksCopy[i];
		}
		for (int place = 0; place < places; place++) {
			for (int player = 0; player < players; player++) {
				result[player][place] = calculateProbability(stacksCopy, chips, player, place, 0, chipModel, result);
			}
		}
		return result;
	}

	private static double calculateProbability(final int[] stacks, final int chips, final int player, final int targetPlace, final int currentPlace, final ChipModel chipModel, final double[][] result) {
		final int players = stacks.length;
		if (currentPlace == targetPlace) {
			return chipModel.getWinProbability(stacks, chips, player);
		}
		double probability = 0;
		for (int winningPlayer = 0; winningPlayer < players; winningPlayer++) {
			final int winningStack = stacks[winningPlayer];
			stacks[winningPlayer] = 0;
			probability += result[winningPlayer][currentPlace] * calculateProbability(stacks, chips - winningStack, player, targetPlace, currentPlace + 1, chipModel, result);
			stacks[winningPlayer] = winningStack;
		}
		return probability;
	}

	public static double[] calculateEquities(final int[] stacks, final double[] payouts, final ChipModel chipModel) {
		final int players = stacks.length;
		final int places = Math.min(payouts.length, players);
		final double[] result = new double[players];
		final double[][] probabilities = calculateProbabilities(stacks, places, chipModel);
		for (int i = 0; i < players; i++) {
			for (int j = 0; j < places; j++) {
				result[i] += probabilities[i][j] * payouts[j];
			}
		}
		return result;
	}

	public static void main(final String[] args) {
		int[] stacks = { 4500, 2000, 1500, 1500, 500 };
		double[] payouts = { 50, 30, 20 };
		double[] equities = calculateEquities(stacks, payouts, IndependentChipModel.getInstance());
		System.out.println(Arrays.toString(equities));
		double[][] result = calculateProbabilities(stacks, 3, IndependentChipModel.getInstance());
		for (int i = 0; i < result.length; i++) {
			System.out.println(Arrays.toString(result[i]));
		}
	}

}
