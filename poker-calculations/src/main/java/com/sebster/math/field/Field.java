package com.sebster.math.field;

public interface Field<T extends FieldValue<T>> {

	public T getZero();
	
	public T getOne();
	
}
