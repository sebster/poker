package com.sebster.math.matrix;

import com.sebster.math.field.FieldValue;

public interface MutableMatrix<T extends FieldValue<T>> extends Matrix<T> {

	MutableMatrix<T> set(int i, int j, T value);
	
	MutableMatrix<T> fill(T value);

	MutableMatrix<T> add(Matrix<T> other);

	MutableMatrix<T> subtract(Matrix<T> other);

	MutableMatrix<T> multiplyBy(Matrix<T> other);
	
	MutableMatrix<T> leftMultiplyBy(Matrix<T> other);
	
	MutableMatrix<T> multiplyBy(T scalar);
	
	MutableMatrix<T> divideBy(T scalar);
	
	MutableMatrix<T> negate();

}
