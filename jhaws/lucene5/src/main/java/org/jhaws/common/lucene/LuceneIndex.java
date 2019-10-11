package org.jhaws.common.lucene;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.lucene.document.Field.Store.NO;
import static org.apache.lucene.document.Field.Store.YES;
import static org.apache.lucene.search.BooleanClause.Occur.MUST;
import static org.jhaws.common.lang.CollectionUtils8.collectList;
import static org.jhaws.common.lang.CollectionUtils8.groupBy;
import static org.jhaws.common.lang.CollectionUtils8.match;
import static org.jhaws.common.lang.CollectionUtils8.notContainedIn;
import static org.jhaws.common.lang.CollectionUtils8.optional;
import static org.jhaws.common.lang.CollectionUtils8.split;
import static org.jhaws.common.lang.CollectionUtils8.stream;
import static org.jhaws.common.lang.CollectionUtils8.streamDeepValues;
import static org.jhaws.common.lang.CollectionUtils8.toList;

import java.io.Closeable;
import java.io.IOException;
import java.io.StringReader;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.IntField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexUpgrader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.MultiFields;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.MatchAllDocsQuery;
import org.apache.lucene.search.PhraseQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.search.TotalHitCountCollector;
import org.apache.lucene.search.WildcardQuery;
import org.apache.lucene.search.postingshighlight.PostingsHighlighter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.MMapDirectory;
import org.apache.lucene.util.Bits;
import org.jhaws.common.io.FilePath;
import org.jhaws.common.lang.CollectionUtils8;
import org.jhaws.common.lang.functions.EConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// https://wiki.apache.org/lucene-java/ImproveIndexingSpeed
/**
 * @see http://stackoverflow.com/questions/8878448/lucene-good-practice-and-thread-safety
 * @see http://blog.swwomm.com/2013/07/tuning-lucene-to-get-most-relevant.html
 */
// SmartLifecycle, InitializingBean
// https://github.com/DmitryKey/luke
public class LuceneIndex implements Closeable {
    protected final Lock lock = new ReentrantLock();

    protected static final String WRITE_LOCK = "write.lock";

    protected static final Logger logger = LoggerFactory.getLogger(LuceneIndex.class);

    public static interface ForceRedo<F extends Indexable<? super F>> {
        void forceRedo(List<Map.Entry<F, F>> match, List<Map.Entry<F, F>> redo);
    }

    protected static final String LUCENE_METADATA = "LUCENE_METADATA";

    public static final String DOC_VERSION = "DOC_VERSION";

    public static final String DOC_UUID = "DOC_UUID";

    public static final String DOC_LASTMOD = "lastmod";

    protected Directory index;

    protected Analyzer indexAnalyzer;

    protected Analyzer searchAnalyzer;

    protected IndexSearcher indexSearcher;

    protected IndexWriter indexWriter;

    protected DirectoryReader indexReader;

    protected IndexWriterConfig indexWriterConfig;

    protected final FilePath dir;

    protected Integer docVersion;

    protected Long activity;

    protected long autoCloseWait = 60000l;

    protected boolean autoCloseWaitEnabled = false;

    protected Thread startupThread;

    protected int maxBatchSize = 100;

    // protected long writeLockTimeout = 10000l;

    public LuceneIndex() {
        this(FilePath.createTempDirectory("" + System.currentTimeMillis()));
    }

    public LuceneIndex(FilePath dir) {
        this.dir = dir;
    }

    protected Directory getIndex() {
        return optional(index, this::createIndex);
    }

    protected Directory createIndex() {
        if (dir.notExists()) {
            try (FSDirectory tmpDir = FSDirectory.open(dir.getPath());
                    IndexWriter tmpW = new IndexWriter(tmpDir, new IndexWriterConfig(getIndexAnalyzer()))) {
                Document tmpDoc = new Document();
                String uuid = uuid(tmpDoc).get(DOC_UUID);
                tmpW.addDocument(tmpDoc);
                tmpW.commit();
                tmpW.deleteDocuments(keyValueQuery(DOC_UUID, uuid).build());
                tmpW.commit();
                // tmpW.close();
                // tmpDir.close();
            } catch (IOException ex) {
                throw new UncheckedIOException(ex);
            }
        }
        new FilePath(dir, WRITE_LOCK).delete();
        MMapDirectory mMapDirectory;
        try {
            mMapDirectory = new MMapDirectory(dir.getPath() /* ,new SimpleFSLockFactory() */);
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
        if (MMapDirectory.UNMAP_SUPPORTED) {
            try {
                mMapDirectory.setUseUnmap(true);
            } catch (Exception ex) {
                //
            }
        }
        return index = mMapDirectory;
    }

    public int getVersion() {
        return docVersion;
    }

    protected void fixDocVersion() {
        if (docVersion != null) {
            return;
        }
        docVersion = 0;
        ScoreDoc metaDataScoreDocs = search1(keyValueQuery(LUCENE_METADATA, LUCENE_METADATA).build());
        Document metaDataDoc = new Document();
        if (metaDataScoreDocs != null) {
            metaDataDoc = getDoc(metaDataScoreDocs);
            docVersion = metaDataDoc.getField(DOC_VERSION).numericValue().intValue();
        }
        if (docVersion < 1) {
            docVersion++;
            List<Document> docs = searchAllDocs();
            deleteAll();
            List<Document> batch = new ArrayList<>();
            for (Document doc : docs) {
                batch.add(doc);
            }
            addDocs(batch);
            metaDataDoc = new Document();
            metaDataDoc.add(new StringField(LUCENE_METADATA, LUCENE_METADATA, YES));
            uuid(metaDataDoc);
            version(metaDataDoc, docVersion);
            addDocs(metaDataDoc);
            shutDown();
        }
        // if (currentDocVersion < 2) {
        // docVersion++;
        // metaDataDoc.removeField(DOC_VERSION);
        // metaDataDoc.add(new IntField(DOC_VERSION, 1, YES));
        // replaceDoc(metaDataDoc);
        // }
    }

    protected Document version(Document doc, int version) {
        return replaceValue(doc, DOC_VERSION, version, true);
    }

    protected Document uuid(Document doc) {
        return replaceValue(doc, DOC_UUID, newUuid(), true);
    }

    public String newUuid() {
        return java.util.UUID.randomUUID().toString();
    }

    protected Document version(Document doc) {
        if (docVersion != null) return replaceValue(doc, DOC_VERSION, docVersion, true);
        return doc;
    }

    public <F extends Indexable<? super F>> void replace(F indexable) {
        replaceDoc(indexable.indexable());
    }

    protected void replaceDoc(Document doc) {
        if (isBlank(doc.get(DOC_UUID))) {
            throw new IllegalArgumentException("doc does not contain " + DOC_UUID);
        }
        wtransaction(w -> {
            w.updateDocument(new Term(DOC_UUID, doc.get(DOC_UUID)), doc);
        });
        // wtransaction(w -> {
        // w.deleteDocuments(uuidQuery(doc.get(DOC_UUID)));
        // w.addDocument(doc);
        // });
    }

    protected Analyzer createIndexAnalyzer() {
        return indexAnalyzer = new LuceneIndexAnalyzer();
    }

    protected Analyzer getIndexAnalyzer() {
        return optional(indexAnalyzer, this::createIndexAnalyzer);
    }

    protected Analyzer createSearchAnalyzer() {
        return searchAnalyzer = new LuceneIndexAnalyzer();
    }

    public Analyzer getSearchAnalyzer() {
        return optional(searchAnalyzer, this::createSearchAnalyzer);
    }

    protected IndexWriter createIndexWriter() {
        try {
            return indexWriter = new IndexWriter(getIndex(), getIndexWriterConfig());
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }

    protected synchronized IndexWriter getIndexWriter() {
        activity = System.currentTimeMillis();
        fixDocVersion();
        return optional(indexWriter, this::createIndexWriter);
    }

    protected IndexWriterConfig createIndexWriterConfig() {
        return indexWriterConfig = new IndexWriterConfig(getIndexAnalyzer())/* .setWriteLockTimeout(writeLockTimeout) */;
    }

    // always creates a new index writer config
    protected IndexWriterConfig getIndexWriterConfig() {
        // return optional(indexWriterConfig, this::createIndexWriterConfig);
        return createIndexWriterConfig();
    }

    protected DirectoryReader createIndexReader() {
        try {
            return indexReader = DirectoryReader.open(getIndex());
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }

    protected DirectoryReader getIndexReader() {
        activity = System.currentTimeMillis();
        fixDocVersion();
        return optional(indexReader, this::createIndexReader);
    }

    protected IndexSearcher createIndexSearcher() {
        return indexSearcher = new IndexSearcher(getIndexReader());
    }

    protected IndexSearcher getIndexSearcher() {
        return optional(indexSearcher, this::createIndexSearcher);
    }

    public void shutDown() {
        lock.lock();
        try {
            if (index == null) return;
            index = null;
            logger.info("shutdown index@{}", dir);
            if (indexWriter != null) try {
                indexWriter.commit();
                indexWriter.forceMergeDeletes();
            } catch (Exception ex) {
                //
            }
            if (indexReader != null) try {
                indexReader.close();
            } catch (Exception ex) {
                //
            }
            if (indexWriter != null) try {
                indexWriter.close();
            } catch (Exception ex) {
                //
            }
            if (indexAnalyzer != null) try {
                indexAnalyzer.close();
            } catch (Exception ex) {
                //
            }
            indexReader = null;
            indexWriter = null;
            indexWriterConfig = null;
            indexAnalyzer = null;
            indexSearcher = null;
            activity = null;
        } finally {
            lock.unlock();
        }
    }

    protected void wtransaction(IndexWriterActionVoid action) {
        lock.lock();
        try {
            action.transaction(getIndexWriter());
            if (indexReader != null) {
                try {
                    indexReader.close();
                } catch (Exception ex) {
                    //
                }
            }
            indexReader = null;
            indexSearcher = null;
        } finally {
            lock.unlock();
        }
    }

    protected void rtransaction(IndexReaderActionVoid action) {
        lock.lock();
        try {
            action.transaction(getIndexReader());
        } finally {
            lock.unlock();
        }
    }

    protected void stransaction(IndexSearcherActionVoid action) {
        lock.lock();
        try {
            action.transaction(getIndexSearcher());
        } finally {
            lock.unlock();
        }
    }

    protected <T> T stransactionReturn(IndexSearcherAction<T> action) {
        lock.lock();
        try {
            return action.transaction(getIndexSearcher());
        } finally {
            lock.unlock();
        }
    }

    public FilePath getDir() {
        return dir;
    }

    public <F extends Indexable<? super F>> List<F> sync(List<F> indexed, List<F> fetched, Consumer<F> onDeleteOptional, Consumer<F> onCreateOptional,
            BiConsumer<F, F> onRematchOptional, ForceRedo<F> forceRedoOptional) {
        fetched.forEach(f -> {
            if (f.getUuid() == null) f.setUuid(newUuid());
            if (f.getVersion() == null) f.setVersion(docVersion);
        });

        Consumer<F> onDelete = optional(onDeleteOptional, (Supplier<Consumer<F>>) CollectionUtils8::consume);
        Consumer<F> onCreate = optional(onCreateOptional, (Supplier<Consumer<F>>) CollectionUtils8::consume);
        BiConsumer<F, F> onRematch2 = optional(onRematchOptional, (Supplier<BiConsumer<F, F>>) CollectionUtils8::biconsume);
        Consumer<Map.Entry<F, F>> onRematch = p -> onRematch2.accept(p.getKey(), p.getValue());

        List<F> delete = indexed.stream()
                .parallel() //
                .filter(notContainedIn(fetched))
                .collect(collectList());
        List<F> create = fetched.stream()
                .parallel() //
                .filter(notContainedIn(indexed))
                .collect(collectList());

        List<Map.Entry<F, F>> match = match(indexed, fetched);
        List<Map.Entry<F, F>> redo = match.stream()
                .parallel() //
                .filter(p -> p.getValue().getLastmodified() != null && p.getValue().getLastmodified().isAfter(p.getKey().getLastmodified()))
                .collect(collectList());
        if (forceRedoOptional != null) {
            forceRedoOptional.forceRedo(match, redo);
        }
        match.removeAll(redo);

        redo.stream()
                .parallel() //
                .forEach(onRematch.andThen(p -> log("sync:index:redo", p)));

        if (redo.size() > 0) logger.info("*{}", redo.size());
        redo.stream().map(Map.Entry::getKey).forEach(delete::add);
        redo.stream().map(Map.Entry::getValue).forEach(create::add);

        if (delete.size() - redo.size() > 0) logger.info("-{}", delete.size() - redo.size());
        if (create.size() - redo.size() > 0) logger.info("+{}", create.size() - redo.size());
        delete.stream()
                .parallel() //
                .forEach(onDelete.andThen(e -> log("sync:index:delete", e)));
        create.stream()
                .parallel() //
                .forEach(onCreate.andThen(f -> log("sync:index:create", f)));

        wtransaction(w -> delete.stream().map(Indexable::term).forEach(EConsumer.enhance(w::deleteDocuments)));
        wtransaction(w -> create.stream().map(Indexable::indexable).forEach(EConsumer.enhance(this::addDocs)));

        List<F> result = new ArrayList<>(create);
        // do not change to parallelStream or it will add null values
        match.stream().map(Map.Entry::getKey).forEach(result::add);
        return result;
    }

    protected <L> L log(String prefix, L l) {
        logger.trace("{}: {}", prefix, l);
        return l;
    }

    public void startUp() throws Exception {
        if (startupThread == null) {
            startupThread = new Thread(() -> {
                while (autoCloseWaitEnabled) {
                    try {
                        Thread.sleep(autoCloseWait);
                    } catch (InterruptedException ex) {
                        //
                    }
                    if (activity != null && System.currentTimeMillis() - activity > autoCloseWait) {
                        shutDown();
                    }
                }
            }, getClass().getName() + "[" + hashCode() + "]");
            startupThread.setDaemon(true);
            startupThread.start();
        }
    }

    public <F extends Indexable<? super F>> void delete(@SuppressWarnings("unchecked") F... indexables) {
        delete(toList(indexables));
    }

    public <F extends Indexable<? super F>> void delete(Collection<F> indexables) {
        deleteDocs(indexables.stream()
                .parallel() //
                .map(Indexable::indexable)
                .collect(collectList()));
    }

    protected void deleteDocs(Document... docs) {
        deleteDocs(toList(docs));
    }

    protected void deleteDocs(Collection<Document> docs) {
        split(docs, maxBatchSize).stream().forEach(batch -> wtransaction(w -> batch.stream().forEach(EConsumer.enhance(doc -> {
            w.deleteDocuments(uuidQuery(doc.get(DOC_UUID).toString()));
            w.forceMergeDeletes();
        }))));
    }

    public ScoreDoc search1(Query query) {
        return search(query, 1).stream().findFirst().orElse(null);
    }

    public List<ScoreDoc> search(Query query, int max) {
        ScoreDoc[] scoreDocs = score(query, max).scoreDocs;
        logger.info("{} -> #{}", query, scoreDocs.length);
        return toList(scoreDocs);
    }

    public TopDocs score(Query query, int max) {
        TopScoreDocCollector collector = TopScoreDocCollector.create(max);
        stransaction(s -> s.search(query, collector));
        return collector.topDocs();
    }

    public BooleanQuery.Builder searchAllQuery() {
        BooleanQuery.Builder query = new BooleanQuery.Builder();
        query.add(new MatchAllDocsQuery(), BooleanClause.Occur.MUST);
        keyValueQuery(query, LuceneIndex.LUCENE_METADATA, LuceneIndex.LUCENE_METADATA, BooleanClause.Occur.MUST_NOT);
        return query;
    }

    public <F extends Indexable<? super F>> F get(ScoreDoc hit, Supplier<F> indexable) {
        return get(getDoc(hit), indexable);
    }

    @SuppressWarnings("unchecked")
    protected <F extends Indexable<? super F>> F get(Document doc, Supplier<F> indexable) {
        return (F) indexable.get().retrieve(doc);
    }

    public Document getDoc(ScoreDoc hit) {
        return stransactionReturn(s -> s.doc(hit.doc));
    }

    public BooleanQuery.Builder keyValueQuery(BooleanQuery.Builder booleanQuery, String key, String value) {
        return keyValueQuery(booleanQuery, key, value, MUST);
    }

    public BooleanQuery.Builder keyValueQuery(BooleanQuery.Builder booleanQuery, String key, String value, BooleanClause.Occur occur) {
        return booleanQuery.add(new TermQuery(new Term(key, value)), occur);
    }

    public BooleanQuery.Builder keyValueQuery(String key, String value) {
        return keyValueQuery(new BooleanQuery.Builder(), key, value);
    }

    public <T> void deleteDuplicates(Query query, int max, Function<Document, T> groupBy, Comparator<Document> comparator, Consumer<Document> after) {
        Consumer<Document> deleter = doc -> deleteDocs(doc);
        Consumer<Document> action = after == null ? deleter : deleter.andThen(after);
        streamDeepValues(groupBy(stream(search(query, max)).map(hit -> getDoc(hit)), groupBy))
                .forEach(stream -> stream.sorted(comparator).skip(1).forEach(action));
    }

    public void addIndexable(Indexable<?>... indexables) {
        addIndexables(toList(indexables));
    }

    public void addIndexables(Collection<Indexable<?>> indexables) {
        addDocs(indexables.stream()
                .parallel() //
                .map(Indexable::indexable)
                .collect(collectList()));
    }

    public void addDocs(Document... docs) {
        List<Document> list = toList(docs);
        addDocs(list);
    }

    public void addDocs(Collection<Document> docs) {
        // System.out.println("+" + docs.size());
        docs.stream()
                .parallel() //
                .filter(d -> isBlank(d.get(DOC_UUID)))
                .forEach(this::uuid);
        docs.stream()
                .parallel() //
                .filter(d -> d.getField(DOC_VERSION) == null)
                .forEach(this::version);
        split(docs, maxBatchSize).stream().forEach(batch -> wtransaction(w -> w.addDocuments(batch)));
    }

    public <F extends Indexable<? super F>> List<F> searchAll(Supplier<F> indexable) {
        return searchAllDocs().stream().filter(doc -> doc.getField(LUCENE_METADATA) == null).map(doc -> get(doc, indexable)).collect(collectList());
    }

    public List<Document> searchAllDocs() {
        List<Document> documents = new ArrayList<>();
        rtransaction(r -> {
            Bits liveDocs = MultiFields.getLiveDocs(r);
            for (int i = 0; i < r.maxDoc(); i++) {
                if (liveDocs != null && !liveDocs.get(i)) continue;
                try {
                    documents.add(r.document(i));
                } catch (IOException ex) {
                    throw new UncheckedIOException(ex);
                }
            }
        });
        return documents;
    }

    public void deleteAll() {
        wtransaction(w -> {
            w.deleteAll();
            w.forceMergeDeletes();
        });
        shutDown();
    }

    public Document replaceValue(Document doc, String key, String value, boolean store) {
        doc.removeField(key);
        doc.add(new StringField(key, value, store ? YES : NO));
        return doc;
    }

    public Document replaceValueLongText(Document doc, String key, String value, boolean store) {
        doc.removeField(key);
        doc.add(new TextField(key, value, store ? YES : NO));
        return doc;
    }

    public Document replaceValue(Document doc, String key, int value, boolean store) {
        doc.removeField(key);
        doc.add(new IntField(key, value, store ? YES : NO));
        return doc;
    }

    public BooleanQuery uuidQuery(String uuid) {
        return keyValueQuery(DOC_UUID, uuid).build();
    }

    public long getAutoCloseWait() {
        return autoCloseWait;
    }

    public void setAutoCloseWait(long autoCloseWait) {
        this.autoCloseWait = autoCloseWait;
    }

    public int getMaxBatchSize() {
        return maxBatchSize;
    }

    public void setMaxBatchSize(int maxBatchSize) {
        this.maxBatchSize = maxBatchSize;
    }

    public void delete(Query query) {
        wtransaction(w -> {
            w.deleteDocuments(query);
            w.forceMergeDeletes();
        });
    }

    public QueryParser newQueryParser(String field) {
        QueryParser queryParser = new QueryParser(field, getSearchAnalyzer());
        queryParser.setAllowLeadingWildcard(true);
        return queryParser;
    }

    public boolean getAutoCloseWaitEnabled() {
        return autoCloseWaitEnabled;
    }

    public void setAutoCloseWaitEnabled(boolean autoCloseWaitEnabled) {
        this.autoCloseWaitEnabled = autoCloseWaitEnabled;
    }

    // public long getWriteLockTimeout() {
    // return this.writeLockTimeout;
    // }

    // public void setWriteLockTimeout(long writeLockTimeout) {
    // this.writeLockTimeout = writeLockTimeout;
    // }

    public void setIndex(Directory index) {
        this.index = index;
    }

    public void setIndexAnalyzer(Analyzer indexAnalyzer) {
        this.indexAnalyzer = indexAnalyzer;
    }

    public void setIndexSearcher(IndexSearcher indexSearcher) {
        this.indexSearcher = indexSearcher;
    }

    public void setIndexWriter(IndexWriter indexWriter) {
        this.indexWriter = indexWriter;
    }

    public void setIndexReader(DirectoryReader indexReader) {
        this.indexReader = indexReader;
    }

    public void setIndexWriterConfig(IndexWriterConfig indexWriterConfig) {
        this.indexWriterConfig = indexWriterConfig;
    }

    public Query buildQuery(String phrase, String defaultField) {
        return buildQuery(phrase, new ArrayList<>(), defaultField);
    }

    @SuppressWarnings("deprecation")
    public Query buildQuery(String phrase, List<String> fields, String defaultField) {
        List<String> tokens;
        try {
            tokens = tokenizePhrase(phrase);
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
        if (tokens.isEmpty()) return null;
        if (fields == null) fields = new ArrayList<>();
        if (tokens.size() == 1) {
            String term = tokens.get(0);
            if (term.indexOf('*') == -1) {
                term = term + "*";
            }
            fields.add(0, defaultField);
            // BooleanQuery b = new BooleanQuery();
            // PhraseQuery p = new PhraseQuery();
            // for (int i = 0; i < fields.size(); ++i) {
            // p.add(new Term(fields.get(i), term));
            // }
            // b.add(p, BooleanClause.Occur.SHOULD);
            BooleanQuery b = new BooleanQuery();
            for (int i = 0; i < fields.size(); ++i) {
                b.add(new WildcardQuery(new Term(fields.get(i), term)), BooleanClause.Occur.MUST);
            }
            return b;
        }
        BooleanQuery q = new BooleanQuery();
        // create term combinations if there are multiple words in the query
        if (tokens.size() > 1) {
            // exact-phrase query
            PhraseQuery phraseQ = new PhraseQuery();
            for (int w = 0; w < tokens.size(); w++)
                phraseQ.add(new Term(defaultField, tokens.get(w)));
            phraseQ.setBoost(tokens.size() * 5);
            phraseQ.setSlop(2);
            q.add(phraseQ, BooleanClause.Occur.SHOULD);
            // 2 out of 4, 3 out of 4, 4 out of 4 (any order), etc
            // stop at 7 in case user enters a pathologically long query
            int maxRequired = Math.min(tokens.size(), 7);
            for (int minRequired = 2; minRequired <= maxRequired; minRequired++) {
                BooleanQuery comboQ = new BooleanQuery();
                for (int w = 0; w < tokens.size(); w++)
                    comboQ.add(new TermQuery(new Term(defaultField, tokens.get(w))), BooleanClause.Occur.SHOULD);
                comboQ.setBoost(minRequired * 3);
                comboQ.setMinimumNumberShouldMatch(minRequired);
                q.add(comboQ, BooleanClause.Occur.SHOULD);
            }
        }
        // create an individual term query for each word for each field
        for (int w = 0; w < tokens.size(); w++)
            for (int f = 0; f < fields.size(); f++)
                q.add(new TermQuery(new Term(fields.get(f), tokens.get(w))), BooleanClause.Occur.SHOULD);
        return q;
    }

    protected List<String> tokenizePhrase(String phrase) throws IOException {
        List<String> tokens = new ArrayList<>();
        TokenStream stream = getSearchAnalyzer().tokenStream("someField", new StringReader(phrase));
        stream.reset();
        while (stream.incrementToken())
            tokens.add(stream.getAttribute(CharTermAttribute.class).toString());
        stream.end();
        stream.close();
        return tokens;
    }

    public List<HighlightResult> highlight(Query query, TopDocs topDocs, String field) {
        return stransactionReturn(s -> {
            PostingsHighlighter highlighter = new PostingsHighlighter();
            // select up to the three best highlights from the "all" field
            // of each result, concatenated with ellipses
            String[] highlights = highlighter.highlight(field, query, s, topDocs, 3);
            int length = topDocs.scoreDocs.length;
            List<HighlightResult> results = new ArrayList<>(length);
            for (int i = 0; i < length; i++) {
                int docId = topDocs.scoreDocs[i].doc;
                results.add(new HighlightResult(s.doc(docId), highlights[i]));
            }
            return results;
        });
    }

    public class HighlightResult {
        final public Document document;

        final public String highlights;

        public HighlightResult(Document document, String highlights) {
            this.document = document;
            this.highlights = highlights;
        }

        public Document getDocument() {
            return this.document;
        }

        public String getHighlights() {
            return this.highlights;
        }
    }

    public boolean upgrade(boolean deleteIfUpgradeFails) {
        try {
            // https://lucene.apache.org/core/5_3_0/MIGRATE.html
            new IndexUpgrader(getIndex(), getIndexWriterConfig(), false).upgrade();
            return true;
        } catch (org.apache.lucene.index.IndexFormatTooOldException ex) {
            if (deleteIfUpgradeFails) {
                try {
                    dir.delete();
                } catch (Exception ex2) {
                    ex.printStackTrace();
                    ex2.printStackTrace();
                }
            } else {
                ex.printStackTrace();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                shutDown();
            } catch (Exception ex2) {
                //
            }
        }
        return false;
    }

    @Override
    public void close() {
        shutDown();
    }

    public int count(Query query) {
        TotalHitCountCollector collector = new TotalHitCountCollector();
        stransaction(s -> s.search(query, collector));
        return collector.getTotalHits();
    }
}
