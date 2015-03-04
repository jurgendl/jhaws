package org.jhaws.common.io;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.NoSuchFileException;
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
    public void create_delete() {
        try {
            FilePath fp = new FilePath(FilePath.getTempDirectory(), String.valueOf(System.currentTimeMillis()));
            fp.createDirectory();
            Assert.assertTrue(fp.exists());
            try {
                fp.createDirectory();
                Assert.fail("FileAlreadyExistsException");
            } catch (FileAlreadyExistsException ex) {
                //
            }
            fp.delete();
            Assert.assertFalse(fp.exists());
            try {
                fp.delete();
                Assert.fail("NoSuchFileException");
            } catch (NoSuchFileException ex) {
                //
            }
            fp.deleteIfExists();
            fp.deleteIfExists();
            fp = fp.createDirectory();
            try {
                fp.createFile();
                Assert.fail("AccessDeniedException");
            } catch (AccessDeniedException ex) {
                //
            }
            FilePath fpp = new FilePath(fp, "file.txt");
            fpp.createFile();
            try {
                fpp.createFile();
                Assert.fail("FileAlreadyExistsException");
            } catch (FileAlreadyExistsException ex) {
                //
            }
            try {
                fp.delete();
                Assert.fail("DirectoryNotEmptyException");
            } catch (DirectoryNotEmptyException ex) {
                //
            }
            fp.deleteAll();
            Assert.assertFalse(fp.exists());
            try {
                fp.deleteAll();
                Assert.fail("NoSuchFileException");
            } catch (NoSuchFileException ex) {
                //
            }
            fp.deleteAllIfExists();
            fp.deleteAllIfExists();
            fp.createDirectoryIfNotExists();
            Assert.assertTrue(fp.exists());
            fp.createDirectoryIfNotExists();
            fpp.createFileIfNotExists();
            Assert.assertTrue(fpp.exists());
            fpp.createFileIfNotExists();
            fp.deleteAllIfExists();
        } catch (Exception ex) {
            ex.printStackTrace();
            Assert.fail(String.valueOf(ex));
        }
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
            FilePath tmp2File = FilePath.createDefaultTempFile("B-" + System.currentTimeMillis(), "txt");
            tmp1File.write(sb.toString().getBytes());
            sb.setCharAt(index, '.');
            tmp2File.write(sb.toString().getBytes());
            Assert.assertTrue(tmp1File.equal(tmp2File, 5));
            Assert.assertFalse(tmp1File.equal(tmp2File, 50));
            Assert.assertFalse(tmp1File.equal(tmp2File));
        } catch (Exception ex) {
            ex.printStackTrace();
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
        } catch (Exception ex) {
            ex.printStackTrace();
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
        } catch (Exception ex) {
            ex.printStackTrace();
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
        } catch (Exception ex) {
            ex.printStackTrace();
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

    @Test
    public void size() {
        try {
            FilePath tmp = FilePath.createDefaultTempFile("prefix", "txt");
            byte[] bytes = "bytes".getBytes();
            try (BufferedOutputStream out = tmp.newBufferedOutputStream()) {
                out.write(bytes);
            }
            Assert.assertEquals(bytes.length, tmp.getTotalSize());
        } catch (Exception ex) {
            ex.printStackTrace();
            Assert.fail(String.valueOf(ex));
        }
    }
}
