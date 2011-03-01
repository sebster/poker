package com.sebster.math.field;

public interface FieldValue<T extends FieldValue<T>> {

	T plus(T other);

	T minus(T other);

	int signum();

	T opposite();
	
	T abs();
	
	T times(T other);
	
	T dividedBy(T other);
	
	Field<T> getField();
	
}
