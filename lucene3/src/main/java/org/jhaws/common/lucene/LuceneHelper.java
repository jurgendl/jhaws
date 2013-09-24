package org.jhaws.common.lucene;

import static org.apache.lucene.document.DateTools.Resolution.MILLISECOND;
import static org.apache.lucene.document.Field.Index.ANALYZED;
import static org.apache.lucene.document.Field.Index.NOT_ANALYZED;
import static org.apache.lucene.document.Field.TermVector.WITH_POSITIONS_OFFSETS;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.net.URLStreamHandler;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

import org.apache.lucene.document.DateTools;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.index.FieldInfo;
import org.apache.lucene.index.FieldInfos;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriter.MaxFieldLength;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.TermDocs;
import org.apache.lucene.index.TermEnum;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.Explanation;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Searcher;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.jhaws.common.io.IODirectory;
import org.jhaws.common.io.IOFile;
import org.jhaws.common.lucene.stat.DocHighScoreTerms;
import org.jhaws.common.lucene.stat.HighScoreTerms;

/**
 * na
 * 
 * @see http://lingpipe-blog.com/2008/11/05/updating-and-deleting-documents-in-lucene-24-lingmed-case-study/
 */
public class LuceneHelper {
    /**
     * internally used
     * 
     * @author Jurgen
     */
    protected static class BreakLoopException extends Exception {
        /** serialVersionUID */
        private static final long serialVersionUID = 8105811033552521805L;
    }

    /** {@link URL} "doc://..." handler */
    public static final URLStreamHandler DOC_HANDLER = new URLStreamHandler() {
        @Override
        protected URLConnection openConnection(URL u) throws IOException {
            return null;
        }

        @SuppressWarnings("unused")
        public void test() throws IOException {
            URL u = new URL("doc", "localhost", 0, "/file", LuceneHelper.DOC_HANDLER);
            System.out.println(u);
            System.out.println(new URL("doc://file"));
        }
    };

    /** "doc://localhost:0/" */
    protected static final String DOC_URL_PREFIX = "doc://localhost:0/";

    /** excape characters +-&|!(){}[]^~?:\" */
    public static final char[] ESCAPE_CHARACTERS = "+-&|!(){}[]^~*?:\\\"".toCharArray();

    /** segments.gen exists means the indexfiles exist */
    protected static final String segments = "segments.gen";

    static {
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                for (LuceneHelper helper : LuceneHelper.instances.keySet()) {
                    try {
                        helper.close();
                    } catch (Exception ex) {
                        //
                    }
                }
            }
        }));
    }

    /** instances */
    protected static HashMap<LuceneHelper, Boolean> instances = new HashMap<LuceneHelper, Boolean>();

    /**
     * convert date to string
     * 
     * @param date
     * 
     * @return
     */
    protected static String convertdate(Date date) {
        return DateTools.timeToString(date.getTime(), MILLISECOND);
    }

    /**
     * convert string to date
     * 
     * @param value
     * 
     * @return
     */
    protected static Date convertdate(String value) throws java.text.ParseException {
        return DateTools.stringToDate(value);
    }

    /**
     * na
     * 
     * @param string
     * 
     * @return
     */
    protected static String escapeText(String string) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < string.length(); i++) {
            char c = string.charAt(i);

            for (char element : LuceneHelper.ESCAPE_CHARACTERS) {
                if (c == element) {
                    sb.append("\\");
                }
            }

            sb.append(c);
        }

        String escaped = sb.toString();

        return escaped;
    }

    /**
     * converts keywords to single string
     * 
     * @param keywords keywords as array
     * 
     * @return keywords as string
     */
    protected static String keywordsToString(final String[] keywords) {
        StringBuilder sb = new StringBuilder();

        if ((keywords != null) && (keywords.length > 0)) {
            for (int i = 0; i < keywords.length; i++) {
                sb.append(keywords[i]);

                if (i < (keywords.length - 1)) {
                    sb.append(" ");
                }
            }
        }

        return sb.toString();
    }

    /**
     * na
     * 
     * @param is
     * 
     * @return
     * 
     * @throws IOException
     */
    protected static byte[] readFromStream(final InputStream is) throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        byte[] buffer = new byte[8192];
        int read = is.read(buffer);

        while (read != -1) {
            os.write(buffer, 0, read);
            read = is.read(buffer);
        }

        is.close();
        os.close();

        return os.toByteArray();
    }

    /** directory */
    protected final Directory directory;

    /** analyzer */
    protected Analyzer analyzer = Analyzer.fetch(Locale.getDefault());

    /** ioDirectory */
    protected final IODirectory dir;

    /** LuceneInterface */
    protected final LuceneInterface myself;

    /** _IndexReader */
    protected IndexReader indexReader;

    /** _IndexReaderRO */
    protected IndexReader readOnlyIndexReader;

    /** _IndexSearcher */
    protected IndexSearcher indexSearcher;

    /** _IndexWriter */
    protected IndexWriter indexWriter;

    /** luceneVersion */
    protected String luceneVersion;

    /** false */
    protected boolean cacheText = false;

    /** 100 */
    protected int defaultMaxSearchResults = 100;

    /**
     * Creates a new LuceneHelper object.
     * 
     * @param ioDirectory
     * 
     * @throws IOException
     */
    public LuceneHelper(IODirectory ioDirectory, LuceneInterface luceneInterface) throws IOException {
        this.dir = ioDirectory;
        this.myself = luceneInterface;
        this.directory = FSDirectory.open(ioDirectory);
        LuceneHelper.instances.put(this, Boolean.TRUE);
    }

    /**
     * na
     * 
     * @param document
     * 
     * @return
     * 
     * @throws IOException
     */
    protected Document add(Document document) throws IOException {
        IndexWriter writer = this.getIndexWriter();
        writer.addDocument(document);
        writer.commit();

        // writer.close(true);
        return document;
    }

    /**
     * addFile
     * 
     * @param file
     * 
     * @return
     * 
     * @throws IOException
     */
    public Document addFile(IOFile file) throws IOException {
        return this.addFile(file, null, null);
    }

    /**
     * addFile
     * 
     * @param file
     * @param keywords
     * @param description
     * 
     * @return
     * 
     * @throws IOException
     */
    public Document addFile(IOFile file, String[] keywords, String description) throws IOException {
        return this.add(this.newDocumentFromFile(file, keywords, description));
    }

    /**
     * addTitle
     * 
     * @param title
     * @param text
     * 
     * @return
     * 
     * @throws IOException
     */
    public Document addTitle(String title, String text) throws IOException {
        return this.addTitle(title, null, null, text);
    }

    /**
     * addTitle
     * 
     * @param title
     * @param keywords
     * @param description
     * @param text
     * 
     * @return
     * 
     * @throws IOException
     */
    public Document addTitle(String title, String[] keywords, String description, String text) throws IOException {
        return this.add(this.newDocumentFromTitle(title, keywords, description, text));
    }

    /**
     * addURL
     * 
     * @param url
     * 
     * @return
     * 
     * @throws IOException
     */
    public Document addURL(URL url) throws IOException {
        return this.addURL(url, null, null);
    }

    /**
     * addURL
     * 
     * @param url
     * @param keywords
     * @param description
     * 
     * @return
     * 
     * @throws IOException
     */
    public Document addURL(URL url, String[] keywords, String description) throws IOException {
        return this.add(this.newDocumentFromUrl(url, keywords, description));
    }

    /**
     * na
     * 
     * @return
     * 
     * @throws IOException
     */
    public ResultList all() throws IOException {
        IndexReader reader = this.getReadOnlyIndexReader();
        ResultList list = new ResultList(this.myself, null, null, null, reader.maxDoc());

        for (int i = 0; i < reader.maxDoc(); i++) {
            Document doc = reader.document(i);
            list.addSearchResult(new SearchResult(i, list, doc, i, 0, this.myself));
        }

        // reader.close();
        return list;
    }

    /**
     * cleanUpNotExistingFileDocuments
     * 
     * @return
     * 
     * @throws IOException
     */
    public int cleanUpNotExistingFileDocuments() throws IOException {
        IndexSearcher searcher0 = this.getIndexSearcher();
        ArrayList<String> toDelete = new ArrayList<String>();

        for (int i = 0; i < searcher0.maxDoc(); i++) {
            try {
                Document doc = searcher0.doc(i);

                if ((doc.getField(ID.text.f()) == null) || !doc.getField(ID.text.f()).isStored()) {
                    String url = doc.get(ID.url.f());

                    try {
                        URL u = new URL(url);

                        if (u.getProtocol().equals("file")) {
                            if (!new IOFile(URLDecoder.decode(u.getFile(), "UTF-8")).exists()) {
                                toDelete.add(url);
                            }
                        }
                    } catch (Exception exx) {
                        exx.printStackTrace();
                    }
                }
            } catch (Exception exxx) {
                exxx.printStackTrace();
            }
        }

        for (String id : toDelete) {
            this.deleteById(id);
        }

        this.optimize();

        return toDelete.size();
    }

    /**
     * close all
     * 
     * @return
     * 
     * @throws IOException
     */
    public boolean close() throws IOException {
        this.closeReader();
        this.closeReadOnlyReader();
        this.closeSearcher();
        this.closeWriter();

        LuceneHelper.instances.put(this, Boolean.FALSE);

        return this.dir.canRead() && this.dir.canWrite();
    }

    /**
     * na
     * 
     * @return
     * 
     * @throws IOException
     */
    protected IndexReader closeReader() throws IOException {
        if (this.indexReader == null) {
            return null;
        }

        this.indexReader.close();

        IndexReader tmp = this.indexReader;
        this.indexReader = null;

        return tmp;
    }

    /**
     * na
     * 
     * @return
     * 
     * @throws IOException
     */
    protected IndexReader closeReadOnlyReader() throws IOException {
        if (this.readOnlyIndexReader == null) {
            return null;
        }

        this.readOnlyIndexReader.close();

        IndexReader tmp = this.readOnlyIndexReader;
        this.readOnlyIndexReader = null;

        return tmp;
    }

    /**
     * na
     * 
     * @throws IOException
     */
    protected void closeSearcher() throws IOException {
        if (this.indexSearcher == null) {
            return;
        }

        this.indexSearcher.close();
        this.indexSearcher = null;
    }

    /**
     * na
     * 
     * @throws IOException
     */
    protected void closeWriter() throws IOException {
        if (this.indexWriter == null) {
            return;
        }

        this.indexWriter.close();
        this.indexWriter = null;
    }

    /**
     * na
     * 
     * @return
     * 
     * @throws IOException
     */
    public int count() throws IOException {
        IndexReader reader = this.getReadOnlyIndexReader();
        int count = reader.maxDoc();

        // reader.close();
        return count;
    }

    /**
     * debug disk files
     */
    protected void debug() {
        for (IOFile o : this.dir.listIOFiles()) {
            System.out.println(o.getLength() + " // " + o.lastModified() + " // " + o);
        }
    }

    /**
     * closes and delete
     * 
     * @return
     * 
     * @throws IOException
     */
    public boolean delete() throws IOException {
        this.close();
        this.dir.erase();

        return this.dir.exists();
    }

    /**
     * na
     * 
     * @param id
     * 
     * @return
     * 
     * @throws IOException
     */
    protected int deleteById(String id) throws IOException {
        // IndexReader reader = internal_getIndexReader_writable();
        // reader.deleteDocument(internal_find_record_result(id).id);
        //
        // //reader.close();
        IndexWriter writer = this.getIndexWriter();
        writer.deleteDocuments(new TermQuery(new Term(ID.url.f(), id)));
        writer.commit();
        writer.expungeDeletes(true);
        writer.commit();

        // writer.close(true);
        return 1;
    }

    /**
     * na
     * 
     * @param file
     * 
     * @return
     * 
     * @throws IOException
     */
    public int deleteFile(IOFile file) throws IOException {
        return this.deleteById(this.fileToId(file));
    }

    /**
     * na
     * 
     * @param title
     * 
     * @return
     * 
     * @throws IOException
     */
    public int deleteTitle(String title) throws IOException {
        return this.deleteById(this.titleToId(title));
    }

    /**
     * na
     * 
     * @param url
     * 
     * @return
     * 
     * @throws IOException
     */
    public int deleteURL(URL url) throws IOException {
        return this.deleteById(this.urlToId(url));
    }

    /**
     * explain
     * 
     * @param query
     * @param doc
     * 
     * @return
     * 
     * @throws IOException
     */
    public Explanation explain(Query query, int doc) throws IOException {
        return this.getIndexSearcher().explain(query, doc);
    }

    /**
     * urlNeedsIndexing
     * 
     * @param file
     * 
     * @return
     * 
     * @throws IOException
     * @throws RuntimeException
     */
    public boolean fileNeedsIndexing(IOFile file) throws IOException {
        Document exists = this.findDocumentById(this.fileToId(file));

        if (exists == null) {
            return false;
        }

        long lastModified = file.lastModified();
        String lastModInIndexString = exists.get(ID.lastmod.f());
        long lastModInIndex;

        try {
            lastModInIndex = LuceneHelper.convertdate(lastModInIndexString).getTime();
        } catch (java.text.ParseException ex) {
            throw new RuntimeException(ex);
        }

        return (lastModInIndex < lastModified);
    }

    /**
     * file to string id
     * 
     * @param file
     * 
     * @return
     * 
     * @throws IOException
     */
    protected String fileToId(IOFile file) throws IOException {
        return file.toURI().toURL().toString();
    }

    /**
     * na
     * 
     * @param id
     * 
     * @return
     * 
     * @throws IOException
     */
    protected SearchResult findById(String id) throws IOException {
        Searcher searcher = this.getIndexSearcher();
        Term term = new Term(ID.url.f(), id);
        TermQuery query = new TermQuery(term);
        TopDocs topDocs = searcher.search(query, 2);

        if (topDocs.totalHits == 0) {
            return null;
        }

        if (topDocs.totalHits > 1) {
            throw new IOException();
        }

        Document doc = searcher.doc(topDocs.scoreDocs[0].doc);

        return new SearchResult(0, (ResultList) null, doc, topDocs.scoreDocs[0].doc, 1.0f, this.myself);
    }

    /**
     * na
     * 
     * @param id
     * 
     * @return
     * 
     * @throws IOException
     */
    protected Document findDocumentById(String id) throws IOException {
        SearchResult results = this.findById(id);

        return (results == null) ? null : results.getLuceneDocument();
    }

    /**
     * na
     * 
     * @return
     */
    protected Analyzer getAnalyzer() {
        return this.analyzer;
    }

    /**
     * getDefaultMaxSearchResults
     * 
     * @return the defaultMaxSearchResults
     */
    public int getDefaultMaxSearchResults() {
        return this.defaultMaxSearchResults;
    }

    /**
     * getDocument
     * 
     * @param index
     * 
     * @return
     * 
     * @throws IOException
     */
    public Document getDocument(int index) throws IOException {
        return this.getReadOnlyIndexReader().document(index);
    }

    /**
     * getFieldNames
     * 
     * @return
     * 
     * @throws IOException
     */
    public SortedSet<String> getFieldNames() throws IOException {
        try {
            IndexReader readOnlyIndexReader2 = this.getReadOnlyIndexReader();
            SortedSet<String> set = new TreeSet<String>();
            FieldInfos finfo = readOnlyIndexReader2.getFieldInfos();
            Iterator<FieldInfo> iterator = finfo.iterator();
            while (iterator.hasNext()) {
                FieldInfo fieldInfo = iterator.next();
                set.add(fieldInfo.name);
            }

            return set;
        } catch (FileNotFoundException ex) {
            return null;
        }
    }

    /**
     * na
     * 
     * @return
     * 
     * @throws IOException
     */
    protected IndexReader getIndexReader() throws IOException {
        if (this.indexReader == null) {
            this.indexReader = IndexReader.open(this.directory, false);
        } else if (!this.indexReader.isCurrent()) {
            throw new IOException("can this be fixed");
        } else if (this.indexReader.hasDeletions()) {
            throw new IOException("can this be fixed");
        }

        return this.indexReader;
    }

    /**
     * na
     * 
     * @return
     * 
     * @throws IOException
     */
    protected IndexSearcher getIndexSearcher() throws IOException {
        if ((this.indexSearcher == null) || (this.readOnlyIndexReader == null)) {
            this.indexSearcher = new IndexSearcher(this.getReadOnlyIndexReader());
        } else {
            if (!this.readOnlyIndexReader.isCurrent()) {
                this.readOnlyIndexReader = this.readOnlyIndexReader.reopen();
            } else if (this.readOnlyIndexReader.hasDeletions()) {
                throw new IOException("can this be fixed");
            }

            this.indexSearcher = new IndexSearcher(this.readOnlyIndexReader);
        }

        return this.indexSearcher;
    }

    /**
     * na
     * 
     * @return
     * 
     * @throws IOException
     */
    protected IndexWriter getIndexWriter() throws IOException {
        if (this.indexWriter == null) {
            this.indexWriter = new IndexWriter(this.directory, this.getAnalyzer().analyzer(), this.directory.listAll().length == 0,
                    MaxFieldLength.LIMITED);
        }

        return this.indexWriter;
    }

    /**
     * getLockId
     * 
     * @return
     * 
     * @throws IOException
     */
    public String getLockId() throws IOException {
        try {
            return this.getReadOnlyIndexReader().directory().getLockID();
        } catch (FileNotFoundException ex) {
            return null;
        }
    }

    /**
     * getLuceneVersion
     * 
     * @return
     */
    public String getLuceneVersion() {
        if (this.luceneVersion == null) {
            try {
                URL loc = org.apache.lucene.index.IndexWriter.class.getClassLoader().getResource("org/apache/lucene/index/IndexWriter.class");
                String jar = URLDecoder.decode(loc.toString(), "UTF-8");
                jar = jar.substring("jar:file:".length(), jar.length() - "!/org/apache/lucene/index/IndexWriter.class".length());

                JarFile jarf = new JarFile(jar);
                Manifest manifest = jarf.getManifest();
                Attributes atts = manifest.getMainAttributes();

                this.luceneVersion = atts.getValue("Implementation-Version");

                if (this.luceneVersion == null) {
                    this.luceneVersion = atts.getValue("Specification-Version");
                }

                if (this.luceneVersion == null) {
                    this.luceneVersion = "Unknown";
                }
            } catch (Exception ex) {
                ex.printStackTrace(System.out);
                this.luceneVersion = "Unknown";
            }
        }

        return this.luceneVersion;
    }

    /**
     * getModificationDate
     * 
     * @return
     */
    public Date getModificationDate() {
        return new Date(new IOFile(this.dir, LuceneHelper.segments).lastModified());
    }

    /**
     * na
     * 
     * @return
     * 
     * @throws IOException
     */
    protected IndexReader getReadOnlyIndexReader() throws IOException {
        if (this.readOnlyIndexReader == null) {
            this.readOnlyIndexReader = IndexReader.open(this.directory, true);
        } else {
            if (!this.readOnlyIndexReader.isCurrent()) {
                this.readOnlyIndexReader = this.readOnlyIndexReader.reopen();
            } else if (this.readOnlyIndexReader.hasDeletions()) {
                throw new IOException("can this be fixed");
            }
        }

        return this.readOnlyIndexReader;
    }

    /**
     * getVersion
     * 
     * @return
     * 
     * @throws IOException
     */
    public Long getVersion() throws IOException {
        try {
            return this.getReadOnlyIndexReader().getVersion();
        } catch (FileNotFoundException ex) {
            return null;
        }
    }

    /**
     * getVersionDate
     * 
     * @return
     * 
     * @throws IOException
     */
    public Date getVersionDate() throws IOException {
        Long version = this.getVersion();

        return (version == null) ? null : new Date(version);
    }

    /**
     * isCacheText
     * 
     * @return the cacheText
     */
    public boolean isCacheText() {
        return this.cacheText;
    }

    /**
     * listDocuments
     * 
     * @return
     * 
     * @throws IOException
     */
    public List<LuceneDocument> listDocuments() throws IOException {
        List<LuceneDocument> list = new ArrayList<LuceneDocument>();

        IndexReader internalGetIndexReaderReadOnly = this.getReadOnlyIndexReader();

        for (int i = 0; i < internalGetIndexReaderReadOnly.maxDoc(); i++) {
            Document document = internalGetIndexReaderReadOnly.document(i);
            LuceneDocument ld = new LuceneDocument(document, this.myself);
            list.add(ld);
        }

        return list;
    }

    /**
     * move
     * 
     * @param from
     * @param to
     * 
     * @return
     * 
     * @throws IOException
     */
    public Document moveFile(IOFile from, IOFile to) throws IOException {
        Document doc = this.searchFile(from);
        this.deleteFile(from);

        doc.getField(ID.url.f()).setValue(this.fileToId(to));
        doc.getField(ID.lastmod.f()).setValue(LuceneHelper.convertdate(new Date(to.lastModified())));

        return this.add(doc);
    }

    /**
     * move
     * 
     * @param from
     * @param to
     * 
     * @return
     * 
     * @throws IOException
     */
    public Document moveTitle(String from, String to) throws IOException {
        Document doc = this.searchTitle(from);
        this.deleteTitle(from);

        doc.getField(ID.url.f()).setValue(this.titleToId(from));
        doc.getField(ID.lastmod.f()).setValue(LuceneHelper.convertdate(new Date()));

        return this.add(doc);
    }

    /**
     * move
     * 
     * @param from
     * @param to
     * 
     * @return
     * 
     * @throws IOException
     */
    public Document moveURL(URL from, URL to) throws IOException {
        Document doc = this.searchURL(from);
        this.deleteURL(from);

        doc.getField(ID.url.f()).setValue(this.urlToId(from));
        doc.getField(ID.lastmod.f()).setValue(LuceneHelper.convertdate(new Date(to.openConnection().getLastModified())));

        return this.add(doc);
    }

    /**
     * na
     * 
     * @param type
     * @param id
     * @param size
     * @param keywords
     * @param description
     * @param lastmod
     * @param text
     * 
     * @return
     * 
     * @throws IOException
     */
    protected Document newDocument(FileType type, String id, long size, String[] keywords, String description, Date lastmod, String text)
            throws IOException {
        Document doc = new Document();

        doc.add(new Field(ID.analyzer.f(), ID.analyzer.f(), Store.YES, NOT_ANALYZED));
        doc.add(new Field(ID.url.f(), id, Store.YES, NOT_ANALYZED));
        doc.add(new Field(ID.lastmod.f(), LuceneHelper.convertdate(lastmod), Store.YES, NOT_ANALYZED)); // mod_date:[20020101 TO 20030101]
        doc.add(new Field(ID.description.f(), (description == null) ? "" : description, Store.YES, ANALYZED));
        doc.add(new Field(ID.keywords.f(), LuceneHelper.keywordsToString(keywords), Store.YES, ANALYZED));
        doc.add(new Field(ID.size.f(), String.valueOf(size), Store.YES, NOT_ANALYZED));
        doc.add(new Field(ID.language.f(), this.getAnalyzer().language(), Store.YES, NOT_ANALYZED));
        doc.add(new Field(ID.filetype.f(), type.toString(), Store.YES, NOT_ANALYZED));
        doc.add(new Field(ID.text.f(), text, ((type == FileType.TEXT) || (type == FileType.URL) || this.isCacheText()) ? Store.YES : Store.NO,
                ANALYZED, WITH_POSITIONS_OFFSETS));

        return doc;
    }

    /**
     * na
     * 
     * @param file
     * @param keywords
     * @param description
     * 
     * @return
     * 
     * @throws IOException
     */
    protected Document newDocumentFromFile(IOFile file, String[] keywords, String description) throws IOException {
        String text = new String(DocumentFactory.getConvertor(file).getText(file));
        String id = this.fileToId(file);

        return this.newDocument(FileType.FILE, id, file.length(), keywords, description, new Date(file.lastModified()), text);
    }

    /**
     * na
     * 
     * @param title
     * @param keywords
     * @param description
     * @param text
     * 
     * @return
     * 
     * @throws IOException
     */
    protected Document newDocumentFromTitle(String title, String[] keywords, String description, String text) throws IOException {
        return this.newDocument(FileType.TEXT, this.titleToId(title), text.length(), keywords, description, new Date(), text);
    }

    /**
     * na
     * 
     * @param url
     * @param keywords
     * @param description
     * 
     * @return
     * 
     * @throws IOException
     */
    protected Document newDocumentFromUrl(URL url, String[] keywords, String description) throws IOException {
        URLConnection connection = url.openConnection();
        String id = this.urlToId(url);
        String text = new String(LuceneHelper.readFromStream(connection.getInputStream()));

        return this.newDocument(FileType.URL, id, connection.getContentLength(), keywords, description, new Date(connection.getLastModified()), text);
    }

    /**
     * na
     * 
     * @throws IOException
     */
    public void optimize() throws IOException {
        IndexWriter writer = this.getIndexWriter();
        writer.optimize();

        // writer.close(true);
    }

    /**
     * na
     * 
     * @param max
     * @param find
     * 
     * @return
     * 
     * @throws IOException
     * @throws ParseException
     */
    public ResultList query(Integer max, String find) throws IOException, ParseException {
        Searcher searcher = this.getIndexSearcher();
        QueryParser parser = new QueryParser(Version.LUCENE_36, "text", this.getAnalyzer().analyzer());
        Query query = parser.parse(find);

        if (max == null) {
            max = this.getDefaultMaxSearchResults();
        }

        TopScoreDocCollector collector = TopScoreDocCollector.create(max, false);
        searcher.search(query, collector);

        ScoreDoc[] hits = collector.topDocs().scoreDocs;
        int numTotalHits = collector.getTotalHits();
        ResultList list = new ResultList(this.myself, find, find, query, numTotalHits);

        for (int j = 0; j < hits.length; j++) {
            Document doc = searcher.doc(hits[j].doc);
            list.addSearchResult(new SearchResult(j, list, doc, hits[j].doc, hits[j].score, this.myself));
        }

        return list;
    }

    /**
     * na
     * 
     * @param find
     * 
     * @return
     * 
     * @throws IOException
     * @throws ParseException
     */
    public ResultList query(String find) throws IOException, ParseException {
        return this.query(null, find);
    }

    /**
     * na
     * 
     * @param file
     * 
     * @return
     * 
     * @throws IOException
     */
    public Document searchFile(IOFile file) throws IOException {
        return this.findDocumentById(this.fileToId(file));
    }

    /**
     * na
     * 
     * @param title
     * 
     * @return
     * 
     * @throws IOException
     */
    public Document searchTitle(String title) throws IOException {
        return this.findDocumentById(this.titleToId(title));
    }

    /**
     * na
     * 
     * @param url
     * 
     * @return
     * 
     * @throws IOException
     */
    public Document searchURL(URL url) throws IOException {
        return this.findDocumentById(this.urlToId(url));
    }

    /**
     * searchWithQuery
     * 
     * @param query
     * 
     * @return
     */
    public ResultList searchWithQuery(String query) {
        // try {
        // logger.debug("search: " + query);
        //
        // return search0(null, query);
        // } catch (FileNotFoundException e) {
        // return new ResultList(this, null, query, null,-1);
        // }
        return null;
    }

    /**
     * searchWithWord
     * 
     * @param word
     * 
     * @return
     */
    public ResultList searchWithWord(String word) {
        //        StringBuilder sb = new StringBuilder().append(word).append("^4 OR *").append(word).append(" OR ").append(word).append("*^2 OR *").append(word).append("* OR ").append(word).append("~0.75^0.5");   //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
        // String query = sb.toString();
        //
        // try {
        // return search0(word, query);
        // } catch (FileNotFoundException e) {
        // return new ResultList(this, word, query, null,-1);
        // }
        return null;
    }

    /**
     * setCacheText
     * 
     * @param cacheText the cacheText to set
     */
    public void setCacheText(boolean cacheText) {
        this.cacheText = cacheText;
    }

    /**
     * setDefaultMaxSearchResults
     * 
     * @param defaultMaxSearchResults the defaultMaxSearchResults to set
     */
    public void setDefaultMaxSearchResults(int defaultMaxSearchResults) {
        this.defaultMaxSearchResults = defaultMaxSearchResults;
    }

    /**
     * title to string id
     * 
     * @param title
     * 
     * @return
     */
    protected String titleToId(String title) {
        return LuceneHelper.DOC_URL_PREFIX + title;
    }

    /**
     * topDocTerms
     * 
     * @param count
     * 
     * @return
     * 
     * @throws IOException
     */
    public DocHighScoreTerms[] topDocTerms(int count) throws IOException {
        return this.topDocTerms(count, ID.text.f());
    }

    /**
     * topDocTerms
     * 
     * @param count
     * @param filter
     * 
     * @return
     * 
     * @throws IOException
     */
    public DocHighScoreTerms[] topDocTerms(int count, String filter) throws IOException {
        if (count == -1) {
            count = Integer.MAX_VALUE;
        }

        ArrayList<Holder<DocHighScoreTerms>> data = new ArrayList<Holder<DocHighScoreTerms>>();
        IndexReader reader0 = this.getReadOnlyIndexReader();
        TermEnum t = reader0.terms();

        while (t.next()) {
            int freq = t.docFreq();
            String field = t.term().field();
            String term = t.term().text();

            if ((filter == null) || field.equals(filter)) {
                if (data.size() < count) {
                    data.add(new Holder<DocHighScoreTerms>(new DocHighScoreTerms(freq, field, term)));
                } else {
                    try {
                        for (Holder<DocHighScoreTerms> info : data) {
                            if (freq > info.value.docFrequency) {
                                info.value = new DocHighScoreTerms(freq, field, term);
                                throw new BreakLoopException();
                            }
                        }
                    } catch (BreakLoopException e) {
                        // expected
                    }
                }
            }
        }

        t.close();

        DocHighScoreTerms[] data_array = new DocHighScoreTerms[data.size()];
        int index = 0;

        for (Holder<DocHighScoreTerms> info : data) {
            data_array[index++] = info.value;
        }

        Arrays.sort(data_array, new Comparator<DocHighScoreTerms>() {
            @Override
            public int compare(DocHighScoreTerms o1, DocHighScoreTerms o2) {
                return o2.docFrequency - o1.docFrequency;
            }
        });

        return data_array;
    }

    /**
     * topTerms
     * 
     * @param count
     * 
     * @return
     * 
     * @throws IOException
     */
    public HighScoreTerms[] topTerms(int count) throws IOException {
        IndexReader reader0 = this.getReadOnlyIndexReader();
        Map<String, HighScoreTerms> occurences = new HashMap<String, HighScoreTerms>();
        TermEnum t = reader0.terms();

        while (t.next()) {
            String text = t.term().text();
            HighScoreTerms hst = occurences.get(text);

            if (hst == null) {
                hst = new HighScoreTerms(text);
                occurences.put(text, hst);
            }

            TermDocs td = reader0.termDocs(t.term());

            while (td.next()) {
                int id = td.doc();
                int freq = td.freq();
                hst.add(id, freq);
            }

            td.close();
        }

        t.close();

        SortedSet<HighScoreTerms> sorted = new TreeSet<HighScoreTerms>();
        sorted.addAll(occurences.values());

        if (count == -1) {
            return sorted.toArray(new HighScoreTerms[0]);
        }

        HighScoreTerms[] top = new HighScoreTerms[Math.min(count, sorted.size())];
        Iterator<HighScoreTerms> it = sorted.iterator();

        for (int i = 0; i < top.length; i++) {
            top[i] = it.next();
        }

        return top;
    }

    // /**
    // * general internal search
    // *
    // * @param word single word or null
    // * @param query query string
    // *
    // * @return {@link ResultList}
    // *
    // * @throws IOException IOException
    // * @throws ParseException ParseException
    // */
    // private ResultList search0(final String word, final String query) throws IOException, ParseException {
    // PerFieldAnalyzerWrapper perFieldAnalyzer = new PerFieldAnalyzerWrapper(analyzer.analyzer());
    // perFieldAnalyzer.addAnalyzer(ID.url.f(), new KeywordAnalyzer());
    // perFieldAnalyzer.addAnalyzer(ID.description.f(), new WhitespaceAnalyzer());
    // perFieldAnalyzer.addAnalyzer(ID.keywords.f(), new WhitespaceAnalyzer());
    //
    // QueryParser queryParser = new QueryParser(Version.LUCENE_CURRENT, ID.text.f(), perFieldAnalyzer);
    // queryParser.setAllowLeadingWildcard(true);
    //
    // logger.info("search0(IndexSearcher, String) - Query query=" + query);
    //
    // Query q = queryParser.parse(query);
    // logger.info("search0(IndexSearcher, String) - Query query=" + q);
    //
    // IndexSearcher searcher0 = getSearcher();
    // TopDocs hits = searcher0.search(q, getDefaultMaxSearchResults());
    //
    // logger.info("search0(IndexSearcher, String) - " + hits.totalHits + " found");
    //
    // ResultList results = new ResultList(this, word, query, q,-1);
    //
    // for (int i = 0; i < hits.totalHits; i++) {
    // ScoreDoc scoreDoc = hits.scoreDocs[i];
    // Document doc = searcher0.doc(scoreDoc.doc);
    // results.addSearchResult(new SearchResult(i, results, doc, scoreDoc.doc, scoreDoc.score, this));
    // }
    //
    // return results;
    // }
    /**
     * urlNeedsIndexing
     * 
     * @param url
     * 
     * @return
     * 
     * @throws IOException
     */
    public boolean urlNeedsIndexing(URL url) throws IOException {
        Document exists = this.findDocumentById(this.urlToId(url));

        if (exists == null) {
            return false;
        }

        URLConnection openConnection = url.openConnection();
        long lastModified = openConnection.getLastModified();

        openConnection.getInputStream().close();

        return (Long.parseLong(exists.get(ID.lastmod.f())) < lastModified);
    }

    /**
     * url to string id
     * 
     * @param url
     * 
     * @return
     */
    protected String urlToId(URL url) {
        return url.toString();
    }
}
