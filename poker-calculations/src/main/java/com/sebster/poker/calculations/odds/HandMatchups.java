package com.sebster.poker.calculations.odds;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashSet;
import java.util.Set;

import com.sebster.math.rational.Rational;
import com.sebster.poker.HoleCategory;
import com.sebster.poker.holdem.odds.TwoPlayerPreFlopHoleCategoryOddsDB;
import com.sebster.poker.odds.Odds;
import com.sebster.util.collections.ImmutablePair;
import com.sebster.util.collections.Pair;

public class HandMatchups {

	private static interface MatchupFilter {
		boolean accept(final HoleCategory hc1, final HoleCategory hc2);
	}

	public static class Result {
		public final Rational minEquity;
		public final Set<Pair<HoleCategory, HoleCategory>> minMatchups;
		public final Rational maxEquity;
		public final Set<Pair<HoleCategory, HoleCategory>> maxMatchups;
		public final Rational avgEquity;

		public Result(Rational minEquity, Set<Pair<HoleCategory, HoleCategory>> minMatchups, Rational maxEquity, Set<Pair<HoleCategory, HoleCategory>> maxMatchups, Rational avgEquity) {
			this.minEquity = minEquity;
			this.minMatchups = minMatchups;
			this.maxEquity = maxEquity;
			this.maxMatchups = maxMatchups;
			this.avgEquity = avgEquity;
		}

	}

	private static Result calculateMatchupStats(final MatchupFilter filter) {
		Rational maxEquity = Rational.ZERO, minEquity = Rational.ONE, totEquity = Rational.ZERO, totWeight = Rational.ZERO;
		Set<Pair<HoleCategory, HoleCategory>> minMatchups = new HashSet<Pair<HoleCategory,HoleCategory>>(), maxMatchups = new HashSet<Pair<HoleCategory,HoleCategory>>();
		for (final HoleCategory hc1 : HoleCategory.values()) {
			for (final HoleCategory hc2 : HoleCategory.values()) {
				if (filter.accept(hc1, hc2)) {
					// 2 overcards vs 2 undercards
					final Odds odds = TwoPlayerPreFlopHoleCategoryOddsDB.getInstance().getOdds(hc1, hc2);
					final Rational equity = odds.getEquity();
					if (equity.compareTo(maxEquity) > 0) {
						maxEquity = equity;
						maxMatchups.add(new ImmutablePair<HoleCategory, HoleCategory>(hc1, hc2));
					}
					if (equity.compareTo(minEquity) < 0) {
						minEquity = equity;
						minMatchups.add(new ImmutablePair<HoleCategory, HoleCategory>(hc1, hc2));
					}
					final int weight1 = hc1.getSize();
					final int weight2 = hc2.getSize();
					final int weight = weight1 * weight2;

					totEquity = totEquity.add(equity.multiply(weight));
					totWeight = totWeight.add(weight);
				}
			}
		}
		return new Result(minEquity, minMatchups, maxEquity, maxMatchups, totEquity.divide(totWeight));
	}

	public static Result getOdds2OverVs2Under() {
		return calculateMatchupStats(new MatchupFilter() {
			@Override
			public boolean accept(final HoleCategory hc1, final HoleCategory hc2) {
				return hc1.getLowRank().getValue() > hc2.getHighRank().getValue() && !hc1.isPair() && !hc2.isPair();
			}
		});
	}

	public static Result getOddsOverUnderVs2Between() {
		return calculateMatchupStats(new MatchupFilter() {
			@Override
			public boolean accept(final HoleCategory hc1, final HoleCategory hc2) {
				return hc1.getHighRank().getValue() > hc2.getHighRank().getValue() && hc2.getLowRank().getValue() > hc1.getLowRank().getValue() && !hc1.isPair() && !hc2.isPair();
			}
		});
	}

	public static Result getOddsOverVs1Between() {
		return calculateMatchupStats(new MatchupFilter() {
			@Override
			public boolean accept(final HoleCategory hc1, final HoleCategory hc2) {
				return hc1.getHighRank().getValue() > hc2.getHighRank().getValue() && hc2.getHighRank().getValue() > hc1.getLowRank().getValue() && hc1.getLowRank().getValue() > hc2.getLowRank().getValue() && !hc1.isPair() && !hc2.isPair();
			}
		});
	}

	public static Result getOddsEqualUnpairedHands() {
		return calculateMatchupStats(new MatchupFilter() {
			@Override
			public boolean accept(final HoleCategory hc1, final HoleCategory hc2) {
				return hc1.getHighRank().getValue() == hc2.getHighRank().getValue() && hc1.getLowRank().getValue() == hc2.getLowRank().getValue() && !hc1.isPair() && !hc2.isPair();
			}
		});
	}

	public static Result getOddsEqualPairs() {
		return calculateMatchupStats(new MatchupFilter() {
			@Override
			public boolean accept(final HoleCategory hc1, final HoleCategory hc2) {
				return hc1.getHighRank().getValue() == hc2.getHighRank().getValue() && hc1.isPair() && hc2.isPair();
			}
		});
	}

	public static Result getOddsPairVs2Over() {
		return calculateMatchupStats(new MatchupFilter() {
			@Override
			public boolean accept(final HoleCategory hc1, final HoleCategory hc2) {
				return hc1.getHighRank().getValue() < hc2.getLowRank().getValue() && hc1.isPair() && !hc2.isPair();
			}
		});
	}

	public static Result getOddsPairVs1Over1Under() {
		return calculateMatchupStats(new MatchupFilter() {
			@Override
			public boolean accept(final HoleCategory hc1, final HoleCategory hc2) {
				return hc1.getHighRank().getValue() < hc2.getHighRank().getValue() && hc1.getHighRank().getValue() > hc2.getLowRank().getValue() && hc1.isPair() && !hc2.isPair();
			}
		});
	}

	public static Result getOddsPairVsDominatedLowCard() {
		return calculateMatchupStats(new MatchupFilter() {
			@Override
			public boolean accept(final HoleCategory hc1, final HoleCategory hc2) {
				return hc1.getHighRank().getValue() < hc2.getHighRank().getValue() && hc1.getHighRank().getValue() == hc2.getLowRank().getValue() && hc1.isPair() && !hc2.isPair();
			}
		});
	}

	public static Result getOddsPairVsDominatedHighCard() {
		return calculateMatchupStats(new MatchupFilter() {
			@Override
			public boolean accept(final HoleCategory hc1, final HoleCategory hc2) {
				return hc1.getHighRank().getValue() == hc2.getHighRank().getValue() && hc1.getHighRank().getValue() > hc2.getLowRank().getValue() && hc1.isPair() && !hc2.isPair();
			}
		});
	}

	public static Result getOddsPairVs2Under() {
		return calculateMatchupStats(new MatchupFilter() {
			@Override
			public boolean accept(final HoleCategory hc1, final HoleCategory hc2) {
				return hc1.getHighRank().getValue() > hc2.getHighRank().getValue() && hc1.isPair() && !hc2.isPair();
			}
		});
	}

	public static Result getOddsOverPairVsUnderPair() {
		return calculateMatchupStats(new MatchupFilter() {
			@Override
			public boolean accept(final HoleCategory hc1, final HoleCategory hc2) {
				return hc1.getHighRank().getValue() > hc2.getHighRank().getValue() && hc1.isPair() && hc2.isPair();
			}
		});
	}

	public static Result getOddsDominatedLowCard() {
		return calculateMatchupStats(new MatchupFilter() {
			@Override
			public boolean accept(final HoleCategory hc1, final HoleCategory hc2) {
				return hc1.getHighRank().getValue() == hc2.getHighRank().getValue() && hc1.getLowRank().getValue() > hc2.getLowRank().getValue() && !hc1.isPair() && !hc2.isPair();
			}
		});
	}

	public static Result getOddsDominatedHighCard() {
		return calculateMatchupStats(new MatchupFilter() {
			@Override
			public boolean accept(final HoleCategory hc1, final HoleCategory hc2) {
				return hc1.getHighRank().getValue() > hc2.getHighRank().getValue() && hc1.getLowRank().getValue() == hc2.getLowRank().getValue() && !hc1.isPair() && !hc2.isPair();
			}
		});
	}

	public static Result getOddsLowCardEqualsOtherHighCard() {
		return calculateMatchupStats(new MatchupFilter() {
			@Override
			public boolean accept(final HoleCategory hc1, final HoleCategory hc2) {
				return hc1.getLowRank().getValue() == hc2.getHighRank().getValue() && !hc1.isPair() && !hc2.isPair();
			}
		});
	}

	public static void main(final String[] args) {
		System.out.println("equal unpaired hands");
		printResults(getOddsEqualUnpairedHands());
		System.out.println("equal paired hands");
		printResults(getOddsEqualPairs());
		System.out.println("2 over vs 2 under");
		printResults(getOdds2OverVs2Under());
		System.out.println("1 over 1 under vs 2 in between");
		printResults(getOddsOverUnderVs2Between());
		System.out.println("1 over vs 1 in between");
		printResults(getOddsOverVs1Between());
		System.out.println("pair vs 2 over");
		printResults(getOddsPairVs2Over());
		System.out.println("pair vs 2 under");
		printResults(getOddsPairVs2Under());
		System.out.println("pair vs 1 over 1 under");
		printResults(getOddsPairVs1Over1Under());
		System.out.println("pair vs dominated low card");
		printResults(getOddsPairVsDominatedLowCard());
		System.out.println("pair vs dominated high card");
		printResults(getOddsPairVsDominatedHighCard());
		System.out.println("overpair vs underpair");
		printResults(getOddsOverPairVsUnderPair());
		System.out.println("dominated low card");
		printResults(getOddsDominatedLowCard());
		System.out.println("dominated high card");
		printResults(getOddsDominatedHighCard());
		System.out.println("low card equals other high card");
		printResults(getOddsLowCardEqualsOtherHighCard());
	}

	private static void printResults(final Result result) {
		System.out.println("Minimum equity: " + equityToString(result.minEquity) + " with holes " + result.minMatchups);
		System.out.println("Maximum equity: " + equityToString(result.maxEquity) + " with hole " + result.maxMatchups);
		System.out.println("Average equity: " + equityToString(result.avgEquity));
		System.out.println();
	}

	private static String equityToString(final Rational equity) {
		NumberFormat decimalFormat = new DecimalFormat("0.0");
		StringBuffer buffer = new StringBuffer();
		final double equityPercent = equity.multiply(100).doubleValue();
		buffer.append(decimalFormat.format(equityPercent));
		buffer.append("-");
		buffer.append(decimalFormat.format(100 - equityPercent));
		buffer.append(" (");
		buffer.append(decimalFormat.format(equityPercent / (100 - equityPercent)));
		buffer.append("-1)");
		return buffer.toString();
	}

}
