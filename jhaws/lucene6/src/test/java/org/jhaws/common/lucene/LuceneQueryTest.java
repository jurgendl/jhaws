package org.jhaws.common.lucene;

import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.MatchAllDocsQuery;
import org.apache.lucene.search.TermQuery;
import org.junit.Assert;
import org.junit.Test;

public class LuceneQueryTest {
    @Test
    public void test1() {
        BooleanQuery.Builder qb = new BooleanQuery.Builder();
        Assert.assertEquals("+key:value", qb.add(new TermQuery(new Term("key", "value")), Occur.MUST).build().toString());
    }

    @Test
    public void test2() {
        BooleanQuery.Builder qb = new BooleanQuery.Builder();
        qb.add(new MatchAllDocsQuery(), Occur.MUST);
        qb.add(new TermQuery(new Term(LuceneIndex.LUCENE_METADATA, LuceneIndex.LUCENE_METADATA)), Occur.MUST_NOT);
        // "+*:* -(+LUCENE_METADATA:LUCENE_METADATA)"
        Assert.assertEquals("+*:* -LUCENE_METADATA:LUCENE_METADATA", qb.build().toString());
    }

    // @Test
    // public void test3() {
    // LuceneIndex i = new LuceneIndex();
    // String string = i.buildQuery("some sentence to query", "defaultfield").toString();
    // System.out.println(string);
    // Assert.assertEquals(
    // "defaultfield:\"some sentence query\"~2^15.0 ((defaultfield:some defaultfield:sentence defaultfield:query)~2^6.0) ((defaultfield:some
    // defaultfield:sentence defaultfield:query)~3^9.0)",
    // string);
    // // defaultfield:"some sentence to query"~2^20.0 ((defaultfield:some defaultfield:sentence defaultfield:to defaultfield:query)~2^6.0)
    // // ((defaultfield:some defaultfield:sentence defaultfield:to defaultfield:query)~3^9.0) ((defaultfield:some defaultfield:sentence
    // // defaultfield:to defaultfield:query)~4^12.0)
    // //
    // // defaultfield:"some sentence query"~2^15.0 ((defaultfield:some defaultfield:sentence defaultfield:query)~2^6.0) ((defaultfield:some
    // // defaultfield:sentence defaultfield:query)~3^9.0)
    // }
}
