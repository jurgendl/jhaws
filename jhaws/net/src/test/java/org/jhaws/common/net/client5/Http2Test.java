package org.jhaws.common.net.client5;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.stream.Stream;

import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLException;

import org.apache.hc.core5.http.HttpHost;
import org.apache.hc.core5.http.Methods;
import org.apache.hc.core5.http.impl.bootstrap.HttpAsyncRequester;
import org.apache.hc.core5.http.nio.AsyncClientEndpoint;
import org.apache.hc.core5.http.nio.entity.BasicAsyncEntityConsumer;
import org.apache.hc.core5.http.nio.entity.StringAsyncEntityConsumer;
import org.apache.hc.core5.http.nio.support.BasicRequestProducer;
import org.apache.hc.core5.http.nio.support.BasicResponseConsumer;
import org.apache.hc.core5.http2.HttpVersionPolicy;
import org.apache.hc.core5.http2.config.H2Config;
import org.apache.hc.core5.http2.impl.nio.bootstrap.H2RequesterBootstrap;
import org.apache.hc.core5.http2.ssl.H2ClientTlsStrategy;
import org.apache.hc.core5.io.CloseMode;
import org.apache.hc.core5.net.NamedEndpoint;
import org.apache.hc.core5.reactor.IOReactorConfig;
import org.apache.hc.core5.reactor.ssl.SSLSessionVerifier;
import org.apache.hc.core5.reactor.ssl.TlsDetails;
import org.apache.hc.core5.ssl.SSLContexts;
import org.apache.hc.core5.util.Timeout;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class Http2Test {
	public static void main(String[] args) throws Exception {
		siteDownload("www.youtube.com", HttpVersionPolicy.FORCE_HTTP_2);
		siteDownload("www.youtube.com", HttpVersionPolicy.FORCE_HTTP_1);
	}

	protected static void siteDownload(String site, HttpVersionPolicy httpVersionPolicy)
			throws InterruptedException, ExecutionException {
		IOReactorConfig ioReactorConfig = IOReactorConfig.custom()//
				.setSoTimeout(5, TimeUnit.SECONDS)//
				// .setTcpNoDelay(true)//
				.build();

		H2Config h2Config = H2Config.custom()//
				.setPushEnabled(false)//
				.setMaxConcurrentStreams(50)//
				.build();

		HttpAsyncRequester requester = H2RequesterBootstrap//
				.bootstrap()//
				.setIOReactorConfig(ioReactorConfig)//
				.setVersionPolicy(httpVersionPolicy)//
				.setH2Config(h2Config)//
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
				}))
				// .setStreamListener(new H2StreamListener() {
				// @Override
				// public void onHeaderInput(HttpConnection connection, int
				// streamId, List<? extends Header> headers) {
				// for (int i = 0; i < headers.size(); i++) {
				// System.out.println(
				// connection.getRemoteAddress() + " (" + streamId + ") << " +
				// headers.get(i));
				// }
				// }
				//
				// @Override
				// public void onHeaderOutput(HttpConnection connection, int
				// streamId,
				// List<? extends Header> headers) {
				// for (int i = 0; i < headers.size(); i++) {
				// System.out.println(
				// connection.getRemoteAddress() + " (" + streamId + ") >> " +
				// headers.get(i));
				// }
				// }
				//
				// @Override
				// public void onFrameInput(HttpConnection connection, int
				// streamId, RawFrame frame) {
				// }
				//
				// @Override
				// public void onFrameOutput(HttpConnection connection, int
				// streamId, RawFrame frame) {
				// }
				//
				// @Override
				// public void onInputFlowControl(HttpConnection connection, int
				// streamId, int delta, int actualSize) {
				// }
				//
				// @Override
				// public void onOutputFlowControl(HttpConnection connection,
				// int streamId, int delta,
				// int actualSize) {
				// }
				// })
				.create();

		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				System.out.println("HTTP requester shutting down");
				requester.close(CloseMode.GRACEFUL);
			}
		});
		requester.start();

		HttpHost target = new HttpHost("https", site, 443);

		String body;
		{
			CountDownLatch latch = new CountDownLatch(1);
			Future<AsyncClientEndpoint> future = requester.connect(target, Timeout.ofSeconds(10));
			AsyncClientEndpoint clientEndpoint = future.get();
			String requestUri = "/";
			HttpResponseCallback<String> callback = new HttpResponseCallback<>(latch, clientEndpoint, requestUri);
			clientEndpoint.execute(//
					new BasicRequestProducer(Methods.GET, target, requestUri)//
					, new BasicResponseConsumer<>(new StringAsyncEntityConsumer())//
					, callback//
			);
			latch.await();
			body = callback.body;
		}

		Set<String> requestUris = new LinkedHashSet<>();

		Consumer<Stream<String>> ssv = ss -> ss//
				.filter(href -> href != null)//
				.filter(href -> !href.startsWith("//"))//
				.filter(href -> href.startsWith("/") || href.startsWith("https://" + site))//
				.map(href -> href.replaceFirst("https://" + site, ""))//
				.filter(href -> !href.equals("/"))//
				.forEach(href -> requestUris.add(href));
		Document parsed = Jsoup.parse(body);
		ssv.accept(parsed.select("link").stream().map(el -> el.attr("href")));
		ssv.accept(parsed.select("img").stream().map(el -> el.attr("src")));
		requestUris.forEach(System.out::println);

		{
			CountDownLatch latch = new CountDownLatch(requestUris.size());
			for (String requestUri : requestUris) {
				Future<AsyncClientEndpoint> future = requester.connect(target, Timeout.ofSeconds(10));
				AsyncClientEndpoint clientEndpoint = future.get();
				HttpResponseCallback<byte[]> callback = new HttpResponseCallback<>(latch, clientEndpoint, requestUri);
				clientEndpoint.execute(//
						new BasicRequestProducer(Methods.GET, target, requestUri)//
						, new BasicResponseConsumer<>(new BasicAsyncEntityConsumer())//
						, callback//
				);
			}
			latch.await();
		}
		System.out.println("Shutting down I/O reactor");
		requester.initiateShutdown();
	}
}
