package org.jhaws.common.lucene;

import java.nio.charset.Charset;
import java.util.UUID;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.LongField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.RAMDirectory;
import org.jhaws.common.io.FilePath;
import org.junit.Test;

public class GhostDocumentTest {
    @Test
    public void ghostDocumentTest() {
        try {
            String text = new FilePath(getClass(), "text.txt").readAll(Charset.forName("utf-8"));
            RAMDirectory dir = new RAMDirectory();
            Analyzer analyzer = new StandardAnalyzer();
            String id = UUID.randomUUID().toString();
            {
                IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
                iwc.setOpenMode(OpenMode.CREATE_OR_APPEND);
                IndexWriter writer = new IndexWriter(dir, iwc);
                {
                    String newid = id;
                    Document doc = new Document();
                    doc.add(new StringField("ID", newid, Field.Store.YES));
                    doc.add(new TextField("TEXT", text, Field.Store.NO));
                    doc.add(new LongField("date", System.currentTimeMillis(), Field.Store.YES));
                    // writer.addDocument(doc);
                    writer.updateDocument(new Term("ID", newid), doc);
                }
                {
                    String newid = UUID.randomUUID().toString();
                    Document doc = new Document();
                    doc.add(new StringField("ID", newid, Field.Store.YES));
                    doc.add(new TextField("TEXT", text, Field.Store.NO));
                    doc.add(new LongField("date", System.currentTimeMillis(), Field.Store.YES));
                    writer.updateDocument(new Term("ID", newid), doc);
                }
                {
                    String newid = UUID.randomUUID().toString();
                    Document doc = new Document();
                    doc.add(new StringField("ID", newid, Field.Store.YES));
                    doc.add(new LongField("date", System.currentTimeMillis(), Field.Store.YES));
                    writer.updateDocument(new Term("ID", newid), doc);
                }
                writer.close();
            }
            {
                IndexReader reader = DirectoryReader.open(dir);
                IndexSearcher searcher = new IndexSearcher(reader);
                {
                    Query query = new TermQuery(new Term("ID", id));
                    System.out.println(query);
                    {
                        TopDocs results = searcher.search(query, 100);
                        ScoreDoc[] hits = results.scoreDocs;
                        for (int docID = 0; docID < hits.length; docID++) {
                            System.out.println(hits[docID].doc);
                            System.out.println(hits[docID].score);
                            System.out.println(hits[docID].shardIndex);
                            Document doc = reader.document(hits[docID].doc);
                            System.out.println(doc);
                            System.out.println(doc.get("ID"));
                            System.out.println(doc.get("TEXT"));
                        }
                    }
                    System.out.println();
                }
                {
                    QueryParser parser = new QueryParser("TEXT", analyzer);
                    Query query = parser.parse("ipsum");
                    System.out.println(query);
                    {
                        TopDocs results = searcher.search(query, 100);
                        ScoreDoc[] hits = results.scoreDocs;
                        for (int docID = 0; docID < hits.length; docID++) {
                            System.out.println(hits[docID].doc);
                            System.out.println(hits[docID].score);
                            System.out.println(hits[docID].shardIndex);
                            Document doc = reader.document(hits[docID].doc);
                            System.out.println(doc);
                            System.out.println(doc.get("ID"));
                            System.out.println(doc.get("TEXT"));
                        }
                    }
                    System.out.println();
                }
                reader.close();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
