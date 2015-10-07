package org.jhaws.common.net.client.v45;

import java.net.URI;

public abstract class AbstractPutRequest<T extends AbstractPutRequest<? super T>> extends AbstractGetRequest<T> {
    private static final long serialVersionUID = -6103040334975043729L;

    protected String body;

    protected String mime;

    public AbstractPutRequest() {
        super();
    }

    public AbstractPutRequest(URI uri) {
        super(uri);
    }

    public AbstractPutRequest(String uri) {
        super(uri);
    }

    public AbstractPutRequest(String uri, String body, String mime) {
        super(uri);
        this.body = body;
        this.mime = mime;
    }

    public AbstractPutRequest(URI uri, String body, String mime) {
        super(uri);
        this.body = body;
        this.mime = mime;
    }

    public String getBody() {
        return body;
    }

    public T setBody(String body) {
        this.body = body;
        return cast();
    }

    public String getMime() {
        return mime;
    }

    public T setMime(String mime) {
        this.mime = mime;
        return cast();
    }
}