package com.sebster.poker.webservices;

import com.sebster.poker.odds.Constants;

/**
 * Class which holds decompress buffers for the odds calculators. The buffers
 * are stored in a thread local so each thread has its own buffer.
 * 
 * @author sebster
 */
public class DecompressBufferHolder {

	private final ThreadLocal<int[][]> buffer = new ThreadLocal<int[][]>();

	/**
	 * Get the decompress buffers for the current thread.
	 * 
	 * @return the decompress buffers for the current thread
	 */
	public int[][] getBuffer() {
		int[][] buffer = this.buffer.get();
		if (buffer == null) {
			buffer = new int[36][Constants.BOARD_COUNT_52];
			this.buffer.set(buffer);
		}
		return buffer;
	}

}
