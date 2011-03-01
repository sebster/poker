package com.sebster.math.matrix;

import com.sebster.math.field.FieldValue;
import com.sebster.math.vector.ImmutableVector;
import com.sebster.math.vector.Vector;
import com.sebster.util.Validate;

public class ArrayMatrix<T extends FieldValue<T>> extends AbstractArrayMatrix<T> implements MutableMatrix<T> {

	public ArrayMatrix(final Matrix<T> other) {
		super(other);
	}

	public ArrayMatrix(final MatrixSpace<T> matrixSpace) {
		super(matrixSpace);
	}

	public ArrayMatrix(final MatrixSpace<T> matrixSpace, final T initialValue) {
		super(matrixSpace, initialValue);
	}

	protected ArrayMatrix(final MatrixSpace<T> matrixSpace, final Object[][] values) {
		super(matrixSpace, values);
	}

	@Override
	public ArrayMatrix<T> add(final Matrix<T> other) {
		checkDimensions(other);
		final int rows = getRows(), columns = getColumns();
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				unsafeSet(i, j, get(i, j).plus(other.get(i, j)));
			}
		}
		return this;
	}

	@Override
	public ArrayMatrix<T> subtract(final Matrix<T> other) {
		checkDimensions(other);
		final int rows = getRows(), columns = getColumns();
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				unsafeSet(i, j, get(i, j).minus(other.get(i, j)));
			}
		}
		return this;
	}

	@Override
	public MutableMatrix<T> multiplyBy(final Matrix<T> other) {
		checkTimesDimensions(other);
		final int rows = getRows(), columns = getColumns(), columnsOther = other.getColumns();
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columnsOther; j++) {
				T value = getScalarField().getZero();
				for (int k = 0; k < columns; k++) {
					value = value.plus(get(i, k).times(other.get(k, j)));
				}
				unsafeSet(i, j, value);
			}
		}
		return this;
	}

	@Override
	public MutableMatrix<T> leftMultiplyBy(final Matrix<T> other) {
		checkLeftTimesDimensions(other);
		final int rows = getRows(), columns = getColumns(), rowsOther = other.getRows();
		for (int i = 0; i < rowsOther; i++) {
			for (int j = 0; j < columns; j++) {
				T value = getScalarField().getZero();
				for (int k = 0; k < rows; k++) {
					value = value.plus(other.get(i, k).times(get(k, j)));
				}
				unsafeSet(i, j, value);
			}
		}
		return this;
	}

	@Override
	public ArrayMatrix<T> multiplyBy(final T scalar) {
		Validate.notNull(scalar, "scalar");
		final int rows = getRows(), columns = getColumns();
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				unsafeSet(i, j, get(i, j).times(scalar));
			}
		}
		return this;
	}

	@Override
	public ArrayMatrix<T> divideBy(final T scalar) {
		Validate.notNull(scalar, "scalar");
		final int rows = getRows(), columns = getColumns();
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				unsafeSet(i, j, get(i, j).times(scalar));
			}
		}
		return this;
	}

	@Override
	public ArrayMatrix<T> negate() {
		final int rows = getRows(), columns = getColumns();
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				unsafeSet(i, j, get(i, j).opposite());
			}
		}
		return this;
	}

	@Override
	public ArrayMatrix<T> set(final int i, final int j, final T value) {
		Validate.notNull(value, "value");
		return (ArrayMatrix<T>) super.set(i, j, value);
	}

	protected ArrayMatrix<T> unsafeSet(final int i, final int j, final T value) {
		return (ArrayMatrix<T>) super.set(i, j, value);
	}

	@Override
	public ImmutableVector<T> times(final Vector<T> vector) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MutableMatrix<T> fill(final T value) {
		// TODO Auto-generated method stub
		return null;
	}

}
