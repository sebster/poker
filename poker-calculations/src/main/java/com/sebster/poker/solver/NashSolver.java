package com.sebster.poker.solver;

import java.util.EnumMap;

import com.sebster.gametheory.nash.NashEquilibrium;
import com.sebster.math.rational.Rational;
import com.sebster.math.rational.matrix.Matrix;
import com.sebster.poker.HoleCategory;
import com.sebster.poker.holdem.odds.TwoPlayerPreFlopHoleCategoryOddsDB;
import com.sebster.poker.odds.Odds;
import com.sebster.util.Assert;
import com.sebster.util.collections.ImmutablePair;
import com.sebster.util.collections.Pair;

public class NashSolver {

	public static Pair<EnumMap<HoleCategory, Rational>, EnumMap<HoleCategory, Rational>> calculateNashEquilibrium(final int effectiveStack, final int smallBlind, final int bigBlind) {
		final Matrix<Rational> E = new Matrix<Rational>(1 + 169, 1 + 2 * 169, Rational.ZERO);
		E.set(0, 0, Rational.ONE);
		for (int i = 0; i < 169; i++) {
			E.set(i + 1, 0, Rational.ONE.negate());
			E.set(i + 1, 2 * i + 1, Rational.ONE);
			E.set(i + 1, 2 * i + 2, Rational.ONE);
		}
		final Matrix<Rational> F = E.copy();
		
		final Matrix<Rational> A = new Matrix<Rational>(1 + 169 * 2, 1 + 169 * 2, Rational.ZERO);
		final Matrix<Rational> B = new Matrix<Rational>(1 + 169 * 2, 1 + 169 * 2, Rational.ZERO);
		
		final TwoPlayerPreFlopHoleCategoryOddsDB db = TwoPlayerPreFlopHoleCategoryOddsDB.getInstance();
		for (final HoleCategory hc1 : HoleCategory.values()) {
			for (final HoleCategory hc2 : HoleCategory.values()) {
				final int p1PushRow = 1 + 2 * hc1.ordinal();
				final int p1FoldRow = p1PushRow + 1;
				final int p2CallCol = 1 + 2 * hc2.ordinal();
				final int p2FoldCol = p2CallCol + 1;
				final Odds odds = db.getOdds(hc1, hc2);
				final Rational pot = new Rational(2 * effectiveStack);
				A.set(p1PushRow, p2CallCol, new Rational(2 * odds.getWins() + odds.getSplits(), 2 * odds.getTotal()).multiply(pot).subtract(effectiveStack));
				A.set(p1PushRow, p2FoldCol, new Rational(bigBlind));
				A.set(p1FoldRow, 0, new Rational(-smallBlind));
				B.set(p1PushRow, p2CallCol, new Rational(2 * odds.getLosses() + odds.getSplits(), 2 * odds.getTotal()).multiply(pot).subtract(effectiveStack));
				B.set(p1PushRow, p2FoldCol, new Rational(-bigBlind));
				B.set(p1FoldRow, 0, new Rational(smallBlind));
				Assert.isTrue(A.get(p1PushRow, p2CallCol).equals(B.get(p1PushRow, p2CallCol).negate()), "not zero sum");
			}
		}
		
		final Pair<Matrix<Rational>, Matrix<Rational>> p = NashEquilibrium.solve(E, F, A, B);
		final Matrix<Rational> x = p.getFirst(), y = p.getSecond();
		final EnumMap<HoleCategory, Rational> p1Strategy = new EnumMap<HoleCategory, Rational>(HoleCategory.class);
		final EnumMap<HoleCategory, Rational> p2Strategy = new EnumMap<HoleCategory, Rational>(HoleCategory.class);
		for (final HoleCategory hc : HoleCategory.values()) {
			p1Strategy.put(hc, x.get(1 + hc.ordinal() * 2, 0));
			p2Strategy.put(hc, y.get(1 + hc.ordinal() * 2, 0));
		}
		
		return new ImmutablePair<EnumMap<HoleCategory, Rational>, EnumMap<HoleCategory, Rational>>(p1Strategy, p2Strategy);
	}

	public static void main(final String[] args) {
		final int stack = Integer.parseInt(args[0]);
		final int sb = Integer.parseInt(args[1]);
		final int bb = Integer.parseInt(args[2]);
		final Pair<EnumMap<HoleCategory, Rational>, EnumMap<HoleCategory, Rational>> result = calculateNashEquilibrium(stack, sb, bb);
		System.out.println("player 1 = " + result.getFirst());
		System.out.println("player 2 = " + result.getSecond());
	}

	
}
