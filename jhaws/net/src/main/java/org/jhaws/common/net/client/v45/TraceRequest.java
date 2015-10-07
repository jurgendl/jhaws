package org.jhaws.common.net.client.v45;

import java.net.URI;

public class TraceRequest extends AbstractGetRequest<TraceRequest> {
    private static final long serialVersionUID = -113043755384527216L;

    public TraceRequest() {
        super();
    }

    public TraceRequest(URI uri) {
        super(uri);
    }

    public TraceRequest(String uri) {
        super(uri);
    }
}