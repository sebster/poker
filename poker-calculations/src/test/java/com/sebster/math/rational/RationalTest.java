package com.sebster.math.rational;

import java.math.BigInteger;

import junit.framework.Assert;

import org.junit.Test;

import com.sebster.math.rational.Rational;

public class RationalTest {

	@Test
	public void testConstructor() {
		final Rational oneHalf = new Rational(2, 4);
		Assert.assertEquals(BigInteger.ONE, oneHalf.getNumerator());
		Assert.assertEquals(BigInteger.valueOf(2), oneHalf.getDenominator());
	}
	
	@Test 
	public void testConversion() {
		final Rational fiveFourths = new Rational(10, 8);
		Assert.assertEquals(1.25D, fiveFourths.doubleValue());
		Assert.assertEquals(1.25F, fiveFourths.floatValue());
		Assert.assertEquals(1, fiveFourths.intValue());
		Assert.assertEquals(1, fiveFourths.longValue());
	}
	
}
