package com.sebster.poker.odds;

import java.util.Arrays;

import net.jcip.annotations.Immutable;

@Immutable
public final class BasicOdds extends Odds {

	private final int[] nWaySplits;

	private final int total;

	public BasicOdds(final int[] nWaySplits) {
		this.nWaySplits = nWaySplits;
		total = super.getTotal();
	}

	public BasicOdds(final Odds odds) {
		int total = 0;
		nWaySplits = new int[odds.getMaxN() + 1];
		for (int n = 0; n < nWaySplits.length; n++) {
			nWaySplits[n] = odds.getNWaySplits(n);
			total += nWaySplits[n];
		}
		this.total = total;
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
