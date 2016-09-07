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

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.IntField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.MultiFields;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.MatchAllDocsQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.MMapDirectory;
import org.apache.lucene.util.Bits;
import org.apache.lucene.util.Version;
import org.jhaws.common.io.FilePath;
import org.jhaws.common.lang.CollectionUtils8;
import org.jhaws.common.lang.functions.EConsumer;
import org.jhaws.common.lang.functions.ESupplier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @see http://stackoverflow.com/questions/8878448/lucene-good-practice-and-thread-safety
 */
public class LuceneIndex implements Runnable {
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

	protected IndexSearcher indexSearcher;

	protected IndexWriter indexWriter;

	protected DirectoryReader indexReader;

	protected IndexWriterConfig indexWriterConfig;

	protected final FilePath dir;

	protected Integer docVersion;

	protected Long activity;

	protected long autoCloseWait = 60000l;

	protected boolean autoCloseWaitEnabled = false;

	protected int maxBatchSize = 1000;

	public LuceneIndex(FilePath dir) {
		this.dir = dir;
		Thread t = new Thread(this);
		t.setDaemon(true);
		t.start();
	}

	protected Directory getIndex() {
		return optional(index, this::initIndex);
	}

	protected Directory initIndex() {
		if (dir.notExists()) {
			try (FSDirectory tmpDir = FSDirectory.open(dir.toFile()); IndexWriter tmpW = new IndexWriter(tmpDir, new IndexWriterConfig(Version.LATEST, new StandardAnalyzer()))) {
				Document tmpDoc = new Document();
				String uuid = uuid(tmpDoc).get(DOC_UUID);
				tmpW.addDocument(tmpDoc);
				tmpW.commit();
				tmpW.deleteDocuments(keyValueQuery(DOC_UUID, uuid));
				tmpW.commit();
				tmpW.close();
				tmpDir.close();
			} catch (IOException ex) {
				throw new UncheckedIOException(ex);
			}
		}
		new FilePath(dir, "write.lock").deleteIfExists();
		MMapDirectory mMapDirectory;
		try {
			mMapDirectory = new MMapDirectory(dir.toFile()/* ,new SimpleFSLockFactory() */);
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
		ScoreDoc metaDataScoreDocs = search1(keyValueQuery(LUCENE_METADATA, LUCENE_METADATA));
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
		return replaceValue(doc, DOC_VERSION, docVersion, true);
	}

	public <F extends Indexable<? super F>> void replace(F indexable) {
		replaceDoc(indexable.indexable());
	}

	protected void replaceDoc(Document doc) {
		if (isBlank(doc.get(DOC_UUID))) {
			throw new IllegalArgumentException("doc does not contain " + DOC_UUID);
		}
		transaction(w -> {
			w.deleteDocuments(uuidQuery(doc.get(DOC_UUID)));
			w.addDocument(doc);
		});
	}

	protected Analyzer getIndexAnalyzer() {
		return optional(indexAnalyzer, () -> indexAnalyzer = new StandardAnalyzer());
	}

	protected IndexWriter getIndexWriter() {
		activity = System.currentTimeMillis();
		fixDocVersion();
		return optional(indexWriter, ESupplier.enhance(() -> indexWriter = new IndexWriter(getIndex(), getIndexWriterConfig())));
	}

	protected IndexWriterConfig getIndexWriterConfig() {
		return optional(indexWriterConfig, () -> indexWriterConfig = new IndexWriterConfig(Version.LATEST, getIndexAnalyzer()).setWriteLockTimeout(10000l));
	}

	protected DirectoryReader getIndexReader() {
		activity = System.currentTimeMillis();
		fixDocVersion();
		return optional(indexReader, ESupplier.enhance(() -> indexReader = DirectoryReader.open(getIndex())));
	}

	protected IndexSearcher getIndexSearcher() {
		return optional(indexSearcher, () -> indexSearcher = new IndexSearcher(getIndexReader()));
	}

	public void shutDown() {
		if (index == null)
			return;
		index = null;
		logger.info("shutdown index@{}", dir);
		try {
			indexWriter.commit();
			indexWriter.forceMergeDeletes();
		} catch (Exception ex) {
			//
		}
		try {
			indexReader.close();
		} catch (Exception ex) {
			//
		}
		try {
			indexWriter.close();
		} catch (Exception ex) {
			//
		}
		try {
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
	}

	protected void transaction(IndexWriterAction action) {
		action.transaction(getIndexWriter());
		try {
			if (indexReader != null)
				indexReader.close();
		} catch (Exception ex) {
			//
		}
		indexReader = null;
		indexSearcher = null;
	}

	public FilePath getDir() {
		return dir;
	}

	public <F extends Indexable<? super F>> List<F> sync(List<F> indexed, List<F> fetched, Consumer<F> onDeleteOptional, Consumer<F> onCreateOptional,
			BiConsumer<F, F> onRematchOptional, ForceRedo<F> forceRedoOptional) {
		fetched.forEach(f -> {
			if (f.getUuid() == null)
				f.setUuid(newUuid());
			if (f.getVersion() == null)
				f.setVersion(docVersion);
		});

		Consumer<F> onDelete = optional(onDeleteOptional, (Supplier<Consumer<F>>) CollectionUtils8::consume);
		Consumer<F> onCreate = optional(onCreateOptional, (Supplier<Consumer<F>>) CollectionUtils8::consume);
		BiConsumer<F, F> onRematch2 = optional(onRematchOptional, (Supplier<BiConsumer<F, F>>) CollectionUtils8::biconsume);
		Consumer<Map.Entry<F, F>> onRematch = p -> onRematch2.accept(p.getKey(), p.getValue());

		List<F> delete = indexed.parallelStream().filter(notContainedIn(fetched)).collect(collectList());
		List<F> create = fetched.parallelStream().filter(notContainedIn(indexed)).collect(collectList());

		List<Map.Entry<F, F>> match = match(indexed, fetched);
		List<Map.Entry<F, F>> redo = match.parallelStream().filter(p -> p.getValue().getLastmodified().isAfter(p.getKey().getLastmodified())).collect(collectList());
		if (forceRedoOptional != null) {
			forceRedoOptional.forceRedo(match, redo);
		}
		match.removeAll(redo);

		redo.parallelStream().forEach(onRematch.andThen(p -> log("sync:index:redo", p)));

		if (redo.size() > 0)
			logger.info("*{}", redo.size());
		redo.stream().map(Map.Entry::getKey).forEach(delete::add);
		redo.stream().map(Map.Entry::getValue).forEach(create::add);

		if (delete.size() - redo.size() > 0)
			logger.info("-{}", delete.size() - redo.size());
		if (create.size() - redo.size() > 0)
			logger.info("+{}", create.size() - redo.size());
		delete.parallelStream().forEach(onDelete.andThen(e -> log("sync:index:delete", e)));
		create.parallelStream().forEach(onCreate.andThen(f -> log("sync:index:create", f)));

		transaction(w -> delete.stream().map(Indexable::term).forEach(EConsumer.enhance(w::deleteDocuments)));
		transaction(w -> create.stream().map(Indexable::indexable).forEach(EConsumer.enhance(this::addDocs)));

		List<F> result = new ArrayList<>(create);
		match.stream().map(Map.Entry::getKey).forEach(result::add); // do not change to parallelStream or it will add null values
		return result;
	}

	protected <L> L log(String prefix, L l) {
		logger.debug("{}: {}", prefix, l);
		return l;
	}

	public void startUp() throws Exception {
		//
	}

	public <F extends Indexable<? super F>> void delete(@SuppressWarnings("unchecked") F... indexables) {
		delete(toList(indexables));
	}

	public <F extends Indexable<? super F>> void delete(Collection<F> indexables) {
		deleteDocs(indexables.stream().parallel().map(Indexable::indexable).collect(collectList()));
	}

	protected void deleteDocs(Document... docs) {
		deleteDocs(toList(docs));
	}

	protected void deleteDocs(Collection<Document> docs) {
		split(docs, maxBatchSize).stream()
				.forEach(batch -> transaction(w -> batch.stream().forEach(EConsumer.enhance(doc -> w.deleteDocuments(uuidQuery(doc.get(DOC_UUID).toString()))))));
	}

	public ScoreDoc search1(Query query) {
		return search(query, 1).stream().findFirst().orElse(null);
	}

	public List<ScoreDoc> search(Query query, int max) {
		TopScoreDocCollector collector = TopScoreDocCollector.create(max, true);
		try {
			getIndexSearcher().search(query, collector);
		} catch (IOException ex) {
			throw new UncheckedIOException(ex);
		}
		return toList(collector.topDocs().scoreDocs);
	}

	public BooleanQuery searchAllQuery() {
		BooleanQuery query = new BooleanQuery();
		query.add(new MatchAllDocsQuery(), BooleanClause.Occur.MUST);
		query.add(keyValueQuery(LuceneIndex.LUCENE_METADATA, LuceneIndex.LUCENE_METADATA), BooleanClause.Occur.MUST_NOT);
		return query;
	}

	public <F extends Indexable<? super F>> F get(ScoreDoc hit, Supplier<F> indexable) {
		return get(getDoc(hit), indexable);
	}

	@SuppressWarnings("unchecked")
	protected <F extends Indexable<? super F>> F get(Document doc, Supplier<F> indexable) {
		return (F) indexable.get().retrieve(doc);
	}

	protected Document getDoc(ScoreDoc hit) {
		try {
			return getIndexSearcher().doc(hit.doc);
		} catch (IOException ex) {
			throw new UncheckedIOException(ex);
		}
	}

	public BooleanQuery keyValueQuery(BooleanQuery booleanQuery, String key, String value) {
		booleanQuery.add(new TermQuery(new Term(key, value)), MUST);
		return booleanQuery;
	}

	public BooleanQuery keyValueQuery(String key, String value) {
		return keyValueQuery(new BooleanQuery(), key, value);
	}

	public <T> void deleteDuplicates(Query query, int max, Function<Document, T> groupBy, Comparator<Document> comparator, Consumer<Document> after) {
		Consumer<Document> deleter = doc -> deleteDocs(doc);
		Consumer<Document> action = after == null ? deleter : deleter.andThen(after);
		streamDeepValues(groupBy(stream(search(query, max)).map(hit -> getDoc(hit)), groupBy)).forEach(stream -> stream.sorted(comparator).skip(1).forEach(action));
	}

	public void add(Indexable<?>... indexables) {
		add(toList(indexables));
	}

	public void add(Collection<Indexable<?>> indexables) {
		addDocs(indexables.stream().parallel().map(Indexable::indexable).collect(collectList()));
	}

	protected void addDocs(Document... docs) {
		addDocs(toList(docs));
	}

	protected void addDocs(Collection<Document> docs) {
		docs.stream().parallel().filter(d -> isBlank(d.get(DOC_UUID))).forEach(this::uuid);
		docs.stream().parallel().filter(d -> d.getField(DOC_VERSION) == null).forEach(this::version);
		split(docs, maxBatchSize).stream().forEach(batch -> transaction(w -> w.addDocuments(batch)));
	}

	public <F extends Indexable<? super F>> List<F> searchAll(Supplier<F> indexable) {
		return searchAllDocs().stream().filter(doc -> doc.getField(LUCENE_METADATA) == null).map(doc -> get(doc, indexable)).collect(collectList());
	}

	protected List<Document> searchAllDocs() {
		DirectoryReader reader = getIndexReader();
		Bits liveDocs = MultiFields.getLiveDocs(reader);
		List<Document> documents = new ArrayList<>();
		for (int i = 0; i < reader.maxDoc(); i++) {
			if (liveDocs != null && !liveDocs.get(i))
				continue;
			try {
				documents.add(reader.document(i));
			} catch (IOException ex) {
				throw new UncheckedIOException(ex);
			}
		}
		return documents;
	}

	public void deleteAll() {
		transaction(w -> w.deleteAll());
		shutDown();
	}

	public Document replaceValue(Document doc, String key, String value, boolean store) {
		doc.removeField(key);
		doc.add(new StringField(key, value, store ? YES : NO));
		return doc;
	}

	public Document replaceValue(Document doc, String key, int value, boolean store) {
		doc.removeField(key);
		doc.add(new IntField(key, value, store ? YES : NO));
		return doc;
	}

	public BooleanQuery uuidQuery(String uuid) {
		return keyValueQuery(DOC_UUID, uuid);
	}

	@Override
	public void run() {
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
		transaction(writer -> writer.deleteDocuments(query));
	}

	public QueryParser newQueryParser(String field) {
		QueryParser queryParser = new QueryParser(field, getIndexAnalyzer());
		queryParser.setAllowLeadingWildcard(true);
		return queryParser;
	}

	public boolean getAutoCloseWaitEnabled() {
		return autoCloseWaitEnabled;
	}

	public void setAutoCloseWaitEnabled(boolean autoCloseWaitEnabled) {
		this.autoCloseWaitEnabled = autoCloseWaitEnabled;
	}
}
