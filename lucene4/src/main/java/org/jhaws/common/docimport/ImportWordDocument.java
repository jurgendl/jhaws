package org.jhaws.common.docimport;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;

import org.jhaws.common.io.IOFile;

/**
 * Word DOC document conversion
 * 
 * @author Jurgen
 * @version 1.0.0 - 22 June 2006
 * 
 * @since 1.5
 */
public class ImportWordDocument implements ImportDocument {
    /**
     * Creates a new WordToLuceneDocument object.
     */
    public ImportWordDocument() {
        super();
    }

    /**
     * 
     * @see org.jhaws.common.docimport.ImportDocument#getText(java.io.InputStream)
     */
    @SuppressWarnings("deprecation")
    @Override
    public String getText(final InputStream file) throws IOException {
        org.apache.poi.hdf.extractor.WordDocument word = new org.apache.poi.hdf.extractor.WordDocument(file);
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
        return "doc";
    }

    /**
     * @see org.jhaws.common.docimport.ImportDocument#getText(org.jhaws.common.io.IOFile)
     */
    @Override
    public String getText(IOFile file) throws IOException {
        return getText(new FileInputStream(file));
    }
}
