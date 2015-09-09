package org.jhaws.common.net.client.tmp;

import java.net.URI;

public abstract class AbstractPutParams<T extends AbstractPutParams<? super T>> extends AbstractGetParams<T> {
    private static final long serialVersionUID = -6103040334975043729L;

    protected String body;

    protected String mime;

    public AbstractPutParams() {
        super();
    }

    public AbstractPutParams(URI uri) {
        super(uri);
    }

    public AbstractPutParams(String uri) {
        super(uri);
    }

    public AbstractPutParams(String uri, String body, String mime) {
        super(uri);
        this.body = body;
        this.mime = mime;
    }

    public AbstractPutParams(URI uri, String body, String mime) {
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