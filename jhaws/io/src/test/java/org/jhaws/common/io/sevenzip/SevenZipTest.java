package org.jhaws.common.io.sevenzip;

import org.jhaws.common.io.FilePath;
import org.junit.Test;

public class SevenZipTest {
	@Test
	public void test() {
		FilePath tmpdir = FilePath.getTempDirectory();
		SevenZip ws7 = new WinSevenZip();
		FilePath tmp = tmpdir.child("7ztest").createDirectoryIfNotExists();
		tmp.child("testfile1.txt").write("testfile1.txt");
		tmp.child("subdir").createDirectoryIfNotExists().child("testfile2.txt").write("testfile2.txt");
		FilePath archive = tmpdir.child("7ztestarchive.7z");
		ws7.compress(archive, null, tmp.getAbsolutePath() + "/*.*");
		ws7.list(archive, null);
		FilePath target = tmpdir.child("7ztestextract");
		ws7.extract(archive, null, target);
	}
}
