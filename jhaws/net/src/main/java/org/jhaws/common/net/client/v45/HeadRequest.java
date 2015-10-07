package org.jhaws.common.net.client.v45;

import java.net.URI;

public class HeadRequest extends AbstractGetRequest<HeadRequest> {
    private static final long serialVersionUID = -6947796698944347163L;

    public HeadRequest() {
        super();
    }

    public HeadRequest(URI uri) {
        super(uri);
    }

    public HeadRequest(String uri) {
        super(uri);
    }
}
