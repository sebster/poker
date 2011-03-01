package com.sebster.math.vector;

import com.sebster.math.field.Field;
import com.sebster.math.field.FieldValue;
import com.sebster.util.Validate;

public abstract class AbstractVector<T extends FieldValue<T>> implements Vector<T> {

	private final VectorSpace<T> vectorSpace;

	protected AbstractVector(final VectorSpace<T> vectorSpace) {
		Validate.notNull(vectorSpace, "vectorSpace");
		this.vectorSpace = vectorSpace;
	}

	@Override
	public int getDimension() {
		return vectorSpace.getDimension();
	}

	@Override
	public Field<T> getScalarField() {
		return vectorSpace.getScalarField();
	}

	@Override
	public VectorSpace<T> getVectorSpace() {
		return vectorSpace;
	}

	@Override
	public VectorSpace<T> getVectorSpace(final int dimension) {
		return vectorSpace.getVectorSpace(dimension);
	}

	@Override
	public T dot(final Vector<T> other) {
		checkDimension(other);
		final int dimension = getDimension();
		T result = getScalarField().getZero();
		for (int i = 0; i < dimension; i++) {
			result = result.plus(get(i).times(other.get(i)));
		}
		return result;
	}

	protected void checkDimension(final Vector<T> other) {
		if (getDimension() != other.getDimension()) {
			throw new IllegalArgumentException("invalid dimension: " + other.getDimension() + "; expected " + getDimension());
		}
	}

	@Override
	public int hashCode() {
		final int dimension = getDimension();
		int result = vectorSpace.hashCode();
		for (int i = 0; i < dimension; i++) {
			final int elementHash = get(i).hashCode();
			result = 31 * result + elementHash;
		}
		return result;
	}

	@Override
	public boolean equals(final Object object) {
		if (this == object) {
			return true;
		}
		if (object instanceof Vector) {
			final Vector<?> other = (Vector<?>) object;
			if (!getVectorSpace().equals(other.getVectorSpace())) {
				return false;
			}
			final int dimension = getDimension();
			for (int i = 0; i < dimension; i++) {
				if (!get(i).equals(other.get(i))) {
					return false;
				}
			}
			return true;
		}
		return false;
	}

	@Override
	public String toString() {
		final StringBuilder buffer = new StringBuilder();
		final int dimension = getDimension();
		buffer.append('<');
		if (dimension > 0) {
			buffer.append(get(0).toString());
			for (int i = 1; i < dimension; i++) {
				buffer.append(", ");
				buffer.append(get(i).toString());
			}
		}
		buffer.append('>');
		return buffer.toString();
	}

}
