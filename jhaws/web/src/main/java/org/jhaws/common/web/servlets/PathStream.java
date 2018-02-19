package org.jhaws.common.web.servlets;

import java.io.IOException;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

import org.jhaws.common.io.FilePath;

public class PathStream implements StreamingSource {
    private static final long serialVersionUID = 8119635440380515310L;

    private final Path path;

    public PathStream(Path path) {
        this.path = path;
    }

    @Override
    public boolean exists() {
        return Files.exists(FilePath.getPath(path));
    }

    @Override
    public long lastModified() throws IOException {
        return Files.getLastModifiedTime(FilePath.getPath(path)).toMillis();
    }

    @Override
    public long length() throws IOException {
        return Files.size(FilePath.getPath(path));
    }

    @Override
    public SeekableByteChannel newSeekableByteChannel() throws IOException {
        return Files.newByteChannel(FilePath.getPath(path), StandardOpenOption.READ);
    }
}
