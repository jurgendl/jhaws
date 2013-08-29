package org.jhaws.common.lucene.doctype;

import junit.framework.Assert;

import org.jhaws.common.io.IODirectory;
import org.jhaws.common.io.IOFile;
import org.jhaws.common.lucene.LuceneHelper;
import org.jhaws.common.lucene.LuceneIndexImpl;
import org.jhaws.common.lucene.ResultList;
import org.junit.Test;


/**
 * NewLuceneIndexTest
 */
public class NewLuceneIndexTest {
    /** n */
    private static final String FILE = "hibernate_in_action.pdf";

    /** n */
    private static final String DIR = "C:/java/test/pdf/";

    /**
     * test
     */
    @Test
    public void test01() {
        try {
            IODirectory ioDirectory = IODirectory.newTempDir("index1");
            Assert.assertTrue(ioDirectory.erase());
            Assert.assertFalse(ioDirectory.exists());
            Assert.assertTrue(ioDirectory.create().exists());

            LuceneHelper luceneHelper = ((LuceneIndexImpl) LuceneIndexImpl.get(ioDirectory)).getHelper();
            IOFile pdf = new IOFile(NewLuceneIndexTest.DIR + NewLuceneIndexTest.FILE);

            Assert.assertNotNull(luceneHelper.addFile(pdf));
            Assert.assertNotNull(luceneHelper.addTitle("title", "text"));
            Assert.assertEquals(2, luceneHelper.all().getSize());
            Assert.assertNotNull(luceneHelper.searchFile(pdf));
            Assert.assertEquals(1, luceneHelper.query(100, "CHAPTER").getSize());
            Assert.assertEquals(1, luceneHelper.deleteFile(pdf));
            Assert.assertEquals(1, luceneHelper.count());
            Assert.assertTrue(luceneHelper.close());
        } catch (Exception ex) {
            ex.printStackTrace(System.out);
            Assert.fail("" + ex);
        }
    }

    /**
     * test
     */
    @Test
    public void test02() {
        try {
            IODirectory ioDirectory = IODirectory.newTempDir("index2");
            Assert.assertTrue(ioDirectory.erase());
            Assert.assertFalse(ioDirectory.exists());
            Assert.assertTrue(ioDirectory.create().exists());

            LuceneHelper luceneHelper = ((LuceneIndexImpl) LuceneIndexImpl.get(ioDirectory)).getHelper();
            IOFile pdf = new IOFile(NewLuceneIndexTest.DIR + NewLuceneIndexTest.FILE);
            IOFile pdf2 = new IOFile(NewLuceneIndexTest.FILE + "2.pdf");
            IOFile pdf3 = new IOFile(NewLuceneIndexTest.FILE + "3.pdf");

            if (!pdf2.exists()) {
                pdf.copy2(pdf2);
            }

            if (!pdf3.exists()) {
                pdf.copy2(pdf3);
            }

            Assert.assertNotNull(luceneHelper.addFile(pdf));
            Assert.assertNotNull(luceneHelper.addFile(pdf2));
            Assert.assertNotNull(luceneHelper.addFile(pdf3));
            Assert.assertEquals(3, luceneHelper.count());

            {
                ResultList query = luceneHelper.query(1, "CHAPTER");
                Assert.assertEquals(1, query.getSize());
                Assert.assertEquals(3, query.getTotalHits());
            }

            {
                ResultList query = luceneHelper.query(2, "CHAPTER");
                Assert.assertEquals(2, query.getSize());
                Assert.assertEquals(3, query.getTotalHits());
            }

            {
                ResultList query = luceneHelper.query(3, "CHAPTER");
                Assert.assertEquals(3, query.getSize());
                Assert.assertEquals(3, query.getTotalHits());
            }

            {
                ResultList query = luceneHelper.query(4, "CHAPTER");
                Assert.assertEquals(3, query.getSize());
                Assert.assertEquals(3, query.getTotalHits());
            }

            Assert.assertTrue(luceneHelper.close());
        } catch (Exception ex) {
            ex.printStackTrace(System.out);
            Assert.fail("" + ex);
        }
    }

    /**
     * test
     */
    @Test
    public void test03() {
        try {
            IODirectory ioDirectory = IODirectory.newTempDir("index2");
            Assert.assertTrue(ioDirectory.erase());
            Assert.assertFalse(ioDirectory.exists());
            Assert.assertTrue(ioDirectory.create().exists());

            LuceneHelper luceneHelper = ((LuceneIndexImpl) LuceneIndexImpl.get(ioDirectory)).getHelper();
            IOFile pdf = new IOFile(NewLuceneIndexTest.DIR + NewLuceneIndexTest.FILE);
            IOFile pdf2 = new IOFile(NewLuceneIndexTest.FILE + "2.pdf");
            IOFile pdf3 = new IOFile(NewLuceneIndexTest.FILE + "3.pdf");

            if (!pdf2.exists()) {
                pdf.copy2(pdf2);
            }

            if (pdf3.exists()) {
                pdf3.erase();
            }

            Assert.assertTrue(pdf2.exists());
            Assert.assertFalse(pdf3.exists());

            luceneHelper.addFile(pdf2);
            Assert.assertEquals(1, luceneHelper.all().getSize());

            Assert.assertNotNull(luceneHelper.searchFile(pdf2));
            Assert.assertNull(luceneHelper.searchFile(pdf3));

            pdf2.move2(pdf3);

            Assert.assertFalse(pdf2.exists());
            Assert.assertTrue(pdf3.exists());

            luceneHelper.deleteFile(pdf2);

            Assert.assertEquals(0, luceneHelper.all().getSize());
            Assert.assertNull(luceneHelper.searchFile(pdf2));

            Assert.assertTrue(luceneHelper.close());
        } catch (Exception ex) {
            ex.printStackTrace(System.out);
            Assert.fail("" + ex);
        }
    }

    /**
     * test
     */
    @Test
    public void test04() {
        try {
            IODirectory ioDirectory = IODirectory.newTempDir("index4");
            Assert.assertTrue(ioDirectory.erase());
            Assert.assertFalse(ioDirectory.exists());
            Assert.assertTrue(ioDirectory.create().exists());

            LuceneHelper luceneHelper = ((LuceneIndexImpl) LuceneIndexImpl.get(ioDirectory)).getHelper();
            IOFile pdf = new IOFile(NewLuceneIndexTest.DIR + NewLuceneIndexTest.FILE);
            IOFile pdf2 = new IOFile(NewLuceneIndexTest.FILE + "2.pdf");
            IOFile pdf3 = new IOFile(NewLuceneIndexTest.FILE + "3.pdf");

            if (!pdf2.exists()) {
                pdf.copy2(pdf2);
            }

            if (pdf3.exists()) {
                pdf3.erase();
            }

            Assert.assertTrue(pdf2.exists());
            Assert.assertFalse(pdf3.exists());

            luceneHelper.addFile(pdf2);
            Assert.assertEquals(1, luceneHelper.all().getSize());

            Assert.assertNotNull(luceneHelper.searchFile(pdf2));
            Assert.assertNull(luceneHelper.searchFile(pdf3));

            pdf2.move2(pdf3);

            Assert.assertFalse(pdf2.exists());
            Assert.assertTrue(pdf3.exists());

            luceneHelper.moveFile(pdf2, pdf3);

            Assert.assertEquals(1, luceneHelper.all().getSize());

            Assert.assertNull(luceneHelper.searchFile(pdf2));
            Assert.assertNotNull(luceneHelper.searchFile(pdf3));

            Assert.assertTrue(luceneHelper.close());
        } catch (Exception ex) {
            ex.printStackTrace(System.out);
            Assert.fail("" + ex);
        }
    }

    /**
     * test
     */
    @Test
    public void test05() {
        try {
            IODirectory ioDirectory = IODirectory.newTempDir("index5");
            Assert.assertTrue(ioDirectory.erase());
            Assert.assertFalse(ioDirectory.exists());
            Assert.assertTrue(ioDirectory.create().exists());

            LuceneHelper luceneHelper = ((LuceneIndexImpl) LuceneIndexImpl.get(ioDirectory)).getHelper();
            IOFile pdf = new IOFile(NewLuceneIndexTest.DIR + NewLuceneIndexTest.FILE);

            luceneHelper.addFile(pdf);

            Assert.assertEquals(1, luceneHelper.listDocuments().size());
            Assert.assertEquals(1, luceneHelper.count());
        } catch (Exception ex) {
            ex.printStackTrace(System.out);
            Assert.fail("" + ex);
        }
    }

    /**
     * test
     */
    @Test
    public void test06() {
        try {
            IODirectory ioDirectory = IODirectory.newTempDir("index6");
            Assert.assertTrue(ioDirectory.erase());
            Assert.assertFalse(ioDirectory.exists());
            Assert.assertTrue(ioDirectory.create().exists());

            LuceneHelper luceneHelper = ((LuceneIndexImpl) LuceneIndexImpl.get(ioDirectory)).getHelper();
            IOFile pdf = new IOFile(NewLuceneIndexTest.DIR + NewLuceneIndexTest.FILE);
            IOFile pdf2 = new IOFile(NewLuceneIndexTest.FILE + "2.pdf");

            pdf.copy2(pdf2);

            luceneHelper.addFile(pdf);
            luceneHelper.addFile(pdf2);

            pdf2.erase();

            luceneHelper.cleanUpNotExistingFileDocuments();

            Assert.assertEquals(1, luceneHelper.listDocuments().size());
            Assert.assertEquals(1, luceneHelper.count());
        } catch (Exception ex) {
            ex.printStackTrace(System.out);
            Assert.fail("" + ex);
        }
    }

    /**
     * test
     */
    @Test
    public void test07() {
        try {
            IODirectory ioDirectory = IODirectory.newTempDir("index7");
            Assert.assertTrue(ioDirectory.erase());
            Assert.assertFalse(ioDirectory.exists());
            Assert.assertTrue(ioDirectory.create().exists());

            LuceneHelper luceneHelper = ((LuceneIndexImpl) LuceneIndexImpl.get(ioDirectory)).getHelper();
            IOFile pdf = new IOFile(NewLuceneIndexTest.DIR + NewLuceneIndexTest.FILE);

            Assert.assertNull(luceneHelper.getFieldNames());
            Assert.assertNull(luceneHelper.getLockId());
            Assert.assertNotNull(luceneHelper.getLuceneVersion());
            Assert.assertNull(luceneHelper.getVersion());
            Assert.assertNotNull(luceneHelper.getModificationDate());
            Assert.assertNull(luceneHelper.getVersionDate());

            luceneHelper.addFile(pdf);

            Assert.assertNotNull(luceneHelper.getFieldNames());
            Assert.assertNotNull(luceneHelper.getLockId());
            Assert.assertNotNull(luceneHelper.getLuceneVersion());
            Assert.assertNotNull(luceneHelper.getVersion());
            Assert.assertNotNull(luceneHelper.getModificationDate());
            Assert.assertNotNull(luceneHelper.getVersionDate());
        } catch (Exception ex) {
            ex.printStackTrace(System.out);
            Assert.fail("" + ex);
        }
    }

    /**
     * test
     */
    @Test
    public void test08() {
        try {
            IODirectory ioDirectory = IODirectory.newTempDir("index8");
            Assert.assertTrue(ioDirectory.erase());
            Assert.assertFalse(ioDirectory.exists());
            Assert.assertTrue(ioDirectory.create().exists());

            LuceneHelper luceneHelper = ((LuceneIndexImpl) LuceneIndexImpl.get(ioDirectory)).getHelper();

            Assert.assertFalse(luceneHelper.delete());
        } catch (Exception ex) {
            ex.printStackTrace(System.out);
            Assert.fail("" + ex);
        }
    }

    /**
     * test
     */
    @Test
    public void test09() {
        try {
            IODirectory ioDirectory = IODirectory.newTempDir("index9");
            Assert.assertTrue(ioDirectory.erase());
            Assert.assertFalse(ioDirectory.exists());
            Assert.assertTrue(ioDirectory.create().exists());

            LuceneHelper luceneHelper = ((LuceneIndexImpl) LuceneIndexImpl.get(ioDirectory)).getHelper();
            IOFile pdf = new IOFile(NewLuceneIndexTest.DIR + NewLuceneIndexTest.FILE);

            luceneHelper.addFile(pdf);

            Assert.assertFalse(luceneHelper.delete());
        } catch (Exception ex) {
            ex.printStackTrace(System.out);
            Assert.fail("" + ex);
        }
    }

    /**
     * test
     */
    @Test
    public void test10() {
        try {
            IODirectory ioDirectory = IODirectory.newTempDir("index10");
            Assert.assertTrue(ioDirectory.erase());
            Assert.assertFalse(ioDirectory.exists());
            Assert.assertTrue(ioDirectory.create().exists());

            LuceneHelper luceneHelper = ((LuceneIndexImpl) LuceneIndexImpl.get(ioDirectory)).getHelper();
            IOFile pdf = new IOFile(NewLuceneIndexTest.DIR + NewLuceneIndexTest.FILE);
            IOFile pdf2 = new IOFile(NewLuceneIndexTest.FILE + "2.pdf");
            pdf.copy2(pdf2);

            luceneHelper.addFile(pdf);
            luceneHelper.addFile(pdf2);

            Assert.assertFalse(luceneHelper.fileNeedsIndexing(pdf2));

            pdf2.setLastModified(pdf2.lastModified() + 1);

            Assert.assertTrue(luceneHelper.fileNeedsIndexing(pdf2));
        } catch (Exception ex) {
            ex.printStackTrace(System.out);
            Assert.fail("" + ex);
        }
    }
}
