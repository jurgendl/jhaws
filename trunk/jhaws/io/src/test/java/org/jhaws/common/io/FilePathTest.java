package org.jhaws.common.io;

import java.io.BufferedWriter;
import java.io.IOException;

import org.jhaws.common.io.FilePath.FileLineIterator;
import org.junit.Assert;
import org.junit.Test;

public class FilePathTest {
    @Test
    public void lines() {
        String testline = "testline";
        BufferedWriter _bw = null;
        int total = 10000;
        int i = 0;
        try {
            FilePath tmpFile = FilePath.createDefaultTempFile(String.valueOf(System.currentTimeMillis()), "csv");
            System.out.println(tmpFile.toAbsolutePath());
            try (BufferedWriter bw = tmpFile.newBufferedWriter()) {
                _bw = bw;
                for (i = 0; i < total; i++) {
                    bw.write(testline);
                    bw.write("\n");
                }
            }
            FileLineIterator lines = tmpFile.lines();
            i = 0;
            while (lines.hasNext()) {
                String line = lines.next();
                i++;
                Assert.assertEquals(testline, line);
            }
        } catch (IOException ex) {
            Assert.fail(String.valueOf(ex));
        } catch (Exception ex) {
            Assert.fail(String.valueOf(ex));
        }
        Assert.assertNotNull(_bw);
        Assert.assertEquals(total, i);
    }

    @Test
    public void test() {
        FilePath thisDir = new FilePath();
        FilePath p = new FilePath("dummyfile");
        Assert.assertNull(p.getParent());
        p = FilePath.class.cast(p.toAbsolutePath());
        Assert.assertEquals(thisDir, p.getParent());
    }
}
