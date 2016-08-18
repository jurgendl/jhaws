package org.jhaws.common.net.client;

import java.net.URI;

public class GetRequest extends AbstractGetRequest<GetRequest> {
	private static final long serialVersionUID = -5590561779919482437L;

	public GetRequest() {
		super();
	}

	public GetRequest(String uri) {
		super(uri);
	}

	public GetRequest(URI uri) {
		super(uri);
	}
}
