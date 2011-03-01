package com.sebster.poker;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

public class IndexedHole {

	private Hole hole;

	private int index;

	public IndexedHole(final Hole hole, final int index) {
		this.hole = hole;
		this.index = index;
	}

	public Hole getHole() {
		return hole;
	}

	public void setHole(final Hole hole) {
		this.hole = hole;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(final int index) {
		this.index = index;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(hole).append(index).toHashCode();
	}

	@Override
	public boolean equals(final Object object) {
		if (this == object) {
			return true;
		}
		if (object instanceof IndexedHole) {
			final IndexedHole other = (IndexedHole) object;
			return new EqualsBuilder().append(hole, other.hole).append(index, other.index).isEquals();
		}
		return false;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("hole", hole).append("index", index).toString();
	}
}
