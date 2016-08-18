package org.jhaws.common.lang;

public class FieldNotFoundException extends RuntimeException {
	private static final long serialVersionUID = 5863861684947177500L;

	/**
	 * Creates a new FieldNotFoundException object.
	 */
	public FieldNotFoundException() {
		super();
	}

	/**
	 * Creates a new FieldNotFoundException object.
	 */
	public FieldNotFoundException(String message) {
		super(message);
	}

	/**
	 * Creates a new FieldNotFoundException object.
	 */
	public FieldNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Creates a new FieldNotFoundException object.
	 */
	public FieldNotFoundException(Throwable cause) {
		super(cause);
	}
}