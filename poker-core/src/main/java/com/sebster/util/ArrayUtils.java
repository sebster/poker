package com.sebster.util;

import java.util.Arrays;
import java.util.Comparator;

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

	public static void insertionSort(final int[] array) {
		for (int i = 1; i < array.length; i++) {
			// Insert array[i] into the sorted sub-array array[0..i-1].
			final int value = array[i];
			int j;
			for (j = i - 1; j >= 0 && value < array[j]; j--) {
				array[j + 1] = array[j];
			}
			array[j + 1] = value;
		}
	}

	public static <T extends Comparable<? super T>> void insertionSort(final T[] array) {
		for (int i = 1; i < array.length; i++) {
			// Insert array[i] into the sorted sub-array array[0..i-1].
			final T value = array[i];
			int j;
			for (j = i - 1; j >= 0 && value.compareTo(array[j]) < 0; j--) {
				array[j + 1] = array[j];
			}
			array[j + 1] = value;
		}
	}

	public static <T> void insertionSort(final T[] array, final Comparator<? super T> comparator) {
		for (int i = 1; i < array.length; i++) {
			// Insert array[i] into the sorted sub-array array[0..i-1].
			final T value = array[i];
			int j;
			for (j = i - 1; j >= 0 && comparator.compare(value, array[j]) < 0; j--) {
				array[j + 1] = array[j];
			}
			array[j + 1] = value;
		}
	}

	public static int[] trackedInsertionSort(final int[] array) {
		final int[] indexes = new int[array.length];
		for (int i = 1; i < array.length; i++) {
			// Insert array[i] into the sorted sub-array array[0..i-1].
			final int value = array[i];
			int j;
			for (j = i - 1; j >= 0 && value < array[j]; j--) {
				array[j + 1] = array[j];
				indexes[j + 1] = indexes[j];
			}
			array[j + 1] = value;
			indexes[j + 1] = i;
		}
		return indexes;
	}

	public static <T extends Comparable<? super T>> int[] trackedInsertionSort(final T[] array) {
		final int[] indexes = new int[array.length];
		for (int i = 1; i < array.length; i++) {
			// Insert array[i] into the sorted sub-array array[0..i-1].
			final T value = array[i];
			int j;
			for (j = i - 1; j >= 0 && value.compareTo(array[j]) < 0; j--) {
				array[j + 1] = array[j];
				indexes[j + 1] = indexes[j];
			}
			array[j + 1] = value;
			indexes[j + 1] = i;
		}
		return indexes;
	}

	public static <T> int[] trackedInsertionSort(final T[] array, final Comparator<? super T> comparator) {
		final int[] indexes = new int[array.length];
		for (int i = 1; i < array.length; i++) {
			// Insert array[i] into the sorted sub-array array[0..i-1].
			final T value = array[i];
			int j;
			for (j = i - 1; j >= 0 && comparator.compare(value, array[j]) < 0; j--) {
				array[j + 1] = array[j];
				indexes[j + 1] = indexes[j];
			}
			array[j + 1] = value;
			indexes[j + 1] = i;
		}
		return indexes;
	}

	public static int max(final int... values) {
		int result = Integer.MIN_VALUE;
		for (final int value : values) {
			if (value > result) {
				result = value;
			}
		}
		return result;
	}

	public static int count(final int[] values, final int target) {
		int result = 0;
		for (final int value : values) {
			if (value == target) {
				result++;
			}
		}
		return result;
	}

	public static int[] constantArray(final int length, final int value) {
		final int[] array = new int[length];
		Arrays.fill(array, value);
		return array;
	}

}
