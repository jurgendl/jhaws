package org.swingeasy;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author Jurgen
 */
public class InMemoryStream implements Stream {
    protected boolean closed = false;

    protected ByteArrayOutputStream out = new ByteArrayOutputStream(1024 * 8) {
        protected void check() {
            if (InMemoryStream.this.closed) {
                throw new RuntimeException(new IOException("stream closed"));
            }
        };

        @Override
        public void close() throws java.io.IOException {
            InMemoryStream.this.closed = true;
        };

        @Override
        public synchronized void reset() {
            super.reset();
            InMemoryStream.this.closed = false;
        }

        @Override
        public synchronized void write(byte[] b, int off, int len) {
            this.check();
            super.write(b, off, len);
        };

        @Override
        public synchronized void write(int b) {
            this.check();
            super.write(b);
        };
    };

    /**
     * 
     * @see org.swingeasy.Stream#getInputStream()
     */
    @Override
    public InputStream getInputStream() {
        if (!this.closed) {
            throw new RuntimeException(new IOException("stream not closed"));
        }
        return new ByteArrayInputStream(this.out.toByteArray());
    }

    /**
     * 
     * @see org.swingeasy.Stream#getOutputStream()
     */
    @Override
    public OutputStream getOutputStream() {
        return this.out;
    }
}
