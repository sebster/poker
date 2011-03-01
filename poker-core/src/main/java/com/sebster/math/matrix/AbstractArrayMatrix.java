package com.sebster.math.matrix;

import com.sebster.math.field.FieldValue;
import com.sebster.math.vector.MutableVector;
import com.sebster.math.vector.Vector;
import com.sebster.util.Validate;

public abstract class AbstractArrayMatrix<T extends FieldValue<T>> extends AbstractMatrix<T> {

	private final Object[][] values;

	public AbstractArrayMatrix(final Matrix<T> other) {
		super(other.getMatrixSpace());
		final int rows = getRows(), columns = getColumns();
		values = new Object[rows][columns];
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				values[i][j] = other.get(i, j);
			}
		}
	}

	public AbstractArrayMatrix(final MatrixSpace<T> matrixSpace) {
		this(matrixSpace.getZero());
	}

	public AbstractArrayMatrix(final MatrixSpace<T> matrixSpace, final T value) {
		super(matrixSpace);
		values = new Object[getRows()][getColumns()];
		internalFill(value);
	}

	protected AbstractArrayMatrix(final MatrixSpace<T> matrixSpace, final Object[][] values) {
		super(matrixSpace);
		this.values = values;
	}

	@SuppressWarnings("unchecked")
	@Override
	public T get(final int i, final int j) {
		return (T) values[i][j];
	}

	protected AbstractArrayMatrix<T> set(final int i, final int j, final T value) {
		values[i][j] = value;
		return this;
	}
	
	protected final void internalFill(final T value) {
		Validate.notNull(value, "value");
		final int rows = getRows(), columns = getColumns();
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				values[i][j] = value;
			}
		}
	}

	@Override
	public ImmutableMatrix<T> plus(final Matrix<T> other) {
		checkDimensions(other);
		final int rows = getRows(), columns = getColumns();
		final Object[][] values = new Object[rows][columns];
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				values[i][j] = get(i, j).plus(other.get(i, j));
			}
		}
		return createImmutableMatrix(values);
	}

	@Override
	public ImmutableMatrix<T> minus(final Matrix<T> other) {
		checkDimensions(other);
		final int rows = getRows(), columns = getColumns();
		final Object[][] values = new Object[rows][columns];
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				values[i][j] = get(i, j).minus(other.get(i, j));
			}
		}
		return createImmutableMatrix(values);
	}

	@Override
	public ImmutableMatrix<T> times(final Matrix<T> other) {
		checkTimesDimensions(other);
		final int rows = getRows(), columns = getColumns(), columnsOther = other.getColumns();
		final Object[][] values = new Object[rows][columnsOther];
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columnsOther; j++) {
				T value = getScalarField().getZero();
				for (int k = 0; k < columns; k++) {
					value = value.plus(get(i, k).times(other.get(k, j)));
				}
				values[i][j] = value;
			}
		}
		return createImmutableMatrix(values);
	}

	@Override
	public ImmutableMatrix<T> leftTimes(final Matrix<T> other) {
		checkLeftTimesDimensions(other);
		final int rows = getRows(), columns = getColumns(), rowsOther = other.getRows();
		final Object[][] values = new Object[rowsOther][columns];
		for (int i = 0; i < rowsOther; i++) {
			for (int j = 0; j < columns; j++) {
				T value = getScalarField().getZero();
				for (int k = 0; k < rows; k++) {
					value = value.plus(other.get(i, k).times(get(k, j)));
				}
				values[i][j] = value;
			}
		}
		return createImmutableMatrix(values);
	}

	@Override
	public ImmutableMatrix<T> times(final T scalar) {
		Validate.notNull(scalar, "scalar");
		final int rows = getRows(), columns = getColumns();
		final Object[][] values = new Object[rows][columns];
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				values[i][j] = get(i, j).times(scalar);
			}
		}
		return createImmutableMatrix(values);
	}

	@Override
	public ImmutableMatrix<T> dividedBy(final T scalar) {
		Validate.notNull(scalar, "scalar");
		final int rows = getRows(), columns = getColumns();
		final Object[][] values = new Object[rows][columns];
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				values[i][j] = get(i, j).dividedBy(scalar);
			}
		}
		return createImmutableMatrix(values);
	}

	@Override
	public ImmutableMatrix<T> opposite() {
		final int rows = getRows(), columns = getColumns();
		final Object[][] values = new Object[rows][columns];
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				values[i][j] = get(i, j).opposite();
			}
		}
		return createImmutableMatrix(values);
	}

	@Override
	public MutableVector<T> solve(final MutableVector<T> x, final Vector<T> b) {
		checkSolveDimensions(x, b);
		// FIXME implement
		throw new UnsupportedOperationException();
	}

	/**
	 * Return a new immutable vector with the specified array of values.
	 * 
	 * @param values
	 *            the array of values
	 * @return a new immutable vector with the specified array of values
	 */
	protected ImmutableMatrix<T> createImmutableMatrix(final Object[][] values) {
		return new ImmutableArrayMatrix<T>(getMatrixSpace(), values);
	}

}
