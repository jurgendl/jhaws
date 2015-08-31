package org.jhaws.common.net.client.tests;

import java.io.IOException;
import java.io.Serializable;
import java.io.UncheckedIOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.StreamSupport;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
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
import org.apache.http.entity.mime.FormBodyPart;
import org.apache.http.entity.mime.FormBodyPartBuilder;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.jhaws.common.net.client.HTTPClientDefaults;

/**
 * @see https://hc.apache.org/
 * @see https://hc.apache.org/httpcomponents-client-ga/tutorial/pdf/httpclient-tutorial.pdf
 */
public class HTTPClient {
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

    public static class GetParams implements Serializable {
        private static final long serialVersionUID = 4305650301682256528L;

        protected URI uri;

        public GetParams() {
            super();
        }

        public GetParams(URI uri) {
            setUri(uri);
        }

        public GetParams(String uri) {
            setUri(uri);
        }

        public URI getUri() {
            return uri;
        }

        public void setUri(URI uri) {
            this.uri = uri;
        }

        public void setUri(String uri) {
            this.uri = URI.create(uri);
        }
    }

    public static class DeleteParams extends GetParams {
        private static final long serialVersionUID = 7789744009919283043L;

        public DeleteParams() {
            super();
        }

        public DeleteParams(URI uri) {
            super(uri);
        }

        public DeleteParams(String uri) {
            super(uri);
        }
    }

    public static class HeadParams extends GetParams {
        private static final long serialVersionUID = -6947796698944347163L;

        public HeadParams() {
            super();
        }

        public HeadParams(URI uri) {
            super(uri);
        }

        public HeadParams(String uri) {
            super(uri);
        }
    }

    public static class OptionsParams extends GetParams {
        private static final long serialVersionUID = -3335823798615631641L;

        public OptionsParams() {
            super();
        }

        public OptionsParams(URI uri) {
            super(uri);
        }

        public OptionsParams(String uri) {
            super(uri);
        }
    }

    public static class TraceParams extends GetParams {
        private static final long serialVersionUID = -113043755384527216L;

        public TraceParams() {
            super();
        }

        public TraceParams(URI uri) {
            super(uri);
        }

        public TraceParams(String uri) {
            super(uri);
        }
    }

    public static class PutParams extends GetParams {
        private static final long serialVersionUID = -6103040334975043729L;

        protected String body;

        protected String mime;

        protected String charSet = "UTF-8";

        protected Map<String, List<String>> formValues = new HashMap<String, List<String>>();

        public PutParams() {
            super();
        }

        public PutParams(URI uri) {
            super(uri);
        }

        public PutParams(String uri) {
            super(uri);
        }

        public PutParams(String uri, String body, String mime) {
            super(uri);
            this.body = body;
            this.mime = mime;
        }

        public PutParams(URI uri, String body, String mime) {
            super(uri);
            this.body = body;
            this.mime = mime;
        }

        public String getBody() {
            return body;
        }

        public void setBody(String body) {
            this.body = body;
        }

        public String getMime() {
            return mime;
        }

        public void setMime(String mime) {
            this.mime = mime;
        }

        public String getCharSet() {
            return charSet;
        }

        public void setCharSet(String charSet) {
            this.charSet = charSet;
        }

        public Map<String, List<String>> getFormValues() {
            return formValues;
        }

        public void setFormValues(Map<String, List<String>> formValues) {
            this.formValues = formValues;
        }
    }

    public static class PostParams extends PutParams {
        private static final long serialVersionUID = -3939699621850878105L;

        private HashMap<String, Path> attachments = new HashMap<String, Path>();

        public PostParams() {
            super();
        }

        public PostParams(URI uri) {
            super(uri);
        }

        public PostParams(String uri) {
            super(uri);
        }

        public PostParams(String uri, String body, String mime) {
            super(uri, body, mime);
        }

        public PostParams(URI uri, String body, String mime) {
            super(uri, body, mime);
        }

        public HashMap<String, Path> getAttachments() {
            return attachments;
        }

        public void setAttachments(HashMap<String, Path> attachments) {
            this.attachments = attachments;
        }
    }

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

    public Response get(GetParams get) {
        HttpGet req = new HttpGet(get.getUri());
        return execute(req);
    }

    public Response delete(DeleteParams delete) {
        HttpDelete req = new HttpDelete(delete.getUri());
        return execute(req);
    }

    public Response put(PutParams put) {
        HttpPut req = new HttpPut(put.getUri());
        addBody(req, put.getBody(), put.getMime(), put.getCharSet());
        return execute(req);
    }

    public Response post(PostParams post) {
        HttpPost req = new HttpPost(post.getUri());
        addBody(req, post.getBody(), post.getMime(), post.getCharSet());
        return execute(req);
    }

    public Response head(HeadParams head) {
        HttpHead req = new HttpHead(head.getUri());
        return execute(req);
    }

    public Response options(OptionsParams options) {
        HttpOptions req = new HttpOptions(options.getUri());
        return execute(req);
    }

    public Response trace(TraceParams trace) {
        HttpTrace req = new HttpTrace(trace.getUri());
        return execute(req);
    }

    protected HttpEntity build(PostParams post) {
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        StreamSupport.stream(post.getAttachments().entrySet().spliterator(), false).forEach(e -> {
            try {
                builder.addBinaryBody(e.getValue().getFileName().toString(), Files.readAllBytes(e.getValue()));
            } catch (IOException ioex) {
                throw new UncheckedIOException(ioex);
            }
        });
        return builder.build();
    }

    protected FormBodyPart build(PutParams put) {
        FormBodyPartBuilder builder = FormBodyPartBuilder.create();
        if (put.getBody() != null) {
            put.setBody(put.getBody());
        }
        StreamSupport.stream(put.getFormValues().entrySet().spliterator(), false)
                .forEach(e -> e.getValue().stream().forEach(i -> builder.addField(e.getKey(), i)));
        return builder.build();
    }
}
