package org.jhaws.common.docimport;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.swing.text.BadLocationException;
import javax.swing.text.rtf.RTFEditorKit;

import org.jhaws.common.io.IOFile;

/**
 * RTF document conversion
 * 
 * @author Jurgen
 * @version 1.0.0 - 22 June 2006
 * 
 * @since 1.5
 */
public class ImportRtfDocument implements ImportDocument {
    /**
     * Creates a new RtfToLuceneDocument object.
     */
    public ImportRtfDocument() {
        super();
    }

    /**
     * 
     * @see org.jhaws.common.docimport.ImportDocument#getText(java.io.InputStream)
     */
    @Override
    public String getText(InputStream file) throws IOException {
        try {
            RTFEditorKit kit = new RTFEditorKit();
            javax.swing.text.Document tdoc = kit.createDefaultDocument();
            kit.read(file, tdoc, 0);

            return tdoc.getText(0, tdoc.getLength());
        } catch (final BadLocationException e) {
            throw new IOException(e.getMessage());
        }
    }

    /**
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "rtf";
    }

    /**
     * @see org.jhaws.common.docimport.ImportDocument#getText(org.jhaws.common.io.IOFile)
     */
    @Override
    public String getText(IOFile file) throws IOException {
        return getText(new FileInputStream(file));
    }
}
