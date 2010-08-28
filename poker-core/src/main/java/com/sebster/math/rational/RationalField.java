package com.sebster.math.rational;

import com.sebster.math.field.Field;

public class RationalField implements Field<Rational> {

	private static final RationalField INSTANCE = new RationalField();
	
	public static final RationalField getInstance() {
		return INSTANCE;
	}
	
	private RationalField() {
		// Singleton.
	}
	
	@Override
	public Rational getZero() {
		return Rational.ZERO;
	}

	@Override
	public Rational getOne() {
		return Rational.ONE;
	}

}
