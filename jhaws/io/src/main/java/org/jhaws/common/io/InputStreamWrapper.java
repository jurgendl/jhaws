package org.jhaws.common.io;

import java.io.IOException;
import java.io.InputStream;

public class InputStreamWrapper extends InputStream {
    private final InputStream wrapped;

    public InputStreamWrapper(InputStream wrapped) {
        this.wrapped = wrapped;
    }

    @Override
    public int read() throws IOException {
        return wrapped.read();
    }

    public InputStream getWrapped() {
        return this.wrapped;
    }
}
