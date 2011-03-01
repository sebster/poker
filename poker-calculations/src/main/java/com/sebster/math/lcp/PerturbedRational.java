package com.sebster.math.lcp;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Arrays;

import com.sebster.math.rational.Rational;
import com.sebster.math.rational.RationalField;
import com.sebster.math.vector.ImmutableVector;
import com.sebster.math.vector.Vector;
import com.sebster.math.vector.VectorSpace;

public final class PerturbedRational extends Number implements Comparable<PerturbedRational>, ImmutableVector<Rational> {

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

	@Override
	public Rational get(final int i) {
		return terms[i];
	}

	public void set(final int i, final Rational value) {
//		Validate.notNull(value, "value == null");
		terms[i] = value;
	}

	@Override
	public PerturbedRational plus(final Vector<Rational> other) {
//		Validate.notNull(other, "other == null");
		final int newSize = Math.min(getDimension(), other.getDimension());
		final Rational[] newTerms = new Rational[newSize];
		for (int i = 0; i < newSize; i++) {
			newTerms[i] = get(i).plus(other.get(i));
		}
		return new PerturbedRational(newTerms.length, newTerms);
	}

	@Override
	public PerturbedRational minus(final Vector<Rational> other) {
//		Validate.notNull(other, "other == null");
		final int newSize = Math.min(getDimension(), other.getDimension());
		final Rational[] newTerms = new Rational[newSize];
		for (int i = 0; i < newSize; i++) {
			newTerms[i] = get(i).minus(other.get(i));
		}
		return new PerturbedRational(newTerms.length, newTerms);
	}

	@Override
	public PerturbedRational opposite() {
		final Rational[] newTerms = new Rational[terms.length];
		for (int i = 0; i < terms.length; i++) {
			newTerms[i] = terms[i].opposite();
		}
		return new PerturbedRational(newTerms.length, newTerms);
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

	public BigDecimal decimalValue(final MathContext mathContext) {
		return get(0).decimalValue(mathContext);
	}

	@Override
	public double doubleValue() {
		return get(0).doubleValue();
	}

	@Override
	public float floatValue() {
		return get(0).floatValue();
	}

	@Override
	public int intValue() {
		return get(0).intValue();
	}

	@Override
	public long longValue() {
		return get(0).longValue();
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
		final int minSize = Math.min(getDimension(), other.getDimension());
		for (int i = 0; i < minSize; i++) {
			final int k = get(i).compareTo(other.get(i));
			if (k != 0) {
				return k;
			}
		}
		if (getDimension() > minSize) {
			return get(minSize).signum();
		}
		if (other.getDimension() > minSize) {
			return -other.get(minSize).signum();
		}
		return 0;
	}

	public PerturbedRational abs() {
		return signum() >= 0 ? this : opposite();
	}

	@Override
	public String toString() {
		final int n = getDimension();
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
	public PerturbedRational times(final Rational scalar) {
//		Validate.notNull(scalar, "scalar == null");
		final Rational[] newTerms = new Rational[terms.length];
		for (int i = 0; i < terms.length; i++) {
			newTerms[i] = terms[i].times(scalar);
		}
		return new PerturbedRational(newTerms.length, newTerms);
	}

	@Override
	public PerturbedRational dividedBy(final Rational scalar) {
//		Validate.notNull(scalar, "scalar == null");
		final Rational[] newTerms = new Rational[terms.length];
		for (int i = 0; i < terms.length; i++) {
			newTerms[i] = terms[i].dividedBy(scalar);
		}
		return new PerturbedRational(newTerms.length, newTerms);
	}

	@Override
	public Rational dot(final Vector<Rational> other) {
		throw new UnsupportedOperationException();
	}

	@Override
	public int getDimension() {
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
		return RationalField.INSTANCE;
	}

	@Override
	public VectorSpace<Rational> getVectorSpace() {
		return getVectorSpace(terms.length);
	}

	@Override
	public VectorSpace<Rational> getVectorSpace(final int dimension) {
		return new VectorSpace<Rational>(RationalField.INSTANCE, dimension);
	}

}
