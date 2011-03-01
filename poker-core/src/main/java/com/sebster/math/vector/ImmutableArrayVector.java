package com.sebster.math.vector;

import net.jcip.annotations.Immutable;

import com.sebster.math.field.FieldValue;

/**
 * An immutable vector over a specified field backed by an array.
 * 
 * @author sebster
 * 
 * @param <T>
 *            the field value type
 */
@Immutable
public final class ImmutableArrayVector<T extends FieldValue<T>> extends AbstractArrayVector<T> implements ImmutableVector<T> {

	/**
	 * Construct a new immutable array backed vector with the same vector field
	 * and the same components as the specified vector.
	 * 
	 * @param other
	 *            the other vector
	 * 
	 * @throws NullPointerException
	 *             if <code>other</code> is <code>null</code>
	 */
	public ImmutableArrayVector(final Vector<T> other) {
		super(other);
	}

	/**
	 * Construct a new immutable array backed vector with the specified vector
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
	public ImmutableArrayVector(final VectorSpace<T> vectorSpace, final T value) {
		super(vectorSpace, value);
	}

	/**
	 * Construct a new immutable array backed vector with the specified vector
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
	public ImmutableArrayVector(final VectorSpace<T> vectorSpace, final T... values) {
		super(vectorSpace, values);
	}

	protected ImmutableArrayVector(final VectorSpace<T> vectorSpace, final Object[] values) {
		super(vectorSpace, values);
	}

}
