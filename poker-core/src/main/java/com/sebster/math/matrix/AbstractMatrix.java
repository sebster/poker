package com.sebster.math.matrix;

import com.sebster.math.field.Field;
import com.sebster.math.field.FieldValue;
import com.sebster.math.vector.ArrayVector;
import com.sebster.math.vector.ImmutableArrayVector;
import com.sebster.math.vector.ImmutableVector;
import com.sebster.math.vector.Vector;
import com.sebster.util.Validate;

public abstract class AbstractMatrix<T extends FieldValue<T>> implements Matrix<T> {

	private final MatrixSpace<T> matrixSpace;

	protected AbstractMatrix(final MatrixSpace<T> MatrixSpace) {
		Validate.notNull(MatrixSpace, "matrixSpace");
		this.matrixSpace = MatrixSpace;
	}

	@Override
	public int getRows() {
		return matrixSpace.getRows();
	}

	@Override
	public int getColumns() {
		return matrixSpace.getColumns();
	}
	
	@Override
	public boolean isSquare() {
		return matrixSpace.isSquare();
	}

	@Override
	public Field<T> getScalarField() {
		return matrixSpace.getScalarField();
	}

	@Override
	public MatrixSpace<T> getMatrixSpace() {
		return matrixSpace;
	}

	@Override
	public ImmutableVector<T> solve(final Vector<T> b) {
		// FIXME can I get rid of the extra array copy?
		return new ImmutableArrayVector<T>(solve(new ArrayVector<T>(b.getVectorSpace(getColumns())), b));
	}
	
	@Override
	public int hashCode() {
		final int rows = getRows(), columns = getColumns();
		int result = 1;
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				final int elementHash = get(i, j).hashCode();
				result = 31 * result + elementHash;
			}
		}
		return result;
	}

	@Override
	public boolean equals(final Object object) {
		if (this == object) {
			return true;
		}
		if (object instanceof Matrix) {
			final Matrix<?> other = (Matrix<?>) object;
			if (!getMatrixSpace().equals(other.getMatrixSpace())) {
				return false;
			}
			final int rows = getRows(), columns = getColumns();
			for (int i = 0; i < rows; i++) {
				for (int j = 0; j < columns; j++) {
					if (!get(i, j).equals(other.get(i, j))) {
						return false;
					}
				}
			}
			return true;
		}
		return false;
	}

	protected void checkDimensions(final Matrix<T> other) {
		checkDimensions(other, getRows(), getColumns());
	}

	protected void checkDimensions(final Matrix<T> other, final int rows, final int columns) {
		if (other.getRows() != rows) {
			throw new IllegalArgumentException("invalid row dimension: " + other.getRows() + "; expected " + rows);
		}
		if (other.getColumns() != columns) {
			throw new IllegalArgumentException("invalid column dimension: " + other.getColumns() + "; expected " + columns);
		}
	}

	protected void checkTimesDimensions(final Matrix<T> other) {
		if (getColumns() != other.getRows()) {
			throw new IllegalArgumentException("invalid row dimension: " + other.getRows() + "; expected " + getColumns());
		}
	}

	protected void checkLeftTimesDimensions(final Matrix<T> other) {
		if (other.getColumns() != getRows()) {
			throw new IllegalArgumentException("invalid column dimension: " + other.getColumns() + "; expected " + getRows());
		}
	}
	
	protected void checkTimesDimensions(final Vector<T> vector) {
		if (getColumns() != vector.getDimension()) {
			throw new IllegalArgumentException("invalid vector dimension: " + vector.getDimension() + "; expected " + getColumns());
		}
	}
	
	protected void checkSolveDimensions(final Vector<T> x, final Vector<T> b) {
		if (getColumns() != x.getDimension()) {
			throw new IllegalArgumentException("invalid x vector dimension: " + x.getDimension() + "; expected " + getColumns());
		}
		if (getRows() != b.getDimension()) {
			throw new IllegalArgumentException("invalid b vector dimension: " + b.getDimension() + "; expected " + getRows());
		}
	}

}
