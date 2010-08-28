package com.sebster.poker.holdem;

import java.util.EnumMap;
import java.util.Map;
import java.util.StringTokenizer;

import com.sebster.math.rational.Rational;
import com.sebster.poker.HoleCategory;
import com.sebster.util.Validate;

public final class MixedAllinOrFoldStrategy extends AbstractAllinOrFoldStrategy {

	private final Map<HoleCategory, Rational> allinFrequencies = new EnumMap<HoleCategory, Rational>(HoleCategory.class);

	public MixedAllinOrFoldStrategy() {
		// All fold strategy.
	}
	
	public MixedAllinOrFoldStrategy(final Map<HoleCategory, Rational> allinFrequenties) {
		Validate.notNull(allinFrequenties, "allinFrequenties == null");
		for (final Map.Entry<HoleCategory, Rational> entry : allinFrequenties.entrySet()) {
			putHoleCategory(entry.getKey(), entry.getValue());
		}
	}
	
	public MixedAllinOrFoldStrategy(final AllinOrFoldStrategy other) {
		Validate.notNull(other, "other == null");
		for (final HoleCategory holeCategory : HoleCategory.values()) {
			putHoleCategory(holeCategory, other.getAllinFrequency(holeCategory));
		}
	}
	
	@Override
	public Rational getAllinFrequency(final HoleCategory holeCategory) {
		Validate.notNull(holeCategory, "holeCategory == null");
		final Rational allinFrequency = allinFrequencies.get(holeCategory);
		return allinFrequency != null ? allinFrequency : Rational.ZERO;
	}

	public Rational putHoleCategory(final HoleCategory holeCategory, final Rational allinFrequency) {
		Validate.notNull(holeCategory, "holeCategory == null");
		Validate.notNull(allinFrequency, "allinFrequency == null");
		Validate.isTrue(Rational.ZERO.compareTo(allinFrequency) <= 0, "allinFrequency <= 0");
		Validate.isTrue(Rational.ONE.compareTo(allinFrequency) >= 0, "allinFrequency >= 1");
		return allinFrequency.signum() == 0 ? allinFrequencies.remove(holeCategory) : allinFrequencies.put(holeCategory, allinFrequency);
	}

	public Rational removeHoleCategory(final HoleCategory holeCategory) {
		Validate.notNull(holeCategory, "holeCategory == null");
		return allinFrequencies.remove(holeCategory);
	}

	@Override
	public String toString() {
		return allinFrequencies.toString();
	}
	
	public static MixedAllinOrFoldStrategy fromString(final String string) {
		final MixedAllinOrFoldStrategy strategy = new MixedAllinOrFoldStrategy();
		Validate.notNull(string, "value == null");
		Validate.isTrue(string.length() >= 2, "value too short");
		Validate.isTrue(string.charAt(0) == '{', "first character must be {");
		Validate.isTrue(string.charAt(string.length() - 1) == '}', "last character must be }");
		StringTokenizer tokenizer = new StringTokenizer(string.substring(1, string.length() - 1), ", ");
		while (tokenizer.hasMoreTokens()) {
			final String token = tokenizer.nextToken();
			final int i = token.indexOf('=');
			Validate.isTrue(i >= 0, "= character expected in token " + token);
			final HoleCategory holeCategory = HoleCategory.fromString(token.substring(0, i));
			final Rational allinFrequency = Rational.fromString(token.substring(i + 1));
			strategy.putHoleCategory(holeCategory, allinFrequency);
		}
		return strategy;
	}
	
}
