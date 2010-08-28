package com.sebster.math;

public final class MathUtil {

	private MathUtil() {
		// Utility class.
	}
	
	public static <T extends Comparable<T>> T max(T first, T second) {
		if (first.compareTo(second) >= 0) {
			return first;
		}
		return second;
	}
	
	public static <T extends Comparable<T>> T min(T first, T second) {
		if (first.compareTo(second) <= 0) {
			return first;
		}
		return second;
	}

}
