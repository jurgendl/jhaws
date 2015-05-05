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
 * newest LuceneInterface implementation
 */
public class LuceneIndexImpl implements LuceneInterface {
    /**
     * na
     * 
     * @param indexdir na
     * 
     * @return
     * 
     * @throws IOException na
     */
    public static LuceneInterface get(final IODirectory indexdir) throws IOException {
        return LuceneIndexImpl.get(indexdir, false);
    }

    /**
     * na
     * 
     * @param indexdir na
     * @param cleanFirst na
     * 
     * @return
     * 
     * @throws IOException na
     */
    public static LuceneInterface get(final IODirectory indexdir,  final boolean cleanFirst) throws IOException {
        return new LuceneIndexImpl(indexdir);
    }

    /** LuceneHelper */
    private final LuceneHelper helper;

    /** Locale.ENGLISH */
    private Locale language = Locale.ENGLISH;

    /** 10 */
    private int maxBufferedDocs = 10;

    /** 10000 */
    private int maxFieldLength = 10000;

    /** 10 */
    private int mergeFactor = 10;

    /**
     * Creates a new LuceneIndexImpl object.
     * 
     * @param ioDirectory
     * 
     * @throws IOException
     */
    protected LuceneIndexImpl(IODirectory ioDirectory) throws IOException {
        this.helper = new LuceneHelper(ioDirectory, this);
    }

    /**
     * 
     * @see org.jhaws.common.lucene.LuceneInterface#cleanUpNotExistingFileDocuments()
     */
    @Override
    public int cleanUpNotExistingFileDocuments() throws IOException {
        return this.helper.cleanUpNotExistingFileDocuments();
    }

    /**
     * 
     * @see org.jhaws.common.lucene.LuceneInterface#close()
     */
    @Override
    public void close() throws IOException {
        this.helper.close();
    }

    /**
     * 
     * @see org.jhaws.common.lucene.LuceneInterface#delete()
     */
    @Override
    public boolean delete() throws IOException {
        this.close();

        return this.helper.dir.erase();
    }

    /**
     * 
     * @see org.jhaws.common.lucene.LuceneInterface#deleteDocument(util.io.IOFile)
     */
    @Override
    public void deleteDocument(IOFile file) throws IOException {
        this.helper.deleteFile(file);
    }

    /**
     * 
     * @see org.jhaws.common.lucene.LuceneInterface#deleteDocument(java.lang.String)
     */
    @Override
    public void deleteDocument(String title) throws IOException {
        this.helper.deleteTitle(title);
    }

    /**
     * 
     * @see org.jhaws.common.lucene.LuceneInterface#deleteDocument(java.net.URL)
     */
    @Override
    public void deleteDocument(URL url) throws IOException {
        this.helper.deleteURL(url);
    }

    /**
     * 
     * @see org.jhaws.common.lucene.LuceneInterface#escape(java.lang.String)
     */
    @Override
    @SuppressWarnings("static-access")
    public String escape(String qword) {
        return this.helper.escapeText(qword);
    }

    /**
     * 
     * @see org.jhaws.common.lucene.LuceneInterface#explain(org.apache.lucene.search.Query, int)
     */
    @Override
    public Explanation explain(Query query, int doc) throws IOException {
        return this.helper.explain(query, doc);
    }

    /**
     * 
     * @see org.jhaws.common.lucene.LuceneInterface#findDocument(util.io.IOFile)
     */
    @Override
    public Document findDocument(IOFile file) throws IOException {
        return this.helper.searchFile(file);
    }

    /**
     * 
     * @see org.jhaws.common.lucene.LuceneInterface#findDocument(java.net.URL)
     */
    @Override
    public Document findDocument(URL url) throws IOException {
        return this.helper.searchURL(url);
    }

    /**
     * 
     * @see org.jhaws.common.lucene.LuceneInterface#getDefaultMaxSearchResults()
     */
    @Override
    public int getDefaultMaxSearchResults() {
        return this.helper.getDefaultMaxSearchResults();
    }

    /**
     * 
     * @see org.jhaws.common.lucene.LuceneInterface#getDocCount()
     */
    @Override
    public int getDocCount() throws IOException {
        return this.helper.count();
    }

    /**
     * 
     * @see org.jhaws.common.lucene.LuceneInterface#getDocument(int)
     */
    @Override
    public Document getDocument(int index) throws IOException {
        return this.helper.getDocument(index);
    }

    /**
     * 
     * @see org.jhaws.common.lucene.LuceneInterface#getFieldNames()
     */
    @Override
    public SortedSet<String> getFieldNames() throws IOException {
        return this.helper.getFieldNames();
    }

    /**
     * gets helper
     * 
     * @return the helper
     */
    public LuceneHelper getHelper() {
        return this.helper;
    }

    /**
     * 
     * @see org.jhaws.common.lucene.LuceneInterface#getIndexLocation()
     */
    @Override
    public IODirectory getIndexLocation() {
        return this.helper.dir;
    }

    /**
     * 
     * @see org.jhaws.common.lucene.LuceneInterface#getLanguage()
     */
    @Override
    public String getLanguage() {
        return this.language.getLanguage();
    }

    /**
     * 
     * @see org.jhaws.common.lucene.LuceneInterface#getLockId()
     */
    @Override
    public String getLockId() throws IOException {
        return this.helper.getLockId();
    }

    /**
     * 
     * @see org.jhaws.common.lucene.LuceneInterface#getLuceneVersion()
     */
    @Override
    public String getLuceneVersion() {
        return this.helper.getLuceneVersion();
    }

    /**
     * 
     * @see org.jhaws.common.lucene.LuceneInterface#getMaxBufferedDocs()
     */
    @Override
    public int getMaxBufferedDocs() throws IOException {
        return this.maxBufferedDocs;
    }

    /**
     * 
     * @see org.jhaws.common.lucene.LuceneInterface#getMaxFieldLength()
     */
    @Override
    public int getMaxFieldLength() throws IOException {
        return this.maxFieldLength;
    }

    /**
     * 
     * @see org.jhaws.common.lucene.LuceneInterface#getMergeFactor()
     */
    @Override
    public int getMergeFactor() throws IOException {
        return this.mergeFactor;
    }

    /**
     * 
     * @see org.jhaws.common.lucene.LuceneInterface#getModificationDate()
     */
    @Override
    public Date getModificationDate() {
        return this.helper.getModificationDate();
    }

    /**
     * 
     * @see org.jhaws.common.lucene.LuceneInterface#getVersion()
     */
    @Override
    public long getVersion() throws IOException {
        return this.helper.getVersion();
    }

    /**
     * 
     * @see org.jhaws.common.lucene.LuceneInterface#getVersionDate()
     */
    @Override
    public Date getVersionDate() throws IOException {
        return this.helper.getVersionDate();
    }

    /**
     * 
     * @see org.jhaws.common.lucene.LuceneInterface#index(util.io.IOFile)
     */
    @Override
    public LuceneInterface index(IOFile file) throws IOException, ConvertorNotSupportedException {
        return this.index(file, (String) null, (String[]) null);
    }

    /**
     * 
     * @see org.jhaws.common.lucene.LuceneInterface#index(util.io.IOFile, java.lang.String, java.lang.String[])
     */
    @Override
    public LuceneInterface index(IOFile file, String description, String... keywords) throws IOException, ConvertorNotSupportedException {
        this.helper.addFile(file, keywords, description);

        return this;
    }

    /**
     * 
     * @see org.jhaws.common.lucene.LuceneInterface#index(java.lang.String, java.lang.String, java.lang.String, java.lang.String[])
     */
    @Override
    public LuceneInterface index(String text, String title, String description, String... keywords) throws IOException,
            ConvertorNotSupportedException {
        this.helper.addTitle(title, keywords, description, text);

        return this;
    }

    /**
     * 
     * @see org.jhaws.common.lucene.LuceneInterface#index(java.net.URL)
     */
    @Override
    public LuceneInterface index(URL url) throws ProtocolException, ConvertorNotSupportedException, IOException {
        return this.index(url, (String) null, (String[]) null);
    }

    /**
     * 
     * @see org.jhaws.common.lucene.LuceneInterface#index(java.net.URL, java.lang.String, java.lang.String[])
     */
    @Override
    public LuceneInterface index(URL url, String description, String... keywords) throws ProtocolException, ConvertorNotSupportedException,
            IOException {
        this.helper.addURL(url, keywords, description);

        return this;
    }

    /**
     * 
     * @see org.jhaws.common.lucene.LuceneInterface#isCacheText()
     */
    @Override
    public boolean isCacheText() {
        return this.helper.isCacheText();
    }

    /**
     * 
     * @see org.jhaws.common.lucene.LuceneInterface#listDocuments()
     */
    @Override
    public List<LuceneDocument> listDocuments() throws IOException {
        return this.helper.listDocuments();
    }

    /**
     * 
     * @see org.jhaws.common.lucene.LuceneInterface#listDocuments(java.io.PrintStream)
     */
    @Override
    public LuceneInterface listDocuments(PrintStream out) throws IOException {
        for (int i = 0; i < this.getDocCount(); i++) {
            Document d = this.getDocument(i);

            for (Object o : d.getFields()) {
                Field f = (Field) o;

                out.println(d.getField(f.name()));
            }

            out.println();
        }

        return this;
    }

    /**
     * 
     * @see org.jhaws.common.lucene.LuceneInterface#moveDocument(util.io.IOFile, util.io.IOFile)
     */
    @Override
    public LuceneInterface moveDocument(IOFile fileFrom, IOFile fileTo) throws IOException {
        this.helper.moveFile(fileFrom, fileTo);

        return this;
    }

    /**
     * 
     * @see org.jhaws.common.lucene.LuceneInterface#moveDocument(java.lang.String, java.lang.String)
     */
    @Override
    public LuceneInterface moveDocument(String titleFrom, String titleTo) throws IOException {
        this.helper.moveTitle(titleFrom, titleTo);

        return this;
    }

    /**
     * 
     * @see org.jhaws.common.lucene.LuceneInterface#moveDocument(java.net.URL, java.net.URL)
     */
    @Override
    public LuceneInterface moveDocument(URL urlFrom, URL urlTo) throws IOException {
        this.helper.moveURL(urlFrom, urlTo);

        return this;
    }

    /**
     * 
     * @see org.jhaws.common.lucene.LuceneInterface#needsIndexing(util.io.IOFile)
     */
    @Override
    public boolean needsIndexing(IOFile file) throws IOException {
        return this.helper.fileNeedsIndexing(file);
    }

    /**
     * 
     * @see org.jhaws.common.lucene.LuceneInterface#needsIndexing(java.net.URL)
     */
    @Override
    public boolean needsIndexing(URL url) throws IOException {
        return this.helper.urlNeedsIndexing(url);
    }

    /**
     * 
     * @see org.jhaws.common.lucene.LuceneInterface#optimize()
     */
    @Override
    public LuceneInterface optimize() throws IOException {
        this.helper.optimize();

        return this;
    }

    /**
     * 
     * @see org.jhaws.common.lucene.LuceneInterface#search(java.lang.String)
     */
    @Override
    public ResultList search(String query) throws IOException, ParseException {
        return this.helper.searchWithQuery(query);
    }

    /**
     * 
     * @see org.jhaws.common.lucene.LuceneInterface#searchSingleWord(java.lang.String)
     */
    @Override
    public ResultList searchSingleWord(String word) throws IOException, ParseException {
        return this.helper.searchWithWord(word);
    }

    /**
     * 
     * @see org.jhaws.common.lucene.LuceneInterface#setCacheText(boolean)
     */
    @Override
    public LuceneInterface setCacheText(boolean cacheText) {
        this.helper.setCacheText(cacheText);

        return this;
    }

    /**
     * 
     * @see org.jhaws.common.lucene.LuceneInterface#setDefaultMaxSearchResults(int)
     */
    @Override
    public LuceneInterface setDefaultMaxSearchResults(int defaultMaxSearchResults) {
        this.helper.setDefaultMaxSearchResults(defaultMaxSearchResults);

        return this;
    }

    /**
     * 
     * @see org.jhaws.common.lucene.LuceneInterface#setLanguage(java.util.Locale)
     */
    @Override
    public LuceneInterface setLanguage(Locale language) throws IOException {
        this.language = language;

        return this;
    }

    /**
     * 
     * @see org.jhaws.common.lucene.LuceneInterface#setMaxBufferedDocs(int)
     */
    @Override
    public LuceneInterface setMaxBufferedDocs(int maxBufferedDocs) throws IOException {
        this.maxBufferedDocs = maxBufferedDocs;

        return this;
    }

    /**
     * 
     * @see org.jhaws.common.lucene.LuceneInterface#setMaxFieldLength(int)
     */
    @Override
    public LuceneInterface setMaxFieldLength(int maxFieldLength) throws IOException {
        this.maxFieldLength = maxFieldLength;

        return this;
    }

    /**
     * 
     * @see org.jhaws.common.lucene.LuceneInterface#setMergeFactor(int)
     */
    @Override
    public LuceneInterface setMergeFactor(int mergeFactor) throws IOException {
        this.mergeFactor = mergeFactor;

        return this;
    }

    /**
     * 
     * @see org.jhaws.common.lucene.LuceneInterface#topDocTerms(int)
     */
    @Override
    public DocHighScoreTerms[] topDocTerms(int count) throws IOException {
        return this.helper.topDocTerms(count);
    }

    /**
     * 
     * @see org.jhaws.common.lucene.LuceneInterface#topDocTerms(int, java.lang.String)
     */
    @Override
    public DocHighScoreTerms[] topDocTerms(int count, String filter) throws IOException {
        return this.helper.topDocTerms(count, filter);
    }

    /**
     * 
     * @see org.jhaws.common.lucene.LuceneInterface#topTerms(int)
     */
    @Override
    public HighScoreTerms[] topTerms(int count) throws IOException {
        return this.helper.topTerms(count);
    }
}
