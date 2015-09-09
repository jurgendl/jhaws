package org.jhaws.common.net.client.latest;

import java.net.URI;

public class HeadParams extends AbstractGetParams<HeadParams> {
    private static final long serialVersionUID = -6947796698944347163L;

    public HeadParams() {
        super();
    }

    public HeadParams(URI uri) {
        super(uri);
    }

    public HeadParams(String uri) {
        super(uri);
    }
}
