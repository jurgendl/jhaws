package org.tools.hqlbuilder.webservice.wicket;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import org.apache.wicket.util.resource.AbstractResourceStream;
import org.apache.wicket.util.resource.IResourceStream;
import org.apache.wicket.util.resource.ResourceStreamNotFoundException;

public class FileStreamResourceReference extends StreamResourceReference {
    private static final long serialVersionUID = 2108240451424543897L;

    protected transient Path path;

    public FileStreamResourceReference(Class<?> scope, String name, Path path) {
        super(scope, name);
        this.path = path;
    }

    public FileStreamResourceReference(Class<?> scope, Path path) {
        this(scope, path.getFileName().toString(), path);
    }

    @Override
    public IResourceStream getResourceStream() {
        return new AbstractResourceStream() {
            private static final long serialVersionUID = 8784023512223151193L;

            protected transient InputStream inputStream;

            @Override
            public InputStream getInputStream() throws ResourceStreamNotFoundException {
                if (inputStream == null) {
                    try {
                        inputStream = Files.newInputStream(path);
                    } catch (IOException e) {
                        throw new ResourceStreamNotFoundException(e);
                    }
                }
                return inputStream;
            }

            @Override
            public void close() throws IOException {
                if (inputStream != null)
                    inputStream.close();
            }
        };
    }
}
