package org.jhaws.common.documents;

import java.io.IOException;
import java.io.InputStream;

import org.jhaws.common.io.FilePath;

public class TxtFileTextExtracter implements FileTextExtracter {
	@Override
	public void extract(InputStream file, FilePath target) {
		target.write(file);
	}

	@Override
	public void extract(FilePath file, FilePath target) throws IOException {
		if (file.equals(target))
			return;
		file.copyTo(target);
	}
}
