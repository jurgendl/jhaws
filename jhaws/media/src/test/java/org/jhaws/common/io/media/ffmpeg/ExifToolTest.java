package org.jhaws.common.io.media.ffmpeg;

import org.jhaws.common.io.FilePath;
import org.jhaws.common.io.media.exif.ExifTool;
import org.junit.jupiter.api.Test;

public class ExifToolTest {
	boolean activate = false;

	@Test
	public void testExe() {
		if (!activate)
			return;
		ExifTool e = new ExifTool(false);
		e.setPath(System.getenv("EXIF"));
		System.out.println(e.getVersion());
	}

	@Test
	public void testPerl() {
		if (!activate)
			return;
		ExifTool e = new ExifTool(false);
		e.setExecutable(new FilePath(System.getenv("EXIFPERL")));
		e.setUsePerl(true);
		System.out.println(e.getVersion());
	}
}
