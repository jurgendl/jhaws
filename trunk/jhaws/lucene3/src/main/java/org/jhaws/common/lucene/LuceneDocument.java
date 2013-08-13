package org.jhaws.common.lucene;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.apache.log4j.Logger;
import org.apache.lucene.document.Document;
import org.jhaws.common.io.IODirectory;
import org.jhaws.common.io.IOFile;

/**
 * na
 * 
 * @author Jurgen
 */
public class LuceneDocument implements Comparable<LuceneDocument>, Serializable {
    /** Logger for this class */
    private static final Logger logger = Logger.getLogger(LuceneDocument.class);

    /** serialVersionUID */
    private static final long serialVersionUID = -1507826843474622688L;

    /** field */
    private final Date lastModified;

    /** field */
    private final Document doc;

    /** field */
    private final LuceneInterface helper;

    /** field */
    private final String description;

    /** field */
    private final String filetype;

    /** field */
    private final String language;

    /** field */
    private final URL url;

    /** field */
    private final String[] keywords;

    /** field */
    private final long length;

    /**
     * Creates a new LuceneDocument object.
     * 
     * @param doc na
     */
    protected LuceneDocument(final Document doc, final LuceneInterface helper) {
        this.doc = doc;
        this.helper = helper;
        this.lastModified = new Date(Long.parseLong(doc.get(ID.lastmod.f())));

        URL tmp = null;

        try {
            String spec = doc.get(ID.url.f());

            if (spec.startsWith("doc")) {
                tmp = new URL("doc", "localhost", 0, spec.substring("doc://localhost:0".length()), LuceneHelper.DOC_HANDLER);
            } else {
                tmp = new URL(spec);
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }

        this.url = tmp;

        this.length = Long.parseLong(doc.get(ID.size.f()));
        this.language = doc.get(ID.language.f());
        this.description = doc.get(ID.description.f());
        this.keywords = doc.get(ID.keywords.f()).split(" "); //$NON-NLS-1$
        this.filetype = doc.get(ID.filetype.f());
    }

    /**
     * 
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    @Override
    public int compareTo(final LuceneDocument other) {
        return new CompareToBuilder().append(this.getURL().toString(), other.getURL().toString())
                .append(this.getLastModified().getTime(), other.getLastModified().getTime()).toComparison();
    }

    /**
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public final boolean equals(final Object other) {
        if (this == other) {
            return true;
        }

        if (!(other instanceof SearchResult)) {
            return false;
        }

        SearchResult castOther = (SearchResult) other;

        return new EqualsBuilder().append(this.getURL(), castOther.getURL())
                .append(this.getLastModified().getTime(), castOther.getLastModified().getTime()).isEquals();
    }

    /**
     * na
     * 
     * @param url0 na
     * 
     * @return
     * 
     * @throws IOException na
     * @throws ArchiveException
     * @throws FileNotFoundException na
     */
    private String getCachedText(String url0) throws IOException {
        LuceneDocument.logger.debug("getCachedText(String) - start"); //$NON-NLS-1$

        IOFile source = new IOFile(new URL(url0).getFile());
        LuceneDocument.logger.debug("cache(String, String) - IOFile source=" + source); //$NON-NLS-1$

        IODirectory cacheDir = new IODirectory(this.helper.getIndexLocation(), "cache").create(); //$NON-NLS-1$
        LuceneDocument.logger.debug("getCachedText(String) - IODirectory cacheDir=" + cacheDir); //$NON-NLS-1$

        IOFile target = new IOFile(cacheDir, source.getAbsolutePath().replace(':', '$').replace('/', '$').replace('\\', '$') + ".txt"); //$NON-NLS-1$
        LuceneDocument.logger.debug("cache(String, String) - IOFile target=" + target); //$NON-NLS-1$

        IOFile bzip = new IOFile(target + ".bz2"); //$NON-NLS-1$
        LuceneDocument.logger.debug("cache(String, String) - IOFile zip=" + bzip); //$NON-NLS-1$

        if (!bzip.exists()) {
            throw new FileNotFoundException(bzip.toString());
        }

        LuceneArchive archive = new LuceneArchive(bzip);
        archive.extract(archive.getArchiveFile().getParentDirectory());

        FileInputStream in = new FileInputStream(target);
        byte[] buffer = new byte[in.available()];
        in.read(buffer);
        in.close();

        target.erase();

        String returnString = new String(buffer);
        LuceneDocument.logger.debug("getCachedText(String) - end"); //$NON-NLS-1$

        return returnString;
    }

    /**
     * na
     * 
     * @return the description
     */
    public final String getDescription() {
        return this.description;
    }

    /**
     * na
     * 
     * @return
     * 
     * @throws IOException na
     */
    public String getDocumentText() throws IOException {
        if (!this.getFiletype().equals(FileType.FILE.toString())) {
            LuceneDocument.logger.debug("NON FILE"); //$NON-NLS-1$

            return this.doc.get(ID.text.f());
        }

        try {
            String text = this.getCachedText(this.getURL().toString());
            LuceneDocument.logger.debug("CACHED TEXT"); //$NON-NLS-1$

            return text;
        } catch (Exception ex) {
            IOFile file = new IOFile(this.getURL().getFile());
            ToLuceneDocument convertor = DocumentFactory.getConvertor(file);

            String text = convertor.getText(file);
            LuceneDocument.logger.debug("TEXT FROM FILE"); //$NON-NLS-1$

            return text;
        }
    }

    /**
     * na
     * 
     * @return the filetype
     */
    public final String getFiletype() {
        return this.filetype;
    }

    /**
     * na
     * 
     * @return the keywords
     */
    public final String[] getKeywords() {
        return this.keywords;
    }

    /**
     * na
     * 
     * @return the language
     */
    public final String getLanguage() {
        return this.language;
    }

    /**
     * na
     * 
     * @return the lastModified
     */
    public final Date getLastModified() {
        return this.lastModified;
    }

    /**
     * na
     * 
     * @return the length
     */
    public final long getLength() {
        return this.length;
    }

    /**
     * na
     * 
     * @return the doc
     */
    public final Document getLuceneDocument() {
        return this.doc;
    }

    /**
     * na
     * 
     * @return the file
     */
    public final URL getURL() {
        return this.url;
    }

    /**
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public final int hashCode() {
        return new HashCodeBuilder(-979826555, 734680275).append(this.getURL()).append(this.getLastModified().getTime()).toHashCode();
    }

    /**
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SIMPLE_STYLE)
                .append("url", this.getURL()).append("lastModified", this.getLastModified()).append("description", this.getDescription()).append("keywords", this.getKeywords()).toString(); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
    }
}
