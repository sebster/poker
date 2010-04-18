package com.sebster.poker.holdem.odds.generation;

import java.io.IOException;

import com.sebster.poker.holdem.odds.PreFlopOddsCalculator;

public class CompressHandValueDB {

	/**
	 * Compress the hand value database using a custom int-aligned LZF
	 * algorithm. The first argument is the input filename of the gzipped hand
	 * value database, the second argument is the filename of the int-aligned
	 * LZF compressed output database. The default input filename is
	 * "holdem_hand_values.dat.gz", the default output filename is
	 * "holdem_hand_values.dat.lzfi.gz".
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

		String output = PreFlopOddsCalculator.DB_FILENAME;
		if (args.length > 1) {
			output = args[1];
		}

		com.sebster.poker.odds.generation.CompressHandValueDB.main(new String[] { input, output });
	}

}
