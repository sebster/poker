package com.sebster.util;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.net.ServerSocket;
import java.net.Socket;

public final class IOUtil {

	private static final byte[] ZERO_BYTES = new byte[4096];

	private IOUtil() {
		// Utility class.
	}

	public static byte readByte(final InputStream in) throws IOException {
		final int b = in.read();
		if (b < 0) {
			throw new EOFException();
		}
		return (byte) b;
	}

	public static int readInt2(final InputStream in) throws IOException {
		final int b1 = in.read();
		final int b2 = in.read();
		if (b2 < 0) {
			throw new EOFException();
		}
		return (b1 << 8 ^ b2) & 0xffff;
	}

	public static int readInt4(final InputStream in) throws IOException {
		final int b1 = in.read();
		final int b2 = in.read();
		final int b3 = in.read();
		final int b4 = in.read();
		if (b4 < 0) {
			throw new EOFException();
		}
		return b1 << 24 ^ b2 << 16 ^ b3 << 8 ^ b4;
	}

	public static long readLong(final InputStream in) throws IOException {
		final long i1 = readInt4(in);
		final long i2 = readInt4(in);
		return i1 << 32 ^ i2 & 0xffffffffL;
	}

	public static void writeInt2(final OutputStream out, final int value) throws IOException {
		writeInt(out, value, 2);
	}

	public static void writeInt4(final OutputStream out, final int value) throws IOException {
		writeInt(out, value, 4);
	}

	public static void writeLong(final OutputStream out, final long value) throws IOException {
		writeInt4(out, (int) (value >> 32));
		writeInt4(out, (int) value);
	}

	public static void writeInt(final OutputStream out, final int value, final int length) throws IOException {
		for (int i = length - 1; i >= 0; i--) {
			out.write(value >> (i << 3));
		}
	}

	public static void writeZeros(final OutputStream out, final int length) throws IOException {
		int bytesRemaining = length;
		while (bytesRemaining > 0) {
			final int count = Math.min(bytesRemaining, ZERO_BYTES.length);
			out.write(ZERO_BYTES, 0, count);
			bytesRemaining -= count;
		}
	}

	public static void skip(final InputStream in, final long count) throws IOException {
		long remaining = count;
		while (remaining > 0) {
			final long skipped = in.skip(remaining);
			if (skipped < 0) {
				throw new EOFException();
			}
			remaining -= skipped;
		}
	}

	public static final IOException ioException(final Exception cause) throws IOException {
		return ioException(null, cause);
	}

	public static final IOException ioException(final String message, final Exception cause) throws IOException {
		if (cause instanceof IOException && message == null) {
			// Don't touch I/O exceptions unless the message is set.
			throw (IOException) cause;
		}
		if (cause instanceof RuntimeException && message == null) {
			// Don't touch runtime exceptions unless the message is set.
			throw (RuntimeException) cause;
		}
		// Wrap the exception with an I/O exception with the specified message.
		return (IOException) new IOException(message != null ? message : cause.getMessage()).initCause(cause);
	}

	public static void closeQuietly(final InputStream in) {
		try {
			if (in != null) {
				in.close();
			}
		} catch (final IOException e) {
			// Ignore.
		}
	}

	public static void closeQuietly(final OutputStream out) {
		try {
			if (out != null) {
				out.close();
			}
		} catch (final IOException e) {
			// Ignore.
		}
	}

	public static void closeQuietly(final Reader reader) {
		try {
			if (reader != null) {
				reader.close();
			}
		} catch (final IOException e) {
			// Ignore.
		}
	}

	public static void closeQuietly(final Writer writer) {
		try {
			if (writer != null) {
				writer.close();
			}
		} catch (final IOException e) {
			// Ignore.
		}
	}

	public static void closeQuietly(final Socket socket) {
		try {
			if (socket != null) {
				socket.close();
			}
		} catch (final IOException e) {
			// Ignore.
		}
	}

	public static void closeQuietly(final ServerSocket serverSocket) {
		try {
			if (serverSocket != null) {
				serverSocket.close();
			}
		} catch (final IOException e) {
			// Ignore.
		}
	}

}
