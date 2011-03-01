package com.sebster.math.field;

public interface FieldValue<T extends FieldValue<T>> {

	T plus(T other);

	T minus(T other);

	T times(T other);
	
	T dividedBy(T other);
	
	T opposite();

	T reciprocal();
	
	T abs();
	
	int signum();
	
	Field<T> getField();
	
}
