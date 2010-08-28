package com.sebster.math.field;

public interface FieldValue<T extends FieldValue<T>> {

	T add(T other);

	T subtract(T other);

	int signum();

	T negate();
	
	T abs();
	
	T multiply(T other);
	
	T divide(T other);
	
	Field<T> getField();
	
}
