package com.sebster.math.rational;

import com.sebster.math.vector.VectorSpace;
import com.sebster.util.Assert;

public class PerturbedRationalVectorSpace implements VectorSpace<Rational, PerturbedRational> {

	private final int dimension;
	
	public static final PerturbedRationalVectorSpace getInstance(final int dimension) {
		return new PerturbedRationalVectorSpace(dimension);
	}
	
	private PerturbedRationalVectorSpace(final int dimension) {
		Assert.isTrue(dimension > 0, "dimension must be positive");
		this.dimension = dimension;
	}
	
	@Override
	public PerturbedRational getZero() {
		return PerturbedRational.zero(dimension);
	}

	@Override
	public PerturbedRational getOne() {
		return PerturbedRational.one(dimension);
	}
	
	public int getDimension() {
		return dimension;
	}

	@Override
	public RationalField getScalarField() {
		return RationalField.INSTANCE;
	}

}
