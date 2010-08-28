package com.sebster.math.vector;

import com.sebster.math.field.Field;
import com.sebster.math.field.FieldValue;

public interface VectorSpace<T extends FieldValue<T>, V extends Vector<T, V>> {

	V getZero();
	
	V getOne();
	
	Field<T> getScalarField();
	
}
