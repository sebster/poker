package com.sebster.math.matrix;

import net.jcip.annotations.Immutable;

import com.sebster.math.field.FieldValue;
import com.sebster.math.vector.ImmutableArrayVector;
import com.sebster.math.vector.ImmutableVector;
import com.sebster.math.vector.MutableVector;
import com.sebster.math.vector.Vector;
import com.sebster.util.Validate;

@Immutable
public final class IdentityMatrix<T extends FieldValue<T>> extends AbstractMatrix<T> implements ImmutableMatrix<T> {

	public IdentityMatrix(final MatrixSpace<T> matrixSpace) {
		super(matrixSpace);
		Validate.isTrue(matrixSpace.isSquare(), "identity matrix must be square");
	}

	@Override
	public T get(final int i, final int j) {
		return i == j ? getScalarField().getOne() : getScalarField().getZero();
	}

	@Override
	public ImmutableMatrix<T> plus(final Matrix<T> other) {
		// FIXME
		// return new ArrayMatrix<T>(this).add(other);
		return null;
	}

	@Override
	public ImmutableMatrix<T> minus(final Matrix<T> other) {
		return new ArrayMatrix<T>(this).minus(other);
	}

	@Override
	public ImmutableMatrix<T> times(final Matrix<T> other) {
		checkTimesDimensions(other);
		if (other instanceof ImmutableMatrix) {
			return (ImmutableMatrix<T>) other;
		}
		return new ImmutableArrayMatrix<T>(other);
	}

	@Override
	public ImmutableMatrix<T> leftTimes(final Matrix<T> other) {
		checkLeftTimesDimensions(other);
		if (other instanceof ImmutableMatrix) {
			return (ImmutableMatrix<T>) other;
		}
		return new ImmutableArrayMatrix<T>(other);
	}

	@Override
	public ImmutableVector<T> times(final Vector<T> vector) {
		checkTimesDimensions(vector);
		if (vector instanceof ImmutableVector) {
			return (ImmutableVector<T>) vector;
		}
		return new ImmutableArrayVector<T>(vector);
	}

	@Override
	public ScalarMatrix<T> times(final T scalar) {
		return new ScalarMatrix<T>(getMatrixSpace(), scalar);
	}

	@Override
	public ScalarMatrix<T> dividedBy(final T scalar) {
		return new ScalarMatrix<T>(getMatrixSpace(), scalar.reciprocal());
	}

	@Override
	public ScalarMatrix<T> opposite() {
		return new ScalarMatrix<T>(getMatrixSpace(), getScalarField().getOne().opposite());
	}

	@Override
	public MutableVector<T> solve(final MutableVector<T> x, final Vector<T> b) {
		checkSolveDimensions(x, b);
		return x.setFrom(b);
	}

}
