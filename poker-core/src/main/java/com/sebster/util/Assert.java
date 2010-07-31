package com.sebster.util;

public class Assert {

	private Assert() {
		// Utility class;
	}

	public static void isTrue(final boolean value, final String message) {
		if (!value) {
			throw new AssertionError(message);
		}
	}

	public static void notNull(final Object value, final String message) {
		if (value == null) {
			throw new AssertionError(message);
		}
	}
	
}
