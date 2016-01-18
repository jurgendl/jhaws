package org.jhaws.common.io;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;

public class ResourcePath {
	/**
	 * @see http://stackoverflow.com/questions/15713119/java-nio-file-path-for-a-classpath-resource
	 */
	public static Path path(URI uri) throws IOException, URISyntaxException {
		String scheme = uri.getScheme();
		if (scheme.equals("file")) {
			return Paths.get(uri);
		}
		if (!scheme.equals("jar")) {
			throw new UnsupportedOperationException("Cannot convert to Path: " + uri);
		}
		String s = uri.toString();
		int separator = s.indexOf("!/");
		String entryName = s.substring(separator + 2);
		URI fileURI = URI.create(s.substring(0, separator));
		FileSystem fs = FileSystems.newFileSystem(fileURI, Collections.<String, Object>emptyMap());
		return fs.getPath(entryName);
	}
}
