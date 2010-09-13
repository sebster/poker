package com.sebster.math.lcp;

import org.junit.Assert;
import org.junit.Test;

import com.sebster.math.rational.Rational;

public class LCPSolverTest {

	@Test
	public void testLCPex2p8() {
		final Rational[][] M = toRational(new int[][] { 
			{  1, -1, -1, -1 }, 
			{ -1,  1, -1, -1 }, 
			{  1,  1,  2,  0 }, 
			{  1,  1,  0,  2 }, 
		});
		final Rational[] b = toRational(new int[] { 3, 5, -9, -5 });

		final Rational[] z = SimpleLCPSolver.solve(M, b);
		
		Assert.assertArrayEquals(toRational(new int[] { 2, 1, 3, 1 }), z);
	}

	@Test
	public void testLCPex2p9() {
		final Rational[][] M = toRational(new int[][] { 
			{ -1,  0, -3 }, 
			{  1, -2, -5 }, 
			{ -2, -1, -2 }, 
		});
		final Rational[] b = toRational(new int[] { -3, -2, -1 });

		try {
			SimpleLCPSolver.solve(M, b);
			Assert.fail("ray termination expected");
		} catch (IllegalStateException e) {
			// Expected.
		}
	}

	@Test
	public void testLCPex2p10() {
		final Rational[][] M = toRational(new int[][] { 
			{ 1, 0, 0 },
			{ 2, 1, 0 }, 
			{ 2, 2, 1 },
		});
		final Rational[] b = toRational(new int[] { -8, -12, -14 });

		final Rational[] z = SimpleLCPSolver.solve(M, b);

		Assert.assertArrayEquals(toRational(new int[] { 8, 0, 0 }), z);
	}

	@Test
	public void testLCPex2p11() {
		final Rational[][] M = toRational(new int[][] { 
			{ 1, 2, 0 }, 
			{ 0, 1, 2 }, 
			{ 2, 0, 1 }, 
		});
		final Rational[] b = toRational(new int[] { -1, -1, -1 });

		final Rational[] z = SimpleLCPSolver.solve(M, b);

		final Rational third = new Rational(1, 3);
		Assert.assertArrayEquals(new Rational[] { third, third, third }, z);
	}

	public static Rational[] toRational(int[] array) {
		final Rational[] result = new Rational[array.length];
		for (int i = 0; i < array.length; i++) {
			result[i] = new Rational(array[i]);
		}
		return result;
	}

	public static Rational[][] toRational(int[][] array) {
		final Rational[][] result = new Rational[array.length][];
		for (int i = 0; i < array.length; i++) {
			result[i] = new Rational[array[i].length];
			for (int j = 0; j < array[i].length; j++) {
				result[i][j] = new Rational(array[i][j]);
			}
		}
		return result;
	}

}
