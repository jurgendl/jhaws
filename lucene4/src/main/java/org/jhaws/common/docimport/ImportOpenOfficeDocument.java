package org.jhaws.common.docimport;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.jhaws.common.io.IODirectory;
import org.jhaws.common.io.IOFile;
import org.jhaws.common.io.Utils;

/**
 * na
 * 
 * @author Jurgen
 */
public class ImportOpenOfficeDocument implements ImportDocument {
    /**
     * Creates a new OpenOfficeToLuceneDocument object.
     */
    public ImportOpenOfficeDocument() {
        super();
    }

    /**
     * 
     * @see org.jhaws.common.docimport.ImportDocument#getText(java.io.InputStream)
     */
    @Override
    public String getText(InputStream file) throws IOException {
        try {
            IODirectory dir = IODirectory.newTempDir("" + file).create();
            dir.deleteOnExit();
            Utils.unzip(file, dir);

            IOFile xml = new IOFile(dir, "content.xml");

            return new ImportXmlDocument().getText(new FileInputStream(xml));
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }

    /**
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "odt;ods;odp";
    }

    /**
     * @see org.jhaws.common.docimport.ImportDocument#getText(org.jhaws.common.io.IOFile)
     */
    @Override
    public String getText(IOFile file) throws IOException {
        return getText(new FileInputStream(file));
    }
}
