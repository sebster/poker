package com.sebster.math;

public final class MathUtil {

	private MathUtil() {
		// Utility class.
	}

	public static <T extends Comparable<? super T>> T min(T first, T second) {
		if (first.compareTo(second) <= 0) {
			return first;
		}
		return second;
	}

	public static <T extends Comparable<? super T>> T max(T first, T second) {
		if (first.compareTo(second) >= 0) {
			return first;
		}
		return second;
	}

	public static <T extends Comparable<? super T>> T min(final T first, final T... rest) {
		T result = first;
		for (T value : rest) {
			if (value.compareTo(result) < 0) {
				result = value;
			}
		}
		return result;
	}

	public static <T extends Comparable<? super T>> T max(final T first, final T... rest) {
		T result = first;
		for (T value : rest) {
			if (value.compareTo(result) > 0) {
				result = value;
			}
		}
		return result;
	}

}
