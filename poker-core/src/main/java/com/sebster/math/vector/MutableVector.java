package com.sebster.math.vector;

import net.jcip.annotations.NotThreadSafe;

import com.sebster.math.field.FieldValue;

/**
 * A mutable vector over a specified field.
 * 
 * @author sebster
 * 
 * @param <T>
 *            the field value type
 */
@NotThreadSafe
public interface MutableVector<T extends FieldValue<T>> extends Vector<T> {

	/**
	 * Set the <code>i</code>th component of this vector to the specified value.
	 * 
	 * @param i
	 *            the index
	 * @param value
	 *            the value
	 * @return this
	 * 
	 * @throws NullPointerException
	 *             if the value is <code>null</code>
	 * @throws IndexOutOfBoundsException
	 *             if the index <code>i</code> is out of range
	 */
	MutableVector<T> set(int i, T value);

	/**
	 * Set the components of this vector to the specified values.
	 * 
	 * @param values
	 *            the components of this vector
	 * @return this
	 * 
	 * @throws NullPointerException
	 *             if the <code>values</code> or one of the specified values is
	 *             <code>null</code>
	 * @throws IllegalArgumentException
	 *             if the number of values does not match the dimension of this
	 *             vector
	 */
	MutableVector<T> set(T... values);

	/**
	 * Set the components of this vector to the components of the specified
	 * vector.
	 * 
	 * @param other
	 *            the other vector
	 * @return this
	 * 
	 * @throws NullPointerException
	 *             if <code>other</code> is null
	 * @throws IllegalArgumentException
	 *             if the dimension of the other vector does not match the
	 *             dimension of this vector
	 */
	MutableVector<T> setFrom(Vector<T> other);

	/**
	 * Set all components of this vector to the specified value.
	 * 
	 * @param value
	 *            the value
	 * @return this
	 * 
	 * @throws NullPointerException
	 *             if the value is <code>null</code>
	 */
	MutableVector<T> fill(T value);

	/**
	 * Add the specified vector to this vector.
	 * 
	 * @param other
	 *            the other vector
	 * @return this
	 * 
	 * @throws NullPointerException
	 *             if <code>other</code> is <code>null</code>
	 * @throws IllegalArgumentException
	 *             if <code>other</code> has a different vector field
	 */
	MutableVector<T> add(Vector<T> other);

	/**
	 * Subtract the specified vector from this vector.
	 * 
	 * @param other
	 *            the other vector
	 * @return this
	 * 
	 * @throws NullPointerException
	 *             if <code>other</code> is <code>null</code>
	 * @throws IllegalArgumentException
	 *             if <code>other</code> has a different vector field
	 */
	MutableVector<T> subtract(Vector<T> other);

	/**
	 * Multiply this vector by the specified scalar.
	 * 
	 * @param other
	 *            the scalar
	 * @return this
	 * 
	 * @throws NullPointerException
	 *             if <code>scalar</code> is <code>null</code>
	 */
	MutableVector<T> multiplyBy(T scalar);

	/**
	 * Divide this vector by the specified scalar.
	 * 
	 * @param other
	 *            the scalar
	 * @return this
	 * 
	 * @throws NullPointerException
	 *             if <code>scalar</code> is <code>null</code>
	 * @throws ArithmeticException
	 *             if <code>scalar</code> is zero
	 */
	MutableVector<T> divideBy(T scalar);

	/**
	 * Negate this vector (change it to its additive inverse).
	 * 
	 * @return this
	 */
	MutableVector<T> negate();

}
