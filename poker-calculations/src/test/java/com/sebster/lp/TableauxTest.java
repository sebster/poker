package com.sebster.lp;

import static com.sebster.math.rational.Rational.ONE;
import static com.sebster.math.rational.Rational.ZERO;

import org.junit.Assert;
import org.junit.Test;

import com.sebster.math.rational.Rational;

public class TableauxTest {

	@Test
	public void testExample1PageXX() {
		int[][] intTableaux = new int[][] {
				{ 2, 3, 1, 1, 0, 0, 5 },
				{ 4, 1, 2, 0, 1, 0, 11 },
				{ 3, 4, 2, 0, 0, 1, 8 },
				{ 5, 4, 3, 0, 0, 0, 0 }
				};
		Tableaux tableaux = new Tableaux(3, 3, toRational(intTableaux));
		tableaux.solve();

		Assert.assertArrayEquals(new Rational[] { new Rational(5), new Rational(4), new Rational(3) }, tableaux.getObjectiveFunction());
		Assert.assertArrayEquals(new int[] { 0, 4, 2 }, tableaux.getBasis());
		Assert.assertArrayEquals(new Rational[] { new Rational(2), ZERO, new Rational(1), ZERO, new Rational(1), ZERO }, tableaux.getVars());
		Assert.assertEquals(new Rational(13), tableaux.getOptimum());
	}

	@Test
	public void testExample2PageXX() {
		int[][] intTableaux = new int[][] {
				{ 1, 3, 1, 1, 0, 0, 0, 3 },
				{ -1, 0, 3, 0, 1, 0, 0, 2 },
				{ 2, -1, 2, 0, 0, 1, 0, 4 },
				{ 2, 3, -1, 0, 0, 0, 1, 2 },
				{ 5, 5, 3, 0, 0, 0, 0, 0 }
				};
		Tableaux tableaux = new Tableaux(3, 4, toRational(intTableaux));
		tableaux.solve();

		Assert.assertArrayEquals(new Rational[] { new Rational(5), new Rational(5), new Rational(3) }, tableaux.getObjectiveFunction());
		Assert.assertArrayEquals(new int[] { 3, 1, 2, 0 }, tableaux.getBasis());
		Assert.assertArrayEquals(new Rational[] { new Rational(32, 29), new Rational(8, 29), new Rational(30, 29), new Rational(1, 29), ZERO, ZERO, ZERO }, tableaux.getVars());
		Assert.assertEquals(new Rational(10), tableaux.getOptimum());
	}

	@Test
	public void testDegnerateExamplePage29() {
		int[][] intTableaux = new int[][] {
				{ 0, 0, 2, 1, 0, 0, 1 },
				{ 2, -4, 6, 0, 1, 0, 3 },
				{ -1, 3, 4, 0, 0, 1, 2 },
				{ 2, -1, 8, 0, 0, 0, 0 }
				};
		Tableaux tableaux = new Tableaux(3, 3, toRational(intTableaux));
		tableaux.solve();

		Assert.assertArrayEquals(new Rational[] { new Rational(2), new Rational(-1), new Rational(8) }, tableaux.getObjectiveFunction());
		Assert.assertArrayEquals(new int[] { 3, 0, 1 }, tableaux.getBasis());
		Assert.assertArrayEquals(new Rational[] { new Rational(17, 2), new Rational(7, 2), ZERO, ONE, ZERO, ZERO }, tableaux.getVars());
		Assert.assertEquals(new Rational(27, 2), tableaux.getOptimum());
	}

	@Test
	public void testCyclingExamplePage31() {
		Rational[][] rationalTableaux = new Rational[][] {
				{ new Rational(1, 2), new Rational(-11, 2), new Rational(-5, 2), new Rational(9), ONE, ZERO, ZERO, ZERO },
				{ new Rational(1, 2), new Rational(-3, 2), new Rational(-1, 2), ONE, ZERO, ONE, ZERO, ZERO },
				{ ONE, ZERO, ZERO, ZERO, ZERO, ZERO, ONE, ONE },
				{ new Rational(10), new Rational(-57), new Rational(-9), new Rational(-24), ZERO, ZERO, ZERO, ZERO }
				};
		Tableaux tableaux = new Tableaux(4, 3, rationalTableaux);
		tableaux.solve();

//		Assert.assertArrayEquals(new Rational[] { new Rational(2), new Rational(-1), new Rational(8) }, tableaux.getObjectiveFunction());
//		Assert.assertArrayEquals(new int[] { 3, 0, 1 }, tableaux.getBasis());
//		Assert.assertArrayEquals(new Rational[] { new Rational(17, 2), new Rational(7, 2), ZERO, ONE, ZERO, ZERO }, tableaux.getVars());
//		Assert.assertEquals(new Rational(27, 2), tableaux.getOptimum());
	}

	public static Rational[][] toRational(int[][] array) {
		Rational[][] result = new Rational[array.length][];
		for (int i = 0; i < array.length; i++) {
			result[i] = new Rational[array[i].length];
			for (int j = 0; j < array[i].length; j++) {
				result[i][j] = new Rational(array[i][j]);
			}
		}
		return result;
	}

}
