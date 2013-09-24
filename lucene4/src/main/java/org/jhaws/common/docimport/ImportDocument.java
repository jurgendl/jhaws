package org.jhaws.common.docimport;

import java.io.IOException;
import java.io.InputStream;

import org.jhaws.common.io.IOFile;

public interface ImportDocument {
    String getText(InputStream file) throws IOException;

    String getText(IOFile file) throws IOException;
}
