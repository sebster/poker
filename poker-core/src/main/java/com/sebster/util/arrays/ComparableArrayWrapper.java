package com.sebster.util.arrays;

public class ComparableArrayWrapper<T extends Comparable<? super T>> extends ObjectArrayWrapper<T> implements Comparable<ComparableArrayWrapper<T>> {

	@Override
	public int compareTo(final ComparableArrayWrapper<T> other) {
		final int length = array.length;
		final int otherLength = other.array.length;
		final int minLength = Math.min(length, otherLength);
		for (int i = 0; i < minLength; i++) {
			final int c = array[i].compareTo(other.array[i]);
			if (c != 0) {
				return c;
			}
		}
		return length < otherLength ? -1 : length > otherLength ? 1 : 0;
	}

}
