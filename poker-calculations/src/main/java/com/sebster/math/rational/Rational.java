package com.sebster.math.rational;

import java.math.BigDecimal;
import java.math.BigInteger;

import com.sebster.util.Validate;

public final class Rational extends Number implements Comparable<Rational> {

	private static final long serialVersionUID = -5235703991206087636L;

	private final BigInteger numerator;

	private final BigInteger denominator;

	public Rational(BigInteger numerator, BigInteger denominator) {
		Validate.notNull(numerator, "numerator is null");
		Validate.notNull(denominator, "denominator is null");

		if (denominator.signum() == 0) {
			throw new ArithmeticException("division by zero");
		}

		if (numerator.signum() == 0) {
			// Zero.
			this.numerator = numerator;
			this.denominator = BigInteger.ONE;
		} else {
			// Non-zero.
			if (denominator.signum() < 0) {
				// Denominator is always positive in the internal representation.
				numerator = numerator.negate();
				denominator = denominator.negate();
			}
			// Divide out the greatest common divisor.
			final BigInteger gcd = numerator.gcd(denominator);
			this.numerator = numerator.divide(gcd);
			this.denominator = denominator.divide(gcd);
		}
	}

	public Rational(final BigInteger numerator) {
		this(numerator, BigInteger.ONE);
	}

	public Rational(final long numerator, final long denominator) {
		this(BigInteger.valueOf(numerator), BigInteger.valueOf(denominator));
	}

	public Rational(final long numerator) {
		this(numerator, 1);
	}

	public BigInteger getNumerator() {
		return numerator;
	}

	public BigInteger getDenominator() {
		return denominator;
	}

	public Rational add(final Rational other) {
		return new Rational(numerator.multiply(other.denominator).add(other.numerator.multiply(denominator)), denominator.multiply(other.denominator));
	}

	public Rational add(final BigInteger other) {
		return add(new Rational(other));
	}

	public Rational add(final long other) {
		return add(new Rational(other));
	}

	public Rational subtract(final Rational other) {
		return new Rational(numerator.multiply(other.denominator).subtract(other.numerator.multiply(denominator)), denominator.multiply(other.denominator));
	}

	public Rational subtract(final BigInteger other) {
		return subtract(new Rational(other));
	}

	public Rational subtract(final long other) {
		return subtract(new Rational(other));
	}

	public Rational multiply(final Rational other) {
		return new Rational(numerator.multiply(other.numerator), denominator.multiply(other.denominator));
	}

	public Rational multiply(final BigInteger other) {
		return multiply(new Rational(other));
	}

	public Rational multiply(final long other) {
		return multiply(new Rational(other));
	}

	public Rational divide(final Rational other) {
		return new Rational(numerator.multiply(other.denominator), denominator.multiply(other.numerator));
	}

	public Rational divide(final BigInteger other) {
		return divide(new Rational(other));
	}

	public Rational divide(final long other) {
		return divide(new Rational(other));
	}

	public Rational negate() {
		return new Rational(numerator.negate(), denominator);
	}

	public Rational invert() {
		return new Rational(denominator, numerator);
	}

	public int signum() {
		return numerator.signum();
	}
	
	public BigDecimal decimalValue() {
		return new BigDecimal(numerator).divide(new BigDecimal(denominator));
	}

	@Override
	public double doubleValue() {
		return decimalValue().doubleValue();
	}

	@Override
	public float floatValue() {
		return decimalValue().floatValue();
	}

	@Override
	public int intValue() {
		return decimalValue().intValue();
	}

	@Override
	public long longValue() {
		return decimalValue().longValue();
	}

	@Override
	public int hashCode() {
		final int prime = 37;
		int result = 1;
		result = prime * result + denominator.hashCode();
		result = prime * result + numerator.hashCode();
		return result;
	}

	@Override
	public boolean equals(final Object object) {
		if (this == object)
			return true;
		if (object == null)
			return false;
		if (getClass() != object.getClass())
			return false;
		final Rational other = (Rational) object;
		if (!denominator.equals(other.denominator))
			return false;
		if (!numerator.equals(other.numerator))
			return false;
		return true;
	}
	
	/*
	 * a/b < c/d
	 * ==
	 * a < cb / d
	 * ==
	 * ad < cb
	 */
	@Override
	public int compareTo(final Rational other) {
		return numerator.multiply(other.denominator).compareTo(other.numerator.multiply(denominator));
	}

	public Rational max(final Rational other) {
		return compareTo(other) >= 0 ? this : other;
	}
	
	public Rational min(final Rational other) {
		return compareTo(other) <= 0 ? this : other;
	}
	
	public Rational abs() {
		return signum() >= 0 ? this : negate();
	}

	public Rational pow(final int n) {
		return new Rational(numerator.pow(n), denominator.pow(n));
	}

	@Override
	public String toString() {
		final StringBuilder buffer = new StringBuilder();
		buffer.append(numerator.toString());
		if (!denominator.equals(BigInteger.ONE)) {
			buffer.append("/");
			buffer.append(denominator.toString());
		}
		return buffer.toString();
	}

	public static final Rational ZERO = new Rational(0);
	
	public static final Rational ONE = new Rational(1);

}
