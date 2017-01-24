package org.jhaws.common.net.client;

import static java.util.stream.StreamSupport.stream;
import static org.apache.commons.io.IOUtils.copyLarge;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.apache.commons.lang3.time.DateUtils.parseDate;
import static org.apache.http.client.utils.URIUtils.resolve;
import static org.apache.http.util.EntityUtils.consumeQuietly;
import static org.apache.http.util.EntityUtils.toByteArray;

import java.io.Closeable;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UncheckedIOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PreDestroy;

import org.apache.commons.io.output.CountingOutputStream;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.ProtocolException;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.AuthCache;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.RedirectStrategy;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.config.RequestConfig.Builder;
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
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.apache.http.impl.client.SystemDefaultCredentialsProvider;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.protocol.HttpContext;
import org.jhaws.common.io.FilePath;
import org.jhaws.common.lang.StringUtils;
import org.jhaws.common.net.NetHelper;

/**
 * http test server: http://httpbin.org/
 * 
 * @see http://httpbin.org/
 * @see http://www.baeldung.com/httpclient-4-basic-authentication
 * @see https://hc.apache.org/httpcomponents-client-ga/httpclient/examples/org/-apache/http/examples/client/
 * @see http://stackoverflow.com/questions/10146692/how-do-i-write-to-an--outputstream-using-defaulthttpclient
 * @see https://hc.apache.org/
 * @see https://hc.apache.org/httpcomponents-client-ga/tutorial/pdf/httpclient--tutorial.pdf
 * @see https://hc.apache.org/httpcomponents-client-4.5.x/tutorial/html/advanced-.html
 */
public class HTTPClient implements Closeable {
    /**
     * Mozilla/5.0 (Windows NT 6.1; WOW64; rv:23.0) Gecko/20100101 Firefox/23.0
     */
    public static final String FIREFOX = "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:23.0) Gecko/20100101 Firefox/23.0";

    /**
     * Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36
     */
    public static final String CHROME = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36";

    /**
     * Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; WOW64; Trident/5.0; SLCC2; .NET CLR 2.0.50727; .NET CLR 3.5.30729; .NET CLR 3.0.30729; Media
     * Center PC 6.0; .NET4.0C; .NET4.0E)
     */
    public static final String IEXPLORER = "Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; WOW64; Trident/5.0; SLCC2; .NET CLR 2.0.50727; .NET CLR 3.5.30729; .NET CLR 3.0.30729; Media Center PC 6.0; .NET4.0C; .NET4.0E)";

    /**
     * Mozilla/5.0 (Linux; U; Android 4.0.4; en-gb; GT-I9300 Build/IMM76D) AppleWebKit/534.30 (KHTML, like Gecko) Version/4.0 Mobile Safari/534.30
     */
    public static final String ANDROID_S3 = "Mozilla/5.0 (Linux; U; Android 4.0.4; en-gb; GT-I9300 Build/IMM76D) AppleWebKit/534.30 (KHTML, like Gecko) Version/4.0 Mobile Safari/534.30";

    protected String charSet = StringUtils.UTF8;

    protected transient CloseableHttpClient httpClient;

    // protected transient
    // org.apache.http.impl.nio.client.CloseableHttpAsyncClient asyncHttpclient;

    protected transient RequestConfig requestConfig;

    protected transient RedirectStrategy redirectStrategy;

    private transient List<HTTPClientAuth> authentication = new ArrayList<>();

    protected String userAgent = CHROME;

    protected org.apache.http.client.CookieStore cookieStore;

    protected HttpClientConnectionManager connectionManager;

    protected transient long downloaded;

    protected String acceptLanguage = "en, en-gb;q=0.9";

    private final boolean compressed;

    private transient CredentialsProvider defaultCredentialsProvider;

    private transient CredentialsProvider preemptiveCredentialsProvider;

    public HTTPClient() {
        this(true);
    }

    public HTTPClient(boolean compressed) {
        this.compressed = compressed;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    @SuppressWarnings("deprecation")
    public RequestConfig getRequestConfig() {
        if (requestConfig == null) {
            Builder requestConfigBuilder = RequestConfig.custom()//
                    .setMaxRedirects(5)//
                    .setCircularRedirectsAllowed(false)//
                    .setConnectionRequestTimeout(5000)//
                    .setConnectTimeout(5000)//
                    .setExpectContinueEnabled(true)//
                    .setRedirectsEnabled(true)//
                    .setCookieSpec(org.apache.http.client.params.CookiePolicy.BROWSER_COMPATIBILITY);
            requestConfigBuilder = requestConfigBuilder.setContentCompressionEnabled(compressed);
            requestConfig = requestConfigBuilder.build();
        }
        return requestConfig;
    }

    private ThreadLocal<List<URI>> chain;

    public RedirectStrategy getRedirectStrategy() {
        if (redirectStrategy == null) {
            redirectStrategy = new LaxRedirectStrategy() {
                @Override
                public HttpUriRequest getRedirect(HttpRequest request, HttpResponse response, HttpContext context) throws ProtocolException {
                    HttpUriRequest redirect = super.getRedirect(request, response, context);
                    chain.get().add(redirect.getURI());
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
                    }
                }
            };
        }
        return redirectStrategy;
    }

    public CloseableHttpClient getHttpClient() {
        if (httpClient == null) {
            HttpClientBuilder httpClientBuilder = HttpClientBuilder.create()//
                    .setDefaultCookieStore(getCookieStore()) //
                    .setUserAgent(getUserAgent())//
                    .setRedirectStrategy(getRedirectStrategy())//
                    .setDefaultRequestConfig(getRequestConfig())//
                    .setConnectionManager(getConnectionManager())//
                    .setDefaultCredentialsProvider(getDefaultCredentialsProvider());
            if (!compressed) httpClientBuilder.disableContentCompression();
            httpClient = httpClientBuilder.build();//
        }
        return httpClient;
    }

    protected HttpClientConnectionManager getConnectionManager() {
        if (connectionManager == null) {
            // replaces ThreadSafeClientConnManager
            PoolingHttpClientConnectionManager poolingConnectionManager = new PoolingHttpClientConnectionManager();
            connectionManager = poolingConnectionManager;
            poolingConnectionManager.setMaxTotal(20);
            poolingConnectionManager.setDefaultMaxPerRoute(2);
        }
        return connectionManager;
    }

    // protected org.apache.http.impl.nio.client.CloseableHttpAsyncClient
    // getAsyncHttpclient() {
    // if (asyncHttpclient == null) {
    // asyncHttpclient =
    // org.apache.http.impl.nio.client.HttpAsyncClients.custom()//
    // .setUserAgent(getUserAgent())//
    // .setRedirectStrategy(getRedirectStrategy())//
    // .setDefaultRequestConfig(getRequestConfig())//
    // .build();
    // asyncHttpclient.start();
    // }
    // return asyncHttpclient;
    // }

    protected URI toFullUri(AbstractGetRequest<? extends AbstractGetRequest<?>> get) {
        URIBuilder uriBuilder = new URIBuilder(get.getUri());
        stream(get.getFormValues().entrySet().spliterator(), false).forEach(e -> e.getValue().forEach(i -> uriBuilder.addParameter(e.getKey(), i)));
        try {
            return uriBuilder.build();
        } catch (URISyntaxException e1) {
            throw new RuntimeException(e1);
        }
    }

    public Response execute(AbstractRequest<? extends AbstractRequest<?>> params, HttpUriRequest req) {
        return execute(params, req, null);
    }

    public Response execute(AbstractRequest<? extends AbstractRequest<?>> params, HttpUriRequest req, OutputStream out) {
        if (params != null) prepareRequest(params, req);

        chain = new ThreadLocal<List<URI>>() {
            @Override
            protected java.util.List<URI> initialValue() {
                return new ArrayList<>();
            };
        };

        URI uri = req.getURI();
        HttpHost targetHost = new HttpHost(uri.getHost(), uri.getPort(), uri.getScheme());
        HttpClientContext context = HttpClientContext.create();

        CredentialsProvider preemptiveCP = getPreemptiveCredentialsProvider();
        if (preemptiveCP != null) {
            context.setCredentialsProvider(preemptiveCP);
            AuthCache authCache = new BasicAuthCache();
            BasicScheme basicAuth = new BasicScheme();
            authCache.put(targetHost, basicAuth);
            context.setAuthCache(authCache);
        }

        Response response = null;
        try (CloseableHttpResponse httpResponse = getHttpClient().execute(targetHost, req, context)) {
            response = buildResponse(req, httpResponse, out);
            consumeQuietly(httpResponse.getEntity());
        } catch (IOException ioex) {
            throw new UncheckedIOException(ioex);
        }

        // if (response == null) {
        // try (CloseableHttpResponse httpResponse =
        // getHttpClient().execute(req, context)) {
        // response = buildResponse(req, httpResponse, out);
        // consumeQuietly(httpResponse.getEntity());
        // } catch (IOException ioex) {
        // throw new UncheckedIOException(ioex);
        // }
        // }

        return response;
    }

    protected Response buildResponse(HttpUriRequest req, CloseableHttpResponse httpResponse, OutputStream out) throws IOException {
        Response response = new Response();
        List<URI> uris = chain.get();
        uris.add(0, req.getURI());
        response.setChain(uris);
        response.setUri(req.getURI());
        for (Header header : httpResponse.getAllHeaders()) {
            response.addHeader(header.getName(), header.getValue());
        }
        response.setStatusCode(httpResponse.getStatusLine().getStatusCode());
        response.setStatusText(httpResponse.getStatusLine().getReasonPhrase());
        response.setLocale(httpResponse.getLocale());
        HttpEntity entity = httpResponse.getEntity();
        if (entity != null) {
            if (out != null) {
                try (CountingOutputStream cout = new CountingOutputStream(out)) {
                    copyLarge(entity.getContent(), cout);
                    downloaded += cout.getByteCount();
                }
            } else {
                byte[] byteArray = toByteArray(entity);
                downloaded += byteArray.length;
                response.setContent(byteArray);
            }
            response.setContentLength(entity.getContentLength());
            response.setContentEncoding(entity.getContentEncoding() == null ? null : entity.getContentEncoding().getValue());
            response.setContentType(entity.getContentType() == null ? null : entity.getContentType().getValue());
            response.setFilename(NetHelper.getName(uris.get(uris.size() - 1)));
        }
        response.setCharset(charSet);
        try {
            response.setDate(parseDate(httpResponse.getFirstHeader("Date").getValue()));
        } catch (ParseException ex) {
            //
        }
        return response;
    }

    public Response get(GetRequest get) {
        return execute(get, createGet(get));
    }

    public HttpGet createGet(GetRequest get) {
        return new HttpGet(toFullUri(get));
    }

    public Response delete(DeleteRequest delete) {
        return execute(delete, createDelete(delete));
    }

    public HttpDelete createDelete(DeleteRequest delete) {
        return new HttpDelete(toFullUri(delete));
    }

    public Response put(PutRequest put) {
        return execute(put, createPut(put));
    }

    public HttpPut createPut(PutRequest put) {
        HttpPut req = new HttpPut(put.getUri());

        HttpEntity body = null;

        if (put.getBody() != null) {
            body = new StringEntity(put.getBody(), ContentType.create(put.getMime(), charSet));
        }

        if (!put.getFormValues().isEmpty()) {
            if (body != null) {
                throw new IllegalArgumentException("multiple bodies");
            }
            // FIXME
        }

        if (body != null) {
            req.setEntity(body);
        }
        return req;
    }

    public Response post(PostRequest post) {
        return execute(post, createPost(post));
    }

    public HttpUriRequest createPost(PostRequest post) {
        HttpUriRequest req;
        if (post.getBody() != null) {
            RequestBuilder builder = RequestBuilder.post().setUri(post.getUri());
            builder.setEntity(new StringEntity(post.getBody(), ContentType.create(post.getMime(), charSet)));
            req = builder.build();
        } else if (post.getAttachments().size() > 0) {
            MultipartEntityBuilder mb = MultipartEntityBuilder.create();
            // STRICT, BROWSER_COMPATIBLE, RFC6532
            mb.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
            post.getAttachments().entrySet().forEach(entry -> mb.addBinaryBody(entry.getKey(), entry.getValue().toFile()));
            post.getFormValues().entrySet().forEach(entry -> entry.getValue().stream().forEach(element -> mb.addTextBody(entry.getKey(), element)));
            req = new HttpPost(post.getUri());
            HttpEntity body = mb.build();
            HttpPost.class.cast(req).setEntity(body);
        } else if (post.getStream() != null) {
            req = new HttpPost(post.getUri());
            HttpEntity body = new InputStreamEntity(post.getStream().get());
            HttpPost.class.cast(req).setEntity(body);
        } else {
            RequestBuilder builder = RequestBuilder.post().setUri(post.getUri());
            post.getFormValues().entrySet().forEach(entry -> entry.getValue().forEach(element -> builder.addParameter(entry.getKey(), element)));
            req = builder.build();
        }
        return req;
    }

    public Response head(HeadRequest head) {
        return execute(head, createHead(head));
    }

    public HttpHead createHead(HeadRequest head) {
        return new HttpHead(head.getUri());
    }

    public Response options(OptionsRequest options) {
        return execute(options, createOptions(options));
    }

    public HttpOptions createOptions(OptionsRequest options) {
        return new HttpOptions(options.getUri());
    }

    public Response trace(TraceRequest trace) {
        return execute(trace, createTrace(trace));
    }

    public HttpTrace createTrace(TraceRequest trace) {
        return new HttpTrace(trace.getUri());
    }

    public Response submit(Form form) {
        return "POST".equalsIgnoreCase(form.getMethod()) ? post(form) : get(form);
    }

    public Response post(Form form) {
        return post(createPost(form));
    }

    public PostRequest createPost(Form form) {
        URI uri = isBlank(form.getAction()) ? form.getUrl() : resolve(form.getUrl(), form.getAction());
        PostRequest post = new PostRequest(uri);
        form.getInputElements().stream().filter(e -> !(e instanceof FileInput)).forEach(e -> post.addFormValue(e.getName(), e.getValue()));
        form.getInputElements()
                .stream()
                .filter(e -> isNotBlank(e.getName()))
                .filter(e -> isNotBlank(e.getValue()))
                .filter(e -> e instanceof FileInput)
                .map(e -> FileInput.class.cast(e))
                .forEach(e -> post.addAttachment(e.getName(), new FilePath(e.getFile())));
        post.setName(form.getId());
        return post;
    }

    public Response get(String url) {
        return get(URI.create(url));
    }

    public Response get(URI url) {
        return get(new GetRequest(url));
    }

    public Response get(URL url) {
        try {
            return get(url.toURI());
        } catch (URISyntaxException ex) {
            throw new IllegalArgumentException("" + url);
        }
    }

    public Response get(Form form) {
        return get(createGet(form));
    }

    public GetRequest createGet(Form form) {
        URI uri = isBlank(form.getAction()) ? form.getUrl() : resolve(form.getUrl(), form.getAction());
        GetRequest get = new GetRequest(uri);
        form.getInputElements().forEach(e -> get.addFormValue(e.getName(), e.getValue()));
        return get;
    }

    @Override
    @PreDestroy
    public void close() {
        if (httpClient != null) {
            try {
                httpClient.close();
            } catch (Exception e) {
                //
            }
            httpClient = null;
        }
        // if (asyncHttpclient != null) {
        // try {
        // asyncHttpclient.close();
        // } catch (Exception e) {
        // //
        // }
        // asyncHttpclient = null;
        // }
        redirectStrategy = null;
        requestConfig = null;
        authentication.clear();
    }

    // public void execute(AbstractRequest<? extends AbstractRequest<?>> params,
    // HttpUriRequest req, OutputStream out) {
    // prepareRequest(params, req);
    // HttpAsyncRequestProducer areq = HttpAsyncMethods.create(req);
    // Future<OutputStream> future = getAsyncHttpclient().execute(areq, new
    // AsyncConsumer(out), null);
    // try {
    // future.get();
    // } catch (ExecutionException e) {
    // wrap(e.getCause());
    // } catch (InterruptedException e) {
    // //
    // }
    // }

    protected void wrap(Throwable cause) throws UncheckedIOException, RuntimeException {
        if (cause instanceof RuntimeException) {
            throw RuntimeException.class.cast(cause);
        }
        if (cause instanceof IOException) {
            throw new UncheckedIOException(IOException.class.cast(cause));
        }
        throw new RuntimeException(cause);
    }

    protected void prepareRequest(AbstractRequest<? extends AbstractRequest<?>> params, HttpUriRequest req) {
        // prepareRequest_prememptiveAuthentication(params, req);
        prepareRequest_singleCookieHeader(params, req);
        prepareRequest_accept(params, req);
        prepareRequest_additionalHeaders(params, req);
    }

    // protected void prepareRequest_prememptiveAuthentication(AbstractRequest<? extends AbstractRequest<?>> params, HttpUriRequest req) {
    // if (user != null && pass != null) {
    // req.setHeader("http.authentication.preemptive", String.valueOf(Boolean.TRUE));
    // }
    // }

    @SuppressWarnings("deprecation")
    protected void prepareRequest_singleCookieHeader(AbstractRequest<? extends AbstractRequest<?>> params, HttpUriRequest req) {
        req.setHeader(org.apache.http.cookie.params.CookieSpecPNames.SINGLE_COOKIE_HEADER, Boolean.TRUE.toString());
    }

    protected void prepareRequest_accept(AbstractRequest<? extends AbstractRequest<?>> params, HttpUriRequest req) {
        if (params != null && StringUtils.isNotBlank(params.getAccept())) {
            req.setHeader(HttpHeaders.ACCEPT, params.getAccept());
        }
        if (StringUtils.isNotBlank(acceptLanguage)) {
            req.setHeader(HttpHeaders.ACCEPT_LANGUAGE, acceptLanguage);
        } else {
            req.removeHeaders(HttpHeaders.ACCEPT_LANGUAGE);
        }
    }

    protected void prepareRequest_additionalHeaders(AbstractRequest<? extends AbstractRequest<?>> params, HttpUriRequest req) {
        if (params != null) {
            params.getHeaders()
                    .entrySet()
                    .stream()
                    .filter(h -> h.getValue() != null)
                    .forEach(h -> req.setHeader(h.getKey(), String.valueOf(h.getValue())));
        }
    }

    public org.apache.http.client.CookieStore getCookieStore() {
        if (cookieStore == null) {
            cookieStore = new org.jhaws.common.net.client.CookieStore();
        }
        return this.cookieStore;
    }

    public void setCookieStore(org.apache.http.client.CookieStore cookieStore) {
        this.cookieStore = cookieStore;
    }

    public long getDownloaded() {
        return downloaded;
    }

    public void setRequestConfig(RequestConfig requestConfig) {
        this.requestConfig = requestConfig;
    }

    public void setRedirectStrategy(RedirectStrategy redirectStrategy) {
        this.redirectStrategy = redirectStrategy;
    }

    public void setConnectionManager(HttpClientConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    public String getAcceptLanguage() {
        return this.acceptLanguage;
    }

    public void setAcceptLanguage(String acceptLanguage) {
        this.acceptLanguage = acceptLanguage;
    }

    public void addAuthentication(HTTPClientAuth httpClientAuth) {
        authentication.add(httpClientAuth);
    }

    public HTTPClient resetAuthentication() {
        authentication.clear();
        return this;
    }

    protected CredentialsProvider getDefaultCredentialsProvider() {
        if (defaultCredentialsProvider == null && !authentication.isEmpty()) {
            boolean init = false;
            defaultCredentialsProvider = new BasicCredentialsProvider();
            for (HTTPClientAuth auth : authentication) {
                if (auth.isPreemptive()) {
                    continue;
                }
                init = true;
                AuthScope authscope;
                if (StringUtils.isNotBlank(auth.getRealm()) && StringUtils.isNotBlank(auth.getScheme())) {
                    authscope = new AuthScope(auth.getHost(), auth.getPort(), auth.getRealm(), auth.getScheme());
                } else if (StringUtils.isNotBlank(auth.getRealm())) {
                    authscope = new AuthScope(auth.getHost(), auth.getPort(), auth.getRealm());
                } else {
                    authscope = new AuthScope(auth.getHost(), auth.getPort());
                }
                defaultCredentialsProvider.setCredentials(authscope, new UsernamePasswordCredentials(auth.getUser(), auth.getPass()));
            }
            if (!init) {
                defaultCredentialsProvider = new SystemDefaultCredentialsProvider();
            }
        }
        return this.defaultCredentialsProvider;
    }

    public void setDefaultCredentialsProvider(CredentialsProvider credentialsProvider) {
        this.defaultCredentialsProvider = credentialsProvider;
    }

    protected CredentialsProvider getPreemptiveCredentialsProvider() {
        if (preemptiveCredentialsProvider == null && !authentication.isEmpty()) {
            boolean init = false;
            preemptiveCredentialsProvider = new BasicCredentialsProvider();
            for (HTTPClientAuth auth : authentication) {
                if (!auth.isPreemptive()) {
                    continue;
                }
                init = true;
                AuthScope authscope;
                if (StringUtils.isNotBlank(auth.getRealm()) && StringUtils.isNotBlank(auth.getScheme())) {
                    authscope = new AuthScope(auth.getHost(), auth.getPort(), auth.getRealm(), auth.getScheme());
                } else if (StringUtils.isNotBlank(auth.getRealm())) {
                    authscope = new AuthScope(auth.getHost(), auth.getPort(), auth.getRealm());
                } else {
                    authscope = new AuthScope(auth.getHost(), auth.getPort());
                }
                preemptiveCredentialsProvider.setCredentials(authscope, new UsernamePasswordCredentials(auth.getUser(), auth.getPass()));
            }
            if (!init) {
                preemptiveCredentialsProvider = null;
            }
        }
        return this.preemptiveCredentialsProvider;
    }

    public void setPreemptiveCredentialsProvider(CredentialsProvider credentialsProvider) {
        this.preemptiveCredentialsProvider = credentialsProvider;
    }
}
