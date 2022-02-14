package org.jhaws.common.net.client;

import java.io.IOException;
import java.io.OutputStream;

public class RequestOutputStreamWrapper extends org.apache.commons.compress.utils.CountingOutputStream {
    protected RequestListener requestListener;

    public RequestOutputStreamWrapper(OutputStream out, RequestListener requestListener) {
        super(out);
        this.requestListener = requestListener;
    }

    @Override
    protected void count(final long written) {
        super.count(written);
        if (written != -1) {
            requestListener.bytesWritten(written, getBytesWritten());
        }
    }

    @Override
    public void write(final int b) throws IOException {
        super.write(b);
        requestListener.write(new byte[] { (byte) b }, 0, 1);
    }

    @Override
    public void write(final byte[] b, final int off, final int len) throws IOException {
        super.write(b, off, len);
        if (len > 0) {
            requestListener.write(b, off, len);
        }
    }
}
