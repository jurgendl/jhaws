package org.jhaws.common.net.client5;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.apache.hc.client5.http.auth.CredentialsProvider;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.cookie.BasicCookieStore;
import org.apache.hc.client5.http.cookie.CookieStore;
import org.apache.hc.client5.http.impl.async.CloseableHttpAsyncClient;
import org.apache.hc.client5.http.impl.async.HttpAsyncClients;
import org.apache.hc.client5.http.impl.auth.BasicCredentialsProvider;
import org.apache.hc.client5.http.protocol.HttpClientContext;
import org.apache.hc.client5.http.ssl.ClientTlsStrategyBuilder;
import org.apache.hc.core5.concurrent.FutureCallback;
import org.apache.hc.core5.http.HttpRequest;
import org.apache.hc.core5.http.HttpResponse;
import org.apache.hc.core5.http.Message;
import org.apache.hc.core5.http.NameValuePair;
import org.apache.hc.core5.http.message.BasicNameValuePair;
import org.apache.hc.core5.http.ssl.TLS;
import org.apache.hc.core5.io.CloseMode;
import org.apache.hc.core5.reactor.IOReactorConfig;
import org.apache.hc.core5.ssl.SSLContexts;
import org.apache.hc.core5.util.Timeout;
import org.apache.http.client.config.CookieSpecs;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ok2c.hc5.json.http.JsonRequestProducers;
import com.ok2c.hc5.json.http.JsonResponseConsumers;

// https://ok2c.github.io/httpclient-migration-guide/migration-to-async-http2.html
public class Http2Client {
    public static void main(String[] args) {
        CloseableHttpAsyncClient client = HttpAsyncClients.customHttp2()
                .setTlsStrategy(ClientTlsStrategyBuilder.create()
                        .setSslContext(SSLContexts.createSystemDefault())
                        .setTlsVersions(TLS.V_1_3, TLS.V_1_2)
                        .build())
                .setIOReactorConfig(IOReactorConfig.custom().setSoTimeout(Timeout.ofSeconds(5)).build())
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setConnectTimeout(Timeout.ofSeconds(5))
                        .setResponseTimeout(Timeout.ofSeconds(5))
                        .setCookieSpec(CookieSpecs.STANDARD_STRICT)
                        .build())
                .build();
        client.start();

        CookieStore cookieStore = new BasicCookieStore();

        CredentialsProvider credentialsProvider = new BasicCredentialsProvider();

        HttpClientContext clientContext = HttpClientContext.create();
        clientContext.setCookieStore(cookieStore);
        clientContext.setCredentialsProvider(credentialsProvider);
        clientContext
                .setRequestConfig(RequestConfig.custom().setConnectTimeout(Timeout.ofSeconds(10)).setResponseTimeout(Timeout.ofSeconds(10)).build());

        JsonFactory jsonFactory = new JsonFactory();
        ObjectMapper objectMapper = new ObjectMapper(jsonFactory);

        HttpRequest httpPost = new HttpPost("https://nghttp2.org/httpbin/post");

        List<NameValuePair> requestData = Arrays.asList(new BasicNameValuePair("name1", "value1"), new BasicNameValuePair("name2", "value2"));

        Future<?> future = client.execute(JsonRequestProducers.create(httpPost, requestData, objectMapper), JsonResponseConsumers.create(jsonFactory),
                new FutureCallback<Message<HttpResponse, JsonNode>>() {

                    @Override
                    public void completed(Message<HttpResponse, JsonNode> message) {
                        System.out.println(message.getBody());
                    }

                    @Override
                    public void failed(Exception ex) {
                        System.out.println("Error executing HTTP request: " + ex.getMessage());
                    }

                    @Override
                    public void cancelled() {
                        System.out.println("HTTP request execution cancelled");
                    }

                });

        try {
            future.get();
        } catch (InterruptedException | ExecutionException ex1) {
            //
        }
        client.close(CloseMode.GRACEFUL);
    }
}
