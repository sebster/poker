package com.sebster.util.arrays;

import java.util.Arrays;

public class ObjectArrayWrapper<T> {

	public T[] array;

	public ObjectArrayWrapper() {
		// Default constructor.
	}

	public ObjectArrayWrapper(final T... array) {
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
		if (object instanceof ObjectArrayWrapper) {
			final ObjectArrayWrapper<?> other = (ObjectArrayWrapper<?>) object;
			return Arrays.equals(array, other.array);
		}
		return false;
	}

	@Override
	public String toString() {
		return Arrays.toString(array);
	}

}
