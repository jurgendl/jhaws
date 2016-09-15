package org.jhaws.common.batch;

public class BatchException extends RuntimeException {
	private static final long serialVersionUID = 4611089555156714534L;

	public BatchException() {
		super();
	}

	public BatchException(String message, Throwable cause) {
		super(message, cause);
	}

	public BatchException(String message) {
		super(message);
	}

	public BatchException(Throwable cause) {
		super(cause);
	}
}
