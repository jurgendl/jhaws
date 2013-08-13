package org.jhaws.common.lucene.doctype;

import java.io.IOException;
import java.io.StringWriter;

import org.jhaws.common.io.IOFile;
import org.jhaws.common.lucene.AbstractToLuceneDocument;


/**
 * Word DOC document conversion
 * 
 * @author Jurgen
 * @version 1.0.0 - 22 June 2006
 * 
 * @since 1.5
 */
public class WordToLuceneDocument extends AbstractToLuceneDocument {
    /**
     * Creates a new WordToLuceneDocument object.
     */
    public WordToLuceneDocument() {
        super();
    }

    /**
     * 
     * @see org.jhaws.common.lucene.AbstractToLuceneDocument#getText(java.io.File)
     */
    @SuppressWarnings("deprecation")
    @Override
    public String getText(final IOFile file) throws IOException {
        org.apache.poi.hdf.extractor.WordDocument word = new org.apache.poi.hdf.extractor.WordDocument(file.getAbsolutePath());
        word.openDoc();

        StringWriter writer = new StringWriter();
        word.writeAllText(writer);

        return writer.getBuffer().toString();
    }

    /**
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "doc"; //$NON-NLS-1$
    }
}
