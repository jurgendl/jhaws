package org.jhaws.common.lang;

public class FieldNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 5863861684947177500L;

    public FieldNotFoundException() {
        super();
    }

    public FieldNotFoundException(String message) {
        super(message);
    }

    public FieldNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public FieldNotFoundException(Throwable cause) {
        super(cause);
    }
}