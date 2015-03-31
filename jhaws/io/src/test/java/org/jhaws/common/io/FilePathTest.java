package org.jhaws.common.io;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.AccessDeniedException;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.NoSuchFileException;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;

import org.jhaws.common.io.FilePath.Iterators.FileLineIterator;
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
            } catch (UncheckedIOException ex) {
                Assert.assertTrue(ex.getCause() instanceof FileAlreadyExistsException);
            }
            fp.delete();
            Assert.assertFalse(fp.exists());
            try {
                fp.delete();
                Assert.fail("NoSuchFileException");
            } catch (UncheckedIOException ex) {
                Assert.assertTrue(ex.getCause() instanceof NoSuchFileException);
            }
            fp.deleteIfExists();
            fp.deleteIfExists();
            fp = fp.createDirectory();
            try {
                fp.createFile();
                Assert.fail("AccessDeniedException");
            } catch (UncheckedIOException ex) {
                Assert.assertTrue(ex.getCause() instanceof AccessDeniedException);
            }
            FilePath fpp = new FilePath(fp, "file.txt");
            fpp.createFile();
            try {
                fpp.createFile();
                Assert.fail("FileAlreadyExistsException");
            } catch (UncheckedIOException ex) {
                Assert.assertTrue(ex.getCause() instanceof FileAlreadyExistsException);
            }
            try {
                fp.delete();
                Assert.fail("DirectoryNotEmptyException");
            } catch (UncheckedIOException ex) {
                Assert.assertTrue(ex.getCause() instanceof DirectoryNotEmptyException);
            }
            fp.deleteAll();
            Assert.assertFalse(fp.exists());
            try {
                fp.deleteAll();
                Assert.fail("NoSuchFileException");
            } catch (UncheckedIOException ex) {
                Assert.assertTrue(ex.getCause() instanceof NoSuchFileException);
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
    public void doubles() {
        String same = "same";
        try {
            FilePath tmpDir = FilePath.createDefaultTempDirectory(String.valueOf(System.currentTimeMillis()));
            FilePath subdir = tmpDir.child("subdir").createDirectories();
            FilePath file1 = tmpDir.child("file1");
            try (BufferedWriter out = file1.newBufferedWriter()) {
                out.write(same);
            }
            FilePath file2 = tmpDir.child("file2");
            try (BufferedWriter out = file2.newBufferedWriter()) {
                out.write(same);
            }
            FilePath file3 = tmpDir.child("file3");
            try (BufferedWriter out = file3.newBufferedWriter()) {
                out.write(same);
                out.write(same);
            }
            FilePath file4 = subdir.child("file4");
            try (BufferedWriter out = file4.newBufferedWriter()) {
                out.write(same);
            }
            Assert.assertTrue(file1.exists());
            Assert.assertTrue(file2.exists());
            Assert.assertTrue(file3.exists());
            Assert.assertTrue(file4.exists());
            List<FilePath> doubles = tmpDir.deleteDoubles();
            Assert.assertTrue(file1.exists() != file2.exists());
            Assert.assertEquals(1, doubles.size());
            Assert.assertTrue(doubles.iterator().next().notExists());
            Assert.assertTrue(file3.exists());
            Assert.assertTrue(file4.exists());
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
    public void extension() {
        try {
            Assert.assertNull(new FilePath("dir/test").getExtension());
            Assert.assertEquals("txt", new FilePath("dir", "test.txt").getExtension());
            Assert.assertEquals("TXT", new FilePath("dir", "test.TXT").getExtension());
            Assert.assertEquals("dir" + FilePath.getPathSeperator() + "test.doc", new FilePath("dir", "test.TXT").changeExtension("doc")
                    .getFullPathName());
            Assert.assertEquals("dir" + FilePath.getPathSeperator() + "test.DOC", new FilePath("dir", "test.TXT").changeExtension("DOC")
                    .getFullPathName());
            Assert.assertEquals("dir" + FilePath.getPathSeperator() + "test.TXT.doc", new FilePath("dir", "test.TXT").addExtension("doc")
                    .getFullPathName());
            Assert.assertEquals("dir" + FilePath.getPathSeperator() + "test.TXT.DOC", new FilePath("dir", "test.TXT").addExtension("DOC")
                    .getFullPathName());
        } catch (Exception ex) {
            ex.printStackTrace();
            Assert.fail(String.valueOf(ex));
        }
    }

    @Test
    public void flatten() {
        String same = "same";
        try {
            FilePath tmpDir = FilePath.createDefaultTempDirectory(String.valueOf(System.currentTimeMillis()));
            FilePath subdir = tmpDir.child("subdir").createDirectories();
            FilePath file1 = tmpDir.child("file1");
            try (BufferedWriter out = file1.newBufferedWriter()) {
                out.write(same);
            }
            FilePath file2 = tmpDir.child("file2");
            try (BufferedWriter out = file2.newBufferedWriter()) {
                out.write(same);
            }
            FilePath file3 = tmpDir.child("file3");
            try (BufferedWriter out = file3.newBufferedWriter()) {
                out.write(same);
                out.write(same);
            }
            FilePath file4 = subdir.child("file3");
            try (BufferedWriter out = file4.newBufferedWriter()) {
                out.write(same);
            }
            tmpDir.flatten();
            Assert.assertTrue(file1.exists());
            Assert.assertTrue(file2.exists());
            Assert.assertTrue(file3.exists());
            Assert.assertTrue(subdir.notExists());
            Assert.assertTrue(file4.notExists());
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
    public void list() {
        try {
            FilePath tmpDir = FilePath.createDefaultTempDirectory(String.valueOf(System.currentTimeMillis()));
            FilePath file = tmpDir.child("file").createFile();
            FilePath subdir = tmpDir.child("subdir").createDirectory();
            FilePath subdirfile = subdir.child("subdirfile").createFile();
            FilePath subsubdir = subdir.child("subsubdir").createDirectory();
            FilePath subsubdirfile = subsubdir.child("subsubdirfile").createFile();

            Assert.assertEquals(Arrays.asList(file), tmpDir.listFiles());
            Assert.assertEquals(Arrays.asList(subdir), tmpDir.listDirectories());
            Assert.assertEquals(Arrays.asList(file, subdir), tmpDir.list());

            Assert.assertEquals(Arrays.asList(subdirfile), subdir.listFiles());
            Assert.assertEquals(Arrays.asList(subsubdir), subdir.listDirectories());
            Assert.assertEquals(Arrays.asList(subdirfile, subsubdir), subdir.list());

            Assert.assertEquals(Arrays.asList(subsubdirfile), subsubdir.listFiles());
            Assert.assertEquals(0, subsubdir.listDirectories().size());
            Assert.assertEquals(Arrays.asList(subsubdirfile), subsubdir.list());

            Assert.assertEquals(Arrays.asList(file, subdirfile, subsubdirfile), tmpDir.listFiles(true));
            Assert.assertEquals(Arrays.asList(subdir, subsubdir), tmpDir.listDirectories(true));
            Assert.assertEquals(Arrays.asList(file, subdir, subdirfile, subsubdir, subsubdirfile), tmpDir.list(true));

            Assert.assertEquals(Arrays.asList(subdirfile, subsubdirfile), subdir.listFiles(true));
            Assert.assertEquals(Arrays.asList(subsubdir), subdir.listDirectories(true));
            Assert.assertEquals(Arrays.asList(subdirfile, subsubdir, subsubdirfile), subdir.list(true));

            Assert.assertEquals(Arrays.asList(subsubdirfile), subsubdir.listFiles(true));
            Assert.assertEquals(0, subsubdir.listDirectories(true).size());
            Assert.assertEquals(Arrays.asList(subsubdirfile), subsubdir.list(true));
        } catch (Exception ex) {
            ex.printStackTrace();
            Assert.fail(String.valueOf(ex));
        }
    }

    @Test
    public void move() {
        try {
            FilePath tmpDir = FilePath.createDefaultTempDirectory("A" + String.valueOf(System.currentTimeMillis()));
            FilePath file = tmpDir.child("file").createFile();
            FilePath subdir = tmpDir.child("subdir").createDirectory();
            FilePath subdirfile = subdir.child("subdirfile").createFile();
            FilePath subsubdir = subdir.child("subsubdir").createDirectory();
            FilePath subsubdirfile = subsubdir.child("subsubdirfile").createFile();

            FilePath tmpDir2 = FilePath.createDefaultTempDirectory("B" + String.valueOf(System.currentTimeMillis()));
            tmpDir.moveTo(tmpDir2);

            FilePath file2 = tmpDir2.child("file");
            FilePath subdir2 = tmpDir2.child("subdir");
            FilePath subdirfile2 = subdir2.child("subdirfile");
            FilePath subsubdir2 = subdir2.child("subsubdir");
            FilePath subsubdirfile2 = subsubdir2.child("subsubdirfile");

            Assert.assertTrue(file.notExists());
            Assert.assertTrue(subdir.notExists());
            Assert.assertTrue(subdirfile.notExists());
            Assert.assertTrue(subsubdir.notExists());
            Assert.assertTrue(subsubdirfile.notExists());

            Assert.assertTrue(file2.exists());
            Assert.assertTrue(subdir2.exists());
            Assert.assertTrue(subdirfile2.exists());
            Assert.assertTrue(subsubdir2.exists());
            Assert.assertTrue(subsubdirfile2.exists());

            FilePath tmpDir3 = new FilePath(tmpDir2.getParent(), "C" + String.valueOf(System.currentTimeMillis()));
            Assert.assertTrue(tmpDir3.notExists());
            tmpDir2.moveTo(tmpDir3);
            Assert.assertTrue(tmpDir3.exists());
            Assert.assertTrue(tmpDir2.notExists());
        } catch (Exception ex) {
            ex.printStackTrace();
            Assert.fail(String.valueOf(ex));
        }
    }

    @Test
    public void seperators() {
        try {
            Assert.assertEquals("\\", FilePath.getPathSeperator());
        } catch (Exception ex) {
            Assert.assertEquals("/", FilePath.getPathSeperator());
        }
        Assert.assertEquals(".", FilePath.getFileSeperator());
    }

    @Test
    public void size() {
        try {
            FilePath tmp = FilePath.createDefaultTempFile("prefix", "txt");
            byte[] bytes = "bytes".getBytes();
            try (BufferedOutputStream out = tmp.newBufferedOutputStream()) {
                out.write(bytes);
            }
            Assert.assertEquals(bytes.length, tmp.getFolderSize());
        } catch (Exception ex) {
            ex.printStackTrace();
            Assert.fail(String.valueOf(ex));
        }
    }
}
