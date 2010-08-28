package com.sebster.math.rational;

import java.math.BigDecimal;
import java.util.Arrays;

import com.sebster.math.vector.Vector;

public final class PerturbedRational extends Number implements Comparable<PerturbedRational>, Vector<Rational, PerturbedRational> {

	private final Rational[] terms;

	public PerturbedRational(final int size) {
//		Validate.isTrue(size > 0, "size must be positive");
		terms = new Rational[size];
		Arrays.fill(terms, Rational.ZERO);
	}

	public PerturbedRational(final int size, final Rational... rationals) {
//		Validate.notNull(rationals, "rationals == null");
//		Validate.isTrue(rationals.length <= size, "size less than number of rationals specified");
		terms = new Rational[size];
		for (int i = 0; i < rationals.length; i++) {
//			Validate.notNull(rationals[i], "rationals[" + i + "] == null");
			terms[i] = rationals[i];
		}
		Arrays.fill(terms, rationals.length, size, Rational.ZERO);
	}

	public Rational get(final int i) {
		return terms[i];
	}

	public void set(final int i, final Rational value) {
//		Validate.notNull(value, "value == null");
		terms[i] = value;
	}

	public PerturbedRational add(final PerturbedRational other) {
//		Validate.notNull(other, "other == null");
		final int newSize = Math.min(size(), other.size());
		final Rational[] newTerms = new Rational[newSize];
		for (int i = 0; i < newSize; i++) {
			newTerms[i] = get(i).add(other.get(i));
		}
		return new PerturbedRational(newTerms.length, newTerms);
	}

	public PerturbedRational subtract(final PerturbedRational other) {
//		Validate.notNull(other, "other == null");
		final int newSize = Math.min(size(), other.size());
		final Rational[] newTerms = new Rational[newSize];
		for (int i = 0; i < newSize; i++) {
			newTerms[i] = get(i).subtract(other.get(i));
		}
		return new PerturbedRational(newTerms.length, newTerms);
	}

	@Override
	public PerturbedRational negate() {
		final Rational[] newTerms = new Rational[terms.length];
		for (int i = 0; i < terms.length; i++) {
			newTerms[i] = terms[i].negate();
		}
		return new PerturbedRational(newTerms.length, newTerms);
	}

	public PerturbedRational simplify() {
		final Rational[] newTerms = new Rational[terms.length];
		for (int i = 0; i < terms.length; i++) {
			newTerms[i] = terms[i].simplify();
		}
		return new PerturbedRational(terms.length, newTerms);
	}

	public int signum() {
		for (int i = 0; i < terms.length; i++) {
			final int s = terms[i].signum();
			if (s != 0) {
				return s;
			}
		}
		return 0;
	}
	
	public Rational rationalValue() {
		return get(0);
	}

	public BigDecimal decimalValue() {
		return get(0).decimalValue();
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
		return 41 * Arrays.hashCode(terms);
	}

	@Override
	public boolean equals(final Object object) {
		if (this == object)
			return true;
		if (object == null)
			return false;
		if (getClass() != object.getClass())
			return false;
		final PerturbedRational other = (PerturbedRational) object;
		if (!Arrays.equals(terms, other.terms))
			return false;
		return true;
	}

	@Override
	public int compareTo(final PerturbedRational other) {
//		Validate.notNull(other, "other == null");
		final int minSize = Math.min(size(), other.size());
		for (int i = 0; i < minSize; i++) {
			final int k = get(i).compareTo(other.get(i));
			if (k != 0) {
				return k;
			}
		}
		if (size() > minSize) {
			return get(minSize).signum();
		}
		if (other.size() > minSize) {
			return -other.get(minSize).signum();
		}
		return 0;
	}

	public PerturbedRational abs() {
		return signum() >= 0 ? this : negate();
	}

	@Override
	public String toString() {
		final int n = size();
		final StringBuilder buffer = new StringBuilder("[ ");
		for (int i = 0; i < n - 1; i++) {
			buffer.append(get(i));
			buffer.append(", ");
		}
		buffer.append(get(n - 1));
		buffer.append(" ]");
		return buffer.toString();
	}

	@Override
	public PerturbedRationalVectorSpace getVectorField() {
		return PerturbedRationalVectorSpace.getInstance(size());
	}

	@Override
	public PerturbedRational multiply(final Rational scalar) {
//		Validate.notNull(scalar, "scalar == null");
		final Rational[] newTerms = new Rational[terms.length];
		for (int i = 0; i < terms.length; i++) {
			newTerms[i] = terms[i].multiply(scalar);
		}
		return new PerturbedRational(newTerms.length, newTerms);
	}

	@Override
	public PerturbedRational divide(final Rational scalar) {
//		Validate.notNull(scalar, "scalar == null");
		final Rational[] newTerms = new Rational[terms.length];
		for (int i = 0; i < terms.length; i++) {
			newTerms[i] = terms[i].divide(scalar);
		}
		return new PerturbedRational(newTerms.length, newTerms);
	}

	@Override
	public int size() {
		return terms.length;
	}

	public static final PerturbedRational zero(final int size) {
		return new PerturbedRational(size);
	}

	public static final PerturbedRational one(final int size) {
//		Validate.isTrue(size > 0, "size must be positive");
		final Rational[] terms = new Rational[size];
		terms[0] = Rational.ONE;
		Arrays.fill(terms, 1, size, Rational.ZERO);
		return new PerturbedRational(terms.length, terms);
	}

	@Override
	public RationalField getScalarField() {
		return RationalField.getInstance();
	}

}
