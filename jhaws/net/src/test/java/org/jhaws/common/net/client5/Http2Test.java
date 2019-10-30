package org.jhaws.common.net.client5;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Future;

import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLException;

import org.apache.hc.core5.concurrent.FutureCallback;
import org.apache.hc.core5.http.Header;
import org.apache.hc.core5.http.HttpConnection;
import org.apache.hc.core5.http.HttpHost;
import org.apache.hc.core5.http.HttpResponse;
import org.apache.hc.core5.http.Message;
import org.apache.hc.core5.http.Methods;
import org.apache.hc.core5.http.impl.bootstrap.HttpAsyncRequester;
import org.apache.hc.core5.http.nio.AsyncClientEndpoint;
import org.apache.hc.core5.http.nio.entity.BasicAsyncEntityConsumer;
import org.apache.hc.core5.http.nio.entity.StringAsyncEntityConsumer;
import org.apache.hc.core5.http.nio.support.BasicRequestProducer;
import org.apache.hc.core5.http.nio.support.BasicResponseConsumer;
import org.apache.hc.core5.http2.config.H2Config;
import org.apache.hc.core5.http2.frame.RawFrame;
import org.apache.hc.core5.http2.impl.nio.H2StreamListener;
import org.apache.hc.core5.http2.impl.nio.bootstrap.H2RequesterBootstrap;
import org.apache.hc.core5.http2.ssl.H2ClientTlsStrategy;
import org.apache.hc.core5.io.CloseMode;
import org.apache.hc.core5.net.NamedEndpoint;
import org.apache.hc.core5.reactor.ssl.SSLSessionVerifier;
import org.apache.hc.core5.reactor.ssl.TlsDetails;
import org.apache.hc.core5.ssl.SSLContexts;
import org.apache.hc.core5.util.Timeout;
import org.jhaws.common.lang.StringValue;
import org.jsoup.Jsoup;

public class Http2Test {
	public static void main(String[] args) throws Exception {
		H2Config h2Config = H2Config.custom().setPushEnabled(false).build();

		HttpAsyncRequester requester = H2RequesterBootstrap.bootstrap().setH2Config(h2Config)
				.setTlsStrategy(new H2ClientTlsStrategy(SSLContexts.createSystemDefault(), new SSLSessionVerifier() {

					@Override
					public TlsDetails verify(NamedEndpoint endpoint, SSLEngine sslEngine) throws SSLException {
						// IMPORTANT uncomment the following line when running
						// Java 9 or older
						// in order to avoid the illegal reflective access
						// operation warning
						// ====
						// return new TlsDetails(sslEngine.getSession(),
						// sslEngine.getApplicationProtocol());
						// ====
						return null;
					}

				})).setStreamListener(new H2StreamListener() {

					@Override
					public void onHeaderInput(HttpConnection connection, int streamId, List<? extends Header> headers) {
						for (int i = 0; i < headers.size(); i++) {
							System.out.println(
									connection.getRemoteAddress() + " (" + streamId + ") << " + headers.get(i));
						}
					}

					@Override
					public void onHeaderOutput(HttpConnection connection, int streamId,
							List<? extends Header> headers) {
						for (int i = 0; i < headers.size(); i++) {
							System.out.println(
									connection.getRemoteAddress() + " (" + streamId + ") >> " + headers.get(i));
						}
					}

					@Override
					public void onFrameInput(HttpConnection connection, int streamId, RawFrame frame) {
					}

					@Override
					public void onFrameOutput(HttpConnection connection, int streamId, RawFrame frame) {
					}

					@Override
					public void onInputFlowControl(HttpConnection connection, int streamId, int delta, int actualSize) {
					}

					@Override
					public void onOutputFlowControl(HttpConnection connection, int streamId, int delta,
							int actualSize) {
					}

				}).create();
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				System.out.println("HTTP requester shutting down");
				requester.close(CloseMode.GRACEFUL);
			}
		});
		requester.start();

		HttpHost target = new HttpHost("https", "www.youtube.com", 443);

		StringValue body = new StringValue();
		CountDownLatch latch1 = new CountDownLatch(1);
		{
			Future<AsyncClientEndpoint> future = requester.connect(target, Timeout.ofSeconds(5));
			AsyncClientEndpoint clientEndpoint = future.get();
			clientEndpoint.execute(new BasicRequestProducer(Methods.GET, target, "/"),
					new BasicResponseConsumer<>(new StringAsyncEntityConsumer()),
					new FutureCallback<Message<HttpResponse, String>>() {

						@Override
						public void completed(Message<HttpResponse, String> message) {
							clientEndpoint.releaseAndReuse();
							body.set(message.getBody());
							latch1.countDown();
						}

						@Override
						public void failed(Exception ex) {
							latch1.countDown();
						}

						@Override
						public void cancelled() {
							latch1.countDown();
						}
					});
		}
		latch1.await();

		Set<String> requestUris = new HashSet<>();

		Jsoup.parse(body.get()).select("link").forEach(el -> {
			String href = el.attr("href");
			if (href != null && !href.startsWith("//")
					&& (href.startsWith("/") || href.startsWith("https://www.youtube.com"))) {
				if (href.startsWith("https://www.youtube.com")) {
					href = href.substring("https://www.youtube.com".length());
				}
				if (!href.equals("/"))
					requestUris.add(href);
			}
		});

		Jsoup.parse(body.get()).select("img").forEach(el -> {
			String href = el.attr("src");
			if (href != null && !href.startsWith("//")
					&& (href.startsWith("/") || href.startsWith("https://www.youtube.com"))) {
				if (href.startsWith("https://www.youtube.com")) {
					href = href.substring("https://www.youtube.com".length());
				}
				if (!href.equals("/"))
					requestUris.add(href);
			}
		});

		requestUris.forEach(System.out::println);

		CountDownLatch latch = new CountDownLatch(requestUris.size());
		for (String requestUri : requestUris) {
			Future<AsyncClientEndpoint> future = requester.connect(target, Timeout.ofSeconds(5));
			AsyncClientEndpoint clientEndpoint = future.get();
			clientEndpoint.execute(new BasicRequestProducer(Methods.GET, target, requestUri),
					new BasicResponseConsumer<>(new BasicAsyncEntityConsumer()),
					new FutureCallback<Message<HttpResponse, byte[]>>() {

						@Override
						public void completed(Message<HttpResponse, byte[]> message) {
							clientEndpoint.releaseAndReuse();
							HttpResponse response = message.getHead();
							byte[] body = message.getBody();
							System.out.println(requestUri + "->" + response.getCode() + " " + response.getVersion());
							// System.out.println(body);
							latch.countDown();
						}

						@Override
						public void failed(Exception ex) {
							clientEndpoint.releaseAndDiscard();
							System.out.println(requestUri + "->" + ex);
							latch.countDown();
						}

						@Override
						public void cancelled() {
							clientEndpoint.releaseAndDiscard();
							System.out.println(requestUri + " cancelled");
							latch.countDown();
						}

					});
		}

		latch.await();
		System.out.println("Shutting down I/O reactor");
		requester.initiateShutdown();
	}
}
