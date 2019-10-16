package org.jhaws.common.lucene;

import java.nio.charset.Charset;
import java.util.List;

import org.apache.lucene.document.Document;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.ScoreDoc;
import org.jhaws.common.io.FilePath;
import org.jhaws.common.lucene.LuceneDocumentBuilderTest.It;
import org.junit.Test;

public class PositionTest {
    public static class Text extends BuildableIndexable<It> {
        @IndexField(big = true, store = true)
        private String text;

        public String getText() {
            return this.text;
        }

        public void setText(String text) {
            this.text = text;
        }
    }

    @Test
    public void testPosition() {
        String t = new FilePath(getClass(), "text.txt").readAll(Charset.forName("utf-8"));
        Text text = new Text();
        text.setText(t);
        LuceneIndex li = new LuceneIndex();
        li.addIndexable(text);
        List<Document> all = li.searchAllDocs();
        BooleanQuery q = li.keyValueQuery("text", "*unde*").build();
        List<ScoreDoc> r = li.search(q, 10);
        Document doc = li.getDoc(r.get(0));
        li.close();
    }
}
