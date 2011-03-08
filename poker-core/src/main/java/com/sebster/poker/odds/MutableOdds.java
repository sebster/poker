package com.sebster.poker.odds;

import java.util.Arrays;

import com.sebster.util.Validate;

public class MutableOdds extends Odds {

	private final int[] nWaySplits;

	public MutableOdds(final int maxN) {
		nWaySplits = new int[maxN + 1];
	}

	public MutableOdds(final Odds odds) {
		this(odds.getMaxN());
		for (int n = 0; n < nWaySplits.length; n++) {
			nWaySplits[n] = odds.getNWaySplits(n);
		}
	}

	@Override
	public int getMaxN() {
		return nWaySplits.length - 1;
	}

	@Override
	public int getNWaySplits(final int n) {
		return nWaySplits[n];
	}

	public void setNWaySplits(final int n, final int count) {
		nWaySplits[n] = count;
	}

	public void addNWaySplits(final int n, final int count) {
		nWaySplits[n] += count;
	}

	public void subtractNWaySplits(final int n, final int count) {
		nWaySplits[n] -= count;
	}

	public void addOdds(final Odds odds) {
		Validate.isTrue(odds.getMaxN() == getMaxN(), "odds.getMaxN() != getMaxN()");
		for (int n = 0; n < nWaySplits.length; n++) {
			nWaySplits[n] += odds.getNWaySplits(n);
		}
	}

	public void substractOdds(final Odds odds) {
		Validate.isTrue(odds.getMaxN() == getMaxN(), "odds.getMaxN() != getMaxN()");
		for (int n = 0; n < nWaySplits.length; n++) {
			nWaySplits[n] -= odds.getNWaySplits(n);
		}
	}

	@Override
	public String toString() {
		return Arrays.toString(nWaySplits);
	}

}
