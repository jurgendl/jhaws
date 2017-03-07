package org.jhaws.common.lucene;

import org.jhaws.common.documents.XmlTextExtracter;
import org.jhaws.common.io.FilePath;
import org.junit.Assert;
import org.junit.Test;

public class LuceneDocExtractTest {
    @Test
    public void testXml() {
        try {
            XmlTextExtracter.SPLIT = " ";
            XmlTextExtracter x = new XmlTextExtracter();
            Assert.assertEquals("UTF-8 ${pattern} false ", x.extract(FilePath.of(XmlTextExtracter.class, "logback.xml")));
        } catch (Exception ex) {
            Assert.fail("" + ex);
        }
    }
}
