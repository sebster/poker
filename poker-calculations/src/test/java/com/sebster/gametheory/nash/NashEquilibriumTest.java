package com.sebster.gametheory.nash;

import org.junit.Assert;
import org.junit.Test;

import com.sebster.math.rational.Rational;
import com.sebster.math.rational.matrix.Matrix;
import com.sebster.util.collections.Pair;

public class NashEquilibriumTest {

	@Test
	public void testGame1() {
		final Matrix<Rational> E = toRational(new int[][] {
				{  1, 0, 0, 0 },
				{ -1, 1, 1, 1 }
		});
		final Matrix<Rational> F = E;
		final Matrix<Rational> A = toRational(new int[][] {
				{ 0, 0, 0, 0 },
				{ 0, 3, 0, 0 },
				{ 0, 2, 1, 2 },
				{ 0, 0, 0, 3 }
		});
		final Matrix<Rational> B = toRational(new int[][] {
				{ 0, 0, 0, 0 },
				{ 0, 0, 2, 3 },
				{ 0, 0, 1, 0 },
				{ 0, 3, 2, 0 }
		});
		
		final Pair<Matrix<Rational>, Matrix<Rational>> result = NashEquilibrium.solve(E, F, A, B);
		final Matrix<Rational> x = result.getFirst();
		final Matrix<Rational> y = result.getSecond();
		
		Assert.assertEquals(toRational(new int[] { 1, 0, 1, 0 }), x);
		Assert.assertEquals(toRational(new int[] { 1, 0, 1, 0 }), y);
	}

	public static Matrix<Rational> toRational(int[] array) {
		final Rational[][] result = new Rational[array.length][1];
		for (int i = 0; i < array.length; i++) {
			result[i] = new Rational[] { new Rational(array[i]) };
		}
		return new Matrix<Rational>(result);
	}

	public static Matrix<Rational> toRational(int[][] array) {
		final Rational[][] result = new Rational[array.length][];
		for (int i = 0; i < array.length; i++) {
			result[i] = new Rational[array[i].length];
			for (int j = 0; j < array[i].length; j++) {
				result[i][j] = new Rational(array[i][j]);
			}
		}
		return new Matrix<Rational>(result);
	}

}
