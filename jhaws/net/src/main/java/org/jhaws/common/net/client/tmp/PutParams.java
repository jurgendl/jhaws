package org.jhaws.common.net.client.tmp;

import java.net.URI;

public class PutParams extends AbstractPutParams<PutParams> {
    private static final long serialVersionUID = -6581441197498420196L;

    public PutParams() {
        super();
    }

    public PutParams(String uri, String body, String mime) {
        super(uri, body, mime);
    }

    public PutParams(String uri) {
        super(uri);
    }

    public PutParams(URI uri, String body, String mime) {
        super(uri, body, mime);
    }

    public PutParams(URI uri) {
        super(uri);
    }
}
