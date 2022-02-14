package org.jhaws.common.net.client;

import java.io.IOException;
import java.io.UncheckedIOException;

@SuppressWarnings("serial")
public class HttpException extends UncheckedIOException {
    private final Response response;
    private final int statusCode;

    public HttpException(Response response, int statusCode, String statusText) {
        super(new IOException(statusCode + ": " + statusText));
        this.response = response;
        this.statusCode = statusCode;
    }

    public Response getResponse() {
        return this.response;
    }

    public int getStatusCode() {
        return this.statusCode;
    }
}
