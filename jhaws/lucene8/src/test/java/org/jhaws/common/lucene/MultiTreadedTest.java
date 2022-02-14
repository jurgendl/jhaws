package org.jhaws.common.lucene;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CountDownLatch;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.StringField;
import org.jhaws.common.io.FilePath;
import org.junit.Assert;
import org.junit.Test;

public class MultiTreadedTest {
    @Test
    public void test() {
        CountDownLatch latch = new CountDownLatch(10);
        FilePath fp = FilePath.createTempDirectory();
        System.out.println(fp);
        @SuppressWarnings("resource")
        LuceneIndex li = new LuceneIndex(fp);
        {
            Document doc = new Document();
            doc.add(new StringField("idx", "", org.apache.lucene.document.Field.Store.YES));
            li.addDocs(doc);
        }
        Random r = new Random();
        for (int i = 0; i < 10; i++) {
            Thread t = new Thread(() -> {
                for (int j = 0; j < 100; j++) {
                    List<Document> docs = new ArrayList<>();
                    for (int k = 0; k < 10000; k++) {
                        Document doc = new Document();
                        doc.add(new StringField("idx", "" + ((char) ('a' + r.nextInt(26))) + ((char) ('a' + r.nextInt(26))), org.apache.lucene.document.Field.Store.YES));
                        docs.add(doc);
                    }
                    try {
                        li.addDocs(docs);
                    } catch (Exception ex) {
                        ex.printStackTrace(System.out);
                        Assert.fail("" + ex);
                    }
                }
                latch.countDown();
            }, "MultiTreadedTest-" + i);
            t.setDaemon(false);
            t.start();
            System.out.println(i);
        }
        for (int i = 0; i < 5; i++) {
            Thread t = new Thread(() -> {
                while (true) {
                    try {
                        li.search(li.keyValueQuery("idx", "" + ((char) ('a' + r.nextInt(26))) + ((char) ('a' + r.nextInt(26)))).build(), 10000);
                    } catch (Exception ex) {
                        ex.printStackTrace(System.out);
                        Assert.fail("" + ex);
                    }
                    try {
                        Thread.sleep(3000l);
                    } catch (InterruptedException ex) {
                        //
                    }
                }
            }, "MultiTreadedTest-" + i);
            t.setDaemon(true);
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
