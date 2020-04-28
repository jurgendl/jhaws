package org.jhaws.common.lucene;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.StringField;
import org.jhaws.common.io.FilePath;

public class EmptyLuceneIndex {
    public static void main(String[] args) {
        FilePath lp = FilePath.getTempDirectory().child("luceneempty8");
        lp.delete();
        System.out.println(lp);
        Document doc = new Document();
        doc.add(new StringField("name", "value", Store.YES));
        try (LuceneIndex li = new LuceneIndex(lp)) {
            li.addDocs(doc);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
