package com.sebster.poker.calculations.odds;

import java.util.HashMap;
import java.util.Map;

import com.sebster.math.rational.Rational;
import com.sebster.poker.HoleCategory;
import com.sebster.poker.Rank;
import com.sebster.poker.holdem.odds.TwoPlayerPreFlopHoleCategoryOddsDB;
import com.sebster.poker.odds.Odds;

public class BestWorstHands {

	public static void main(String[] args) {
		Map<HoleCategory, Rational> minEquityMap = new HashMap<HoleCategory, Rational>(), maxEquityMap = new HashMap<HoleCategory, Rational>();
		Map<HoleCategory, HoleCategory> minHoleMap = new HashMap<HoleCategory, HoleCategory>(), maxHoleMap = new HashMap<HoleCategory, HoleCategory>();
		for (HoleCategory hc1 : HoleCategory.values()) {
			Rational minEquity = Rational.ONE, maxEquity = Rational.ZERO;
			HoleCategory minHole = null, maxHole = null;
			for (HoleCategory hc2 : HoleCategory.values()) {
				Odds odds = TwoPlayerPreFlopHoleCategoryOddsDB.getInstance().getOdds(hc1, hc2);
				final Rational equity = odds.getEquity();
				if (equity.compareTo(minEquity) < 0) {
					minEquity = equity;
					minHole = hc2;
				}
				if (equity.compareTo(maxEquity) > 0) {
					maxEquity = equity;
					maxHole = hc2;
				}
			}
			minEquityMap.put(hc1, minEquity);
			minHoleMap.put(hc1, minHole);
			maxEquityMap.put(hc1, maxEquity);
			maxHoleMap.put(hc1, maxHole);
		}

		System.out.println("Does best against:");
		System.out.println("<table>");
		System.out.print("<tr><th></th>");
		for (int i = 14; i >= 2; i--) {
			System.out.print("<th>" + Rank.byValue(i) + "</th>");
		}
		System.out.println("</tr>");
		for (int i = 14; i >= 2; i--) {
			System.out.print("<tr><th>" + Rank.byValue(i) + "</th>");
			for (int j = 14; j >= 2; j--) {
				HoleCategory hc = HoleCategory.byDescription(Rank.byValue(i), Rank.byValue(j), i > j);
				HoleCategory maxHole = maxHoleMap.get(hc);
				Rational maxEquity = maxEquityMap.get(hc);
				System.out.printf("<td class=\"%s\">%3s (%4.1f%%)</td>", i > j ? "suited" : i < j ? "offsuit" : "pair", maxHole, maxEquity.times(100).doubleValue());
			}
			System.out.println("</tr>");
		}
		System.out.println("</table>");

		System.out.println("Does worst against:");
		System.out.println("<table>");
		System.out.print("<tr><th></th>");
		for (int i = 14; i >= 2; i--) {
			System.out.print("<th>" + Rank.byValue(i) + "</th>");
		}
		System.out.println("</tr>");
		for (int i = 14; i >= 2; i--) {
			System.out.print("<tr><th>" + Rank.byValue(i) + "</th>");
			for (int j = 14; j >= 2; j--) {
				HoleCategory hc = HoleCategory.byDescription(Rank.byValue(i), Rank.byValue(j), i > j);
				HoleCategory minHole = minHoleMap.get(hc);
				Rational minEquity = minEquityMap.get(hc);
				System.out.printf("<td class=\"%s\">%3s (%4.1f%%)</td>", i > j ? "suited" : i < j ? "offsuit" : "pair", minHole, minEquity.times(100).doubleValue());
			}
			System.out.println("</tr>");
		}
		System.out.println("</table>");

	}

}
