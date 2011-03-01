package com.sebster.math.vector;

import net.jcip.annotations.Immutable;

import com.sebster.math.field.FieldValue;
import com.sebster.util.Validate;

/**
 * An immutable zero vector which does only has a reference to the vector space
 * and consequently uses almost no memory. It also has optimized versions of the
 * vector operations for the special case of the zero vector.
 * 
 * @author sebster
 * 
 * @param <T>
 *            the field value type
 */
@Immutable
public final class ZeroVector<T extends FieldValue<T>> extends AbstractVector<T> implements ImmutableVector<T> {

	/**
	 * Create a new zero vector for the specified vector space.
	 * 
	 * @param vectorSpace
	 *            the vector space
	 * 
	 * @throws NullPointerException
	 *             if <code>vectorSpace</code> is <code>null</code>
	 */
	public ZeroVector(final VectorSpace<T> vectorSpace) {
		super(vectorSpace);
	}

	@Override
	public T get(final int i) {
		if (i < 0 || i >= getDimension()) {
			throw new IndexOutOfBoundsException(String.valueOf(i));
		}
		return getScalarField().getZero();
	}

	@Override
	public ImmutableVector<T> plus(final Vector<T> other) {
		checkDimension(other);
		if (other instanceof ImmutableVector) {
			// Optimization to avoid unnecessary cloning of immutable vector.
		return (ImmutableVector<T>) other;
		}
		return new ImmutableArrayVector<T>(other);
	}

	@Override
	public ImmutableVector<T> minus(final Vector<T> other) {
		checkDimension(other);
		return other.opposite();
	}

	@Override
	public T dot(final Vector<T> other) {
		checkDimension(other);
		return getScalarField().getZero();
	}

	@Override
	public ZeroVector<T> times(final T scalar) {
		Validate.notNull(scalar, "scalar");
		return this;
	}

	@Override
	public ZeroVector<T> dividedBy(final T scalar) {
		if (scalar.signum() == 0) {
			throw new ArithmeticException("division by zero");
		}
		return this;
	}

	@Override
	public ZeroVector<T> opposite() {
		return this;
	}

	@Override
	public int hashCode() {
		return 31 + getVectorSpace().hashCode();
	}

	@Override
	public boolean equals(final Object object) {
		if (this == object) {
			return true;
		}
		if (object instanceof ZeroVector) {
			// Optimization if the other is also a zero vector.
			final ZeroVector<?> other = (ZeroVector<?>) object;
			return getVectorSpace().equals(other.getVectorSpace());
		}
		return super.equals(object);
	}

}
