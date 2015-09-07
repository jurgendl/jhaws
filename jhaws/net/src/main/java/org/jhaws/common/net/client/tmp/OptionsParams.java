package org.jhaws.common.net.client.tmp;

import java.net.URI;

public class OptionsParams extends GetParams {
    private static final long serialVersionUID = -3335823798615631641L;

    public OptionsParams() {
        super();
    }

    public OptionsParams(URI uri) {
        super(uri);
    }

    public OptionsParams(String uri) {
        super(uri);
    }
}
