package org.jhaws.common.web.wicket.filestore;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

public interface FileService {
    int add(FileMeta file, InputStream inputStream) throws IOException;

    List<FileMeta> deleteFiles(String name) throws IOException;

    FileMeta getFile(String name) throws IOException;

    List<FileMeta> getFiles();

    List<FileMeta> getFiles(String name);

    void readFile(FileMeta file, OutputStream out) throws IOException;

    byte[] readFileFully(FileMeta file) throws IOException;

    void remove(FileMeta file) throws IOException;
}