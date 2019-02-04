package org.jhaws.common.io;

import java.io.IOException;
import java.io.OutputStream;

public class LoggingOutputStream extends OutputStreamWrapper {
    private long size;

    public LoggingOutputStream(OutputStream outputStream) {
        super(outputStream);
    }

    @Override
    public void write(int b) throws IOException {
        super.write(b);
        size++;
        if (size % (1024 * 1024) == 0) {
            System.out.println((size / (1024 * 1024)) + "kB");
        }
    }
}
