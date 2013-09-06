package org.jhaws.common.docimport;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.jhaws.common.io.IOFile;
import org.jhaws.common.io.IOnOSUtils;

/**
 * TXT document conversion
 * 
 * @author Jurgen
 * @version 1.0.0 - 22 June 2006
 * 
 * @since 1.5
 */
public class ImportTxtDocument implements ImportDocument {
    /**
     * Creates a new TxtToLuceneDocument object.
     */
    public ImportTxtDocument() {
        super();
    }

    /**
     * 
     * @see org.jhaws.common.docimport.ImportDocument#getText(java.io.InputStream)
     */
    @Override
    public String getText(InputStream file) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        IOnOSUtils.copy(file, out);
        return new String(out.toByteArray());
    }

    /**
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "txt";
    }

    /**
     * @see org.jhaws.common.docimport.ImportDocument#getText(org.jhaws.common.io.IOFile)
     */
    @Override
    public String getText(IOFile file) throws IOException {
        return getText(new FileInputStream(file));
    }
}
