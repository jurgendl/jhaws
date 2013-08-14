package org.jhaws.common.lucene.doctype;

import java.io.IOException;

import org.jhaws.common.io.IOFile;
import org.jhaws.common.lucene.AbstractToLuceneDocument;
import org.jhaws.common.lucene.doctype.conversion.XPdfExtractor;

/**
 * uses xpdf as command line utility to convert pdf to text (very performant), needs connection to internet and library ftp server to be up to work
 * 
 * @author Jurgen
 */
public class PdfToLuceneDocument extends AbstractToLuceneDocument {
    /**
     * na
     * 
     * @param pdff
     * 
     * @return
     * 
     * @throws IOException
     */
    private static IOFile extract(IOFile pdff) throws IOException {
        return XPdfExtractor.extract(pdff);
    }

    /**
     * na
     * 
     * @param pdff na
     * 
     * @return
     * 
     * @throws IOException
     */
    public static String extractText(IOFile pdff) throws IOException {
        return XPdfExtractor.read(PdfToLuceneDocument.extract(pdff));
    }

    public PdfToLuceneDocument() {
        throw new RuntimeException("*");
    }

    /**
     * 
     * @see org.jhaws.common.lucene.AbstractToLuceneDocument#getText(util.io.IOFile)
     */
    @Override
    public String getText(IOFile file) throws IOException {
        return PdfToLuceneDocument.extractText(file);
    }

    /**
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "pdf"; //$NON-NLS-1$
    }
}
