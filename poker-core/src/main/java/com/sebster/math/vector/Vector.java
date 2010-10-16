package com.sebster.math.vector;

import com.sebster.math.field.Field;
import com.sebster.math.field.FieldValue;

public interface Vector<T extends FieldValue<T>, V extends Vector<T, V>> {

	int size();
	
	V add(V other);

	V subtract(V other);

	V multiply(T scalar);
	
	V divide(T scalar);
	
	V negate();
	
	Field<T> getScalarField();
	
	VectorSpace<T, V> getVectorField();
	
}
