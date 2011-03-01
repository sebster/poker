package com.sebster.math.matrix;

import com.sebster.math.field.FieldValue;
import com.sebster.math.vector.ImmutableVector;
import com.sebster.math.vector.MutableVector;
import com.sebster.math.vector.Vector;

public class TransposeMatrix<T extends FieldValue<T>> extends AbstractMatrix<T> {

	private final Matrix<T> delegate;

	protected TransposeMatrix(final MatrixSpace<T> matrixSpace) {
		super(matrixSpace);
		// TODO Auto-generated constructor stub
		delegate = null;
	}

	@Override
	public T get(final int i, final int j) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ImmutableMatrix<T> plus(final Matrix<T> other) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ImmutableMatrix<T> minus(final Matrix<T> other) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ImmutableMatrix<T> times(final Matrix<T> other) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ImmutableMatrix<T> leftTimes(final Matrix<T> other) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ImmutableVector<T> times(final Vector<T> vector) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ImmutableMatrix<T> times(final T scalar) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ImmutableMatrix<T> dividedBy(final T scalar) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ImmutableMatrix<T> opposite() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MutableVector<T> solve(final MutableVector<T> x, final Vector<T> b) {
		// TODO Auto-generated method stub
		return null;
	}

}
