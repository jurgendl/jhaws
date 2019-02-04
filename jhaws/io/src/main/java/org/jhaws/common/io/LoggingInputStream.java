package org.jhaws.common.io;

import java.io.IOException;
import java.io.InputStream;

public class LoggingInputStream extends InputStreamWrapper {
    private long size;

    public LoggingInputStream(InputStream wrapped) {
        super(wrapped);
    }

    @Override
    public int read() throws IOException {
        int read = super.read();
        size++;
        if (size % (1024 * 1024) == 0) {
            System.out.println((size / (1024 * 1024)) + "kB");
        }
        return read;
    }
}
