package com.sebster.poker.odds;

import com.sebster.math.rational.Rational;

public abstract class Odds implements Comparable<Odds> {

	/**
	 * Get the number of n way splits. A zero-way split means a loss, a 1-way
	 * split means a win, a 2-way split to 10-way split means a split between
	 * the respective number of players.
	 * 
	 * @param n
	 *            the number of players in the split
	 * @return the number of n way splits
	 */
	public abstract int getNWaySplits(final int n);

	public abstract int getMaxN();

	public Rational getNWaySplitProbability(final int n) {
		return new Rational(getNWaySplits(n), getTotal());
	}

	public int getLosses() {
		return getNWaySplits(0);
	}

	public Rational getLossProbability() {
		return new Rational(getLosses(), getTotal());
	}

	public int getWins() {
		return getNWaySplits(1);
	}

	public Rational getWinProbability() {
		return new Rational(getWins(), getTotal());
	}

	public int getSplits() {
		return getTotal() - getWins() - getLosses();
	}

	public Rational getSplitProbability() {
		return new Rational(getSplits(), getTotal());
	}

	public int getTotal() {
		int total = 0;
		for (int n = getMaxN(); n >= 0; n--) {
			total += getNWaySplits(n);
		}
		return total;
	}

	public Rational getEquity() {
		Rational equity = Rational.ZERO;
		for (int n = getMaxN(); n > 0; n--) {
			equity.add(getNWaySplitProbability(n).divide(n));
		}
		return equity;
	}

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("[ ");
		final int maxN = getMaxN();
		for (int n = 0; n < maxN; n++) {
			builder.append(String.valueOf(getNWaySplits(n)));
			builder.append(", ");
		}
		builder.append(String.valueOf(getNWaySplits(maxN)));
		builder.append(" ]");
		return builder.toString();
	}

	@Override
	public int compareTo(final Odds odds) {
		return getEquity().compareTo(odds.getEquity());
	}

}
