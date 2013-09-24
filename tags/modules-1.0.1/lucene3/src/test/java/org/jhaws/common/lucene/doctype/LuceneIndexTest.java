package org.jhaws.common.lucene.doctype;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import junit.framework.Assert;

import org.apache.lucene.document.Document;
import org.jhaws.common.io.IODirectory;
import org.jhaws.common.io.IOFile;
import org.jhaws.common.lucene.LuceneDocument;
import org.jhaws.common.lucene.LuceneIndex;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * test LuceneIndex
 * 
 * @author Jurgen
 */
@SuppressWarnings("deprecation")
public class LuceneIndexTest {
    /** dir */
    private IODirectory newTempDir;

    /** file */
    private IOFile ioFile;

    /** index */
    private LuceneIndex index;

    @SuppressWarnings("unused")
    private static int i = 0;

    /**
     * Creates a new LuceneIndexTest object.
     */
    public LuceneIndexTest() {
        super();
    }

    /**
     * 
     * @see junit.framework.TestCase#setUp()
     */
    @Before
    public void setUp() {
        try {
            // newTempDir = IODirectory.newTempDir("lucene-test-"+(i++));

            this.newTempDir = IODirectory.newTempDir("lucene-test");

            if (this.newTempDir.exists()) {
                this.newTempDir.erase();
            }

            if (this.newTempDir.exists()) {
                IOException ex = new IOException(this.newTempDir + " exists");
                throw ex;
            }

            this.index = (LuceneIndex) LuceneIndex.get(this.newTempDir);
            this.ioFile = new IOFile(this.newTempDir, "text.txt");
            this.index.index(this.ioFile.writeBytes("text"));
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
    }

    /**
     * 
     * @see junit.framework.TestCase#tearDown()
     */
    @After
    public void tearDown() throws Exception {
        this.index.close();
        this.newTempDir.erase();

        if (this.newTempDir.exists()) {
            throw (new IOException("cannot delete"));
        }
    }

    /**
     * LuceneHelper method for {@link org.jhaws.common.lucene.LuceneIndex#cleanUpNotExistingFileDocuments()}.
     */
    @Test
    public final void testCleanUpNotExistingFileDocuments() {
        try {
            Assert.assertEquals(0, this.index.cleanUpNotExistingFileDocuments());
        } catch (Exception ex) {
            Assert.fail("" + ex);
        }
    }

    /**
     * LuceneHelper method for {@link org.jhaws.common.lucene.LuceneIndex#delete()}.
     */
    @Test
    public final void testDelete() {
        try {
            Assert.assertTrue(this.index.delete());
        } catch (Exception ex) {
            Assert.fail("" + ex);
        }
    }

    /**
     * LuceneHelper method for {@link org.jhaws.common.lucene.LuceneIndex#deleteDocument(util.io.IOFile)}.
     */
    @Test
    public final void testDeleteDocumentIOFile() {
        try {
            this.index.deleteDocument(this.ioFile);
        } catch (Exception ex) {
            Assert.fail("" + ex);
        }
    }

    /**
     * LuceneHelper method for {@link org.jhaws.common.lucene.LuceneIndex#deleteDocument(java.lang.String)}.
     */
    @Test
    public final void testDeleteDocumentString() {
        try {
            this.index.deleteDocument(this.ioFile.toString());
        } catch (Exception ex) {
            Assert.fail("" + ex);
        }
    }

    /**
     * LuceneHelper method for {@link org.jhaws.common.lucene.LuceneIndex#deleteDocument(java.net.URL)}.
     */
    @Test
    public final void testDeleteDocumentURL() {
        try {
            this.index.deleteDocument(this.ioFile.toURI().toURL());
        } catch (Exception ex) {
            Assert.fail("" + ex);
        }
    }

    /**
     * LuceneHelper method for {@link org.jhaws.common.lucene.LuceneIndex#escape(java.lang.String)}.
     */
    @Test
    public final void testEscape() {
        Assert.assertEquals("\\+\\-\\&\\|\\!\\(\\)\\{\\}\\[\\]\\^\\~\\*\\?\\:\\\\\\\"", this.index.escape("+-&|!(){}[]^~*?:\\\""));
    }

    /**
     * LuceneHelper method for {@link org.jhaws.common.lucene.LuceneIndex#explain(org.apache.lucene.search.Query, int)}.
     */
    @Test
    public final void testExplain() {
        // TODO
    }

    /**
     * LuceneHelper method for {@link org.jhaws.common.lucene.LuceneIndex#findDocument(util.io.IOFile)}.
     */
    @Test
    public final void testFindDocumentIOFile() {
        try {
            Document doc = this.index.findDocument(this.ioFile);
            Assert.assertNotNull(doc);
        } catch (IOException ex) {
            Assert.fail("" + ex);
        }
    }

    /**
     * LuceneHelper method for {@link org.jhaws.common.lucene.LuceneIndex#findDocument(java.net.URL)}.
     */
    @Test
    public final void testFindDocumentURL() {
        try {
            Document doc = this.index.findDocument(this.ioFile.toURI().toURL());
            Assert.assertNotNull(doc);
        } catch (IOException ex) {
            Assert.fail("" + ex);
        }
    }

    /**
     * LuceneHelper method for {@link org.jhaws.common.lucene.LuceneIndex#getDefaultMaxSearchResults()}.
     */
    @Test
    public final void testGetDefaultMaxSearchResults() {
        Assert.assertEquals(100, this.index.getDefaultMaxSearchResults());
    }

    /**
     * LuceneHelper method for {@link org.jhaws.common.lucene.LuceneIndex#getDocCount()}.
     */
    @Test
    public final void testGetDocCount() {
        try {
            Assert.assertEquals(1, this.index.getDocCount());
        } catch (Exception ex) {
            Assert.fail("" + ex);
        }
    }

    /**
     * LuceneHelper method for {@link org.jhaws.common.lucene.LuceneIndex#getDocument(int)}.
     */
    @Test
    public final void testGetDocument() {
        try {
            System.out.println(this.index.getDocument(0));
        } catch (Exception ex) {
            Assert.fail("" + ex);
        }
    }

    /**
     * LuceneHelper method for {@link org.jhaws.common.lucene.LuceneIndex#getFieldNames()}.
     */
    @Test
    public final void testGetFieldNames() {
        try {
            System.out.println(this.index.getFieldNames());
        } catch (Exception ex) {
            Assert.fail("" + ex);
        }
    }

    /**
     * LuceneHelper method for {@link org.jhaws.common.lucene.LuceneIndex#getIndexLocation()}.
     */

    @Test
    public final void testGetIndexLocation() {
        Assert.assertEquals(IODirectory.newTempDir("lucene-test"), this.index.getIndexLocation());
    }

    /**
     * LuceneHelper method for {@link org.jhaws.common.lucene.LuceneIndex#get(util.io.IODirectory, boolean)}.
     */
    @Test
    public final void testGetIODirectoryBoolean() {
        // TODO
    }

    /**
     * LuceneHelper method for {@link org.jhaws.common.lucene.LuceneIndex#getLanguage()}.
     */
    @Test
    public final void testGetLanguage() {
        Assert.assertEquals("nl", this.index.getLanguage());
    }

    /**
     * LuceneHelper method for {@link org.jhaws.common.lucene.LuceneIndex#getLockId()}.
     */
    @Test
    public final void testGetLockId() {
        try {
            Assert.assertTrue(this.index.getLockId().startsWith("lucene-"));
        } catch (Exception ex) {
            Assert.fail("" + ex);
        }
    }

    /**
     * LuceneHelper method for {@link org.jhaws.common.lucene.LuceneIndex#getLuceneVersion()}.
     */
    @Test
    public final void testGetLuceneVersion() {
        // 3.0.0 883080 - 2009-11-22 15:43:58
        Assert.assertTrue(this.index.getLuceneVersion().startsWith("3"));
    }

    /**
     * LuceneHelper method for {@link org.jhaws.common.lucene.LuceneIndex#getMaxBufferedDocs()}.
     */
    @Test
    public final void testGetMaxBufferedDocs() {
        try {
            this.index.getMaxBufferedDocs();
        } catch (Exception ex) {
            Assert.fail("" + ex);
        }
    }

    /**
     * LuceneHelper method for {@link org.jhaws.common.lucene.LuceneIndex#getMaxFieldLength()}.
     */
    @Test
    public final void testGetMaxFieldLength() {
        try {
            this.index.getMaxFieldLength();
        } catch (Exception ex) {
            Assert.fail("" + ex);
        }
    }

    /**
     * LuceneHelper method for {@link org.jhaws.common.lucene.LuceneIndex#getMergeFactor()}.
     */
    @Test
    public final void testGetMergeFactor() {
        try {
            this.index.getMergeFactor();
        } catch (Exception ex) {
            Assert.fail("" + ex);
        }
    }

    /**
     * LuceneHelper method for {@link org.jhaws.common.lucene.LuceneIndex#getModificationDate()}.
     */
    @Test
    public final void testGetModificationDate() {
        Assert.assertNotNull(this.index.getModificationDate());
    }

    /**
     * LuceneHelper method for {@link org.jhaws.common.lucene.LuceneIndex#getVersion()}.
     */
    @Test
    public final void testGetVersion() {
        try {
            Assert.assertTrue(this.index.getVersion() > 0);
        } catch (Exception ex) {
            Assert.fail("" + ex);
        }
    }

    /**
     * LuceneHelper method for {@link org.jhaws.common.lucene.LuceneIndex#getVersionDate()}.
     */
    @Test
    public final void testGetVersionDate() {
        try {
            Assert.assertNotNull(this.index.getVersionDate());
        } catch (Exception ex) {
            Assert.fail("" + ex);
        }
    }

    /**
     * LuceneHelper method for {@link org.jhaws.common.lucene.LuceneIndex#index(util.io.IOFile)}.
     */
    @Test
    public final void testIndexIOFile() {
        // TODO
    }

    /**
     * LuceneHelper method for {@link org.jhaws.common.lucene.LuceneIndex#index(util.io.IOFile, java.lang.String, java.lang.String[])}.
     */
    @Test
    public final void testIndexIOFileStringStringArray() {
        // TODO
    }

    /**
     * LuceneHelper method for
     * {@link org.jhaws.common.lucene.LuceneIndex#index(java.lang.String, java.lang.String, java.lang.String, java.lang.String[])}.
     */
    @Test
    public final void testIndexStringStringStringStringArray() {
        // TODO
    }

    /**
     * LuceneHelper method for {@link org.jhaws.common.lucene.LuceneIndex#index(java.net.URL)}.
     */
    @Test
    public final void testIndexURL() {
        // TODO
    }

    /**
     * LuceneHelper method for {@link org.jhaws.common.lucene.LuceneIndex#index(java.net.URL, java.lang.String, java.lang.String[])}.
     */
    @Test
    public final void testIndexURLStringStringArray() {
        // TODO
    }

    /**
     * LuceneHelper method for {@link org.jhaws.common.lucene.LuceneIndex#isCacheText()}.
     */
    @Test
    public final void testIsCacheText() {
        Assert.assertFalse(this.index.isCacheText());
    }

    /**
     * LuceneHelper method for {@link org.jhaws.common.lucene.LuceneIndex#listDocuments()}.
     */
    @Test
    public final void testListDocuments() {
        try {
            List<LuceneDocument> listDocuments = this.index.listDocuments();
            Assert.assertNotNull(listDocuments);

            for (LuceneDocument doc : listDocuments) {
                System.out.println(doc);
            }
        } catch (Exception ex) {
            Assert.fail("" + ex);
        }
    }

    /**
     * LuceneHelper method for {@link org.jhaws.common.lucene.LuceneIndex#listDocuments(java.io.PrintStream)}.
     */
    @Test
    public final void testListDocumentsPrintStream() {
        try {
            this.index.listDocuments(System.out);
        } catch (IOException ex) {
            Assert.fail("" + ex);
        }
    }

    /**
     * LuceneHelper method for {@link org.jhaws.common.lucene.LuceneIndex#moveDocument(util.io.IOFile, util.io.IOFile)}.
     */
    @Test
    public final void testMoveDocumentIOFileIOFile() {
        try {
            this.index.moveDocument(this.ioFile, new IOFile(this.ioFile.getAbsolutePath() + ".txt"));
        } catch (IOException ex) {
            Assert.fail("" + ex);
        }
    }

    /**
     * LuceneHelper method for {@link org.jhaws.common.lucene.LuceneIndex#moveDocument(java.lang.String, java.lang.String)}.
     */
    @Test
    public final void testMoveDocumentStringString() {
        // TODO
    }

    /**
     * LuceneHelper method for {@link org.jhaws.common.lucene.LuceneIndex#moveDocument(java.net.URL, java.net.URL)}.
     */
    @Test
    public final void testMoveDocumentURLURL() {
        // TODO
    }

    /**
     * LuceneHelper method for {@link org.jhaws.common.lucene.LuceneIndex#needsIndexing(util.io.IOFile)}.
     */
    @Test
    public final void testNeedsIndexingIOFile() {
        try {
            Assert.assertFalse(this.index.needsIndexing(this.ioFile));
        } catch (IOException ex) {
            Assert.fail("" + ex);
        }
    }

    /**
     * LuceneHelper method for {@link org.jhaws.common.lucene.LuceneIndex#needsIndexing(java.net.URL)}.
     */
    @Test
    public final void testNeedsIndexingURL() {
        try {
            Assert.assertFalse(this.index.needsIndexing(this.ioFile.toURI().toURL()));
        } catch (IOException ex) {
            Assert.fail("" + ex);
        }
    }

    /**
     * LuceneHelper method for {@link org.jhaws.common.lucene.LuceneIndex#newExtensionFilter()}.
     */
    @Test
    public final void testNewExtensionFilter() {
        System.out.println(LuceneIndex.newExtensionFilter());
    }

    /**
     * LuceneHelper method for {@link org.jhaws.common.lucene.LuceneIndex#newExtensionFilter(java.lang.String[])}.
     */
    @Test
    public final void testNewExtensionFilterStringArray() {
        System.out.println(org.jhaws.common.lucene.LuceneIndex.newExtensionFilter("txt"));
    }

    /**
     * LuceneHelper method for {@link org.jhaws.common.lucene.LuceneIndex#optimize()}.
     */
    @Test
    public final void testOptimize() {
        try {
            this.index.optimize();
        } catch (IOException ex) {
            Assert.fail("" + ex);
        }
    }

    /**
     * LuceneHelper method for {@link org.jhaws.common.lucene.LuceneIndex#search(java.lang.String)}.
     */
    @Test
    public final void testSearch() {
        // TODO
    }

    /**
     * LuceneHelper method for {@link org.jhaws.common.lucene.LuceneIndex#searchSingleWord(java.lang.String)}.
     */
    @Test
    public final void testSearchSingleWord() {
        // TODO
    }

    /**
     * LuceneHelper method for {@link org.jhaws.common.lucene.LuceneIndex#setCacheText(boolean)}.
     */
    @Test
    public final void testSetCacheText() {
        this.index.setCacheText(true);
    }

    /**
     * LuceneHelper method for {@link org.jhaws.common.lucene.LuceneIndex#setDefaultMaxSearchResults(int)}.
     */
    @Test
    public final void testSetDefaultMaxSearchResults() {
        this.index.setDefaultMaxSearchResults(250);
    }

    /**
     * LuceneHelper method for {@link org.jhaws.common.lucene.LuceneIndex#setLanguage(java.util.Locale)}.
     */
    @Test
    public final void testSetLanguage() {
        try {
            this.index.setLanguage(Locale.getDefault());
        } catch (Exception ex) {
            Assert.fail("" + ex);
        }
    }

    /**
     * LuceneHelper method for {@link org.jhaws.common.lucene.LuceneIndex#setMaxBufferedDocs(int)}.
     */
    @Test
    public final void testSetMaxBufferedDocs() {
        try {
            this.index.setMaxBufferedDocs(20);
        } catch (Exception ex) {
            Assert.fail("" + ex);
        }
    }

    /**
     * LuceneHelper method for {@link org.jhaws.common.lucene.LuceneIndex#setMaxFieldLength(int)}.
     */
    @Test
    public final void testSetMaxFieldLength() {
        try {
            this.index.setMaxFieldLength(5000);
        } catch (Exception ex) {
            Assert.fail("" + ex);
        }
    }

    /**
     * LuceneHelper method for {@link org.jhaws.common.lucene.LuceneIndex#setMergeFactor(int)}.
     */
    @Test
    public final void testSetMergeFactor() {
        try {
            this.index.setMergeFactor(20);
        } catch (Exception ex) {
            Assert.fail("" + ex);
        }
    }

    /**
     * LuceneHelper method for {@link org.jhaws.common.lucene.LuceneIndex#topDocTerms(int)}.
     */
    @Test
    public final void testTopDocTermsInt() {
        // TODO
    }

    /**
     * LuceneHelper method for {@link org.jhaws.common.lucene.LuceneIndex#topDocTerms(int, java.lang.String)}.
     */
    @Test
    public final void testTopDocTermsIntString() {
        // TODO
    }

    /**
     * LuceneHelper method for {@link org.jhaws.common.lucene.LuceneIndex#topTerms(int)}.
     */
    @Test
    public final void testTopTerms() {
        // TODO
    }
}
