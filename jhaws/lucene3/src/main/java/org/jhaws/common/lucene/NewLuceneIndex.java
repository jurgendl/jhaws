package org.jhaws.common.lucene;

import static org.apache.lucene.document.DateTools.Resolution.MILLISECOND;
import static org.apache.lucene.document.Field.Index.ANALYZED;
import static org.apache.lucene.document.Field.Index.NOT_ANALYZED;
import static org.apache.lucene.document.Field.TermVector.WITH_POSITIONS_OFFSETS;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;
import java.util.Date;
import java.util.Locale;

import org.apache.lucene.document.DateTools;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriter.MaxFieldLength;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Searcher;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.LockObtainFailedException;
import org.apache.lucene.util.Version;
import org.jhaws.common.io.IODirectory;
import org.jhaws.common.io.IOFile;

/**
 * na
 * 
 * @see http://lingpipe-blog.com/2008/11/05/updating-and-deleting-documents-in-lucene-24-lingmed-case-study/
 */
@SuppressWarnings("deprecation")
public class NewLuceneIndex {
	/** doc:// handler */
	public static final URLStreamHandler DOC_HANDLER = new URLStreamHandler() {
		@Override
		protected URLConnection openConnection(URL u) throws IOException {
			return null;
		}
	};

	/** excape characters "+-&|!(){}[]^~?:\\\"" */
	public static final char[] ESCAPE_CHARACTERS = "+-&|!(){}[]^~*?:\\\"".toCharArray(); //$NON-NLS-1$

	/**
	 * na
	 * 
	 * @param date
	 * 
	 * @return
	 */
	protected static String internal_convertdate(Date date) {
		return DateTools.timeToString(date.getTime(), MILLISECOND);
	}

	/**
	 * na
	 * 
	 * @param string
	 * 
	 * @return
	 */
	protected static String internal_escapeText(String string) {
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < string.length(); i++) {
			char c = string.charAt(i);

			for (char element : NewLuceneIndex.ESCAPE_CHARACTERS) {
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
	 * @param keywords
	 *            keywords as array
	 * 
	 * @return keywords as string
	 */
	protected static String internal_keywordsToString(final String[] keywords) {
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
	protected static byte[] internal_readFromStream(final InputStream is) throws IOException {
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
	 * na
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			URL u = new URL("doc", "localhost", 0, "/file", NewLuceneIndex.DOC_HANDLER);
			System.out.println(u);
			System.out.println(new URL("doc://file"));
		} catch (Exception ex) {
			ex.printStackTrace(System.out);
		}
	}

	/** directory */
	protected final Directory directory;

	/** analyzer */
	protected Analyzer analyzer = Analyzer.fetch(Locale.getDefault());

	/** LuceneInterface */
	protected final LuceneInterface myself;

	/** _IndexReader */
	protected IndexReader _IndexReader;

	/** _IndexReaderRO */
	protected IndexReader _IndexReaderRO;

	/** _IndexSearcher */
	protected IndexSearcher _IndexSearcher;

	/** _IndexWriter */
	protected IndexWriter _IndexWriter;

	/**
	 * Creates a new NewLuceneIndex object.
	 * 
	 * @param ioDirectory
	 * 
	 * @throws IOException
	 */
	public NewLuceneIndex(IODirectory ioDirectory) throws IOException {
		super();
		this.myself = null;
		this.directory = FSDirectory.open(ioDirectory);
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
	public Document addFile(IOFile file, String[] keywords, String description) throws IOException {
		return this.internal_add(this.internal_document_file(file, keywords, description));
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
	public Document addTitle(String title, String[] keywords, String description, String text) throws IOException {
		return this.internal_add(this.internal_document_text(title, keywords, description, text));
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
	public Document addURL(URL url, String[] keywords, String description) throws IOException {
		return this.internal_add(this.internal_document_url(url, keywords, description));
	}

	/**
	 * na
	 * 
	 * @return
	 * 
	 * @throws IOException
	 */
	public ResultList all() throws IOException {
		IndexReader reader = this.internal_getIndexReader_readOnly();
		ResultList list = new ResultList(this.myself, null, null, null, reader.maxDoc());

		for (int i = 0; i < reader.maxDoc(); i++) {
			Document doc = reader.document(i);
			list.addSearchResult(new SearchResult(i, list, doc, i, 0, this.myself));
		}

		// reader.close();
		return list;
	}

	/**
	 * close all
	 * 
	 * @throws IOException
	 */
	public void close() throws IOException {
		this.internal_close_reader();
		this.internal_close_readerRO();
		this.internal_close_searcher();
		this.internal_close_writer();
	}

	/**
	 * na
	 * 
	 * @return
	 * 
	 * @throws IOException
	 */
	public int count() throws IOException {
		IndexReader reader = this.internal_getIndexReader_readOnly();
		int count = reader.maxDoc();

		// reader.close();
		return count;
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
		return this.internal_delete(file.toURI().toURL().toString());
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
		return this.internal_delete(title);
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
		return this.internal_delete(url.toString());
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
	protected Document internal_add(Document document) throws IOException {
		IndexWriter writer = this.internal_getIndexWriter();
		writer.addDocument(document);
		writer.commit();

		// writer.close(true);
		return document;
	}

	/**
	 * na
	 * 
	 * @return
	 * 
	 * @throws IOException
	 */
	protected IndexReader internal_close_reader() throws IOException {
		if (this._IndexReader == null) {
			return null;
		}

		this._IndexReader.close();

		IndexReader tmp = this._IndexReader;
		this._IndexReader = null;

		return tmp;
	}

	/**
	 * na
	 * 
	 * @return
	 * 
	 * @throws IOException
	 */
	protected IndexReader internal_close_readerRO() throws IOException {
		if (this._IndexReaderRO == null) {
			return null;
		}

		this._IndexReaderRO.close();

		IndexReader tmp = this._IndexReaderRO;
		this._IndexReaderRO = null;

		return tmp;
	}

	/**
	 * na
	 * 
	 * @throws IOException
	 */
	protected void internal_close_searcher() throws IOException {
		if (this._IndexSearcher == null) {
			return;
		}

		this._IndexSearcher.close();
		this._IndexSearcher = null;
	}

	/**
	 * na
	 * 
	 * @throws IOException
	 */
	protected void internal_close_writer() throws IOException {
		if (this._IndexWriter == null) {
			return;
		}

		this._IndexWriter.close();
		this._IndexWriter = null;
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
	protected int internal_delete(String id) throws IOException {
		// IndexReader reader = internal_getIndexReader_writable();
		// reader.deleteDocument(internal_find_record_result(id).id);
		//
		// //reader.close();
		IndexWriter writer = this.internal_getIndexWriter();
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
	 * @param type
	 * @param url
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
	protected Document internal_document(FileType type, String url, long size, String[] keywords, String description, Date lastmod, String text) throws IOException {
		Document doc = new Document();

		doc.add(new Field(ID.analyzer.f(), ID.analyzer.f(), Store.YES, NOT_ANALYZED));
		doc.add(new Field(ID.url.f(), url, Store.YES, NOT_ANALYZED));
		doc.add(new Field(ID.lastmod.f(), NewLuceneIndex.internal_convertdate(lastmod), Store.YES, NOT_ANALYZED)); // mod_date:[20020101 TO 20030101]
		doc.add(new Field(ID.text.f(), text, Store.NO, ANALYZED, WITH_POSITIONS_OFFSETS));
		doc.add(new Field(ID.description.f(), (description == null) ? "" : description, Store.YES, ANALYZED));
		doc.add(new Field(ID.keywords.f(), NewLuceneIndex.internal_keywordsToString(keywords), Store.YES, ANALYZED));
		doc.add(new Field(ID.size.f(), String.valueOf(size), Store.YES, NOT_ANALYZED));
		doc.add(new Field(ID.language.f(), this.internal_getAnalyzer().language(), Store.YES, NOT_ANALYZED));
		doc.add(new Field(ID.filetype.f(), type.toString(), Store.YES, NOT_ANALYZED));

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
	protected Document internal_document_file(IOFile file, String[] keywords, String description) throws IOException {
		String text = new String(DocumentFactory.getConvertor(file).getText(file));

		return this.internal_document(FileType.FILE, file.toURI().toURL().toString(), file.length(), keywords, description, new Date(file.lastModified()), text);
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
	protected Document internal_document_text(String title, String[] keywords, String description, String text) throws IOException {
		return this.internal_document(FileType.TEXT, "doc://localhost:0/" + title, text.length(), keywords, description, new Date(), text);
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
	protected Document internal_document_url(URL url, String[] keywords, String description) throws IOException {
		return this.internal_document(FileType.URL, url.toString(), url.openConnection().getContentLength(), keywords, description,
				new Date(url.openConnection().getLastModified()), new String(NewLuceneIndex.internal_readFromStream(url.openConnection().getInputStream())));
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
	protected Document internal_find_record(String id) throws IOException {
		return this.internal_find_record_result(id).getLuceneDocument();
	}

	/**
	 * na
	 * 
	 * @param id
	 * 
	 * @return
	 * 
	 * @throws IOException
	 * @throws RuntimeException
	 */
	protected SearchResult internal_find_record_result(String id) throws IOException {
		Searcher searcher = this.internal_getIndexSearcher();
		Term term = new Term(ID.url.f(), id);
		TermQuery query = new TermQuery(term);
		TopDocs topDocs = searcher.search(query, 2);

		if (topDocs.totalHits == 0) {
			return null;
		}

		if (topDocs.totalHits > 1) {
			throw new RuntimeException();
		}

		Document doc = searcher.doc(topDocs.scoreDocs[0].doc);

		return new SearchResult(0, (ResultList) null, doc, topDocs.scoreDocs[0].doc, 1.0f, this.myself);
	}

	/**
	 * na
	 * 
	 * @return
	 */
	protected Analyzer internal_getAnalyzer() {
		return this.analyzer;
	}

	/**
	 * na
	 * 
	 * @return
	 * 
	 * @throws CorruptIndexException
	 * @throws IOException
	 * @throws RuntimeException
	 */
	protected IndexReader internal_getIndexReader_readOnly() throws CorruptIndexException, IOException {
		if (this._IndexReaderRO == null) {
			this._IndexReaderRO = IndexReader.open(this.directory, true);
		} else {
			if (!this._IndexReaderRO.isCurrent()) {
				this._IndexReaderRO = this._IndexReaderRO.reopen();
			} else if (this._IndexReaderRO.hasDeletions()) {
				// TODO can this be fixed?
				throw new RuntimeException();
			}
		}

		return this._IndexReaderRO;
	}

	/**
	 * na
	 * 
	 * @return
	 * 
	 * @throws CorruptIndexException
	 * @throws IOException
	 * @throws RuntimeException
	 */
	protected IndexReader internal_getIndexReader_writable() throws CorruptIndexException, IOException {
		if (this._IndexReader == null) {
			this._IndexReader = IndexReader.open(this.directory, false);
		} else if (!this._IndexReader.isCurrent()) {
			// TODO can this be fixed?
			throw new RuntimeException();
		} else if (this._IndexReader.hasDeletions()) {
			// TODO can this be fixed?
			throw new RuntimeException();
		}

		return this._IndexReader;
	}

	/**
	 * na
	 * 
	 * @return
	 * 
	 * @throws CorruptIndexException
	 * @throws IOException
	 */
	protected IndexSearcher internal_getIndexSearcher() throws CorruptIndexException, IOException {
		if (this._IndexSearcher == null) {
			this._IndexSearcher = new IndexSearcher(this.internal_getIndexReader_readOnly());
		}

		return this._IndexSearcher;
	}

	/**
	 * na
	 * 
	 * @return
	 * 
	 * @throws CorruptIndexException
	 * @throws LockObtainFailedException
	 * @throws IOException
	 */
	protected IndexWriter internal_getIndexWriter() throws CorruptIndexException, LockObtainFailedException, IOException {
		if (this._IndexWriter == null) {
			this._IndexWriter = new IndexWriter(this.directory, this.internal_getAnalyzer().analyzer(), this.directory.listAll().length == 0, MaxFieldLength.LIMITED);
		}

		return this._IndexWriter;
	}

	/**
	 * na
	 * 
	 * @throws IOException
	 */
	public void optimize() throws IOException {
		IndexWriter writer = this.internal_getIndexWriter();
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
	public ResultList query(int max, String find) throws IOException, ParseException {
		Searcher searcher = this.internal_getIndexSearcher();
		QueryParser parser = new QueryParser(Version.LUCENE_36, "text", this.internal_getAnalyzer().analyzer());
		Query query = parser.parse(find);
		TopScoreDocCollector collector = TopScoreDocCollector.create(max, false);
		searcher.search(query, collector);

		ScoreDoc[] hits = collector.topDocs().scoreDocs;

		// int numTotalHits = collector.getTotalHits();
		ResultList list = new ResultList(this.myself, find, find, query, collector.getTotalHits());

		for (int j = 0; j < hits.length; j++) {
			Document doc = searcher.doc(hits[j].doc);
			list.addSearchResult(new SearchResult(j, list, doc, hits[j].doc, hits[j].score, this.myself));
		}

		return list;
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
		return this.internal_find_record(file.toURI().toURL().toString());
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
		return this.internal_find_record("doc://localhost:0/" + title);
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
		return this.internal_find_record(url.toString());
	}
}
