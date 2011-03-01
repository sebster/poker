package com.sebster.math.matrix;

import net.jcip.annotations.Immutable;

import com.sebster.math.field.FieldValue;
import com.sebster.math.vector.ImmutableVector;
import com.sebster.math.vector.Vector;

/**
 * An immutable matrix over a specified field backed by an array.
 * 
 * @author sebster
 * 
 * @param <T>
 *            the field value type
 */
@Immutable
public final class ImmutableArrayMatrix<T extends FieldValue<T>> extends AbstractArrayMatrix<T> implements ImmutableMatrix<T> {

	/**
	 * Construct a new immutable array backed array with the same matrix field
	 * and the same components as the specified matrix.
	 * 
	 * @param other
	 *            the other matrix
	 * 
	 * @throws NullPointerException
	 *             if <code>other</code> is <code>null</code>
	 */
	public ImmutableArrayMatrix(final Matrix<T> other) {
		super(other);
	}

	/**
	 * Construct a new immutable matrix backed vector with the specified matrix
	 * space and all components initialized to the specified value.
	 * 
	 * @param matrixSpace
	 *            the matrix space
	 * @param value
	 *            the initial value for all components
	 * 
	 * @throws NullPointerException
	 *             if <code>matrixSpace</code> or <code>value</code> is
	 *             <code>null</code>
	 */
	public ImmutableArrayMatrix(final MatrixSpace<T> matrixSpace, final T value) {
		super(matrixSpace, value);
	}

	protected ImmutableArrayMatrix(final MatrixSpace<T> matrixSpace, final Object[][] values) {
		super(matrixSpace, values);
	}

	@Override
	public ImmutableVector<T> times(final Vector<T> vector) {
		// TODO Auto-generated method stub
		return null;
	}

}
