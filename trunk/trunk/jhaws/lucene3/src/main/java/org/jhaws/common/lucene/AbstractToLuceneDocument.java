package org.jhaws.common.lucene;

import java.io.IOException;
import java.net.URL;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.jhaws.common.io.IOFile;

/**
 * document support, abstract
 * 
 * @author Jurgen De Landsheer
 * @version 1.0.0 - 22 June 2006
 * 
 * @since 1.5
 */
public abstract class AbstractToLuceneDocument implements ToLuceneDocument {
    /**
     * gets description
     * 
     * @param description description or null
     * 
     * @return description or ""
     */
    protected static final String getDescription(final String description) {
        return (description == null) ? "" : description; //$NON-NLS-1$
    }

    /**
     * converts keywords to single string
     * 
     * @param keywords keywords as array
     * 
     * @return keywords as string
     */
    protected static final String keywordsToString(final String[] keywords) {
        StringBuilder sb = new StringBuilder();

        if ((keywords != null) && (keywords.length > 0)) {
            for (int i = 0; i < keywords.length; i++) {
                sb.append(keywords[i]);

                if (i < (keywords.length - 1)) {
                    sb.append(" "); //$NON-NLS-1$
                }
            }
        }

        return sb.toString();
    }

    /**
     * Creates a new AbstractToLuceneDocument object.
     */
    protected AbstractToLuceneDocument() {
        super();
    }

    /**
     * 
     * @see org.jhaws.common.lucene.ToLuceneDocument#convert(org.jhaws.common.lucene.Analyzer, java.io.File, String[], String)
     */
    @Override
    public Document convert(Analyzer analyzer, IOFile file, URL url, String[] keywords, String description) throws IOException {
        Document doc = new Document();

        doc.add(new Field(ID.analyzer.f(), analyzer.analyzer().getClass().getName(), LuceneInterface.STORED, LuceneInterface.NOT_ANALYZED,
                LuceneInterface.NOTERM));
        doc.add(new Field(ID.description.f(), AbstractToLuceneDocument.getDescription(description), LuceneInterface.STORED, LuceneInterface.ANALYZED,
                LuceneInterface.NOTERM));
        doc.add(new Field(ID.filetype.f(), FileType.FILE.toString(), LuceneInterface.STORED, LuceneInterface.NOT_ANALYZED, LuceneInterface.NOTERM));
        doc.add(new Field(ID.keywords.f(), AbstractToLuceneDocument.keywordsToString(keywords), LuceneInterface.STORED, LuceneInterface.ANALYZED,
                LuceneInterface.NOTERM));
        doc.add(new Field(ID.language.f(), analyzer.language(), LuceneInterface.STORED, LuceneInterface.NOT_ANALYZED, LuceneInterface.NOTERM));
        doc.add(new Field(ID.lastmod.f(), String.valueOf(file.lastModified()), LuceneInterface.STORED, LuceneInterface.NOT_ANALYZED,
                LuceneInterface.NOTERM));
        doc.add(new Field(ID.size.f(), String.valueOf(file.length()), LuceneInterface.STORED, LuceneInterface.NOT_ANALYZED, LuceneInterface.NOTERM));
        doc.add(new Field(ID.text.f(), this.getText(file), LuceneInterface.NOTSTORED, LuceneInterface.ANALYZED, LuceneInterface.TERM));
        doc.add(new Field(ID.url.f(), url.toString(), LuceneInterface.STORED, LuceneInterface.NOT_ANALYZED, LuceneInterface.NOTERM));

        return doc;
    }

    /**
     * 
     * @see org.jhaws.common.lucene.ToLuceneDocument#getText(java.io.File)
     */
    @Override
    public abstract String getText(final IOFile file) throws IOException;
}
