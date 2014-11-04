package org.jhaws.common.net.client;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.ProxySelector;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.ProtocolException;
import org.apache.http.StatusLine;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.auth.params.AuthPNames;
import org.apache.http.client.AuthCache;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.RedirectStrategy;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpOptions;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.methods.HttpTrace;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.params.AuthPolicy;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.routing.HttpRoutePlanner;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.DefaultRedirectStrategy;
import org.apache.http.impl.client.cache.CacheConfig;
import org.apache.http.impl.client.cache.CachingHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.impl.conn.ProxySelectorRoutePlanner;
import org.apache.http.impl.cookie.DateUtils;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.params.SyncBasicHttpParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.ExecutionContext;
import org.apache.http.protocol.HttpContext;
import org.jhaws.common.io.IOFile;
import org.jhaws.common.io.security.Security;
import org.jhaws.common.net.client.cookies.CookieStore;
import org.jhaws.common.net.client.cookies.PreloadUnixChromeCookies;
import org.jhaws.common.net.client.cookies.PreloadUnixFirefoxCookies;
import org.jhaws.common.net.client.cookies.PreloadWinChromeCookies;
import org.jhaws.common.net.client.cookies.PreloadWinFirefoxCookies;
import org.jhaws.common.net.client.cookies.PreloadWinIExplorerCookies;
import org.jhaws.common.net.client.forms.FileInput;
import org.jhaws.common.net.client.forms.Form;
import org.jhaws.common.net.client.forms.InputElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * http://hc.apache.org<br>
 * http://hc.apache.org/httpcomponents-client-ga/<br>
 * http://en.wikipedia.org/wiki/Post/Redirect/Get<br>
 * http://wiki.apache.org/HttpComponents/LessonsLearned<br>
 * http://wiki.apache.org/HttpComponents/FrequentlyAskedConnectionManagementQuestions<br>
 */
@SuppressWarnings("deprecation")
public class HTTPClient implements Serializable {
    /**
     * GetParams
     */
    public static class GetParams {
        private String accept;

        private String url;

        public GetParams(String url) {
            this.url = url;
        }

        public String getAccept() {
            return this.accept;
        }

        public String getUrl() {
            return this.url;
        }

        public GetParams setAccept(String accept) {
            this.accept = accept;
            return this;
        }

        public GetParams setUrl(String url) {
            this.url = url;
            return this;
        }

        @Override
        public String toString() {
            return "GetParams [url=" + this.url + ", accept=" + this.accept + "]";
        }
    }

    /**
     * InputStreamBody
     */
    protected static class InputStreamBody extends org.apache.http.entity.mime.content.InputStreamBody {
        private int lenght;

        public InputStreamBody(byte[] buffer, String mimeType, String filename) {
            super(new ByteArrayInputStream(buffer), mimeType, filename);
            this.lenght = buffer.length;
        }

        public InputStreamBody(InputStream in, int lenght, String mimeType, String filename) {
            super(in, mimeType, filename);
            this.lenght = lenght;
        }

        /**
         * @see org.apache.http.entity.mime.content.InputStreamBody#getContentLength()
         */
        @Override
        public long getContentLength() {
            return this.lenght;
        }
    }

    /**
     * PostParams
     */
    public static class PostParams extends PutParams {
        private HashMap<String, IOFile> attachments = new HashMap<String, IOFile>();

        public PostParams(String url) {
            super(url);
        }

        public PostParams(String url, HashMap<String, String> formValues) {
            super(url, formValues);
        }

        public PostParams(String url, HashMap<String, String> formValues, HashMap<String, IOFile> attachments) {
            super(url, formValues);
            this.attachments = attachments;
        }

        public HashMap<String, IOFile> getAttachments() {
            return this.attachments;
        }

        public PostParams setAttachments(HashMap<String, IOFile> attachments) {
            this.attachments = attachments;

            return this;
        }

        /**
         * @see org.jhaws.common.net.client.HTTPClient.PutParams#toString()
         */
        @Override
        public String toString() {
            return super.toString() + " & PostParams [attachments=" + this.attachments + "]";
        }
    }

    /**
     * PutParams
     */
    public static class PutParams extends GetParams {
        private HashMap<String, String> formValues = new HashMap<String, String>();

        public PutParams(String url) {
            super(url);
        }

        public PutParams(String url, HashMap<String, String> formValues) {
            super(url);
            this.formValues = formValues;
        }

        public HashMap<String, String> getFormValues() {
            return this.formValues;
        }

        public PutParams setFormValues(HashMap<String, String> formValues) {
            this.formValues = formValues;
            return this;
        }

        /**
         * @see org.jhaws.common.net.client.HTTPClient.GetParams#toString()
         */
        @Override
        public String toString() {
            return super.toString() + " & PutParams [this.formValues=" + this.formValues + "]";
        }
    }

    /**
     * httpResponse + normal response
     */
    protected class ResponseContext {
        protected HttpResponse httpResponse;

        protected Response response;

        protected ResponseContext() {
            super();
        }

        protected ResponseContext(HttpResponse httpResponse, Response response) {
            this.httpResponse = httpResponse;
            this.response = response;
        }

        protected ResponseContext(Response response) {
            this.response = response;
        }
    }

    private static final long serialVersionUID = 5252842616737283754L;

    /** Logger for this class */
    private static Logger logger = LoggerFactory.getLogger(HTTPClient.class);

    /** httpclient */
    protected transient CachingHttpClient httpclientcacher;

    /** connectionManager */
    protected transient ClientConnectionManager connectionManager;

    /** httpclient */
    protected transient DefaultHttpClient httpclient;

    /** params */
    protected transient HttpParams params;

    /** HttpRequestRetryHandler */
    protected transient HttpRequestRetryHandler retryHandler;

    /** HttpRoutePlanner */
    protected transient HttpRoutePlanner httpRoutePlanner;

    /** CookieStore */
    protected CookieStore cookieStore;

    /** 1.1 */
    protected HttpVersion httpVersion;

    /** chain */
    protected transient List<java.net.URI> chain;

    /** httpClientListeners */
    protected transient List<HTTPClientListener> httpClientListeners;

    /** redirectStrategy */
    protected transient RedirectStrategy redirectStrategy;

    /** security */
    protected transient Security secure;

    /** accept types */
    protected String accept;

    /** gzip, deflate */
    protected String acceptEncoding;

    /** default language, en */
    protected String acceptLanguage;

    /** UTF-8 */
    protected String charset;

    /** compatibility */
    protected String cookiePolicy;

    /** current domain */
    protected String domain;

    /** user */
    protected String user;

    /** userAgent: html/script/document.write(navigator.userAgent);/script/html */
    protected String userAgent;

    /** pass */
    protected byte[] pass;

    /** caching */
    protected boolean caching;

    /** false */
    protected boolean expectContinue;

    /** false */
    protected boolean handleRedirects;

    /** true */
    protected boolean singleCookieHeader;

    /** 300 */
    protected int keepAlive;

    /** test values */
    protected int maxCacheEntries;

    /** test values */
    protected int maxCacheEntrySizeBytes;

    /** 1000L */
    protected int timeout;

    /** version */
    protected int version;

    protected long downloaded = 0;

    public HTTPClient() {
        this.cookiePolicy = HTTPClientDefaults.BROWSER_COMPATIBILITY;
        this.userAgent = HTTPClientDefaults.CHROME;
        this.charset = HTTPClientDefaults.CHARSET;
        this.acceptEncoding = HTTPClientDefaults.ACCEPT_ENCODING;
        this.accept = HTTPClientDefaults.ACCEPT;
        this.httpVersion = HTTPClientDefaults.HTTP_VERSION;
        this.expectContinue = HTTPClientDefaults.EXPECT_CONTINUE;
        this.handleRedirects = HTTPClientDefaults.HANDLE_REDIRECTS;
        this.singleCookieHeader = HTTPClientDefaults.SINGLE_COOKIE_HEADER;
        this.keepAlive = HTTPClientDefaults.KEEP_ALIVE;
        this.timeout = HTTPClientDefaults.TIMEOUT;

        this.maxCacheEntries = 128;
        this.maxCacheEntrySizeBytes = 1024 * 1024;
        this.caching = false;

        this.version = 0;

        try {
            this.secure = (Security) Class.forName("org.jhaws.common.io.security.SecureMeHard").newInstance();
        } catch (Exception ex1) {
            try {
                this.secure = (Security) Class.forName("org.jhaws.common.io.security.SecureMe").newInstance();
            } catch (Exception ex2) {
                System.err.println("cannot use passwords");
                ex1.printStackTrace();
                ex2.printStackTrace();
            }
        }

        this.httpClientListeners = new ArrayList<HTTPClientListener>();

        this.chain = new ArrayList<java.net.URI>();

        this.redirectStrategy = new DefaultRedirectStrategy() {
            @Override
            public HttpUriRequest getRedirect(HttpRequest request, HttpResponse response, HttpContext context) throws ProtocolException {
                HttpUriRequest redirect = super.getRedirect(request, response, context);
                HTTPClient.this.chain.add(redirect.getURI());
                HTTPClient.this.domain = redirect.getURI().getHost();
                // System.out.println("redirect: " + redirect.getURI());
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
                        return (method.equalsIgnoreCase(HttpGet.METHOD_NAME) || method.equalsIgnoreCase(HttpPost.METHOD_NAME) || method
                                .equalsIgnoreCase(HttpHead.METHOD_NAME)) && (locationHeader != null);
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

    /**
     *
     * @see org.apache.http.client.CookieStore#addCookie(org.apache.http.cookie.Cookie)
     */
    public HTTPClient addCookie(Cookie cookie) {
        this.getCookieStore().addCookie(cookie);
        return this;
    }

    public HTTPClient addHTTPClientListener(HTTPClientListener listener) {
        this.httpClientListeners.add(listener);
        return this;
    }

    /**
     *
     * @see org.apache.http.client.CookieStore#clear()
     */
    public HTTPClient clearCookies() {
        this.getCookieStore().clear();
        return this;
    }

    /**
     *
     * @see org.apache.http.client.CookieStore#clearExpired(java.util.Date)
     */
    public boolean clearExpiredCookies(Date date) {
        return this.getCookieStore().clearExpired(date);
    }

    public HTTPClient clearHTTPClientListener() {
        this.httpClientListeners.clear();
        return this;
    }

    public void compare(HTTPClient other) {
        this.compare("accept", this.accept, other.accept);
        this.compare("acceptEncoding", this.acceptEncoding, other.acceptEncoding);
        this.compare("acceptLanguage", this.acceptLanguage, other.acceptLanguage);
        this.compare("caching", this.caching, other.caching);
        this.compare("charset", this.charset, other.charset);
        this.compare("cookiePolicy", this.cookiePolicy, other.cookiePolicy);
        this.compare("domain", this.domain, other.domain);
        this.compare("expectContinue", this.expectContinue, other.expectContinue);
        this.compare("handleRedirects", this.handleRedirects, other.handleRedirects);
        this.compare("httpVersion", this.httpVersion, other.httpVersion);
        this.compare("keepAlive", this.keepAlive, other.keepAlive);
        this.compare("maxCacheEntries", this.maxCacheEntries, other.maxCacheEntries);
        this.compare("maxCacheEntrySizeBytes", this.maxCacheEntrySizeBytes, other.maxCacheEntrySizeBytes);
        this.compare("singleCookieHeader", this.singleCookieHeader, other.singleCookieHeader);
        this.compare("timeout", this.timeout, other.timeout);
        this.compare("user", this.user, other.user);
        this.compare("userAgent", this.userAgent, other.userAgent);
        this.compare("version", this.version, other.version);
    }

    private void compare(String key, Object v1, Object v2) {
        if (new EqualsBuilder().append(v1, v2).isEquals()) {
            return;
        }
        System.out.println(key + " : " + v1 + " <> " + v2);
    }

    protected HTTPClient consume(HttpResponse response) {
        try {
            if (response != null) {
                org.apache.http.util.EntityUtils.consume(response.getEntity());
            }
        } catch (Exception ex) {
            //
        }
        return this;
    }

    protected synchronized ResponseContext execute(HttpRequestBase httpRequest, String ccpt) throws IOException, InternalServerError {
        this.chain.clear();

        ResponseContext response = this.innerExecute(httpRequest, ccpt);

        // if (isHandleRedirects() && (response.getRedirect() != null)) {
        // try {
        // response = get(response.getRedirect());
        // } catch (HttpException ex) {
        // throw new RuntimeException(ex);
        // } catch (URISyntaxException ex) {
        // throw new RuntimeException(ex);
        // }
        // }
        response.response.chain = this.chain;

        return response;
    }

    public Response get(GetParams prm) throws HttpException, IOException, URISyntaxException {
        HttpRequestBase method = new HttpGet(prm.getUrl());
        return this.execute(method, prm.getAccept()).response;
    }

    protected byte[] get(HttpEntity entity) throws IOException {
        int size = (entity.getContentLength() != -1) ? (int) entity.getContentLength() : 2048;
        ByteArrayOutputStream bout = new ByteArrayOutputStream(size);
        entity.writeTo(bout);
        bout.close();
        byte[] arr = bout.toByteArray();
        this.downloaded += arr.length;
        return arr;
    }

    public Response get(String url) throws HttpException, IOException, URISyntaxException {
        return this.get(new GetParams(url));
    }

    public String getAccept() {
        return this.accept;
    }

    public String getAcceptEncoding() {
        return this.acceptEncoding;
    }

    public String getAcceptLanguage() {
        if (this.acceptLanguage == null) {
            String localLanguage = Locale.getDefault().getLanguage();
            if (HTTPClientDefaults.ACCEPT_LANGUAGE.equals(localLanguage)) {
                this.acceptLanguage = HTTPClientDefaults.ACCEPT_LANGUAGE;
            } else {
                this.acceptLanguage = localLanguage + "," + HTTPClientDefaults.ACCEPT_LANGUAGE;
            }
        }
        return this.acceptLanguage;
    }

    /**
     * getAllowedMethods
     */
    public Set<String> getAllowedMethods(String url) throws IOException, URISyntaxException, InternalServerError {
        HttpOptions optionRequest = new HttpOptions(url);
        ResponseContext response = this.execute(optionRequest, null);

        return optionRequest.getAllowedMethods(response.httpResponse);
    }

    public String getCharset() {
        return this.charset;
    }

    protected ClientConnectionManager getConnectionManager() {
        if (this.connectionManager == null) {
            SchemeRegistry schemeRegistry = new SchemeRegistry();

            Scheme http = new Scheme("http", 80, PlainSocketFactory.getSocketFactory());
            schemeRegistry.register(http);

            Scheme https = new Scheme("https", 443, SSLSocketFactory.getSocketFactory());
            schemeRegistry.register(https);

            PoolingClientConnectionManager cm = new PoolingClientConnectionManager(schemeRegistry);
            // ThreadSafeClientConnManager cm = new ThreadSafeClientConnManager(schemeRegistry);
            cm.setMaxTotal(10);
            cm.setDefaultMaxPerRoute(3);

            this.connectionManager = cm;
        }

        return this.connectionManager;
    }

    public String getCookiePolicy() {
        return this.cookiePolicy;
    }

    /**
     * @see org.apache.http.client.CookieStore#getCookies()
     */
    public List<Cookie> getCookies() {
        return this.getCookieStore().getCookies();
    }

    public CookieStore getCookieStore() {
        if (this.cookieStore == null) {
            this.cookieStore = new CookieStore(this);
        }
        return this.cookieStore;
    }

    public String getDomain() {
        return this.domain;
    }

    public long getDownloaded() {
        return this.downloaded;
    }

    /**
     * get headers only (HEAD method)
     */
    public Header[] getHeaders(String url) throws IOException, URISyntaxException, InternalServerError {
        HttpHead head = new HttpHead(url);
        ResponseContext response = this.execute(head, null);
        return response.httpResponse.getAllHeaders();
    }

    protected DefaultHttpClient getHttpclient() {
        if (this.httpclient == null) {
            // ContentEncodingHttpClient hc = new ContentEncodingHttpClient(getConnectionManager(), getParams());
            DefaultHttpClient hc = new DefaultHttpClient(this.getConnectionManager(), this.getParams());
            hc.setHttpRequestRetryHandler(this.getRetryHandler());
            hc.setRoutePlanner(this.getHttpRoutePlanner());
            hc.setCookieStore(this.getCookieStore());
            hc.setRedirectStrategy(this.redirectStrategy);

            this.httpclient = hc;
        }

        return this.httpclient;
    }

    protected HttpClient getHttpclientcacher() {
        if (this.httpclientcacher == null) {
            CacheConfig cacheConfig = new CacheConfig();
            cacheConfig.setMaxCacheEntries(this.maxCacheEntries);
            cacheConfig.setMaxObjectSize(this.maxCacheEntrySizeBytes);
            this.httpclientcacher = new CachingHttpClient(this.getHttpclient(), cacheConfig);
        }

        return this.httpclientcacher;
    }

    protected HttpRoutePlanner getHttpRoutePlanner() {
        if (this.httpRoutePlanner == null) {
            this.httpRoutePlanner = new ProxySelectorRoutePlanner(this.getConnectionManager().getSchemeRegistry(), ProxySelector.getDefault());
        }
        return this.httpRoutePlanner;
    }

    public HttpVersion getHttpVersion() {
        return this.httpVersion;
    }

    public int getKeepAlive() {
        return this.keepAlive;
    }

    protected String getName(URI url) {
        String tmp = url.toString();
        return tmp.substring(tmp.lastIndexOf("/") + 1);
    }

    public HttpParams getParams() {
        if (this.params == null) {
            this.params = new SyncBasicHttpParams();
            HttpProtocolParams.setVersion(this.params, this.httpVersion);
            HttpProtocolParams.setContentCharset(this.params, this.charset);
            HttpProtocolParams.setUserAgent(this.params, this.userAgent);
            HttpProtocolParams.setUseExpectContinue(this.params, this.expectContinue);
            this.params.setParameter(ClientPNames.COOKIE_POLICY, this.cookiePolicy);
            this.params.setBooleanParameter(HTTPClientDefaults.PARAM_SINGLE_COOKIE_HEADER, this.singleCookieHeader);
            this.params.setParameter(HTTPClientDefaults.PARAM_ACCEPT, this.accept);
            this.params.setParameter(HTTPClientDefaults.PARAM_ACCEPT_ENCODING, this.acceptEncoding);
            this.params.setParameter(HTTPClientDefaults.PARAM_ACCEPT_LANGUAGE, this.getAcceptLanguage());
            this.params.setParameter(HTTPClientDefaults.PARAM_KEEP_ALIVE, String.valueOf(this.keepAlive));
            this.params.setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, this.timeout);
            this.params.setBooleanParameter(ClientPNames.HANDLE_REDIRECTS, this.handleRedirects);

            List<String> authpref = new ArrayList<String>();
            authpref.add(AuthPolicy.BASIC);
            authpref.add(AuthPolicy.DIGEST);
            this.params.setParameter(AuthPNames.PROXY_AUTH_PREF, authpref);
        }

        return this.params;
    }

    protected String getPass() {
        try {
            return this.secure.decrypt(this.pass);
        } catch (RuntimeException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public HttpRequestRetryHandler getRetryHandler() {
        if (this.retryHandler == null) {
            this.retryHandler = new DefaultHttpRequestRetryHandler();
        }

        return this.retryHandler;
    }

    public int getTimeout() {
        return this.timeout;
    }

    public java.net.URI getUrl() {
        return this.chain.get(this.chain.size() - 1);
    }

    public String getUserAgent() {
        return this.userAgent;
    }

    protected synchronized ResponseContext innerExecute(HttpRequestBase httpRequest, String ccpt) throws IOException,
    org.jhaws.common.net.client.InternalServerError, org.apache.http.conn.ConnectTimeoutException,
    org.apache.http.client.ClientProtocolException {
        HTTPClient.logger.info("url=" + httpRequest.getURI());
        this.chain.add(httpRequest.getURI());

        HttpResponse response = null;
        HttpContext httpContext = new BasicHttpContext();

        try {
            for (HTTPClientListener httpClientListener : this.httpClientListeners) {
                httpClientListener.beforeExecute(this, httpRequest.getURI(), httpRequest);
            }

            if ((this.user != null) && (this.pass != null)) {
                HttpHost targetHost = new HttpHost(this.domain, 80, "http");
                AuthScope scope = new AuthScope(targetHost.getHostName(), targetHost.getPort());

                if (this.getHttpclient().getCredentialsProvider().getCredentials(scope) == null) {
                    this.getHttpclient()
                    .getCredentialsProvider()
                    .setCredentials(new AuthScope(targetHost.getHostName(), targetHost.getPort()),
                            new UsernamePasswordCredentials(this.user, this.getPass()));

                    AuthCache authCache = new BasicAuthCache();
                    BasicScheme basicAuth = new BasicScheme();
                    authCache.put(targetHost, basicAuth);
                    httpContext.setAttribute(ClientContext.AUTH_CACHE, authCache);
                }
            }

            if (ccpt != null) {
                httpRequest.setHeader("Accept", ccpt);
            }

            try {
                response = this.caching ? this.getHttpclientcacher().execute(httpRequest, httpContext) : this.getHttpclient().execute(httpRequest,
                        httpContext);
            } catch (ClientProtocolException ex) {
                // if ((ex.getCause() != null) && ex.getCause() instanceof CircularRedirectException && isHandleRedirects()) {
                // setHandleRedirects(false);
                // response = getHttpclient().execute(httpRequest, httpContext);
                // } else {
                throw ex;

                // }
            }

            this.version++;

            try {
                if (this.httpclient.getRedirectStrategy().isRedirected(httpRequest, response, httpContext)) {
                    HttpUriRequest redirect = this.httpclient.getRedirectStrategy().getRedirect(httpRequest, response, httpContext);

                    return new ResponseContext(new Response(redirect.getURI().toString()));
                }
            } catch (ProtocolException ex1) {
                throw new RuntimeException(ex1);
            }

            StatusLine status = response.getStatusLine();

            switch (status.getStatusCode()) {
                case HttpStatus.SC_OK:
                    break;

                case HttpStatus.SC_NOT_FOUND:
                    throw new IOException(httpRequest.getURI() + " " + status.getReasonPhrase());

                case HttpStatus.SC_UNAUTHORIZED:
                    throw new RuntimeException("not implemented: " + status);

                case HttpStatus.SC_INTERNAL_SERVER_ERROR: {
                    byte[] fetchContent = null;

                    if (!(httpRequest instanceof HttpHead)) {
                        HttpEntity entity = response.getEntity();

                        if (entity != null) {
                            try {
                                fetchContent = this.get(entity);
                            } catch (IOException ex) {
                                ex.printStackTrace(System.out);
                            }
                        }
                    }

                    throw new InternalServerError((fetchContent == null) ? status.getReasonPhrase() : new String(fetchContent));
                }

                // case HttpStatus.SC_MOVED_PERMANENTLY:
                // case HttpStatus.SC_MOVED_TEMPORARILY:
                // case HttpStatus.SC_SEE_OTHER:
                // case HttpStatus.SC_TEMPORARY_REDIRECT:
                // Header header = response.getFirstHeader(HTTPClientDefaults.LOCATION);
                //
                // return new Response(header.getValue());
                case HttpStatus.SC_GATEWAY_TIMEOUT:
                    throw new IOException(status.getReasonPhrase());

                case HttpStatus.SC_FORBIDDEN:
                    throw new IOException(status.getReasonPhrase());

                default:
                    throw new RuntimeException("not implemented: " + status);
            }

            HttpHost target = (HttpHost) httpContext.getAttribute(ExecutionContext.HTTP_TARGET_HOST);
            HttpUriRequest req = (HttpUriRequest) httpContext.getAttribute(ExecutionContext.HTTP_REQUEST);
            HTTPClient.logger.debug("targetHost: " + target);
            HTTPClient.logger.debug("requestURI: " + req.getURI());
            HTTPClient.logger.debug("requestMethod: " + req.getMethod());

            byte[] fetchContent = null;

            if (!(httpRequest instanceof HttpHead)) {
                HttpEntity entity = response.getEntity();

                if (entity == null) {
                    throw new IOException("no entity");
                }

                try {
                    fetchContent = this.get(entity);

                    HTTPClient.logger.debug("contentLength: " + entity.getContentLength());
                    HTTPClient.logger.debug("contentEncoding: " + entity.getContentEncoding());
                    HTTPClient.logger.debug("contentType: " + entity.getContentType());
                } catch (IOException ex) {
                    ex.printStackTrace(System.out);
                }
            }

            for (Header header : response.getAllHeaders()) {
                HTTPClient.logger.debug("" + header);
            }

            Object userToken = httpContext.getAttribute(ClientContext.USER_TOKEN);
            HTTPClient.logger.debug("userToken: " + userToken);

            for (Cookie cookie : this.getCookies()) {
                HTTPClient.logger.debug("cookie: " + cookie);
            }

            String mime = null;

            try {
                mime = response.getFirstHeader(HTTPClientDefaults.CONTENT_TYPE).getValue();
            } catch (Exception ex) {
                //
            }

            String filename = null;

            try {
                filename = this.getName(this.chain.get(this.chain.size() - 1));
            } catch (Exception ex) {
                //
            }

            Response returns = new Response(fetchContent, mime, filename, this.getCharset());

            try {
                returns.setDate(DateUtils.parseDate(response.getFirstHeader(HTTPClientDefaults.DATE).getValue()));
            } catch (Exception ex) {
                //
            }

            for (HTTPClientListener httpClientListener : this.httpClientListeners) {
                httpClientListener.afterExecute(this, httpRequest.getURI(), httpRequest, returns);
            }

            return new ResponseContext(response, returns);
        } finally {
            this.consume(response);
        }
    }

    public boolean isCaching() {
        return this.caching;
    }

    public boolean isExpectContinue() {
        return this.expectContinue;
    }

    public boolean isHandleRedirects() {
        return this.handleRedirects;
    }

    public boolean isSingleCookieHeader() {
        return this.singleCookieHeader;
    }

    public Response post(PostParams prms) throws HttpException, IOException, URISyntaxException, InternalServerError {
        for (HTTPClientListener httpClientListener : this.httpClientListeners) {
            httpClientListener.beforeMethod(this, "POST", prms.getUrl(), prms.getFormValues(), prms.getAttachments());
        }

        HttpPost method;
        HttpEntity entity;

        if ((prms.getAttachments() == null) || (prms.getAttachments().size() == 0)) {
            List<NameValuePair> formparams = new ArrayList<NameValuePair>();

            for (Map.Entry<String, String> entry : prms.getFormValues().entrySet()) {
                formparams.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
            }

            entity = new UrlEncodedFormEntity(formparams, this.charset);
        } else {
            entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);

            for (Map.Entry<String, String> entry : prms.getFormValues().entrySet()) {
                ((MultipartEntity) entity).addPart(entry.getKey(), new StringBody(entry.getValue(), "text/plain", Charset.forName(this.charset)));
            }

            for (Map.Entry<String, IOFile> attachment : prms.getAttachments().entrySet()) {
                ((MultipartEntity) entity).addPart(attachment.getKey(),
                        new FileBody(attachment.getValue(), HTTPClientUtils.getMimeType(attachment.getValue())));
            }
        }

        method = new HttpPost(prms.getUrl());
        method.setEntity(entity);

        return this.execute(method, prms.getAccept()).response;
    }

    public Response post(String url, HashMap<String, String> formValues) throws HttpException, IOException, URISyntaxException {
        return this.post(new PostParams(url, formValues));
    }

    public Response post(String url, HashMap<String, String> formValues, HashMap<String, IOFile> attachments) throws HttpException, IOException,
    URISyntaxException {
        return this.post(new PostParams(url, formValues, attachments));
    }

    public Response put(PutParams prms) throws HttpException, IOException, URISyntaxException, InternalServerError {
        for (HTTPClientListener httpClientListener : this.httpClientListeners) {
            httpClientListener.beforeMethod(this, "PUT", prms.getUrl(), prms.getFormValues(), null);
        }

        List<NameValuePair> formparams = new ArrayList<NameValuePair>();

        for (Map.Entry<String, String> entry : prms.getFormValues().entrySet()) {
            formparams.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
        }

        java.net.URI uri = new java.net.URI(prms.getUrl());
        String uriPlusQuery = uri.toString() + URLEncodedUtils.format(formparams, this.charset);
        HttpEntityEnclosingRequestBase method = new HttpPut(uriPlusQuery);

        return this.execute(method, prms.getAccept()).response;
    }

    public Response put(String url, HashMap<String, String> formValues) throws HttpException, IOException, URISyntaxException {
        return this.put(new PutParams(url, formValues));
    }

    public HTTPClient removeHTTPClientListener(HTTPClientListener listener) {
        this.httpClientListeners.remove(listener);
        return this;
    }

    public HTTPClient resetAuthentication() {
        this.user = null;
        this.pass = null;
        return this;
    }

    public HTTPClient serialize(OutputStream out) throws IOException {
        HTTPClientUtils.serialize(this, out);
        return this;
    }

    public HTTPClient setAccept(String accept) {
        this.accept = accept;
        return this.setParams(null);
    }

    public HTTPClient setAcceptEncoding(String acceptEncoding) {
        this.acceptEncoding = acceptEncoding;
        return this.setParams(null);
    }

    public HTTPClient setAcceptLanguage(String acceptLanguage) {
        this.acceptLanguage = acceptLanguage;
        return this.setParams(null);
    }

    public HTTPClient setAuthentication(String user, String pass) {
        this.user = user;
        try {
            this.pass = this.secure.encrypt(pass);
        } catch (RuntimeException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        return this;
    }

    public HTTPClient setCaching(boolean caching) {
        this.caching = caching;
        this.setHttpclientcacher(null);
        return this;
    }

    public HTTPClient setCharset(String charset) {
        this.charset = charset;
        return this.setHttpclient(null);
    }

    public HTTPClient setCookiePolicy(String cookiePolicy) {
        this.cookiePolicy = cookiePolicy;
        return this.setParams(null);
    }

    public HTTPClient setCookieStore(CookieStore cookieStore) {
        this.cookieStore = cookieStore;
        return this.setHttpclient(null);
    }

    public HTTPClient setDomain(String domain) {
        this.domain = domain;
        return this;
    }

    public HTTPClient setExpectContinue(boolean expectContinue) {
        this.expectContinue = expectContinue;
        return this.setParams(null);
    }

    public HTTPClient setHandleRedirects(boolean handleRedirects) {
        this.handleRedirects = handleRedirects;
        return this.setParams(null);
    }

    protected HTTPClient setHttpclient(DefaultHttpClient httpclient) {
        this.httpclient = httpclient;
        return this;
    }

    protected HTTPClient setHttpclientcacher(CachingHttpClient httpclientcacher) {
        this.httpclientcacher = httpclientcacher;
        return this;
    }

    protected HTTPClient setHttpRoutePlanner(HttpRoutePlanner httpRoutePlanner) {
        this.httpRoutePlanner = httpRoutePlanner;
        return this.setHttpclient(null);
    }

    public HTTPClient setHttpVersion(HttpVersion httpVersion) {
        this.httpVersion = httpVersion;
        return this.setParams(null);
    }

    public HTTPClient setKeepAlive(int keepAlive) {
        this.keepAlive = keepAlive;
        return this.setParams(null);
    }

    protected HTTPClient setParams(HttpParams params) {
        this.params = params;
        return this.setHttpclient(null);
    }

    public HTTPClient setPreloadCookies(boolean preloadCookies) {
        if (preloadCookies) {
            CookieStore cs = this.getCookieStore();

            if (!PreloadUnixChromeCookies.windows) {
                try {
                    cs.addCookieStoreInterceptor(new PreloadUnixFirefoxCookies());
                } catch (Exception ex) {
                    HTTPClient.logger.error(ExceptionUtils.getFullStackTrace(ex));
                }
            }

            if (!PreloadUnixChromeCookies.windows) {
                try {
                    cs.addCookieStoreInterceptor(new PreloadUnixChromeCookies());
                } catch (Exception ex) {
                    HTTPClient.logger.error(ExceptionUtils.getFullStackTrace(ex));
                }
            }

            if (PreloadUnixChromeCookies.windows) {
                try {
                    cs.addCookieStoreInterceptor(new PreloadWinFirefoxCookies());
                } catch (Exception ex) {
                    HTTPClient.logger.error(ExceptionUtils.getFullStackTrace(ex));
                }
            }

            if (PreloadUnixChromeCookies.windows) {
                try {
                    cs.addCookieStoreInterceptor(new PreloadWinIExplorerCookies());
                } catch (Exception ex) {
                    HTTPClient.logger.error(ExceptionUtils.getFullStackTrace(ex));
                }
            }

            if (PreloadUnixChromeCookies.windows) {
                try {
                    cs.addCookieStoreInterceptor(new PreloadWinChromeCookies());
                } catch (Exception ex) {
                    HTTPClient.logger.error(ExceptionUtils.getFullStackTrace(ex));
                }
            }
        } else {
            this.getCookieStore().clearCookieStoreInterceptors();
        }

        return this;
    }

    protected HTTPClient setRetryHandler(HttpRequestRetryHandler retryHandler) {
        this.retryHandler = retryHandler;
        return this.setHttpclient(null);
    }

    public HTTPClient setSingleCookieHeader(boolean singleCookieHeader) {
        this.singleCookieHeader = singleCookieHeader;
        return this.setParams(null);
    }

    public HTTPClient setTimeout(int timeout) {
        this.timeout = timeout;
        return this.setParams(null);
    }

    public HTTPClient setUserAgent(String userAgent) {
        this.userAgent = userAgent;
        return this.setParams(null);
    }

    public boolean shutdown() {
        if (this.httpclient == null) {
            return false;
        }

        this.httpclient.getConnectionManager().shutdown();
        this.httpclientcacher = null;
        this.httpclient = null;

        return true;
    }

    /**
     * submit a form
     */
    public Response submit(Form form) throws HttpException, IOException, URISyntaxException {
        HashMap<String, String> formValues = new HashMap<String, String>();

        for (InputElement element : form.getInputElements()) {
            if (StringUtils.isNotBlank(element.getName()) && StringUtils.isNotBlank(element.getValue())) {
                formValues.put(element.getName(), element.getValue());
            }
        }

        HashMap<String, IOFile> uploadValues = new HashMap<String, IOFile>();

        for (InputElement element : form.getInputElements()) {
            if ((element instanceof FileInput) && StringUtils.isNotBlank(element.getName()) && StringUtils.isNotBlank(element.getValue())) {
                uploadValues.put(element.getName(), ((FileInput) element).getFile());
            }
        }

        URI uri = StringUtils.isBlank(form.getAction()) ? form.getUrl() : URIUtils.resolve(form.getUrl(), form.getAction());

        if ("put".equals(form.getMethod())) {
            return this.put(uri.toString(), formValues);
        }

        if (uploadValues.size() > 0) {
            return this.post(uri.toString(), formValues, uploadValues);
        }

        return this.post(uri.toString(), formValues);
    }

    /**
     * trace
     */
    public Response trace(String url) throws IOException, URISyntaxException, InternalServerError {
        HttpTrace optionRequest = new HttpTrace(url);
        return this.execute(optionRequest, null).response;
    }
}
