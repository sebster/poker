package com.sebster.math.matrix;

import net.jcip.annotations.Immutable;

import com.sebster.math.field.FieldValue;
import com.sebster.math.vector.ImmutableVector;
import com.sebster.math.vector.MutableVector;
import com.sebster.math.vector.Vector;
import com.sebster.util.Validate;

@Immutable
public class ScalarMatrix<T extends FieldValue<T>> extends AbstractMatrix<T> implements ImmutableMatrix<T> {

	private final T scalar;

	public ScalarMatrix(final MatrixSpace<T> matrixSpace, final T scalar) {
		super(matrixSpace);
		Validate.isTrue(matrixSpace.isSquare(), "scalar matrix must be square");
		Validate.notNull(scalar, "scalar");
		this.scalar = scalar;
	}

	@Override
	public T get(final int i, final int j) {
		return i == j ? scalar : getScalarField().getZero();
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
		return other.times(scalar);
	}

	@Override
	public ImmutableMatrix<T> leftTimes(final Matrix<T> other) {
		checkLeftTimesDimensions(other);
		return other.times(scalar);
	}

	@Override
	public ImmutableVector<T> times(final Vector<T> vector) {
		checkTimesDimensions(vector);
		return vector.times(scalar);
	}

	@Override
	public ScalarMatrix<T> times(final T scalar) {
		return new ScalarMatrix<T>(getMatrixSpace(), this.scalar.times(scalar));
	}

	@Override
	public ScalarMatrix<T> dividedBy(final T scalar) {
		return new ScalarMatrix<T>(getMatrixSpace(), this.scalar.dividedBy(scalar));
	}

	@Override
	public ScalarMatrix<T> opposite() {
		return new ScalarMatrix<T>(getMatrixSpace(), scalar.opposite());
	}

	@Override
	public MutableVector<T> solve(final MutableVector<T> x, final Vector<T> b) {
		checkSolveDimensions(x, b);
		final int dimension = x.getDimension();
		for (int i = 0; i < dimension; i++) {
			x.set(i, b.get(i).dividedBy(scalar));
		}
		return x;
	}

}
