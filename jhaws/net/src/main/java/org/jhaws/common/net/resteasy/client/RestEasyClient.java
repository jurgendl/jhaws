package org.jhaws.common.net.resteasy.client;

import java.net.URI;

public class RestEasyClient<R> extends org.jhaws.common.net.resteasy.client.NewRestEasyClient<R> {
	public RestEasyClient(Class<R> resourceClass) {
		super(resourceClass);
	}

	public RestEasyClient(String serviceUrl, Class<R> resourceClass) {
		super(serviceUrl, resourceClass);
	}

	public RestEasyClient(URI serviceUrl, Class<R> resourceClass) {
		super(serviceUrl, resourceClass);
	}
}
