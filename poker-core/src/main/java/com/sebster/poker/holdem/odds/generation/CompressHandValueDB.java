package com.sebster.poker.holdem.odds.generation;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sebster.io.compress.CompressLZFI;
import com.sebster.poker.holdem.odds.Constants;

public class CompressHandValueDB {

	private static final Logger logger = LoggerFactory.getLogger(CompressHandValueDB.class);

	public static final String FILENAME = "hand_value_db.lzfi.gz";

	/**
	 * Compress the hand value database using a custom int-aligned LZF
	 * algorithm. The first argument is the input filename of the gzipped hand
	 * value database, the second argument is the filename of the int-aligned
	 * LZF compressed output database. The default input filename is
	 * "hand_values.dat.gz", the default output filename is
	 * "hand_values.dat.lzfi".
	 * 
	 * @param args
	 *            the input and output filenames
	 * @throws IOException
	 *             if an I/O exception occurs
	 */
	public static void main(final String[] args) throws IOException {

		String input = GenerateHandValueDB.FILENAME;
		if (args.length > 0) {
			input = args[0];
		}

		String output = FILENAME;
		if (args.length > 1) {
			output = args[1];
		}

		final DataInputStream in = new DataInputStream(new GZIPInputStream(new FileInputStream(input)));
		final DataOutputStream out = new DataOutputStream(new GZIPOutputStream(new FileOutputStream(output)));

		final byte[] dataBytes = new byte[Constants.BOARD_COUNT_0 * 4];
		final int[] data = new int[Constants.BOARD_COUNT_0];
		final int[] dataCompressed = new int[Constants.BOARD_COUNT_0];
		final int[] dataVerify = new int[Constants.BOARD_COUNT_0];
		for (int i = 0; i < Constants.HOLE_COUNT; i++) {

			// Read data.
			in.readFully(dataBytes);
			int k = 0;
			for (int j = 0; j < Constants.BOARD_COUNT_0; j++) {
				data[j] = dataBytes[k++] & 0xff;
				data[j] = (data[j] << 8) | (dataBytes[k++] & 0xff);
				data[j] = (data[j] << 8) | (dataBytes[k++] & 0xff);
				data[j] = (data[j] << 8) | (dataBytes[k++] & 0xff);
			}

			// Compress.
			final long t1 = System.currentTimeMillis();
			final int length = CompressLZFI.compress(data, data.length, dataCompressed, 0);
			final long t2 = System.currentTimeMillis();

			// Save.
			out.writeInt(length);
			for (int j = 0; j < length; j++) {
				out.writeInt(dataCompressed[j]);
			}

			// Verify.
			CompressLZFI.expand(dataCompressed, 0, length, dataVerify, 0, Constants.BOARD_COUNT_0);
			for (int j = 0; j < Constants.BOARD_COUNT_0; j++) {
				if (data[j] != dataVerify[j]) {
					throw new IllegalStateException("error at " + j + ": " + data[j] + " != " + dataVerify[j]);
				}
			}
			logger.info("hand={} converted length={} compress time={}", new Object[] { i, length, (t2 - t1) / 1000.0 });
		}
		out.close();
		in.close();
	}

}
