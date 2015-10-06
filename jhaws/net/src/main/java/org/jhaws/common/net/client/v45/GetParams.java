package org.jhaws.common.net.client.v45;

import java.net.URI;

public class GetParams extends AbstractGetParams<GetParams> {
    private static final long serialVersionUID = -5590561779919482437L;

    public GetParams() {
        super();
    }

    public GetParams(String uri) {
        super(uri);
    }

    public GetParams(URI uri) {
        super(uri);
    }
}
