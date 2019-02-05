package org.jhaws.common.io;

import java.io.IOException;
import java.io.OutputStream;

public class OutputStreamWrapper extends OutputStream {
    private final OutputStream outputStream;

    public OutputStreamWrapper(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    @Override
    public void write(int b) throws IOException {
        outputStream.write(b);
    }

    public OutputStream getOutputStream() {
        return this.outputStream;
    }

}
