package com.sebster.math.field;

import net.jcip.annotations.ThreadSafe;

@ThreadSafe
public interface Field<T extends FieldValue<T>> {

	public T getZero();
	
	public T getOne();
	
}
