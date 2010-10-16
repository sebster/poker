package com.sebster.math.rational;

import com.sebster.math.field.Field;

public enum RationalField implements Field<Rational> {

	INSTANCE;
	
	@Override
	public Rational getZero() {
		return Rational.ZERO;
	}

	@Override
	public Rational getOne() {
		return Rational.ONE;
	}

}
