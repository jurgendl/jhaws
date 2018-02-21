package org.jhaws.common.lucene;

import java.util.concurrent.CountDownLatch;

import org.apache.lucene.document.Document;
import org.jhaws.common.io.FilePath;
import org.jhaws.common.lucene.LuceneIndex;
import org.junit.Test;

public class MultiTreadedTest {
    @Test
    public void test() {
        CountDownLatch latch = new CountDownLatch(10);
        FilePath fp = FilePath.createTempDirectory();
        System.out.println(fp);
        @SuppressWarnings("resource")
        LuceneIndex li = new LuceneIndex(fp);
        for (int i = 0; i < 10; i++) {
            final int ii = i;
            Thread t = new Thread(() -> {
                for (int j = 0; j < 100; j++) {
                    Document doc = new Document();
                    li.addDocs(doc);
                    if (j % 10 == 0) System.out.println(ii + "/" + j);
                }
                latch.countDown();
            }, "MultiTreadedTest-" + i);
            t.setDaemon(false);
            t.start();
            System.out.println(i);
        }
        try {
            latch.await();
        } catch (InterruptedException ex) {
            //
        }
        li.shutDown();
    }
}
