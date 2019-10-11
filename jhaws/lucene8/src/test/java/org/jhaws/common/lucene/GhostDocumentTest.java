package org.jhaws.common.lucene;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.util.UUID;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.MMapDirectory;
import org.jhaws.common.io.FilePath;
import org.junit.Assert;
import org.junit.Test;

public class GhostDocumentTest {
    Analyzer analyzer = new StandardAnalyzer();

    String id = UUID.randomUUID().toString();

    String id2 = UUID.randomUUID().toString();

    String id3 = UUID.randomUUID().toString();

    @Test
    public void ghostDocumentTest() {
        try {
            String text = new FilePath(getClass(), "text.txt").readAll(Charset.forName("utf-8"));
            Path path = FilePath.getTempDirectory().child("L7-" + System.currentTimeMillis()).toPath();
            System.out.println(path);
            Directory dir = new MMapDirectory(path);
            create(text, dir);
            IndexReader reader = DirectoryReader.open(dir);
            IndexSearcher searcher = new IndexSearcher(reader);
            read(reader, searcher);
            delete(dir);
            read(reader, searcher);
            reader.close();
            reader = DirectoryReader.open(dir);
            searcher = new IndexSearcher(reader);
            read(reader, searcher);
            reader.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            Assert.fail("" + ex);
        }
    }

    protected void create(String text, Directory dir) throws IOException {
        System.out.println("====================" + "CREATE" + "====================");
        IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
        iwc.setOpenMode(OpenMode.CREATE_OR_APPEND);
        IndexWriter writer = new IndexWriter(dir, iwc);
        {
            String newid = id;
            Document doc = new Document();
            doc.add(new StringField("ID", newid, Field.Store.YES));
            doc.add(new TextField("TEXT", text, Field.Store.NO));
            doc.add(new StoredField("date", System.currentTimeMillis()));
            writer.addDocument(doc);
            // writer.updateDocument(new Term("ID", newid), doc);
        }
        {
            Document doc = new Document();
            doc.add(new StringField("ID", id2, Field.Store.YES));
            doc.add(new TextField("TEXT", text, Field.Store.NO));
            doc.add(new StoredField("date", System.currentTimeMillis()));
            writer.addDocument(doc);
            // writer.updateDocument(new Term("ID", id2), doc);
        }
        {
            Document doc = new Document();
            doc.add(new StringField("ID", id3, Field.Store.YES));
            doc.add(new StoredField("date", System.currentTimeMillis()));
            writer.addDocument(doc);
            // writer.updateDocument(new Term("ID", id3), doc);
        }
        // writer.flush();
        // writer.commit();
        writer.close();
    }

    protected void delete(Directory dir) throws IOException {
        System.out.println("====================" + "DELETE" + "====================");
        IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
        iwc.setOpenMode(OpenMode.CREATE_OR_APPEND);
        IndexWriter writer = new IndexWriter(dir, iwc);
        writer.deleteDocuments(new Term("ID", id));
        // writer.flush();
        // writer.commit();
        writer.close();
    }

    protected void read(IndexReader reader, IndexSearcher searcher) throws IOException, ParseException {
        System.out.println("====================" + "READ" + "====================");
        System.out.println("#" + reader.numDocs() + " / -" + reader.numDeletedDocs() + "\n");
        {
            Query query = new TermQuery(new Term("ID", id));
            System.out.println(query);
            {
                TopDocs results = searcher.search(query, 100);
                ScoreDoc[] hits = results.scoreDocs;
                loop(reader, hits);
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
                loop(reader, hits);
            }
            System.out.println();
        }
    }

    protected void loop(IndexReader reader, ScoreDoc[] hits) throws IOException {
        for (int docID = 0; docID < hits.length; docID++) {
            System.out.println();
            System.out.println(hits[docID].doc);
            System.out.println(hits[docID].score);
            // System.out.println(hits[docID].shardIndex);
            Document doc = reader.document(hits[docID].doc);
            System.out.println(doc);
            System.out.println(doc.get("ID"));
            System.out.println(doc.get("date"));
            // System.out.println(doc.get("TEXT"));
        }
    }
}
