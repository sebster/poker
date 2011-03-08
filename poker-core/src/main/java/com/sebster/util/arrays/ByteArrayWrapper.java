package com.sebster.util.arrays;

import java.util.Arrays;

public class ByteArrayWrapper implements Comparable<ByteArrayWrapper> {

	public byte[] array;

	public ByteArrayWrapper() {
		// Default constructor.
	}

	public ByteArrayWrapper(final byte[] array) {
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
		if (object instanceof ByteArrayWrapper) {
			final ByteArrayWrapper other = (ByteArrayWrapper) object;
			return Arrays.equals(array, other.array);
		}
		return false;
	}

	@Override
	public int compareTo(final ByteArrayWrapper other) {
		final int length = array.length;
		final int otherLength = other.array.length;
		final int minLength = Math.min(length, otherLength);
		for (int i = 0; i < minLength; i++) {
			final byte element = array[i];
			final byte otherElement = other.array[i];
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
