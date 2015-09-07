package org.jhaws.common.net.client.tmp;

import java.io.Serializable;
import java.net.URI;

public class Params implements Serializable {
    private static final long serialVersionUID = -8834915649537196310L;

    protected URI uri;

    public Params() {
        super();
    }

    public Params(URI uri) {
        setUri(uri);
    }

    public Params(String uri) {
        setUri(uri);
    }

    public URI getUri() {
        return uri;
    }

    public void setUri(URI uri) {
        this.uri = uri;
    }

    public void setUri(String uri) {
        this.uri = URI.create(uri);
    }
}