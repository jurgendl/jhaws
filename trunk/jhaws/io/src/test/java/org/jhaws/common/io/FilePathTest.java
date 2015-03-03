package org.jhaws.common.io;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Random;

import org.jhaws.common.io.FilePath.FileLineIterator;
import org.junit.Assert;
import org.junit.Test;

public class FilePathTest {
    private String testline = "testline";

    private int total = 10000;

    @Test
    public void absolute_path() {
        FilePath thisDir = new FilePath();
        FilePath p = new FilePath("dummyfile");
        Assert.assertNull(p.getParent());
        p = FilePath.class.cast(p.toAbsolutePath());
        Assert.assertEquals(thisDir, p.getParent());
    }

    @Test
    public void equals() {
        try {
            int index = 25;
            StringBuilder chars = new StringBuilder();
            for (char i = 'a'; i <= 'z'; i++) {
                chars.append(new Character(i)).append(new Character(Character.toUpperCase(i)));
            }
            for (char i = '0'; i <= '9'; i++) {
                chars.append(new Character(i));
            }
            Random r = new Random(0);
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < 30; i++) {
                sb.append(chars.charAt(r.nextInt(chars.length())));
                if (((i % 9) == 0) && (i != 0)) {
                    sb.append("_");
                }
            }
            FilePath tmp1File = FilePath.createDefaultTempFile("A-" + System.currentTimeMillis(), "txt");
            System.out.println(tmp1File.toAbsolutePath());
            FilePath tmp2File = FilePath.createDefaultTempFile("B-" + System.currentTimeMillis(), "txt");
            System.out.println(tmp2File.toAbsolutePath());
            System.out.println("=====================================");
            System.out.println(sb.toString());
            System.out.println("=====================================");
            tmp1File.write(sb.toString().getBytes());
            sb.setCharAt(index, '.');
            System.out.println(sb.toString());
            System.out.println("=====================================");
            tmp2File.write(sb.toString().getBytes());
            Assert.assertTrue(tmp1File.equals(tmp2File, 10, 5));
            System.out.println("=====================================");
            Assert.assertFalse(tmp1File.equals(tmp2File, 10, 50));
        } catch (IOException ex) {
            ex.printStackTrace(System.out);
            Assert.fail(String.valueOf(ex));
        } catch (Exception ex) {
            ex.printStackTrace(System.out);
            Assert.fail(String.valueOf(ex));
        }
    }

    @Test
    public void lines_closed_auto() {
        FileLineIterator closeMe = null;
        try {
            FilePath tmpFile = FilePath.createDefaultTempFile(String.valueOf(System.currentTimeMillis()), "csv");
            this.lines_prepare(tmpFile);
            try (FileLineIterator lines = tmpFile.lines()) {
                closeMe = lines;
            }
        } catch (IOException ex) {
            Assert.fail(String.valueOf(ex));
        } catch (Exception ex) {
            Assert.fail(String.valueOf(ex));
        }
        Assert.assertNotNull(closeMe);
        Assert.assertFalse(closeMe.hasNext());
        try {
            closeMe.next();
            Assert.fail("NoSuchElementException");// should throw NoSuchElementException
        } catch (NoSuchElementException ex) {
            // should throw NoSuchElementException
        }
    }

    @Test
    public void lines_closed_early() {
        try {
            FilePath tmpFile = FilePath.createDefaultTempFile(String.valueOf(System.currentTimeMillis()), "csv");
            this.lines_prepare(tmpFile);
            try (FileLineIterator lines = tmpFile.lines()) {
                lines.next();
                lines.close();
                lines.close();// should not throw exception
                try {
                    lines.next();
                    Assert.fail("NoSuchElementException");// should throw NoSuchElementException
                } catch (NoSuchElementException ex) {
                    // should throw NoSuchElementException
                }
            }
        } catch (IOException ex) {
            Assert.fail(String.valueOf(ex));
        } catch (Exception ex) {
            Assert.fail(String.valueOf(ex));
        }
    }

    @Test
    public void lines_normal() {
        int i = 0;
        try {
            FilePath tmpFile = FilePath.createDefaultTempFile(String.valueOf(System.currentTimeMillis()), "csv");
            this.lines_prepare(tmpFile);
            try (FileLineIterator lines = tmpFile.lines()) {
                while (lines.hasNext()) {
                    String line = lines.next();
                    i++;
                    Assert.assertEquals(this.testline, line);
                }
            }
        } catch (IOException ex) {
            Assert.fail(String.valueOf(ex));
        } catch (Exception ex) {
            Assert.fail(String.valueOf(ex));
        }
        Assert.assertEquals(this.total, i); // same number of lines
    }

    private void lines_prepare(FilePath tmpFile) throws IOException {
        try (BufferedWriter bw = tmpFile.newBufferedWriter()) {
            for (int i = 0; i < this.total; i++) {
                bw.write(this.testline);
                bw.write("\n");
            }
        }
    }
}
