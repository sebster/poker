package com.sebster.poker.calculations.tournament.icm;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import com.sebster.math.rational.Rational;
import com.sebster.poker.holdem.tournament.icm.ChipModel;
import com.sebster.poker.holdem.tournament.icm.EquityCalculator;
import com.sebster.poker.holdem.tournament.icm.IndependentChipModel;

/**
 * Calculate the showdown equity you need to call an all in for n players
 * remaining in an N player STT assuming all stacks are equal, for n=1..N.
 * 
 * @author sebster
 */
public class GapSizeCalculator {

	public static void main(final String[] args) throws Exception {
		final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("Payout structure (e.g. 5 3 2): ");
		final String structure = reader.readLine();
		final String[] prizeStrings = structure.split("\\s+");
		final Rational[] prizes = new Rational[prizeStrings.length];
		for (int i = 0; i < prizeStrings.length; i++) {
			prizes[i] = Rational.fromString(prizeStrings[i]);
		}
		System.out.println("Number of seats: ");
		final int seats = Integer.parseInt(reader.readLine());
		// System.out.println("Starting stacks: ");
		// int stacks = Integer.parseInt(reader.readLine());
		final int stacks = 10 * 9 * 8 * 7 * 6 * 5 * 4 * 3 * 2;
		final ChipModel chipModel = IndependentChipModel.INSTANCE;

		// equityAfter > equityBefore
		// win% * equityAfterW + (1 - win%) * equityAfterL > activityBefore
		// win% * (equityAfterW - equityAfterL) > equityBefore - equityAfterL
		// win% > (equityBefore - equityAfterL) / (equityAfterW - equityAfterL)
		for (int left = seats; left > 1; left--) {
			final Rational[] stacksBefore = new Rational[left], stacksAfter = new Rational[left - 1];
			stacksBefore[0] = new Rational(seats * stacks, left);
			for (int i = 1; i < left; i++) {
				stacksBefore[i] = stacksBefore[0];
				stacksAfter[i - 1] = stacksBefore[0];
			}
			stacksAfter[0] = stacksAfter[0].multiply(2);
			Rational[] equities = EquityCalculator.calculateEquities(stacksBefore, prizes, chipModel);
			Rational[] equitiesAfter = EquityCalculator.calculateEquities(stacksAfter, prizes, chipModel);
			Rational equityAfterL = left > prizes.length ? Rational.ZERO : prizes[left - 1];
			// System.out.println("Players left: " + left);
			// System.out.println("Before: " + equities[0]);
			// System.out.println("After:  " + equitiesAfter[0]);
			// double leakage = 2 * equities[0] - equitiesAfter[0];
			// System.out.println("Leakage: " + leakage);
			if (equitiesAfter[0] != equityAfterL) {
				System.out.printf("%2d %5.5f\n", left, (equities[0].subtract(equityAfterL)).divide(equitiesAfter[0].subtract(equityAfterL)).doubleValue());
			}
		}
	}
}
