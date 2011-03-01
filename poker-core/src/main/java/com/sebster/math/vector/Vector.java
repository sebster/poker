package com.sebster.math.vector;

import com.sebster.math.field.Field;
import com.sebster.math.field.FieldValue;

/**
 * A vector over a specified field.
 * 
 * @author sebster
 * 
 * @param <T>
 *            the field value type
 */
public interface Vector<T extends FieldValue<T>> {

	/**
	 * Get the dimension of this vector.
	 * 
	 * @return the dimension of this vector
	 */
	int getDimension();

	/**
	 * Get the <code>i</code>th component of this vector.
	 * 
	 * @param i
	 *            the index
	 * @return the <code>i</code>th component of this vector
	 * 
	 * @throws IndexOutOfBoundsException
	 *             if the index <code>i</code> is out of range
	 */
	T get(int i);

	/**
	 * Return an immutable vector whose value is this vector plus the specified
	 * other vector.
	 * 
	 * @param other
	 *            the other vector
	 * @return a vector whose value is this vector plus the specified other
	 *         vector
	 * 
	 * @throws NullPointerException
	 *             if <code>other</code> is <code>null</code>
	 * @throws IllegalArgumentException
	 *             if <code>other</code> has a different vector field
	 */
	ImmutableVector<T> plus(Vector<T> other);

	/**
	 * Return a vector whose value is this vector minus the specified other
	 * vector.
	 * 
	 * @param other
	 *            the other vector
	 * @return a vector whose value is this vector minus the specified other
	 *         vector
	 * 
	 * @throws NullPointerException
	 *             if <code>other</code> is <code>null</code>
	 * @throws IllegalArgumentException
	 *             if <code>other</code> has a different vector field
	 */
	ImmutableVector<T> minus(Vector<T> other);

	/**
	 * Return an immutable vector whose value is the dot product of this vector
	 * and the specified other vector.
	 * 
	 * @param other
	 *            the other vector
	 * @return a vector whose value is the dot product of this vector and the
	 *         specified other vector
	 * 
	 * @throws NullPointerException
	 *             if <code>other</code> is <code>null</code>
	 * @throws IllegalArgumentException
	 *             if <code>other</code> has a different vector field
	 */
	T dot(Vector<T> other);

	/**
	 * Return an immutable vector whose value is this vector times the specified
	 * scalar.
	 * 
	 * @param scalar
	 *            the scalar
	 * @return a vector whose value is this vector times the specified scalar
	 *         value
	 * 
	 * @throws NullPointerException
	 *             if <code>scalar</code> is <code>null</code>
	 */
	ImmutableVector<T> times(T scalar);

	/**
	 * Return an immutable vector whose value is this vector divided by the
	 * specified scalar.
	 * 
	 * @param scalar
	 *            the scalar
	 * @return a vector whose value is this vector divided by the specified
	 *         scalar value
	 * 
	 * @throws NullPointerException
	 *             if <code>scalar</code> is <code>null</code>
	 * @throws ArithmeticException
	 *             if <code>scalar</code> is zero
	 */
	ImmutableVector<T> dividedBy(T scalar);

	/**
	 * Return an immutable vector whose value is the additive inverse (or
	 * opposite) of this vector.
	 * 
	 * @return the opposite of this vector
	 */
	ImmutableVector<T> opposite();

	/**
	 * Return the scalar field associated with this vector.
	 * 
	 * @return the scalar field associated with this vector
	 */
	Field<T> getScalarField();

	/**
	 * Return the vector space associated with this vector.
	 * 
	 * @return the vector space associated with this vector
	 */
	VectorSpace<T> getVectorSpace();

	/**
	 * Return the vector space of the specified dimension associated with this
	 * vector.
	 * 
	 * @return the vector space of the specified dimension associated with this
	 *         vector
	 */
	VectorSpace<T> getVectorSpace(int dimension);

}
