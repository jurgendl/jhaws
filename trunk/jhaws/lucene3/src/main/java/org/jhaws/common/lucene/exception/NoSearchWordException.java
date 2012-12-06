package org.jhaws.common.lucene.exception;

/**
 * not a single word in search
 *
 * @author Jurgen De Landsheer
 * @version 1.0.0 - 22 June 2006
 *
 * @since 1.5
 */
public class NoSearchWordException extends LuceneHelperException {
    /** serialVersionUID */
    private static final long serialVersionUID = 7700598828690911744L;

/**
     * Creates a new NoSearchWordException object.
     *
     * @param word search word
     * @param query search query
     */
    public NoSearchWordException(final String word, final String query) {
        super("word=" + word + ";query=" + query); //$NON-NLS-1$//$NON-NLS-2$
    }
}
