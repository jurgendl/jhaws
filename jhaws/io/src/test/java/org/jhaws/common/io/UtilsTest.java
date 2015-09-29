package org.jhaws.common.io;

import java.io.OutputStream;
import java.util.Random;

import org.junit.Assert;
import org.junit.Test;

public class UtilsTest {
	@Test
	public void testZip() {
		try {
			FilePath tmp = FilePath.createDefaultTempFile("tmp");
			Random rnd = new Random(System.currentTimeMillis());
			try (OutputStream out = tmp.newBufferedOutputStream()) {
				for (int i = 0; i < 10000; i++) {
					out.write(rnd.nextInt(127));
				}
			}
			FilePath zip = FilePath.createDefaultTempFile("zip");
			Utils.zip(zip.newOutputStream(), tmp.getFileNameString(), tmp.newInputStream());
			FilePath tmpdir = FilePath.createDefaultTempDirectory("tmpdir");
			Utils.unzip(zip.newInputStream(), tmpdir);
			Assert.assertArrayEquals(tmp.readAllBytes(), tmpdir.child(tmp.getFileNameString()).readAllBytes());
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}
}
