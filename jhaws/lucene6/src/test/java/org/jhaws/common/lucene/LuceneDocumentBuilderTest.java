package org.jhaws.common.lucene;

import java.time.LocalDateTime;

import org.apache.lucene.document.Document;
import org.junit.Assert;
import org.junit.Test;

public class LuceneDocumentBuilderTest {
    public static class It extends BuildableIndexable<It> {
        @IndexField
        private String field1;

        @IndexField
        private String field2;

        public String getField1() {
            return field1;
        }

        public void setField1(String field1) {
            this.field1 = field1;
        }

        public String getField2() {
            return field2;
        }

        public void setField2(String field2) {
            this.field2 = field2;
        }
    }

    @Test
    public void test() {
        try {
            It it = new It();
            it.setLastmodified(LocalDateTime.now());
            it.setUuid("uuid");
            it.setVersion(1);
            it.setField1("field1");
            it.setField2("field2");
            Document d = it.indexable();
            It it2 = new It();
            it2 = it2.retrieve(d);
            Assert.assertFalse(it == it2);
            Assert.assertEquals(it.getUuid(), it2.getUuid());
            Assert.assertEquals(it.getVersion(), it2.getVersion());
            Assert.assertEquals(it.getLastmodified(), it2.getLastmodified());
            Assert.assertEquals(it.getField1(), it2.getField1());
            Assert.assertEquals(it.getField2(), it2.getField2());
        } catch (RuntimeException ex) {
            ex.printStackTrace(System.out);
            throw ex;
        }
    }
}
