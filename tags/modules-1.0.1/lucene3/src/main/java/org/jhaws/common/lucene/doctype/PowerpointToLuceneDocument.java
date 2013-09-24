package org.jhaws.common.lucene.doctype;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.poi.poifs.eventfilesystem.POIFSReader;
import org.apache.poi.poifs.eventfilesystem.POIFSReaderEvent;
import org.apache.poi.poifs.eventfilesystem.POIFSReaderListener;
import org.apache.poi.poifs.filesystem.DocumentInputStream;
import org.apache.poi.util.LittleEndian;
import org.jhaws.common.io.IOFile;
import org.jhaws.common.lucene.AbstractToLuceneDocument;

/**
 * Powerpoint PPT document conversion
 * 
 * @author Jurgen
 * @version 1.0.0 - 22 June 2006
 * 
 * @since 1.5
 */
public class PowerpointToLuceneDocument extends AbstractToLuceneDocument implements POIFSReaderListener {
    /** field */
    private ByteArrayOutputStream writer;

    /**
     * Creates a new PowerpointToLuceneDocument object.
     */
    public PowerpointToLuceneDocument() {
        super();
    }

    /**
     * 
     * @see org.jhaws.common.lucene.AbstractToLuceneDocument#getText(java.io.File)
     */
    @Override
    public String getText(final IOFile file) throws IOException {
        POIFSReader reader = new POIFSReader();
        FileInputStream is = new FileInputStream(file);
        this.writer = new ByteArrayOutputStream();
        reader.registerListener(this);
        reader.read(is);

        String contents = this.writer.toString();
        is.close();

        return contents;
    }

    /**
     * 
     * @see org.apache.poi.poifs.eventfilesystem.POIFSReaderListener#processPOIFSReaderEvent(org.apache.poi.poifs.eventfilesystem.POIFSReaderEvent)
     */
    @Override
    public void processPOIFSReaderEvent(final POIFSReaderEvent event) {
        try {
            if (!event.getName().equalsIgnoreCase("PowerPoint Document")) { //$NON-NLS-1$

                return;
            }

            DocumentInputStream input = event.getStream();

            byte[] buffer = new byte[input.available()];

            input.read(buffer, 0, input.available());

            for (int i = 0; i < (buffer.length - 20); i++) {
                long type = LittleEndian.getUShort(buffer, i + 2);

                long size = LittleEndian.getUInt(buffer, i + 4);

                if (type == 4008) {
                    this.writer.write(buffer, i + 4 + 1, (int) size + 3);

                    i = (i + 4 + 1 + (int) size) - 1;
                }

                try {
                    Thread.sleep(10);
                } catch (final Exception ex) {
                    //
                }
            }
        } catch (final Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "ppt"; //$NON-NLS-1$
    }
}
