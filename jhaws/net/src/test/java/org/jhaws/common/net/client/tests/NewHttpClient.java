package org.jhaws.common.net.client.tests;

import java.io.IOException;
import java.net.URI;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpOptions;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.methods.HttpTrace;
import org.apache.http.impl.client.HttpClientBuilder;

public class NewHttpClient {
    protected HttpClient httpClient;

    public HttpClient getHttpClient() {
        if (httpClient == null) {
            HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
            httpClient = httpClientBuilder.build();
        }
        return httpClient;
    }

    public void setHttpClient(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    protected HttpResponse execute(HttpRequestBase req) throws IOException {
        return getHttpClient().execute(req);
    }

    public HttpResponse get(URI uri) throws IOException {
        return execute(new HttpGet(uri));
    }

    public HttpResponse delete(URI uri) throws IOException {
        return execute(new HttpDelete(uri));
    }

    public HttpResponse put(URI uri) throws IOException {
        return execute(new HttpPut(uri));
    }

    public HttpResponse post(URI uri) throws IOException {
        return execute(new HttpPost(uri));
    }

    public HttpResponse head(URI uri) throws IOException {
        return execute(new HttpHead(uri));
    }

    public HttpResponse options(URI uri) throws IOException {
        return execute(new HttpOptions(uri));
    }

    public HttpResponse trace(URI uri) throws IOException {
        return execute(new HttpTrace(uri));
    }
}
