package org.jhaws.common.lucene;

import java.nio.file.Path;
import java.util.UUID;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.BinaryDocValuesField;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.NumericDocValuesField;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.MMapDirectory;
import org.apache.lucene.util.BytesRef;
import org.jhaws.common.io.FilePath;
import org.junit.Assert;
import org.junit.Test;

public class UpdateFieldTest {
    private static final String BIN = "bin";

    private static final String NUM = "num";

    Analyzer analyzer = new StandardAnalyzer();

    String id = UUID.randomUUID().toString();

    @Test
    public void updateFieldTest() {
        try {
            Path path = FilePath.getTempDirectory().child("L7-" + System.currentTimeMillis()).toPath();
            System.out.println(path);
            Directory dir = new MMapDirectory(path);
            {
                IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
                iwc.setOpenMode(OpenMode.CREATE_OR_APPEND);
                IndexWriter writer = new IndexWriter(dir, iwc);
                Document doc = new Document();
                doc.add(new StringField("ID", id, Field.Store.YES));
                doc.add(new NumericDocValuesField(NUM, 1l));
                doc.add(new BinaryDocValuesField(BIN, new BytesRef("1".getBytes())));
                doc.add(new StoredField(NUM, -1l));
                doc.add(new StoredField(BIN, new BytesRef("-1".getBytes())));
                writer.addDocument(doc);
                writer.close();
            }
            {
                IndexReader reader = DirectoryReader.open(dir);
                IndexSearcher searcher = new IndexSearcher(reader);
                Query query = new TermQuery(new Term("ID", id));
                TopDocs results = searcher.search(query, 100);
                ScoreDoc[] hits = results.scoreDocs;
                Document document = reader.document(hits[0].doc);
                System.out.println(document);
                System.out.println(document.getField(NUM).numericValue().longValue());
                System.out.println(new String(document.getField(BIN).binaryValue().bytes));
                reader.close();
            }
            {
                IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
                iwc.setOpenMode(OpenMode.CREATE_OR_APPEND);
                IndexWriter writer = new IndexWriter(dir, iwc);
                writer.updateNumericDocValue(new Term("ID", id), NUM, 2l);
                writer.updateBinaryDocValue(new Term("ID", id), BIN, new BytesRef("2".getBytes()));
                writer.close();
            }
            {
                IndexReader reader = DirectoryReader.open(dir);
                IndexSearcher searcher = new IndexSearcher(reader);
                Query query = new TermQuery(new Term("ID", id));
                TopDocs results = searcher.search(query, 100);
                ScoreDoc[] hits = results.scoreDocs;
                Document document = reader.document(hits[0].doc);
                System.out.println(document);
                System.out.println(document.getField(NUM).numericValue().longValue());
                System.out.println(new String(document.getField(BIN).binaryValue().bytes));
                reader.close();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            Assert.fail("" + ex);
        }
    }
}
