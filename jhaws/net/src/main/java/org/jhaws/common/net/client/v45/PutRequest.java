package org.jhaws.common.net.client.v45;

import java.net.URI;

public class PutRequest extends AbstractPutRequest<PutRequest> {
    private static final long serialVersionUID = -6581441197498420196L;

    public PutRequest() {
        super();
    }

    public PutRequest(String uri, String body, String mime) {
        super(uri, body, mime);
    }

    public PutRequest(String uri) {
        super(uri);
    }

    public PutRequest(URI uri, String body, String mime) {
        super(uri, body, mime);
    }

    public PutRequest(URI uri) {
        super(uri);
    }
}
