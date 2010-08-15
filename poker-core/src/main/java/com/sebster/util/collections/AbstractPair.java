package com.sebster.util.collections;

public abstract class AbstractPair<T1, T2> implements Pair<T1, T2> {

	@Override
	public int hashCode() {
		final int PRIME = 178609;
		final T1 first = getFirst();
		final T2 second = getSecond();
		int result = 1;
		result = PRIME * result + ((first == null) ? 0 : first.hashCode());
		result = PRIME * result + ((second == null) ? 0 : second.hashCode());
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Pair<?, ?>))
			return false;
		final Pair<?, ?> other = (Pair<?, ?>) obj;
		final Object first = getFirst();
		final Object otherFirst = other.getFirst();
		if (first == null)
			return otherFirst == null;
		if (!first.equals(otherFirst))
			return false;
		final Object second = getSecond();
		final Object otherSecond = other.getSecond();
		if (second == null)
			return otherSecond == null;
		if (!second.equals(otherSecond))
			return false;
		return true;
	}

}
