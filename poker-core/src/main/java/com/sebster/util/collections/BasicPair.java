package com.sebster.util.collections;

public class BasicPair<T1, T2> extends AbstractPair<T1, T2> {
	
	private static final long serialVersionUID = 1L;

	private T1 first;
	
	private T2 second;
	
	public BasicPair() {
		this(null, null);
	}
	
	public BasicPair(final T1 firstElement, final T2 secondElement) {
		this.first = firstElement;
		this.second = secondElement;
	}
	
	@Override
	public T1 getFirst() {
		return first;
	}
	
	@Override
	public void setFirst(final T1 firstElement) {
		this.first = firstElement;
	}
	
	@Override
	public T2 getSecond() {
		return second;
	}
	
	@Override
	public void setSecond(final T2 secondElement) {
		this.second = secondElement;
	}

}
