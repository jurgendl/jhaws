package org.jhaws.common.net.client;

import java.net.URI;

public interface RequestListener {
    void bytesWritten(long written, long total);

    void start(URI uri, long contentLength);

    void end();

    void write(byte[] b, int off, int len);
}
