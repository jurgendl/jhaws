package org.jhaws.common.io;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;

import org.jhaws.common.g11n.G11n;
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

//	@Test
//	public void create_delete() {
//		try {
//			FilePath fp = new FilePath(FilePath.getTempDirectory(), String.valueOf(System.currentTimeMillis()));
//			fp.createDirectory();
//			Assert.assertTrue(fp.exists());
//			try {
//				fp.createDirectory();
//				Assert.fail("FileAlreadyExistsException");
//			} catch (UncheckedIOException ex) {
//				Assert.assertTrue(ex.getCause() instanceof FileAlreadyExistsException);
//			}
//			fp.delete(true);
//			Assert.assertFalse(fp.exists());
//			try {
//				fp.delete(true);
//				Assert.fail("NoSuchFileException");
//			} catch (UncheckedIOException ex) {
//				Assert.assertTrue(ex.getCause() instanceof NoSuchFileException);
//			}
//			fp.delete();
//			fp.delete();
//			fp = fp.createDirectory();
//			try {
//				fp.createFile();
//				Assert.fail("AccessDeniedException");
//			} catch (UncheckedIOException ex) {
//				Assert.assertTrue(ex.getCause() instanceof AccessDeniedException);
//			}
//			FilePath fpp = new FilePath(fp, "file.txt");
//			fpp.createFile();
//			try {
//				fpp.createFile();
//				Assert.fail("FileAlreadyExistsException");
//			} catch (UncheckedIOException ex) {
//				Assert.assertTrue(ex.getCause() instanceof FileAlreadyExistsException);
//			}
//			try {
//				fp.delete(true);
//				Assert.fail("DirectoryNotEmptyException");
//			} catch (UncheckedIOException ex) {
//				Assert.assertTrue(ex.getCause() instanceof DirectoryNotEmptyException);
//			}
//			fp.delete();
//			Assert.assertFalse(fp.exists());
//			try {
//				fp.delete(true);
//				Assert.fail("NoSuchFileException");
//			} catch (UncheckedIOException ex) {
//				Assert.assertTrue(ex.getCause() instanceof NoSuchFileException);
//			}
//			fp.delete();
//			fp.delete();
//			fp.createDirectory();
//			Assert.assertTrue(fp.exists());
//			fp.createDirectory();
//			fpp.createFileIfNotExists();
//			Assert.assertTrue(fpp.exists());
//			fpp.createFileIfNotExists();
//			fp.delete();
//		} catch (Exception ex) {
//			ex.printStackTrace();
//			Assert.fail(String.valueOf(ex));
//		}
//	}

	@Test
	public void doubles() {
		String same = "same";
		try {
			FilePath tmpDir = FilePath.createTempDirectory(String.valueOf(System.currentTimeMillis()));
			FilePath subdir = tmpDir.child("subdir").createDirectory();
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
			Collection<List<FilePath>> doubles = tmpDir.deleteDuplicates();
			Assert.assertTrue(file1.exists() != file2.exists());
			Assert.assertEquals(1, doubles.size());
			doubles.stream().forEach(c -> {
				c.stream().limit(1).forEach(fp -> Assert.assertTrue(fp.exists()));
				c.stream().skip(1).forEach(fp -> Assert.assertTrue(fp.notExists()));
			});
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
				chars.append(Character.valueOf(i)).append(Character.valueOf(Character.toUpperCase(i)));
			}
			for (char i = '0'; i <= '9'; i++) {
				chars.append(Character.valueOf(i));
			}
			Random r = new Random(0);
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < 30; i++) {
				sb.append(chars.charAt(r.nextInt(chars.length())));
				if (((i % 9) == 0) && (i != 0)) {
					sb.append("_");
				}
			}
			FilePath tmp1File = FilePath.createTempFile("A-" + System.currentTimeMillis(), "txt");
			FilePath tmp2File = FilePath.createTempFile("B-" + System.currentTimeMillis(), "txt");
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
			Assert.assertEquals("dir" + FilePath.getSystemPathSeperator() + "test.doc",
					new FilePath("dir", "test.TXT").changeExtension("doc").getFullPathName());
			Assert.assertEquals("dir" + FilePath.getSystemPathSeperator() + "test.DOC",
					new FilePath("dir", "test.TXT").changeExtension("DOC").getFullPathName());
			Assert.assertEquals("dir" + FilePath.getSystemPathSeperator() + "test.TXT.doc",
					new FilePath("dir", "test.TXT").addExtension("doc").getFullPathName());
			Assert.assertEquals("dir" + FilePath.getSystemPathSeperator() + "test.TXT.DOC",
					new FilePath("dir", "test.TXT").addExtension("DOC").getFullPathName());
		} catch (Exception ex) {
			ex.printStackTrace();
			Assert.fail(String.valueOf(ex));
		}
	}

	@Test
	public void flatten() {
		String same = "same";
		try {
			FilePath tmpDir = FilePath.createTempDirectory(String.valueOf(System.currentTimeMillis()));
			FilePath subdir = tmpDir.child("subdir").createDirectory();
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
			FilePath subdir2 = subdir.child("subdir2").createDirectory();
			FilePath file5 = subdir2.child("file5");
			try (BufferedWriter out = file5.newBufferedWriter()) {
				out.write(same);
			}
			tmpDir.flatten();
			Assert.assertTrue(file1.exists());
			Assert.assertTrue(file2.exists());
			Assert.assertTrue(file3.exists());
			Assert.assertTrue(subdir.notExists());
			Assert.assertTrue(file4.notExists());
			Assert.assertTrue(tmpDir.child(file4.getFileName()).exists());
			Assert.assertTrue(subdir2.notExists());
			Assert.assertTrue(file5.notExists());
			Assert.assertTrue(tmpDir.child(file5.getFileName()).exists());
		} catch (Exception ex) {
			ex.printStackTrace();
			Assert.fail(String.valueOf(ex));
		}
	}

	@Test
	public void lines_closed_auto() {
		FileLineIterator closeMe = null;
		try {
			FilePath tmpFile = FilePath.createTempFile(String.valueOf(System.currentTimeMillis()), "csv");
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
			Assert.fail("NoSuchElementException");// should throw
													// NoSuchElementException
		} catch (NoSuchElementException ex) {
			// should throw NoSuchElementException
		}
	}

	@Test
	public void lines_closed_early() {
		try {
			FilePath tmpFile = FilePath.createTempFile(String.valueOf(System.currentTimeMillis()), "csv");
			this.lines_prepare(tmpFile);
			try (FileLineIterator lines = tmpFile.lines()) {
				lines.next();
				lines.close();
				lines.close();// should not throw exception
				try {
					lines.next();
					Assert.fail("NoSuchElementException");// should throw
															// NoSuchElementException
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
			FilePath tmpFile = FilePath.createTempFile(String.valueOf(System.currentTimeMillis()), "csv");
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

//	@Test
//	public void list() {
//		try {
//			FilePath tmpDir = FilePath.createTempDirectory(String.valueOf(System.currentTimeMillis()));
//			FilePath file = tmpDir.child("file").createFile();
//			FilePath subdir = tmpDir.child("subdir").createDirectory();
//			FilePath subdirfile = subdir.child("subdirfile").createFile();
//			FilePath subsubdir = subdir.child("subsubdir").createDirectory();
//			FilePath subsubdirfile = subsubdir.child("subsubdirfile").createFile();
//
//			Assert.assertEquals(Arrays.asList(file), tmpDir.listFiles());
//			Assert.assertEquals(Arrays.asList(subdir), tmpDir.listDirectories());
//			Assert.assertEquals(Arrays.asList(file, subdir), tmpDir.list());
//
//			Assert.assertEquals(Arrays.asList(subdirfile), subdir.listFiles());
//			Assert.assertEquals(Arrays.asList(subsubdir), subdir.listDirectories());
//			Assert.assertEquals(Arrays.asList(subdirfile, subsubdir), subdir.list());
//
//			Assert.assertEquals(Arrays.asList(subsubdirfile), subsubdir.listFiles());
//			Assert.assertEquals(0, subsubdir.listDirectories().size());
//			Assert.assertEquals(Arrays.asList(subsubdirfile), subsubdir.list());
//
//			Assert.assertEquals(Arrays.asList(file, subdirfile, subsubdirfile), tmpDir.listFiles(true));
//			Assert.assertEquals(Arrays.asList(subdir, subsubdir), tmpDir.listDirectories(true));
//			Assert.assertEquals(Arrays.asList(file, subdir, subdirfile, subsubdir, subsubdirfile), tmpDir.list(true));
//
//			Assert.assertEquals(Arrays.asList(subdirfile, subsubdirfile), subdir.listFiles(true));
//			Assert.assertEquals(Arrays.asList(subsubdir), subdir.listDirectories(true));
//			Assert.assertEquals(Arrays.asList(subdirfile, subsubdir, subsubdirfile), subdir.list(true));
//
//			Assert.assertEquals(Arrays.asList(subsubdirfile), subsubdir.listFiles(true));
//			Assert.assertEquals(0, subsubdir.listDirectories(true).size());
//			Assert.assertEquals(Arrays.asList(subsubdirfile), subsubdir.list(true));
//		} catch (Exception ex) {
//			ex.printStackTrace();
//			Assert.fail(String.valueOf(ex));
//		}
//	}

	@Test
	public void move() {
		try {
			FilePath tmpDir = FilePath.createTempDirectory("A" + String.valueOf(System.currentTimeMillis()));
			FilePath file = tmpDir.child("file").createFile();
			FilePath subdir = tmpDir.child("subdir").createDirectory();
			FilePath subdirfile = subdir.child("subdirfile").createFile();
			FilePath subsubdir = subdir.child("subsubdir").createDirectory();
			FilePath subsubdirfile = subsubdir.child("subsubdirfile").createFile();

			FilePath tmpDir2 = FilePath.createTempDirectory("B" + String.valueOf(System.currentTimeMillis()));
			tmpDir.moveAllTo(tmpDir2);

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
	public void size() {
		try {
			FilePath tmp = FilePath.createTempFile("prefix", "txt");
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

	@Test
	public void test() {
		{
			Path p1 = Paths.get("path");
			Path p2 = Paths.get("path");
			Assert.assertEquals(p1, p2);
			List<Path> l = new ArrayList<>();
			l.add(p1);
			l.remove(p2);
			Assert.assertEquals(0, l.size());
		}
		{
			FilePath p1 = new FilePath("path");
			FilePath p2 = new FilePath("path");
			Assert.assertEquals(p1, p2);
			List<FilePath> l = new ArrayList<>();
			l.add(p1);
			l.remove(p2);
			Assert.assertEquals(0, l.size());
		}
	}

	@Test
	public void testSizeText1() {
		try {
			FilePath tmp = FilePath.createTempFile("prefix", "txt");
			byte[] bytes = new byte[5];
			try (BufferedOutputStream out = tmp.newBufferedOutputStream()) {
				out.write(bytes);
			}
			Assert.assertEquals("5 B", tmp.getHumanReadableFileSize());
		} catch (Exception ex) {
			ex.printStackTrace();
			Assert.fail(String.valueOf(ex));
		}
	}

	@Test
	public void testSizeText2() {
		try {
			FilePath tmp = FilePath.createTempFile("prefix", "txt");
			byte[] bytes = new byte[1024];
			try (BufferedOutputStream out = tmp.newBufferedOutputStream()) {
				out.write(bytes);
			}
			Assert.assertEquals("1 KiB", tmp.getHumanReadableFileSize());
		} catch (Exception ex) {
			ex.printStackTrace();
			Assert.fail(String.valueOf(ex));
		}
	}

	@Test
	public void testSizeText3() {
		try {
			FilePath tmp = FilePath.createTempFile("prefix", "txt");
			byte[] bytes = new byte[1024 + 512];
			try (BufferedOutputStream out = tmp.newBufferedOutputStream()) {
				out.write(bytes);
			}
			Assert.assertEquals("1" + G11n.getDecimalFormatSymbols().getDecimalSeparator() + "5 KiB",
					tmp.getHumanReadableFileSize());
		} catch (Exception ex) {
			ex.printStackTrace();
			Assert.fail(String.valueOf(ex));
		}
	}

	@Test
	public void testSizeText4() {
		try {
			FilePath tmp = FilePath.createTempFile("prefix", "txt");
			byte[] bytes = new byte[1024 + 256];
			try (BufferedOutputStream out = tmp.newBufferedOutputStream()) {
				out.write(bytes);
			}
			Assert.assertEquals("1" + G11n.getDecimalFormatSymbols().getDecimalSeparator() + "25 KiB",
					tmp.getHumanReadableFileSize());
		} catch (Exception ex) {
			ex.printStackTrace();
			Assert.fail(String.valueOf(ex));
		}
	}

	@Test
	public void testSizeText5() {
		try {
			FilePath tmp = FilePath.createTempFile("prefix", "txt");
			byte[] bytes = new byte[1024 + 128];
			try (BufferedOutputStream out = tmp.newBufferedOutputStream()) {
				out.write(bytes);
			}
			Assert.assertEquals("1" + G11n.getDecimalFormatSymbols().getDecimalSeparator() + "125 KiB",
					tmp.getHumanReadableFileSize());
		} catch (Exception ex) {
			ex.printStackTrace();
			Assert.fail(String.valueOf(ex));
		}
	}

	@Test
	public void testSizeText6() {
		try {
			FilePath tmp = FilePath.createTempFile("prefix", "txt");
			byte[] bytes = new byte[1024 + 64];
			try (BufferedOutputStream out = tmp.newBufferedOutputStream()) {
				out.write(bytes);
			}
			Assert.assertEquals("1" + G11n.getDecimalFormatSymbols().getDecimalSeparator() + "062 KiB",
					tmp.getHumanReadableFileSize());
		} catch (Exception ex) {
			ex.printStackTrace();
			Assert.fail(String.valueOf(ex));
		}
	}

	@Test
	public void testResource() {
		{
			String path = "FilePath.txt";
			{
				FilePath filePath = new FilePath(path);
				System.out.println(filePath.getAbsolutePath());
				Assert.assertNotNull(filePath.getAbsolutePath());
			}
			{
				FilePath filePath = FilePath.of(FilePathTest.class, path);
				System.out.println(filePath.getAbsolutePath());
				Assert.assertNotNull(filePath.getAbsolutePath());
			}
			{
				FilePath filePath = FilePath.of((Class<?>) null, path);
				System.out.println(filePath.getAbsolutePath());
				Assert.assertNotNull(filePath.getAbsolutePath());
			}
			{
				FilePath filePath = FilePath.of(FilePathTest.class.getClassLoader(), path);
				System.out.println(filePath.getAbsolutePath());
				Assert.assertNotNull(filePath.getAbsolutePath());
			}
			{
				FilePath filePath = FilePath.of((ClassLoader) null, path);
				System.out.println(filePath.getAbsolutePath());
				Assert.assertNotNull(filePath.getAbsolutePath());
			}
			{
				FilePath filePath = FilePath.of(FilePath.url(path, (Class<?>) null, (ClassLoader) null));
				System.out.println(filePath.getAbsolutePath());
				Assert.assertNotNull(filePath.getAbsolutePath());
			}
			{
				FilePath filePath = FilePath.of(FilePath.uri(FilePath.url(path, (Class<?>) null, (ClassLoader) null)));
				System.out.println(filePath.getAbsolutePath());
				Assert.assertNotNull(filePath.getAbsolutePath());
			}
			{
				FilePath filePath = FilePath.of(FilePath.url(path, FilePathTest.class, (ClassLoader) null));
				System.out.println(filePath.getAbsolutePath());
				Assert.assertNotNull(filePath.getAbsolutePath());
			}
			{
				FilePath filePath = FilePath
						.of(FilePath.url(path, FilePathTest.class, FilePathTest.class.getClassLoader()));
				System.out.println(filePath.getAbsolutePath());
				Assert.assertNotNull(filePath.getAbsolutePath());
			}
			{
				FilePath filePath = FilePath
						.of(FilePath.uri(FilePath.url(path, FilePathTest.class, (ClassLoader) null)));
				System.out.println(filePath.getAbsolutePath());
				Assert.assertNotNull(filePath.getAbsolutePath());
			}
			{
				FilePath filePath = FilePath
						.of(FilePath.url(path, (Class<?>) null, FilePathTest.class.getClassLoader()));
				System.out.println(filePath.getAbsolutePath());
				Assert.assertNotNull(filePath.getAbsolutePath());
			}
			{
				FilePath filePath = FilePath
						.of(FilePath.uri(FilePath.url(path, (Class<?>) null, FilePathTest.class.getClassLoader())));
				System.out.println(filePath.getAbsolutePath());
				Assert.assertNotNull(filePath.getAbsolutePath());
			}
			{
				FilePath filePath = FilePath
						.of(FilePath.url(path, FilePathTest.class, FilePathTest.class.getClassLoader()));
				System.out.println(filePath.getAbsolutePath());
				Assert.assertNotNull(filePath.getAbsolutePath());
			}
			{
				FilePath filePath = FilePath
						.of(FilePath.uri(FilePath.url(path, FilePathTest.class, FilePathTest.class.getClassLoader())));
				System.out.println(filePath.getAbsolutePath());
				Assert.assertNotNull(filePath.getAbsolutePath());
			}
		}
		{
			String path = "FilePathTest_" + System.currentTimeMillis() + ".txt";
			FilePath tmp = FilePath.getTempDirectory().child(path);
			System.out.println(tmp.getAbsolutePath());
			tmp.write(path);
			FilePath zip = tmp.appendExtension("zip");
			System.out.println(zip.getAbsolutePath());
			zip.zip(tmp);
			ClassLoader classLoader = URLClassLoader.newInstance(new URL[] { zip.toUrl() });
			{
				FilePath filePath = FilePath.of(classLoader, path);
				System.out.println(filePath.getAbsolutePath());
				Assert.assertNotNull(filePath.getAbsolutePath());
			}
			{
				FilePath filePath = FilePath.of(FilePath.url(path, null, classLoader));
				System.out.println(filePath.getAbsolutePath());
				Assert.assertNotNull(filePath.getAbsolutePath());
			}
			{
				FilePath filePath = FilePath.of(FilePath.uri(FilePath.url(path, null, classLoader)));
				System.out.println(filePath.getAbsolutePath());
				Assert.assertNotNull(filePath.getAbsolutePath());
			}
		}
	}

	// @Test
	// public void testOpen() {
	// String fn = "FilePathTest_" + System.currentTimeMillis() + ".txt";
	// FilePath tmp = FilePath.getTempDirectory().child(fn);
	// System.out.println(tmp.getAbsolutePath());
	// tmp.write(fn);
	// tmp.open();
	// }

//    @Test
//    public void testRecycle() {
//        String path = "FilePathTest_" + System.currentTimeMillis() + ".txt";
//        FilePath tmp = FilePath.getTempDirectory().child(path);
//        System.out.println(tmp.getAbsolutePath());
//        tmp.write(path);
//        Assert.assertTrue(tmp.exists());
//        tmp.recycle();
//        Assert.assertFalse(tmp.exists());
//    }

	@Test
	public void testCreateIfNotExists() {
		FilePath d = FilePath.getTempDirectory().child("dir" + System.currentTimeMillis())
				.child("dir" + System.currentTimeMillis());
		Assert.assertFalse(d.exists());
		d.createDirectory();
		Assert.assertTrue(d.exists());
		d.createDirectory();
		Assert.assertTrue(d.exists());
	}

//	@Test
//	public void testLink() {
//		FilePath d1 = FilePath.getTempDirectory().child("dir" + System.currentTimeMillis() + "-1");
//		d1.createDirectory();
//		FilePath d2 = FilePath.getTempDirectory().child("dir" + System.currentTimeMillis() + "-2");
//		d2.createSymbolicLinkFrom(d1);
//		System.out.println(d1);
//		System.out.println(d2);
//	}
}
