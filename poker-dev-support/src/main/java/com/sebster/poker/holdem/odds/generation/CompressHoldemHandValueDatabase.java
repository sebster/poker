package com.sebster.poker.holdem.odds.generation;

import java.io.IOException;

import com.sebster.poker.holdem.odds.FastHoldemPreflopOddsCalculator;
import com.sebster.poker.odds.generation.CompressHandValueDatabase;

public class CompressHoldemHandValueDatabase {

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

		String input = GenerateHandValueDB.DEFAULT_FILENAME;
		if (args.length > 0) {
			input = args[0];
		}

		String output = FastHoldemPreflopOddsCalculator.DB_FILENAME;
		if (args.length > 1) {
			output = args[1];
		}

		CompressHandValueDatabase.main(new String[] { input, output });
	}

}
