package com.sebster.util;

public interface LinearOrder<T> extends Comparable<T> {

	/**
	 * Return the next element in the order.
	 * 
	 * @return the next element in the order
	 */
	T next();

	/**
	 * Return the previous element in the order.
	 * 
	 * @return the previous element in the order
	 */
	T prev();

}
