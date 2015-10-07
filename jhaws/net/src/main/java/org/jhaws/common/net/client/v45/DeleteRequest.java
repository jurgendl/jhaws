package org.jhaws.common.net.client.v45;

import java.net.URI;

public class DeleteRequest extends AbstractGetRequest<DeleteRequest> {
    private static final long serialVersionUID = 7789744009919283043L;

    public DeleteRequest() {
        super();
    }

    public DeleteRequest(URI uri) {
        super(uri);
    }

    public DeleteRequest(String uri) {
        super(uri);
    }
}