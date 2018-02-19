package org.jhaws.common.lang;

public class MappingException extends RuntimeException {
    private static final long serialVersionUID = 8395540039829444625L;

    public MappingException() {
        super();
    }

    public MappingException(String message) {
        super(message);
    }

    public MappingException(String message, Throwable cause) {
        super(message, cause);
    }

    public MappingException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public MappingException(Throwable cause) {
        super(cause);
    }
}
