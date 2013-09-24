package org.jhaws.common.docimport;

import java.io.IOException;
import java.io.InputStream;

import org.jhaws.common.docimport.conversion.XPdfExtractor;
import org.jhaws.common.io.IOFile;

/**
 * uses xpdf as command line utility to convert pdf to text (very performant), needs connection to internet and library ftp server to be up to work
 * 
 * @author Jurgen
 */
public class ImportPdfDocument implements ImportDocument {
    public static final String PDF = "pdf";

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
        return new String(ImportPdfDocument.extract(pdff).read());
    }

    public ImportPdfDocument() {
        super();
    }

    /**
     * @see org.jhaws.common.docimport.ImportDocument#getText(java.io.InputStream)
     */
    @Override
    public String getText(InputStream file) throws IOException {
        IOFile tmp = IOFile.newTmpFileExt(PDF).writeBytes(file);
        return getText(tmp);
    }

    /**
     * @see org.jhaws.common.docimport.ImportDocument#getText(org.jhaws.common.io.IOFile)
     */
    @Override
    public String getText(IOFile file) throws IOException {
        return ImportPdfDocument.extractText(file);
    }

    /**
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return PDF;
    }
}
