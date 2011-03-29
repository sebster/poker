package com.sebster.poker.holdem.odds.generation;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;

public class Convert {

	public static void main(final String[] args) throws IOException {
		final DataInputStream in = new DataInputStream(new BufferedInputStream(new GZIPInputStream(new FileInputStream(args[0]))));
		final DataOutputStream out = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(args[1])));
		// Skip number of players.
		in.readByte();
		// Read number of records.
		final int records = in.readInt();
		System.out.println("number of records: " + records);

		// Read data.
		System.out.println("reading data...");
		final int[] data = new int[10 * records];
		int index = 0;
		for (int i = 0; i < records; i++) {
			long longIndex = 0;
			for (int j = 0; j < 3; j++) {
				final int holeIndex = in.readShort();
				longIndex = longIndex * 1326 + holeIndex;
			}
			data[index++] = Integer.MIN_VALUE + (int) longIndex;
			for (int j = 0; j < 3 * 3; j++) {
				data[index++] = in.readInt();
			}
		}
		in.close();

		// Sort data.
		System.out.println("sorting data...");
		final int[] dataIndex = new int[records];
		for (int i = 0; i < records; i++) {
			dataIndex[i] = i;
		}
		sort1(data, dataIndex, 0, dataIndex.length);

		// Write data.
		System.out.println("writing data...");
		out.writeInt(records);
		for (int i = 0; i < records; i++) {
			index = dataIndex[i] * 10;
			for (int j = 0; j < 10; j++) {
				out.writeInt(data[index++]);
			}
		}
		out.close();

		System.out.println("done");
	}

	private static int compare(final int[] data, final int x, final int y) {
		final int i = data[x * 10], j = data[y * 10];
		return i < j ? -1 : i > j ? 1 : 0;
	}

	private static void sort1(final int[] data, final int x[], final int off, final int len) {
		// Insertion sort on smallest arrays
		if (len < 7) {
			for (int i = off; i < len + off; i++) {
				for (int j = i; j > off && compare(data, x[j - 1], x[j]) > 0; j--) {
					swap(x, j, j - 1);
				}
			}
			return;
		}

		// Choose a partition element, v
		int m = off + (len >> 1); // Small arrays, middle element
		if (len > 7) {
			int l = off;
			int n = off + len - 1;
			if (len > 40) { // Big arrays, pseudomedian of 9
				final int s = len / 8;
				l = med3(data, x, l, l + s, l + 2 * s);
				m = med3(data, x, m - s, m, m + s);
				n = med3(data, x, n - 2 * s, n - s, n);
			}
			m = med3(data, x, l, m, n); // Mid-size, med of 3
		}
		final int v = x[m];

		// Establish Invariant: v* (<v)* (>v)* v*
		int a = off, b = a, c = off + len - 1, d = c;
		while (true) {
			while (b <= c && compare(data, x[b], v) <= 0) {
				if (compare(data, x[b], v) == 0) {
					swap(x, a++, b);
				}
				b++;
			}
			while (c >= b && compare(data, x[c], v) >= 0) {
				if (compare(data, x[c], v) == 0) {
					swap(x, c, d--);
				}
				c--;
			}
			if (b > c) {
				break;
			}
			swap(x, b++, c--);
		}

		// Swap partition elements back to middle
		int s;
		final int n = off + len;
		s = Math.min(a - off, b - a);
		vecswap(x, off, b - s, s);
		s = Math.min(d - c, n - d - 1);
		vecswap(x, b, n - s, s);

		// Recursively sort non-partition-elements
		if ((s = b - a) > 1) {
			sort1(data, x, off, s);
		}
		if ((s = d - c) > 1) {
			sort1(data, x, n - s, s);
		}
	}

	private static int med3(final int[] data, final int x[], final int a, final int b, final int c) {
		return compare(data, x[a], x[b]) < 0 ?
				(compare(data, x[b], x[c]) < 0 ? b : compare(data, x[a], x[c]) < 0 ? c : a) :
				compare(data, x[b], x[c]) > 0 ? b : compare(data, x[a], x[c]) > 0 ? c : a;
	}

	private static void swap(final int x[], final int a, final int b) {
		final int t = x[a];
		x[a] = x[b];
		x[b] = t;
	}

	private static void vecswap(final int x[], int a, int b, final int n) {
		for (int i = 0; i < n; i++, a++, b++) {
			swap(x, a, b);
		}
	}

}
