package com.sebster.poker.calculations.odds;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import com.sebster.poker.HoleCategory;
import com.sebster.poker.holdem.odds.TwoPlayerPreFlopHoleCategoryOddsDB;
import com.sebster.poker.odds.Odds;

public class HandMatchups {

	public static Object[] getOdds2OverVs2Under() {
		double maxEquity = 0, minEquity = 1, totEquity = 0, totWeight = 0;
		HoleCategory[] maxHoles = null, minHoles = null;
		for (final HoleCategory hc1 : HoleCategory.values()) {
			for (final HoleCategory hc2 : HoleCategory.values()) {
				if (hc1.getLowRank().getValue() > hc2.getHighRank().getValue() && !hc1.isPair() && !hc2.isPair()) {
					// 2 overcards vs 2 undercards
					final Odds odds = TwoPlayerPreFlopHoleCategoryOddsDB.getInstance().getOdds(hc1, hc2);
					final double equity = odds.getEquity();
					if (equity > maxEquity) {
						maxEquity = equity;
						maxHoles = new HoleCategory[] { hc1, hc2 };
					}
					if (equity < minEquity) {
						minEquity = equity;
						minHoles = new HoleCategory[] { hc1, hc2 };
					}
					final int weight1 = hc1.getSize();
					final int weight2 = hc2.getSize();
					final int weight = weight1 * weight2;

					totEquity += equity * weight;
					totWeight += weight;
				}
			}
		}
		return new Object[] { minEquity, minHoles, maxEquity, maxHoles, totEquity / totWeight };
	}

	public static Object[] getOddsOverUnderVs2Between() {
		double maxEquity = 0, minEquity = 1, totEquity = 0, totWeight = 0;
		HoleCategory[] maxHoles = null, minHoles = null;
		for (final HoleCategory hc1 : HoleCategory.values()) {
			for (final HoleCategory hc2 : HoleCategory.values()) {
				if (hc1.getHighRank().getValue() > hc2.getHighRank().getValue() && hc2.getLowRank().getValue() > hc1.getLowRank().getValue() && !hc1.isPair() && !hc2.isPair()) {
					// 1 over 1 under vs 2 in between.
					final Odds odds = TwoPlayerPreFlopHoleCategoryOddsDB.getInstance().getOdds(hc1, hc2);
					final double equity = odds.getEquity();
					if (equity > maxEquity) {
						maxEquity = equity;
						maxHoles = new HoleCategory[] { hc1, hc2 };
					}
					if (equity < minEquity) {
						minEquity = equity;
						minHoles = new HoleCategory[] { hc1, hc2 };
					}
					final int weight1 = hc1.getSize();
					final int weight2 = hc2.getSize();
					final int weight = weight1 * weight2;

					totEquity += equity * weight;
					totWeight += weight;
				}
			}
		}
		return new Object[] { minEquity, minHoles, maxEquity, maxHoles, totEquity / totWeight };
	}

	public static Object[] getOddsOverVs1Between() {
		double maxEquity = 0, minEquity = 1, totEquity = 0, totWeight = 0;
		HoleCategory[] maxHoles = null, minHoles = null;
		for (final HoleCategory hc1 : HoleCategory.values()) {
			for (final HoleCategory hc2 : HoleCategory.values()) {
				if (hc1.getHighRank().getValue() > hc2.getHighRank().getValue() && hc2.getHighRank().getValue() > hc1.getLowRank().getValue() && hc1.getLowRank().getValue() > hc2.getLowRank().getValue() && !hc1.isPair() && !hc2.isPair()) {
					// 1 over vs 1 in between.
					final Odds odds = TwoPlayerPreFlopHoleCategoryOddsDB.getInstance().getOdds(hc1, hc2);
					final double equity = odds.getEquity();
					if (equity > maxEquity) {
						maxEquity = equity;
						maxHoles = new HoleCategory[] { hc1, hc2 };
					}
					if (equity < minEquity) {
						minEquity = equity;
						minHoles = new HoleCategory[] { hc1, hc2 };
					}
					final int weight1 = hc1.getSize();
					final int weight2 = hc2.getSize();
					final int weight = weight1 * weight2;

					totEquity += equity * weight;
					totWeight += weight;
				}
			}
		}
		return new Object[] { minEquity, minHoles, maxEquity, maxHoles, totEquity / totWeight };
	}

	public static Object[] getOddsPairVs2Over() {
		double maxEquity = 0, minEquity = 1, totEquity = 0, totWeight = 0;
		HoleCategory[] maxHoles = null, minHoles = null;
		for (final HoleCategory hc1 : HoleCategory.values()) {
			for (final HoleCategory hc2 : HoleCategory.values()) {
				if (hc1.getHighRank().getValue() < hc2.getLowRank().getValue() && hc1.isPair() && !hc2.isPair()) {
					// 1 over vs 1 in between.
					final Odds odds = TwoPlayerPreFlopHoleCategoryOddsDB.getInstance().getOdds(hc1, hc2);
					final double equity = odds.getEquity();
					if (equity > maxEquity) {
						maxEquity = equity;
						maxHoles = new HoleCategory[] { hc1, hc2 };
					}
					if (equity < minEquity) {
						minEquity = equity;
						minHoles = new HoleCategory[] { hc1, hc2 };
					}
					final int weight1 = hc1.getSize();
					final int weight2 = hc2.getSize();
					final int weight = weight1 * weight2;

					totEquity += equity * weight;
					totWeight += weight;
				}
			}
		}
		return new Object[] { minEquity, minHoles, maxEquity, maxHoles, totEquity / totWeight };
	}

	public static Object[] getOddsPairVs1Over() {
		double maxEquity = 0, minEquity = 1, totEquity = 0, totWeight = 0;
		HoleCategory[] maxHoles = null, minHoles = null;
		for (final HoleCategory hc1 : HoleCategory.values()) {
			for (final HoleCategory hc2 : HoleCategory.values()) {
				if (hc1.getHighRank().getValue() < hc2.getHighRank().getValue() && hc1.getHighRank().getValue() >= hc2.getLowRank().getValue() && hc1.isPair() && !hc2.isPair()) {
					// 1 over vs 1 in between.
					final Odds odds = TwoPlayerPreFlopHoleCategoryOddsDB.getInstance().getOdds(hc1, hc2);
					final double equity = odds.getEquity();
					if (equity > maxEquity) {
						maxEquity = equity;
						maxHoles = new HoleCategory[] { hc1, hc2 };
					}
					if (equity < minEquity) {
						minEquity = equity;
						minHoles = new HoleCategory[] { hc1, hc2 };
					}
					final int weight1 = hc1.getSize();
					final int weight2 = hc2.getSize();
					final int weight = weight1 * weight2;

					totEquity += equity * weight;
					totWeight += weight;
				}
			}
		}
		return new Object[] { minEquity, minHoles, maxEquity, maxHoles, totEquity / totWeight };
	}

	public static Object[] getOddsPairVs2Under() {
		double maxEquity = 0, minEquity = 1, totEquity = 0, totWeight = 0;
		HoleCategory[] maxHoles = null, minHoles = null;
		for (final HoleCategory hc1 : HoleCategory.values()) {
			for (final HoleCategory hc2 : HoleCategory.values()) {
				if (hc1.getHighRank().getValue() > hc2.getHighRank().getValue() && hc1.isPair() && !hc2.isPair()) {
					// 1 over vs 1 in between.
					final Odds odds = TwoPlayerPreFlopHoleCategoryOddsDB.getInstance().getOdds(hc1, hc2);
					final double equity = odds.getEquity();
					if (equity > maxEquity) {
						maxEquity = equity;
						maxHoles = new HoleCategory[] { hc1, hc2 };
					}
					if (equity < minEquity) {
						minEquity = equity;
						minHoles = new HoleCategory[] { hc1, hc2 };
					}
					final int weight1 = hc1.getSize();
					final int weight2 = hc2.getSize();
					final int weight = weight1 * weight2;

					totEquity += equity * weight;
					totWeight += weight;
				}
			}
		}
		return new Object[] { minEquity, minHoles, maxEquity, maxHoles, totEquity / totWeight };
	}

	public static Object[] getOddsOverPairVsUnderPair() {
		double maxEquity = 0, minEquity = 1, totEquity = 0, totWeight = 0;
		HoleCategory[] maxHoles = null, minHoles = null;
		for (final HoleCategory hc1 : HoleCategory.values()) {
			for (final HoleCategory hc2 : HoleCategory.values()) {
				if (hc1.getHighRank().getValue() > hc2.getHighRank().getValue() && hc1.isPair() && hc2.isPair()) {
					// 1 over vs 1 in between.
					final Odds odds = TwoPlayerPreFlopHoleCategoryOddsDB.getInstance().getOdds(hc1, hc2);
					final double equity = odds.getEquity();
					if (equity > maxEquity) {
						maxEquity = equity;
						maxHoles = new HoleCategory[] { hc1, hc2 };
					}
					if (equity < minEquity) {
						minEquity = equity;
						minHoles = new HoleCategory[] { hc1, hc2 };
					}
					final int weight1 = hc1.getSize();
					final int weight2 = hc2.getSize();
					final int weight = weight1 * weight2;

					totEquity += equity * weight;
					totWeight += weight;
				}
			}
		}
		return new Object[] { minEquity, minHoles, maxEquity, maxHoles, totEquity / totWeight };
	}

	public static Object[] getOddsDominatedLowCard() {
		double maxEquity = 0, minEquity = 1, totEquity = 0, totWeight = 0;
		HoleCategory[] maxHoles = null, minHoles = null;
		for (final HoleCategory hc1 : HoleCategory.values()) {
			for (final HoleCategory hc2 : HoleCategory.values()) {
				if (hc1.getHighRank().getValue() == hc2.getHighRank().getValue() && hc1.getLowRank().getValue() > hc2.getLowRank().getValue() && !hc1.isPair() && !hc2.isPair()) {
					// 1 over vs 1 in between.
					final Odds odds = TwoPlayerPreFlopHoleCategoryOddsDB.getInstance().getOdds(hc1, hc2);
					final double equity = odds.getEquity();
					if (equity > maxEquity) {
						maxEquity = equity;
						maxHoles = new HoleCategory[] { hc1, hc2 };
					}
					if (equity < minEquity) {
						minEquity = equity;
						minHoles = new HoleCategory[] { hc1, hc2 };
					}
					final int weight1 = hc1.getSize();
					final int weight2 = hc2.getSize();
					final int weight = weight1 * weight2;

					totEquity += equity * weight;
					totWeight += weight;
				}
			}
		}
		return new Object[] { minEquity, minHoles, maxEquity, maxHoles, totEquity / totWeight };
	}

	public static Object[] getOddsDominatedHighCard() {
		double maxEquity = 0, minEquity = 1, totEquity = 0, totWeight = 0;
		HoleCategory[] maxHoles = null, minHoles = null;
		for (final HoleCategory hc1 : HoleCategory.values()) {
			for (final HoleCategory hc2 : HoleCategory.values()) {
				if (hc1.getHighRank().getValue() > hc2.getHighRank().getValue() && hc1.getLowRank().getValue() == hc2.getLowRank().getValue() && !hc1.isPair() && !hc2.isPair()) {
					// 1 over vs 1 in between.
					final Odds odds = TwoPlayerPreFlopHoleCategoryOddsDB.getInstance().getOdds(hc1, hc2);
					final double equity = odds.getEquity();
					if (equity > maxEquity) {
						maxEquity = equity;
						maxHoles = new HoleCategory[] { hc1, hc2 };
					}
					if (equity < minEquity) {
						minEquity = equity;
						minHoles = new HoleCategory[] { hc1, hc2 };
					}
					final int weight1 = hc1.getSize();
					final int weight2 = hc2.getSize();
					final int weight = weight1 * weight2;

					totEquity += equity * weight;
					totWeight += weight;
				}
			}
		}
		return new Object[] { minEquity, minHoles, maxEquity, maxHoles, totEquity / totWeight };
	}


	public static void main(final String[] args) {
		System.out.println("2 over vs 2 under");
		printResults(getOdds2OverVs2Under());
		System.out.println("1 over 1 under vs 2 in between");
		printResults(getOddsOverUnderVs2Between());
		System.out.println("1 over vs 1 in between");
		printResults(getOddsOverVs1Between());
		System.out.println("pair vs 2 over");
		printResults(getOddsPairVs2Over());
		System.out.println("pair vs 1 over");
		printResults(getOddsPairVs1Over());
		System.out.println("pair vs 2 under");
		printResults(getOddsPairVs2Under());
		System.out.println("overpair vs underpair");
		printResults(getOddsOverPairVsUnderPair());
		System.out.println("dominated low card");
		printResults(getOddsDominatedLowCard());
		System.out.println("dominated high card");
		printResults(getOddsDominatedHighCard());
	}

	private static void printResults(Object[] results) {
		final Double minEquity = (Double) results[0], maxEquity = (Double) results[2], avgEquity = (Double) results[4];
		final HoleCategory[] minHoles = (HoleCategory[]) results[1], maxHoles = (HoleCategory[]) results[3];
		System.out.println("Minimum equity: " + equityToString(minEquity) + " with hole " + minHoles[0] + " vs " + minHoles[1]);
		System.out.println("Maximum equity: " + equityToString(maxEquity) + " with hole " + maxHoles[0] + " vs " + maxHoles[1]);
		System.out.println("Average equity: " + equityToString(avgEquity));

	}

	private static String equityToString(final double equity) {
		NumberFormat decimalFormat = new DecimalFormat("#.0");
		StringBuffer buffer = new StringBuffer();
		buffer.append(decimalFormat.format(equity * 100));
		buffer.append("-");
		buffer.append(decimalFormat.format((1 - equity) * 100));
		buffer.append(" (");
		buffer.append(decimalFormat.format(equity / (1 - equity)));
		buffer.append("-1)");
		return buffer.toString();
	}

}
