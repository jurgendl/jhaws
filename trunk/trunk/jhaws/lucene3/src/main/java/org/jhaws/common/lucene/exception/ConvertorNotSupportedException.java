package org.jhaws.common.lucene.exception;

/**
 * document conversion not supported exception
 *
 * @author Jurgen De Landsheer
 * @version 1.0.0 - 22 June 2006
 *
 * @since 1.5
 */
public class ConvertorNotSupportedException extends LuceneHelperException {
    /** serialVersionUID */
    private static final long serialVersionUID = -151114161901090603L;

/**
     * Creates a new ConvertorNotSupportedException object.
     *
     * @param ext file or extension name
     */
    public ConvertorNotSupportedException(final String ext) {
        super("document to text convertor not found/supported for file/extension " + ext); //$NON-NLS-1$
    }

/**
     * Creates a new ConvertorNotSupportedException object.
     *
     * @param ext file or extension name
     * @param cause cause
     */
    public ConvertorNotSupportedException(final String ext, final Throwable cause) {
        super("document to text convertor not found/supported for file/extension " + ext, cause); //$NON-NLS-1$
    }
}
