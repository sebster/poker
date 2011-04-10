package com.sebster.util;

/**
 * An element of a linear order.
 * 
 * @author Sebastiaan van Erk
 * 
 * @param <T>
 *            the element type
 */
public interface LinearOrderElement<T> extends Comparable<T> {

	/**
	 * Return the next element in the order, or {@code null} if this is the last
	 * element.
	 * 
	 * @return the next element in the order, or {@code null} if this is the
	 *         last element
	 */
	T next();

	/**
	 * Return the previous element in the order, or {@code null} if this is the
	 * first element
	 * 
	 * @return the previous element in the order, or {@code null} if this is the
	 *         previous element
	 */
	T prev();

}
