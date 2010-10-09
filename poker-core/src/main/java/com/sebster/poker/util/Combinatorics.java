package com.sebster.poker.util;

public final class Combinatorics {

	private Combinatorics() {
		// Utility class.
	}
	
	public static int factorial(final int n) {
		int result = 1;
		for (int i = 2; i <= n; i++) {
			result *= i;
		}
		return result;
	}
	
	public static int combinations(final int n, final int m) {
		int result = 1, nn = n;
		for (int i = 1; i <= m; i++, nn--) {
			result = result * nn / i;
		}
		return result;
	}
	
}
