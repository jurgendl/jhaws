package org.jhaws.common.net.client5;

import java.util.Arrays;
import java.util.concurrent.Future;

import org.apache.hc.client5.http.async.methods.SimpleHttpRequest;
import org.apache.hc.client5.http.async.methods.SimpleHttpResponse;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.async.CloseableHttpAsyncClient;
import org.apache.hc.client5.http.impl.async.HttpAsyncClients;
import org.apache.hc.client5.http.impl.nio.PoolingAsyncClientConnectionManager;
import org.apache.hc.client5.http.nio.AsyncClientConnectionManager;
import org.apache.hc.client5.http.ssl.DefaultClientTlsStrategy;
import org.apache.hc.core5.concurrent.FutureCallback;
import org.apache.hc.core5.http.HttpResponse;
import org.apache.hc.core5.http.config.RegistryBuilder;
import org.apache.hc.core5.http.nio.ssl.TlsStrategy;
import org.apache.hc.core5.pool.PoolConcurrencyPolicy;
import org.apache.hc.core5.pool.PoolReusePolicy;
import org.apache.hc.core5.util.TimeValue;

public class Http2Other2Test {
    public static void main(String[] args) {
        try {
            AsyncClientConnectionManager connManager = new PoolingAsyncClientConnectionManager(RegistryBuilder.<TlsStrategy> create()//
                    .register("https", DefaultClientTlsStrategy.getDefault())//
                    .build()//
                    , PoolConcurrencyPolicy.STRICT//
                    , PoolReusePolicy.LIFO//
                    , TimeValue.NEG_ONE_MILLISECOND//
                    , null//
                    , null//
            );
            CloseableHttpAsyncClient client = HttpAsyncClients.custom().setConnectionManager(connManager).build();
            client.start();
            FutureCallback<SimpleHttpResponse> callback = new FutureCallback<SimpleHttpResponse>() {
                @Override
                public void failed(Exception ex) {
                    ex.printStackTrace(System.out);
                }

                @Override
                public void completed(SimpleHttpResponse result) {
                    System.out.println(result);
                    System.out.println(result.getBodyText());
                }

                @Override
                public void cancelled() {
                    System.out.println("cancelled");
                }
            };
            SimpleHttpRequest request = SimpleHttpRequest.copy(new HttpGet("http://www.google.com"));
            Future<SimpleHttpResponse> future = client.execute(request, callback);
            HttpResponse response = future.get();
            client.close();
            System.out.println(response.getCode());
            System.out.println(response.getReasonPhrase());
            System.out.println(response.getVersion());
            System.out.println(Arrays.asList(response.getHeaders()));
        } catch (Exception ex) {
            ex.printStackTrace(System.out);
        }
    }
}
