package com.sebster.math.field;

public interface Vector<T extends FieldValue<T>, V extends Vector<T, V>> {

	int size();
	
	V add(V other);

	V subtract(V other);

	V negate();
	
	V multiply(T scalar);
	
	V divide(T scalar);
	
	Field<T> getScalarField();
	
	VectorField<T, V> getVectorField();
	
}
