package com.sebster.util;

public class Validate {

	private Validate() {
		// Utility class;
	}

	public static void isTrue(final boolean value, final String message) {
		if (!value) {
			throw new IllegalArgumentException(message);
		}
	}

	public static void notNull(final Object value, final String message) {
		if (value == null) {
			throw new IllegalArgumentException(message);
		}
	}
	
}
