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

	private static final Map<Rational, AllinOrFoldStrategy> sbNashEquilibria;
	
	private static final Map<Rational, AllinOrFoldStrategy> bbNashEquilibria;
	
	private NashEquilibria() {
		// Singleton.
		
	}
	public static AllinOrFoldStrategy getSBNashEquilibrium(final Rational r) {
		return sbNashEquilibria.get(r);
	}
	
	public static AllinOrFoldStrategy getBBNashEqulibrium(final Rational r) {
		return bbNashEquilibria.get(r);
	}
	
	public static Set<Rational> getRs() {
		return Collections.unmodifiableSet(sbNashEquilibria.keySet());
	}
	
	// Initialization.
	static {
		sbNashEquilibria = new TreeMap<Rational, AllinOrFoldStrategy>();
		bbNashEquilibria = new TreeMap<Rational, AllinOrFoldStrategy>();
		
		String sb, bb;
		AllinOrFoldStrategy sbStrategy, bbStrategy;

		// 1 bb
		sb = "{22=1, 32s=1, 42s=1, 52s=1, 62s=1, 72s=1, 82s=1, 92s=1, T2s=1, J2s=1, Q2s=1, K2s=1, A2s=1, 32o=1, 33=1, 43s=1, 53s=1, 63s=1, 73s=1, 83s=1, 93s=1, T3s=1, J3s=1, Q3s=1, K3s=1, A3s=1, 42o=1, 43o=1, 44=1, 54s=1, 64s=1, 74s=1, 84s=1, 94s=1, T4s=1, J4s=1, Q4s=1, K4s=1, A4s=1, 52o=1, 53o=1, 54o=1, 55=1, 65s=1, 75s=1, 85s=1, 95s=1, T5s=1, J5s=1, Q5s=1, K5s=1, A5s=1, 62o=1, 63o=1, 64o=1, 65o=1, 66=1, 76s=1, 86s=1, 96s=1, T6s=1, J6s=1, Q6s=1, K6s=1, A6s=1, 72o=1, 73o=1, 74o=1, 75o=1, 76o=1, 77=1, 87s=1, 97s=1, T7s=1, J7s=1, Q7s=1, K7s=1, A7s=1, 82o=1, 83o=1, 84o=1, 85o=1, 86o=1, 87o=1, 88=1, 98s=1, T8s=1, J8s=1, Q8s=1, K8s=1, A8s=1, 92o=1, 93o=1, 94o=1, 95o=1, 96o=1, 97o=1, 98o=1, 99=1, T9s=1, J9s=1, Q9s=1, K9s=1, A9s=1, T2o=1, T3o=1, T4o=1, T5o=1, T6o=1, T7o=1, T8o=1, T9o=1, TT=1, JTs=1, QTs=1, KTs=1, ATs=1, J2o=1, J3o=1, J4o=1, J5o=1, J6o=1, J7o=1, J8o=1, J9o=1, JTo=1, JJ=1, QJs=1, KJs=1, AJs=1, Q2o=1, Q3o=1, Q4o=1, Q5o=1, Q6o=1, Q7o=1, Q8o=1, Q9o=1, QTo=1, QJo=1, QQ=1, KQs=1, AQs=1, K2o=1, K3o=1, K4o=1, K5o=1, K6o=1, K7o=1, K8o=1, K9o=1, KTo=1, KJo=1, KQo=1, KK=1, AKs=1, A2o=1, A3o=1, A4o=1, A5o=1, A6o=1, A7o=1, A8o=1, A9o=1, ATo=1, AJo=1, AQo=1, AKo=1, AA=1}";
		bb = "{22=1, 32s=1, 42s=1, 52s=1, 62s=1, 72s=1, 82s=1, 92s=1, T2s=1, J2s=1, Q2s=1, K2s=1, A2s=1, 32o=1, 33=1, 43s=1, 53s=1, 63s=1, 73s=1, 83s=1, 93s=1, T3s=1, J3s=1, Q3s=1, K3s=1, A3s=1, 42o=1, 43o=1, 44=1, 54s=1, 64s=1, 74s=1, 84s=1, 94s=1, T4s=1, J4s=1, Q4s=1, K4s=1, A4s=1, 52o=1, 53o=1, 54o=1, 55=1, 65s=1, 75s=1, 85s=1, 95s=1, T5s=1, J5s=1, Q5s=1, K5s=1, A5s=1, 62o=1, 63o=1, 64o=1, 65o=1, 66=1, 76s=1, 86s=1, 96s=1, T6s=1, J6s=1, Q6s=1, K6s=1, A6s=1, 72o=1, 73o=1, 74o=1, 75o=1, 76o=1, 77=1, 87s=1, 97s=1, T7s=1, J7s=1, Q7s=1, K7s=1, A7s=1, 82o=1, 83o=1, 84o=1, 85o=1, 86o=1, 87o=1, 88=1, 98s=1, T8s=1, J8s=1, Q8s=1, K8s=1, A8s=1, 92o=1, 93o=1, 94o=1, 95o=1, 96o=1, 97o=1, 98o=1, 99=1, T9s=1, J9s=1, Q9s=1, K9s=1, A9s=1, T2o=1, T3o=1, T4o=1, T5o=1, T6o=1, T7o=1, T8o=1, T9o=1, TT=1, JTs=1, QTs=1, KTs=1, ATs=1, J2o=1, J3o=1, J4o=1, J5o=1, J6o=1, J7o=1, J8o=1, J9o=1, JTo=1, JJ=1, QJs=1, KJs=1, AJs=1, Q2o=1, Q3o=1, Q4o=1, Q5o=1, Q6o=1, Q7o=1, Q8o=1, Q9o=1, QTo=1, QJo=1, QQ=1, KQs=1, AQs=1, K2o=1, K3o=1, K4o=1, K5o=1, K6o=1, K7o=1, K8o=1, K9o=1, KTo=1, KJo=1, KQo=1, KK=1, AKs=1, A2o=1, A3o=1, A4o=1, A5o=1, A6o=1, A7o=1, A8o=1, A9o=1, ATo=1, AJo=1, AQo=1, AKo=1, AA=1}";
		sbStrategy = new ReadOnlyAllinOrFoldStrategy(MixedAllinOrFoldStrategy.fromString(sb));
		bbStrategy = new ReadOnlyAllinOrFoldStrategy(MixedAllinOrFoldStrategy.fromString(bb));
		sbNashEquilibria.put(new Rational(1), sbStrategy);
		bbNashEquilibria.put(new Rational(1), bbStrategy);

		// 1.5 bb
		sb = "{22=1, 32s=1, 42s=1, 52s=1, 62s=1, 72s=1, 82s=1, 92s=1, T2s=1, J2s=1, Q2s=1, K2s=1, A2s=1, 33=1, 43s=1, 53s=1, 63s=1, 73s=1, 83s=1, 93s=1, T3s=1, J3s=1, Q3s=1, K3s=1, A3s=1, 43o=1, 44=1, 54s=1, 64s=1, 74s=1, 84s=1, 94s=1, T4s=1, J4s=1, Q4s=1, K4s=1, A4s=1, 52o=1, 53o=1, 54o=1, 55=1, 65s=1, 75s=1, 85s=1, 95s=1, T5s=1, J5s=1, Q5s=1, K5s=1, A5s=1, 62o=1, 63o=1, 64o=1, 65o=1, 66=1, 76s=1, 86s=1, 96s=1, T6s=1, J6s=1, Q6s=1, K6s=1, A6s=1, 72o=1, 73o=1, 74o=1, 75o=1, 76o=1, 77=1, 87s=1, 97s=1, T7s=1, J7s=1, Q7s=1, K7s=1, A7s=1, 82o=1, 83o=1, 84o=1, 85o=1, 86o=1, 87o=1, 88=1, 98s=1, T8s=1, J8s=1, Q8s=1, K8s=1, A8s=1, 92o=1, 93o=1, 94o=1, 95o=1, 96o=1, 97o=1, 98o=1, 99=1, T9s=1, J9s=1, Q9s=1, K9s=1, A9s=1, T2o=1, T3o=1, T4o=1, T5o=1, T6o=1, T7o=1, T8o=1, T9o=1, TT=1, JTs=1, QTs=1, KTs=1, ATs=1, J2o=1, J3o=1, J4o=1, J5o=1, J6o=1, J7o=1, J8o=1, J9o=1, JTo=1, JJ=1, QJs=1, KJs=1, AJs=1, Q2o=1, Q3o=1, Q4o=1, Q5o=1, Q6o=1, Q7o=1, Q8o=1, Q9o=1, QTo=1, QJo=1, QQ=1, KQs=1, AQs=1, K2o=1, K3o=1, K4o=1, K5o=1, K6o=1, K7o=1, K8o=1, K9o=1, KTo=1, KJo=1, KQo=1, KK=1, AKs=1, A2o=1, A3o=1, A4o=1, A5o=1, A6o=1, A7o=1, A8o=1, A9o=1, ATo=1, AJo=1, AQo=1, AKo=1, AA=1}";
		bb = "{22=1, 32s=1, 42s=1, 52s=1, 62s=1, 72s=1, 82s=1, 92s=1, T2s=1, J2s=1, Q2s=1, K2s=1, A2s=1, 32o=1, 33=1, 43s=1, 53s=1, 63s=1, 73s=1, 83s=1, 93s=1, T3s=1, J3s=1, Q3s=1, K3s=1, A3s=1, 42o=1, 43o=1, 44=1, 54s=1, 64s=1, 74s=1, 84s=1, 94s=1, T4s=1, J4s=1, Q4s=1, K4s=1, A4s=1, 52o=1, 53o=1, 54o=1, 55=1, 65s=1, 75s=1, 85s=1, 95s=1, T5s=1, J5s=1, Q5s=1, K5s=1, A5s=1, 62o=1, 63o=1, 64o=1, 65o=1, 66=1, 76s=1, 86s=1, 96s=1, T6s=1, J6s=1, Q6s=1, K6s=1, A6s=1, 72o=1, 73o=1, 74o=1, 75o=1, 76o=1, 77=1, 87s=1, 97s=1, T7s=1, J7s=1, Q7s=1, K7s=1, A7s=1, 82o=1, 83o=1, 84o=1, 85o=1, 86o=1, 87o=1, 88=1, 98s=1, T8s=1, J8s=1, Q8s=1, K8s=1, A8s=1, 92o=1, 93o=1, 94o=1, 95o=1, 96o=1, 97o=1, 98o=1, 99=1, T9s=1, J9s=1, Q9s=1, K9s=1, A9s=1, T2o=1, T3o=1, T4o=1, T5o=1, T6o=1, T7o=1, T8o=1, T9o=1, TT=1, JTs=1, QTs=1, KTs=1, ATs=1, J2o=1, J3o=1, J4o=1, J5o=1, J6o=1, J7o=1, J8o=1, J9o=1, JTo=1, JJ=1, QJs=1, KJs=1, AJs=1, Q2o=1, Q3o=1, Q4o=1, Q5o=1, Q6o=1, Q7o=1, Q8o=1, Q9o=1, QTo=1, QJo=1, QQ=1, KQs=1, AQs=1, K2o=1, K3o=1, K4o=1, K5o=1, K6o=1, K7o=1, K8o=1, K9o=1, KTo=1, KJo=1, KQo=1, KK=1, AKs=1, A2o=1, A3o=1, A4o=1, A5o=1, A6o=1, A7o=1, A8o=1, A9o=1, ATo=1, AJo=1, AQo=1, AKo=1, AA=1}";
		sbStrategy = new ReadOnlyAllinOrFoldStrategy(MixedAllinOrFoldStrategy.fromString(sb));
		bbStrategy = new ReadOnlyAllinOrFoldStrategy(MixedAllinOrFoldStrategy.fromString(bb));
		sbNashEquilibria.put(new Rational(3, 2), sbStrategy);
		bbNashEquilibria.put(new Rational(3, 2), bbStrategy);

		// 2 bb
		sb = "{22=1, 52s=1, 62s=1, 72s=1, 82s=1, 92s=1, T2s=1, J2s=1, Q2s=1, K2s=1, A2s=1, 33=1, 43s=1, 53s=1, 63s=1, 73s=1, 83s=1, 93s=1, T3s=1, J3s=1, Q3s=1, K3s=1, A3s=1, 44=1, 54s=1, 64s=1, 74s=1, 84s=1, 94s=1, T4s=1, J4s=1, Q4s=1, K4s=1, A4s=1, 54o=1, 55=1, 65s=1, 75s=1, 85s=1, 95s=1, T5s=1, J5s=1, Q5s=1, K5s=1, A5s=1, 64o=1, 65o=1, 66=1, 76s=1, 86s=1, 96s=1, T6s=1, J6s=1, Q6s=1, K6s=1, A6s=1, 74o=1, 75o=1, 76o=1, 77=1, 87s=1, 97s=1, T7s=1, J7s=1, Q7s=1, K7s=1, A7s=1, 84o=1, 85o=1, 86o=1, 87o=1, 88=1, 98s=1, T8s=1, J8s=1, Q8s=1, K8s=1, A8s=1, 92o=1, 93o=1, 94o=1, 95o=1, 96o=1, 97o=1, 98o=1, 99=1, T9s=1, J9s=1, Q9s=1, K9s=1, A9s=1, T2o=1, T3o=1, T4o=1, T5o=1, T6o=1, T7o=1, T8o=1, T9o=1, TT=1, JTs=1, QTs=1, KTs=1, ATs=1, J2o=1, J3o=1, J4o=1, J5o=1, J6o=1, J7o=1, J8o=1, J9o=1, JTo=1, JJ=1, QJs=1, KJs=1, AJs=1, Q2o=1, Q3o=1, Q4o=1, Q5o=1, Q6o=1, Q7o=1, Q8o=1, Q9o=1, QTo=1, QJo=1, QQ=1, KQs=1, AQs=1, K2o=1, K3o=1, K4o=1, K5o=1, K6o=1, K7o=1, K8o=1, K9o=1, KTo=1, KJo=1, KQo=1, KK=1, AKs=1, A2o=1, A3o=1, A4o=1, A5o=1, A6o=1, A7o=1, A8o=1, A9o=1, ATo=1, AJo=1, AQo=1, AKo=1, AA=1}";
		bb = "{22=1, 32s=1, 42s=1, 52s=1, 62s=1, 72s=1, 82s=1, 92s=1, T2s=1, J2s=1, Q2s=1, K2s=1, A2s=1, 32o=1, 33=1, 43s=1, 53s=1, 63s=1, 73s=1, 83s=1, 93s=1, T3s=1, J3s=1, Q3s=1, K3s=1, A3s=1, 42o=1, 43o=1, 44=1, 54s=1, 64s=1, 74s=1, 84s=1, 94s=1, T4s=1, J4s=1, Q4s=1, K4s=1, A4s=1, 52o=1, 53o=1, 54o=1, 55=1, 65s=1, 75s=1, 85s=1, 95s=1, T5s=1, J5s=1, Q5s=1, K5s=1, A5s=1, 62o=1, 63o=1, 64o=1, 65o=1, 66=1, 76s=1, 86s=1, 96s=1, T6s=1, J6s=1, Q6s=1, K6s=1, A6s=1, 72o=1, 73o=1, 74o=1, 75o=1, 76o=1, 77=1, 87s=1, 97s=1, T7s=1, J7s=1, Q7s=1, K7s=1, A7s=1, 82o=1, 83o=1, 84o=1, 85o=1, 86o=1, 87o=1, 88=1, 98s=1, T8s=1, J8s=1, Q8s=1, K8s=1, A8s=1, 92o=1, 93o=1, 94o=1, 95o=1, 96o=1, 97o=1, 98o=1, 99=1, T9s=1, J9s=1, Q9s=1, K9s=1, A9s=1, T2o=1, T3o=1, T4o=1, T5o=1, T6o=1, T7o=1, T8o=1, T9o=1, TT=1, JTs=1, QTs=1, KTs=1, ATs=1, J2o=1, J3o=1, J4o=1, J5o=1, J6o=1, J7o=1, J8o=1, J9o=1, JTo=1, JJ=1, QJs=1, KJs=1, AJs=1, Q2o=1, Q3o=1, Q4o=1, Q5o=1, Q6o=1, Q7o=1, Q8o=1, Q9o=1, QTo=1, QJo=1, QQ=1, KQs=1, AQs=1, K2o=1, K3o=1, K4o=1, K5o=1, K6o=1, K7o=1, K8o=1, K9o=1, KTo=1, KJo=1, KQo=1, KK=1, AKs=1, A2o=1, A3o=1, A4o=1, A5o=1, A6o=1, A7o=1, A8o=1, A9o=1, ATo=1, AJo=1, AQo=1, AKo=1, AA=1}";
		sbStrategy = new ReadOnlyAllinOrFoldStrategy(MixedAllinOrFoldStrategy.fromString(sb));
		bbStrategy = new ReadOnlyAllinOrFoldStrategy(MixedAllinOrFoldStrategy.fromString(bb));
		sbNashEquilibria.put(new Rational(2), sbStrategy);
		bbNashEquilibria.put(new Rational(2), bbStrategy);

		// 2.5 bb
		sb = "{22=1, 82s=1, 92s=1, T2s=1, J2s=1, Q2s=1, K2s=1, A2s=1, 33=1, 73s=1, 83s=1, 93s=1, T3s=1, J3s=1, Q3s=1, K3s=1, A3s=1, 44=1, 54s=1, 64s=1, 74s=1, 84s=1, 94s=1, T4s=1, J4s=1, Q4s=1, K4s=1, A4s=1, 55=1, 65s=1, 75s=1, 85s=1, 95s=1, T5s=1, J5s=1, Q5s=1, K5s=1, A5s=1, 66=1, 76s=1, 86s=1, 96s=1, T6s=1, J6s=1, Q6s=1, K6s=1, A6s=1, 75o=1, 76o=1, 77=1, 87s=1, 97s=1, T7s=1, J7s=1, Q7s=1, K7s=1, A7s=1, 85o=1, 86o=1, 87o=1, 88=1, 98s=1, T8s=1, J8s=1, Q8s=1, K8s=1, A8s=1, 93o=1, 94o=1, 95o=1, 96o=1, 97o=1, 98o=1, 99=1, T9s=1, J9s=1, Q9s=1, K9s=1, A9s=1, T2o=1, T3o=1, T4o=1, T5o=1, T6o=1, T7o=1, T8o=1, T9o=1, TT=1, JTs=1, QTs=1, KTs=1, ATs=1, J2o=1, J3o=1, J4o=1, J5o=1, J6o=1, J7o=1, J8o=1, J9o=1, JTo=1, JJ=1, QJs=1, KJs=1, AJs=1, Q2o=1, Q3o=1, Q4o=1, Q5o=1, Q6o=1, Q7o=1, Q8o=1, Q9o=1, QTo=1, QJo=1, QQ=1, KQs=1, AQs=1, K2o=1, K3o=1, K4o=1, K5o=1, K6o=1, K7o=1, K8o=1, K9o=1, KTo=1, KJo=1, KQo=1, KK=1, AKs=1, A2o=1, A3o=1, A4o=1, A5o=1, A6o=1, A7o=1, A8o=1, A9o=1, ATo=1, AJo=1, AQo=1, AKo=1, AA=1}";
		bb = "{22=1, 32s=1, 42s=1, 52s=1, 62s=1, 72s=1, 82s=1, 92s=1, T2s=1, J2s=1, Q2s=1, K2s=1, A2s=1, 32o=1, 33=1, 43s=1, 53s=1, 63s=1, 73s=1, 83s=1, 93s=1, T3s=1, J3s=1, Q3s=1, K3s=1, A3s=1, 42o=1, 43o=1, 44=1, 54s=1, 64s=1, 74s=1, 84s=1, 94s=1, T4s=1, J4s=1, Q4s=1, K4s=1, A4s=1, 52o=1, 53o=1, 54o=1, 55=1, 65s=1, 75s=1, 85s=1, 95s=1, T5s=1, J5s=1, Q5s=1, K5s=1, A5s=1, 62o=1, 63o=1, 64o=1, 65o=1, 66=1, 76s=1, 86s=1, 96s=1, T6s=1, J6s=1, Q6s=1, K6s=1, A6s=1, 72o=1, 73o=1, 74o=1, 75o=1, 76o=1, 77=1, 87s=1, 97s=1, T7s=1, J7s=1, Q7s=1, K7s=1, A7s=1, 82o=1, 83o=1, 84o=1, 85o=1, 86o=1, 87o=1, 88=1, 98s=1, T8s=1, J8s=1, Q8s=1, K8s=1, A8s=1, 92o=1, 93o=1, 94o=1, 95o=1, 96o=1, 97o=1, 98o=1, 99=1, T9s=1, J9s=1, Q9s=1, K9s=1, A9s=1, T2o=1, T3o=1, T4o=1, T5o=1, T6o=1, T7o=1, T8o=1, T9o=1, TT=1, JTs=1, QTs=1, KTs=1, ATs=1, J2o=1, J3o=1, J4o=1, J5o=1, J6o=1, J7o=1, J8o=1, J9o=1, JTo=1, JJ=1, QJs=1, KJs=1, AJs=1, Q2o=1, Q3o=1, Q4o=1, Q5o=1, Q6o=1, Q7o=1, Q8o=1, Q9o=1, QTo=1, QJo=1, QQ=1, KQs=1, AQs=1, K2o=1, K3o=1, K4o=1, K5o=1, K6o=1, K7o=1, K8o=1, K9o=1, KTo=1, KJo=1, KQo=1, KK=1, AKs=1, A2o=1, A3o=1, A4o=1, A5o=1, A6o=1, A7o=1, A8o=1, A9o=1, ATo=1, AJo=1, AQo=1, AKo=1, AA=1}";
		sbStrategy = new ReadOnlyAllinOrFoldStrategy(MixedAllinOrFoldStrategy.fromString(sb));
		bbStrategy = new ReadOnlyAllinOrFoldStrategy(MixedAllinOrFoldStrategy.fromString(bb));
		sbNashEquilibria.put(new Rational(5, 2), sbStrategy);
		bbNashEquilibria.put(new Rational(5, 2), bbStrategy);

		// 3 bb
		sb = "{22=1, 92s=1, T2s=1, J2s=1, Q2s=1, K2s=1, A2s=1, 33=1, 93s=1, T3s=1, J3s=1, Q3s=1, K3s=1, A3s=1, 44=1, 54s=1, 64s=1, 74s=1, 84s=1, 94s=1, T4s=1, J4s=1, Q4s=1, K4s=1, A4s=1, 55=1, 65s=1, 75s=1, 85s=1, 95s=1, T5s=1, J5s=1, Q5s=1, K5s=1, A5s=1, 66=1, 76s=1, 86s=1, 96s=1, T6s=1, J6s=1, Q6s=1, K6s=1, A6s=1, 76o=1, 77=1, 87s=1, 97s=1, T7s=1, J7s=1, Q7s=1, K7s=1, A7s=1, 85o=1, 86o=1, 87o=1, 88=1, 98s=1, T8s=1, J8s=1, Q8s=1, K8s=1, A8s=1, 95o=1, 96o=1, 97o=1, 98o=1, 99=1, T9s=1, J9s=1, Q9s=1, K9s=1, A9s=1, T2o=217109/630922, T3o=1, T4o=1, T5o=1, T6o=1, T7o=1, T8o=1, T9o=1, TT=1, JTs=1, QTs=1, KTs=1, ATs=1, J2o=1, J3o=1, J4o=1, J5o=1, J6o=1, J7o=1, J8o=1, J9o=1, JTo=1, JJ=1, QJs=1, KJs=1, AJs=1, Q2o=1, Q3o=1, Q4o=1, Q5o=1, Q6o=1, Q7o=1, Q8o=1, Q9o=1, QTo=1, QJo=1, QQ=1, KQs=1, AQs=1, K2o=1, K3o=1, K4o=1, K5o=1, K6o=1, K7o=1, K8o=1, K9o=1, KTo=1, KJo=1, KQo=1, KK=1, AKs=1, A2o=1, A3o=1, A4o=1, A5o=1, A6o=1, A7o=1, A8o=1, A9o=1, ATo=1, AJo=1, AQo=1, AKo=1, AA=1}";
		bb = "{22=1, 32s=1, 42s=1, 52s=1, 62s=1, 72s=1, 82s=1, 92s=1, T2s=1, J2s=1, Q2s=1, K2s=1, A2s=1, 33=1, 43s=1, 53s=1, 63s=1, 73s=1, 83s=1, 93s=1, T3s=1, J3s=1, Q3s=1, K3s=1, A3s=1, 43o=1, 44=1, 54s=1, 64s=1, 74s=1, 84s=1, 94s=1, T4s=1, J4s=1, Q4s=1, K4s=1, A4s=1, 53o=1, 54o=1, 55=1, 65s=1, 75s=1, 85s=1, 95s=1, T5s=1, J5s=1, Q5s=1, K5s=1, A5s=1, 63o=1748147/3785532, 64o=1, 65o=1, 66=1, 76s=1, 86s=1, 96s=1, T6s=1, J6s=1, Q6s=1, K6s=1, A6s=1, 74o=1, 75o=1, 76o=1, 77=1, 87s=1, 97s=1, T7s=1, J7s=1, Q7s=1, K7s=1, A7s=1, 84o=1, 85o=1, 86o=1, 87o=1, 88=1, 98s=1, T8s=1, J8s=1, Q8s=1, K8s=1, A8s=1, 92o=1, 93o=1, 94o=1, 95o=1, 96o=1, 97o=1, 98o=1, 99=1, T9s=1, J9s=1, Q9s=1, K9s=1, A9s=1, T2o=1, T3o=1, T4o=1, T5o=1, T6o=1, T7o=1, T8o=1, T9o=1, TT=1, JTs=1, QTs=1, KTs=1, ATs=1, J2o=1, J3o=1, J4o=1, J5o=1, J6o=1, J7o=1, J8o=1, J9o=1, JTo=1, JJ=1, QJs=1, KJs=1, AJs=1, Q2o=1, Q3o=1, Q4o=1, Q5o=1, Q6o=1, Q7o=1, Q8o=1, Q9o=1, QTo=1, QJo=1, QQ=1, KQs=1, AQs=1, K2o=1, K3o=1, K4o=1, K5o=1, K6o=1, K7o=1, K8o=1, K9o=1, KTo=1, KJo=1, KQo=1, KK=1, AKs=1, A2o=1, A3o=1, A4o=1, A5o=1, A6o=1, A7o=1, A8o=1, A9o=1, ATo=1, AJo=1, AQo=1, AKo=1, AA=1}";
		sbStrategy = new ReadOnlyAllinOrFoldStrategy(MixedAllinOrFoldStrategy.fromString(sb));
		bbStrategy = new ReadOnlyAllinOrFoldStrategy(MixedAllinOrFoldStrategy.fromString(bb));
		sbNashEquilibria.put(new Rational(3), sbStrategy);
		bbNashEquilibria.put(new Rational(3), bbStrategy);

		// 3.5 bb
		sb = "{22=1, T2s=1, J2s=1, Q2s=1, K2s=1, A2s=1, 33=1, 93s=1, T3s=1, J3s=1, Q3s=1, K3s=1, A3s=1, 44=1, 54s=1, 64s=1, 74s=1, 84s=1, 94s=1, T4s=1, J4s=1, Q4s=1, K4s=1, A4s=1, 55=1, 65s=1, 75s=1, 85s=1, 95s=1, T5s=1, J5s=1, Q5s=1, K5s=1, A5s=1, 66=1, 76s=1, 86s=1, 96s=1, T6s=1, J6s=1, Q6s=1, K6s=1, A6s=1, 76o=1, 77=1, 87s=1, 97s=1, T7s=1, J7s=1, Q7s=1, K7s=1, A7s=1, 86o=1, 87o=1, 88=1, 98s=1, T8s=1, J8s=1, Q8s=1, K8s=1, A8s=1, 95o=8912898/9070177, 96o=1, 97o=1, 98o=1, 99=1, T9s=1, J9s=1, Q9s=1, K9s=1, A9s=1, T4o=1, T5o=1, T6o=1, T7o=1, T8o=1, T9o=1, TT=1, JTs=1, QTs=1, KTs=1, ATs=1, J2o=1, J3o=1, J4o=1, J5o=1, J6o=1, J7o=1, J8o=1, J9o=1, JTo=1, JJ=1, QJs=1, KJs=1, AJs=1, Q2o=1, Q3o=1, Q4o=1, Q5o=1, Q6o=1, Q7o=1, Q8o=1, Q9o=1, QTo=1, QJo=1, QQ=1, KQs=1, AQs=1, K2o=1, K3o=1, K4o=1, K5o=1, K6o=1, K7o=1, K8o=1, K9o=1, KTo=1, KJo=1, KQo=1, KK=1, AKs=1, A2o=1, A3o=1, A4o=1, A5o=1, A6o=1, A7o=1, A8o=1, A9o=1, ATo=1, AJo=1, AQo=1, AKo=1, AA=1}";
		bb = "{22=1, 52s=1, 82s=1, 92s=1, T2s=1, J2s=1, Q2s=1, K2s=1, A2s=1, 33=1, 43s=1, 53s=1, 63s=1, 73s=1, 83s=1, 93s=1, T3s=1, J3s=1, Q3s=1, K3s=1, A3s=1, 44=1, 54s=1, 64s=1, 74s=1, 84s=1, 94s=1, T4s=1, J4s=1, Q4s=1, K4s=1, A4s=1, 54o=8698733/9070177, 55=1, 65s=1, 75s=1, 85s=1, 95s=1, T5s=1, J5s=1, Q5s=1, K5s=1, A5s=1, 65o=1, 66=1, 76s=1, 86s=1, 96s=1, T6s=1, J6s=1, Q6s=1, K6s=1, A6s=1, 75o=1, 76o=1, 77=1, 87s=1, 97s=1, T7s=1, J7s=1, Q7s=1, K7s=1, A7s=1, 85o=1, 86o=1, 87o=1, 88=1, 98s=1, T8s=1, J8s=1, Q8s=1, K8s=1, A8s=1, 95o=1, 96o=1, 97o=1, 98o=1, 99=1, T9s=1, J9s=1, Q9s=1, K9s=1, A9s=1, T2o=1, T3o=1, T4o=1, T5o=1, T6o=1, T7o=1, T8o=1, T9o=1, TT=1, JTs=1, QTs=1, KTs=1, ATs=1, J2o=1, J3o=1, J4o=1, J5o=1, J6o=1, J7o=1, J8o=1, J9o=1, JTo=1, JJ=1, QJs=1, KJs=1, AJs=1, Q2o=1, Q3o=1, Q4o=1, Q5o=1, Q6o=1, Q7o=1, Q8o=1, Q9o=1, QTo=1, QJo=1, QQ=1, KQs=1, AQs=1, K2o=1, K3o=1, K4o=1, K5o=1, K6o=1, K7o=1, K8o=1, K9o=1, KTo=1, KJo=1, KQo=1, KK=1, AKs=1, A2o=1, A3o=1, A4o=1, A5o=1, A6o=1, A7o=1, A8o=1, A9o=1, ATo=1, AJo=1, AQo=1, AKo=1, AA=1}";
		sbStrategy = new ReadOnlyAllinOrFoldStrategy(MixedAllinOrFoldStrategy.fromString(sb));
		bbStrategy = new ReadOnlyAllinOrFoldStrategy(MixedAllinOrFoldStrategy.fromString(bb));
		sbNashEquilibria.put(new Rational(7, 2), sbStrategy);
		bbNashEquilibria.put(new Rational(7, 2), bbStrategy);
		
		// 4 bb
		sb = "{22=1, T2s=1, J2s=1, Q2s=1, K2s=1, A2s=1, 33=1, 93s=1, T3s=1, J3s=1, Q3s=1, K3s=1, A3s=1, 44=1, 54s=1, 64s=1, 74s=1, 84s=1, 94s=1, T4s=1, J4s=1, Q4s=1, K4s=1, A4s=1, 55=1, 65s=1, 75s=1, 85s=1, 95s=1, T5s=1, J5s=1, Q5s=1, K5s=1, A5s=1, 66=1, 76s=1, 86s=1, 96s=1, T6s=1, J6s=1, Q6s=1, K6s=1, A6s=1, 76o=1, 77=1, 87s=1, 97s=1, T7s=1, J7s=1, Q7s=1, K7s=1, A7s=1, 86o=1, 87o=1, 88=1, 98s=1, T8s=1, J8s=1, Q8s=1, K8s=1, A8s=1, 96o=1, 97o=1, 98o=1, 99=1, T9s=1, J9s=1, Q9s=1, K9s=1, A9s=1, T5o=1, T6o=1, T7o=1, T8o=1, T9o=1, TT=1, JTs=1, QTs=1, KTs=1, ATs=1, J2o=1, J3o=1, J4o=1, J5o=1, J6o=1, J7o=1, J8o=1, J9o=1, JTo=1, JJ=1, QJs=1, KJs=1, AJs=1, Q2o=1, Q3o=1, Q4o=1, Q5o=1, Q6o=1, Q7o=1, Q8o=1, Q9o=1, QTo=1, QJo=1, QQ=1, KQs=1, AQs=1, K2o=1, K3o=1, K4o=1, K5o=1, K6o=1, K7o=1, K8o=1, K9o=1, KTo=1, KJo=1, KQo=1, KK=1, AKs=1, A2o=1, A3o=1, A4o=1, A5o=1, A6o=1, A7o=1, A8o=1, A9o=1, ATo=1, AJo=1, AQo=1, AKo=1, AA=1}";
		bb = "{22=1, T2s=1, J2s=1, Q2s=1, K2s=1, A2s=1, 33=1, 53s=1, 93s=1, T3s=1, J3s=1, Q3s=1, K3s=1, A3s=1, 44=1, 54s=1, 64s=1, 74s=1, 84s=1, 94s=1, T4s=1, J4s=1, Q4s=1, K4s=1, A4s=1, 55=1, 65s=1, 75s=1, 85s=1, 95s=1, T5s=1, J5s=1, Q5s=1, K5s=1, A5s=1, 66=1, 76s=1, 86s=1, 96s=1, T6s=1, J6s=1, Q6s=1, K6s=1, A6s=1, 76o=1, 77=1, 87s=1, 97s=1, T7s=1, J7s=1, Q7s=1, K7s=1, A7s=1, 86o=1, 87o=1, 88=1, 98s=1, T8s=1, J8s=1, Q8s=1, K8s=1, A8s=1, 96o=1, 97o=1, 98o=1, 99=1, T9s=1, J9s=1, Q9s=1, K9s=1, A9s=1, T5o=1, T6o=1, T7o=1, T8o=1, T9o=1, TT=1, JTs=1, QTs=1, KTs=1, ATs=1, J2o=1, J3o=1, J4o=1, J5o=1, J6o=1, J7o=1, J8o=1, J9o=1, JTo=1, JJ=1, QJs=1, KJs=1, AJs=1, Q2o=1, Q3o=1, Q4o=1, Q5o=1, Q6o=1, Q7o=1, Q8o=1, Q9o=1, QTo=1, QJo=1, QQ=1, KQs=1, AQs=1, K2o=1, K3o=1, K4o=1, K5o=1, K6o=1, K7o=1, K8o=1, K9o=1, KTo=1, KJo=1, KQo=1, KK=1, AKs=1, A2o=1, A3o=1, A4o=1, A5o=1, A6o=1, A7o=1, A8o=1, A9o=1, ATo=1, AJo=1, AQo=1, AKo=1, AA=1}";
		sbStrategy = new ReadOnlyAllinOrFoldStrategy(MixedAllinOrFoldStrategy.fromString(sb));
		bbStrategy = new ReadOnlyAllinOrFoldStrategy(MixedAllinOrFoldStrategy.fromString(bb));
		sbNashEquilibria.put(new Rational(4), sbStrategy);
		bbNashEquilibria.put(new Rational(4), bbStrategy);

		// 4.5 bb
		sb = "{22=1, T2s=1, J2s=1, Q2s=1, K2s=1, A2s=1, 33=1, 53s=1, 93s=2957717/3145283, T3s=1, J3s=1, Q3s=1, K3s=1, A3s=1, 44=1, 54s=1, 64s=1, 74s=1, 84s=1, 94s=1, T4s=1, J4s=1, Q4s=1, K4s=1, A4s=1, 55=1, 65s=1, 75s=1, 85s=1, 95s=1, T5s=1, J5s=1, Q5s=1, K5s=1, A5s=1, 66=1, 76s=1, 86s=1, 96s=1, T6s=1, J6s=1, Q6s=1, K6s=1, A6s=1, 76o=1, 77=1, 87s=1, 97s=1, T7s=1, J7s=1, Q7s=1, K7s=1, A7s=1, 86o=1, 87o=1, 88=1, 98s=1, T8s=1, J8s=1, Q8s=1, K8s=1, A8s=1, 96o=1, 97o=1, 98o=1, 99=1, T9s=1, J9s=1, Q9s=1, K9s=1, A9s=1, T6o=1, T7o=1, T8o=1, T9o=1, TT=1, JTs=1, QTs=1, KTs=1, ATs=1, J2o=1, J3o=1, J4o=1, J5o=1, J6o=1, J7o=1, J8o=1, J9o=1, JTo=1, JJ=1, QJs=1, KJs=1, AJs=1, Q2o=1, Q3o=1, Q4o=1, Q5o=1, Q6o=1, Q7o=1, Q8o=1, Q9o=1, QTo=1, QJo=1, QQ=1, KQs=1, AQs=1, K2o=1, K3o=1, K4o=1, K5o=1, K6o=1, K7o=1, K8o=1, K9o=1, KTo=1, KJo=1, KQo=1, KK=1, AKs=1, A2o=1, A3o=1, A4o=1, A5o=1, A6o=1, A7o=1, A8o=1, A9o=1, ATo=1, AJo=1, AQo=1, AKo=1, AA=1}";
		bb = "{22=1, T2s=1, J2s=1, Q2s=1, K2s=1, A2s=1, 33=1, T3s=1, J3s=1, Q3s=1, K3s=1, A3s=1, 44=1, 54s=1, T4s=1, J4s=1, Q4s=1, K4s=1, A4s=1, 55=1, 65s=1, 75s=1, 85s=1, 95s=1, T5s=1, J5s=1, Q5s=1, K5s=1, A5s=1, 66=1, 76s=1, 86s=1, 96s=1, T6s=1, J6s=1, Q6s=1, K6s=1, A6s=1, 77=1, 87s=1, 97s=1, T7s=1, J7s=1, Q7s=1, K7s=1, A7s=1, 87o=1, 88=1, 98s=1, T8s=1, J8s=1, Q8s=1, K8s=1, A8s=1, 97o=1, 98o=1, 99=1, T9s=1, J9s=1, Q9s=1, K9s=1, A9s=1, T6o=1, T7o=1, T8o=1, T9o=1, TT=1, JTs=1, QTs=1, KTs=1, ATs=1, J3o=5911337/9435849, J4o=1, J5o=1, J6o=1, J7o=1, J8o=1, J9o=1, JTo=1, JJ=1, QJs=1, KJs=1, AJs=1, Q2o=1, Q3o=1, Q4o=1, Q5o=1, Q6o=1, Q7o=1, Q8o=1, Q9o=1, QTo=1, QJo=1, QQ=1, KQs=1, AQs=1, K2o=1, K3o=1, K4o=1, K5o=1, K6o=1, K7o=1, K8o=1, K9o=1, KTo=1, KJo=1, KQo=1, KK=1, AKs=1, A2o=1, A3o=1, A4o=1, A5o=1, A6o=1, A7o=1, A8o=1, A9o=1, ATo=1, AJo=1, AQo=1, AKo=1, AA=1}";
		sbStrategy = new ReadOnlyAllinOrFoldStrategy(MixedAllinOrFoldStrategy.fromString(sb));
		bbStrategy = new ReadOnlyAllinOrFoldStrategy(MixedAllinOrFoldStrategy.fromString(bb));
		sbNashEquilibria.put(new Rational(9, 2), sbStrategy);
		bbNashEquilibria.put(new Rational(9, 2), bbStrategy);

		// 5 bb
		sb = "{22=1, T2s=1, J2s=1, Q2s=1, K2s=1, A2s=1, 33=1, 43s=1, 53s=1, T3s=1, J3s=1, Q3s=1, K3s=1, A3s=1, 44=1, 54s=1, 64s=1, 74s=1, 84s=1, 94s=1, T4s=1, J4s=1, Q4s=1, K4s=1, A4s=1, 55=1, 65s=1, 75s=1, 85s=1, 95s=1, T5s=1, J5s=1, Q5s=1, K5s=1, A5s=1, 66=1, 76s=1, 86s=1, 96s=1, T6s=1, J6s=1, Q6s=1, K6s=1, A6s=1, 76o=1, 77=1, 87s=1, 97s=1, T7s=1, J7s=1, Q7s=1, K7s=1, A7s=1, 86o=1, 87o=1, 88=1, 98s=1, T8s=1, J8s=1, Q8s=1, K8s=1, A8s=1, 96o=5692536/36412441, 97o=1, 98o=1, 99=1, T9s=1, J9s=1, Q9s=1, K9s=1, A9s=1, T6o=1, T7o=1, T8o=1, T9o=1, TT=1, JTs=1, QTs=1, KTs=1, ATs=1, J3o=1, J4o=1, J5o=1, J6o=1, J7o=1, J8o=1, J9o=1, JTo=1, JJ=1, QJs=1, KJs=1, AJs=1, Q2o=1, Q3o=1, Q4o=1, Q5o=1, Q6o=1, Q7o=1, Q8o=1, Q9o=1, QTo=1, QJo=1, QQ=1, KQs=1, AQs=1, K2o=1, K3o=1, K4o=1, K5o=1, K6o=1, K7o=1, K8o=1, K9o=1, KTo=1, KJo=1, KQo=1, KK=1, AKs=1, A2o=1, A3o=1, A4o=1, A5o=1, A6o=1, A7o=1, A8o=1, A9o=1, ATo=1, AJo=1, AQo=1, AKo=1, AA=1}";
		bb = "{22=1, J2s=1, Q2s=1, K2s=1, A2s=1, 33=1, J3s=1, Q3s=1, K3s=1, A3s=1, 44=1, T4s=1, J4s=1, Q4s=1, K4s=1, A4s=1, 55=1, 95s=1, T5s=1, J5s=1, Q5s=1, K5s=1, A5s=1, 66=1, 76s=1, 86s=1, 96s=1, T6s=1, J6s=1, Q6s=1, K6s=1, A6s=1, 77=1, 87s=1, 97s=1, T7s=1, J7s=1, Q7s=1, K7s=1, A7s=1, 88=1, 98s=1, T8s=1, J8s=1, Q8s=1, K8s=1, A8s=1, 97o=27369506/36412441, 98o=1, 99=1, T9s=1, J9s=1, Q9s=1, K9s=1, A9s=1, T7o=1, T8o=1, T9o=1, TT=1, JTs=1, QTs=1, KTs=1, ATs=1, J5o=1, J6o=1, J7o=1, J8o=1, J9o=1, JTo=1, JJ=1, QJs=1, KJs=1, AJs=1, Q2o=1, Q3o=1, Q4o=1, Q5o=1, Q6o=1, Q7o=1, Q8o=1, Q9o=1, QTo=1, QJo=1, QQ=1, KQs=1, AQs=1, K2o=1, K3o=1, K4o=1, K5o=1, K6o=1, K7o=1, K8o=1, K9o=1, KTo=1, KJo=1, KQo=1, KK=1, AKs=1, A2o=1, A3o=1, A4o=1, A5o=1, A6o=1, A7o=1, A8o=1, A9o=1, ATo=1, AJo=1, AQo=1, AKo=1, AA=1}";
		sbStrategy = new ReadOnlyAllinOrFoldStrategy(MixedAllinOrFoldStrategy.fromString(sb));
		bbStrategy = new ReadOnlyAllinOrFoldStrategy(MixedAllinOrFoldStrategy.fromString(bb));
		sbNashEquilibria.put(new Rational(5), sbStrategy);
		bbNashEquilibria.put(new Rational(5), bbStrategy);

		// 5.5 bb
		sb = "{22=1, T2s=1, J2s=1, Q2s=1, K2s=1, A2s=1, 33=1, 43s=1, 53s=1, T3s=1, J3s=1, Q3s=1, K3s=1, A3s=1, 44=1, 54s=1, 64s=1, 74s=1, 84s=1, 94s=1, T4s=1, J4s=1, Q4s=1, K4s=1, A4s=1, 55=1, 65s=1, 75s=1, 85s=1, 95s=1, T5s=1, J5s=1, Q5s=1, K5s=1, A5s=1, 66=1, 76s=1, 86s=1, 96s=1, T6s=1, J6s=1, Q6s=1, K6s=1, A6s=1, 76o=1, 77=1, 87s=1, 97s=1, T7s=1, J7s=1, Q7s=1, K7s=1, A7s=1, 86o=1, 87o=1, 88=1, 98s=1, T8s=1, J8s=1, Q8s=1, K8s=1, A8s=1, 97o=1, 98o=1, 99=1, T9s=1, J9s=1, Q9s=1, K9s=1, A9s=1, T6o=1968894/2346577, T7o=1, T8o=1, T9o=1, TT=1, JTs=1, QTs=1, KTs=1, ATs=1, J5o=1, J6o=1, J7o=1, J8o=1, J9o=1, JTo=1, JJ=1, QJs=1, KJs=1, AJs=1, Q2o=1, Q3o=1, Q4o=1, Q5o=1, Q6o=1, Q7o=1, Q8o=1, Q9o=1, QTo=1, QJo=1, QQ=1, KQs=1, AQs=1, K2o=1, K3o=1, K4o=1, K5o=1, K6o=1, K7o=1, K8o=1, K9o=1, KTo=1, KJo=1, KQo=1, KK=1, AKs=1, A2o=1, A3o=1, A4o=1, A5o=1, A6o=1, A7o=1, A8o=1, A9o=1, ATo=1, AJo=1, AQo=1, AKo=1, AA=1}";
		bb = "{22=1, J2s=1, Q2s=1, K2s=1, A2s=1, 33=1, J3s=1, Q3s=1, K3s=1, A3s=1, 44=1, J4s=1, Q4s=1, K4s=1, A4s=1, 55=1, J5s=1, Q5s=1, K5s=1, A5s=1, 66=1, 86s=1, 96s=1, T6s=1, J6s=1, Q6s=1, K6s=1, A6s=1, 77=1, 87s=1, 97s=1, T7s=1, J7s=1, Q7s=1, K7s=1, A7s=1, 88=1, 98s=1, T8s=1, J8s=1, Q8s=1, K8s=1, A8s=1, 98o=1, 99=1, T9s=1, J9s=1, Q9s=1, K9s=1, A9s=1, T7o=2057219/2346577, T8o=1, T9o=1, TT=1, JTs=1, QTs=1, KTs=1, ATs=1, J7o=1, J8o=1, J9o=1, JTo=1, JJ=1, QJs=1, KJs=1, AJs=1, Q2o=1, Q3o=1, Q4o=1, Q5o=1, Q6o=1, Q7o=1, Q8o=1, Q9o=1, QTo=1, QJo=1, QQ=1, KQs=1, AQs=1, K2o=1, K3o=1, K4o=1, K5o=1, K6o=1, K7o=1, K8o=1, K9o=1, KTo=1, KJo=1, KQo=1, KK=1, AKs=1, A2o=1, A3o=1, A4o=1, A5o=1, A6o=1, A7o=1, A8o=1, A9o=1, ATo=1, AJo=1, AQo=1, AKo=1, AA=1}";
		sbStrategy = new ReadOnlyAllinOrFoldStrategy(MixedAllinOrFoldStrategy.fromString(sb));
		bbStrategy = new ReadOnlyAllinOrFoldStrategy(MixedAllinOrFoldStrategy.fromString(bb));
		sbNashEquilibria.put(new Rational(11, 2), sbStrategy);
		bbNashEquilibria.put(new Rational(11, 2), bbStrategy);

		// 6 bb
		sb = "{22=1, T2s=1, J2s=1, Q2s=1, K2s=1, A2s=1, 33=1, 43s=1, 53s=1, T3s=1, J3s=1, Q3s=1, K3s=1, A3s=1, 44=1, 54s=1, 64s=1, 74s=1, 84s=1, T4s=1, J4s=1, Q4s=1, K4s=1, A4s=1, 55=1, 65s=1, 75s=1, 85s=1, 95s=1, T5s=1, J5s=1, Q5s=1, K5s=1, A5s=1, 66=1, 76s=1, 86s=1, 96s=1, T6s=1, J6s=1, Q6s=1, K6s=1, A6s=1, 76o=1, 77=1, 87s=1, 97s=1, T7s=1, J7s=1, Q7s=1, K7s=1, A7s=1, 86o=9055669/9143474, 87o=1, 88=1, 98s=1, T8s=1, J8s=1, Q8s=1, K8s=1, A8s=1, 97o=1, 98o=1, 99=1, T9s=1, J9s=1, Q9s=1, K9s=1, A9s=1, T7o=1, T8o=1, T9o=1, TT=1, JTs=1, QTs=1, KTs=1, ATs=1, J5o=1, J6o=1, J7o=1, J8o=1, J9o=1, JTo=1, JJ=1, QJs=1, KJs=1, AJs=1, Q2o=1, Q3o=1, Q4o=1, Q5o=1, Q6o=1, Q7o=1, Q8o=1, Q9o=1, QTo=1, QJo=1, QQ=1, KQs=1, AQs=1, K2o=1, K3o=1, K4o=1, K5o=1, K6o=1, K7o=1, K8o=1, K9o=1, KTo=1, KJo=1, KQo=1, KK=1, AKs=1, A2o=1, A3o=1, A4o=1, A5o=1, A6o=1, A7o=1, A8o=1, A9o=1, ATo=1, AJo=1, AQo=1, AKo=1, AA=1}";
		bb = "{22=1, Q2s=1, K2s=1, A2s=1, 33=1, Q3s=1, K3s=1, A3s=1, 44=1, J4s=1, Q4s=1, K4s=1, A4s=1, 55=1, J5s=1, Q5s=1, K5s=1, A5s=1, 66=1, T6s=1, J6s=1, Q6s=1, K6s=1, A6s=1, 77=1, 87s=1, 97s=1, T7s=1, J7s=1, Q7s=1, K7s=1, A7s=1, 88=1, 98s=1, T8s=1, J8s=1, Q8s=1, K8s=1, A8s=1, 98o=4567801/4571737, 99=1, T9s=1, J9s=1, Q9s=1, K9s=1, A9s=1, T8o=1, T9o=1, TT=1, JTs=1, QTs=1, KTs=1, ATs=1, J7o=1, J8o=1, J9o=1, JTo=1, JJ=1, QJs=1, KJs=1, AJs=1, Q4o=1, Q5o=1, Q6o=1, Q7o=1, Q8o=1, Q9o=1, QTo=1, QJo=1, QQ=1, KQs=1, AQs=1, K2o=1, K3o=1, K4o=1, K5o=1, K6o=1, K7o=1, K8o=1, K9o=1, KTo=1, KJo=1, KQo=1, KK=1, AKs=1, A2o=1, A3o=1, A4o=1, A5o=1, A6o=1, A7o=1, A8o=1, A9o=1, ATo=1, AJo=1, AQo=1, AKo=1, AA=1}";
		sbStrategy = new ReadOnlyAllinOrFoldStrategy(MixedAllinOrFoldStrategy.fromString(sb));
		bbStrategy = new ReadOnlyAllinOrFoldStrategy(MixedAllinOrFoldStrategy.fromString(bb));
		sbNashEquilibria.put(new Rational(6), sbStrategy);
		bbNashEquilibria.put(new Rational(6), bbStrategy);

		// 7 bb
		sb = "{22=1, J2s=1, Q2s=1, K2s=1, A2s=1, 33=1, 43s=1, 53s=1, 63s=1, T3s=1, J3s=1, Q3s=1, K3s=1, A3s=1, 44=1, 54s=1, 64s=1, 74s=1, 84s=1, T4s=1, J4s=1, Q4s=1, K4s=1, A4s=1, 55=1, 65s=1, 75s=1, 85s=1, 95s=1, T5s=1, J5s=1, Q5s=1, K5s=1, A5s=1, 66=1, 76s=1, 86s=1, 96s=1, T6s=1, J6s=1, Q6s=1, K6s=1, A6s=1, 76o=1, 77=1, 87s=1, 97s=1, T7s=1, J7s=1, Q7s=1, K7s=1, A7s=1, 86o=48461563/62703678, 87o=1, 88=1, 98s=1, T8s=1, J8s=1, Q8s=1, K8s=1, A8s=1, 97o=1, 98o=1, 99=1, T9s=1, J9s=1, Q9s=1, K9s=1, A9s=1, T7o=1, T8o=1, T9o=1, TT=1, JTs=1, QTs=1, KTs=1, ATs=1, J7o=1, J8o=1, J9o=1, JTo=1, JJ=1, QJs=1, KJs=1, AJs=1, Q2o=1, Q3o=1, Q4o=1, Q5o=1, Q6o=1, Q7o=1, Q8o=1, Q9o=1, QTo=1, QJo=1, QQ=1, KQs=1, AQs=1, K2o=1, K3o=1, K4o=1, K5o=1, K6o=1, K7o=1, K8o=1, K9o=1, KTo=1, KJo=1, KQo=1, KK=1, AKs=1, A2o=1, A3o=1, A4o=1, A5o=1, A6o=1, A7o=1, A8o=1, A9o=1, ATo=1, AJo=1, AQo=1, AKo=1, AA=1}";
		bb = "{22=1, Q2s=1, K2s=1, A2s=1, 33=1, Q3s=1, K3s=1, A3s=1, 44=1, Q4s=1, K4s=1, A4s=1, 55=1, Q5s=1, K5s=1, A5s=1, 66=1, J6s=1, Q6s=1, K6s=1, A6s=1, 77=1, 97s=13756767/20901226, T7s=1, J7s=1, Q7s=1, K7s=1, A7s=1, 88=1, 98s=1, T8s=1, J8s=1, Q8s=1, K8s=1, A8s=1, 99=1, T9s=1, J9s=1, Q9s=1, K9s=1, A9s=1, T9o=1, TT=1, JTs=1, QTs=1, KTs=1, ATs=1, J8o=1, J9o=1, JTo=1, JJ=1, QJs=1, KJs=1, AJs=1, Q6o=1, Q7o=1, Q8o=1, Q9o=1, QTo=1, QJo=1, QQ=1, KQs=1, AQs=1, K2o=1, K3o=1, K4o=1, K5o=1, K6o=1, K7o=1, K8o=1, K9o=1, KTo=1, KJo=1, KQo=1, KK=1, AKs=1, A2o=1, A3o=1, A4o=1, A5o=1, A6o=1, A7o=1, A8o=1, A9o=1, ATo=1, AJo=1, AQo=1, AKo=1, AA=1}";
		sbStrategy = new ReadOnlyAllinOrFoldStrategy(MixedAllinOrFoldStrategy.fromString(sb));
		bbStrategy = new ReadOnlyAllinOrFoldStrategy(MixedAllinOrFoldStrategy.fromString(bb));
		sbNashEquilibria.put(new Rational(7), sbStrategy);
		bbNashEquilibria.put(new Rational(7), bbStrategy);

		// 8 bb
		sb = "{22=1, J2s=1, Q2s=1, K2s=1, A2s=1, 33=1, 53s=1, J3s=1, Q3s=1, K3s=1, A3s=1, 44=1, 54s=1, 64s=1, 74s=1, T4s=1, J4s=1, Q4s=1, K4s=1, A4s=1, 55=1, 65s=1, 75s=1, 85s=1, 95s=1, T5s=1, J5s=1, Q5s=1, K5s=1, A5s=1, 66=1, 76s=1, 86s=1, 96s=1, T6s=1, J6s=1, Q6s=1, K6s=1, A6s=1, 76o=1, 77=1, 87s=1, 97s=1, T7s=1, J7s=1, Q7s=1, K7s=1, A7s=1, 87o=1, 88=1, 98s=1, T8s=1, J8s=1, Q8s=1, K8s=1, A8s=1, 97o=1, 98o=1, 99=1, T9s=1, J9s=1, Q9s=1, K9s=1, A9s=1, T7o=1, T8o=1, T9o=1, TT=1, JTs=1, QTs=1, KTs=1, ATs=1, J7o=1, J8o=1, J9o=1, JTo=1, JJ=1, QJs=1, KJs=1, AJs=1, Q4o=378937/1976868, Q5o=1, Q6o=1, Q7o=1, Q8o=1, Q9o=1, QTo=1, QJo=1, QQ=1, KQs=1, AQs=1, K2o=1, K3o=1, K4o=1, K5o=1, K6o=1, K7o=1, K8o=1, K9o=1, KTo=1, KJo=1, KQo=1, KK=1, AKs=1, A2o=1, A3o=1, A4o=1, A5o=1, A6o=1, A7o=1, A8o=1, A9o=1, ATo=1, AJo=1, AQo=1, AKo=1, AA=1}";
		bb = "{22=1, K2s=1, A2s=1, 33=1, K3s=1, A3s=1, 44=1, Q4s=1, K4s=1, A4s=1, 55=1, Q5s=1, K5s=1, A5s=1, 66=1, Q6s=1, K6s=1, A6s=1, 77=1, J7s=1, Q7s=1, K7s=1, A7s=1, 88=1, 98s=1, T8s=1, J8s=1, Q8s=1, K8s=1, A8s=1, 99=1, T9s=1, J9s=1, Q9s=1, K9s=1, A9s=1, T9o=1, TT=1, JTs=1, QTs=1, KTs=1, ATs=1, J9o=1, JTo=1, JJ=1, QJs=1, KJs=1, AJs=1, Q7o=1567051/2965302, Q8o=1, Q9o=1, QTo=1, QJo=1, QQ=1, KQs=1, AQs=1, K2o=1, K3o=1, K4o=1, K5o=1, K6o=1, K7o=1, K8o=1, K9o=1, KTo=1, KJo=1, KQo=1, KK=1, AKs=1, A2o=1, A3o=1, A4o=1, A5o=1, A6o=1, A7o=1, A8o=1, A9o=1, ATo=1, AJo=1, AQo=1, AKo=1, AA=1}";
		sbStrategy = new ReadOnlyAllinOrFoldStrategy(MixedAllinOrFoldStrategy.fromString(sb));
		bbStrategy = new ReadOnlyAllinOrFoldStrategy(MixedAllinOrFoldStrategy.fromString(bb));
		sbNashEquilibria.put(new Rational(8), sbStrategy);
		bbNashEquilibria.put(new Rational(8), bbStrategy);

		// 9 bb
		sb = "{22=1, Q2s=1, K2s=1, A2s=1, 33=1, 43s=1, 53s=1, J3s=1, Q3s=1, K3s=1, A3s=1, 44=1, 54s=1, 64s=1, 74s=1, T4s=1, J4s=1, Q4s=1, K4s=1, A4s=1, 55=1, 65s=1, 75s=1, 85s=1, 95s=1, T5s=1, J5s=1, Q5s=1, K5s=1, A5s=1, 66=1, 76s=1, 86s=1, 96s=1, T6s=1, J6s=1, Q6s=1, K6s=1, A6s=1, 76o=1, 77=1, 87s=1, 97s=1, T7s=1, J7s=1, Q7s=1, K7s=1, A7s=1, 87o=1, 88=1, 98s=1, T8s=1, J8s=1, Q8s=1, K8s=1, A8s=1, 97o=1, 98o=1, 99=1, T9s=1, J9s=1, Q9s=1, K9s=1, A9s=1, T7o=1, T8o=1, T9o=1, TT=1, JTs=1, QTs=1, KTs=1, ATs=1, J8o=1, J9o=1, JTo=1, JJ=1, QJs=1, KJs=1, AJs=1, Q6o=1, Q7o=1, Q8o=1, Q9o=1, QTo=1, QJo=1, QQ=1, KQs=1, AQs=1, K2o=1, K3o=1, K4o=1, K5o=1, K6o=1, K7o=1, K8o=1, K9o=1, KTo=1, KJo=1, KQo=1, KK=1, AKs=1, A2o=1, A3o=1, A4o=1, A5o=1, A6o=1, A7o=1, A8o=1, A9o=1, ATo=1, AJo=1, AQo=1, AKo=1, AA=1}";
		bb = "{22=1, K2s=1, A2s=1, 33=1, K3s=1, A3s=1, 44=1, K4s=1, A4s=1, 55=1, K5s=1, A5s=1, 66=1, Q6s=1, K6s=1, A6s=1, 77=1, Q7s=1, K7s=1, A7s=1, 88=1, T8s=1, J8s=1, Q8s=1, K8s=1, A8s=1, 99=1, T9s=1, J9s=1, Q9s=1, K9s=1, A9s=1, TT=1, JTs=1, QTs=1, KTs=1, ATs=1, J9o=1, JTo=1, JJ=1, QJs=1, KJs=1, AJs=1, Q8o=1, Q9o=1, QTo=1, QJo=1, QQ=1, KQs=1, AQs=1, K4o=1, K5o=1, K6o=1, K7o=1, K8o=1, K9o=1, KTo=1, KJo=1, KQo=1, KK=1, AKs=1, A2o=1, A3o=1, A4o=1, A5o=1, A6o=1, A7o=1, A8o=1, A9o=1, ATo=1, AJo=1, AQo=1, AKo=1, AA=1}";
		sbStrategy = new ReadOnlyAllinOrFoldStrategy(MixedAllinOrFoldStrategy.fromString(sb));
		bbStrategy = new ReadOnlyAllinOrFoldStrategy(MixedAllinOrFoldStrategy.fromString(bb));
		sbNashEquilibria.put(new Rational(9), sbStrategy);
		bbNashEquilibria.put(new Rational(9), bbStrategy);

		// 10 bb
		sb = "{22=1, Q2s=1, K2s=1, A2s=1, 33=1, 43s=8731989/12285638, 53s=1, J3s=1, Q3s=1, K3s=1, A3s=1, 44=1, 54s=1, 64s=1, 74s=1, 84s=1, T4s=1, J4s=1, Q4s=1, K4s=1, A4s=1, 55=1, 65s=1, 75s=1, 85s=1, 95s=1, T5s=1, J5s=1, Q5s=1, K5s=1, A5s=1, 66=1, 76s=1, 86s=1, 96s=1, T6s=1, J6s=1, Q6s=1, K6s=1, A6s=1, 76o=1, 77=1, 87s=1, 97s=1, T7s=1, J7s=1, Q7s=1, K7s=1, A7s=1, 87o=1, 88=1, 98s=1, T8s=1, J8s=1, Q8s=1, K8s=1, A8s=1, 97o=1, 98o=1, 99=1, T9s=1, J9s=1, Q9s=1, K9s=1, A9s=1, T8o=1, T9o=1, TT=1, JTs=1, QTs=1, KTs=1, ATs=1, J8o=1, J9o=1, JTo=1, JJ=1, QJs=1, KJs=1, AJs=1, Q7o=1, Q8o=1, Q9o=1, QTo=1, QJo=1, QQ=1, KQs=1, AQs=1, K2o=1, K3o=1, K4o=1, K5o=1, K6o=1, K7o=1, K8o=1, K9o=1, KTo=1, KJo=1, KQo=1, KK=1, AKs=1, A2o=1, A3o=1, A4o=1, A5o=1, A6o=1, A7o=1, A8o=1, A9o=1, ATo=1, AJo=1, AQo=1, AKo=1, AA=1}";
		bb = "{22=1, K2s=1, A2s=1, 33=1, K3s=1, A3s=1, 44=1, K4s=1, A4s=1, 55=1, K5s=1, A5s=1, 66=1, Q6s=4948399/12285638, K6s=1, A6s=1, 77=1, Q7s=1, K7s=1, A7s=1, 88=1, J8s=1, Q8s=1, K8s=1, A8s=1, 99=1, T9s=1, J9s=1, Q9s=1, K9s=1, A9s=1, TT=1, JTs=1, QTs=1, KTs=1, ATs=1, JTo=1, JJ=1, QJs=1, KJs=1, AJs=1, Q9o=1, QTo=1, QJo=1, QQ=1, KQs=1, AQs=1, K5o=1, K6o=1, K7o=1, K8o=1, K9o=1, KTo=1, KJo=1, KQo=1, KK=1, AKs=1, A2o=1, A3o=1, A4o=1, A5o=1, A6o=1, A7o=1, A8o=1, A9o=1, ATo=1, AJo=1, AQo=1, AKo=1, AA=1}";
		sbStrategy = new ReadOnlyAllinOrFoldStrategy(MixedAllinOrFoldStrategy.fromString(sb));
		bbStrategy = new ReadOnlyAllinOrFoldStrategy(MixedAllinOrFoldStrategy.fromString(bb));
		sbNashEquilibria.put(new Rational(10), sbStrategy);
		bbNashEquilibria.put(new Rational(10), bbStrategy);

		// 11 bb
		sb = "{22=1, Q2s=1, K2s=1, A2s=1, 33=1, 53s=1, Q3s=1, K3s=1, A3s=1, 44=1, 54s=1, 64s=1, 74s=1, J4s=1, Q4s=1, K4s=1, A4s=1, 55=1, 65s=1, 75s=1, 85s=1, 95s=1, T5s=1, J5s=1, Q5s=1, K5s=1, A5s=1, 66=1, 76s=1, 86s=1, 96s=1, T6s=1, J6s=1, Q6s=1, K6s=1, A6s=1, 76o=393733/7195524, 77=1, 87s=1, 97s=1, T7s=1, J7s=1, Q7s=1, K7s=1, A7s=1, 87o=1, 88=1, 98s=1, T8s=1, J8s=1, Q8s=1, K8s=1, A8s=1, 98o=1, 99=1, T9s=1, J9s=1, Q9s=1, K9s=1, A9s=1, T8o=1, T9o=1, TT=1, JTs=1, QTs=1, KTs=1, ATs=1, J8o=1, J9o=1, JTo=1, JJ=1, QJs=1, KJs=1, AJs=1, Q8o=1, Q9o=1, QTo=1, QJo=1, QQ=1, KQs=1, AQs=1, K2o=1, K3o=1, K4o=1, K5o=1, K6o=1, K7o=1, K8o=1, K9o=1, KTo=1, KJo=1, KQo=1, KK=1, AKs=1, A2o=1, A3o=1, A4o=1, A5o=1, A6o=1, A7o=1, A8o=1, A9o=1, ATo=1, AJo=1, AQo=1, AKo=1, AA=1}";
		bb = "{22=1, A2s=1, 33=1, K3s=1, A3s=1, 44=1, K4s=1, A4s=1, 55=1, K5s=1, A5s=1, 66=1, K6s=1, A6s=1, 77=1, K7s=1, A7s=1, 88=1, Q8s=1, K8s=1, A8s=1, 99=1, T9s=1, J9s=1, Q9s=1, K9s=1, A9s=1, TT=1, JTs=1, QTs=1, KTs=1, ATs=1, JTo=1, JJ=1, QJs=1, KJs=1, AJs=1, Q9o=1, QTo=1, QJo=1, QQ=1, KQs=1, AQs=1, K6o=1314617/2398508, K7o=1, K8o=1, K9o=1, KTo=1, KJo=1, KQo=1, KK=1, AKs=1, A2o=1, A3o=1, A4o=1, A5o=1, A6o=1, A7o=1, A8o=1, A9o=1, ATo=1, AJo=1, AQo=1, AKo=1, AA=1}";
		sbStrategy = new ReadOnlyAllinOrFoldStrategy(MixedAllinOrFoldStrategy.fromString(sb));
		bbStrategy = new ReadOnlyAllinOrFoldStrategy(MixedAllinOrFoldStrategy.fromString(bb));
		sbNashEquilibria.put(new Rational(11), sbStrategy);
		bbNashEquilibria.put(new Rational(11), bbStrategy);
	
		// 12 bb
		sb = "{22=1, Q2s=1, K2s=1, A2s=1, 33=1, 53s=1, Q3s=1, K3s=1, A3s=1, 44=1, 54s=1, 64s=1, 74s=1, J4s=1, Q4s=1, K4s=1, A4s=1, 55=1, 65s=1, 75s=1, 85s=1, 95s=1, J5s=1, Q5s=1, K5s=1, A5s=1, 66=1, 76s=1, 86s=1, 96s=1, T6s=1, J6s=1, Q6s=1, K6s=1, A6s=1, 77=1, 87s=1, 97s=1, T7s=1, J7s=1, Q7s=1, K7s=1, A7s=1, 87o=1, 88=1, 98s=1, T8s=1, J8s=1, Q8s=1, K8s=1, A8s=1, 98o=1, 99=1, T9s=1, J9s=1, Q9s=1, K9s=1, A9s=1, T8o=1, T9o=1, TT=1, JTs=1, QTs=1, KTs=1, ATs=1, J8o=1, J9o=1, JTo=1, JJ=1, QJs=1, KJs=1, AJs=1, Q8o=1, Q9o=1, QTo=1, QJo=1, QQ=1, KQs=1, AQs=1, K3o=1, K4o=1, K5o=1, K6o=1, K7o=1, K8o=1, K9o=1, KTo=1, KJo=1, KQo=1, KK=1, AKs=1, A2o=1, A3o=1, A4o=1, A5o=1, A6o=1, A7o=1, A8o=1, A9o=1, ATo=1, AJo=1, AQo=1, AKo=1, AA=1}";
		bb = "{22=1, A2s=1, 33=1, A3s=1, 44=1, K4s=1, A4s=1, 55=1, K5s=1, A5s=1, 66=1, K6s=1, A6s=1, 77=1, K7s=1, A7s=1, 88=1, Q8s=1, K8s=1, A8s=1, 99=1, J9s=1, Q9s=1, K9s=1, A9s=1, TT=1, JTs=1, QTs=1, KTs=1, ATs=1, JTo=1, JJ=1, QJs=1, KJs=1, AJs=1, QTo=1, QJo=1, QQ=1, KQs=1, AQs=1, K7o=1, K8o=1, K9o=1, KTo=1, KJo=1, KQo=1, KK=1, AKs=1, A2o=1, A3o=1, A4o=1, A5o=1, A6o=1, A7o=1, A8o=1, A9o=1, ATo=1, AJo=1, AQo=1, AKo=1, AA=1}";
		sbStrategy = new ReadOnlyAllinOrFoldStrategy(MixedAllinOrFoldStrategy.fromString(sb));
		bbStrategy = new ReadOnlyAllinOrFoldStrategy(MixedAllinOrFoldStrategy.fromString(bb));
		sbNashEquilibria.put(new Rational(12), sbStrategy);
		bbNashEquilibria.put(new Rational(12), bbStrategy);

		// 15 bb
		sb = "{22=1, K2s=1, A2s=1, 33=1, K3s=1, A3s=1, 44=1, 54s=1, 64s=1, Q4s=9219319/10911294, K4s=1, A4s=1, 55=1, 65s=1, 75s=1, 85s=1, Q5s=1, K5s=1, A5s=1, 66=1, 76s=1, 86s=1, 96s=1, T6s=1, J6s=1, Q6s=1, K6s=1, A6s=1, 77=1, 87s=1, 97s=1, T7s=1, J7s=1, Q7s=1, K7s=1, A7s=1, 88=1, 98s=1, T8s=1, J8s=1, Q8s=1, K8s=1, A8s=1, 98o=1, 99=1, T9s=1, J9s=1, Q9s=1, K9s=1, A9s=1, T8o=1, T9o=1, TT=1, JTs=1, QTs=1, KTs=1, ATs=1, J9o=1, JTo=1, JJ=1, QJs=1, KJs=1, AJs=1, Q9o=1, QTo=1, QJo=1, QQ=1, KQs=1, AQs=1, K6o=1, K7o=1, K8o=1, K9o=1, KTo=1, KJo=1, KQo=1, KK=1, AKs=1, A2o=1, A3o=1, A4o=1, A5o=1, A6o=1, A7o=1, A8o=1, A9o=1, ATo=1, AJo=1, AQo=1, AKo=1, AA=1}";
		bb = "{22=1, A2s=1, 33=1, A3s=1, 44=1, A4s=1, 55=1, A5s=1, 66=1, A6s=1, 77=1, K7s=1, A7s=1, 88=1, K8s=1, A8s=1, 99=1, Q9s=1, K9s=1, A9s=1, TT=1, JTs=1, QTs=1, KTs=1, ATs=1, JJ=1, QJs=1, KJs=1, AJs=1, QTo=3881177/5455647, QJo=1, QQ=1, KQs=1, AQs=1, K9o=1, KTo=1, KJo=1, KQo=1, KK=1, AKs=1, A2o=1, A3o=1, A4o=1, A5o=1, A6o=1, A7o=1, A8o=1, A9o=1, ATo=1, AJo=1, AQo=1, AKo=1, AA=1}";
		sbStrategy = new ReadOnlyAllinOrFoldStrategy(MixedAllinOrFoldStrategy.fromString(sb));
		bbStrategy = new ReadOnlyAllinOrFoldStrategy(MixedAllinOrFoldStrategy.fromString(bb));
		sbNashEquilibria.put(new Rational(15), sbStrategy);
		bbNashEquilibria.put(new Rational(15), bbStrategy);

		// 20 bb
		sb = "{22=1, A2s=1, 33=1, A3s=1, 44=1, 54s=1, K4s=1, A4s=1, 55=1, 65s=1, 75s=1, Q5s=2399910/2709587, K5s=1, A5s=1, 66=1, 76s=1, 86s=1, 96s=1, T6s=1, Q6s=1, K6s=1, A6s=1, 77=1, 87s=1, 97s=1, T7s=1, J7s=1, Q7s=1, K7s=1, A7s=1, 88=1, 98s=1, T8s=1, J8s=1, Q8s=1, K8s=1, A8s=1, 98o=1, 99=1, T9s=1, J9s=1, Q9s=1, K9s=1, A9s=1, T9o=1, TT=1, JTs=1, QTs=1, KTs=1, ATs=1, J9o=1, JTo=1, JJ=1, QJs=1, KJs=1, AJs=1, Q9o=1, QTo=1, QJo=1, QQ=1, KQs=1, AQs=1, K9o=1, KTo=1, KJo=1, KQo=1, KK=1, AKs=1, A2o=1, A3o=1, A4o=1, A5o=1, A6o=1, A7o=1, A8o=1, A9o=1, ATo=1, AJo=1, AQo=1, AKo=1, AA=1}";
		bb = "{A2s=1, 33=1, A3s=1, 44=1, A4s=1, 55=1, A5s=1, 66=1, A6s=1, 77=1, A7s=1, 88=1, A8s=1, 99=1, K9s=1, A9s=1, TT=1, QTs=1, KTs=1, ATs=1, JJ=1, QJs=1, KJs=1, AJs=1, QJo=201077/32515044, QQ=1, KQs=1, AQs=1, KTo=1, KJo=1, KQo=1, KK=1, AKs=1, A5o=1, A6o=1, A7o=1, A8o=1, A9o=1, ATo=1, AJo=1, AQo=1, AKo=1, AA=1}";
		sbStrategy = new ReadOnlyAllinOrFoldStrategy(MixedAllinOrFoldStrategy.fromString(sb));
		bbStrategy = new ReadOnlyAllinOrFoldStrategy(MixedAllinOrFoldStrategy.fromString(bb));
		sbNashEquilibria.put(new Rational(20), sbStrategy);
		bbNashEquilibria.put(new Rational(20), bbStrategy);

		// 25 bb
		sb = "{22=1, A2s=1, 33=1, A3s=1, 44=1, K4s=1, A4s=1, 55=1, 65s=1, K5s=1, A5s=1, 66=1, 76s=1, 86s=1, 96s=1, Q6s=1, K6s=1, A6s=1, 77=1, 87s=1, 97s=1, T7s=1, J7s=1, Q7s=1, K7s=1, A7s=1, 88=1, 98s=1, T8s=1, J8s=1, Q8s=1, K8s=1, A8s=1, 99=1, T9s=1, J9s=1, Q9s=1, K9s=1, A9s=1, T9o=1, TT=1, JTs=1, QTs=1, KTs=1, ATs=1, J9o=1, JTo=1, JJ=1, QJs=1, KJs=1, AJs=1, QTo=1, QJo=1, QQ=1, KQs=1, AQs=1, KTo=1, KJo=1, KQo=1, KK=1, AKs=1, A2o=1, A3o=1, A4o=1, A5o=1, A6o=1, A7o=1, A8o=1, A9o=1, ATo=1, AJo=1, AQo=1, AKo=1, AA=1}";
		bb = "{44=1, A4s=1, 55=1, A5s=1, 66=1, A6s=1, 77=1, A7s=1, 88=1, A8s=1, 99=1, A9s=1, TT=1, KTs=1, ATs=1, JJ=1, QJs=1, KJs=1, AJs=1, QQ=1, KQs=1, AQs=1, KJo=1, KQo=1, KK=1, AKs=1, A7o=1, A8o=1, A9o=1, ATo=1, AJo=1, AQo=1, AKo=1, AA=1}";
		sbStrategy = new ReadOnlyAllinOrFoldStrategy(MixedAllinOrFoldStrategy.fromString(sb));
		bbStrategy = new ReadOnlyAllinOrFoldStrategy(MixedAllinOrFoldStrategy.fromString(bb));
		sbNashEquilibria.put(new Rational(25), sbStrategy);
		bbNashEquilibria.put(new Rational(25), bbStrategy);

		// 30 bb
		sb = "{22=1, A2s=1, 33=1, A3s=1, 44=1, A4s=1, 55=1, K5s=1, A5s=1, 66=1, 76s=1, 86s=1, K6s=1, A6s=1, 77=1, 87s=1, 97s=1, T7s=1, J7s=1, K7s=1, A7s=1, 88=1, 98s=1, T8s=1, J8s=1, Q8s=1, K8s=1, A8s=1, 99=1, T9s=1, J9s=1, Q9s=1, K9s=1, A9s=1, T9o=1, TT=1, JTs=1, QTs=1, KTs=1, ATs=1, JTo=1, JJ=1, QJs=1, KJs=1, AJs=1, QTo=1, QJo=1, QQ=1, KQs=1, AQs=1, KTo=1, KJo=1, KQo=1, KK=1, AKs=1, A3o=11957599/15391806, A4o=1, A5o=1, A6o=1, A7o=1, A8o=1, A9o=1, ATo=1, AJo=1, AQo=1, AKo=1, AA=1}";
		bb = "{44=1, 55=1, A5s=38315/72262, 66=1, A6s=1, 77=1, A7s=1, 88=1, A8s=1, 99=1, A9s=1, TT=1, KTs=1, ATs=1, JJ=1, KJs=1, AJs=1, QQ=1, KQs=1, AQs=1, KQo=1, KK=1, AKs=1, A8o=1, A9o=1, ATo=1, AJo=1, AQo=1, AKo=1, AA=1}";
		sbStrategy = new ReadOnlyAllinOrFoldStrategy(MixedAllinOrFoldStrategy.fromString(sb));
		bbStrategy = new ReadOnlyAllinOrFoldStrategy(MixedAllinOrFoldStrategy.fromString(bb));
		sbNashEquilibria.put(new Rational(30), sbStrategy);
		bbNashEquilibria.put(new Rational(30), bbStrategy);

		// 50 bb
		sb = "{22=1, 33=1, A3s=1, 44=1, A4s=1, 55=1, A5s=1, 66=1, A6s=1, 77=1, A7s=1, 88=1, 98s=1, T8s=1, Q8s=1, K8s=1, A8s=1, 99=1, T9s=1, J9s=1, Q9s=1, K9s=1, A9s=1, TT=1, JTs=1, QTs=1, KTs=1, ATs=1, JJ=1, QJs=1, KJs=1, AJs=1, QJo=1, QQ=1, KQs=1, AQs=1, KTo=79677800/193976393, KJo=1, KQo=1, KK=1, AKs=1, ATo=1, AJo=1, AQo=1, AKo=1, AA=1}";
		bb = "{66=1, 77=1, 88=1, 99=1, TT=1, ATs=1, JJ=1, AJs=1, QQ=1, KQs=1, AQs=1, KK=1, AKs=1, ATo=111413979/193976393, AJo=1, AQo=1, AKo=1, AA=1}";
		sbStrategy = new ReadOnlyAllinOrFoldStrategy(MixedAllinOrFoldStrategy.fromString(sb));
		bbStrategy = new ReadOnlyAllinOrFoldStrategy(MixedAllinOrFoldStrategy.fromString(bb));
		sbNashEquilibria.put(new Rational(50), sbStrategy);
		bbNashEquilibria.put(new Rational(50), bbStrategy);

	}
	
}
