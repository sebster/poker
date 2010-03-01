package com.sebster.util;

public interface LinearOrder<T> extends Comparable<T> {

	T next();

	T prev();

}
