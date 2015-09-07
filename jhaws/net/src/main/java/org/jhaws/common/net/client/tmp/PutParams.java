package org.jhaws.common.net.client.tmp;

import java.net.URI;

public class PutParams extends GetParams {
    private static final long serialVersionUID = -6103040334975043729L;

    protected String body;

    protected String mime;

    public PutParams() {
        super();
    }

    public PutParams(URI uri) {
        super(uri);
    }

    public PutParams(String uri) {
        super(uri);
    }

    public PutParams(String uri, String body, String mime) {
        super(uri);
        this.body = body;
        this.mime = mime;
    }

    public PutParams(URI uri, String body, String mime) {
        super(uri);
        this.body = body;
        this.mime = mime;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getMime() {
        return mime;
    }

    public void setMime(String mime) {
        this.mime = mime;
    }
}