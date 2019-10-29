package org.jhaws.common.net.client5;

import java.io.IOException;

import org.apache.hc.client5.http.ClientProtocolException;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ClassicHttpResponse;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.HttpStatus;
import org.apache.hc.core5.http.io.HttpClientResponseHandler;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.junit.Test;

public class Client5Test {
	@Test
	public void test() {
		try (final CloseableHttpClient httpclient = HttpClients.createDefault()) {
			final HttpGet httpget = new HttpGet("http://httpbin.org/get");

			System.out.println("Executing request " + httpget.getMethod() + " " + httpget.getUri());

			// Create a custom response handler
			final HttpClientResponseHandler<String> responseHandler = new HttpClientResponseHandler<String>() {

				@Override
				public String handleResponse(final ClassicHttpResponse response) throws IOException {
					final int status = response.getCode();
					if (status >= HttpStatus.SC_SUCCESS && status < HttpStatus.SC_REDIRECTION) {
						final HttpEntity entity = response.getEntity();
						try {
							return entity != null ? EntityUtils.toString(entity) : null;
						} catch (final Exception ex) {
							throw new ClientProtocolException(ex);
						}
					} else {
						throw new ClientProtocolException("Unexpected response status: " + status);
					}
				}

			};
			final String responseBody = httpclient.execute(httpget, responseHandler);
			System.out.println("----------------------------------------");
			System.out.println(responseBody);
		} catch (Exception ex1) {
			ex1.printStackTrace(System.out);
		}
	}
}
