package com.sebster.math.matrix;

import com.sebster.math.field.Field;
import com.sebster.math.field.FieldValue;
import com.sebster.util.Validate;

public class MatrixSpace<T extends FieldValue<T>> {

	private final Field<T> field;

	private final int rows;

	private final int columns;

	private final ZeroMatrix<T> zero;

	private final IdentityMatrix<T> one;

	public MatrixSpace(final Field<T> field, final int rows, final int columns) {
		Validate.notNull(field, "field");
		Validate.isTrue(rows >= 0, "rows < 0");
		Validate.isTrue(columns >= 0, "columns < 0");
		this.field = field;
		this.rows = rows;
		this.columns = columns;
		this.zero = new ZeroMatrix<T>(this);
		this.one = new IdentityMatrix<T>(this);
	}

	public int getRows() {
		return rows;
	}

	public int getColumns() {
		return columns;
	}
	
	public boolean isSquare() {
		return rows == columns;
	}

	public ImmutableMatrix<T> getZero() {
		return zero;
	}

	public ImmutableMatrix<T> getOne() {
		return one;
	}

	public Field<T> getScalarField() {
		return field;
	}

	public MatrixSpace<T> getMatrixSpace(final int rows, final int columns) {
		return rows == this.rows && columns == this.columns ? this : new MatrixSpace<T>(field, rows, columns);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + rows;
		result = prime * result + columns;
		result = prime * result + field.hashCode();
		return result;
	}

	@Override
	public boolean equals(final Object object) {
		if (this == object)
			return true;
		if (object instanceof MatrixSpace) {
			final MatrixSpace<?> other = (MatrixSpace<?>) object;
			return rows == other.rows && columns == other.columns && field.equals(other.field);
		}
		return false;
	}


}
