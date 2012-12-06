package org.jhaws.common.lucene.doctype;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.jhaws.common.io.IODirectory;
import org.jhaws.common.io.IOFile;
import org.jhaws.common.lucene.AbstractToLuceneDocument;


/**
 * na
 * 
 * @author Jurgen De Landsheer
 */
public class OpenOfficeToLuceneDocument extends AbstractToLuceneDocument {
    /**
     * Creates a new OpenOfficeToLuceneDocument object.
     */
    public OpenOfficeToLuceneDocument() {
        super();
    }

    /**
     * 
     * @see org.jhaws.common.lucene.AbstractToLuceneDocument#getText(util.io.IOFile)
     */
    @Override
    public String getText(IOFile file) throws IOException {
        try {
            IODirectory dir = IODirectory.newTempDir(file.getShortName()).create();
            dir.deleteOnExit();
            this.unzip(file, dir);

            IOFile xml = new IOFile(dir, "content.xml"); //$NON-NLS-1$

            return new XmlToLuceneDocument().getText(xml);
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
        return "odt;ods;odp"; //$NON-NLS-1$
    }

    protected void unzip(IOFile source, IODirectory target) throws IOException {
        ZipInputStream zin = new ZipInputStream(new FileInputStream(source));
        ZipEntry entry;
        int read;

        while ((entry = zin.getNextEntry()) != null) {
            byte[] buffer = new byte[8 * 1024];
            IOFile file = new IOFile(target, entry.getName());
            file.mkDir();
            OutputStream out = new FileOutputStream(file);

            while ((read = zin.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
        }

        zin.close();
    }
}
