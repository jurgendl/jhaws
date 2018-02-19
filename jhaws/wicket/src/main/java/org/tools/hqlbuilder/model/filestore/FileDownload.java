package org.tools.hqlbuilder.model.filestore;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UncheckedIOException;
import java.net.HttpURLConnection;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.StreamingOutput;

import org.apache.commons.io.output.TeeOutputStream;
import org.tools.hqlbuilder.service.filestore.FileService;

import com.Ostermiller.util.CircularByteBuffer;

public class FileDownload implements StreamingOutput {
    private final FileService fileService;

    private final FileMeta file;

    public FileDownload(FileService fileService, FileMeta file) {
        super();
        this.fileService = fileService;
        this.file = file;
    }

    @Override
    public void write(OutputStream output) {
        CircularByteBuffer cbb = new CircularByteBuffer(CircularByteBuffer.INFINITE_SIZE);
        try (OutputStream tee = new TeeOutputStream(cbb.getOutputStream(), output)) {
            fileService.readFile(file, tee);
        } catch (IOException | UncheckedIOException ex) {
            throw new WebApplicationException(HttpURLConnection.HTTP_NOT_FOUND);
        }
    }
}
