package org.jhaws.common.lucene;

import java.nio.charset.Charset;
import java.util.List;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.QueryParserBase;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.jhaws.common.io.FilePath;
import org.jhaws.common.lucene.LuceneDocumentBuilderTest.It;
import org.jhaws.common.lucene.LuceneIndex.HighlightResult;
import org.junit.Assert;
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
        try {
            String t = new FilePath(getClass(), "text.txt").readAll(Charset.forName("utf-8"));
            Text text = new Text();
            text.setText(t);
            LuceneIndex li = new LuceneIndex(new FilePath("c:/tmp/--index--" + System.currentTimeMillis())) {
                @Override
                protected IndexWriterConfig createIndexWriterConfig() {
                    IndexWriterConfig cfg = super.createIndexWriterConfig();
                    cfg.setCodec(new org.apache.lucene.codecs.simpletext.SimpleTextCodec());
                    return cfg;
                }
            };
            li.addIndexable(text);
            List<Document> all = li.searchAllDocs();
            Query q = MultiFieldQueryParser.parse(new String[] { QueryParserBase.escape("*") + "ab" + QueryParserBase.escape("*") },
                    new String[] { "text" }, li.getSearchAnalyzer());
            List<ScoreDoc> r = li.search(q, 10);
            List<HighlightResult> hi = li.highlight(q, li.score(q, 10), "text");
            Document doc = li.getDoc(r.get(0));
            System.out.println(doc);
            li.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            Assert.fail("");
        }
    }
}
