package com.sebster.math.field;

public interface VectorField<T extends FieldValue<T>, V extends Vector<T, V>> {

	V getZero();
	
	V getOne();
	
	Field<T> getScalarField();
	
}
