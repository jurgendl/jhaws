package org.swingeasy.io;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author Jurgen
 */
public class InMemoryPipe implements Pipe {
    protected boolean closed = false;

    protected ByteArrayOutputStream out = new ByteArrayOutputStream(1024 * 8) {
        protected void check() {
            if (closed) {
                throw new RuntimeException(new IOException("stream closed"));
            }
        };

        @Override
        public void close() throws java.io.IOException {
            closed = true;
        };

        @Override
        public synchronized void reset() {
            super.reset();
            closed = false;
        }

        @Override
        public synchronized void write(byte[] b, int off, int len) {
            check();
            super.write(b, off, len);
        };

        @Override
        public synchronized void write(int b) {
            check();
            super.write(b);
        };
    };

    /**
     *
     * @see org.swingeasy.io.Pipe#getInputStream()
     */
    @Override
    public InputStream getInputStream() {
        if (!closed) {
            throw new RuntimeException(new IOException("stream not closed"));
        }
        return new ByteArrayInputStream(out.toByteArray());
    }

    /**
     *
     * @see org.swingeasy.io.Pipe#getOutputStream()
     */
    @Override
    public OutputStream getOutputStream() {
        return out;
    }
}
