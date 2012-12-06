package org.jhaws.common.lucene.doctype;

import java.io.FileInputStream;
import java.io.IOException;

import javax.swing.text.BadLocationException;
import javax.swing.text.rtf.RTFEditorKit;

import org.jhaws.common.io.IOFile;
import org.jhaws.common.lucene.AbstractToLuceneDocument;


/**
 * RTF document conversion
 * 
 * @author Jurgen De Landsheer
 * @version 1.0.0 - 22 June 2006
 * 
 * @since 1.5
 */
public class RtfToLuceneDocument extends AbstractToLuceneDocument {
    /**
     * Creates a new RtfToLuceneDocument object.
     */
    public RtfToLuceneDocument() {
        super();
    }

    /**
     * 
     * @see org.jhaws.common.lucene.AbstractToLuceneDocument#getText(java.io.File)
     */
    @Override
    public String getText(final IOFile file) throws IOException {
        try {
            FileInputStream stream = new FileInputStream(file);
            RTFEditorKit kit = new RTFEditorKit();
            javax.swing.text.Document tdoc = kit.createDefaultDocument();
            kit.read(stream, tdoc, 0);

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
        return "rtf"; //$NON-NLS-1$
    }
}
