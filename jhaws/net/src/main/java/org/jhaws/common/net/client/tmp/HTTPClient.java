package org.jhaws.common.net.client.tmp;

import static java.util.stream.StreamSupport.stream;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.apache.http.client.utils.URIUtils.resolve;
import static org.apache.http.util.EntityUtils.consumeQuietly;
import static org.apache.http.util.EntityUtils.toByteArray;

import java.io.Closeable;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.ProtocolException;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.RedirectStrategy;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpOptions;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpTrace;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.apache.http.impl.client.SystemDefaultCredentialsProvider;
import org.apache.http.protocol.HttpContext;
import org.jhaws.common.io.FilePath;
import org.jhaws.common.io.security.SecureMe;
import org.jhaws.common.io.security.Security;
import org.jhaws.common.net.client.HTTPClientDefaults;
import org.jhaws.common.net.client.tmp.forms.FileInput;
import org.jhaws.common.net.client.tmp.forms.Form;

/**
 * @see https://hc.apache.org/
 * @see https://hc.apache.org/httpcomponents-client-ga/tutorial/pdf/httpclient- tutorial.pdf
 */
public class HTTPClient implements Closeable {
    protected String charSet = HTTPClientDefaults.CHARSET;

    protected transient CloseableHttpClient httpClient;

    protected String userAgent = HTTPClientDefaults.CHROME;

    protected String user;

    protected transient byte[] pass;

    protected transient Security security;

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    protected CloseableHttpClient getHttpClient() {
        if (httpClient == null) {
            httpClient = HttpClientBuilder.create()//
                    .setUserAgent(getUserAgent())//
                    .setRedirectStrategy(getRedirectStrategy())//
                    .setDefaultRequestConfig(//
                            RequestConfig.custom()//
                                    .setContentCompressionEnabled(true)//
                                    .setMaxRedirects(5)//
                                    .setCircularRedirectsAllowed(false)//
                                    .setConnectionRequestTimeout(HTTPClientDefaults.TIMEOUT)//
                                    .setConnectTimeout(HTTPClientDefaults.TIMEOUT)//
                                    .setExpectContinueEnabled(HTTPClientDefaults.EXPECT_CONTINUE)//
                                    .setRedirectsEnabled(true)//
                                    .setCookieSpec(HTTPClientDefaults.BROWSER_COMPATIBILITY)//
                                    .build())//
                    .build();//
        }
        return httpClient;
    }

    protected RedirectStrategy getRedirectStrategy() {
        return new LaxRedirectStrategy() {
            @Override
            public HttpUriRequest getRedirect(HttpRequest request, HttpResponse response, HttpContext context) throws ProtocolException {
                HttpUriRequest redirect = super.getRedirect(request, response, context);
                // HTTPClient.this.chain.add(redirect.getURI());
                // HTTPClient.this.domain = redirect.getURI().getHost();
                System.out.println("redirect: " + redirect.getURI());
                return redirect;
            }

            @Override
            public boolean isRedirected(HttpRequest request, HttpResponse response, HttpContext context) throws ProtocolException {
                if (response == null) {
                    throw new IllegalArgumentException("HTTP response may not be null");
                }

                int statusCode = response.getStatusLine().getStatusCode();
                String method = request.getRequestLine().getMethod();
                Header locationHeader = response.getFirstHeader("location");

                switch (statusCode) {
                    case HttpStatus.SC_MOVED_TEMPORARILY:
                        return (method.equalsIgnoreCase(HttpGet.METHOD_NAME) || method.equalsIgnoreCase(HttpPost.METHOD_NAME)
                                || method.equalsIgnoreCase(HttpHead.METHOD_NAME)) && (locationHeader != null);
                    case HttpStatus.SC_MOVED_PERMANENTLY:
                    case HttpStatus.SC_TEMPORARY_REDIRECT:
                        return method.equalsIgnoreCase(HttpGet.METHOD_NAME) || method.equalsIgnoreCase(HttpPost.METHOD_NAME)
                                || method.equalsIgnoreCase(HttpHead.METHOD_NAME);
                    case HttpStatus.SC_SEE_OTHER:
                        return true;
                    default:
                        return false;
                } // end of switch
            }
        };
    }

    protected URI toFullUri(GetParams get) {
        URIBuilder uriBuilder = new URIBuilder(get.getUri());
        stream(get.formValues.entrySet().spliterator(), false).forEach(e -> e.getValue().forEach(i -> uriBuilder.addParameter(e.getKey(), i)));
        try {
            return uriBuilder.build();
        } catch (URISyntaxException e1) {
            throw new RuntimeException(e1);
        }
    }

    @SuppressWarnings("deprecation")
    protected Response execute(HttpUriRequest req) {
        req.getParams().setParameter(HTTPClientDefaults.PARAM_SINGLE_COOKIE_HEADER, HTTPClientDefaults.SINGLE_COOKIE_HEADER);

        HttpClientContext context = HttpClientContext.create();
        context.setCredentialsProvider(new SystemDefaultCredentialsProvider());
        Response response = null;
        try (CloseableHttpResponse httpResponse = getHttpClient().execute(req, context)) {
            if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_UNAUTHORIZED && user != null && pass != null) {
                CredentialsProvider credsProvider = new BasicCredentialsProvider();
                credsProvider.setCredentials(new AuthScope(req.getURI().getHost(), AuthScope.ANY_PORT),
                        new UsernamePasswordCredentials(user, getPass()));
                context.setCredentialsProvider(credsProvider);
            } else {
                response = buildResponse(req, httpResponse);
            }
            consumeQuietly(httpResponse.getEntity());
        } catch (IOException ioex) {
            throw new UncheckedIOException(ioex);
        }
        if (response == null) {
            try (CloseableHttpResponse httpResponse = getHttpClient().execute(req, context)) {
                response = buildResponse(req, httpResponse);
                consumeQuietly(httpResponse.getEntity());
            } catch (IOException ioex) {
                throw new UncheckedIOException(ioex);
            }
        }
        return response;
    }

    protected Response buildResponse(HttpUriRequest req, CloseableHttpResponse httpResponse) throws IOException {
        Response response = new Response();
        response.setUri(req.getURI());
        for (Header header : httpResponse.getAllHeaders()) {
            response.addHeader(header.getName(), header.getValue());
        }
        response.setStatusCode(httpResponse.getStatusLine().getStatusCode());
        response.setLocale(httpResponse.getLocale());
        switch (httpResponse.getStatusLine().getStatusCode()) {
            case HttpStatus.SC_OK:// 200
                break;
            case HttpStatus.SC_NOT_FOUND: // 404
                break;
            case HttpStatus.SC_UNAUTHORIZED: // 401
                break;
        }
        HttpEntity entity = httpResponse.getEntity();
        if (entity != null) {
            response.setContent(toByteArray(entity));
            response.setContentLength(entity.getContentLength());
            response.setContentEncoding(entity.getContentEncoding() == null ? null : entity.getContentEncoding().getValue());
            response.setContentType(entity.getContentType() == null ? null : entity.getContentType().getValue());
        }
        return response;
    }

    public Response get(GetParams get) {
        HttpGet req = new HttpGet(toFullUri(get));
        return execute(req);
    }

    public Response delete(DeleteParams delete) {
        HttpDelete req = new HttpDelete(toFullUri(delete));
        return execute(req);
    }

    public Response put(PutParams put) {
        HttpPut req = new HttpPut(put.getUri());

        HttpEntity body = null;

        if (put.getBody() != null) {
            body = new StringEntity(put.getBody(), ContentType.create(put.getMime(), charSet));
        }

        if (!put.getFormValues().isEmpty()) {
            if (body != null) {
                throw new IllegalArgumentException("multiple bodies");
            }

            // TODO
        }

        if (body != null)
            req.setEntity(body);

        return execute(req);
    }

    public Response post(PostParams post) {
        HttpUriRequest req;
        if (post.getBody() != null) {
            RequestBuilder builder = RequestBuilder.post().setUri(post.getUri());
            builder.setEntity(new StringEntity(post.getBody(), ContentType.create(post.getMime(), charSet)));
            req = builder.build();
        } else if (post.getAttachments().size() > 0) {
            MultipartEntityBuilder mb = MultipartEntityBuilder.create();
            mb.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
            post.getAttachments().entrySet().forEach(entry -> mb.addBinaryBody(entry.getKey(), entry.getValue().toFile()));
            post.getFormValues().entrySet().forEach(entry -> entry.getValue().stream().forEach(element -> mb.addTextBody(entry.getKey(), element)));
            req = new HttpPost(post.getUri());
            HttpEntity body = mb.build();
            HttpPost.class.cast(req).setEntity(body);
        } else {
            RequestBuilder builder = RequestBuilder.post().setUri(post.getUri());
            post.getFormValues().entrySet().forEach(entry -> entry.getValue().forEach(element -> builder.addParameter(entry.getKey(), element)));
            req = builder.build();
        }
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

    protected String getPass() {
        try {
            return getSecurity().decrypt(this.pass);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public HTTPClient resetAuthentication() {
        this.user = null;
        this.pass = null;
        this.security = null;
        return this;
    }

    public void setAuthentication(String user, String pass) {
        this.user = user;
        try {
            this.pass = getSecurity().encrypt(pass);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected Security getSecurity() {
        if (security == null) {
            try {
                security = Security.class.cast(Class.forName("org.jhaws.common.io.security.SecureMeBC").newInstance());
            } catch (Exception ignore) {
                security = new SecureMe();
            }
        }
        return security;
    }

    public Response submit(Form form) {
        return HTTPClientDefaults.POST.equalsIgnoreCase(form.getMethod()) ? post(form) : get(form);
    }

    public Response post(Form form) {
        URI uri = isBlank(form.getAction()) ? form.getUrl() : resolve(form.getUrl(), form.getAction());
        PostParams post = new PostParams(uri);
        form.getInputElements().stream().filter(e -> !(e instanceof FileInput)).forEach(e -> post.addFormValue(e.getName(), e.getValue()));
        form.getInputElements()
                .stream()
                .filter(e -> isNotBlank(e.getName()))
                .filter(e -> isNotBlank(e.getValue()))
                .filter(e -> e instanceof FileInput)
                .map(e -> FileInput.class.cast(e))
                .forEach(e -> post.getAttachments().put(e.getName(), new FilePath(e.getFile())));
        post.setName(form.getId());
        return post(post);
    }

    public Response get(Form form) {
        URI uri = isBlank(form.getAction()) ? form.getUrl() : resolve(form.getUrl(), form.getAction());
        GetParams get = new GetParams(uri);
        form.getInputElements().forEach(e -> get.addFormValue(e.getName(), e.getValue()));
        return get(get);
    }

    @Override
    public void close() {
        if(httpClient!=null) {
            try {
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
