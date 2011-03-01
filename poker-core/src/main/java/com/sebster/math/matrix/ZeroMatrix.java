package com.sebster.math.matrix;

import net.jcip.annotations.Immutable;

import com.sebster.math.field.FieldValue;
import com.sebster.math.vector.MutableVector;
import com.sebster.math.vector.Vector;
import com.sebster.math.vector.ZeroVector;
import com.sebster.util.Validate;

@Immutable
public class ZeroMatrix<T extends FieldValue<T>> extends AbstractMatrix<T> implements ImmutableMatrix<T> {

	public ZeroMatrix(final MatrixSpace<T> matrixSpace) {
		super(matrixSpace);
	}

	@Override
	public T get(final int i, final int j) {
		if (i < 0 || i >= getRows()) {
			throw new IndexOutOfBoundsException(String.valueOf(i));
		}
		if (j < 0 || j >= getColumns()) {
			throw new IndexOutOfBoundsException(String.valueOf(j));
		}
		return getScalarField().getZero();
	}

	@Override
	public ImmutableMatrix<T> plus(final Matrix<T> other) {
		checkDimensions(other);
		if (other instanceof ImmutableMatrix) {
			// Optimization to avoid unnecessary cloning of immutable matrix.
			return (ImmutableMatrix<T>) other;
		}
		return new ImmutableArrayMatrix<T>(other);
	}

	@Override
	public ImmutableMatrix<T> minus(final Matrix<T> other) {
		return other.opposite();
	}

	@Override
	public ZeroMatrix<T> times(final Matrix<T> other) {
		checkTimesDimensions(other);
		return new ZeroMatrix<T>(getMatrixSpace().getMatrixSpace(getRows(), other.getColumns()));
	}

	@Override
	public ZeroMatrix<T> leftTimes(final Matrix<T> other) {
		checkLeftTimesDimensions(other);
		return new ZeroMatrix<T>(getMatrixSpace().getMatrixSpace(other.getRows(), getColumns()));
	}

	@Override
	public ZeroVector<T> times(final Vector<T> vector) {
		checkTimesDimensions(vector);
		return vector.getVectorSpace(getRows()).getZero();
	}

	@Override
	public ZeroMatrix<T> times(final T scalar) {
		Validate.notNull(scalar, "scalar");
		return this;
	}

	@Override
	public ZeroMatrix<T> dividedBy(final T scalar) {
		if (scalar.signum() == 0) {
			throw new ArithmeticException("division by zero");
		}
		return this;
	}

	@Override
	public ZeroMatrix<T> opposite() {
		return this;
	}

	@Override
	public MutableVector<T> solve(final MutableVector<T> x, final Vector<T> b) {
		checkSolveDimensions(x, b);
		if (!b.equals(b.getVectorSpace().getZero())) {
			throw new ArithmeticException("no solutions");
		}
		return x;
	}

	@Override
	public int hashCode() {
		return 31 + getMatrixSpace().hashCode();
	}

	@Override
	public boolean equals(final Object object) {
		if (this == object) {
			return true;
		}
		if (object instanceof ZeroMatrix) {
			final ZeroMatrix<?> other = (ZeroMatrix<?>) object;
			return getMatrixSpace().equals(other.getMatrixSpace());
		}
		return super.equals(object);
	}

}
