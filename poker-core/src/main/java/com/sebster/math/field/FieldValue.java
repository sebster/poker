package com.sebster.math.field;

public interface FieldValue<T extends FieldValue<T>> {

	T add(T other);

	T subtract(T other);

	T multiply(T other);
	
	T divide(T other);
	
	T negate();

	T abs();
	
	int signum();
	
	Field<T> getField();
	
}
