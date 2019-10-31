package org.jhaws.common.net.client5;

import java.util.concurrent.CountDownLatch;

import org.apache.hc.core5.concurrent.FutureCallback;
import org.apache.hc.core5.http.HttpResponse;
import org.apache.hc.core5.http.Message;
import org.apache.hc.core5.http.nio.AsyncClientEndpoint;

public class HttpResponseCallback<T> implements FutureCallback<Message<HttpResponse, T>> {
	public HttpResponseCallback(CountDownLatch latch, AsyncClientEndpoint clientEndpoint, String requestUri) {
		this.requestUri = requestUri;
		this.latch = latch;
		this.clientEndpoint = clientEndpoint;
	}

	T body;

	final CountDownLatch latch;

	final AsyncClientEndpoint clientEndpoint;

	final String requestUri;

	int total;

	@Override
	public void completed(Message<HttpResponse, T> message) {
		clientEndpoint.releaseAndReuse();
		HttpResponse response = message.getHead();
		body = message.getBody();
		int len = 0;
		try {
			len = Integer.parseInt(response.getHeader("content-length").getValue());
		} catch (Exception ex) {
			//
		}
		total += len;
		System.out.println(Thread.currentThread().getName() + " :: " + requestUri + " -> " + response.getCode() + " :: "
				+ response.getVersion() + " :: " + len);
		latch.countDown();
		completedBody(body);
	}

	protected void completedBody(T body) {
		//
	}

	@Override
	public void failed(Exception ex) {
		clientEndpoint.releaseAndReuse();
		latch.countDown();
		System.out.println(requestUri + "->" + ex);
	}

	@Override
	public void cancelled() {
		clientEndpoint.releaseAndReuse();
		latch.countDown();
		System.out.println(requestUri + " cancelled");
	}

	public T getBody() {
		return this.body;
	}

	public int getTotal() {
		return this.total;
	}
}