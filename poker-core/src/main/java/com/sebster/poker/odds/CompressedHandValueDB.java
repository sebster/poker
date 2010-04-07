package com.sebster.poker.odds;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sebster.io.compress.CompressLZFI;

/**
 * In memory compressed hand value database, compressed using a custom
 * int-aligned LZF.
 * 
 * @author sebster
 * 
 */
public class CompressedHandValueDB {

	private static final Logger logger = LoggerFactory.getLogger(CompressedHandValueDB.class);

	/**
	 * The compressed hand value database.
	 */
	private final int[][] data = new int[Constants.HOLE_COUNT][];

	public CompressedHandValueDB(final InputStream in) throws IOException {
		final long t1 = System.currentTimeMillis();
		final DataInputStream dis = new DataInputStream(in);
		for (int i = 0; i < Constants.HOLE_COUNT; i++) {
			final int length = dis.readInt();
			final byte[] bytes = new byte[length * 4];
			dis.readFully(bytes);
			final int[] compressedBoardData = new int[length];
			int k = 0;
			for (int j = 0; j < length; j++) {
				int z = bytes[k++] & 0xff;
				z = (z << 8) | (bytes[k++] & 0xff);
				z = (z << 8) | (bytes[k++] & 0xff);
				z = (z << 8) | (bytes[k++] & 0xff);
				compressedBoardData[j] = z;
			}
			data[i] = compressedBoardData;
		}
		final long t2 = System.currentTimeMillis();
		logger.info("db init in {} ms", (t2 - t1));
	}

	/**
	 * Expand the compressed data for the specified hole index to the specified
	 * array. The array must be at least {@link Constants#BOARD_COUNT_52} long.
	 * 
	 * @param index
	 *            the index of the hole
	 * @param udata
	 *            the array to contain the uncompressed data
	 * @throws ArrayIndexOutOfBoundsException
	 *             if the hole index is invalid, or the udata array is too short
	 */
	public void expand(final int index, final int[] udata) {
		CompressLZFI.expand(data[index], 0, data.length, udata, 0, udata.length);
	}

}
