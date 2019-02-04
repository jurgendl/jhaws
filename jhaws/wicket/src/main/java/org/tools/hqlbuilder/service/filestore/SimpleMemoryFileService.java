package org.tools.hqlbuilder.service.filestore;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.io.IOUtils;
import org.tools.hqlbuilder.model.filestore.FileMeta;

public class SimpleMemoryFileService implements FileService {
    private Map<FileMeta, byte[]> files = new HashMap<>();

    /**
     * 
     * @see org.tools.hqlbuilder.service.filestore.FileService#add(org.tools.hqlbuilder.model.filestore.FileMeta, java.io.InputStream)
     */
    @Override
    public int add(FileMeta file, InputStream inputStream) throws IOException {
        byte[] byteArray = IOUtils.toByteArray(inputStream);
        files.put(file, byteArray);
        file.size = byteArray.length;
        return byteArray.length;
    }

    /**
     * 
     * @see org.tools.hqlbuilder.service.filestore.FileService#deleteFiles(java.lang.String)
     */
    @Override
    public List<FileMeta> deleteFiles(String name) {
        List<FileMeta> toRemove = this.getFiles(name);
        toRemove.forEach(file -> remove(file));
        return toRemove;
    }

    /**
     * 
     * @see org.tools.hqlbuilder.service.filestore.FileService#getFile(java.lang.String)
     */
    @Override
    public FileMeta getFile(String name) throws IOException {
        FileMeta file = this.getFiles().stream().filter(candidate -> candidate.getName().equals(name)).findAny().orElse(null);
        if (file == null) {
            throw new IOException(name);
        }
        return file;
    }

    /**
     * 
     * @see org.tools.hqlbuilder.service.filestore.FileService#getFiles()
     */
    @Override
    public List<FileMeta> getFiles() {
        return new ArrayList<>(files.keySet());
    }

    /**
     * 
     * @see org.tools.hqlbuilder.service.filestore.FileService#getFiles(java.lang.String)
     */
    @Override
    public List<FileMeta> getFiles(String name) {
        return this.getFiles().stream().filter(candidate -> candidate.getName().equals(name)).collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * 
     * @see org.tools.hqlbuilder.service.filestore.FileService#readFile(org.tools.hqlbuilder.model.filestore.FileMeta, java.io.OutputStream)
     */
    @Override
    public void readFile(FileMeta file, OutputStream out) throws IOException {
        out.write(files.get(file));
        out.flush();
    }

    /**
     * 
     * @see org.tools.hqlbuilder.service.filestore.FileService#remove(org.tools.hqlbuilder.model.filestore.FileMeta)
     */
    @Override
    public void remove(FileMeta file) {
        files.remove(file);
    }

    @Override
    public byte[] readFileFully(FileMeta file) throws IOException {
        return files.get(file);
    }
}
