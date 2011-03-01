package com.sebster.math.vector;

import net.jcip.annotations.Immutable;

import com.sebster.math.field.Field;
import com.sebster.math.field.FieldValue;
import com.sebster.util.Validate;

/**
 * A vector space over a specified field.
 * 
 * @author sebster
 * 
 * @param <T>
 *            the field value type
 */
@Immutable
public final class VectorSpace<T extends FieldValue<T>> {

	private final Field<T> field;

	private final int dimension;

	private final ZeroVector<T> zero;

	public VectorSpace(final Field<T> field, final int dimension) {
		Validate.notNull(field, "field");
		Validate.isTrue(dimension >= 0, "dimension < 0");
		this.field = field;
		this.dimension = dimension;
		this.zero = new ZeroVector<T>(this);
	}

	/**
	 * Return the dimension of this vector space. The dimension is a number
	 * greater than or equal to zero.
	 * 
	 * @return the dimension of this vector space
	 */
	public int getDimension() {
		return dimension;
	}

	/**
	 * Return the additive identity of this vector space. This is the zero
	 * vector. An immutable representation is returned.
	 * 
	 * @return the zero vector of this vector space
	 */
	public ZeroVector<T> getZero() {
		return zero;
	}

	/**
	 * Return the scalar field of this vector space.
	 * 
	 * @return the scalar field of this vector space
	 */
	public Field<T> getScalarField() {
		return field;
	}

	/**
	 * Return a vector field of the same scalar field with the specified
	 * dimension.
	 * 
	 * @param dimension
	 *            the dimension
	 * 
	 * @return a vector field of the same scalar field with the specified
	 *         dimension
	 */
	public VectorSpace<T> getVectorSpace(final int dimension) {
		return dimension == this.dimension ? this : new VectorSpace<T>(field, dimension);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + dimension;
		result = prime * result + field.hashCode();
		return result;
	}

	@Override
	public boolean equals(final Object object) {
		if (this == object)
			return true;
		if (object instanceof VectorSpace) {
			final VectorSpace<?> other = (VectorSpace<?>) object;
			return dimension == other.dimension && field.equals(other.field);
		}
		return false;
	}

}
