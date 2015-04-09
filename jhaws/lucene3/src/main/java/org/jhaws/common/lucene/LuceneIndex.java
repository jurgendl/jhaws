package org.jhaws.common.lucene;

import java.io.ByteArrayOutputStream;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

import org.apache.log4j.Logger;
import org.apache.lucene.analysis.KeywordAnalyzer;
import org.apache.lucene.analysis.PerFieldAnalyzerWrapper;
import org.apache.lucene.analysis.WhitespaceAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.FieldInfo;
import org.apache.lucene.index.FieldInfos;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriter.MaxFieldLength;
import org.apache.lucene.index.TermDocs;
import org.apache.lucene.index.TermEnum;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.Explanation;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.NIOFSDirectory;
import org.apache.lucene.util.Version;
import org.jhaws.common.io.IODirectory;
import org.jhaws.common.io.IOFile;
import org.jhaws.common.io.IOGeneralFile;
import org.jhaws.common.io.filter.FileExtensionFilter;
import org.jhaws.common.lucene.exception.ConvertorNotSupportedException;
import org.jhaws.common.lucene.exception.ProtocolException;
import org.jhaws.common.lucene.stat.DocHighScoreTerms;
import org.jhaws.common.lucene.stat.HighScoreTerms;

/**
 * na
 * 
 * @author Jurgen
 * @version 3.0.0 - 21 August 2007
 * 
 * @see http://lucene.apache.org/java/docs/
 * @see http://wiki.apache.org/lucene-java/LuceneFAQ
 * @since Java 5.0 and Lucene 2.1.0
 */
@Deprecated
public class LuceneIndex implements LuceneInterface {
    /**
     * internally used
     * 
     * @author Jurgen
     */
    private static class BreakLoopException extends Exception {
        /** serialVersionUID */
        private static final long serialVersionUID = 8105811033552521805L;
    }

    /** compound file */
    private static final boolean COMPOUND = false;

    /** Logger for this class */
    private static final Logger logger = Logger.getLogger(LuceneIndex.class);

    /** cached */
    private static final Map<IODirectory, LuceneIndex> cached = new HashMap<IODirectory, LuceneIndex>();

    /** excape characters */
    public static final char[] ESCAPE_CHARACTERS = "+-&|!(){}[]^~*?:\\\"".toCharArray(); //$NON-NLS-1$

    /**
     * escapeText
     * 
     * @param string
     * 
     * @return
     */
    private static String escapeText(String string) {
        LuceneIndex.logger.info("escapeText text=" + string); //$NON-NLS-1$

        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < string.length(); i++) {
            char c = string.charAt(i);

            for (char element : LuceneIndex.ESCAPE_CHARACTERS) {
                if (c == element) {
                    sb.append("\\"); //$NON-NLS-1$
                }
            }

            sb.append(c);
        }

        String escaped = sb.toString();
        LuceneIndex.logger.info("escapeText escaped=" + escaped); //$NON-NLS-1$

        return escaped;
    }

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
        return LuceneIndex.get(indexdir, false);
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
    public static LuceneInterface get(final IODirectory indexdir, final boolean cleanFirst) throws IOException {
        LuceneIndex index = LuceneIndex.cached.get(indexdir);

        if (cleanFirst && indexdir.exists() && (index == null)) {
            indexdir.erase();
        }

        if (!indexdir.exists()) {
            indexdir.create();
        }

        if (index == null) {
            index = new LuceneIndex(indexdir);
            LuceneIndex.cached.put(indexdir, index);

            Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
                @Override
                public void run() {
                    for (LuceneIndex index0 : LuceneIndex.cached.values()) {
                        try {
                            index0.close();
                        } catch (Exception ex) {
                            ex.printStackTrace(System.out);
                        }
                    }
                }
            }));
        }

        return index;
    }

    /**
     * creates new FileFilter from all supported extensions
     * 
     * @return {@link FileFilter}
     */
    public static FileFilter newExtensionFilter() {
        return LuceneIndex.newExtensionFilter(DocumentFactory.getSupportedExtensions());
    }

    /**
     * creates new FileFilter from give extensions
     * 
     * @return {@link FileFilter}
     * 
     * @throws RuntimeException RuntimeException
     */
    public static FileFilter newExtensionFilter(final String... ext) {
        for (String element : ext) {
            if (element.indexOf('.') != -1) {
                throw new RuntimeException("extension cannot have the '.' sign"); //$NON-NLS-1$
            }
        }

        return new FileExtensionFilter(ext);
    }

    /** analyzer for language */
    private Analyzer analyzer = null;

    /** {@link IODirectory} */
    private IODirectory dir = null;

    /** {@link IndexSearcher} */
    private IndexSearcher searcher = null;

    /** {@link IndexWriter} */
    private IndexWriter modifier = null;

    /** field */
    private String luceneVersion = null;

    /** cache text */
    private boolean cacheText = false;

    /** defaultMaxSearchResults */
    private int defaultMaxSearchResults = 100;

    /** field */
    private int maxBufferedDocs = 10;

    /** field */
    private int maxFieldLength = 10000;

    /** field */
    private int mergeFactor = 10;

    /**
     * Creates a new LuceneIndex object.
     * 
     * @param indexdir na
     * 
     * @throws CorruptIndexException na
     * @throws IOException na
     */
    private LuceneIndex(final IODirectory dir) throws IOException {
        this.dir = dir;
        this.setLanguage(Locale.getDefault());
        this.getModifier();
    }

    /**
     * cache text from files
     * 
     * @param url URL as text
     * @param text contents of file
     */
    private void cacheText(final String url, final String text) {
        LuceneIndex.logger.info("cache(String, String) - start"); //$NON-NLS-1$

        if (text.length() == 0) {
            return;
        }

        try {
            IOFile source = new IOFile(new URL(url).getFile());
            String sap = source.getAbsolutePath();
            IODirectory cacheDir = new IODirectory(this.dir, "cache").create(); //$NON-NLS-1$
            IOFile target = new IOFile(cacheDir, sap.replace(':', '$').replace('/', '$').replace('\\', '$') + ".txt"); //$NON-NLS-1$

            LuceneIndex.logger.info("cacheText(String, String) - IOFile source=" + source); //$NON-NLS-1$
            LuceneIndex.logger.info("cacheText(String, String) - String sap=" + sap); //$NON-NLS-1$
            LuceneIndex.logger.info("cacheText(String, String) - String dir=" + this.dir.getAbsolutePath()); //$NON-NLS-1$           
            LuceneIndex.logger.info("cacheText(String, String) - IOFile target=" + target); //$NON-NLS-1$

            FileOutputStream out = new FileOutputStream(target);
            out.write(text.getBytes());
            out.close();

            IOFile bzip = new IOFile(target + ".bz2"); //$NON-NLS-1$
            LuceneIndex.logger.info("cache(String, String) - IOFile zip=" + bzip); //$NON-NLS-1$

            bzip.erase();

            LuceneArchive archive = new LuceneArchive(bzip);
            archive.addEntry(target);

            try {
                archive.create();
            } catch (Exception exx) {
                LuceneIndex.logger.error(exx, exx);
            }

            target.erase();
        } catch (Exception ex) {
            LuceneIndex.logger.error(ex, ex);
        }

        LuceneIndex.logger.info("cache(String, String) - end"); //$NON-NLS-1$
    }

    /**
     * 
     * @see org.jhaws.common.lucene.LuceneInterface#cleanUpNotExistingFileDocuments()
     */
    @Override
    public int cleanUpNotExistingFileDocuments() throws IOException {
        LuceneIndex.logger.info("cleanUpNotExistingFileDocuments()"); //$NON-NLS-1$

        IndexSearcher searcher0 = this.getSearcher();
        ArrayList<Integer> toDelete = new ArrayList<Integer>();
        ArrayList<String> urlToDelete = new ArrayList<String>();

        for (int i = 0; i < searcher0.maxDoc(); i++) {
            try {
                Document doc = searcher0.doc(i);

                if ((doc.getField(ID.text.f()) == null) || !doc.getField(ID.text.f()).isStored()) {
                    String url = doc.get(ID.url.f());

                    try {
                        URL u = new URL(url);

                        if (u.getProtocol().equals("file")) { //$NON-NLS-1$

                            if (!new IOFile(URLDecoder.decode(u.getFile(), "UTF-8")).exists()) {
                                LuceneIndex.logger.info("file " + url + " will be deleted"); //$NON-NLS-1$ //$NON-NLS-2$
                                toDelete.add(new Integer(i));
                                urlToDelete.add(u.getFile());
                            }
                        }
                    } catch (Exception exx) {
                        LuceneIndex.logger.error("cleanUpNotExistingFileDocuments()", exx); //$NON-NLS-1$
                    }
                }
            } catch (Exception exxx) {
                LuceneIndex.logger.error("cleanUpNotExistingFileDocuments()", exxx); //$NON-NLS-1$
            }
        }

        IndexReader reader0 = this.getReader();

        for (Integer id : toDelete) {
            reader0.deleteDocument(id);
        }

        this.optimize();

        IODirectory cacheDir = new IODirectory(this.dir, "cache").create(); //$NON-NLS-1$

        for (String u : urlToDelete) {
            IOFile target = new IOFile(cacheDir, u.replace(':', '$').replace('/', '$').replace('\\', '$') + ".txt"); //$NON-NLS-1$
            IOFile bzip = new IOFile(target + ".bz2"); //$NON-NLS-1$

            if (bzip.exists()) {
                bzip.erase();
            }
        }

        return toDelete.size();
    }

    /**
     * 
     * @see org.jhaws.common.lucene.LuceneInterface#close()
     */
    @Override
    public void close() throws IOException {
        LuceneIndex.logger.info("close()"); //$NON-NLS-1$

        this.closeModifier();
        this.closeSearcher();

        LuceneIndex.cached.remove(this.dir);
    }

    private void closeModifier() throws IOException {
        if (this.modifier != null) {
            LuceneIndex.logger.info("closeModifier");
            this.optimize();
            this.modifier.commit();
            this.modifier.close();
            this.modifier = null;
        }
    }

    private void closeSearcher() throws IOException {
        if (this.searcher != null) {
            LuceneIndex.logger.info("closeSearcher");
            this.searcher.close();
            this.searcher = null;
        }
    }

    /**
     * 
     * @see org.jhaws.common.lucene.LuceneInterface#delete()
     */
    @Override
    public boolean delete() throws IOException {
        LuceneIndex.logger.info("delete() - start"); //$NON-NLS-1$

        if (this.dir == null) {
            return false;
        }

        this.close();
        this.dir.erase();
        this.dir = null;

        return true;
    }

    /**
     * 
     * @see org.jhaws.common.lucene.LuceneInterface#deleteDocument(util.io.IOFile)
     */
    @Override
    public void deleteDocument(IOFile file) throws IOException {
        this.deleteDocument(file.toURI().toURL());
    }

    /**
     * 
     * @see org.jhaws.common.lucene.LuceneInterface#deleteDocument(java.lang.String)
     */
    @Override
    public void deleteDocument(String title) throws IOException {
        int id = this.findDocumentId("doc://" + title);
        this.getReader().deleteDocument(id);
    }

    /**
     * 
     * @see org.jhaws.common.lucene.LuceneInterface#deleteDocument(java.net.URL)
     */
    @Override
    public void deleteDocument(URL url) throws IOException {
        int id = this.findDocumentId(url.toString());
        IndexReader reader2 = this.getReader();
        reader2.deleteDocument(id);
    }

    /**
     * 
     * @see org.jhaws.common.lucene.LuceneInterface#escape(java.lang.String)
     */
    @Override
    public String escape(String qword) {
        return LuceneIndex.escapeText(qword);
    }

    /**
     * 
     * @see org.jhaws.common.lucene.LuceneInterface#explain(org.apache.lucene.search.Query, int)
     */
    @Override
    public Explanation explain(Query query, int doc) throws IOException {
        return this.getSearcher().explain(query, doc);
    }

    /**
     * indexes a file
     * 
     * @param file {@link IOFile}
     * @param description description
     * @param keywords String[] keywords
     * 
     * @return {@link Document}
     * 
     * @throws IOException when something fails
     * @throws ConvertorNotSupportedException ConvertorNotSupportedException
     */
    private Document fileToDocument(final IOFile file, final String description, final String[] keywords) throws IOException,
            ConvertorNotSupportedException {
        if (DocumentFactory.supports(file)) {
            LuceneIndex.logger.info("fileToDocument(...) - IOFile file=" + file); //$NON-NLS-1$

            Document doc = DocumentFactory.getConvertor(file).convert(this.analyzer, file, file.toURI().toURL(), keywords, description);
            String text = doc.get(ID.text.f());
            String url = doc.get(ID.url.f());

            // no need to save txt files somewhere else
            if (this.isCacheText() && !"txt".equals(IOGeneralFile.getExtension(url).toLowerCase())) { //$NON-NLS-1$
                this.cacheText(url, text);
            }

            return doc;
        }

        throw new ConvertorNotSupportedException(file.getName());
    }

    /**
     * 
     * @see org.jhaws.common.lucene.LuceneInterface#findDocument(util.io.IOFile)
     */
    @Override
    public Document findDocument(IOFile file) throws IOException {
        try {
            return this.findDocument(file.toURI().toURL());
        } catch (MalformedURLException e) {
            System.err.println("IMPOSSIBLE"); //$NON-NLS-1$
            e.printStackTrace();
            throw new RuntimeException("IMPOSSIBLE", e); //$NON-NLS-1$
        }
    }

    /**
     * 
     * @see org.jhaws.common.lucene.LuceneInterface#findDocument(java.net.URL)
     */
    @Override
    public Document findDocument(URL url) throws IOException {
        Integer findDocumentId = this.findDocumentId(url.toString());

        if (findDocumentId == null) {
            return null;
        }

        return this.getReader().document(findDocumentId);
    }

    /**
     * findDocumentId
     * 
     * @param url
     * 
     * @return
     * 
     * @throws IOException
     * @throws RuntimeException
     */
    public Integer findDocumentId(String url) throws IOException {
        try {
            String qs = this.escape(url.toString());
            Query query = new QueryParser(Version.LUCENE_CURRENT, ID.url.f(), new WhitespaceAnalyzer()).parse(qs);

            TopDocs search = this.getSearcher().search(query, this.getDefaultMaxSearchResults());

            if (search.scoreDocs.length != 1) {
                return null;
            }

            return search.scoreDocs[0].doc;
        } catch (ParseException ex) {
            System.err.println("IMPOSSIBLE"); //$NON-NLS-1$
            ex.printStackTrace();
            throw new RuntimeException("IMPOSSIBLE", ex); //$NON-NLS-1$
        }
    }

    /**
     * read contents from file
     * 
     * @param url {@link URL}
     * 
     * @return byte[]
     * 
     * @throws IOException IOException
     */
    private byte[] getContents(final URL url) throws IOException {
        if (url.toString().startsWith("http")) { //$NON-NLS-1$

            return this.readFromStream(url.openConnection().getInputStream());
        }

        if (url.toString().startsWith("ftp")) { //$NON-NLS-1$

            return this.readFromStream(url.openConnection().getInputStream());
        }

        if (url.toString().startsWith("jar")) { //$NON-NLS-1$

            return this.readFromStream(url.openConnection().getInputStream());
        }

        return new byte[0];
    }

    /**
     * 
     * @see org.jhaws.common.lucene.LuceneInterface#getDefaultMaxSearchResults()
     */
    @Override
    public int getDefaultMaxSearchResults() {
        return this.defaultMaxSearchResults;
    }

    /**
     * gets description
     * 
     * @param description description or null
     * 
     * @return description or ""
     */
    private String getDescription(final String description) {
        return (description == null) ? "" : description; //$NON-NLS-1$
    }

    /**
     * 
     * @see org.jhaws.common.lucene.LuceneInterface#getDocCount()
     */
    @Override
    public int getDocCount() throws IOException {
        return this.getReader().numDocs();
    }

    /**
     * 
     * @see org.jhaws.common.lucene.LuceneInterface#getDocument(int)
     */
    @Override
    public Document getDocument(int index) throws IOException {
        LuceneIndex.logger.info("getDocument(int) - start"); //$NON-NLS-1$

        Document returnDocument = this.getReader().document(index);
        LuceneIndex.logger.info("getDocument(int) - end"); //$NON-NLS-1$

        return returnDocument;
    }

    /**
     * 
     * @see org.jhaws.common.lucene.LuceneInterface#getFieldNames()
     */
    @Override
    public SortedSet<String> getFieldNames() throws IOException {
        SortedSet<String> set = new TreeSet<String>();
        FieldInfos finfo = this.getReader().getFieldInfos();
        Iterator<FieldInfo> iterator = finfo.iterator();
        while (iterator.hasNext()) {
            FieldInfo fieldInfo = iterator.next();
            set.add(fieldInfo.name);
        }

        return set;
    }

    /**
     * 
     * @see org.jhaws.common.lucene.LuceneInterface#getIndexLocation()
     */
    @Override
    public IODirectory getIndexLocation() {
        return this.dir;
    }

    /**
     * 
     * @see org.jhaws.common.lucene.LuceneInterface#getLanguage()
     */
    @Override
    public String getLanguage() {
        return this.analyzer.toString();
    }

    /**
     * 
     * @see org.jhaws.common.lucene.LuceneInterface#getLockId()
     */
    @Override
    public String getLockId() throws IOException {
        return this.getReader().directory().getLockID();
    }

    /**
     * 
     * @see org.jhaws.common.lucene.LuceneInterface#getLuceneVersion()
     */
    @Override
    public String getLuceneVersion() {
        if (this.luceneVersion == null) {
            try {
                URL loc = org.apache.lucene.index.IndexWriter.class.getClassLoader().getResource("org/apache/lucene/index/IndexWriter.class"); //$NON-NLS-1$
                String jar = URLDecoder.decode(loc.toString(), "UTF-8");
                jar = jar.substring("jar:file:".length(), jar.length() - "!/org/apache/lucene/index/IndexWriter.class".length()); //$NON-NLS-1$ //$NON-NLS-2$

                JarFile jarf = new JarFile(jar);
                Manifest manifest = jarf.getManifest();
                Attributes atts = manifest.getMainAttributes();

                this.luceneVersion = atts.getValue("Implementation-Version"); //$NON-NLS-1$

                if (this.luceneVersion == null) {
                    this.luceneVersion = atts.getValue("Specification-Version"); //$NON-NLS-1$
                }

                if (this.luceneVersion == null) {
                    this.luceneVersion = "Unknown"; //$NON-NLS-1$
                }
                jarf.close();
            } catch (Exception ex) {
                ex.printStackTrace(System.out);
                this.luceneVersion = "Unknown"; //$NON-NLS-1$
            }
        }

        return this.luceneVersion;
    }

    /**
     * 
     * @see org.jhaws.common.lucene.LuceneInterface#getMaxBufferedDocs()
     */
    @Override
    public int getMaxBufferedDocs() throws IOException {
        return this.getModifier().getMaxBufferedDocs();
    }

    /**
     * 
     * @see org.jhaws.common.lucene.LuceneInterface#getMaxFieldLength()
     */
    @Override
    public int getMaxFieldLength() throws IOException {
        return this.getModifier().getMaxFieldLength();
    }

    /**
     * 
     * @see org.jhaws.common.lucene.LuceneInterface#getMergeFactor()
     */
    @Override
    public int getMergeFactor() throws IOException {
        return this.getModifier().getMergeFactor();
    }

    /**
     * 
     * @see org.jhaws.common.lucene.LuceneInterface#getModificationDate()
     */
    @Override
    public Date getModificationDate() {
        return new Date(new IOFile(this.dir, LuceneInterface.segments).lastModified());
    }

    /**
     * na
     * 
     * @return
     * 
     * @throws IOException na
     */
    private synchronized IndexWriter getModifier() throws IOException {
        if (this.modifier == null) {
            this.closeModifier();
            LuceneIndex.logger.info("getModifier() - NEW"); //$NON-NLS-1$

            // if (IndexReader.isLocked(dir.getAbsolutePath())) {
            // IndexReader.unlock(FSDirectory.getDirectory(dir));
            // }
            Directory p1 = new NIOFSDirectory(this.dir);
            org.apache.lucene.analysis.Analyzer p2 = this.analyzer.analyzer();
            boolean p3 = !new IOFile(this.dir, LuceneInterface.segments).exists();
            MaxFieldLength p4 = new MaxFieldLength(this.maxFieldLength);
            this.modifier = new IndexWriter(p1, p2, p3, p4);
            this.modifier.setUseCompoundFile(LuceneIndex.COMPOUND);
            this.modifier.setMaxBufferedDocs(this.maxBufferedDocs);
            this.modifier.setMaxFieldLength(this.maxFieldLength);
            this.modifier.setMergeFactor(this.mergeFactor);
        }

        return this.modifier;
    }

    /**
     * na
     * 
     * @return
     * 
     * @throws IOException na
     */
    private synchronized IndexReader getReader() throws IOException {
        IndexReader reader = this.getModifier().getReader();

        return reader;
    }

    /**
     * na
     * 
     * @return
     * 
     * @throws IOException
     */
    private synchronized IndexSearcher getSearcher() throws IOException {
        boolean iscurrent = false;

        if (this.searcher != null) {
            iscurrent = this.searcher.getIndexReader().isCurrent();
        }

        if ((this.searcher == null) || !iscurrent) {
            this.closeSearcher();
            LuceneIndex.logger.info("getSearcher() - NEW"); //$NON-NLS-1$
            this.searcher = new IndexSearcher(this.getReader());
        }

        return this.searcher;
    }

    /**
     * 
     * @see org.jhaws.common.lucene.LuceneInterface#getVersion()
     */
    @Override
    public long getVersion() throws IOException {
        return this.getReader().getVersion();
    }

    /**
     * 
     * @see org.jhaws.common.lucene.LuceneInterface#getVersionDate()
     */
    @Override
    public Date getVersionDate() throws IOException {
        return new Date(this.getVersion());
    }

    /**
     * 
     * @see org.jhaws.common.lucene.LuceneInterface#index(util.io.IOFile)
     */
    @Override
    public synchronized LuceneInterface index(final IOFile file) throws IOException, ConvertorNotSupportedException {
        return this.index(file, null);
    }

    /**
     * 
     * @see org.jhaws.common.lucene.LuceneInterface#index(util.io.IOFile, java.lang.String, java.lang.String[])
     */
    @Override
    public synchronized LuceneInterface index(final IOFile file, final String description, final String... keywords) throws IOException,
            ConvertorNotSupportedException {
        Integer docIndex = this.findDocumentId(file.toURI().toURL().toString());

        if (docIndex == null) {
            // new
            this.getModifier().addDocument(this.fileToDocument(file, description, keywords));
            this.getModifier().commit();
        } else {
            Document existing = this.getReader().document(docIndex);

            if ((Long.parseLong(existing.get(ID.lastmod.f())) < file.lastModified())) {
                // update
                this.getReader().deleteDocument(docIndex);
                this.getModifier().addDocument(this.fileToDocument(file, description, keywords));
                this.getModifier().commit();
            } else {
                // nothing to see, move along
            }
        }

        // //search for existing document with the same name as the absolute filename
        // SearchResult sr_exists = null;
        // URL url = file.toURI().toURL();
        //
        // try {
        //            for (SearchResult tmp : search(ID.url + ":" + escape(url.toString()))) { //$NON-NLS-1$
        //
        // if (tmp.getURL().equals(url)) {
        // sr_exists = tmp;
        // throw new BreakLoopException();
        // }
        // }
        // } catch (BreakLoopException e) {
        // // expected
        // } catch (ParseException e) {
        //            System.out.println("IMPOSSIBLE"); //$NON-NLS-1$
        // e.printStackTrace();
        //            throw new RuntimeException("IMPOSSIBLE", e); //$NON-NLS-1$
        // }
        //
        // Document exists = findDocument(file);
        // int docIndex = (sr_exists == null) ? (-1) : sr_exists.getId();
        // boolean needsUpdate = (exists == null) || (Long.parseLong(exists.get(ID.lastmod.f())) < file.lastModified());
        //
        // if (exists != null) {
        // if (needsUpdate) {
        // // delete first and the adds document (=update)
        // getReader().deleteDocument(docIndex);
        // getModifier().addDocument(fileToDocument(file, description, keywords));
        //                logger.info("" + file + "': replaced old version (not up to date)"); //$NON-NLS-1$ //$NON-NLS-2$
        // } else {
        // // document up to date
        //                logger.info("" + file + "': does not need indexing (up to date)"); //$NON-NLS-1$ //$NON-NLS-2$
        // }
        // } else {
        // // adds document
        // getModifier().addDocument(fileToDocument(file, description, keywords));
        //            logger.info("" + file + "': indexed"); //$NON-NLS-1$ //$NON-NLS-2$
        // }
        //
        // getModifier().commit();
        return this;
    }

    /**
     * 
     * @see org.jhaws.common.lucene.LuceneInterface#index(java.lang.String, java.lang.String, java.lang.String, java.lang.String[])
     */
    @Override
    public synchronized LuceneInterface index(final String text, final String title, final String description, final String... keywords)
            throws IOException {
        if (title == null) {
            throw new IllegalArgumentException("title cannot be null"); //$NON-NLS-1$
        }

        Document exists = null;
        int docIndex = -1;

        IndexSearcher searcher0 = this.getSearcher();

        for (int i = 0; (i < searcher0.maxDoc()) && (exists == null); i++) {
            Document document = searcher0.doc(i);
            String containingFileName = document.get(ID.url.f());
            String newFileName = "doc://" + title; //$NON-NLS-1$

            if (containingFileName.equals(newFileName)) {
                exists = document;
                docIndex = i;
            }
        }

        LuceneIndex.logger.info("index(String, String, String, String) - int docIndex=" + docIndex); //$NON-NLS-1$

        if (docIndex != -1) {
            this.getReader().deleteDocument(docIndex);
        }

        this.getModifier().addDocument(this.textToDocument(text, title, description, keywords));

        LuceneIndex.logger.info("indexed"); //$NON-NLS-1$

        return this;
    }

    /**
     * 
     * @see org.jhaws.common.lucene.LuceneInterface#index(java.net.URL)
     */
    @Override
    public synchronized LuceneInterface index(final URL url) throws ProtocolException, ConvertorNotSupportedException, IOException {
        return this.index(url, null);
    }

    /**
     * indexes a file
     * 
     * @param url {@link URL} for file
     * @param lastmod last modification
     * @param description description
     * 
     * @return {@link Document}
     * 
     * @throws IOException IOException
     * @throws ConvertorNotSupportedException ConvertorNotSupportedException
     * @throws RuntimeException na
     */
    private synchronized LuceneInterface index(final URL url, final long lastmod, final String description, final String... keywords)
            throws IOException, ConvertorNotSupportedException {
        // search for existing document with the same name as the absolute filename
        SearchResult sr_exists = null;

        try {
            for (SearchResult tmp : this.search(ID.url + ":" + url.toString())) { //$NON-NLS-1$

                if (tmp.getURL().equals(url)) {
                    sr_exists = tmp;
                    throw new BreakLoopException();
                }
            }
        } catch (BreakLoopException e) {
            // expected
        } catch (ParseException e) {
            System.out.println("IMPOSSIBLE"); //$NON-NLS-1$
            e.printStackTrace();
            throw new RuntimeException("IMPOSSIBLE", e); //$NON-NLS-1$
        }

        Document exists = (sr_exists == null) ? null : sr_exists.getLuceneDocument();
        int docIndex = (sr_exists == null) ? (-1) : sr_exists.getId();
        boolean needsUpdate = (exists == null) || (Long.parseLong(exists.get(ID.lastmod.f())) < url.openConnection().getLastModified());

        if (exists != null) {
            if (needsUpdate) {
                this.getReader().deleteDocument(docIndex);
                this.getModifier().addDocument(this.urlToDocument(url, lastmod, description, keywords));
                LuceneIndex.logger.info("" + url + "': replaced old version (not up to date)"); //$NON-NLS-1$ //$NON-NLS-2$
            } else {
                LuceneIndex.logger.info("" + url + "': does not need indexing (up to date)"); //$NON-NLS-1$ //$NON-NLS-2$
            }
        } else {
            this.getModifier().addDocument(this.urlToDocument(url, lastmod, description, keywords));
            LuceneIndex.logger.info("" + url + "': indexed"); //$NON-NLS-1$ //$NON-NLS-2$
        }

        return this;
    }

    /**
     * 
     * @see org.jhaws.common.lucene.LuceneInterface#index(java.net.URL, java.lang.String, java.lang.String[])
     */
    @Override
    public synchronized LuceneInterface index(final URL url, final String description, final String... keywords) throws ProtocolException,
            ConvertorNotSupportedException, IOException {
        LuceneIndex.logger.info("url=" + url); //$NON-NLS-1$
        LuceneIndex.logger.info("protocol=" + url.getProtocol()); //$NON-NLS-1$

        if (url.getProtocol().equals("file")) { //$NON-NLS-1$

            return this.index(new IOFile(url.getFile()), description, keywords);
        }

        if (url.getProtocol().equals("http")) { //$NON-NLS-1$

            long lastmod = url.openConnection().getLastModified();

            return this.index(url, lastmod, description, keywords);
        }

        if (url.getProtocol().equals("ftp")) { //$NON-NLS-1$

            long lastmod = url.openConnection().getLastModified();

            return this.index(url, lastmod, description, keywords);
        }

        throw new ProtocolException(url);
    }

    /**
     * 
     * @see org.jhaws.common.lucene.LuceneInterface#isCacheText()
     */
    @Override
    public boolean isCacheText() {
        return this.cacheText;
    }

    /**
     * converts keywords to single string
     * 
     * @param keywords keywords as array
     * 
     * @return keywords as string
     */
    private String keywordsToString(final String[] keywords) {
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
     * 
     * @see org.jhaws.common.lucene.LuceneInterface#listDocuments()
     */
    @Override
    public List<LuceneDocument> listDocuments() throws IOException {
        List<LuceneDocument> results = new ArrayList<LuceneDocument>();

        try {
            IndexReader reader0 = this.getReader();

            for (int i = 0; i < reader0.numDocs(); i++) {
                Document doc = reader0.document(i);
                results.add(new LuceneDocument(doc, this));
            }
        } catch (java.io.FileNotFoundException ex) {
            //
        }

        return results;
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

    private LuceneInterface move(Integer findDocument, URL url) throws IOException {
        // Document doc_new = new Document();
        // doc_new.add(new Field(ID.analyzer.f(), findDocument.get(ID.analyzer.f()), STORED, NOT_ANALYZED, NOTERM));
        // doc_new.add(new Field(ID.description.f(), findDocument.get(ID.description.f()), STORED, ANALYZED, NOTERM));
        // doc_new.add(new Field(ID.filetype.f(), findDocument.get(ID.filetype.f()), STORED, NOT_ANALYZED, NOTERM));
        // doc_new.add(new Field(ID.keywords.f(), findDocument.get(ID.keywords.f()), STORED, ANALYZED, NOTERM));
        // doc_new.add(new Field(ID.language.f(), findDocument.get(ID.language.f()), STORED, NOT_ANALYZED, NOTERM));
        // doc_new.add(new Field(ID.lastmod.f(), findDocument.get(ID.lastmod.f()), STORED, NOT_ANALYZED, NOTERM));
        // doc_new.add(new Field(ID.size.f(), findDocument.get(ID.size.f()), STORED, NOT_ANALYZED, NOTERM));
        // doc_new.add(new Field(ID.text.f(), findDocument.get(ID.text.f()), COMPRESSED, ANALYZED, TERM));
        // doc_new.add(new Field(ID.url.f(), url.toString(), STORED, NOT_ANALYZED, NOTERM));
        //
        // // FIXME
        // getReader().deleteDocument(0);
        // getModifier().updateDocument(doc_new);
        //
        // getModifier().
        Document doc = this.getSearcher().doc(findDocument);
        doc.removeField(ID.url.f());
        doc.add(new Field(ID.url.f(), url.toString(), LuceneInterface.STORED, LuceneInterface.NOT_ANALYZED, LuceneInterface.NOTERM));

        return this;
    }

    /**
     * na
     * 
     * @param from na
     * @param to na
     * 
     * @return
     * 
     * @throws IOException
     */
    @SuppressWarnings("unchecked")
    private LuceneInterface move(String from, String to) throws IOException {
        try {
            ResultList results = this.search(ID.url + ":" + this.escape(from)); //$NON-NLS-1$
            IndexWriter modifier0 = this.getModifier();
            SortedMap<Integer, Document> to_rename = new TreeMap<Integer, Document>();

            for (SearchResult result : results) {
                if (result.getURL().toString().equalsIgnoreCase(from)) {
                    to_rename.put(new Integer(result.getId()), result.getLuceneDocument());
                }
            }

            Map.Entry<Integer, Document>[] to_rename_doc = to_rename.entrySet().toArray(new Map.Entry[0]);
            IndexReader reader0 = this.getReader();

            for (Entry<Integer, Document> element : to_rename_doc) {
                Document doc_old = element.getValue();
                int index = element.getKey().intValue();

                Document doc_new = new Document();
                doc_new.add(new Field(ID.analyzer.f(), doc_old.get(ID.analyzer.f()), LuceneInterface.STORED, LuceneInterface.NOT_ANALYZED,
                        LuceneInterface.NOTERM));
                doc_new.add(new Field(ID.description.f(), doc_old.get(ID.description.f()), LuceneInterface.STORED, LuceneInterface.ANALYZED,
                        LuceneInterface.NOTERM));
                doc_new.add(new Field(ID.filetype.f(), doc_old.get(ID.filetype.f()), LuceneInterface.STORED, LuceneInterface.NOT_ANALYZED,
                        LuceneInterface.NOTERM));
                doc_new.add(new Field(ID.keywords.f(), doc_old.get(ID.keywords.f()), LuceneInterface.STORED, LuceneInterface.ANALYZED,
                        LuceneInterface.NOTERM));
                doc_new.add(new Field(ID.language.f(), doc_old.get(ID.language.f()), LuceneInterface.STORED, LuceneInterface.NOT_ANALYZED,
                        LuceneInterface.NOTERM));
                doc_new.add(new Field(ID.lastmod.f(), doc_old.get(ID.lastmod.f()), LuceneInterface.STORED, LuceneInterface.NOT_ANALYZED,
                        LuceneInterface.NOTERM));
                doc_new.add(new Field(ID.size.f(), doc_old.get(ID.size.f()), LuceneInterface.STORED, LuceneInterface.NOT_ANALYZED,
                        LuceneInterface.NOTERM));
                doc_new.add(new Field(ID.text.f(), doc_old.get(ID.text.f()), LuceneInterface.COMPRESSED, LuceneInterface.ANALYZED,
                        LuceneInterface.TERM));
                doc_new.add(new Field(ID.url.f(), to, LuceneInterface.STORED, LuceneInterface.NOT_ANALYZED, LuceneInterface.NOTERM));

                reader0.deleteDocument(index);
                modifier0.addDocument(doc_new);
            }

            modifier0.commit();
        } catch (ParseException ex) {
            ex.printStackTrace(System.out);
        }

        return this;
    }

    /**
     * 
     * @see org.jhaws.common.lucene.LuceneInterface#moveDocument(util.io.IOFile, util.io.IOFile)
     */
    @Override
    public LuceneInterface moveDocument(IOFile file_from, IOFile file_to) throws IOException {
        return this.move(this.findDocumentId(file_from.toURI().toURL().toString()), file_to.toURI().toURL());
    }

    /**
     * 
     * @see org.jhaws.common.lucene.LuceneInterface#moveDocument(java.lang.String, java.lang.String)
     */
    @Override
    public LuceneInterface moveDocument(String title_from, String title_to) throws IOException {
        return this.move("doc://" + title_from, "doc://" + title_to); //$NON-NLS-1$//$NON-NLS-2$
    }

    /**
     * 
     * @see org.jhaws.common.lucene.LuceneInterface#moveDocument(java.net.URL, java.net.URL)
     */
    @Override
    public LuceneInterface moveDocument(URL url_from, URL url_to) throws IOException {
        return this.move(url_from.toString(), url_to.toString());
    }

    /**
     * 
     * @see org.jhaws.common.lucene.LuceneInterface#needsIndexing(util.io.IOFile)
     */
    @Override
    public boolean needsIndexing(IOFile file) throws IOException {
        Document exists = this.findDocument(file);

        return (exists == null) || (Long.parseLong(exists.get(ID.lastmod.f())) < file.lastModified());
    }

    /**
     * 
     * @see org.jhaws.common.lucene.LuceneInterface#needsIndexing(java.net.URL)
     */
    @Override
    public boolean needsIndexing(URL url) throws IOException {
        Document exists = this.findDocument(url);

        if (exists == null) {
            return false;
        }

        URLConnection openConnection = url.openConnection();
        long lastModified = openConnection.getLastModified();

        try {
            openConnection.getInputStream().close();
        } catch (Exception ex) {
            LuceneIndex.logger.error(ex, ex);
        }

        return (Long.parseLong(exists.get(ID.lastmod.f())) < lastModified);
    }

    /**
     * 
     * @see org.jhaws.common.lucene.LuceneInterface#optimize()
     */
    @Override
    public synchronized LuceneInterface optimize() throws IOException {
        LuceneIndex.logger.info("optimize() - optimize and flush index"); //$NON-NLS-1$

        this.getModifier().commit();
        this.getModifier().optimize();

        return this;
    }

    /**
     * read file from stream
     * 
     * @param is {@link InputStream}
     * 
     * @return byte[]
     * 
     * @throws IOException IOException
     */
    private byte[] readFromStream(final InputStream is) throws IOException {
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

    /**
     * 
     * @see org.jhaws.common.lucene.LuceneInterface#search(java.lang.String)
     */
    @Override
    public ResultList search(final String query) throws IOException, ParseException {
        try {
            LuceneIndex.logger.debug("search: " + query);

            return this.search0(null, query);
        } catch (FileNotFoundException e) {
            return new ResultList(this, null, query, null, -1);
        }
    }

    /**
     * general internal search
     * 
     * @param word single word or null
     * @param query query string
     * 
     * @return {@link ResultList}
     * 
     * @throws IOException IOException
     * @throws ParseException ParseException
     */
    private ResultList search0(final String word, final String query) throws IOException, ParseException {
        PerFieldAnalyzerWrapper perFieldAnalyzer = new PerFieldAnalyzerWrapper(this.analyzer.analyzer());
        perFieldAnalyzer.addAnalyzer(ID.url.f(), new KeywordAnalyzer());
        perFieldAnalyzer.addAnalyzer(ID.description.f(), new WhitespaceAnalyzer());
        perFieldAnalyzer.addAnalyzer(ID.keywords.f(), new WhitespaceAnalyzer());

        QueryParser queryParser = new QueryParser(Version.LUCENE_CURRENT, ID.text.f(), perFieldAnalyzer);
        queryParser.setAllowLeadingWildcard(true);

        LuceneIndex.logger.info("search0(IndexSearcher, String) - Query query=" + query); //$NON-NLS-1$

        Query q = queryParser.parse(query);
        LuceneIndex.logger.info("search0(IndexSearcher, String) - Query query=" + q); //$NON-NLS-1$

        IndexSearcher searcher0 = this.getSearcher();
        TopDocs hits = searcher0.search(q, this.getDefaultMaxSearchResults());

        LuceneIndex.logger.info("search0(IndexSearcher, String) - " + hits.totalHits + " found"); //$NON-NLS-1$ //$NON-NLS-2$

        ResultList results = new ResultList(this, word, query, q, -1);

        for (int i = 0; i < hits.totalHits; i++) {
            ScoreDoc scoreDoc = hits.scoreDocs[i];
            Document doc = searcher0.doc(scoreDoc.doc);
            results.addSearchResult(new SearchResult(i, results, doc, scoreDoc.doc, scoreDoc.score, this));
        }

        return results;
    }

    /**
     * 
     * @see org.jhaws.common.lucene.LuceneInterface#searchSingleWord(java.lang.String)
     */
    @Override
    public ResultList searchSingleWord(final String word) throws IOException, ParseException {
        StringBuilder sb = new StringBuilder()
                .append(word)
                .append("^4 OR *").append(word).append(" OR ").append(word).append("*^2 OR *").append(word).append("* OR ").append(word).append("~0.75^0.5"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
        String query = sb.toString();

        try {
            return this.search0(word, query);
        } catch (FileNotFoundException e) {
            return new ResultList(this, word, query, null, -1);
        }
    }

    /**
     * 
     * @see org.jhaws.common.lucene.LuceneInterface#setCacheText(boolean)
     */
    @Override
    public LuceneInterface setCacheText(final boolean cacheText) {
        this.cacheText = cacheText;

        return this;
    }

    /**
     * 
     * @see org.jhaws.common.lucene.LuceneInterface#setDefaultMaxSearchResults(int)
     */
    @Override
    public LuceneIndex setDefaultMaxSearchResults(int defaultMaxSearchResults) {
        this.defaultMaxSearchResults = defaultMaxSearchResults;

        return this;
    }

    /**
     * 
     * @see org.jhaws.common.lucene.LuceneInterface#setLanguage(java.util.Locale)
     */
    @Override
    public LuceneInterface setLanguage(final Locale locale) throws IOException {
        this.analyzer = Analyzer.fetch(locale);

        this.closeModifier();

        return this;
    }

    /**
     * 
     * @see org.jhaws.common.lucene.LuceneInterface#setMaxBufferedDocs(int)
     */
    @Override
    public LuceneInterface setMaxBufferedDocs(int maxBufferedDocs) throws IOException {
        this.maxBufferedDocs = maxBufferedDocs;
        this.getModifier().setMaxBufferedDocs(maxBufferedDocs);

        return this;
    }

    /**
     * 
     * @see org.jhaws.common.lucene.LuceneInterface#setMaxFieldLength(int)
     */
    @Override
    public LuceneInterface setMaxFieldLength(int maxFieldLength) throws IOException {
        this.maxFieldLength = maxFieldLength;
        this.getModifier().setMaxFieldLength(maxFieldLength);

        return this;
    }

    /**
     * 
     * @see org.jhaws.common.lucene.LuceneInterface#setMergeFactor(int)
     */
    @Override
    public LuceneInterface setMergeFactor(int mergeFactor) throws IOException {
        this.mergeFactor = mergeFactor;
        this.getModifier().setMergeFactor(mergeFactor);

        return this;
    }

    /**
     * converts a text in memory (string) to test.lucene Document
     * 
     * @param text documents text
     * @param title title
     * @param description description
     * 
     * @return {@link Document}
     */
    private Document textToDocument(final String text, final String title, final String description, final String... keywords) {
        Document doc = new Document();

        doc.add(new Field(ID.analyzer.f(), this.analyzer.analyzer().getClass().getName(), LuceneInterface.STORED, LuceneInterface.NOT_ANALYZED,
                LuceneInterface.NOTERM));
        doc.add(new Field(ID.description.f(), this.getDescription(description), LuceneInterface.STORED, LuceneInterface.ANALYZED,
                LuceneInterface.NOTERM));
        doc.add(new Field(ID.filetype.f(), FileType.TEXT.toString(), LuceneInterface.STORED, LuceneInterface.NOT_ANALYZED, LuceneInterface.NOTERM));
        doc.add(new Field(ID.keywords.f(), this.keywordsToString(keywords), LuceneInterface.STORED, LuceneInterface.ANALYZED, LuceneInterface.NOTERM));
        doc.add(new Field(ID.language.f(), this.analyzer.language(), LuceneInterface.STORED, LuceneInterface.NOT_ANALYZED, LuceneInterface.NOTERM));
        doc.add(new Field(ID.lastmod.f(), String.valueOf(new Date().getTime()), LuceneInterface.STORED, LuceneInterface.NOT_ANALYZED,
                LuceneInterface.NOTERM));
        doc.add(new Field(ID.size.f(), String.valueOf(text.length()), LuceneInterface.STORED, LuceneInterface.NOT_ANALYZED, LuceneInterface.NOTERM));
        doc.add(new Field(ID.text.f(), text, LuceneInterface.COMPRESSED, LuceneInterface.ANALYZED, LuceneInterface.TERM));
        doc.add(new Field(ID.url.f(), "doc://" + title, LuceneInterface.STORED, LuceneInterface.NOT_ANALYZED, LuceneInterface.NOTERM)); //$NON-NLS-1$

        return doc;
    }

    /**
     * 
     * @see org.jhaws.common.lucene.LuceneInterface#topDocTerms(int)
     */
    @Override
    public DocHighScoreTerms[] topDocTerms(int count) throws IOException {
        return this.topDocTerms(count, ID.text.f());
    }

    /**
     * 
     * @see org.jhaws.common.lucene.LuceneInterface#topDocTerms(int, java.lang.String)
     */
    @Override
    public DocHighScoreTerms[] topDocTerms(int count, String filter) throws IOException {
        if (count == -1) {
            count = Integer.MAX_VALUE;
        }

        ArrayList<Holder<DocHighScoreTerms>> data = new ArrayList<Holder<DocHighScoreTerms>>();
        IndexReader reader0 = this.getReader();
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
     * 
     * @see org.jhaws.common.lucene.LuceneInterface#topTerms(int)
     */
    @Override
    public HighScoreTerms[] topTerms(int count) throws IOException {
        IndexReader reader0 = this.getReader();
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

    /**
     * converts file on URL to Lucene document
     * 
     * @param url {@link URL}
     * @param lastmod time of last modification
     * @param description description
     * 
     * @return {@link Document}
     * 
     * @throws IOException IOException
     * @throws ConvertorNotSupportedException ConvertorNotSupportedException
     */
    private Document urlToDocument(final URL url, final long lastmod, final String description, final String... keywords) throws IOException,
            ConvertorNotSupportedException {
        String cleanurl = url.toString().replace('\\', '/');
        String shortName = cleanurl.substring(1 + cleanurl.lastIndexOf('/'));

        byte[] contents = this.getContents(url);
        LuceneIndex.logger.info("contents.length=" + contents.length); //$NON-NLS-1$

        Document doc = new Document();

        doc.add(new Field(ID.analyzer.f(), this.analyzer.analyzer().getClass().getName(), LuceneInterface.STORED, LuceneInterface.NOT_ANALYZED,
                LuceneInterface.NOTERM));
        doc.add(new Field(ID.description.f(), this.getDescription(description), LuceneInterface.STORED, LuceneInterface.ANALYZED,
                LuceneInterface.NOTERM));
        doc.add(new Field(ID.filetype.f(), FileType.URL.toString(), LuceneInterface.STORED, LuceneInterface.NOT_ANALYZED, LuceneInterface.NOTERM));
        doc.add(new Field(ID.keywords.f(), this.keywordsToString(keywords), LuceneInterface.STORED, LuceneInterface.ANALYZED, LuceneInterface.NOTERM));
        doc.add(new Field(ID.language.f(), this.analyzer.language(), LuceneInterface.STORED, LuceneInterface.NOT_ANALYZED, LuceneInterface.NOTERM));
        doc.add(new Field(ID.lastmod.f(), String.valueOf(lastmod), LuceneInterface.STORED, LuceneInterface.NOT_ANALYZED, LuceneInterface.NOTERM));
        doc.add(new Field(ID.size.f(), String.valueOf(contents.length), LuceneInterface.STORED, LuceneInterface.NOT_ANALYZED, LuceneInterface.NOTERM));
        doc.add(new Field(ID.url.f(), url.toString(), LuceneInterface.STORED, LuceneInterface.NOT_ANALYZED, LuceneInterface.NOTERM));

        IOFile tmp = new IOFile(shortName);
        LuceneIndex.logger.info("shortName=" + tmp); //$NON-NLS-1$

        FileOutputStream os = new FileOutputStream(tmp);
        os.write(contents);
        os.close();

        String text = ""; //$NON-NLS-1$

        if (DocumentFactory.supports(url.toString())) {
            text = DocumentFactory.getConvertor(tmp).getText(tmp);
        } else {
            throw new ConvertorNotSupportedException(url.toString());
        }

        LuceneIndex.logger.info("text.length=" + text.length()); //$NON-NLS-1$

        doc.add(new Field(ID.text.f(), text, LuceneInterface.COMPRESSED, LuceneInterface.ANALYZED, LuceneInterface.TERM));

        tmp.eraseOnExit();

        return doc;
    }
}
