package com.sebster.math.vector;

import net.jcip.annotations.NotThreadSafe;

import com.sebster.math.field.FieldValue;
import com.sebster.util.Validate;

/**
 * A mutable vector over a specified field backed by an array.
 * 
 * @author sebster
 * 
 * @param <T>
 *            the field value type
 */
@NotThreadSafe
public final class ArrayVector<T extends FieldValue<T>> extends AbstractArrayVector<T> implements MutableVector<T> {

	/**
	 * Construct a new mutable array backed vector with the same vector field
	 * and the same components as the specified vector.
	 * 
	 * @param other
	 *            the other vector
	 * 
	 * @throws NullPointerException
	 *             if <code>other</code> is <code>null</code>
	 */
	public ArrayVector(final Vector<T> other) {
		super(other);
	}

	/**
	 * Construct a new mutable array backed vector with the specified vector
	 * space and all components initialized to zero.
	 * 
	 * @param vectorSpace
	 *            the vector space
	 * @param value
	 *            the initial value for all components
	 * 
	 * @throws NullPointerException
	 *             if <code>vectorSpace</code> or <code>value</code> is
	 *             <code>null</code>
	 */
	public ArrayVector(final VectorSpace<T> vectorSpace) {
		super(vectorSpace);
	}

	/**
	 * Construct a new mutable array backed vector with the specified vector
	 * space and all components initialized to the specified value.
	 * 
	 * @param vectorSpace
	 *            the vector space
	 * @param value
	 *            the initial value for all components
	 * 
	 * @throws NullPointerException
	 *             if <code>vectorSpace</code> or <code>value</code> is
	 *             <code>null</code>
	 */
	public ArrayVector(final VectorSpace<T> vectorSpace, final T value) {
		super(vectorSpace, value);
	}

	/**
	 * Construct a new mutable array backed vector with the specified vector
	 * space and all components initialized to the specified value.
	 * 
	 * @param vectorSpace
	 *            the vector space
	 * @param values
	 *            the initial values for all components
	 * 
	 * @throws IllegalArgumentException
	 *             if the number of values supplied is incorrect
	 * @throws NullPointerException
	 *             if <code>vectorSpace</code> or <code>values</code> or one of
	 *             the supplied values is <code>null</code>
	 */
	public ArrayVector(final VectorSpace<T> vectorSpace, final T... values) {
		super(vectorSpace, values);
	}

	protected ArrayVector(final VectorSpace<T> vectorSpace, final Object[] values) {
		super(vectorSpace, values);
	}

	@Override
	public ArrayVector<T> set(final int i, final T value) {
		Validate.notNull(value, "value");
		internalSet(i, value);
		return this;
	}

	@Override
	public ArrayVector<T> set(final T... values) {
		internalSet(values);
		return this;
	}
	
	@Override
	public ArrayVector<T> setFrom(final Vector<T> other) {
		internalSetFrom(other);
		return this;
	}
	
	@Override
	public ArrayVector<T> fill(final T value) {
		internalFill(value);
		return this;
	}

	@Override
	public ArrayVector<T> add(final Vector<T> other) {
		checkDimension(other);
		final int dimension = getDimension();
		for (int i = 0; i < dimension; i++) {
			internalSet(i, get(i).plus(other.get(i)));
		}
		return this;
	}

	@Override
	public ArrayVector<T> subtract(final Vector<T> other) {
		checkDimension(other);
		final int dimension = getDimension();
		for (int i = 0; i < dimension; i++) {
			internalSet(i, get(i).minus(other.get(i)));
		}
		return this;
	}

	@Override
	public ArrayVector<T> multiplyBy(final T scalar) {
		final int dimension = getDimension();
		for (int i = 0; i < dimension; i++) {
			internalSet(i, get(i).times(scalar));
		}
		return this;
	}

	@Override
	public ArrayVector<T> divideBy(final T scalar) {
		final int dimension = getDimension();
		for (int i = 0; i < dimension; i++) {
			internalSet(i, get(i).dividedBy(scalar));
		}
		return this;
	}

	@Override
	public ArrayVector<T> negate() {
		final int dimension = getDimension();
		for (int i = 0; i < dimension; i++) {
			internalSet(i, get(i).opposite());
		}
		return this;
	}

}
