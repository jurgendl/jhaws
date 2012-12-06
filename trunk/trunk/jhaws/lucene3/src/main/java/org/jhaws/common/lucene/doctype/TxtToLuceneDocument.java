package org.jhaws.common.lucene.doctype;

import java.io.FileInputStream;
import java.io.IOException;

import org.jhaws.common.io.IOFile;
import org.jhaws.common.lucene.AbstractToLuceneDocument;


/**
 * TXT document conversion
 * 
 * @author Jurgen De Landsheer
 * @version 1.0.0 - 22 June 2006
 * 
 * @since 1.5
 */
public class TxtToLuceneDocument extends AbstractToLuceneDocument {
    /**
     * Creates a new TxtToLuceneDocument object.
     */
    public TxtToLuceneDocument() {
        super();
    }

    /**
     * 
     * @see org.jhaws.common.lucene.AbstractToLuceneDocument#getText(java.io.File)
     */
    @Override
    public String getText(final IOFile file) throws IOException {
        FileInputStream is = new FileInputStream(file);
        byte[] buffer = new byte[is.available()];
        is.read(buffer);
        is.close();

        return new String(buffer);
    }

    /**
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "txt"; //$NON-NLS-1$
    }
}
