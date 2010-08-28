package com.sebster.poker.holdem;

import java.util.EnumSet;

import com.sebster.math.rational.Rational;
import com.sebster.poker.HoleCategory;
import com.sebster.util.Validate;

public final class PureAllinOrFoldStrategy extends AbstractAllinOrFoldStrategy {

	private final EnumSet<HoleCategory> allinHoleCategories;

	public PureAllinOrFoldStrategy() {
		allinHoleCategories = EnumSet.noneOf(HoleCategory.class);
	}
	
	public PureAllinOrFoldStrategy(final EnumSet<HoleCategory> allinHoleCategories) {
		this.allinHoleCategories = EnumSet.copyOf(allinHoleCategories);
	}
	
	@Override
	public Rational getAllinFrequency(final HoleCategory holeCategory) {
		return containsHoleCategory(holeCategory) ? Rational.ONE : Rational.ZERO;
	}

	public boolean addHoleCategory(final HoleCategory holeCategory) {
		Validate.notNull(holeCategory, "holeCategory == null");
		return allinHoleCategories.add(holeCategory);
	}

	public boolean removeHoleCategory(final HoleCategory holeCategory) {
		Validate.notNull(holeCategory, "holeCategory == null");
		return allinHoleCategories.remove(holeCategory);
	}
	
	public boolean containsHoleCategory(final HoleCategory holeCategory) {
		Validate.notNull(holeCategory, "holeCategory == null");
		return allinHoleCategories.contains(holeCategory);
	}

}
