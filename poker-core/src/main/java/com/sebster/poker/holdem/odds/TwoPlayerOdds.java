package com.sebster.poker.holdem.odds;

public class TwoPlayerOdds extends Odds {

	private final int wins;

	private final int losses;

	private final int splits;

	public TwoPlayerOdds(final int wins, final int losses, final int splits) {
		this.wins = wins;
		this.losses = losses;
		this.splits = splits;
	}

	@Override
	public int getMaxN() {
		return 2;
	}

	@Override
	public int getTotal() {
		return wins + losses + splits;
	}

	@Override
	public int getNWaySplits(final int n) {
		switch (n) {
		case 0:
			return losses;
		case 1:
			return wins;
		case 2:
			return splits;
		}
		throw new ArrayIndexOutOfBoundsException(n);
	}

	public TwoPlayerOdds reverse() {
		return new TwoPlayerOdds(losses, wins, splits);
	}

}
