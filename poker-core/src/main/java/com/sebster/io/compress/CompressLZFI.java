package com.sebster.io.compress;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import com.sebster.util.Validate;

public final class CompressLZFI {

	/**
	 * The number of bits in the control word to use. For example, this can be
	 * used to restrict the control word to 3 bytes. Generally just use the
	 * entire word (32 bits).
	 * 
	 * The high bit of the control word chooses between a literal run and a back
	 * reference. For a literal run the remaining bits are the unsigned length
	 * of the run. For a back reference the next OFFSET_BITS bits are the
	 * unsigned offset of the back reference, and the remaining bits are the
	 * unsigned length.
	 */
	private static final int CONTROL_BITS = 32;

	/**
	 * The number of bits to use for the back reference offset in the control
	 * word.
	 */
	private static final int OFFSET_BITS = 20;

	/*
	 * Don't change the values below. They are fully determined by the above two
	 * values and changing them will break the algorithm.
	 */

	/**
	 * The number of bits to use for the back reference length in the control
	 * word.
	 */
	private static final int LENGTH_BITS = CONTROL_BITS - OFFSET_BITS - 1;

	/**
	 * The maximum value of the length in a literal run.
	 */
	private static final int MAX_LITERALS = 1 << CONTROL_BITS - 1;

	/**
	 * The maximum value of the offset in a back reference.
	 */
	private static final int MAX_OFFSET = 1 << OFFSET_BITS - 1;

	/**
	 * The maximum value of the length in a back reference.
	 */
	public static final int MAX_LENGTH = 1 << LENGTH_BITS - 1;

	/**
	 * Private constructor to avoid instantiation.
	 */
	private CompressLZFI() {
		// Utility class.
	}

	/**
	 * A hashable triplet of integers.
	 * 
	 * @author sebster
	 */
	public static final class Triplet {

		private final int v1, v2, v3;

		public Triplet(final int v1, final int v2, final int v3) {
			this.v1 = v1;
			this.v2 = v2;
			this.v3 = v3;
		}

		@Override
		public int hashCode() {
			return 31 + (v1 * 37 + v2) * 37 + v3;
		}

		@Override
		public boolean equals(final Object object) {
			if (object instanceof Triplet) {
				final Triplet other = (Triplet) object;
				return v1 == other.v1 && v2 == other.v2 && v3 == other.v3;
			}
			return false;
		}
	}

	/**
	 * Compress an array of integers from the source to the destination array.
	 * 
	 * @param in
	 *            the input array to compress
	 * @param inLen
	 *            the number of integers to compress
	 * @param out
	 *            the output array
	 * @param outPos
	 *            the destination index to which to compress
	 * @return the compressed size
	 */
	public static int compress(final int[] in, final int inLen, final int[] out, int outPos) {
		final Map<Triplet, SortedSet<Integer>> locationMap = new HashMap<Triplet, SortedSet<Integer>>();

		// Find locations of all triplets.
		final int last = inLen - 2;
		for (int i = 0; i < last; i++) {
			final Triplet triplet = new Triplet(in[i], in[i + 1], in[i + 2]);
			SortedSet<Integer> locations = locationMap.get(triplet);
			if (locations == null) {
				locations = new TreeSet<Integer>();
				locationMap.put(triplet, locations);
			}
			locations.add(i);
		}

		int inPos = 0;
		int literals = 0;
		outPos++; // Make space for control word.
		while (inPos < last) {
			final Triplet triplet = new Triplet(in[inPos], in[inPos + 1], in[inPos + 2]);
			SortedSet<Integer> locations = locationMap.get(triplet);
			if (locations != null) {
				locations = locations.tailSet(inPos - MAX_OFFSET - 1).headSet(inPos);
			}
			if (locations != null && !locations.isEmpty()) {
				// Match.

				// Save the literals.
				if (literals == 0) {
					// Multiple back-references, so there is no literal run
					// control word.
					outPos--;
				} else {
					// Set the control word at the start of the literal run
					// to store the number of literals
					out[outPos - literals - 1] = literals - 1;
					literals = 0;
				}

				// Find reference with longest match.
				int bestOff = 0;
				int bestLen = 0;
				for (final int ref : locations) {
					if (ref >= inPos) {
						// Can't do forward references.
						throw new IllegalStateException("ref " + ref + " not a backreference (inPos = " + inPos + ")");
					}
					final int off = inPos - ref - 1;
					if (off < 0 || off > MAX_OFFSET) {
						throw new IllegalStateException("offset " + off + " out of range [0.." + MAX_OFFSET + "]");
					}
					int maxLen = inLen - inPos - 3;
					if (maxLen > MAX_LENGTH) {
						maxLen = MAX_LENGTH;
					}

					// Calculate the length of the match.
					int len = 0;
					while (len < maxLen && in[ref + len + 3] == in[inPos + len + 3]) {
						len++;
					}
					if (len >= bestLen) {
						/*
						 * Better length or closer match. Closer is good because
						 * the data may still be in the processor cache.
						 */
						bestLen = len;
						bestOff = off;
					}
					// Can't find a better match.
					if (len == MAX_LENGTH) {
						break;
					}
				}

				out[outPos++] = 0x80000000 | bestOff << LENGTH_BITS | bestLen;
				// Move one word forward to allow for a literal run control
				// word.
				outPos++;

				inPos += bestLen + 3;
			} else {
				// Literal.
				out[outPos++] = in[inPos++];
				literals++;
				if (literals == MAX_LITERALS) {
					out[outPos - literals - 1] = literals - 1;
					literals = 0;
					outPos++;
				}

			}
		}
		// Write the remaining few ints as literals.
		while (inPos < inLen) {
			out[outPos++] = in[inPos++];
			literals++;
			if (literals == MAX_LITERALS) {
				out[outPos - literals - 1] = literals - 1;
				literals = 0;
				outPos++;
			}
		}
		// Writes the final literal run length to the control byte
		out[outPos - literals - 1] = literals - 1;
		if (literals == 0) {
			outPos--;
		}
		return outPos;
	}

	/**
	 * Decompress an array of integers from the source to the destination array.
	 * 
	 * @param in
	 *            the input array to decompress
	 * @param inPos
	 *            the input position to start compressing from
	 * @param inLen
	 *            the number of integers to decompress
	 * @param out
	 *            the output array
	 * @param outPos
	 *            the destination index to which to decompress
	 * @throws ArrayIndexOutOfBoundsException
	 *             if the compressed input is invalid
	 */
	public static void expand(final int[] in, int inPos, final int inLen, final int[] out, int outPos) {
		final int inEnd = inPos + inLen;
		if (inPos < 0 || inLen < 0 || outPos < 0 || inEnd > in.length) {
			throw new IllegalArgumentException();
		}
		while (inPos < inEnd) {
			final int ctrl = in[inPos++];
			if (ctrl >= 0) {
				// Literal run of length = ctrl + 1.
				final int len = ctrl + 1;
				// Copy to output and move forward this many bytes.
				System.arraycopy(in, inPos, out, outPos, len);
				outPos += len;
				inPos += len;
			} else {
				// Back reference, get the length and offset from ctrl.
				final int len = (ctrl & (1 << LENGTH_BITS) - 1) + 3;
				final int off = ((ctrl & Integer.MAX_VALUE) >> LENGTH_BITS) + 1;

				/*
				 * Copy the back-reference values from the given location in the
				 * output to current position. Can't us System.arrayCopy() here
				 * because of the possibility of overlap.
				 */
				int ref = outPos - off;
				if (outPos + len > out.length) {
					/*
					 * Reduce array bounds checking (allows HotSpot to deduce no
					 * checks are needed below).
					 */
					throw new ArrayIndexOutOfBoundsException();
				}
				for (int i = 0; i < len; i++) {
					out[outPos++] = out[ref++];
				}
			}
		}
	}

	public static void main(final String[] args) throws IOException {
		if (args.length != 3 || !"c".equals(args[0]) && !"x".equals(args[0])) {
			System.err.println("usage: " + CompressLZFI.class.getSimpleName() + " <c|x> <infile> <outfile>");
			System.exit(1);
		}
		final File infile = new File(args[1]);
		final DataInputStream in = new DataInputStream(new BufferedInputStream(new FileInputStream(infile)));
		int size = (int) infile.length();
		Validate.isTrue(size % 4 == 0, "file must be multiple of 4 bytes long");
		size = size / 4;
		if ("c".equals(args[0])) {
			System.out.println("compressing file of size=" + size);
			final int[] inbuf = new int[size], outbuf = new int[size];
			int index = 0;
			while (index < size) {
				inbuf[index++] = in.readInt();
			}
			in.close();
			final int outSize = compress(inbuf, size, outbuf, 0);
			System.out.println("compressed size=" + outSize);
			final DataOutputStream out = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(args[2])));
			out.writeInt(outSize);
			for (int i = 0; i < outSize; i++) {
				out.writeInt(outbuf[i]);
			}
			out.close();
		} else {
			final int outSize = in.readInt();
			size--;
			int index = 0;
			final int[] inbuf = new int[size], outbuf = new int[outSize];
			while (index < size) {
				inbuf[index++] = in.readInt();
			}
			in.close();
			expand(inbuf, 0, size, outbuf, 0);
			final DataOutputStream out = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(args[2])));
			for (int i = 0; i < outSize; i++) {
				out.writeInt(outbuf[i]);
			}
			out.close();
		}
	}

}