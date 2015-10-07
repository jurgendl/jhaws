package org.jhaws.common.net.client.v45;

import java.net.URI;

public class OptionsRequest extends AbstractGetRequest<OptionsRequest> {
    private static final long serialVersionUID = -3335823798615631641L;

    public OptionsRequest() {
        super();
    }

    public OptionsRequest(URI uri) {
        super(uri);
    }

    public OptionsRequest(String uri) {
        super(uri);
    }
}
