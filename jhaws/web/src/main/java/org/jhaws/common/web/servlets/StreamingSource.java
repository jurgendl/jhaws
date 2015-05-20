package org.jhaws.common.web.servlets;

import java.io.IOException;
import java.io.Serializable;
import java.nio.channels.SeekableByteChannel;

public interface StreamingSource extends Serializable {
	boolean exists() throws IOException;

	long lastModified() throws IOException;

	long length() throws IOException;

	SeekableByteChannel newSeekableByteChannel() throws IOException;
}