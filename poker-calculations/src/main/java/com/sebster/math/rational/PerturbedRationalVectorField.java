package com.sebster.math.rational;

import com.sebster.math.field.Field;
import com.sebster.math.field.VectorField;
import com.sebster.util.Assert;

public class PerturbedRationalVectorField implements Field<PerturbedRational>, VectorField<Rational, PerturbedRational> {

	private final int size;
	
	public static final PerturbedRationalVectorField getInstance(final int size) {
		return new PerturbedRationalVectorField(size);
	}
	
	private PerturbedRationalVectorField(final int size) {
		Assert.isTrue(size > 0, "size must be positive");
		this.size = size;
	}
	
	@Override
	public PerturbedRational getZero() {
		return PerturbedRational.zero(size);
	}

	@Override
	public PerturbedRational getOne() {
		return PerturbedRational.one(size);
	}
	
	public int getSize() {
		return size;
	}

	@Override
	public Field<Rational> getScalarField() {
		return RationalField.getInstance();
	}

}
