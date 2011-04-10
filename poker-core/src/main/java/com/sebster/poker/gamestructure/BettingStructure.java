package com.sebster.poker.gamestructure;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.jcip.annotations.Immutable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * The betting structure of a tournament consists of the initial stack size and
 * all the blind levels.
 */
@Immutable
public class BettingStructure {

	/** The initial stack in chips. */
	private final int initialStack;

	private final List<BlindLevel> blindLevels;

	public BettingStructure(final int initialStack, final List<BlindLevel> blindLevels) {
		this.initialStack = initialStack;
		this.blindLevels = new ArrayList<BlindLevel>(blindLevels);
	}

	/**
	 * Get the initial stack in chips.
	 * 
	 * @return the initial stack in chips
	 */
	public int getInitialStack() {
		return initialStack;
	}

	public List<BlindLevel> getBlindLevels() {
		return Collections.unmodifiableList(blindLevels);
	}

	public int getNumberOfBlindLevels() {
		return blindLevels.size();
	}

	public BlindLevel getBlindLevel(final int level) {
		return blindLevels.get(level);
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(initialStack).append(blindLevels).toHashCode();
	}

	@Override
	public boolean equals(final Object object) {
		if (this == object) {
			return true;
		}
		if (object instanceof BettingStructure) {
			final BettingStructure other = (BettingStructure) object;
			return new EqualsBuilder().append(initialStack, other.initialStack).append(blindLevels, other.blindLevels).isEquals();
		}
		return false;
	}

}
