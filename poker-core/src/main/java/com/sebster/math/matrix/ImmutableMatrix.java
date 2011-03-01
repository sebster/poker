package com.sebster.math.matrix;

import net.jcip.annotations.Immutable;

import com.sebster.math.field.FieldValue;

@Immutable
public interface ImmutableMatrix<T extends FieldValue<T>> extends Matrix<T> {

	// Empty tagging interface.
	
}
