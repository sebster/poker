package com.sebster.util.collections;

public class ImmutablePair<T1, T2> extends AbstractPair<T1, T2> {

	private final T1 first;
	
	private final T2 second;
	
	public ImmutablePair(final T1 first, final T2 second) {
		this.first = first;
		this.second = second;
	}
	
	@Override
	public T1 getFirst() {
		return first;
	}

	@Override
	public void setFirst(final T1 first) {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public T2 getSecond() {
		return second;
	}

	@Override
	public void setSecond(final T2 second) {
		throw new UnsupportedOperationException();
	}

}
