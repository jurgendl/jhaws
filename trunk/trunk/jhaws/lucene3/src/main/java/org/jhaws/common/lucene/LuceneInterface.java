package org.jhaws.common.lucene;

import java.io.IOException;
import java.io.PrintStream;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.SortedSet;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.search.Explanation;
import org.apache.lucene.search.Query;
import org.jhaws.common.io.IODirectory;
import org.jhaws.common.io.IOFile;
import org.jhaws.common.lucene.exception.ConvertorNotSupportedException;
import org.jhaws.common.lucene.exception.ProtocolException;
import org.jhaws.common.lucene.stat.DocHighScoreTerms;
import org.jhaws.common.lucene.stat.HighScoreTerms;

/**
 * lucene index interface
 * 
 * @author Jurgen
 */
public interface LuceneInterface {
    /** field will be saved */
    public static final Field.Store STORED = Field.Store.YES;

    /** field will not be saved */
    public static final Field.Store NOTSTORED = Field.Store.NO;

    /** field will be saved and compressed */
    public static final Field.Store COMPRESSED = LuceneInterface.STORED;

    /** field will be tokenized */
    public static final Field.Index ANALYZED = Field.Index.ANALYZED;

    /** field will not be tokenized */
    public static final Field.Index NOT_ANALYZED = Field.Index.NOT_ANALYZED;

    /** field is not a term */
    public static final Field.TermVector NOTERM = Field.TermVector.NO;

    /** field is a term */
    public static final Field.TermVector TERM = Field.TermVector.WITH_POSITIONS_OFFSETS;

    /** segments.gen exists means the indexfiles exist */
    public static final String segments = "segments.gen"; //$NON-NLS-1$

    /**
     * clean up indexed files that do not exist any more (checks via abolute location)
     * 
     * @return number of deleted files, -1 when index does not exist
     * 
     * @throws IOException IOException
     */
    public abstract int cleanUpNotExistingFileDocuments() throws IOException;

    /**
     * close
     * 
     * @throws IOException IOException
     */
    public abstract void close() throws IOException;

    /**
     * deletes index, makes this object unusable
     * 
     * @return success?
     * 
     * @throws IOException IOException
     */
    public abstract boolean delete() throws IOException;

    /**
     * delete document
     * 
     * @param file file
     * 
     * @return number of documents deleted
     * 
     * @throws IOException IOException
     */
    public abstract void deleteDocument(IOFile file) throws IOException;

    /**
     * delete document(s)
     * 
     * @param title with title
     * 
     * @return number of documents deleted
     * 
     * @throws IOException IOException
     */
    public abstract void deleteDocument(String title) throws IOException;

    /**
     * delete document
     * 
     * @param url from url
     * 
     * @return number of documents deleted
     * 
     * @throws IOException IOException
     */
    public abstract void deleteDocument(URL url) throws IOException;

    /**
     * escape special characters
     * 
     * @param qword original searchword
     * 
     * @return escaped searchword
     */
    public abstract String escape(String qword);

    /**
     * explain query on document with index
     * 
     * @param query {@link Query}
     * @param doc index
     * 
     * @return {@link Explanation}
     * 
     * @throws IOException IOException
     */
    public abstract Explanation explain(Query query, int doc) throws IOException;

    /**
     * na
     * 
     * @param file na
     * 
     * @return
     * 
     * @throws IOException na
     */
    public Document findDocument(IOFile file) throws IOException;

    /**
     * na
     * 
     * @param url na
     * 
     * @return
     * 
     * @throws IOException na
     */
    public Document findDocument(URL url) throws IOException;

    /**
     * getDefaultMaxSearchResults
     * 
     * @return the defaultMaxSearchResults
     */
    public int getDefaultMaxSearchResults();

    /**
     * gets number of documents
     * 
     * @return number of documents
     * 
     * @throws IOException IOException
     */
    public abstract int getDocCount() throws IOException;

    /**
     * gets document with index
     * 
     * @param index index
     * 
     * @return {@link DocumentFactory}
     * 
     * @throws IOException IOException
     */
    public abstract Document getDocument(int index) throws IOException;

    /**
     * get names of fields sorted
     * 
     * @return SortedSet of fieldnames
     * 
     * @throws IOException IOException
     */
    public abstract SortedSet<String> getFieldNames() throws IOException;

    /**
     * gets index directory
     * 
     * @return IODirectory
     */
    public abstract IODirectory getIndexLocation();

    /**
     * gets language
     * 
     * @return Returns the language.
     */
    public abstract String getLanguage();

    /**
     * gets lock id
     * 
     * @return lock id
     * 
     * @throws IOException IOException
     */
    public abstract String getLockId() throws IOException;

    /**
     * gets lucene version string (from manifest in core jar)
     * 
     * @return lucene version string
     */
    public String getLuceneVersion();

    /**
     * gets maximum buffered documents before autoflush
     * 
     * @return maximum buffered documents (default 10)
     * 
     * @throws IOException IOException
     */
    public abstract int getMaxBufferedDocs() throws IOException;

    /**
     * gets max field length
     * 
     * @return max field length
     * 
     * @throws IOException IOException
     */
    public abstract int getMaxFieldLength() throws IOException;

    /**
     * gets merge factor
     * 
     * @return merge factor (default 10)
     * 
     * @throws IOException IOException
     */
    public abstract int getMergeFactor() throws IOException;

    /**
     * gets last modification date
     * 
     * @return Date
     */
    public abstract Date getModificationDate();

    /**
     * gets version
     * 
     * @return long
     * 
     * @throws IOException IOException
     */
    public abstract long getVersion() throws IOException;

    /**
     * gets version date
     * 
     * @return Date
     * 
     * @throws IOException IOException
     */
    public abstract Date getVersionDate() throws IOException;

    /**
     * indexes (or updates) a file
     * 
     * @param file {@link IOFile}
     * 
     * @return this
     * 
     * @throws IOException when something fails
     * @throws ConvertorNotSupportedException ConvertorNotSupportedException
     */
    public abstract LuceneInterface index(final IOFile file) throws IOException, ConvertorNotSupportedException;

    /**
     * indexes (or updates) a file
     * 
     * @param file {@link IOFile}
     * @param description description
     * 
     * @return this
     * 
     * @throws IOException when something fails
     * @throws ConvertorNotSupportedException ConvertorNotSupportedException
     */
    public abstract LuceneInterface index(final IOFile file, final String description, final String... keywords) throws IOException,
            ConvertorNotSupportedException;

    /**
     * indexes text (and saves it)
     * 
     * @param text the text (String[])
     * @param title id like the name of a file or location or an URL
     * @param description description
     * 
     * @return this
     * 
     * @throws IOException "description cannot be null"
     * @throws ConvertorNotSupportedException ConvertorNotSupportedException
     */
    public abstract LuceneInterface index(final String text, final String title, final String description, final String... keywords)
            throws IOException, ConvertorNotSupportedException;

    /**
     * indexes text from URL
     * 
     * @param url {@link URL} location
     * 
     * @return this
     * 
     * @throws ProtocolException ProtocolException
     * @throws ConvertorNotSupportedException ConvertorNotSupportedException
     * @throws IOException IOException
     */
    public abstract LuceneInterface index(final URL url) throws ProtocolException, ConvertorNotSupportedException, IOException;

    /**
     * indexes text from URL
     * 
     * @param url {@link URL} location
     * @param description description
     * 
     * @return this
     * 
     * @throws ProtocolException ProtocolException
     * @throws ConvertorNotSupportedException ConvertorNotSupportedException
     * @throws IOException IOException
     */
    public abstract LuceneInterface index(final URL url, final String description, final String... keywords) throws ProtocolException,
            ConvertorNotSupportedException, IOException;

    /**
     * Gets cacheText.
     * 
     * @return Returns the cacheText.
     */
    public abstract boolean isCacheText();

    /**
     * lists all files that are indexed as a sorted array
     * 
     * @return {@link URL}[]
     * 
     * @throws IOException IOException
     */
    public abstract List<LuceneDocument> listDocuments() throws IOException;

    /**
     * list info on all documents
     * 
     * @param out {@link PrintStream}
     * 
     * @return this
     * 
     * @throws IOException IOException
     */
    public abstract LuceneInterface listDocuments(PrintStream out) throws IOException;

    /**
     * move document (rename), will also reindex the document but all other info is kept as it is
     * 
     * @param file_from from
     * @param file_to to
     * 
     * @return this
     * 
     * @throws IOException IOException
     */
    public LuceneInterface moveDocument(IOFile file_from, IOFile file_to) throws IOException;

    /**
     * move document (rename), will also reindex the document but all other info is kept as it is
     * 
     * @param title_from from
     * @param title_to to
     * 
     * @return this
     * 
     * @throws IOException IOException
     */
    public LuceneInterface moveDocument(String title_from, String title_to) throws IOException;

    /**
     * move document (rename), will also reindex the document but all other info is kept as it is
     * 
     * @param url_from from
     * @param url_to to
     * 
     * @return this
     * 
     * @throws IOException IOException
     */
    public LuceneInterface moveDocument(URL url_from, URL url_to) throws IOException;

    /**
     * does the source needs indexing?
     * 
     * @param file file
     * 
     * @return boolean
     * 
     * @throws IOException IOException
     */
    public abstract boolean needsIndexing(final IOFile file) throws IOException;

    /**
     * does the source needs indexing?
     * 
     * @param url source on given url
     * 
     * @return boolean
     * 
     * @throws IOException IOException
     */
    public abstract boolean needsIndexing(final URL url) throws IOException;

    /**
     * optimizes index (can take a while)
     * 
     * @return this
     * 
     * @throws IOException IOException
     */
    public abstract LuceneInterface optimize() throws IOException;

    /**
     * searches index with given query
     * 
     * @param query query
     * 
     * @return {@link ResultList}
     * 
     * @throws IOException IOException
     * @throws ParseException ParseException
     */
    public abstract ResultList search(final String query) throws IOException, ParseException;

    /**
     * searches index with a single word (query is build internally)
     * 
     * @param word SINGLE word
     * 
     * @return {@link ResultList}
     * 
     * @throws IOException when something fails
     * @throws ParseException ParseException
     */
    public abstract ResultList searchSingleWord(final String word) throws IOException, ParseException;

    /**
     * Sets cacheText.
     * 
     * @param cacheText The cacheText to set.
     * 
     * @return this
     */
    public abstract LuceneInterface setCacheText(boolean cacheText);

    /**
     * setDefaultMaxSearchResults
     * 
     * @param defaultMaxSearchResults the defaultMaxSearchResults to set
     */
    public LuceneInterface setDefaultMaxSearchResults(int defaultMaxSearchResults);

    /**
     * will extract language from locale and choose correct analyzer
     * 
     * @param locale Locale
     * 
     * @return this
     * 
     * @throws IOException
     */
    public abstract LuceneInterface setLanguage(final Locale locale) throws IOException;

    /**
     * sets maximum buffered documents before autoflush
     * 
     * @param maxBufferedDocs maximum buffered documents (default 10)
     * 
     * @return this
     * 
     * @throws IOException IOException
     */
    public abstract LuceneInterface setMaxBufferedDocs(int maxBufferedDocs) throws IOException;

    /**
     * sets max field length
     * 
     * @param arg max field length
     * 
     * @return this
     * 
     * @throws IOException IOException
     */
    public abstract LuceneInterface setMaxFieldLength(int arg) throws IOException;

    /**
     * sets merge factor
     * 
     * @param arg merge factor (default 10)
     * 
     * @return this
     * 
     * @throws IOException IOException
     */
    public abstract LuceneInterface setMergeFactor(int arg) throws IOException;

    /**
     * statistical data, don't overuse
     * 
     * @param count maximum number of results
     * 
     * @return array of {@link DocHighScoreTerms}
     * 
     * @throws IOException IOException
     */
    public abstract DocHighScoreTerms[] topDocTerms(int count) throws IOException;

    /**
     * statistical data, don't overuse
     * 
     * @param count maximum number of results
     * @param filter field name filter (equality)
     * 
     * @return array of {@link DocHighScoreTerms}
     * 
     * @throws IOException IOException
     */
    public abstract DocHighScoreTerms[] topDocTerms(int count, String filter) throws IOException;

    /**
     * statistical data, don't overuse
     * 
     * @param count maximum number of results
     * 
     * @return array of {@link HighScoreTerms}
     * 
     * @throws IOException IOException
     */
    public abstract HighScoreTerms[] topTerms(int count) throws IOException;
}
