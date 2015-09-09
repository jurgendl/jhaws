package org.jhaws.common.net.client.tmp;

import java.net.URI;

public class TraceParams extends AbstractGetParams<TraceParams> {
    private static final long serialVersionUID = -113043755384527216L;

    public TraceParams() {
        super();
    }

    public TraceParams(URI uri) {
        super(uri);
    }

    public TraceParams(String uri) {
        super(uri);
    }
}