package org.jhaws.common.lang;

@SuppressWarnings("serial")
public class InterruptedRuntimeException extends RuntimeWrappedException {
    public InterruptedRuntimeException(Exception exception) {
        super(exception);
    }

    public InterruptedRuntimeException() {
        super();
    }
}
