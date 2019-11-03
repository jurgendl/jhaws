package org.jhaws.common.net.client5;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.apache.hc.client5.http.async.methods.HttpRequests;
import org.apache.hc.client5.http.async.methods.SimpleHttpRequest;
import org.apache.hc.client5.http.async.methods.SimpleHttpResponse;
import org.apache.hc.client5.http.async.methods.SimpleRequestProducer;
import org.apache.hc.client5.http.async.methods.SimpleResponseConsumer;
import org.apache.hc.client5.http.impl.async.CloseableHttpAsyncClient;
import org.apache.hc.client5.http.impl.async.HttpAsyncClients;
import org.apache.hc.core5.concurrent.FutureCallback;
import org.apache.hc.core5.http.HttpRequest;
import org.apache.hc.core5.http.nio.AsyncRequestProducer;
import org.apache.hc.core5.http.nio.AsyncResponseConsumer;
import org.apache.hc.core5.io.CloseMode;

// https://ok2c.github.io/httpclient-migration-guide/
public class Http2ClientTest {
	public static void main(String[] args) {
//		H2Config h2Config = H2Config.custom()//
//				.setPushEnabled(false)//
//				.setMaxConcurrentStreams(50)//
//				.build();

		CloseableHttpAsyncClient client = HttpAsyncClients.createHttp2Default();
		// CloseableHttpAsyncClient client =
		// HttpAsyncClients.createHttp2System();
//		HttpAsyncClients.customHttp2()
//				.setTlsStrategy(ClientTlsStrategyBuilder.create().setSslContext(SSLContexts.createSystemDefault())
//						.setTlsVersions(TLS.V_1_3, TLS.V_1_2).build())
//				.setIOReactorConfig(IOReactorConfig.custom().setSoTimeout(Timeout.ofSeconds(5)).build())
//				.setDefaultRequestConfig(RequestConfig.custom().setConnectTimeout(Timeout.ofSeconds(5))
//						.setResponseTimeout(Timeout.ofSeconds(5)).setCookieSpec(CookieSpecs.STANDARD.ident).build())
//				.setH2Config(h2Config).build();
		client.start();

		// CookieStore cookieStore = new BasicCookieStore();

		// CredentialsProvider credentialsProvider = new
		// BasicCredentialsProvider();

		// HttpClientContext clientContext = HttpClientContext.create();
		// clientContext.setCookieStore(cookieStore);
		// clientContext.setCredentialsProvider(credentialsProvider);
		// clientContext.setRequestConfig(RequestConfig.custom().setConnectTimeout(Timeout.ofSeconds(10))
		// .setResponseTimeout(Timeout.ofSeconds(10)).build());

		HttpRequest request = HttpRequests.GET.create("https://www.youtube.com/");
		SimpleHttpRequest copy = SimpleHttpRequest.copy(request);
		AsyncRequestProducer requestProducer = SimpleRequestProducer.create(copy);
		AsyncResponseConsumer<SimpleHttpResponse> responseConsumer = SimpleResponseConsumer.create();
		FutureCallback<SimpleHttpResponse> callback = new FutureCallback<SimpleHttpResponse>() {
			@Override
			public void completed(SimpleHttpResponse result) {
				System.out.println("COMPLETED");
			}

			@Override
			public void failed(Exception ex) {
				ex.printStackTrace(System.out);
			}

			@Override
			public void cancelled() {
				new RuntimeException("cancelled").printStackTrace(System.out);
			}
		};
		Future<SimpleHttpResponse> future = client.execute(requestProducer, responseConsumer, callback);
		try {
			System.out.println(future.get().getBody().getBodyText());
		} catch (InterruptedException | ExecutionException ex1) {
			//
		}
		client.close(CloseMode.GRACEFUL);
	}
}
