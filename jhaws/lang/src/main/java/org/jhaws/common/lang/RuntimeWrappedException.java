package org.jhaws.common.lang;

@SuppressWarnings("serial")
public class RuntimeWrappedException extends RuntimeException {
    public RuntimeWrappedException(Exception exception) {
        super(exception);
    }
}
