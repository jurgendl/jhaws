package org.jhaws.common.docimport;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.poi.poifs.eventfilesystem.POIFSReader;
import org.jhaws.common.io.IOFile;

/**
 * Powerpoint PPT document conversion
 * 
 * @author Jurgen
 * @version 1.0.0 - 22 June 2006
 * 
 * @since 1.5
 */
public class ImportPowerpointDocument implements ImportDocument {
    /** field */
    private ByteArrayOutputStream writer;

    /**
     * Creates a new PowerpointToLuceneDocument object.
     */
    public ImportPowerpointDocument() {
        super();
    }

    /**
     * 
     * @see org.jhaws.common.docimport.ImportDocument#getText(java.io.InputStream)
     */
    @Override
    public String getText(InputStream file) throws IOException {
        POIFSReader reader = new POIFSReader();
        this.writer = new ByteArrayOutputStream();
        reader.read(file);

        String contents = this.writer.toString();
        file.close();

        return contents;
    }

    /**
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "ppt";
    }

    /**
     * @see org.jhaws.common.docimport.ImportDocument#getText(org.jhaws.common.io.IOFile)
     */
    @Override
    public String getText(IOFile file) throws IOException {
        return getText(new FileInputStream(file));
    }
}
