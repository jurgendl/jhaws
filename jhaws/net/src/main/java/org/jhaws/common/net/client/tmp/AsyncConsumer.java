package org.jhaws.common.net.client.tmp;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;

import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.nio.IOControl;
import org.apache.http.nio.client.methods.AsyncByteConsumer;
import org.apache.http.protocol.HttpContext;
import org.jhaws.common.net.client.HTTPClientDefaults;

public class AsyncConsumer extends AsyncByteConsumer<OutputStream> {
    protected final OutputStream out;

    public AsyncConsumer(OutputStream out) {
        super(HTTPClientDefaults.BUFF_LEN);
        this.out = out;
    }

    @Override
    protected void onByteReceived(ByteBuffer buf, IOControl ioctrl) throws IOException {
        copy(buf, out);
        out.flush();
    }

    @Override
    protected void onResponseReceived(HttpResponse response) throws HttpException, IOException {
        //
    }

    @Override
    protected void releaseResources() {
        super.releaseResources();
        try {
            out.close();
        } catch (Exception e) {
            //
        }
    }

    @Override
    protected OutputStream buildResult(HttpContext context) throws Exception {
        return out;
    }

    public static int copy(ByteBuffer src, OutputStream dest) throws IOException {
        int len = src.remaining();
        int totalWritten = 0;
        byte[] buf = null;
        while (totalWritten < len) {
            int bytesToWrite = Math.min(len - totalWritten, HTTPClientDefaults.BUFF_LEN);
            if (buf == null || buf.length < bytesToWrite) {
                buf = new byte[bytesToWrite];
            }
            src.get(buf, 0, bytesToWrite);
            dest.write(buf, 0, bytesToWrite);
            totalWritten += bytesToWrite;
        }
        return totalWritten;
    }
}
