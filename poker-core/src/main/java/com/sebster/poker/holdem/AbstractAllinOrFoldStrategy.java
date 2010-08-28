package com.sebster.poker.holdem;

import com.sebster.poker.HoleCategory;

public abstract class AbstractAllinOrFoldStrategy implements AllinOrFoldStrategy {

	@Override
	public int hashCode() {
		int result = 1;
		for (final HoleCategory holeCategory : HoleCategory.values()) {
			result = holeCategory.hashCode() + 31 * result;
			result = getAllinFrequency(holeCategory).hashCode() + 31 * result;
		}
		return result;
	}

	@Override
	public boolean equals(final Object object) {
		if (this == object)
			return true;
		if (object == null)
			return false;
		if (!(object instanceof AllinOrFoldStrategy))
			return false;
		final AllinOrFoldStrategy other = (AllinOrFoldStrategy) object;
		for (final HoleCategory holeCategory : HoleCategory.values()) {
			if (!getAllinFrequency(holeCategory).equals(other.getAllinFrequency(holeCategory))) {
				return false;
			}
		}
		return true;
	}

}
