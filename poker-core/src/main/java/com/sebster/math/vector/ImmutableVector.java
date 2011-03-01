package com.sebster.math.vector;

import net.jcip.annotations.Immutable;

import com.sebster.math.field.FieldValue;

@Immutable
public interface ImmutableVector<T extends FieldValue<T>> extends Vector<T> {

	// Empty tagging interface.
	
}
