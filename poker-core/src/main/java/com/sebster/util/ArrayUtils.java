package com.sebster.util;

public class ArrayUtils {

	public static <T> void swap(final T[] array, final int i, final int j) {
		final T value = array[i];
		array[i] = array[j];
		array[j] = value;
	}

	public static <T> void swap(final byte[] array, final int i, final int j) {
		final byte value = array[i];
		array[i] = array[j];
		array[j] = value;
	}

	public static <T> void swap(final short[] array, final int i, final int j) {
		final short value = array[i];
		array[i] = array[j];
		array[j] = value;
	}

	public static <T> void swap(final int[] array, final int i, final int j) {
		final int value = array[i];
		array[i] = array[j];
		array[j] = value;
	}

	public static <T> void swap(final long[] array, final int i, final int j) {
		final long value = array[i];
		array[i] = array[j];
		array[j] = value;
	}

	public static <T> void swap(final float[] array, final int i, final int j) {
		final float value = array[i];
		array[i] = array[j];
		array[j] = value;
	}

	public static <T> void swap(final double[] array, final int i, final int j) {
		final double value = array[i];
		array[i] = array[j];
		array[j] = value;
	}

	public static <T> void swap(final boolean[] array, final int i, final int j) {
		final boolean value = array[i];
		array[i] = array[j];
		array[j] = value;
	}

	public static <T> void swap(final char[] array, final int i, final int j) {
		final char value = array[i];
		array[i] = array[j];
		array[j] = value;
	}

}
