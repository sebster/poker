package com.sebster.math.matrix;

import com.sebster.math.field.Field;
import com.sebster.math.field.FieldValue;
import com.sebster.math.vector.ImmutableVector;
import com.sebster.math.vector.MutableVector;
import com.sebster.math.vector.Vector;

public interface Matrix<T extends FieldValue<T>> {

	int getRows();

	int getColumns();
	
	boolean isSquare();

	T get(int i, int j);

	ImmutableMatrix<T> plus(Matrix<T> other);

	ImmutableMatrix<T> minus(Matrix<T> other);

	ImmutableMatrix<T> times(Matrix<T> other);

	ImmutableMatrix<T> leftTimes(Matrix<T> other);

	ImmutableVector<T> times(Vector<T> vector);

	ImmutableMatrix<T> times(T scalar);

	ImmutableMatrix<T> dividedBy(T scalar);

	ImmutableMatrix<T> opposite();

	ImmutableVector<T> solve(Vector<T> b);
	
	MutableVector<T> solve(MutableVector<T> x, Vector<T> b);

	Field<T> getScalarField();

	MatrixSpace<T> getMatrixSpace();

}
