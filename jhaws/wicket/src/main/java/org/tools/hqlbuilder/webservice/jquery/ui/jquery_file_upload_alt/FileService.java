package org.tools.hqlbuilder.webservice.jquery.ui.jquery_file_upload_alt;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

public interface FileService {
	public abstract int add(FileMeta file, InputStream inputStream) throws IOException;

	public abstract List<FileMeta> deleteFiles(String name) throws IOException;

	public abstract FileMeta getFile(String name) throws IOException;

	public abstract List<FileMeta> getFiles();

	public abstract List<FileMeta> getFiles(String name);

	public abstract void readFile(FileMeta file, OutputStream out) throws IOException;

	public abstract void remove(FileMeta file) throws IOException;
}