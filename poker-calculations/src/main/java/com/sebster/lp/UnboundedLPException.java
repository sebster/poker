package com.sebster.lp;

public class UnboundedLPException extends RuntimeException {

	public UnboundedLPException() {
		super();
	}

	public UnboundedLPException(final String message) {
		super(message);
	}

	public UnboundedLPException(final Throwable cause) {
		super(cause);
	}

	public UnboundedLPException(final String message, final Throwable cause) {
		super(message, cause);
	}

}
