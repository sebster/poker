package com.sebster.poker.holdem.odds.generation;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;

public class HoldemThreePlayerPreflopOddsDatabaseSorter {

	public static void main(final String[] args) throws IOException {
		final DataInputStream in = new DataInputStream(new BufferedInputStream(new FileInputStream(args[0])));
		final DataOutputStream out = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(args[1])));

		// Read number of records.
		final int records = in.readInt();
		System.out.println("number of records: " + records);

		// Read data.
		System.out.println("reading data...");
		final int[] data = new int[14 * records];
		int index = 0;
		for (int i = 0; i < data.length; i++) {
			data[index++] = in.readInt();
		}
		in.close();

		// Sort data.
		System.out.println("sorting data...");
		final Integer[] dataIndex = new Integer[records];
		for (int i = 0; i < records; i++) {
			dataIndex[i] = i;
		}
		Arrays.sort(dataIndex, new Comparator<Integer>() {
			@Override
			public int compare(final Integer o1, final Integer o2) {
				final int v1 = data[o1.intValue() * 14];
				final int v2 = data[o2.intValue() * 14];
				return v1 < v2 ? -1 : v1 > v2 ? 1 : 0;
			}
		});

		// Write data.
		System.out.println("writing data...");
		out.writeInt(records);
		for (int i = 0; i < records; i++) {
			index = dataIndex[i] * 14;
			for (int j = 0; j < 14; j++) {
				out.writeInt(data[index++]);
			}
		}
		out.close();

		System.out.println("done");
	}

}
