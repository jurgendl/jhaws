package org.jhaws.common.lucene;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.junit.Assert;
import org.junit.Test;

public class LuceneTokenizerTest {
    @Test
    public void test() {
        List<String> l = Arrays.asList("4.5", "some", "stuff", "need", "analysis", "we", "love", "words", "class");
        List<String> t = new ArrayList<>();
        try (Analyzer analyzer = new LuceneIndexAnalyzer();
                TokenStream tokenStream = analyzer.tokenStream("fieldName",
                        "4.5 Some stuff that is in need of analysis, we love words and classes.")) {
            tokenStream.reset();
            CharTermAttribute token = tokenStream.getAttribute(CharTermAttribute.class);
            while (tokenStream.incrementToken()) {
                t.add(token.toString());
            }
            tokenStream.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        Assert.assertEquals(l, t);
    }
}
