package org.jhaws.common.lucene.exception;

/**
 * generale Lucene helper exception
 *
 * @author Jurgen
 * @version 1.0.0 - 22 June 2006
 *
 * @since 1.5
 */
public class LuceneHelperException extends Exception {
    /** serialVersionUID */
    private static final long serialVersionUID = 8803241819334903689L;

/**
     * Creates a new LuceneHelperException object.
     */
    public LuceneHelperException() {
        super();
    }

/**
     * Creates a new LuceneHelperException object.
     *
     * @param message message
     */
    public LuceneHelperException(final String message) {
        super(message);
    }

/**
     * Creates a new LuceneHelperException object.
     *
     * @param message message
     * @param cause cause
     */
    public LuceneHelperException(final String message, final Throwable cause) {
        super(message, cause);
    }

/**
     * Creates a new LuceneHelperException object.
     *
     * @param cause cause
     */
    public LuceneHelperException(final Throwable cause) {
        super(cause);
    }
}
