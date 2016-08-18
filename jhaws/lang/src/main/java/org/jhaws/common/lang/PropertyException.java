package org.jhaws.common.lang;

public class PropertyException extends RuntimeException {
	private static final long serialVersionUID = -9178900002471939847L;

	public PropertyException() {
		super();
	}

	public PropertyException(String message, Throwable cause) {
		super(message, cause);
	}

	public PropertyException(String message) {
		super(message);
	}

	public PropertyException(Throwable cause) {
		super(cause);
	}
}
