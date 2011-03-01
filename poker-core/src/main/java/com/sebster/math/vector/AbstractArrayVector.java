package com.sebster.math.vector;

import com.sebster.math.field.FieldValue;
import com.sebster.util.Validate;

public abstract class AbstractArrayVector<T extends FieldValue<T>> extends AbstractVector<T> {

	private final Object[] values;

	public AbstractArrayVector(final Vector<T> other) {
		super(other.getVectorSpace());
		values = new Object[getDimension()];
		internalSetFrom(other);
	}

	public AbstractArrayVector(final VectorSpace<T> vectorSpace) {
		this(vectorSpace.getZero());
	}

	public AbstractArrayVector(final VectorSpace<T> vectorSpace, final T value) {
		super(vectorSpace);
		values = new Object[getDimension()];
		internalFill(value);
	}

	public AbstractArrayVector(final VectorSpace<T> vectorSpace, final T... values) {
		super(vectorSpace);
		this.values = new Object[getDimension()];
		internalSet(values);
	}

	protected AbstractArrayVector(final VectorSpace<T> vectorSpace, final Object[] values) {
		super(vectorSpace);
		this.values = values;
	}

	@SuppressWarnings("unchecked")
	@Override
	public T get(final int i) {
		return (T) values[i];
	}

	protected final void internalSet(final int i, final T value) {
		values[i] = value;
	}

	protected final void internalSet(final T... values) {
		final int dimension = getDimension();
		Validate.isTrue(dimension == values.length, "invalid number of values specified");
		for (int i = 0; i < dimension; i++) {
			final T value = values[i];
			if (value == null) {
				throw new NullPointerException("values[" + i + "]");
			}
			this.values[i] = value;
		}
	}

	protected final void internalSetFrom(final Vector<T> other) {
		final int dimension = getDimension();
		for (int i = 0; i < dimension; i++) {
			values[i] = other.get(i);
		}
	}

	protected final void internalFill(final T value) {
		Validate.notNull(value, "value");
		final int dimension = getDimension();
		for (int i = 0; i < dimension; i++) {
			values[i] = value;
		}
	}

	@Override
	public ImmutableVector<T> plus(final Vector<T> other) {
		checkDimension(other);
		final int dimension = getDimension();
		final Object[] values = new Object[dimension];
		for (int i = 0; i < dimension; i++) {
			values[i] = get(i).plus(other.get(i));
		}
		return createImmutableVector(values);
	}

	@Override
	public ImmutableVector<T> minus(final Vector<T> other) {
		checkDimension(other);
		final int dimension = getDimension();
		final Object[] values = new Object[dimension];
		for (int i = 0; i < dimension; i++) {
			values[i] = get(i).plus(other.get(i));
		}
		return createImmutableVector(values);
	}

	@Override
	public ImmutableVector<T> times(final T scalar) {
		if (scalar.signum() == 0) {
			return getVectorSpace().getZero();
		}
		final int dimension = getDimension();
		final Object[] values = new Object[dimension];
		for (int i = 0; i < dimension; i++) {
			values[i] = get(i).times(scalar);
		}
		return createImmutableVector(values);
	}

	@Override
	public ImmutableVector<T> dividedBy(final T scalar) {
		final int dimension = getDimension();
		final Object[] values = new Object[dimension];
		for (int i = 0; i < dimension; i++) {
			values[i] = get(i).dividedBy(scalar);
		}
		return createImmutableVector(values);
	}

	@Override
	public ImmutableVector<T> opposite() {
		final int dimension = getDimension();
		final Object[] values = new Object[dimension];
		for (int i = 0; i < dimension; i++) {
			values[i] = get(i).opposite();
		}
		return createImmutableVector(values);
	}

	/**
	 * Return a new immutable vector with the specified array of values.
	 * 
	 * @param values
	 *            the array of values
	 * @return a new immutable vector with the specified array of values
	 */
	protected ImmutableVector<T> createImmutableVector(final Object[] values) {
		return new ImmutableArrayVector<T>(getVectorSpace(), values);
	}

}
