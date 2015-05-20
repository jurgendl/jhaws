package org.jhaws.common.web.servlets;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.SeekableByteChannel;

public class FileStream implements StreamingSource {
	private static final long serialVersionUID = 8806901042756473249L;

	private final File file;

	public FileStream(File file) {
		this.file = file;
	}

	@Override
	public boolean exists() {
		return file.exists();
	}

	@Override
	public long lastModified() {
		return file.lastModified();
	}

	@Override
	public long length() {
		return file.length();
	}

	@SuppressWarnings("resource")
	@Override
	public SeekableByteChannel newSeekableByteChannel() throws IOException {
		return new RandomAccessFile(file, "r").getChannel();
	}
}