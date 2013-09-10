package org.jhaws.common.lucene4;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Date;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.LongField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.jhaws.common.docimport.DocumentFactory;
import org.jhaws.common.io.IODirectory;
import org.jhaws.common.io.IOnOSUtils;

public class LuceneTest {
    public static void main(String[] args) {
        try {
            Version LUCENE_VERSION = Version.LUCENE_44;
            Directory dir = FSDirectory.open(IODirectory.newTempDir("lucenetests"));
            Analyzer analyzer = new StandardAnalyzer(LUCENE_VERSION);
            IndexWriterConfig iwc = new IndexWriterConfig(LUCENE_VERSION, analyzer);
            iwc.setOpenMode(OpenMode.CREATE_OR_APPEND);
            IndexWriter writer = new IndexWriter(dir, iwc);
            String testfiles = LuceneTest.class.getPackage().getName().replace('.', '/') + "/testfiles";
            ByteArrayOutputStream tmp = new ByteArrayOutputStream();
            IOnOSUtils.copyResource(testfiles, tmp);
            BufferedReader br = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(tmp.toByteArray())));
            String line;
            while ((line = br.readLine()) != null) {
                String testfile = LuceneTest.class.getPackage().getName().replace('.', '/') + "/" + line;
                indexDocs(writer, testfile);
            }
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void indexDocs(IndexWriter writer, String testfile) throws IOException {
        InputStream fis = LuceneTest.class.getClassLoader().getResourceAsStream(testfile);
        String content = DocumentFactory.getConvertor(testfile).getText(fis);
        try {

            // make a new, empty document
            Document doc = new Document();

            // Add the path of the file as a field named "path". Use a
            // field that is indexed (i.e. searchable), but don't tokenize
            // the field into separate words and don't index term frequency
            // or positional information:
            Field pathField = new StringField("path", testfile, Field.Store.YES);
            doc.add(pathField);

            // Add the last modified date of the file a field named "modified".
            // Use a LongField that is indexed (i.e. efficiently filterable with
            // NumericRangeFilter). This indexes to milli-second resolution, which
            // is often too fine. You could instead create a number based on
            // year/month/day/hour/minutes/seconds, down the resolution you require.
            // For example the long value 2011021714 would mean
            // February 17, 2011, 2-3 PM.
            doc.add(new LongField("modified", new Date().getTime(), Field.Store.NO));

            // Add the contents of the file to a field named "contents". Specify a Reader,
            // so that the text of the file is tokenized and indexed, but not stored.
            // Note that FileReader expects the file to be in UTF-8 encoding.
            // If that's not the case searching for special characters will fail.
            doc.add(new TextField("contents", content, Store.YES));

            if (writer.getConfig().getOpenMode() == OpenMode.CREATE) {
                // New index, so we just add the document (no old document can be there):
                System.out.println("adding " + testfile);
                writer.addDocument(doc);
            } else {
                // Existing index (an old copy of this document may have been indexed) so
                // we use updateDocument instead to replace the old one matching the exact
                // path, if present:
                System.out.println("updating " + testfile);
                writer.updateDocument(new Term("path", testfile), doc);
            }
        } finally {
            fis.close();
        }
    }
}
