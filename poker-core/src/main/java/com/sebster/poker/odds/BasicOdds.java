package com.sebster.poker.odds;

import java.util.Arrays;


public class BasicOdds extends Odds {

	private final int[] nWaySplits;

	private final int total;

	public BasicOdds(final int[] nWaySplits) {
		this.nWaySplits = nWaySplits;
		total = super.getTotal();
	}

	@Override
	public int getMaxN() {
		return nWaySplits.length - 1;
	}

	@Override
	public int getNWaySplits(final int n) {
		return nWaySplits[n];
	}

	@Override
	public int getTotal() {
		return total;
	}

	@Override
	public String toString() {
		return Arrays.toString(nWaySplits);
	}

}
