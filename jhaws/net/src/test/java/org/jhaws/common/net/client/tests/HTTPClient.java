package org.jhaws.common.net.client.tests;

import java.io.IOException;
import java.io.Serializable;
import java.io.UncheckedIOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpOptions;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.methods.HttpTrace;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.jhaws.common.net.client.HTTPClientDefaults;

/**
 * @see https://hc.apache.org/
 * @see https://hc.apache.org/httpcomponents-client-ga/tutorial/pdf/httpclient-tutorial.pdf
 */
public class HTTPClient {
    protected HttpClient httpClient;

    protected String userAgent = HTTPClientDefaults.CHROME;

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    protected HttpClient getHttpClient() {
        if (httpClient == null) {
            HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
            httpClientBuilder.setUserAgent(userAgent);
            httpClient = httpClientBuilder.build();
        }
        return httpClient;
    }

    protected Response execute(HttpRequestBase req) {
        try {
            HttpResponse httpResponse = getHttpClient().execute(req);
            Response response = new Response();
            for (Header header : httpResponse.getAllHeaders()) {
                response.addHeader(header.getName(), header.getValue());
            }
            response.statusCode = httpResponse.getStatusLine().getStatusCode();
            if (httpResponse.getEntity() != null)
                response.response = EntityUtils.toByteArray(httpResponse.getEntity());
            EntityUtils.consumeQuietly(httpResponse.getEntity());
            switch (response.statusCode) {
                case HttpStatus.SC_OK:
                    break;
            }
            return response;
        } catch (IOException ioex) {
            throw new UncheckedIOException(ioex);
        }
    }

    protected void addBody(HttpEntityEnclosingRequestBase req, String body, String mime, String charSet) {
        if (body != null) {
            req.setEntity(new StringEntity(body, ContentType.create(mime, charSet)));
        }
    }

    public Response get(URI uri) {
        HttpGet req = new HttpGet(uri);
        return execute(req);
    }

    public Response delete(URI uri) {
        HttpDelete req = new HttpDelete(uri);
        return execute(req);
    }

    public Response put(URI uri, String body, String mime, String charSet) {
        HttpPut req = new HttpPut(uri);
        addBody(req, body, mime, charSet);
        return execute(req);
    }

    public Response post(URI uri, String body, String mime, String charSet) {
        HttpPost req = new HttpPost(uri);
        addBody(req, body, mime, charSet);
        return execute(req);
    }

    public Response head(URI uri) {
        HttpHead req = new HttpHead(uri);
        return execute(req);
    }

    public Response options(URI uri) {
        HttpOptions req = new HttpOptions(uri);
        return execute(req);
    }

    public Response trace(URI uri) {
        HttpTrace req = new HttpTrace(uri);
        return execute(req);
    }

    public static class Response implements Serializable {
        private static final long serialVersionUID = 1806430557697629499L;

        private int statusCode;

        private byte[] response;

        private final Map<String, List<Object>> headers = new HashMap<>();

        protected void addHeader(String key, Object value) {
            List<Object> list = headers.get(key);
            if (list == null) {
                list = new ArrayList<>();
                headers.put(key, list);
            }
            list.add(value);
        }

        public Map<String, List<Object>> getHeaders() {
            return Collections.unmodifiableMap(headers);
        }

        public int getStatusCode() {
            return statusCode;
        }

        public void setStatusCode(int statusCode) {
            this.statusCode = statusCode;
        }

        public byte[] getResponse() {
            return response;
        }

        public void setResponse(byte[] response) {
            this.response = response;
        }

        @Override
        public String toString() {
            return statusCode + "," + (response != null) + "," + headers;
        }
    }
}
