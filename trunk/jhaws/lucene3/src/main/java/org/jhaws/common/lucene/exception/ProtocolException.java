package org.jhaws.common.lucene.exception;

import java.net.URL;


/**
 * protocol not supported exception
 *
 * @author Jurgen De Landsheer
 * @version 1.0.0 - 22 June 2006
 *
 * @since 1.5
 */
public class ProtocolException extends LuceneHelperException {
    /** serialVersionUID */
    private static final long serialVersionUID = -4257863153588947064L;

/**
     * Creates a new ProtocolException object.
     *
     * @param url URL
     */
    public ProtocolException(final URL url) {
        super("not supported: " + url.toString()); //$NON-NLS-1$
    }
}
