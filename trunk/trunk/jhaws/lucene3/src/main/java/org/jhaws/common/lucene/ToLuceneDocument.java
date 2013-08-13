package org.jhaws.common.lucene;

import java.io.IOException;
import java.net.URL;

import org.apache.lucene.document.Document;
import org.jhaws.common.io.IOFile;

/**
 * interface for a document reader
 * 
 * @author Jurgen
 * @since 1.5
 * @version 1.0.0 - 22 June 2006
 */
public interface ToLuceneDocument {
    /**
     * na
     * 
     * @param analyzer na
     * @param file na
     * @param url na
     * @param keywords na
     * @param description na
     * 
     * @return
     * 
     * @throws IOException na
     */
    public Document convert(final Analyzer analyzer, final IOFile file, final URL url, final String[] keywords, final String description)
            throws IOException;

    /**
     * na
     * 
     * @param file na
     * 
     * @return
     * 
     * @throws IOException na
     */
    public String getText(final IOFile file) throws IOException;
}
