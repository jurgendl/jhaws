package org.jhaws.common.net.client.tmp;

import java.net.URI;

public class HeadParams extends GetParams {
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
