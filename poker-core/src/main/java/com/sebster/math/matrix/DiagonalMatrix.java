package com.sebster.math.matrix;

import net.jcip.annotations.Immutable;

import com.sebster.math.field.FieldValue;
import com.sebster.math.vector.ImmutableVector;
import com.sebster.math.vector.MutableVector;
import com.sebster.math.vector.Vector;
import com.sebster.util.Validate;

@Immutable
public class DiagonalMatrix<T extends FieldValue<T>> extends AbstractMatrix<T> implements ImmutableMatrix<T> {

	private final ImmutableVector<T> diagonal;

	public DiagonalMatrix(final MatrixSpace<T> matrixSpace, final ImmutableVector<T> diagonal) {
		super(matrixSpace);
		Validate.isTrue(matrixSpace.isSquare(), "diagonal matrix must be square");
		Validate.isTrue(matrixSpace.getRows() == diagonal.getDimension(), "diagonal dimension does not match matrix space dimension");
		this.diagonal = diagonal;
	}

	@Override
	public T get(final int i, final int j) {
		return i == j ? diagonal.get(i) : getScalarField().getZero();
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
		// FIXME
		// return other.times(scalar);
		return null;
	}

	@Override
	public ImmutableMatrix<T> leftTimes(final Matrix<T> other) {
		checkLeftTimesDimensions(other);
		// FIXME
		// return other.times(scalar);
		return null;
	}

	@Override
	public ImmutableVector<T> times(final Vector<T> vector) {
		checkTimesDimensions(vector);
		// FIXME
		// return vector.dot(scalar);
		return null;
	}

	@Override
	public DiagonalMatrix<T> times(final T scalar) {
		return new DiagonalMatrix<T>(getMatrixSpace(), diagonal.times(scalar));
	}

	@Override
	public DiagonalMatrix<T> dividedBy(final T scalar) {
		return new DiagonalMatrix<T>(getMatrixSpace(), diagonal.dividedBy(scalar));
	}

	@Override
	public DiagonalMatrix<T> opposite() {
		return new DiagonalMatrix<T>(getMatrixSpace(), diagonal.opposite());
	}

	@Override
	public MutableVector<T> solve(final MutableVector<T> x, final Vector<T> b) {
		checkSolveDimensions(x, b);
		final int dimension = x.getDimension();
		for (int i = 0; i < dimension; i++) {
			x.set(i, b.get(i).dividedBy(diagonal.get(i)));
		}
		return x;
	}

}
