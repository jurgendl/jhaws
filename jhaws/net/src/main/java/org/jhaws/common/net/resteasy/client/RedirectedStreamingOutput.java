package org.jhaws.common.net.resteasy.client;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.StreamingOutput;

public class RedirectedStreamingOutput implements StreamingOutput {
    protected final InputStream input;

    public RedirectedStreamingOutput(InputStream input) {
        this.input = input;
    }

    public InputStream getInput() {
        return this.input;
    }

    /**
     * @see javax.ws.rs.core.StreamingOutput#write(java.io.OutputStream)
     */
    @Override
    public void write(OutputStream output) throws IOException, WebApplicationException {
        byte[] buffer = new byte[1024 * 4];
        int read;
        while ((read = input.read(buffer)) != -1) {
            output.write(buffer, 0, read);
        }
        output.close();
        input.close();
    }
}