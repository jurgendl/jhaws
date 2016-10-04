package org.jhaws.common.documents;

import org.jhaws.common.io.FilePath;

public class TxtFileTextExtracter implements FileTextExtracter {
	@Override
	public void extract(FilePath file, FilePath target) {
		if (file.equals(target)) return;
		file.copyTo(target);
	}
}
