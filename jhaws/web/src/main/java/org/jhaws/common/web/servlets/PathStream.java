package org.jhaws.common.web.servlets;

import java.io.IOException;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class PathStream implements StreamingSource {
	private static final long serialVersionUID = 8119635440380515310L;

	private final Path path;

	public PathStream(Path path) {
		this.path = path;
	}

	@Override
	public boolean exists() {
		return Files.exists(path);
	}

	@Override
	public long lastModified() throws IOException {
		return Files.getLastModifiedTime(path).toMillis();
	}

	@Override
	public long length() throws IOException {
		return Files.size(path);
	}

	@Override
	public SeekableByteChannel newSeekableByteChannel() throws IOException {
		return Files.newByteChannel(path, StandardOpenOption.READ);
	}
}
