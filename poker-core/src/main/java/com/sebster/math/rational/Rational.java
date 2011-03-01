package com.sebster.math.rational;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sebster.math.field.Field;
import com.sebster.math.field.FieldValue;

public final class Rational extends Number implements Comparable<Rational>, FieldValue<Rational> {

	private static Logger logger = LoggerFactory.getLogger(Rational.class);

	private static long multiplications = 0;

	private static long divisions = 0;

	private static long additions = 0;

	private static long gcds = 0;

	private static long compares = 0;
	
	public static long getMultiplications() {
		return multiplications;
	}

	public static long getDivisions() {
		return divisions;
	}

	public static long getAdditions() {
		return additions;
	}

	public static long getGcds() {
		return gcds;
	}

	public static long getCompares() {
		return compares;
	}

	public static void resetCounters() {
		logger.debug("counters reset");
		multiplications = 0;
		divisions = 0;
		additions = 0;
		gcds = 0;
		compares = 0;
	}

	public static void logCounters(final String message) {
		if (logger.isDebugEnabled()) {
			logger.debug("{}: multiplications={} divisions={} additions={} gcds={} compares={}", new Object[] { message, multiplications, divisions, additions, gcds, compares });
		}
	}

	private static final long serialVersionUID = 1L;

	public static final Rational ZERO = new Rational(BigInteger.ZERO, BigInteger.ONE, false);

	public static final Rational ONE = new Rational(BigInteger.ONE, BigInteger.ONE, false);

	private final BigInteger numerator;

	private final BigInteger denominator;

	private Rational(final BigInteger numerator, final BigInteger denominator, final boolean canonicalize) {
		if (canonicalize) {
			if (denominator.signum() == 0) {
				throw new ArithmeticException("division by zero");
			}
			if (numerator.signum() == 0) {
				// Zero.
				this.numerator = BigInteger.ZERO;
				this.denominator = BigInteger.ONE;
			} else {
				// Non-zero.
				BigInteger tmpNumerator = numerator;
				BigInteger tmpDenominator = denominator;
				if (denominator.signum() < 0) {
					// Denominator is always positive in the internal
					// representation.
					tmpNumerator = tmpNumerator.negate();
					tmpDenominator = tmpDenominator.negate();
				}

				final BigInteger gcd = tmpNumerator.gcd(tmpDenominator);
				gcds++;
				this.numerator = tmpNumerator.divide(gcd);
				this.denominator = tmpDenominator.divide(gcd);
				divisions += 2;
			}
		} else {
			this.numerator = numerator;
			this.denominator = denominator;
		}

	}

	public Rational(final BigInteger numerator, final BigInteger denominator) {
		this(numerator, denominator, true);
	}

	public Rational(final BigInteger numerator) {
		this(numerator, BigInteger.ONE, false);
	}

	public Rational(final long numerator, final long denominator) {
		this(BigInteger.valueOf(numerator), BigInteger.valueOf(denominator));
	}

	public Rational(final long numerator) {
		this(BigInteger.valueOf(numerator));
	}

	public Rational(final String numerator, final String denominator) {
		this(new BigInteger(numerator), new BigInteger(denominator));
	}

	public Rational(final String numerator) {
		this(new BigInteger(numerator));
	}

	public BigInteger getNumerator() {
		return numerator;
	}

	public BigInteger getDenominator() {
		return denominator;
	}

	@Override
	public Rational plus(final Rational other) {
		additions++;
		multiplications += 3;
		return new Rational(numerator.multiply(other.denominator).add(other.numerator.multiply(denominator)), denominator.multiply(other.denominator));
	}

	public Rational add(final BigInteger other) {
		additions++;
		multiplications++;
		return new Rational(numerator.add(other.multiply(denominator)), denominator);
	}

	public Rational add(final long other) {
		return add(BigInteger.valueOf(other));
	}

	@Override
	public Rational minus(final Rational other) {
		additions++;
		multiplications += 3;
		return new Rational(numerator.multiply(other.denominator).subtract(other.numerator.multiply(denominator)), denominator.multiply(other.denominator));
	}

	public Rational minus(final BigInteger other) {
		additions++;
		multiplications++;
		return new Rational(numerator.subtract(other.multiply(denominator)), denominator);
	}

	public Rational minus(final long other) {
		return minus(BigInteger.valueOf(other));
	}

	@Override
	public Rational times(final Rational other) {
		multiplications += 2;
		return new Rational(numerator.multiply(other.numerator), denominator.multiply(other.denominator));
	}

	public Rational times(final BigInteger other) {
		multiplications++;
		return new Rational(numerator.multiply(other), denominator);
	}

	public Rational times(final long other) {
		return times(BigInteger.valueOf(other));
	}

	@Override
	public Rational dividedBy(final Rational other) {
		multiplications += 2;
		return new Rational(numerator.multiply(other.denominator), denominator.multiply(other.numerator));
	}

	public Rational dividedBy(final BigInteger other) {
		multiplications++;
		return new Rational(numerator, denominator.multiply(other));
	}

	public Rational dividedBy(final long other) {
		return dividedBy(BigInteger.valueOf(other));
	}

	@Override
	public Rational opposite() {
		return new Rational(numerator.negate(), denominator, false);
	}

	@Override
	public Rational reciprocal() {
		final int numeratorSignum = numerator.signum();
		if (numeratorSignum > 0) {
			return new Rational(denominator, numerator, false);
		}
		if (numeratorSignum < 0) {
			return new Rational(denominator.negate(), numerator.negate(), false);
		}
		throw new ArithmeticException("division by zero");
	}

	@Override
	public int signum() {
		return numerator.signum();
	}

	public BigDecimal decimalValue(final MathContext mathContext) {
		return new BigDecimal(numerator).divide(new BigDecimal(denominator), mathContext);
	}

	@Override
	public double doubleValue() {
		return decimalValue(MathContext.DECIMAL64).doubleValue();
	}

	@Override
	public float floatValue() {
		return decimalValue(MathContext.DECIMAL32).floatValue();
	}

	@Override
	public int intValue() {
		return numerator.divide(denominator).intValue();
	}

	@Override
	public long longValue() {
		return numerator.divide(denominator).longValue();
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
		if (object instanceof Rational) {
			final Rational other = (Rational) object;
			return compareTo(other) == 0;
		}
		return false;
	}

	/*
	 * a/b < c/d == a < cb / d == ad < cb
	 */
	@Override
	public int compareTo(final Rational other) {
		final int k = numerator.signum() - other.numerator.signum();
		if (k != 0) {
			return k;
		}
		multiplications += 2;
		compares++;
		return numerator.multiply(other.denominator).compareTo(other.numerator.multiply(denominator));
	}

	public Rational max(final Rational other) {
		return compareTo(other) >= 0 ? this : other;
	}

	public Rational min(final Rational other) {
		return compareTo(other) <= 0 ? this : other;
	}

	@Override
	public Rational abs() {
		return signum() >= 0 ? this : opposite();
	}

	public Rational pow(final int n) {
		return new Rational(numerator.pow(n), denominator.pow(n), false);
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

	@Override
	public Field<Rational> getField() {
		return RationalField.INSTANCE;
	}

	public static Rational fromString(final String string) {
		final int i = string.indexOf('/');
		if (i == -1) {
			return new Rational(new BigInteger(string));
		}
		return new Rational(new BigInteger(string.substring(0, i)), new BigInteger(string.substring(i + 1)));
	}

}
