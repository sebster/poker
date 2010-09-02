package com.sebster.poker.solver;

import com.sebster.poker.holdem.AllinOrFoldStrategy;
import com.sebster.poker.holdem.MixedAllinOrFoldStrategy;

public class CheckNashEquilibrium {

	public static void checkNashEquilibrium(final AllinOrFoldStrategy sbStrategy, final AllinOrFoldStrategy bbStrategy, final int effectiveStack, final int bigBlind) {
		AllinOrFoldStrategy optBBStrategy = Solver.optimalBBStrategy(sbStrategy, effectiveStack, bigBlind);
		AllinOrFoldStrategy optSBStrategy = Solver.optimalSBStrategy(bbStrategy, effectiveStack, bigBlind);
		System.out.println("Computed strategy EV = " + Solver.computeSBEV(sbStrategy, bbStrategy, effectiveStack, bigBlind));
		System.out.println("Against opt bb EV    = " + Solver.computeSBEV(sbStrategy, optBBStrategy, effectiveStack, bigBlind));
		System.out.println("Against opt sb EV    = " + Solver.computeSBEV(optSBStrategy, bbStrategy, effectiveStack, bigBlind));
	}
	
	public static void main(String[] args) {
		
		String sb, bb;
		AllinOrFoldStrategy sbStrategy, bbStrategy;
		
		// 10 bb
		sb = "{22=1, Q2s=1, K2s=1, A2s=1, 33=1, 43s=8731989/12285638, 53s=1, J3s=1, Q3s=1, K3s=1, A3s=1, 44=1, 54s=1, 64s=1, 74s=1, 84s=1, T4s=1, J4s=1, Q4s=1, K4s=1, A4s=1, 55=1, 65s=1, 75s=1, 85s=1, 95s=1, T5s=1, J5s=1, Q5s=1, K5s=1, A5s=1, 66=1, 76s=1, 86s=1, 96s=1, T6s=1, J6s=1, Q6s=1, K6s=1, A6s=1, 76o=1, 77=1, 87s=1, 97s=1, T7s=1, J7s=1, Q7s=1, K7s=1, A7s=1, 87o=1, 88=1, 98s=1, T8s=1, J8s=1, Q8s=1, K8s=1, A8s=1, 97o=1, 98o=1, 99=1, T9s=1, J9s=1, Q9s=1, K9s=1, A9s=1, T8o=1, T9o=1, TT=1, JTs=1, QTs=1, KTs=1, ATs=1, J8o=1, J9o=1, JTo=1, JJ=1, QJs=1, KJs=1, AJs=1, Q7o=1, Q8o=1, Q9o=1, QTo=1, QJo=1, QQ=1, KQs=1, AQs=1, K2o=1, K3o=1, K4o=1, K5o=1, K6o=1, K7o=1, K8o=1, K9o=1, KTo=1, KJo=1, KQo=1, KK=1, AKs=1, A2o=1, A3o=1, A4o=1, A5o=1, A6o=1, A7o=1, A8o=1, A9o=1, ATo=1, AJo=1, AQo=1, AKo=1, AA=1}";
		bb = "{22=1, K2s=1, A2s=1, 33=1, K3s=1, A3s=1, 44=1, K4s=1, A4s=1, 55=1, K5s=1, A5s=1, 66=1, Q6s=4948399/12285638, K6s=1, A6s=1, 77=1, Q7s=1, K7s=1, A7s=1, 88=1, J8s=1, Q8s=1, K8s=1, A8s=1, 99=1, T9s=1, J9s=1, Q9s=1, K9s=1, A9s=1, TT=1, JTs=1, QTs=1, KTs=1, ATs=1, JTo=1, JJ=1, QJs=1, KJs=1, AJs=1, Q9o=1, QTo=1, QJo=1, QQ=1, KQs=1, AQs=1, K5o=1, K6o=1, K7o=1, K8o=1, K9o=1, KTo=1, KJo=1, KQo=1, KK=1, AKs=1, A2o=1, A3o=1, A4o=1, A5o=1, A6o=1, A7o=1, A8o=1, A9o=1, ATo=1, AJo=1, AQo=1, AKo=1, AA=1}";
		sbStrategy = MixedAllinOrFoldStrategy.fromString(sb);
		bbStrategy = MixedAllinOrFoldStrategy.fromString(bb);
		checkNashEquilibrium(sbStrategy, bbStrategy, 1000, 100);
		
		// 6 bb
		sb = "{22=1, T2s=1, J2s=1, Q2s=1, K2s=1, A2s=1, 33=1, 43s=1, 53s=1, T3s=1, J3s=1, Q3s=1, K3s=1, A3s=1, 44=1, 54s=1, 64s=1, 74s=1, 84s=1, T4s=1, J4s=1, Q4s=1, K4s=1, A4s=1, 55=1, 65s=1, 75s=1, 85s=1, 95s=1, T5s=1, J5s=1, Q5s=1, K5s=1, A5s=1, 66=1, 76s=1, 86s=1, 96s=1, T6s=1, J6s=1, Q6s=1, K6s=1, A6s=1, 76o=1, 77=1, 87s=1, 97s=1, T7s=1, J7s=1, Q7s=1, K7s=1, A7s=1, 86o=9055669/9143474, 87o=1, 88=1, 98s=1, T8s=1, J8s=1, Q8s=1, K8s=1, A8s=1, 97o=1, 98o=1, 99=1, T9s=1, J9s=1, Q9s=1, K9s=1, A9s=1, T7o=1, T8o=1, T9o=1, TT=1, JTs=1, QTs=1, KTs=1, ATs=1, J5o=1, J6o=1, J7o=1, J8o=1, J9o=1, JTo=1, JJ=1, QJs=1, KJs=1, AJs=1, Q2o=1, Q3o=1, Q4o=1, Q5o=1, Q6o=1, Q7o=1, Q8o=1, Q9o=1, QTo=1, QJo=1, QQ=1, KQs=1, AQs=1, K2o=1, K3o=1, K4o=1, K5o=1, K6o=1, K7o=1, K8o=1, K9o=1, KTo=1, KJo=1, KQo=1, KK=1, AKs=1, A2o=1, A3o=1, A4o=1, A5o=1, A6o=1, A7o=1, A8o=1, A9o=1, ATo=1, AJo=1, AQo=1, AKo=1, AA=1}";
		bb = "{22=1, Q2s=1, K2s=1, A2s=1, 33=1, Q3s=1, K3s=1, A3s=1, 44=1, J4s=1, Q4s=1, K4s=1, A4s=1, 55=1, J5s=1, Q5s=1, K5s=1, A5s=1, 66=1, T6s=1, J6s=1, Q6s=1, K6s=1, A6s=1, 77=1, 87s=1, 97s=1, T7s=1, J7s=1, Q7s=1, K7s=1, A7s=1, 88=1, 98s=1, T8s=1, J8s=1, Q8s=1, K8s=1, A8s=1, 98o=4567801/4571737, 99=1, T9s=1, J9s=1, Q9s=1, K9s=1, A9s=1, T8o=1, T9o=1, TT=1, JTs=1, QTs=1, KTs=1, ATs=1, J7o=1, J8o=1, J9o=1, JTo=1, JJ=1, QJs=1, KJs=1, AJs=1, Q4o=1, Q5o=1, Q6o=1, Q7o=1, Q8o=1, Q9o=1, QTo=1, QJo=1, QQ=1, KQs=1, AQs=1, K2o=1, K3o=1, K4o=1, K5o=1, K6o=1, K7o=1, K8o=1, K9o=1, KTo=1, KJo=1, KQo=1, KK=1, AKs=1, A2o=1, A3o=1, A4o=1, A5o=1, A6o=1, A7o=1, A8o=1, A9o=1, ATo=1, AJo=1, AQo=1, AKo=1, AA=1}";
		sbStrategy = MixedAllinOrFoldStrategy.fromString(sb);
		bbStrategy = MixedAllinOrFoldStrategy.fromString(bb);
		checkNashEquilibrium(sbStrategy, bbStrategy, 600, 100);
		
		// 5 bb
		sb = "{22=1, T2s=1, J2s=1, Q2s=1, K2s=1, A2s=1, 33=1, 43s=1, 53s=1, T3s=1, J3s=1, Q3s=1, K3s=1, A3s=1, 44=1, 54s=1, 64s=1, 74s=1, 84s=1, 94s=1, T4s=1, J4s=1, Q4s=1, K4s=1, A4s=1, 55=1, 65s=1, 75s=1, 85s=1, 95s=1, T5s=1, J5s=1, Q5s=1, K5s=1, A5s=1, 66=1, 76s=1, 86s=1, 96s=1, T6s=1, J6s=1, Q6s=1, K6s=1, A6s=1, 76o=1, 77=1, 87s=1, 97s=1, T7s=1, J7s=1, Q7s=1, K7s=1, A7s=1, 86o=1, 87o=1, 88=1, 98s=1, T8s=1, J8s=1, Q8s=1, K8s=1, A8s=1, 96o=5692536/36412441, 97o=1, 98o=1, 99=1, T9s=1, J9s=1, Q9s=1, K9s=1, A9s=1, T6o=1, T7o=1, T8o=1, T9o=1, TT=1, JTs=1, QTs=1, KTs=1, ATs=1, J3o=1, J4o=1, J5o=1, J6o=1, J7o=1, J8o=1, J9o=1, JTo=1, JJ=1, QJs=1, KJs=1, AJs=1, Q2o=1, Q3o=1, Q4o=1, Q5o=1, Q6o=1, Q7o=1, Q8o=1, Q9o=1, QTo=1, QJo=1, QQ=1, KQs=1, AQs=1, K2o=1, K3o=1, K4o=1, K5o=1, K6o=1, K7o=1, K8o=1, K9o=1, KTo=1, KJo=1, KQo=1, KK=1, AKs=1, A2o=1, A3o=1, A4o=1, A5o=1, A6o=1, A7o=1, A8o=1, A9o=1, ATo=1, AJo=1, AQo=1, AKo=1, AA=1}";
		bb = "{22=1, J2s=1, Q2s=1, K2s=1, A2s=1, 33=1, J3s=1, Q3s=1, K3s=1, A3s=1, 44=1, T4s=1, J4s=1, Q4s=1, K4s=1, A4s=1, 55=1, 95s=1, T5s=1, J5s=1, Q5s=1, K5s=1, A5s=1, 66=1, 76s=1, 86s=1, 96s=1, T6s=1, J6s=1, Q6s=1, K6s=1, A6s=1, 77=1, 87s=1, 97s=1, T7s=1, J7s=1, Q7s=1, K7s=1, A7s=1, 88=1, 98s=1, T8s=1, J8s=1, Q8s=1, K8s=1, A8s=1, 97o=27369506/36412441, 98o=1, 99=1, T9s=1, J9s=1, Q9s=1, K9s=1, A9s=1, T7o=1, T8o=1, T9o=1, TT=1, JTs=1, QTs=1, KTs=1, ATs=1, J5o=1, J6o=1, J7o=1, J8o=1, J9o=1, JTo=1, JJ=1, QJs=1, KJs=1, AJs=1, Q2o=1, Q3o=1, Q4o=1, Q5o=1, Q6o=1, Q7o=1, Q8o=1, Q9o=1, QTo=1, QJo=1, QQ=1, KQs=1, AQs=1, K2o=1, K3o=1, K4o=1, K5o=1, K6o=1, K7o=1, K8o=1, K9o=1, KTo=1, KJo=1, KQo=1, KK=1, AKs=1, A2o=1, A3o=1, A4o=1, A5o=1, A6o=1, A7o=1, A8o=1, A9o=1, ATo=1, AJo=1, AQo=1, AKo=1, AA=1}";
		sbStrategy = MixedAllinOrFoldStrategy.fromString(sb);
		bbStrategy = MixedAllinOrFoldStrategy.fromString(bb);
		checkNashEquilibrium(sbStrategy, bbStrategy, 500, 100);
	}

}
