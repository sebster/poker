package com.sebster.util.arrays;

import java.util.Arrays;

public class IntArrayWrapper implements Comparable<IntArrayWrapper> {

	public int[] array;

	public IntArrayWrapper() {
		// Default constructor.
	}

	public IntArrayWrapper(final int[] array) {
		this.array = array;
	}

	@Override
	public int hashCode() {
		return Arrays.hashCode(array);
	}

	@Override
	public boolean equals(final Object object) {
		if (object == this) {
			return true;
		}
		if (object instanceof IntArrayWrapper) {
			final IntArrayWrapper other = (IntArrayWrapper) object;
			return Arrays.equals(array, other.array);
		}
		return false;
	}

	@Override
	public int compareTo(final IntArrayWrapper other) {
		final int length = array.length;
		final int otherLength = other.array.length;
		final int minLength = Math.min(length, otherLength);
		for (int i = 0; i < minLength; i++) {
			final int element = array[i];
			final int otherElement = other.array[i];
			if (element < otherElement) {
				return -1;
			}
			if (element > otherElement) {
				return 1;
			}
		}
		return length < otherLength ? -1 : length > otherLength ? 1 : 0;
	}

	@Override
	public String toString() {
		return Arrays.toString(array);
	}

}
