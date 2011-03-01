package com.sebster.util;

public class Assert {

	private Assert() {
		// Utility class;
	}

	public static void assertTrue(final boolean value, final String message) {
		if (!value) {
			throw new AssertionError(message);
		}
	}

	public static void assertNotNull(final Object value, final String message) {
		if (value == null) {
			throw new AssertionError(message);
		}
	}

	public static void assertEqual(final int expected, final int actual, final String message) {
		if (expected != actual) {
			throw new AssertionError(message + ", expected=" + expected + ", actual=" + actual);
		}
	}

}
