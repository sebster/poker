package com.sebster.poker.holdem.headsup;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import com.sebster.math.rational.Rational;
import com.sebster.poker.holdem.AllinOrFoldStrategy;
import com.sebster.poker.holdem.MixedAllinOrFoldStrategy;
import com.sebster.poker.holdem.ReadOnlyAllinOrFoldStrategy;

public class NashEquilibria {

	private static final Map<Rational, AllinOrFoldStrategy> sbNashEquilibria = new TreeMap<Rational, AllinOrFoldStrategy>();
	
	private static final Map<Rational, AllinOrFoldStrategy> bbNashEquilibria = new TreeMap<Rational, AllinOrFoldStrategy>();
	
	private static final Map<Rational, Rational> sbNashEquilibriaEV = new TreeMap<Rational, Rational>();
	
	private NashEquilibria() {
		// Singleton.
		
	}
	public static AllinOrFoldStrategy getSBNashEquilibrium(final Rational r) {
		return sbNashEquilibria.get(r);
	}
	
	public static AllinOrFoldStrategy getBBNashEqulibrium(final Rational r) {
		return bbNashEquilibria.get(r);
	}
	
	public static Rational getSBNashEquilibriumEV(final Rational r) {
		return sbNashEquilibriaEV.get(r);
	}
	
	public static Rational getBBNashEquilibriumEV(final Rational r) {
		// Zero sum.
		final Rational sbEV = sbNashEquilibriaEV.get(r);
		return sbEV == null ? null : sbEV.negate();
	}
	
	public static Set<Rational> getRs() {
		return Collections.unmodifiableSet(sbNashEquilibria.keySet());
	}
	
	private static void register(final Rational r, final String sbStrategy, final String bbStrategy, final Rational sbEV) {
		sbNashEquilibria.put(r, new ReadOnlyAllinOrFoldStrategy(MixedAllinOrFoldStrategy.fromString(sbStrategy)));
		bbNashEquilibria.put(r, new ReadOnlyAllinOrFoldStrategy(MixedAllinOrFoldStrategy.fromString(bbStrategy)));
		sbNashEquilibriaEV.put(r, sbEV);
	}
	
	// Initialization.
	static {
		String sb, bb;

		// 1 bb
		sb = "{22=1, 32s=1, 42s=1, 52s=1, 62s=1, 72s=1, 82s=1, 92s=1, T2s=1, J2s=1, Q2s=1, K2s=1, A2s=1, 32o=1, 33=1, 43s=1, 53s=1, 63s=1, 73s=1, 83s=1, 93s=1, T3s=1, J3s=1, Q3s=1, K3s=1, A3s=1, 42o=1, 43o=1, 44=1, 54s=1, 64s=1, 74s=1, 84s=1, 94s=1, T4s=1, J4s=1, Q4s=1, K4s=1, A4s=1, 52o=1, 53o=1, 54o=1, 55=1, 65s=1, 75s=1, 85s=1, 95s=1, T5s=1, J5s=1, Q5s=1, K5s=1, A5s=1, 62o=1, 63o=1, 64o=1, 65o=1, 66=1, 76s=1, 86s=1, 96s=1, T6s=1, J6s=1, Q6s=1, K6s=1, A6s=1, 72o=1, 73o=1, 74o=1, 75o=1, 76o=1, 77=1, 87s=1, 97s=1, T7s=1, J7s=1, Q7s=1, K7s=1, A7s=1, 82o=1, 83o=1, 84o=1, 85o=1, 86o=1, 87o=1, 88=1, 98s=1, T8s=1, J8s=1, Q8s=1, K8s=1, A8s=1, 92o=1, 93o=1, 94o=1, 95o=1, 96o=1, 97o=1, 98o=1, 99=1, T9s=1, J9s=1, Q9s=1, K9s=1, A9s=1, T2o=1, T3o=1, T4o=1, T5o=1, T6o=1, T7o=1, T8o=1, T9o=1, TT=1, JTs=1, QTs=1, KTs=1, ATs=1, J2o=1, J3o=1, J4o=1, J5o=1, J6o=1, J7o=1, J8o=1, J9o=1, JTo=1, JJ=1, QJs=1, KJs=1, AJs=1, Q2o=1, Q3o=1, Q4o=1, Q5o=1, Q6o=1, Q7o=1, Q8o=1, Q9o=1, QTo=1, QJo=1, QQ=1, KQs=1, AQs=1, K2o=1, K3o=1, K4o=1, K5o=1, K6o=1, K7o=1, K8o=1, K9o=1, KTo=1, KJo=1, KQo=1, KK=1, AKs=1, A2o=1, A3o=1, A4o=1, A5o=1, A6o=1, A7o=1, A8o=1, A9o=1, ATo=1, AJo=1, AQo=1, AKo=1, AA=1}";
		bb = "{22=1, 32s=1, 42s=1, 52s=1, 62s=1, 72s=1, 82s=1, 92s=1, T2s=1, J2s=1, Q2s=1, K2s=1, A2s=1, 32o=1, 33=1, 43s=1, 53s=1, 63s=1, 73s=1, 83s=1, 93s=1, T3s=1, J3s=1, Q3s=1, K3s=1, A3s=1, 42o=1, 43o=1, 44=1, 54s=1, 64s=1, 74s=1, 84s=1, 94s=1, T4s=1, J4s=1, Q4s=1, K4s=1, A4s=1, 52o=1, 53o=1, 54o=1, 55=1, 65s=1, 75s=1, 85s=1, 95s=1, T5s=1, J5s=1, Q5s=1, K5s=1, A5s=1, 62o=1, 63o=1, 64o=1, 65o=1, 66=1, 76s=1, 86s=1, 96s=1, T6s=1, J6s=1, Q6s=1, K6s=1, A6s=1, 72o=1, 73o=1, 74o=1, 75o=1, 76o=1, 77=1, 87s=1, 97s=1, T7s=1, J7s=1, Q7s=1, K7s=1, A7s=1, 82o=1, 83o=1, 84o=1, 85o=1, 86o=1, 87o=1, 88=1, 98s=1, T8s=1, J8s=1, Q8s=1, K8s=1, A8s=1, 92o=1, 93o=1, 94o=1, 95o=1, 96o=1, 97o=1, 98o=1, 99=1, T9s=1, J9s=1, Q9s=1, K9s=1, A9s=1, T2o=1, T3o=1, T4o=1, T5o=1, T6o=1, T7o=1, T8o=1, T9o=1, TT=1, JTs=1, QTs=1, KTs=1, ATs=1, J2o=1, J3o=1, J4o=1, J5o=1, J6o=1, J7o=1, J8o=1, J9o=1, JTo=1, JJ=1, QJs=1, KJs=1, AJs=1, Q2o=1, Q3o=1, Q4o=1, Q5o=1, Q6o=1, Q7o=1, Q8o=1, Q9o=1, QTo=1, QJo=1, QQ=1, KQs=1, AQs=1, K2o=1, K3o=1, K4o=1, K5o=1, K6o=1, K7o=1, K8o=1, K9o=1, KTo=1, KJo=1, KQo=1, KK=1, AKs=1, A2o=1, A3o=1, A4o=1, A5o=1, A6o=1, A7o=1, A8o=1, A9o=1, ATo=1, AJo=1, AQo=1, AKo=1, AA=1}";
		register(Rational.ONE, sb, bb, Rational.ZERO);
		
		// 1.5 bb
		sb = "{22=1, 32s=1, 42s=1, 52s=1, 62s=1, 72s=1, 82s=1, 92s=1, T2s=1, J2s=1, Q2s=1, K2s=1, A2s=1, 33=1, 43s=1, 53s=1, 63s=1, 73s=1, 83s=1, 93s=1, T3s=1, J3s=1, Q3s=1, K3s=1, A3s=1, 43o=1, 44=1, 54s=1, 64s=1, 74s=1, 84s=1, 94s=1, T4s=1, J4s=1, Q4s=1, K4s=1, A4s=1, 52o=1, 53o=1, 54o=1, 55=1, 65s=1, 75s=1, 85s=1, 95s=1, T5s=1, J5s=1, Q5s=1, K5s=1, A5s=1, 62o=1, 63o=1, 64o=1, 65o=1, 66=1, 76s=1, 86s=1, 96s=1, T6s=1, J6s=1, Q6s=1, K6s=1, A6s=1, 72o=1, 73o=1, 74o=1, 75o=1, 76o=1, 77=1, 87s=1, 97s=1, T7s=1, J7s=1, Q7s=1, K7s=1, A7s=1, 82o=1, 83o=1, 84o=1, 85o=1, 86o=1, 87o=1, 88=1, 98s=1, T8s=1, J8s=1, Q8s=1, K8s=1, A8s=1, 92o=1, 93o=1, 94o=1, 95o=1, 96o=1, 97o=1, 98o=1, 99=1, T9s=1, J9s=1, Q9s=1, K9s=1, A9s=1, T2o=1, T3o=1, T4o=1, T5o=1, T6o=1, T7o=1, T8o=1, T9o=1, TT=1, JTs=1, QTs=1, KTs=1, ATs=1, J2o=1, J3o=1, J4o=1, J5o=1, J6o=1, J7o=1, J8o=1, J9o=1, JTo=1, JJ=1, QJs=1, KJs=1, AJs=1, Q2o=1, Q3o=1, Q4o=1, Q5o=1, Q6o=1, Q7o=1, Q8o=1, Q9o=1, QTo=1, QJo=1, QQ=1, KQs=1, AQs=1, K2o=1, K3o=1, K4o=1, K5o=1, K6o=1, K7o=1, K8o=1, K9o=1, KTo=1, KJo=1, KQo=1, KK=1, AKs=1, A2o=1, A3o=1, A4o=1, A5o=1, A6o=1, A7o=1, A8o=1, A9o=1, ATo=1, AJo=1, AQo=1, AKo=1, AA=1}";
		bb = "{22=1, 32s=1, 42s=1, 52s=1, 62s=1, 72s=1, 82s=1, 92s=1, T2s=1, J2s=1, Q2s=1, K2s=1, A2s=1, 32o=1, 33=1, 43s=1, 53s=1, 63s=1, 73s=1, 83s=1, 93s=1, T3s=1, J3s=1, Q3s=1, K3s=1, A3s=1, 42o=1, 43o=1, 44=1, 54s=1, 64s=1, 74s=1, 84s=1, 94s=1, T4s=1, J4s=1, Q4s=1, K4s=1, A4s=1, 52o=1, 53o=1, 54o=1, 55=1, 65s=1, 75s=1, 85s=1, 95s=1, T5s=1, J5s=1, Q5s=1, K5s=1, A5s=1, 62o=1, 63o=1, 64o=1, 65o=1, 66=1, 76s=1, 86s=1, 96s=1, T6s=1, J6s=1, Q6s=1, K6s=1, A6s=1, 72o=1, 73o=1, 74o=1, 75o=1, 76o=1, 77=1, 87s=1, 97s=1, T7s=1, J7s=1, Q7s=1, K7s=1, A7s=1, 82o=1, 83o=1, 84o=1, 85o=1, 86o=1, 87o=1, 88=1, 98s=1, T8s=1, J8s=1, Q8s=1, K8s=1, A8s=1, 92o=1, 93o=1, 94o=1, 95o=1, 96o=1, 97o=1, 98o=1, 99=1, T9s=1, J9s=1, Q9s=1, K9s=1, A9s=1, T2o=1, T3o=1, T4o=1, T5o=1, T6o=1, T7o=1, T8o=1, T9o=1, TT=1, JTs=1, QTs=1, KTs=1, ATs=1, J2o=1, J3o=1, J4o=1, J5o=1, J6o=1, J7o=1, J8o=1, J9o=1, JTo=1, JJ=1, QJs=1, KJs=1, AJs=1, Q2o=1, Q3o=1, Q4o=1, Q5o=1, Q6o=1, Q7o=1, Q8o=1, Q9o=1, QTo=1, QJo=1, QQ=1, KQs=1, AQs=1, K2o=1, K3o=1, K4o=1, K5o=1, K6o=1, K7o=1, K8o=1, K9o=1, KTo=1, KJo=1, KQo=1, KK=1, AKs=1, A2o=1, A3o=1, A4o=1, A5o=1, A6o=1, A7o=1, A8o=1, A9o=1, ATo=1, AJo=1, AQo=1, AKo=1, AA=1}";
		register(new Rational(3, 2), sb, bb, new Rational(24409207, 77260583400L));

		// 2 bb
		sb = "{22=1, 52s=1, 62s=1, 72s=1, 82s=1, 92s=1, T2s=1, J2s=1, Q2s=1, K2s=1, A2s=1, 33=1, 43s=1, 53s=1, 63s=1, 73s=1, 83s=1, 93s=1, T3s=1, J3s=1, Q3s=1, K3s=1, A3s=1, 44=1, 54s=1, 64s=1, 74s=1, 84s=1, 94s=1, T4s=1, J4s=1, Q4s=1, K4s=1, A4s=1, 54o=1, 55=1, 65s=1, 75s=1, 85s=1, 95s=1, T5s=1, J5s=1, Q5s=1, K5s=1, A5s=1, 64o=1, 65o=1, 66=1, 76s=1, 86s=1, 96s=1, T6s=1, J6s=1, Q6s=1, K6s=1, A6s=1, 74o=1, 75o=1, 76o=1, 77=1, 87s=1, 97s=1, T7s=1, J7s=1, Q7s=1, K7s=1, A7s=1, 84o=1, 85o=1, 86o=1, 87o=1, 88=1, 98s=1, T8s=1, J8s=1, Q8s=1, K8s=1, A8s=1, 92o=1, 93o=1, 94o=1, 95o=1, 96o=1, 97o=1, 98o=1, 99=1, T9s=1, J9s=1, Q9s=1, K9s=1, A9s=1, T2o=1, T3o=1, T4o=1, T5o=1, T6o=1, T7o=1, T8o=1, T9o=1, TT=1, JTs=1, QTs=1, KTs=1, ATs=1, J2o=1, J3o=1, J4o=1, J5o=1, J6o=1, J7o=1, J8o=1, J9o=1, JTo=1, JJ=1, QJs=1, KJs=1, AJs=1, Q2o=1, Q3o=1, Q4o=1, Q5o=1, Q6o=1, Q7o=1, Q8o=1, Q9o=1, QTo=1, QJo=1, QQ=1, KQs=1, AQs=1, K2o=1, K3o=1, K4o=1, K5o=1, K6o=1, K7o=1, K8o=1, K9o=1, KTo=1, KJo=1, KQo=1, KK=1, AKs=1, A2o=1, A3o=1, A4o=1, A5o=1, A6o=1, A7o=1, A8o=1, A9o=1, ATo=1, AJo=1, AQo=1, AKo=1, AA=1}";
		bb = "{22=1, 32s=1, 42s=1, 52s=1, 62s=1, 72s=1, 82s=1, 92s=1, T2s=1, J2s=1, Q2s=1, K2s=1, A2s=1, 32o=1, 33=1, 43s=1, 53s=1, 63s=1, 73s=1, 83s=1, 93s=1, T3s=1, J3s=1, Q3s=1, K3s=1, A3s=1, 42o=1, 43o=1, 44=1, 54s=1, 64s=1, 74s=1, 84s=1, 94s=1, T4s=1, J4s=1, Q4s=1, K4s=1, A4s=1, 52o=1, 53o=1, 54o=1, 55=1, 65s=1, 75s=1, 85s=1, 95s=1, T5s=1, J5s=1, Q5s=1, K5s=1, A5s=1, 62o=1, 63o=1, 64o=1, 65o=1, 66=1, 76s=1, 86s=1, 96s=1, T6s=1, J6s=1, Q6s=1, K6s=1, A6s=1, 72o=1, 73o=1, 74o=1, 75o=1, 76o=1, 77=1, 87s=1, 97s=1, T7s=1, J7s=1, Q7s=1, K7s=1, A7s=1, 82o=1, 83o=1, 84o=1, 85o=1, 86o=1, 87o=1, 88=1, 98s=1, T8s=1, J8s=1, Q8s=1, K8s=1, A8s=1, 92o=1, 93o=1, 94o=1, 95o=1, 96o=1, 97o=1, 98o=1, 99=1, T9s=1, J9s=1, Q9s=1, K9s=1, A9s=1, T2o=1, T3o=1, T4o=1, T5o=1, T6o=1, T7o=1, T8o=1, T9o=1, TT=1, JTs=1, QTs=1, KTs=1, ATs=1, J2o=1, J3o=1, J4o=1, J5o=1, J6o=1, J7o=1, J8o=1, J9o=1, JTo=1, JJ=1, QJs=1, KJs=1, AJs=1, Q2o=1, Q3o=1, Q4o=1, Q5o=1, Q6o=1, Q7o=1, Q8o=1, Q9o=1, QTo=1, QJo=1, QQ=1, KQs=1, AQs=1, K2o=1, K3o=1, K4o=1, K5o=1, K6o=1, K7o=1, K8o=1, K9o=1, KTo=1, KJo=1, KQo=1, KK=1, AKs=1, A2o=1, A3o=1, A4o=1, A5o=1, A6o=1, A7o=1, A8o=1, A9o=1, ATo=1, AJo=1, AQo=1, AKo=1, AA=1}";
		register(new Rational(2), sb, bb, new Rational(3320088313L, 347672625300L));

		// 2.5 bb
		sb = "{22=1, 82s=1, 92s=1, T2s=1, J2s=1, Q2s=1, K2s=1, A2s=1, 33=1, 73s=1, 83s=1, 93s=1, T3s=1, J3s=1, Q3s=1, K3s=1, A3s=1, 44=1, 54s=1, 64s=1, 74s=1, 84s=1, 94s=1, T4s=1, J4s=1, Q4s=1, K4s=1, A4s=1, 55=1, 65s=1, 75s=1, 85s=1, 95s=1, T5s=1, J5s=1, Q5s=1, K5s=1, A5s=1, 66=1, 76s=1, 86s=1, 96s=1, T6s=1, J6s=1, Q6s=1, K6s=1, A6s=1, 75o=1, 76o=1, 77=1, 87s=1, 97s=1, T7s=1, J7s=1, Q7s=1, K7s=1, A7s=1, 85o=1, 86o=1, 87o=1, 88=1, 98s=1, T8s=1, J8s=1, Q8s=1, K8s=1, A8s=1, 93o=1, 94o=1, 95o=1, 96o=1, 97o=1, 98o=1, 99=1, T9s=1, J9s=1, Q9s=1, K9s=1, A9s=1, T2o=1, T3o=1, T4o=1, T5o=1, T6o=1, T7o=1, T8o=1, T9o=1, TT=1, JTs=1, QTs=1, KTs=1, ATs=1, J2o=1, J3o=1, J4o=1, J5o=1, J6o=1, J7o=1, J8o=1, J9o=1, JTo=1, JJ=1, QJs=1, KJs=1, AJs=1, Q2o=1, Q3o=1, Q4o=1, Q5o=1, Q6o=1, Q7o=1, Q8o=1, Q9o=1, QTo=1, QJo=1, QQ=1, KQs=1, AQs=1, K2o=1, K3o=1, K4o=1, K5o=1, K6o=1, K7o=1, K8o=1, K9o=1, KTo=1, KJo=1, KQo=1, KK=1, AKs=1, A2o=1, A3o=1, A4o=1, A5o=1, A6o=1, A7o=1, A8o=1, A9o=1, ATo=1, AJo=1, AQo=1, AKo=1, AA=1}";
		bb = "{22=1, 32s=1, 42s=1, 52s=1, 62s=1, 72s=1, 82s=1, 92s=1, T2s=1, J2s=1, Q2s=1, K2s=1, A2s=1, 32o=1, 33=1, 43s=1, 53s=1, 63s=1, 73s=1, 83s=1, 93s=1, T3s=1, J3s=1, Q3s=1, K3s=1, A3s=1, 42o=1, 43o=1, 44=1, 54s=1, 64s=1, 74s=1, 84s=1, 94s=1, T4s=1, J4s=1, Q4s=1, K4s=1, A4s=1, 52o=1, 53o=1, 54o=1, 55=1, 65s=1, 75s=1, 85s=1, 95s=1, T5s=1, J5s=1, Q5s=1, K5s=1, A5s=1, 62o=1, 63o=1, 64o=1, 65o=1, 66=1, 76s=1, 86s=1, 96s=1, T6s=1, J6s=1, Q6s=1, K6s=1, A6s=1, 72o=1, 73o=1, 74o=1, 75o=1, 76o=1, 77=1, 87s=1, 97s=1, T7s=1, J7s=1, Q7s=1, K7s=1, A7s=1, 82o=1, 83o=1, 84o=1, 85o=1, 86o=1, 87o=1, 88=1, 98s=1, T8s=1, J8s=1, Q8s=1, K8s=1, A8s=1, 92o=1, 93o=1, 94o=1, 95o=1, 96o=1, 97o=1, 98o=1, 99=1, T9s=1, J9s=1, Q9s=1, K9s=1, A9s=1, T2o=1, T3o=1, T4o=1, T5o=1, T6o=1, T7o=1, T8o=1, T9o=1, TT=1, JTs=1, QTs=1, KTs=1, ATs=1, J2o=1, J3o=1, J4o=1, J5o=1, J6o=1, J7o=1, J8o=1, J9o=1, JTo=1, JJ=1, QJs=1, KJs=1, AJs=1, Q2o=1, Q3o=1, Q4o=1, Q5o=1, Q6o=1, Q7o=1, Q8o=1, Q9o=1, QTo=1, QJo=1, QQ=1, KQs=1, AQs=1, K2o=1, K3o=1, K4o=1, K5o=1, K6o=1, K7o=1, K8o=1, K9o=1, KTo=1, KJo=1, KQo=1, KK=1, AKs=1, A2o=1, A3o=1, A4o=1, A5o=1, A6o=1, A7o=1, A8o=1, A9o=1, ATo=1, AJo=1, AQo=1, AKo=1, AA=1}";
		register(new Rational(5, 2), sb, bb, new Rational(788544640, 26744048100L));

		// 3 bb
		sb = "{22=1, 92s=1, T2s=1, J2s=1, Q2s=1, K2s=1, A2s=1, 33=1, 93s=1, T3s=1, J3s=1, Q3s=1, K3s=1, A3s=1, 44=1, 54s=1, 64s=1, 74s=1, 84s=1, 94s=1, T4s=1, J4s=1, Q4s=1, K4s=1, A4s=1, 55=1, 65s=1, 75s=1, 85s=1, 95s=1, T5s=1, J5s=1, Q5s=1, K5s=1, A5s=1, 66=1, 76s=1, 86s=1, 96s=1, T6s=1, J6s=1, Q6s=1, K6s=1, A6s=1, 76o=1, 77=1, 87s=1, 97s=1, T7s=1, J7s=1, Q7s=1, K7s=1, A7s=1, 85o=1, 86o=1, 87o=1, 88=1, 98s=1, T8s=1, J8s=1, Q8s=1, K8s=1, A8s=1, 95o=1, 96o=1, 97o=1, 98o=1, 99=1, T9s=1, J9s=1, Q9s=1, K9s=1, A9s=1, T2o=217109/630922, T3o=1, T4o=1, T5o=1, T6o=1, T7o=1, T8o=1, T9o=1, TT=1, JTs=1, QTs=1, KTs=1, ATs=1, J2o=1, J3o=1, J4o=1, J5o=1, J6o=1, J7o=1, J8o=1, J9o=1, JTo=1, JJ=1, QJs=1, KJs=1, AJs=1, Q2o=1, Q3o=1, Q4o=1, Q5o=1, Q6o=1, Q7o=1, Q8o=1, Q9o=1, QTo=1, QJo=1, QQ=1, KQs=1, AQs=1, K2o=1, K3o=1, K4o=1, K5o=1, K6o=1, K7o=1, K8o=1, K9o=1, KTo=1, KJo=1, KQo=1, KK=1, AKs=1, A2o=1, A3o=1, A4o=1, A5o=1, A6o=1, A7o=1, A8o=1, A9o=1, ATo=1, AJo=1, AQo=1, AKo=1, AA=1}";
		bb = "{22=1, 32s=1, 42s=1, 52s=1, 62s=1, 72s=1, 82s=1, 92s=1, T2s=1, J2s=1, Q2s=1, K2s=1, A2s=1, 33=1, 43s=1, 53s=1, 63s=1, 73s=1, 83s=1, 93s=1, T3s=1, J3s=1, Q3s=1, K3s=1, A3s=1, 43o=1, 44=1, 54s=1, 64s=1, 74s=1, 84s=1, 94s=1, T4s=1, J4s=1, Q4s=1, K4s=1, A4s=1, 53o=1, 54o=1, 55=1, 65s=1, 75s=1, 85s=1, 95s=1, T5s=1, J5s=1, Q5s=1, K5s=1, A5s=1, 63o=1748147/3785532, 64o=1, 65o=1, 66=1, 76s=1, 86s=1, 96s=1, T6s=1, J6s=1, Q6s=1, K6s=1, A6s=1, 74o=1, 75o=1, 76o=1, 77=1, 87s=1, 97s=1, T7s=1, J7s=1, Q7s=1, K7s=1, A7s=1, 84o=1, 85o=1, 86o=1, 87o=1, 88=1, 98s=1, T8s=1, J8s=1, Q8s=1, K8s=1, A8s=1, 92o=1, 93o=1, 94o=1, 95o=1, 96o=1, 97o=1, 98o=1, 99=1, T9s=1, J9s=1, Q9s=1, K9s=1, A9s=1, T2o=1, T3o=1, T4o=1, T5o=1, T6o=1, T7o=1, T8o=1, T9o=1, TT=1, JTs=1, QTs=1, KTs=1, ATs=1, J2o=1, J3o=1, J4o=1, J5o=1, J6o=1, J7o=1, J8o=1, J9o=1, JTo=1, JJ=1, QJs=1, KJs=1, AJs=1, Q2o=1, Q3o=1, Q4o=1, Q5o=1, Q6o=1, Q7o=1, Q8o=1, Q9o=1, QTo=1, QJo=1, QQ=1, KQs=1, AQs=1, K2o=1, K3o=1, K4o=1, K5o=1, K6o=1, K7o=1, K8o=1, K9o=1, KTo=1, KJo=1, KQo=1, KK=1, AKs=1, A2o=1, A3o=1, A4o=1, A5o=1, A6o=1, A7o=1, A8o=1, A9o=1, ATo=1, AJo=1, AQo=1, AKo=1, AA=1}";
		register(new Rational(3), sb, bb, new Rational(7544553883312945L, 146236205399684400L));

		// 3.5 bb
		sb = "{22=1, T2s=1, J2s=1, Q2s=1, K2s=1, A2s=1, 33=1, 93s=1, T3s=1, J3s=1, Q3s=1, K3s=1, A3s=1, 44=1, 54s=1, 64s=1, 74s=1, 84s=1, 94s=1, T4s=1, J4s=1, Q4s=1, K4s=1, A4s=1, 55=1, 65s=1, 75s=1, 85s=1, 95s=1, T5s=1, J5s=1, Q5s=1, K5s=1, A5s=1, 66=1, 76s=1, 86s=1, 96s=1, T6s=1, J6s=1, Q6s=1, K6s=1, A6s=1, 76o=1, 77=1, 87s=1, 97s=1, T7s=1, J7s=1, Q7s=1, K7s=1, A7s=1, 86o=1, 87o=1, 88=1, 98s=1, T8s=1, J8s=1, Q8s=1, K8s=1, A8s=1, 95o=8912898/9070177, 96o=1, 97o=1, 98o=1, 99=1, T9s=1, J9s=1, Q9s=1, K9s=1, A9s=1, T4o=1, T5o=1, T6o=1, T7o=1, T8o=1, T9o=1, TT=1, JTs=1, QTs=1, KTs=1, ATs=1, J2o=1, J3o=1, J4o=1, J5o=1, J6o=1, J7o=1, J8o=1, J9o=1, JTo=1, JJ=1, QJs=1, KJs=1, AJs=1, Q2o=1, Q3o=1, Q4o=1, Q5o=1, Q6o=1, Q7o=1, Q8o=1, Q9o=1, QTo=1, QJo=1, QQ=1, KQs=1, AQs=1, K2o=1, K3o=1, K4o=1, K5o=1, K6o=1, K7o=1, K8o=1, K9o=1, KTo=1, KJo=1, KQo=1, KK=1, AKs=1, A2o=1, A3o=1, A4o=1, A5o=1, A6o=1, A7o=1, A8o=1, A9o=1, ATo=1, AJo=1, AQo=1, AKo=1, AA=1}";
		bb = "{22=1, 52s=1, 82s=1, 92s=1, T2s=1, J2s=1, Q2s=1, K2s=1, A2s=1, 33=1, 43s=1, 53s=1, 63s=1, 73s=1, 83s=1, 93s=1, T3s=1, J3s=1, Q3s=1, K3s=1, A3s=1, 44=1, 54s=1, 64s=1, 74s=1, 84s=1, 94s=1, T4s=1, J4s=1, Q4s=1, K4s=1, A4s=1, 54o=8698733/9070177, 55=1, 65s=1, 75s=1, 85s=1, 95s=1, T5s=1, J5s=1, Q5s=1, K5s=1, A5s=1, 65o=1, 66=1, 76s=1, 86s=1, 96s=1, T6s=1, J6s=1, Q6s=1, K6s=1, A6s=1, 75o=1, 76o=1, 77=1, 87s=1, 97s=1, T7s=1, J7s=1, Q7s=1, K7s=1, A7s=1, 85o=1, 86o=1, 87o=1, 88=1, 98s=1, T8s=1, J8s=1, Q8s=1, K8s=1, A8s=1, 95o=1, 96o=1, 97o=1, 98o=1, 99=1, T9s=1, J9s=1, Q9s=1, K9s=1, A9s=1, T2o=1, T3o=1, T4o=1, T5o=1, T6o=1, T7o=1, T8o=1, T9o=1, TT=1, JTs=1, QTs=1, KTs=1, ATs=1, J2o=1, J3o=1, J4o=1, J5o=1, J6o=1, J7o=1, J8o=1, J9o=1, JTo=1, JJ=1, QJs=1, KJs=1, AJs=1, Q2o=1, Q3o=1, Q4o=1, Q5o=1, Q6o=1, Q7o=1, Q8o=1, Q9o=1, QTo=1, QJo=1, QQ=1, KQs=1, AQs=1, K2o=1, K3o=1, K4o=1, K5o=1, K6o=1, K7o=1, K8o=1, K9o=1, KTo=1, KJo=1, KQo=1, KK=1, AKs=1, A2o=1, A3o=1, A4o=1, A5o=1, A6o=1, A7o=1, A8o=1, A9o=1, ATo=1, AJo=1, AQo=1, AKo=1, AA=1}";
		register(new Rational(7, 2), sb, bb, new Rational(199611855696188128L, 3153452249525678100L));
		
		// 4 bb
		sb = "{22=1, T2s=1, J2s=1, Q2s=1, K2s=1, A2s=1, 33=1, 93s=1, T3s=1, J3s=1, Q3s=1, K3s=1, A3s=1, 44=1, 54s=1, 64s=1, 74s=1, 84s=1, 94s=1, T4s=1, J4s=1, Q4s=1, K4s=1, A4s=1, 55=1, 65s=1, 75s=1, 85s=1, 95s=1, T5s=1, J5s=1, Q5s=1, K5s=1, A5s=1, 66=1, 76s=1, 86s=1, 96s=1, T6s=1, J6s=1, Q6s=1, K6s=1, A6s=1, 76o=1, 77=1, 87s=1, 97s=1, T7s=1, J7s=1, Q7s=1, K7s=1, A7s=1, 86o=1, 87o=1, 88=1, 98s=1, T8s=1, J8s=1, Q8s=1, K8s=1, A8s=1, 96o=1, 97o=1, 98o=1, 99=1, T9s=1, J9s=1, Q9s=1, K9s=1, A9s=1, T5o=1, T6o=1, T7o=1, T8o=1, T9o=1, TT=1, JTs=1, QTs=1, KTs=1, ATs=1, J2o=1, J3o=1, J4o=1, J5o=1, J6o=1, J7o=1, J8o=1, J9o=1, JTo=1, JJ=1, QJs=1, KJs=1, AJs=1, Q2o=1, Q3o=1, Q4o=1, Q5o=1, Q6o=1, Q7o=1, Q8o=1, Q9o=1, QTo=1, QJo=1, QQ=1, KQs=1, AQs=1, K2o=1, K3o=1, K4o=1, K5o=1, K6o=1, K7o=1, K8o=1, K9o=1, KTo=1, KJo=1, KQo=1, KK=1, AKs=1, A2o=1, A3o=1, A4o=1, A5o=1, A6o=1, A7o=1, A8o=1, A9o=1, ATo=1, AJo=1, AQo=1, AKo=1, AA=1}";
		bb = "{22=1, T2s=1, J2s=1, Q2s=1, K2s=1, A2s=1, 33=1, 53s=1, 93s=1, T3s=1, J3s=1, Q3s=1, K3s=1, A3s=1, 44=1, 54s=1, 64s=1, 74s=1, 84s=1, 94s=1, T4s=1, J4s=1, Q4s=1, K4s=1, A4s=1, 55=1, 65s=1, 75s=1, 85s=1, 95s=1, T5s=1, J5s=1, Q5s=1, K5s=1, A5s=1, 66=1, 76s=1, 86s=1, 96s=1, T6s=1, J6s=1, Q6s=1, K6s=1, A6s=1, 76o=1, 77=1, 87s=1, 97s=1, T7s=1, J7s=1, Q7s=1, K7s=1, A7s=1, 86o=1, 87o=1, 88=1, 98s=1, T8s=1, J8s=1, Q8s=1, K8s=1, A8s=1, 96o=1, 97o=1, 98o=1, 99=1, T9s=1, J9s=1, Q9s=1, K9s=1, A9s=1, T5o=1, T6o=1, T7o=1, T8o=1, T9o=1, TT=1, JTs=1, QTs=1, KTs=1, ATs=1, J2o=1, J3o=1, J4o=1, J5o=1, J6o=1, J7o=1, J8o=1, J9o=1, JTo=1, JJ=1, QJs=1, KJs=1, AJs=1, Q2o=1, Q3o=1, Q4o=1, Q5o=1, Q6o=1, Q7o=1, Q8o=1, Q9o=1, QTo=1, QJo=1, QQ=1, KQs=1, AQs=1, K2o=1, K3o=1, K4o=1, K5o=1, K6o=1, K7o=1, K8o=1, K9o=1, KTo=1, KJo=1, KQo=1, KK=1, AKs=1, A2o=1, A3o=1, A4o=1, A5o=1, A6o=1, A7o=1, A8o=1, A9o=1, ATo=1, AJo=1, AQo=1, AKo=1, AA=1}";
		register(new Rational(4), sb, bb, new Rational(22957228796L, 347672625300L));

		// 4.5 bb
		sb = "{22=1, T2s=1, J2s=1, Q2s=1, K2s=1, A2s=1, 33=1, 53s=1, 93s=2957717/3145283, T3s=1, J3s=1, Q3s=1, K3s=1, A3s=1, 44=1, 54s=1, 64s=1, 74s=1, 84s=1, 94s=1, T4s=1, J4s=1, Q4s=1, K4s=1, A4s=1, 55=1, 65s=1, 75s=1, 85s=1, 95s=1, T5s=1, J5s=1, Q5s=1, K5s=1, A5s=1, 66=1, 76s=1, 86s=1, 96s=1, T6s=1, J6s=1, Q6s=1, K6s=1, A6s=1, 76o=1, 77=1, 87s=1, 97s=1, T7s=1, J7s=1, Q7s=1, K7s=1, A7s=1, 86o=1, 87o=1, 88=1, 98s=1, T8s=1, J8s=1, Q8s=1, K8s=1, A8s=1, 96o=1, 97o=1, 98o=1, 99=1, T9s=1, J9s=1, Q9s=1, K9s=1, A9s=1, T6o=1, T7o=1, T8o=1, T9o=1, TT=1, JTs=1, QTs=1, KTs=1, ATs=1, J2o=1, J3o=1, J4o=1, J5o=1, J6o=1, J7o=1, J8o=1, J9o=1, JTo=1, JJ=1, QJs=1, KJs=1, AJs=1, Q2o=1, Q3o=1, Q4o=1, Q5o=1, Q6o=1, Q7o=1, Q8o=1, Q9o=1, QTo=1, QJo=1, QQ=1, KQs=1, AQs=1, K2o=1, K3o=1, K4o=1, K5o=1, K6o=1, K7o=1, K8o=1, K9o=1, KTo=1, KJo=1, KQo=1, KK=1, AKs=1, A2o=1, A3o=1, A4o=1, A5o=1, A6o=1, A7o=1, A8o=1, A9o=1, ATo=1, AJo=1, AQo=1, AKo=1, AA=1}";
		bb = "{22=1, T2s=1, J2s=1, Q2s=1, K2s=1, A2s=1, 33=1, T3s=1, J3s=1, Q3s=1, K3s=1, A3s=1, 44=1, 54s=1, T4s=1, J4s=1, Q4s=1, K4s=1, A4s=1, 55=1, 65s=1, 75s=1, 85s=1, 95s=1, T5s=1, J5s=1, Q5s=1, K5s=1, A5s=1, 66=1, 76s=1, 86s=1, 96s=1, T6s=1, J6s=1, Q6s=1, K6s=1, A6s=1, 77=1, 87s=1, 97s=1, T7s=1, J7s=1, Q7s=1, K7s=1, A7s=1, 87o=1, 88=1, 98s=1, T8s=1, J8s=1, Q8s=1, K8s=1, A8s=1, 97o=1, 98o=1, 99=1, T9s=1, J9s=1, Q9s=1, K9s=1, A9s=1, T6o=1, T7o=1, T8o=1, T9o=1, TT=1, JTs=1, QTs=1, KTs=1, ATs=1, J3o=5911337/9435849, J4o=1, J5o=1, J6o=1, J7o=1, J8o=1, J9o=1, JTo=1, JJ=1, QJs=1, KJs=1, AJs=1, Q2o=1, Q3o=1, Q4o=1, Q5o=1, Q6o=1, Q7o=1, Q8o=1, Q9o=1, QTo=1, QJo=1, QQ=1, KQs=1, AQs=1, K2o=1, K3o=1, K4o=1, K5o=1, K6o=1, K7o=1, K8o=1, K9o=1, KTo=1, KJo=1, KQo=1, KK=1, AKs=1, A2o=1, A3o=1, A4o=1, A5o=1, A6o=1, A7o=1, A8o=1, A9o=1, ATo=1, AJo=1, AQo=1, AKo=1, AA=1}";
		register(new Rational(9, 2), sb, bb, new Rational(923826125060469L, 14727660578066800L));

		// 5 bb
		sb = "{22=1, T2s=1, J2s=1, Q2s=1, K2s=1, A2s=1, 33=1, 43s=1, 53s=1, T3s=1, J3s=1, Q3s=1, K3s=1, A3s=1, 44=1, 54s=1, 64s=1, 74s=1, 84s=1, 94s=1, T4s=1, J4s=1, Q4s=1, K4s=1, A4s=1, 55=1, 65s=1, 75s=1, 85s=1, 95s=1, T5s=1, J5s=1, Q5s=1, K5s=1, A5s=1, 66=1, 76s=1, 86s=1, 96s=1, T6s=1, J6s=1, Q6s=1, K6s=1, A6s=1, 76o=1, 77=1, 87s=1, 97s=1, T7s=1, J7s=1, Q7s=1, K7s=1, A7s=1, 86o=1, 87o=1, 88=1, 98s=1, T8s=1, J8s=1, Q8s=1, K8s=1, A8s=1, 96o=5692536/36412441, 97o=1, 98o=1, 99=1, T9s=1, J9s=1, Q9s=1, K9s=1, A9s=1, T6o=1, T7o=1, T8o=1, T9o=1, TT=1, JTs=1, QTs=1, KTs=1, ATs=1, J3o=1, J4o=1, J5o=1, J6o=1, J7o=1, J8o=1, J9o=1, JTo=1, JJ=1, QJs=1, KJs=1, AJs=1, Q2o=1, Q3o=1, Q4o=1, Q5o=1, Q6o=1, Q7o=1, Q8o=1, Q9o=1, QTo=1, QJo=1, QQ=1, KQs=1, AQs=1, K2o=1, K3o=1, K4o=1, K5o=1, K6o=1, K7o=1, K8o=1, K9o=1, KTo=1, KJo=1, KQo=1, KK=1, AKs=1, A2o=1, A3o=1, A4o=1, A5o=1, A6o=1, A7o=1, A8o=1, A9o=1, ATo=1, AJo=1, AQo=1, AKo=1, AA=1}";
		bb = "{22=1, J2s=1, Q2s=1, K2s=1, A2s=1, 33=1, J3s=1, Q3s=1, K3s=1, A3s=1, 44=1, T4s=1, J4s=1, Q4s=1, K4s=1, A4s=1, 55=1, 95s=1, T5s=1, J5s=1, Q5s=1, K5s=1, A5s=1, 66=1, 76s=1, 86s=1, 96s=1, T6s=1, J6s=1, Q6s=1, K6s=1, A6s=1, 77=1, 87s=1, 97s=1, T7s=1, J7s=1, Q7s=1, K7s=1, A7s=1, 88=1, 98s=1, T8s=1, J8s=1, Q8s=1, K8s=1, A8s=1, 97o=27369506/36412441, 98o=1, 99=1, T9s=1, J9s=1, Q9s=1, K9s=1, A9s=1, T7o=1, T8o=1, T9o=1, TT=1, JTs=1, QTs=1, KTs=1, ATs=1, J5o=1, J6o=1, J7o=1, J8o=1, J9o=1, JTo=1, JJ=1, QJs=1, KJs=1, AJs=1, Q2o=1, Q3o=1, Q4o=1, Q5o=1, Q6o=1, Q7o=1, Q8o=1, Q9o=1, QTo=1, QJo=1, QQ=1, KQs=1, AQs=1, K2o=1, K3o=1, K4o=1, K5o=1, K6o=1, K7o=1, K8o=1, K9o=1, KTo=1, KJo=1, KQo=1, KK=1, AKs=1, A2o=1, A3o=1, A4o=1, A5o=1, A6o=1, A7o=1, A8o=1, A9o=1, ATo=1, AJo=1, AQo=1, AKo=1, AA=1}";
		register(new Rational(5), sb, bb, new Rational("1422957015868430851", "25319217912102714600"));

		// 5.5 bb
		sb = "{22=1, T2s=1, J2s=1, Q2s=1, K2s=1, A2s=1, 33=1, 43s=1, 53s=1, T3s=1, J3s=1, Q3s=1, K3s=1, A3s=1, 44=1, 54s=1, 64s=1, 74s=1, 84s=1, 94s=1, T4s=1, J4s=1, Q4s=1, K4s=1, A4s=1, 55=1, 65s=1, 75s=1, 85s=1, 95s=1, T5s=1, J5s=1, Q5s=1, K5s=1, A5s=1, 66=1, 76s=1, 86s=1, 96s=1, T6s=1, J6s=1, Q6s=1, K6s=1, A6s=1, 76o=1, 77=1, 87s=1, 97s=1, T7s=1, J7s=1, Q7s=1, K7s=1, A7s=1, 86o=1, 87o=1, 88=1, 98s=1, T8s=1, J8s=1, Q8s=1, K8s=1, A8s=1, 97o=1, 98o=1, 99=1, T9s=1, J9s=1, Q9s=1, K9s=1, A9s=1, T6o=1968894/2346577, T7o=1, T8o=1, T9o=1, TT=1, JTs=1, QTs=1, KTs=1, ATs=1, J5o=1, J6o=1, J7o=1, J8o=1, J9o=1, JTo=1, JJ=1, QJs=1, KJs=1, AJs=1, Q2o=1, Q3o=1, Q4o=1, Q5o=1, Q6o=1, Q7o=1, Q8o=1, Q9o=1, QTo=1, QJo=1, QQ=1, KQs=1, AQs=1, K2o=1, K3o=1, K4o=1, K5o=1, K6o=1, K7o=1, K8o=1, K9o=1, KTo=1, KJo=1, KQo=1, KK=1, AKs=1, A2o=1, A3o=1, A4o=1, A5o=1, A6o=1, A7o=1, A8o=1, A9o=1, ATo=1, AJo=1, AQo=1, AKo=1, AA=1}";
		bb = "{22=1, J2s=1, Q2s=1, K2s=1, A2s=1, 33=1, J3s=1, Q3s=1, K3s=1, A3s=1, 44=1, J4s=1, Q4s=1, K4s=1, A4s=1, 55=1, J5s=1, Q5s=1, K5s=1, A5s=1, 66=1, 86s=1, 96s=1, T6s=1, J6s=1, Q6s=1, K6s=1, A6s=1, 77=1, 87s=1, 97s=1, T7s=1, J7s=1, Q7s=1, K7s=1, A7s=1, 88=1, 98s=1, T8s=1, J8s=1, Q8s=1, K8s=1, A8s=1, 98o=1, 99=1, T9s=1, J9s=1, Q9s=1, K9s=1, A9s=1, T7o=2057219/2346577, T8o=1, T9o=1, TT=1, JTs=1, QTs=1, KTs=1, ATs=1, J7o=1, J8o=1, J9o=1, JTo=1, JJ=1, QJs=1, KJs=1, AJs=1, Q2o=1, Q3o=1, Q4o=1, Q5o=1, Q6o=1, Q7o=1, Q8o=1, Q9o=1, QTo=1, QJo=1, QQ=1, KQs=1, AQs=1, K2o=1, K3o=1, K4o=1, K5o=1, K6o=1, K7o=1, K8o=1, K9o=1, KTo=1, KJo=1, KQo=1, KK=1, AKs=1, A2o=1, A3o=1, A4o=1, A5o=1, A6o=1, A7o=1, A8o=1, A9o=1, ATo=1, AJo=1, AQo=1, AKo=1, AA=1}";
		register(new Rational(11, 2), sb, bb, new Rational(44003145987701L, 921333242302200L));

		// 6 bb
		sb = "{22=1, T2s=1, J2s=1, Q2s=1, K2s=1, A2s=1, 33=1, 43s=1, 53s=1, T3s=1, J3s=1, Q3s=1, K3s=1, A3s=1, 44=1, 54s=1, 64s=1, 74s=1, 84s=1, T4s=1, J4s=1, Q4s=1, K4s=1, A4s=1, 55=1, 65s=1, 75s=1, 85s=1, 95s=1, T5s=1, J5s=1, Q5s=1, K5s=1, A5s=1, 66=1, 76s=1, 86s=1, 96s=1, T6s=1, J6s=1, Q6s=1, K6s=1, A6s=1, 76o=1, 77=1, 87s=1, 97s=1, T7s=1, J7s=1, Q7s=1, K7s=1, A7s=1, 86o=9055669/9143474, 87o=1, 88=1, 98s=1, T8s=1, J8s=1, Q8s=1, K8s=1, A8s=1, 97o=1, 98o=1, 99=1, T9s=1, J9s=1, Q9s=1, K9s=1, A9s=1, T7o=1, T8o=1, T9o=1, TT=1, JTs=1, QTs=1, KTs=1, ATs=1, J5o=1, J6o=1, J7o=1, J8o=1, J9o=1, JTo=1, JJ=1, QJs=1, KJs=1, AJs=1, Q2o=1, Q3o=1, Q4o=1, Q5o=1, Q6o=1, Q7o=1, Q8o=1, Q9o=1, QTo=1, QJo=1, QQ=1, KQs=1, AQs=1, K2o=1, K3o=1, K4o=1, K5o=1, K6o=1, K7o=1, K8o=1, K9o=1, KTo=1, KJo=1, KQo=1, KK=1, AKs=1, A2o=1, A3o=1, A4o=1, A5o=1, A6o=1, A7o=1, A8o=1, A9o=1, ATo=1, AJo=1, AQo=1, AKo=1, AA=1}";
		bb = "{22=1, Q2s=1, K2s=1, A2s=1, 33=1, Q3s=1, K3s=1, A3s=1, 44=1, J4s=1, Q4s=1, K4s=1, A4s=1, 55=1, J5s=1, Q5s=1, K5s=1, A5s=1, 66=1, T6s=1, J6s=1, Q6s=1, K6s=1, A6s=1, 77=1, 87s=1, 97s=1, T7s=1, J7s=1, Q7s=1, K7s=1, A7s=1, 88=1, 98s=1, T8s=1, J8s=1, Q8s=1, K8s=1, A8s=1, 98o=4567801/4571737, 99=1, T9s=1, J9s=1, Q9s=1, K9s=1, A9s=1, T8o=1, T9o=1, TT=1, JTs=1, QTs=1, KTs=1, ATs=1, J7o=1, J8o=1, J9o=1, JTo=1, JJ=1, QJs=1, KJs=1, AJs=1, Q4o=1, Q5o=1, Q6o=1, Q7o=1, Q8o=1, Q9o=1, QTo=1, QJo=1, QQ=1, KQs=1, AQs=1, K2o=1, K3o=1, K4o=1, K5o=1, K6o=1, K7o=1, K8o=1, K9o=1, KTo=1, KJo=1, KQo=1, KK=1, AKs=1, A2o=1, A3o=1, A4o=1, A5o=1, A6o=1, A7o=1, A8o=1, A9o=1, ATo=1, AJo=1, AQo=1, AKo=1, AA=1}";
		register(new Rational(6), sb, bb, new Rational(1189266424175762L, 31166035391591100L));

		// 6.5 bb
		sb = "{22=1, T2s=1, J2s=1, Q2s=1, K2s=1, A2s=1, 33=1, 43s=1, 53s=1, 63s=1, T3s=1, J3s=1, Q3s=1, K3s=1, A3s=1, 44=1, 54s=1, 64s=1, 74s=1, 84s=1, 94s=1, T4s=1, J4s=1, Q4s=1, K4s=1, A4s=1, 55=1, 65s=1, 75s=1, 85s=1, 95s=1, T5s=1, J5s=1, Q5s=1, K5s=1, A5s=1, 66=1, 76s=1, 86s=1, 96s=1, T6s=1, J6s=1, Q6s=1, K6s=1, A6s=1, 76o=1, 77=1, 87s=1, 97s=1, T7s=1, J7s=1, Q7s=1, K7s=1, A7s=1, 86o=1, 87o=1, 88=1, 98s=1, T8s=1, J8s=1, Q8s=1, K8s=1, A8s=1, 97o=1, 98o=1, 99=1, T9s=1, J9s=1, Q9s=1, K9s=1, A9s=1, T7o=1, T8o=1, T9o=1, TT=1, JTs=1, QTs=1, KTs=1, ATs=1, J6o=1, J7o=1, J8o=1, J9o=1, JTo=1, JJ=1, QJs=1, KJs=1, AJs=1, Q2o=1, Q3o=1, Q4o=1, Q5o=1, Q6o=1, Q7o=1, Q8o=1, Q9o=1, QTo=1, QJo=1, QQ=1, KQs=1, AQs=1, K2o=1, K3o=1, K4o=1, K5o=1, K6o=1, K7o=1, K8o=1, K9o=1, KTo=1, KJo=1, KQo=1, KK=1, AKs=1, A2o=1, A3o=1, A4o=1, A5o=1, A6o=1, A7o=1, A8o=1, A9o=1, ATo=1, AJo=1, AQo=1, AKo=1, AA=1}";
		bb = "{22=1, Q2s=1, K2s=1, A2s=1, 33=1, Q3s=1, K3s=1, A3s=1, 44=1, Q4s=1, K4s=1, A4s=1, 55=1, J5s=1, Q5s=1, K5s=1, A5s=1, 66=1, J6s=1, Q6s=1, K6s=1, A6s=1, 77=1, 87s=1, 97s=1, T7s=1, J7s=1, Q7s=1, K7s=1, A7s=1, 88=1, 98s=1, T8s=1, J8s=1, Q8s=1, K8s=1, A8s=1, 99=1, T9s=1, J9s=1, Q9s=1, K9s=1, A9s=1, T8o=1, T9o=1, TT=1, JTs=1, QTs=1, KTs=1, ATs=1, J8o=1, J9o=1, JTo=1, JJ=1, QJs=1, KJs=1, AJs=1, Q5o=1, Q6o=1, Q7o=1, Q8o=1, Q9o=1, QTo=1, QJo=1, QQ=1, KQs=1, AQs=1, K2o=1, K3o=1, K4o=1, K5o=1, K6o=1, K7o=1, K8o=1, K9o=1, KTo=1, KJo=1, KQo=1, KK=1, AKs=1, A2o=1, A3o=1, A4o=1, A5o=1, A6o=1, A7o=1, A8o=1, A9o=1, ATo=1, AJo=1, AQo=1, AKo=1, AA=1}";
		register(new Rational(13, 2), sb, bb, new Rational(38620450039L, 1390690501200L));

		// 7 bb
		sb = "{22=1, J2s=1, Q2s=1, K2s=1, A2s=1, 33=1, 43s=1, 53s=1, 63s=1, T3s=1, J3s=1, Q3s=1, K3s=1, A3s=1, 44=1, 54s=1, 64s=1, 74s=1, 84s=1, T4s=1, J4s=1, Q4s=1, K4s=1, A4s=1, 55=1, 65s=1, 75s=1, 85s=1, 95s=1, T5s=1, J5s=1, Q5s=1, K5s=1, A5s=1, 66=1, 76s=1, 86s=1, 96s=1, T6s=1, J6s=1, Q6s=1, K6s=1, A6s=1, 76o=1, 77=1, 87s=1, 97s=1, T7s=1, J7s=1, Q7s=1, K7s=1, A7s=1, 86o=48461563/62703678, 87o=1, 88=1, 98s=1, T8s=1, J8s=1, Q8s=1, K8s=1, A8s=1, 97o=1, 98o=1, 99=1, T9s=1, J9s=1, Q9s=1, K9s=1, A9s=1, T7o=1, T8o=1, T9o=1, TT=1, JTs=1, QTs=1, KTs=1, ATs=1, J7o=1, J8o=1, J9o=1, JTo=1, JJ=1, QJs=1, KJs=1, AJs=1, Q2o=1, Q3o=1, Q4o=1, Q5o=1, Q6o=1, Q7o=1, Q8o=1, Q9o=1, QTo=1, QJo=1, QQ=1, KQs=1, AQs=1, K2o=1, K3o=1, K4o=1, K5o=1, K6o=1, K7o=1, K8o=1, K9o=1, KTo=1, KJo=1, KQo=1, KK=1, AKs=1, A2o=1, A3o=1, A4o=1, A5o=1, A6o=1, A7o=1, A8o=1, A9o=1, ATo=1, AJo=1, AQo=1, AKo=1, AA=1}";
		bb = "{22=1, Q2s=1, K2s=1, A2s=1, 33=1, Q3s=1, K3s=1, A3s=1, 44=1, Q4s=1, K4s=1, A4s=1, 55=1, Q5s=1, K5s=1, A5s=1, 66=1, J6s=1, Q6s=1, K6s=1, A6s=1, 77=1, 97s=13756767/20901226, T7s=1, J7s=1, Q7s=1, K7s=1, A7s=1, 88=1, 98s=1, T8s=1, J8s=1, Q8s=1, K8s=1, A8s=1, 99=1, T9s=1, J9s=1, Q9s=1, K9s=1, A9s=1, T9o=1, TT=1, JTs=1, QTs=1, KTs=1, ATs=1, J8o=1, J9o=1, JTo=1, JJ=1, QJs=1, KJs=1, AJs=1, Q6o=1, Q7o=1, Q8o=1, Q9o=1, QTo=1, QJo=1, QQ=1, KQs=1, AQs=1, K2o=1, K3o=1, K4o=1, K5o=1, K6o=1, K7o=1, K8o=1, K9o=1, KTo=1, KJo=1, KQo=1, KK=1, AKs=1, A2o=1, A3o=1, A4o=1, A5o=1, A6o=1, A7o=1, A8o=1, A9o=1, ATo=1, AJo=1, AQo=1, AKo=1, AA=1}";
		register(new Rational(7), sb, bb, new Rational(35011277169791707L, 2076224032973890800L));

		// 7.5 bb
		sb = "{22=1, J2s=1, Q2s=1, K2s=1, A2s=1, 33=1, 43s=1, 53s=1, T3s=1, J3s=1, Q3s=1, K3s=1, A3s=1, 44=1, 54s=1, 64s=1, 74s=1, 84s=1, T4s=1, J4s=1, Q4s=1, K4s=1, A4s=1, 55=1, 65s=1, 75s=1, 85s=1, 95s=1, T5s=1, J5s=1, Q5s=1, K5s=1, A5s=1, 66=1, 76s=1, 86s=1, 96s=1, T6s=1, J6s=1, Q6s=1, K6s=1, A6s=1, 76o=1, 77=1, 87s=1, 97s=1, T7s=1, J7s=1, Q7s=1, K7s=1, A7s=1, 87o=1, 88=1, 98s=1, T8s=1, J8s=1, Q8s=1, K8s=1, A8s=1, 97o=1, 98o=1, 99=1, T9s=1, J9s=1, Q9s=1, K9s=1, A9s=1, T7o=1, T8o=1, T9o=1, TT=1, JTs=1, QTs=1, KTs=1, ATs=1, J7o=1, J8o=1, J9o=1, JTo=1, JJ=1, QJs=1, KJs=1, AJs=1, Q3o=1, Q4o=1, Q5o=1, Q6o=1, Q7o=1, Q8o=1, Q9o=1, QTo=1, QJo=1, QQ=1, KQs=1, AQs=1, K2o=1, K3o=1, K4o=1, K5o=1, K6o=1, K7o=1, K8o=1, K9o=1, KTo=1, KJo=1, KQo=1, KK=1, AKs=1, A2o=1, A3o=1, A4o=1, A5o=1, A6o=1, A7o=1, A8o=1, A9o=1, ATo=1, AJo=1, AQo=1, AKo=1, AA=1}";
		bb = "{22=1, K2s=1, A2s=1, 33=1, Q3s=1, K3s=1, A3s=1, 44=1, Q4s=1, K4s=1, A4s=1, 55=1, Q5s=1, K5s=1, A5s=1, 66=1, Q6s=1, K6s=1, A6s=1, 77=1, J7s=1, Q7s=1, K7s=1, A7s=1, 88=1, 98s=1, T8s=1, J8s=1, Q8s=1, K8s=1, A8s=1, 99=1, T9s=1, J9s=1, Q9s=1, K9s=1, A9s=1, T9o=1, TT=1, JTs=1, QTs=1, KTs=1, ATs=1, J8o=1, J9o=1, JTo=1, JJ=1, QJs=1, KJs=1, AJs=1, Q7o=1, Q8o=1, Q9o=1, QTo=1, QJo=1, QQ=1, KQs=1, AQs=1, K2o=1, K3o=1, K4o=1, K5o=1, K6o=1, K7o=1, K8o=1, K9o=1, KTo=1, KJo=1, KQo=1, KK=1, AKs=1, A2o=1, A3o=1, A4o=1, A5o=1, A6o=1, A7o=1, A8o=1, A9o=1, ATo=1, AJo=1, AQo=1, AKo=1, AA=1}";
		register(new Rational(15, 2), sb, bb, new Rational(1444111187L, 231781750200L));

		// 8 bb
		sb = "{22=1, J2s=1, Q2s=1, K2s=1, A2s=1, 33=1, 53s=1, J3s=1, Q3s=1, K3s=1, A3s=1, 44=1, 54s=1, 64s=1, 74s=1, T4s=1, J4s=1, Q4s=1, K4s=1, A4s=1, 55=1, 65s=1, 75s=1, 85s=1, 95s=1, T5s=1, J5s=1, Q5s=1, K5s=1, A5s=1, 66=1, 76s=1, 86s=1, 96s=1, T6s=1, J6s=1, Q6s=1, K6s=1, A6s=1, 76o=1, 77=1, 87s=1, 97s=1, T7s=1, J7s=1, Q7s=1, K7s=1, A7s=1, 87o=1, 88=1, 98s=1, T8s=1, J8s=1, Q8s=1, K8s=1, A8s=1, 97o=1, 98o=1, 99=1, T9s=1, J9s=1, Q9s=1, K9s=1, A9s=1, T7o=1, T8o=1, T9o=1, TT=1, JTs=1, QTs=1, KTs=1, ATs=1, J7o=1, J8o=1, J9o=1, JTo=1, JJ=1, QJs=1, KJs=1, AJs=1, Q4o=378937/1976868, Q5o=1, Q6o=1, Q7o=1, Q8o=1, Q9o=1, QTo=1, QJo=1, QQ=1, KQs=1, AQs=1, K2o=1, K3o=1, K4o=1, K5o=1, K6o=1, K7o=1, K8o=1, K9o=1, KTo=1, KJo=1, KQo=1, KK=1, AKs=1, A2o=1, A3o=1, A4o=1, A5o=1, A6o=1, A7o=1, A8o=1, A9o=1, ATo=1, AJo=1, AQo=1, AKo=1, AA=1}";
		bb = "{22=1, K2s=1, A2s=1, 33=1, K3s=1, A3s=1, 44=1, Q4s=1, K4s=1, A4s=1, 55=1, Q5s=1, K5s=1, A5s=1, 66=1, Q6s=1, K6s=1, A6s=1, 77=1, J7s=1, Q7s=1, K7s=1, A7s=1, 88=1, 98s=1, T8s=1, J8s=1, Q8s=1, K8s=1, A8s=1, 99=1, T9s=1, J9s=1, Q9s=1, K9s=1, A9s=1, T9o=1, TT=1, JTs=1, QTs=1, KTs=1, ATs=1, J9o=1, JTo=1, JJ=1, QJs=1, KJs=1, AJs=1, Q7o=1567051/2965302, Q8o=1, Q9o=1, QTo=1, QJo=1, QQ=1, KQs=1, AQs=1, K2o=1, K3o=1, K4o=1, K5o=1, K6o=1, K7o=1, K8o=1, K9o=1, KTo=1, KJo=1, KQo=1, KK=1, AKs=1, A2o=1, A3o=1, A4o=1, A5o=1, A6o=1, A7o=1, A8o=1, A9o=1, ATo=1, AJo=1, AQo=1, AKo=1, AA=1}";
		register(new Rational(8), sb, bb, new Rational(-236974729961854L, 57275240619296700L));

		// 8.5bb
		sb = "{22=1, J2s=1, Q2s=1, K2s=1, A2s=1, 33=1, 43s=3710830970482/7788586737071, 53s=1, J3s=1, Q3s=1, K3s=1, A3s=1, 44=1, 54s=1, 64s=1, 74s=1, 84s=1, T4s=1, J4s=1, Q4s=1, K4s=1, A4s=1, 55=1, 65s=1, 75s=1, 85s=1, 95s=1, T5s=1, J5s=1, Q5s=1, K5s=1, A5s=1, 66=1, 76s=1, 86s=1, 96s=1, T6s=1, J6s=1, Q6s=1, K6s=1, A6s=1, 76o=1, 77=1, 87s=1, 97s=1, T7s=1, J7s=1, Q7s=1, K7s=1, A7s=1, 87o=1, 88=1, 98s=1, T8s=1, J8s=1, Q8s=1, K8s=1, A8s=1, 97o=1, 98o=1, 99=1, T9s=1, J9s=1, Q9s=1, K9s=1, A9s=1, T7o=1, T8o=1, T9o=1, TT=1, JTs=1, QTs=1, KTs=1, ATs=1, J7o=286348586080442/350486403168195, J8o=1, J9o=1, JTo=1, JJ=1, QJs=1, KJs=1, AJs=1, Q5o=1, Q6o=1, Q7o=1, Q8o=1, Q9o=1, QTo=1, QJo=1, QQ=1, KQs=1, AQs=1, K2o=1, K3o=1, K4o=1, K5o=1, K6o=1, K7o=1, K8o=1, K9o=1, KTo=1, KJo=1, KQo=1, KK=1, AKs=1, A2o=1, A3o=1, A4o=1, A5o=1, A6o=1, A7o=1, A8o=1, A9o=1, ATo=1, AJo=1, AQo=1, AKo=1, AA=1}";
		bb = "{22=1, K2s=1, A2s=1, 33=1, K3s=1, A3s=1, 44=1, Q4s=14604492207/37283804390, K4s=1, A4s=1, 55=1, Q5s=1, K5s=1, A5s=1, 66=1, Q6s=1, K6s=1, A6s=1, 77=1, J7s=1, Q7s=1, K7s=1, A7s=1, 88=1, T8s=1, J8s=1, Q8s=1, K8s=1, A8s=1, 99=1, T9s=1, J9s=1, Q9s=1, K9s=1, A9s=1, T9o=274132628804114/817801607392455, TT=1, JTs=1, QTs=1, KTs=1, ATs=1, J9o=1, JTo=1, JJ=1, QJs=1, KJs=1, AJs=1, Q8o=1, Q9o=1, QTo=1, QJo=1, QQ=1, KQs=1, AQs=1, K3o=1, K4o=1, K5o=1, K6o=1, K7o=1, K8o=1, K9o=1, KTo=1, KJo=1, KQo=1, KK=1, AKs=1, A2o=1, A3o=1, A4o=1, A5o=1, A6o=1, A7o=1, A8o=1, A9o=1, ATo=1, AJo=1, AQo=1, AKo=1, AA=1}";
		register(new Rational(17, 2), sb, bb, new Rational("-197529820842413923869998", "13539391991271177012481500"));
		
		// 9 bb
		sb = "{22=1, Q2s=1, K2s=1, A2s=1, 33=1, 43s=1, 53s=1, J3s=1, Q3s=1, K3s=1, A3s=1, 44=1, 54s=1, 64s=1, 74s=1, T4s=1, J4s=1, Q4s=1, K4s=1, A4s=1, 55=1, 65s=1, 75s=1, 85s=1, 95s=1, T5s=1, J5s=1, Q5s=1, K5s=1, A5s=1, 66=1, 76s=1, 86s=1, 96s=1, T6s=1, J6s=1, Q6s=1, K6s=1, A6s=1, 76o=1, 77=1, 87s=1, 97s=1, T7s=1, J7s=1, Q7s=1, K7s=1, A7s=1, 87o=1, 88=1, 98s=1, T8s=1, J8s=1, Q8s=1, K8s=1, A8s=1, 97o=1, 98o=1, 99=1, T9s=1, J9s=1, Q9s=1, K9s=1, A9s=1, T7o=1, T8o=1, T9o=1, TT=1, JTs=1, QTs=1, KTs=1, ATs=1, J8o=1, J9o=1, JTo=1, JJ=1, QJs=1, KJs=1, AJs=1, Q6o=1, Q7o=1, Q8o=1, Q9o=1, QTo=1, QJo=1, QQ=1, KQs=1, AQs=1, K2o=1, K3o=1, K4o=1, K5o=1, K6o=1, K7o=1, K8o=1, K9o=1, KTo=1, KJo=1, KQo=1, KK=1, AKs=1, A2o=1, A3o=1, A4o=1, A5o=1, A6o=1, A7o=1, A8o=1, A9o=1, ATo=1, AJo=1, AQo=1, AKo=1, AA=1}";
		bb = "{22=1, K2s=1, A2s=1, 33=1, K3s=1, A3s=1, 44=1, K4s=1, A4s=1, 55=1, K5s=1, A5s=1, 66=1, Q6s=1, K6s=1, A6s=1, 77=1, Q7s=1, K7s=1, A7s=1, 88=1, T8s=1, J8s=1, Q8s=1, K8s=1, A8s=1, 99=1, T9s=1, J9s=1, Q9s=1, K9s=1, A9s=1, TT=1, JTs=1, QTs=1, KTs=1, ATs=1, J9o=1, JTo=1, JJ=1, QJs=1, KJs=1, AJs=1, Q8o=1, Q9o=1, QTo=1, QJo=1, QQ=1, KQs=1, AQs=1, K4o=1, K5o=1, K6o=1, K7o=1, K8o=1, K9o=1, KTo=1, KJo=1, KQo=1, KK=1, AKs=1, A2o=1, A3o=1, A4o=1, A5o=1, A6o=1, A7o=1, A8o=1, A9o=1, ATo=1, AJo=1, AQo=1, AKo=1, AA=1}";
		register(new Rational(9), sb, bb, new Rational(-39776383, 1576746600));

		// 9.5 bb
		sb = "{22=1, Q2s=1, K2s=1, A2s=1, 33=1, 43s=9907666/21608017, 53s=1, J3s=1, Q3s=1, K3s=1, A3s=1, 44=1, 54s=1, 64s=1, 74s=1, T4s=1, J4s=1, Q4s=1, K4s=1, A4s=1, 55=1, 65s=1, 75s=1, 85s=1, 95s=1, T5s=1, J5s=1, Q5s=1, K5s=1, A5s=1, 66=1, 76s=1, 86s=1, 96s=1, T6s=1, J6s=1, Q6s=1, K6s=1, A6s=1, 76o=1, 77=1, 87s=1, 97s=1, T7s=1, J7s=1, Q7s=1, K7s=1, A7s=1, 87o=1, 88=1, 98s=1, T8s=1, J8s=1, Q8s=1, K8s=1, A8s=1, 97o=1, 98o=1, 99=1, T9s=1, J9s=1, Q9s=1, K9s=1, A9s=1, T8o=1, T9o=1, TT=1, JTs=1, QTs=1, KTs=1, ATs=1, J8o=1, J9o=1, JTo=1, JJ=1, QJs=1, KJs=1, AJs=1, Q6o=1, Q7o=1, Q8o=1, Q9o=1, QTo=1, QJo=1, QQ=1, KQs=1, AQs=1, K2o=1, K3o=1, K4o=1, K5o=1, K6o=1, K7o=1, K8o=1, K9o=1, KTo=1, KJo=1, KQo=1, KK=1, AKs=1, A2o=1, A3o=1, A4o=1, A5o=1, A6o=1, A7o=1, A8o=1, A9o=1, ATo=1, AJo=1, AQo=1, AKo=1, AA=1}";
		bb = "{22=1, K2s=1, A2s=1, 33=1, K3s=1, A3s=1, 44=1, K4s=1, A4s=1, 55=1, K5s=1, A5s=1, 66=1, Q6s=1, K6s=1, A6s=1, 77=1, Q7s=1, K7s=1, A7s=1, 88=1, J8s=1, Q8s=1, K8s=1, A8s=1, 99=1, T9s=1, J9s=1, Q9s=1, K9s=1, A9s=1, TT=1, JTs=1, QTs=1, KTs=1, ATs=1, J9o=39087371/64824051, JTo=1, JJ=1, QJs=1, KJs=1, AJs=1, Q8o=1, Q9o=1, QTo=1, QJo=1, QQ=1, KQs=1, AQs=1, K5o=1, K6o=1, K7o=1, K8o=1, K9o=1, KTo=1, KJo=1, KQo=1, KK=1, AKs=1, A2o=1, A3o=1, A4o=1, A5o=1, A6o=1, A7o=1, A8o=1, A9o=1, ATo=1, AJo=1, AQo=1, AKo=1, AA=1}";
		register(new Rational(19, 2), sb, bb, new Rational(-264146146671736510L, 7512515997917030100L));

		// 10 bb
		sb = "{22=1, Q2s=1, K2s=1, A2s=1, 33=1, 43s=8731989/12285638, 53s=1, J3s=1, Q3s=1, K3s=1, A3s=1, 44=1, 54s=1, 64s=1, 74s=1, 84s=1, T4s=1, J4s=1, Q4s=1, K4s=1, A4s=1, 55=1, 65s=1, 75s=1, 85s=1, 95s=1, T5s=1, J5s=1, Q5s=1, K5s=1, A5s=1, 66=1, 76s=1, 86s=1, 96s=1, T6s=1, J6s=1, Q6s=1, K6s=1, A6s=1, 76o=1, 77=1, 87s=1, 97s=1, T7s=1, J7s=1, Q7s=1, K7s=1, A7s=1, 87o=1, 88=1, 98s=1, T8s=1, J8s=1, Q8s=1, K8s=1, A8s=1, 97o=1, 98o=1, 99=1, T9s=1, J9s=1, Q9s=1, K9s=1, A9s=1, T8o=1, T9o=1, TT=1, JTs=1, QTs=1, KTs=1, ATs=1, J8o=1, J9o=1, JTo=1, JJ=1, QJs=1, KJs=1, AJs=1, Q7o=1, Q8o=1, Q9o=1, QTo=1, QJo=1, QQ=1, KQs=1, AQs=1, K2o=1, K3o=1, K4o=1, K5o=1, K6o=1, K7o=1, K8o=1, K9o=1, KTo=1, KJo=1, KQo=1, KK=1, AKs=1, A2o=1, A3o=1, A4o=1, A5o=1, A6o=1, A7o=1, A8o=1, A9o=1, ATo=1, AJo=1, AQo=1, AKo=1, AA=1}";
		bb = "{22=1, K2s=1, A2s=1, 33=1, K3s=1, A3s=1, 44=1, K4s=1, A4s=1, 55=1, K5s=1, A5s=1, 66=1, Q6s=4948399/12285638, K6s=1, A6s=1, 77=1, Q7s=1, K7s=1, A7s=1, 88=1, J8s=1, Q8s=1, K8s=1, A8s=1, 99=1, T9s=1, J9s=1, Q9s=1, K9s=1, A9s=1, TT=1, JTs=1, QTs=1, KTs=1, ATs=1, JTo=1, JJ=1, QJs=1, KJs=1, AJs=1, Q9o=1, QTo=1, QJo=1, QQ=1, KQs=1, AQs=1, K5o=1, K6o=1, K7o=1, K8o=1, K9o=1, KTo=1, KJo=1, KQo=1, KK=1, AKs=1, A2o=1, A3o=1, A4o=1, A5o=1, A6o=1, A7o=1, A8o=1, A9o=1, ATo=1, AJo=1, AQo=1, AKo=1, AA=1}";
		register(new Rational(10), sb, bb, new Rational(-193795725978624491L, 4271380016945441400L));

		// 11 bb
		sb = "{22=1, Q2s=1, K2s=1, A2s=1, 33=1, 53s=1, Q3s=1, K3s=1, A3s=1, 44=1, 54s=1, 64s=1, 74s=1, J4s=1, Q4s=1, K4s=1, A4s=1, 55=1, 65s=1, 75s=1, 85s=1, 95s=1, T5s=1, J5s=1, Q5s=1, K5s=1, A5s=1, 66=1, 76s=1, 86s=1, 96s=1, T6s=1, J6s=1, Q6s=1, K6s=1, A6s=1, 76o=393733/7195524, 77=1, 87s=1, 97s=1, T7s=1, J7s=1, Q7s=1, K7s=1, A7s=1, 87o=1, 88=1, 98s=1, T8s=1, J8s=1, Q8s=1, K8s=1, A8s=1, 98o=1, 99=1, T9s=1, J9s=1, Q9s=1, K9s=1, A9s=1, T8o=1, T9o=1, TT=1, JTs=1, QTs=1, KTs=1, ATs=1, J8o=1, J9o=1, JTo=1, JJ=1, QJs=1, KJs=1, AJs=1, Q8o=1, Q9o=1, QTo=1, QJo=1, QQ=1, KQs=1, AQs=1, K2o=1, K3o=1, K4o=1, K5o=1, K6o=1, K7o=1, K8o=1, K9o=1, KTo=1, KJo=1, KQo=1, KK=1, AKs=1, A2o=1, A3o=1, A4o=1, A5o=1, A6o=1, A7o=1, A8o=1, A9o=1, ATo=1, AJo=1, AQo=1, AKo=1, AA=1}";
		bb = "{22=1, A2s=1, 33=1, K3s=1, A3s=1, 44=1, K4s=1, A4s=1, 55=1, K5s=1, A5s=1, 66=1, K6s=1, A6s=1, 77=1, K7s=1, A7s=1, 88=1, Q8s=1, K8s=1, A8s=1, 99=1, T9s=1, J9s=1, Q9s=1, K9s=1, A9s=1, TT=1, JTs=1, QTs=1, KTs=1, ATs=1, JTo=1, JJ=1, QJs=1, KJs=1, AJs=1, Q9o=1, QTo=1, QJo=1, QQ=1, KQs=1, AQs=1, K6o=1314617/2398508, K7o=1, K8o=1, K9o=1, KTo=1, KJo=1, KQo=1, KK=1, AKs=1, A2o=1, A3o=1, A4o=1, A5o=1, A6o=1, A7o=1, A8o=1, A9o=1, ATo=1, AJo=1, AQo=1, AKo=1, AA=1}";
		register(new Rational(11), sb, bb, new Rational(-69529815780025L, 1075300545664800L));
	
		// 12 bb
		sb = "{22=1, Q2s=1, K2s=1, A2s=1, 33=1, 53s=1, Q3s=1, K3s=1, A3s=1, 44=1, 54s=1, 64s=1, 74s=1, J4s=1, Q4s=1, K4s=1, A4s=1, 55=1, 65s=1, 75s=1, 85s=1, 95s=1, J5s=1, Q5s=1, K5s=1, A5s=1, 66=1, 76s=1, 86s=1, 96s=1, T6s=1, J6s=1, Q6s=1, K6s=1, A6s=1, 77=1, 87s=1, 97s=1, T7s=1, J7s=1, Q7s=1, K7s=1, A7s=1, 87o=1, 88=1, 98s=1, T8s=1, J8s=1, Q8s=1, K8s=1, A8s=1, 98o=1, 99=1, T9s=1, J9s=1, Q9s=1, K9s=1, A9s=1, T8o=1, T9o=1, TT=1, JTs=1, QTs=1, KTs=1, ATs=1, J8o=1, J9o=1, JTo=1, JJ=1, QJs=1, KJs=1, AJs=1, Q8o=1, Q9o=1, QTo=1, QJo=1, QQ=1, KQs=1, AQs=1, K3o=1, K4o=1, K5o=1, K6o=1, K7o=1, K8o=1, K9o=1, KTo=1, KJo=1, KQo=1, KK=1, AKs=1, A2o=1, A3o=1, A4o=1, A5o=1, A6o=1, A7o=1, A8o=1, A9o=1, ATo=1, AJo=1, AQo=1, AKo=1, AA=1}";
		bb = "{22=1, A2s=1, 33=1, A3s=1, 44=1, K4s=1, A4s=1, 55=1, K5s=1, A5s=1, 66=1, K6s=1, A6s=1, 77=1, K7s=1, A7s=1, 88=1, Q8s=1, K8s=1, A8s=1, 99=1, J9s=1, Q9s=1, K9s=1, A9s=1, TT=1, JTs=1, QTs=1, KTs=1, ATs=1, JTo=1, JJ=1, QJs=1, KJs=1, AJs=1, QTo=1, QJo=1, QQ=1, KQs=1, AQs=1, K7o=1, K8o=1, K9o=1, KTo=1, KJo=1, KQo=1, KK=1, AKs=1, A2o=1, A3o=1, A4o=1, A5o=1, A6o=1, A7o=1, A8o=1, A9o=1, ATo=1, AJo=1, AQo=1, AKo=1, AA=1}";
		register(new Rational(12), sb, bb, new Rational(-735719134, 8914682700L));

		// 13 bb
		sb = "{22=1, K2s=1, A2s=1, 33=1, Q3s=1, K3s=1, A3s=1, 44=1, 54s=1, 64s=1, 74s=1, J4s=1, Q4s=1, K4s=1, A4s=1, 55=1, 65s=1, 75s=1, 85s=1, 95s=1, J5s=1, Q5s=1, K5s=1, A5s=1, 66=1, 76s=1, 86s=1, 96s=1, T6s=1, J6s=1, Q6s=1, K6s=1, A6s=1, 77=1, 87s=1, 97s=1, T7s=1, J7s=1, Q7s=1, K7s=1, A7s=1, 87o=1, 88=1, 98s=1, T8s=1, J8s=1, Q8s=1, K8s=1, A8s=1, 98o=1, 99=1, T9s=1, J9s=1, Q9s=1, K9s=1, A9s=1, T8o=1, T9o=1, TT=1, JTs=1, QTs=1, KTs=1, ATs=1, J8o=83945656/103402081, J9o=1, JTo=1, JJ=1, QJs=1, KJs=1, AJs=1, Q8o=1, Q9o=1, QTo=1, QJo=1, QQ=1, KQs=1, AQs=1, K4o=1, K5o=1, K6o=1, K7o=1, K8o=1, K9o=1, KTo=1, KJo=1, KQo=1, KK=1, AKs=1, A2o=1, A3o=1, A4o=1, A5o=1, A6o=1, A7o=1, A8o=1, A9o=1, ATo=1, AJo=1, AQo=1, AKo=1, AA=1}";
		bb = "{22=1, A2s=1, 33=1, A3s=1, 44=1, A4s=1, 55=1, K5s=1, A5s=1, 66=1, K6s=1, A6s=1, 77=1, K7s=1, A7s=1, 88=1, Q8s=1, K8s=1, A8s=1, 99=1, J9s=1, Q9s=1, K9s=1, A9s=1, TT=1, JTs=1, QTs=1, KTs=1, ATs=1, JTo=3649501/103402081, JJ=1, QJs=1, KJs=1, AJs=1, QTo=1, QJo=1, QQ=1, KQs=1, AQs=1, K8o=1, K9o=1, KTo=1, KJo=1, KQo=1, KK=1, AKs=1, A2o=1, A3o=1, A4o=1, A5o=1, A6o=1, A7o=1, A8o=1, A9o=1, ATo=1, AJo=1, AQo=1, AKo=1, AA=1}";
		register(new Rational(13), sb, bb, new Rational("-323620883337597694", "3268188451159386300"));

		// 14 bb
		sb = "{22=1, K2s=1, A2s=1, 33=1, K3s=1, A3s=1, 44=1, 54s=1, 64s=1, Q4s=1, K4s=1, A4s=1, 55=1, 65s=1, 75s=1, 85s=1, 95s=1, J5s=1, Q5s=1, K5s=1, A5s=1, 66=1, 76s=1, 86s=1, 96s=1, T6s=1, J6s=1, Q6s=1, K6s=1, A6s=1, 77=1, 87s=1, 97s=1, T7s=1, J7s=1, Q7s=1, K7s=1, A7s=1, 87o=1, 88=1, 98s=1, T8s=1, J8s=1, Q8s=1, K8s=1, A8s=1, 98o=1, 99=1, T9s=1, J9s=1, Q9s=1, K9s=1, A9s=1, T8o=1, T9o=1, TT=1, JTs=1, QTs=1, KTs=1, ATs=1, J8o=15846080/55056889, J9o=1, JTo=1, JJ=1, QJs=1, KJs=1, AJs=1, Q9o=1, QTo=1, QJo=1, QQ=1, KQs=1, AQs=1, K5o=1, K6o=1, K7o=1, K8o=1, K9o=1, KTo=1, KJo=1, KQo=1, KK=1, AKs=1, A2o=1, A3o=1, A4o=1, A5o=1, A6o=1, A7o=1, A8o=1, A9o=1, ATo=1, AJo=1, AQo=1, AKo=1, AA=1}";
		bb = "{22=1, A2s=1, 33=1, A3s=1, 44=1, A4s=1, 55=1, A5s=1, 66=1, K6s=1, A6s=1, 77=1, K7s=1, A7s=1, 88=1, K8s=1, A8s=1, 99=1, Q9s=1, K9s=1, A9s=1, TT=1, JTs=1, QTs=1, KTs=1, ATs=1, JJ=1, QJs=1, KJs=1, AJs=1, QTo=1, QJo=1, QQ=1, KQs=1, AQs=1, K8o=11502434/55056889, K9o=1, KTo=1, KJo=1, KQo=1, KK=1, AKs=1, A2o=1, A3o=1, A4o=1, A5o=1, A6o=1, A7o=1, A8o=1, A9o=1, ATo=1, AJo=1, AQo=1, AKo=1, AA=1}";
		register(new Rational(14), sb, bb, new Rational(-726246600302342425L, 6380591046493563900L));
		
		// 15 bb
		sb = "{22=1, K2s=1, A2s=1, 33=1, K3s=1, A3s=1, 44=1, 54s=1, 64s=1, Q4s=9219319/10911294, K4s=1, A4s=1, 55=1, 65s=1, 75s=1, 85s=1, Q5s=1, K5s=1, A5s=1, 66=1, 76s=1, 86s=1, 96s=1, T6s=1, J6s=1, Q6s=1, K6s=1, A6s=1, 77=1, 87s=1, 97s=1, T7s=1, J7s=1, Q7s=1, K7s=1, A7s=1, 88=1, 98s=1, T8s=1, J8s=1, Q8s=1, K8s=1, A8s=1, 98o=1, 99=1, T9s=1, J9s=1, Q9s=1, K9s=1, A9s=1, T8o=1, T9o=1, TT=1, JTs=1, QTs=1, KTs=1, ATs=1, J9o=1, JTo=1, JJ=1, QJs=1, KJs=1, AJs=1, Q9o=1, QTo=1, QJo=1, QQ=1, KQs=1, AQs=1, K6o=1, K7o=1, K8o=1, K9o=1, KTo=1, KJo=1, KQo=1, KK=1, AKs=1, A2o=1, A3o=1, A4o=1, A5o=1, A6o=1, A7o=1, A8o=1, A9o=1, ATo=1, AJo=1, AQo=1, AKo=1, AA=1}";
		bb = "{22=1, A2s=1, 33=1, A3s=1, 44=1, A4s=1, 55=1, A5s=1, 66=1, A6s=1, 77=1, K7s=1, A7s=1, 88=1, K8s=1, A8s=1, 99=1, Q9s=1, K9s=1, A9s=1, TT=1, JTs=1, QTs=1, KTs=1, ATs=1, JJ=1, QJs=1, KJs=1, AJs=1, QTo=3881177/5455647, QJo=1, QQ=1, KQs=1, AQs=1, K9o=1, KTo=1, KJo=1, KQo=1, KK=1, AKs=1, A2o=1, A3o=1, A4o=1, A5o=1, A6o=1, A7o=1, A8o=1, A9o=1, ATo=1, AJo=1, AQo=1, AKo=1, AA=1}";
		register(new Rational(15), sb, bb, new Rational(-53627003340653761L, 421506470044459800L));

		// 17.5bb
		sb = "{22=1, K2s=1, A2s=1, 33=1, K3s=1, A3s=1, 44=1, 54s=1, K4s=1, A4s=1, 55=1, 65s=1, 75s=1, 85s=1, Q5s=1, K5s=1, A5s=1, 66=1, 76s=1, 86s=1, 96s=1, T6s=1, J6s=1, Q6s=1, K6s=1, A6s=1, 77=1, 87s=1, 97s=1, T7s=1, J7s=1, Q7s=1, K7s=1, A7s=1, 88=1, 98s=1, T8s=1, J8s=1, Q8s=1, K8s=1, A8s=1, 98o=1, 99=1, T9s=1, J9s=1, Q9s=1, K9s=1, A9s=1, T8o=10936517/12940566, T9o=1, TT=1, JTs=1, QTs=1, KTs=1, ATs=1, J9o=1, JTo=1, JJ=1, QJs=1, KJs=1, AJs=1, Q9o=1, QTo=1, QJo=1, QQ=1, KQs=1, AQs=1, K8o=1, K9o=1, KTo=1, KJo=1, KQo=1, KK=1, AKs=1, A2o=1, A3o=1, A4o=1, A5o=1, A6o=1, A7o=1, A8o=1, A9o=1, ATo=1, AJo=1, AQo=1, AKo=1, AA=1}";
		bb = "{A2s=1, 33=1, A3s=1, 44=1, A4s=1, 55=1, A5s=1, 66=1, A6s=1, 77=1, A7s=1, 88=1, K8s=1, A8s=1, 99=1, K9s=1, A9s=1, TT=1, JTs=1, QTs=1, KTs=1, ATs=1, JJ=1, QJs=1, KJs=1, AJs=1, QJo=1, QQ=1, KQs=1, AQs=1, K9o=1225937/51762264, KTo=1, KJo=1, KQo=1, KK=1, AKs=1, A4o=1, A5o=1, A6o=1, A7o=1, A8o=1, A9o=1, ATo=1, AJo=1, AQo=1, AKo=1, AA=1}";
		register(new Rational(35, 2), sb, bb, new Rational("-234748306075458559", "1499693518029306600"));

		// 20 bb
		sb = "{22=1, A2s=1, 33=1, A3s=1, 44=1, 54s=1, K4s=1, A4s=1, 55=1, 65s=1, 75s=1, Q5s=2399910/2709587, K5s=1, A5s=1, 66=1, 76s=1, 86s=1, 96s=1, T6s=1, Q6s=1, K6s=1, A6s=1, 77=1, 87s=1, 97s=1, T7s=1, J7s=1, Q7s=1, K7s=1, A7s=1, 88=1, 98s=1, T8s=1, J8s=1, Q8s=1, K8s=1, A8s=1, 98o=1, 99=1, T9s=1, J9s=1, Q9s=1, K9s=1, A9s=1, T9o=1, TT=1, JTs=1, QTs=1, KTs=1, ATs=1, J9o=1, JTo=1, JJ=1, QJs=1, KJs=1, AJs=1, Q9o=1, QTo=1, QJo=1, QQ=1, KQs=1, AQs=1, K9o=1, KTo=1, KJo=1, KQo=1, KK=1, AKs=1, A2o=1, A3o=1, A4o=1, A5o=1, A6o=1, A7o=1, A8o=1, A9o=1, ATo=1, AJo=1, AQo=1, AKo=1, AA=1}";
		bb = "{A2s=1, 33=1, A3s=1, 44=1, A4s=1, 55=1, A5s=1, 66=1, A6s=1, 77=1, A7s=1, 88=1, A8s=1, 99=1, K9s=1, A9s=1, TT=1, QTs=1, KTs=1, ATs=1, JJ=1, QJs=1, KJs=1, AJs=1, QJo=201077/32515044, QQ=1, KQs=1, AQs=1, KTo=1, KJo=1, KQo=1, KK=1, AKs=1, A5o=1, A6o=1, A7o=1, A8o=1, A9o=1, ATo=1, AJo=1, AQo=1, AKo=1, AA=1}";
		register(new Rational(20), sb, bb, new Rational(-172573730625831202L, 942049225768751100L));

		// 25 bb
		sb = "{22=1, A2s=1, 33=1, A3s=1, 44=1, K4s=1, A4s=1, 55=1, 65s=1, K5s=1, A5s=1, 66=1, 76s=1, 86s=1, 96s=1, Q6s=1, K6s=1, A6s=1, 77=1, 87s=1, 97s=1, T7s=1, J7s=1, Q7s=1, K7s=1, A7s=1, 88=1, 98s=1, T8s=1, J8s=1, Q8s=1, K8s=1, A8s=1, 99=1, T9s=1, J9s=1, Q9s=1, K9s=1, A9s=1, T9o=1, TT=1, JTs=1, QTs=1, KTs=1, ATs=1, J9o=1, JTo=1, JJ=1, QJs=1, KJs=1, AJs=1, QTo=1, QJo=1, QQ=1, KQs=1, AQs=1, KTo=1, KJo=1, KQo=1, KK=1, AKs=1, A2o=1, A3o=1, A4o=1, A5o=1, A6o=1, A7o=1, A8o=1, A9o=1, ATo=1, AJo=1, AQo=1, AKo=1, AA=1}";
		bb = "{44=1, A4s=1, 55=1, A5s=1, 66=1, A6s=1, 77=1, A7s=1, 88=1, A8s=1, 99=1, A9s=1, TT=1, KTs=1, ATs=1, JJ=1, QJs=1, KJs=1, AJs=1, QQ=1, KQs=1, AQs=1, KJo=1, KQo=1, KK=1, AKs=1, A7o=1, A8o=1, A9o=1, ATo=1, AJo=1, AQo=1, AKo=1, AA=1}";
		register(new Rational(25), sb, bb, new Rational(-79718728513L, 347672625300L));

		// 30 bb
		sb = "{22=1, A2s=1, 33=1, A3s=1, 44=1, A4s=1, 55=1, K5s=1, A5s=1, 66=1, 76s=1, 86s=1, K6s=1, A6s=1, 77=1, 87s=1, 97s=1, T7s=1, J7s=1, K7s=1, A7s=1, 88=1, 98s=1, T8s=1, J8s=1, Q8s=1, K8s=1, A8s=1, 99=1, T9s=1, J9s=1, Q9s=1, K9s=1, A9s=1, T9o=1, TT=1, JTs=1, QTs=1, KTs=1, ATs=1, JTo=1, JJ=1, QJs=1, KJs=1, AJs=1, QTo=1, QJo=1, QQ=1, KQs=1, AQs=1, KTo=1, KJo=1, KQo=1, KK=1, AKs=1, A3o=11957599/15391806, A4o=1, A5o=1, A6o=1, A7o=1, A8o=1, A9o=1, ATo=1, AJo=1, AQo=1, AKo=1, AA=1}";
		bb = "{44=1, 55=1, A5s=38315/72262, 66=1, A6s=1, 77=1, A7s=1, 88=1, A8s=1, 99=1, A9s=1, TT=1, KTs=1, ATs=1, JJ=1, KJs=1, AJs=1, QQ=1, KQs=1, AQs=1, KQo=1, KK=1, AKs=1, A8o=1, A9o=1, ATo=1, AJo=1, AQo=1, AKo=1, AA=1}";
		register(new Rational(30), sb, bb, new Rational(-2248631916057643L, 8374506416476200L));

		// 35 bb
		sb = "{22=1, A2s=1, 33=1, A3s=1, 44=1, A4s=1, 55=1, A5s=1, 66=1, 76s=1, K6s=1, A6s=1, 77=1, 87s=1, 97s=1, T7s=30960205/74449302, K7s=1, A7s=1, 88=1, 98s=1, T8s=1, J8s=1, Q8s=1, K8s=1, A8s=1, 99=1, T9s=1, J9s=1, Q9s=1, K9s=1, A9s=1, TT=1, JTs=1, QTs=1, KTs=1, ATs=1, JTo=1, JJ=1, QJs=1, KJs=1, AJs=1, QTo=1, QJo=1, QQ=1, KQs=1, AQs=1, KTo=1, KJo=1, KQo=1, KK=1, AKs=1, A5o=1, A7o=1, A8o=1, A9o=1, ATo=1, AJo=1, AQo=1, AKo=1, AA=1}";
		bb = "{55=1, 66=1, 77=1, A7s=55028939/74449302, 88=1, A8s=1, 99=1, A9s=1, TT=1, ATs=1, JJ=1, KJs=1, AJs=1, QQ=1, KQs=1, AQs=1, KQo=1, KK=1, AKs=1, A9o=1, ATo=1, AJo=1, AQo=1, AKo=1, AA=1}";
		register(new Rational(35), sb, bb, new Rational(-51945984476977319L, 173137018582558800L));

		// 40 bb
		sb = "{22=1, A2s=1, 33=1, A3s=1, 44=1, A4s=1, 55=1, A5s=1, 66=1, A6s=1, 77=1, 87s=1, K7s=1, A7s=1, 88=1, 98s=1, T8s=1, J8s=1, Q8s=1, K8s=1, A8s=1, 99=1, T9s=1, J9s=1, Q9s=1, K9s=1, A9s=1, TT=1, JTs=1, QTs=1, KTs=1, ATs=1, JTo=1, JJ=1, QJs=1, KJs=1, AJs=1, QTo=1, QJo=1, QQ=1, KQs=1, AQs=1, KTo=97244474878142/478387429469901, KJo=1, KQo=1, KK=1, AKs=1, A7o=69722067935605/478387429469901, A8o=1, A9o=1, ATo=1, AJo=1, AQo=1, AKo=1, AA=1}";
		bb = "{55=1, 66=1, 77=1, 88=1, A8s=41147220590178/53154158829989, 99=1, A9s=1, TT=1, ATs=1, JJ=1, KJs=1, AJs=1, QQ=1, KQs=1, AQs=1, KQo=48942151688659/68341061352843, KK=1, AKs=1, ATo=1, AJo=1, AQo=1, AKo=1, AA=1}";
		register(new Rational(40), sb, bb, new Rational("-17970724056431083776040724", "55440737838106356000365100"));

		// 45 bb
		sb = "{22=1, A2s=1, 33=1, A3s=1, 44=1, A4s=1, 55=1, A5s=1, 66=1, A6s=1, 77=1, K7s=1, A7s=1, 88=1, 98s=1, T8s=1, J8s=1, Q8s=1, K8s=1, A8s=1, 99=1, T9s=1, J9s=1, Q9s=1, K9s=1, A9s=1, TT=1, JTs=1, QTs=1, KTs=1, ATs=1, JTo=1, JJ=1, QJs=1, KJs=1, AJs=1, QJo=1, QQ=1, KQs=1, AQs=1, KTo=31835420/38118789, KJo=1, KQo=1, KK=1, AKs=1, ATo=1, AJo=1, AQo=1, AKo=1, AA=1}";
		bb = "{66=1, 77=1, 88=1, 99=1, A9s=1, TT=1, ATs=1, JJ=1, AJs=1, QQ=1, KQs=1, AQs=1, KQo=8700289/38118789, KK=1, AKs=1, ATo=1, AJo=1, AQo=1, AKo=1, AA=1}";
		register(new Rational(45), sb, bb, new Rational(-1691408949272936L, 4908466461069171L));

		// 50 bb
		sb = "{22=1, 33=1, A3s=1, 44=1, A4s=1, 55=1, A5s=1, 66=1, A6s=1, 77=1, A7s=1, 88=1, 98s=1, T8s=1, Q8s=1, K8s=1, A8s=1, 99=1, T9s=1, J9s=1, Q9s=1, K9s=1, A9s=1, TT=1, JTs=1, QTs=1, KTs=1, ATs=1, JJ=1, QJs=1, KJs=1, AJs=1, QJo=1, QQ=1, KQs=1, AQs=1, KTo=79677800/193976393, KJo=1, KQo=1, KK=1, AKs=1, ATo=1, AJo=1, AQo=1, AKo=1, AA=1}";
		bb = "{66=1, 77=1, 88=1, 99=1, TT=1, ATs=1, JJ=1, AJs=1, QQ=1, KQs=1, AQs=1, KK=1, AKs=1, ATo=111413979/193976393, AJo=1, AQo=1, AKo=1, AA=1}";
		register(new Rational(50), sb, bb, new Rational(-315452816168028157L, 875847815591357700L));

		// 75 bb
		sb = "{44=490421647681379/5478711979995903, 55=1, A5s=1, 66=1, 77=1, 88=1, 99=1, K9s=1, A9s=1, TT=1, JTs=1, QTs=1, KTs=1, ATs=1, JJ=1, QJs=1, KJs=1, AJs=1, QQ=1, KQs=1, AQs=1, KJo=3825282724403653/5478711979995903, KQo=1, KK=1, AKs=1, AJo=1, AQo=1, AKo=1, AA=1}";
		bb = "{88=142331077886817/166021575151391, 99=1, TT=1, JJ=1, AJs=1287239171337290/1826237326665301, QQ=1, AQs=1, KK=1, AKs=1, AQo=1, AKo=1, AA=1}";
		register(new Rational(75), sb, bb, new Rational("-19299739284311574155958013", "47032053761672510598867800"));

		// 100 bb
		sb = "{A3s=1, A4s=1, A5s=1, 66=1, 77=74609885871155/915816488020737, 88=1, 99=1, TT=1, QTs=1, KTs=1, ATs=1, JJ=1, QJs=1, KJs=1, AJs=1, QQ=1, KQs=1, AQs=1, KQo=2162242294808/915816488020737, KK=1, AKs=1, AJo=1, AQo=1, AKo=1, AA=1}";
		bb = "{99=253739777465263/915816488020737, TT=1, JJ=1, QQ=1, AQs=1, KK=1, AKs=1, AQo=135265396530631/915816488020737, AKo=1, AA=1}";
		register(new Rational(100), sb, bb, new Rational("-46115602049034385061873368", "106134774227731877876948700"));

		// 150 bb
		sb = "{A3s=193353853498333/799513507306072, A4s=1, A5s=1, 66=23258868224590/99939188413259, TT=1, JJ=1, AJs=1, QQ=1, KQs=1, AQs=1, KK=1, AKs=1, AKo=1, AA=1}";
		bb = "{JJ=1471281611285659/2398540521918216, QQ=1, KK=1, AKs=1, AKo=2132557309753987/2398540521918216, AA=1}";
		register(new Rational(150), sb, bb, new Rational("-7052813785989863196974101", "15442720002661821270601200"));

		// 200 bb
		sb = "{A3s=33095734889520/858508166940703, A4s=1, A5s=1, TT=449054619321683/2575524500822109, ATs=1, QQ=1, AQs=1, KK=1, AKs=1, AKo=1, AA=1}";
		bb = "{QQ=1944902524303975/2575524500822109, KK=1, AKs=1, AKo=2840388995982001/5151049001644218, AA=1}";
		register(new Rational(200), sb, bb, new Rational("-138930272347748499298396850", "298479788241764881437585900"));

		// 250 bb
		sb = "{A3s=2915636099859915/42824481075489896, A4s=1, A5s=1, TT=2810661329737457/16059180403308711, ATs=1, QQ=1, AQs=1, KK=1, AKs=1, AKo=1, AA=1}";
		bb = "{QQ=13041267660160975/64236721613234844, KK=1, AKs=1, AKo=23618982177799583/64236721613234844, AA=1}";
		register(new Rational(250), sb, bb, new Rational("-3507739752418225959340878625", "7444449881312869812971984400"));

		// 300 bb
		sb = "{A3s=16618367/50202676, A4s=1, A5s=1, ATs=1, KK=1, AKs=1, AKo=1, AA=1}";
		bb = "{KK=1, AKs=1, AKo=1813883/12550669, AA=1}";
		register(new Rational(300), sb, bb, new Rational(-76861222381557200L, 161612001500049100L));

		// 350 bb
		sb = "{A4s=1, A5s=1, ATs=46199461/125170202, KK=1, AKs=1, AKo=1, AA=1}";
		bb = "{KK=1, AKs=46585797/62585101, AA=1}";
		register(new Rational(350), sb, bb, new Rational(-1102913350704401L, 2302553054956154L));

		// 400 bb
		sb = "{A4s=1, A5s=1, ATs=6706942/17866169, KK=1, AKs=1, AKo=1, AA=1}";
		bb = "{KK=1, AKs=7983861/35732338, AA=1}";
		register(new Rational(400), sb, bb, new Rational(-110894260667085050L, 230058440010499100L));

		// 450 bb
		sb = "{A5s=16668965/28720306, KK=1, AKs=1, AKo=1, AA=1}";
		bb = "{KK=13377961/14360153, AA=1}";
		register(new Rational(450), sb, bb, new Rational(-640160832645546L, 1320802141063405L));

		// 500 bb
		sb = "{A5s=83915593/143506402, AKs=1, AKo=1, AA=1}";
		bb = "{KK=52347112/71753201, AA=1}";
		register(new Rational(500), sb, bb, new Rational(-112364861927729657L, 230987257086560975L));
		
		// 550 bb
		sb = "{A5s=16837601/28685714, AKs=1, AKo=1, AA=1}";
		bb = "{KK=8089709/14342857, AA=1}";
		register(new Rational(550), sb, bb, new Rational(-8190922744792761L, 16789962112769300L));

		// 600 bb
		sb = "{A5s=1, ATs=75870259106331/97753735252723, AKs=1, AKo=30904646994525/97753735252723, AA=1}";
		bb = "{KK=37827991851398/97753735252723, AKs=13890077614999/391014941010892, AA=1}";
		register(new Rational(600), sb, bb, new Rational("-1847163901069991374508284", "3776255307577262709299100"));

		// 700 bb
		sb = "{A5s=1, ATs=53422911/62371063, AKs=1, AA=1}";
		bb = "{AKs=42040399/124742126, AA=1}";
		register(new Rational(700), sb, bb, new Rational(-887755104182170178L, 1807059267996807825L));

		// 800 bb
		sb = "{A5s=1, ATs=30573243/35625319, AKs=1, AA=1}";
		bb = "{AKs=5325337/71250638, AA=1}";
		register(new Rational(800), sb, bb, new Rational(-5589962413828579L, 11342443391831475L));

		// 850 bb
		sb = "{AA=1}";
		bb = "{AA=1}";
		register(new Rational(850), sb, bb, new Rational(-133526, 270725));

		// 900 bb
		sb = "{AA=1}";
		bb = "{AA=1}";
		register(new Rational(900), sb, bb, new Rational(-133526, 270725));

		// 1000 bb
		sb = "{AA=1}";
		bb = "{AA=1}";
		register(new Rational(1000), sb, bb, new Rational(-133526, 270725));

		// 2000 bb
		sb = "{AA=1}";
		bb = "{AA=1}";
		register(new Rational(2000), sb, bb, new Rational(-133526, 270725));

	}
	
}
