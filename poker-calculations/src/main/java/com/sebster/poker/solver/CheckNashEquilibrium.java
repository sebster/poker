package com.sebster.poker.solver;

import java.util.EnumSet;

import com.sebster.math.rational.Rational;
import com.sebster.math.rational.matrix.Matrix;
import com.sebster.poker.HoleCategory;
import com.sebster.poker.HoleRange;
import com.sebster.poker.holdem.AllinOrFoldStrategy;
import com.sebster.poker.holdem.MixedAllinOrFoldStrategy;
import com.sebster.poker.holdem.PureAllinOrFoldStrategy;
import com.sebster.poker.holdem.odds.TwoPlayerPreFlopHoleCategoryOddsDB;
import com.sebster.poker.odds.Odds;

public class CheckNashEquilibrium {

	public static void main(String[] args) {
		String sb = "{22=1, 32s=0, 42s=0, 52s=0, 62s=0, 72s=0, 82s=0, 92s=0, T2s=0, J2s=0, Q2s=0, K2s=0, A2s=1, 32o=0, 33=1, 43s=0, 53s=0, 63s=0, 73s=0, 83s=0, 93s=0, T3s=0, J3s=0, Q3s=0, K3s=0, A3s=1, 42o=0, 43o=0, 44=1, 54s=0, 64s=0, 74s=0, 84s=0, 94s=0, T4s=0, J4s=0, Q4s=0, K4s=22044013/39329424, A4s=1, 52o=0, 53o=0, 54o=0, 55=1, 65s=1, 75s=0, 85s=0, 95s=0, T5s=0, J5s=0, Q5s=0, K5s=1, A5s=1, 62o=0, 63o=0, 64o=0, 65o=0, 66=1, 76s=1, 86s=1, 96s=0, T6s=0, J6s=0, Q6s=0, K6s=1, A6s=1, 72o=0, 73o=0, 74o=0, 75o=0, 76o=0, 77=1, 87s=1, 97s=1, T7s=1, J7s=0, Q7s=0, K7s=1, A7s=1, 82o=0, 83o=0, 84o=0, 85o=0, 86o=0, 87o=0, 88=1, 98s=1, T8s=1, J8s=1, Q8s=1, K8s=1, A8s=1, 92o=0, 93o=0, 94o=0, 95o=0, 96o=0, 97o=0, 98o=0, 99=1, T9s=1, J9s=1, Q9s=1, K9s=1, A9s=1, T2o=0, T3o=0, T4o=0, T5o=0, T6o=0, T7o=0, T8o=0, T9o=0, TT=1, JTs=1, QTs=1, KTs=1, ATs=1, J2o=0, J3o=0, J4o=0, J5o=0, J6o=0, J7o=0, J8o=0, J9o=0, JTo=1, JJ=1, QJs=1, KJs=1, AJs=1, Q2o=0, Q3o=0, Q4o=0, Q5o=0, Q6o=0, Q7o=0, Q8o=0, Q9o=0, QTo=1, QJo=1, QQ=1, KQs=1, AQs=1, K2o=0, K3o=0, K4o=0, K5o=0, K6o=0, K7o=0, K8o=0, K9o=1, KTo=1, KJo=1, KQo=1, KK=1, AKs=1, A2o=1, A3o=1, A4o=1, A5o=1, A6o=1, A7o=1, A8o=1, A9o=1, ATo=1, AJo=1, AQo=1, AKo=1, AA=1}";
		String bb = "{22=0, 32s=0, 42s=0, 52s=0, 62s=0, 72s=0, 82s=0, 92s=0, T2s=0, J2s=0, Q2s=0, K2s=0, A2s=1, 32o=0, 33=1, 43s=0, 53s=0, 63s=0, 73s=0, 83s=0, 93s=0, T3s=0, J3s=0, Q3s=0, K3s=0, A3s=1, 42o=0, 43o=0, 44=1, 54s=0, 64s=0, 74s=0, 84s=0, 94s=0, T4s=0, J4s=0, Q4s=0, K4s=0, A4s=1, 52o=0, 53o=0, 54o=0, 55=1, 65s=0, 75s=0, 85s=0, 95s=0, T5s=0, J5s=0, Q5s=0, K5s=0, A5s=1, 62o=0, 63o=0, 64o=0, 65o=0, 66=1, 76s=0, 86s=0, 96s=0, T6s=0, J6s=0, Q6s=0, K6s=0, A6s=1, 72o=0, 73o=0, 74o=0, 75o=0, 76o=0, 77=1, 87s=0, 97s=0, T7s=0, J7s=0, Q7s=0, K7s=0, A7s=1, 82o=0, 83o=0, 84o=0, 85o=0, 86o=0, 87o=0, 88=1, 98s=0, T8s=0, J8s=0, Q8s=0, K8s=2282797/6554904, A8s=1, 92o=0, 93o=0, 94o=0, 95o=0, 96o=0, 97o=0, 98o=0, 99=1, T9s=0, J9s=0, Q9s=0, K9s=1, A9s=1, T2o=0, T3o=0, T4o=0, T5o=0, T6o=0, T7o=0, T8o=0, T9o=0, TT=1, JTs=1, QTs=1, KTs=1, ATs=1, J2o=0, J3o=0, J4o=0, J5o=0, J6o=0, J7o=0, J8o=0, J9o=0, JTo=0, JJ=1, QJs=1, KJs=1, AJs=1, Q2o=0, Q3o=0, Q4o=0, Q5o=0, Q6o=0, Q7o=0, Q8o=0, Q9o=0, QTo=0, QJo=1, QQ=1, KQs=1, AQs=1, K2o=0, K3o=0, K4o=0, K5o=0, K6o=0, K7o=0, K8o=0, K9o=0, KTo=1, KJo=1, KQo=1, KK=1, AKs=1, A2o=0, A3o=0, A4o=0, A5o=1, A6o=1, A7o=1, A8o=1, A9o=1, ATo=1, AJo=1, AQo=1, AKo=1, AA=1}";
		MixedAllinOrFoldStrategy sbStrategy = MixedAllinOrFoldStrategy.fromString(sb);
		MixedAllinOrFoldStrategy bbStrategy = MixedAllinOrFoldStrategy.fromString(bb);
		AllinOrFoldStrategy optBBStrategy = Solver.optimalBBStrategy(sbStrategy, 1000, 100);
		AllinOrFoldStrategy optSBStrategy = Solver.optimalSBStrategy(bbStrategy, 1000, 100);
		System.out.println("Computed strategy EV = " + Solver.computeSBEV(sbStrategy, bbStrategy, 1000, 100).decimalValue());
		System.out.println("Against opt bb EV    = " + Solver.computeSBEV(sbStrategy, optBBStrategy, 1000, 100).decimalValue());
		System.out.println("Against opt sb EV    = " + Solver.computeSBEV(optSBStrategy, bbStrategy, 1000, 100).decimalValue());
		
		final int effectiveStack = 1000;
		final int bigBlind = 100;
		final int smallBlind = bigBlind / 2;
		final Matrix<Rational> A = new Matrix<Rational>(1 + 169 * 2, 1 + 169 * 2, Rational.ZERO);
		final TwoPlayerPreFlopHoleCategoryOddsDB db = TwoPlayerPreFlopHoleCategoryOddsDB.getInstance();
		for (final HoleCategory hc1 : HoleCategory.values()) {
			for (final HoleCategory hc2 : HoleCategory.values()) {
				final int p1PushRow = 1 + 2 * hc1.ordinal();
				final int p1FoldRow = p1PushRow + 1;
				final int p2CallCol = 1 + 2 * hc2.ordinal();
				final int p2FoldCol = p2CallCol + 1;
				final Odds odds = db.getOdds(hc1, hc2);
				final Rational hc1Prob = hc1.getProbability();
				final Rational hc1hc2Prob = db.getProbability(hc1, hc2);
				
				A.set(p1PushRow, p2CallCol, hc1hc2Prob.multiply(odds.getWinProbability().subtract(odds.getLossProbability()).multiply(effectiveStack).simplify()));
				A.set(p1PushRow, p2FoldCol, hc1hc2Prob.multiply(bigBlind));
				A.set(p1FoldRow, 0, hc1Prob.multiply(-smallBlind));
			}
		}
		
		final Matrix<Rational> x = new Matrix<Rational>(1 + 169 * 2, 1, Rational.ZERO);
		final Matrix<Rational> y = new Matrix<Rational>(1 + 169 * 2, 1, Rational.ZERO);
		x.set(0, 0, Rational.ONE);
		y.set(0, 0, Rational.ONE);
		for (final HoleCategory hc : HoleCategory.values()) {
			x.set(1 + hc.ordinal() * 2, 0, sbStrategy.getAllinFrequency(hc));
			x.set(1 + hc.ordinal() * 2 + 1, 0, Rational.ONE.subtract(sbStrategy.getAllinFrequency(hc)));
			y.set(1 + hc.ordinal() * 2, 0, bbStrategy.getAllinFrequency(hc));
			y.set(1 + hc.ordinal() * 2 + 1, 0, Rational.ONE.subtract(bbStrategy.getAllinFrequency(hc)));
		}
		
		final Rational sbEv = x.transpose().times(A).times(y).get(0, 0);
		System.out.println("Matrix EV            = " + sbEv.decimalValue());

		EnumSet<HoleCategory> sbR = HoleRange.fromString("22-AA,A2s+,K2s+,Q2s+,J3s+,T4s+,95s+,84s+,74s+,64s+,53s+,43s,A2o+,K2o+,Q7o+,J8o+,T8o+,97o+,87o,76o");
		EnumSet<HoleCategory> bbR = HoleRange.fromString("22-AA,A2s+,K2s+,Q6s+,J8s+,T9s,A2o+,K5o+,Q9o+,JTo");
		sbStrategy = new MixedAllinOrFoldStrategy(new PureAllinOrFoldStrategy(sbR));
		sbStrategy.putHoleCategory(HoleCategory.s43, new Rational(71, 100));
		bbStrategy = new MixedAllinOrFoldStrategy(new PureAllinOrFoldStrategy(bbR));
		bbStrategy.putHoleCategory(HoleCategory.sQ6, new Rational(40, 100));
		optBBStrategy = Solver.optimalBBStrategy(sbStrategy, 1000, 100);
		optSBStrategy = Solver.optimalSBStrategy(bbStrategy, 1000, 100);
		
		System.out.println("SNG wizard opt EV    = " + Solver.computeSBEV(sbStrategy, bbStrategy, 1000, 100).decimalValue());
		System.out.println("Against opt BB EV    = " + Solver.computeSBEV(sbStrategy, optBBStrategy, 1000, 100).decimalValue());
		System.out.println("Against opt SB EV    = " + Solver.computeSBEV(optSBStrategy, bbStrategy, 1000, 100).decimalValue());
	}

}
