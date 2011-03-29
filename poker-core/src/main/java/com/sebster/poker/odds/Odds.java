package com.sebster.poker.odds;

import org.apache.commons.lang.builder.HashCodeBuilder;

import com.sebster.math.rational.Rational;

// TODO add double methods in addition to rational methods for probability and equity
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

	public final Rational getNWaySplitProbability(final int n) {
		return new Rational(getNWaySplits(n), getTotal());
	}

	public final int getLosses() {
		return getNWaySplits(0);
	}

	public final Rational getLossProbability() {
		return new Rational(getLosses(), getTotal());
	}

	public final int getWins() {
		return getNWaySplits(1);
	}

	public final Rational getWinProbability() {
		return new Rational(getWins(), getTotal());
	}

	public final int getSplits() {
		return getTotal() - getWins() - getLosses();
	}

	public final Rational getSplitProbability() {
		return new Rational(getSplits(), getTotal());
	}

	public int getTotal() {
		int total = 0;
		for (int n = getMaxN(); n >= 0; n--) {
			total += getNWaySplits(n);
		}
		return total;
	}

	public final Rational getEquity() {
		Rational equity = Rational.ZERO;
		for (int n = getMaxN(); n > 0; n--) {
			equity = equity.plus(getNWaySplitProbability(n).dividedBy(n));
		}
		return equity;
	}

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append('[');
		final int maxN = getMaxN();
		for (int n = 0; n < maxN; n++) {
			builder.append(String.valueOf(getNWaySplits(n)));
			builder.append(", ");
		}
		builder.append(String.valueOf(getNWaySplits(maxN)));
		builder.append(']');
		return builder.toString();
	}

	@Override
	public final int compareTo(final Odds odds) {
		return getEquity().compareTo(odds.getEquity());
	}

	@Override
	public final int hashCode() {
		final HashCodeBuilder builder = new HashCodeBuilder();
		final int maxN = getMaxN();
		builder.append(maxN);
		for (int n = 0; n < maxN; n++) {
			builder.append(getNWaySplits(n));
		}
		return builder.toHashCode();
	}

	@Override
	public final boolean equals(final Object object) {
		if (this == object) {
			return true;
		}
		if (object instanceof Odds) {
			final Odds other = (Odds) object;
			final int maxN = getMaxN();
			if (maxN != other.getMaxN()) {
				return false;
			}
			for (int n = 0; n < maxN; n++) {
				if (getNWaySplits(n) != other.getNWaySplits(n)) {
					return false;
				}
			}
			return true;
		}
		return false;
	}

}
