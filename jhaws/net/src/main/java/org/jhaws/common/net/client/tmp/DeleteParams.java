package org.jhaws.common.net.client.tmp;

import java.net.URI;

public class DeleteParams extends GetParams {
    private static final long serialVersionUID = 7789744009919283043L;

    public DeleteParams() {
        super();
    }

    public DeleteParams(URI uri) {
        super(uri);
    }

    public DeleteParams(String uri) {
        super(uri);
    }
}