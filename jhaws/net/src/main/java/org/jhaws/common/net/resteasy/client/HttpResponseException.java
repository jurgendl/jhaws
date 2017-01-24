package org.jhaws.common.net.resteasy.client;

public class HttpResponseException extends RuntimeException {
    private static final long serialVersionUID = 848412227615981040L;

    private final int code;

    public HttpResponseException(int code, String message) {
        super(message);
        this.code = code;
    }

    public int getCode() {
        return this.code;
    }
}
