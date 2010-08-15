package com.sebster.util.collections;

import java.io.Serializable;

public interface Pair<T1, T2> extends Serializable {

	T1 getFirst();
	
	void setFirst(T1 first);
	
	T2 getSecond();
	
	void setSecond(T2 second);
	
}
