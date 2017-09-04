package org.jhaws.common.lucene;

import org.junit.Assert;
import org.junit.Test;

public class LuceneQueryTest {
    @Test
    public void test3() {
        LuceneIndex i = new LuceneIndex();
        Assert.assertEquals(
                "defaultfield:\"some sentence query\"~2^15.0 ((defaultfield:some defaultfield:sentence defaultfield:query)~2^6.0) ((defaultfield:some defaultfield:sentence defaultfield:query)~3^9.0)",
                i.buildQuery("some sentence to query", "defaultfield").toString());
    }
}
