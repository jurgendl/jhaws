package org.jhaws.common.net.client;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.apache.commons.lang3.time.DateUtils.parseDate;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UncheckedIOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Objects;

import javax.annotation.PreDestroy;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;

import org.apache.commons.io.IOUtils;
import org.apache.hc.client5.http.ssl.HttpsSupport;
import org.apache.hc.core5.util.TextUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.ProtocolException;
import org.apache.http.auth.AuthSchemeProvider;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.NTCredentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.AuthCache;
import org.apache.http.client.CookieStore;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.RedirectStrategy;
import org.apache.http.client.config.AuthSchemes;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpOptions;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpTrace;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
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
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HttpContext;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;
import org.jhaws.common.io.FilePath;
import org.jhaws.common.lang.EnhancedArrayList;
import org.jhaws.common.lang.EnhancedList;
import org.jhaws.common.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("serial")
public class HTTPClient extends HTTPClientBase<HTTPClient> {
	protected static Logger logger = LoggerFactory.getLogger(HTTPClient.class);

	protected transient CloseableHttpClient httpClient;

	protected transient RequestConfig requestConfig;

	protected transient RedirectStrategy redirectStrategy;

	protected transient CookieStore cookieStore;

	protected final ThreadLocal<HttpClientContext> context = new ThreadLocal<HttpClientContext>() {
		@Override
		protected HttpClientContext initialValue() {
			HttpClientContext _context = HttpClientContext.create();
			CredentialsProvider preemptiveCP = getPreemptiveCredentialsProvider();
			if (preemptiveCP != null) {
				_context.setCredentialsProvider(preemptiveCP);
				AuthCache authCache = new BasicAuthCache();
				BasicScheme basicAuth = new BasicScheme();
				URI _preemptiveCPBaseUrl = preemptiveCPBaseUrl.get();
				HttpHost targetHost = new HttpHost(_preemptiveCPBaseUrl.getHost(), _preemptiveCPBaseUrl.getPort(),
						_preemptiveCPBaseUrl.getScheme());
				authCache.put(targetHost, basicAuth);
				_context.setAuthCache(authCache);
			}
			return _context;
		};
	};

	protected transient HttpClientConnectionManager connectionManager;

	protected transient CredentialsProvider defaultCredentialsProvider;

	protected transient CredentialsProvider preemptiveCredentialsProvider;

	protected transient EnhancedList<HTTPClientAuth> authentication = new EnhancedArrayList<>();

	public HTTPClient() {
		super();
	}

	public RequestConfig getRequestConfig() {
		if (requestConfig == null) {
			requestConfig = getRequestConfigBuilder().build();
		}
		return requestConfig;
	}

	protected RequestConfig.Builder getRequestConfigBuilder() {
		RequestConfig.Builder requestConfigBuilder = RequestConfig//
				.custom()//
				.setMaxRedirects(maxRedirects)//
				.setCircularRedirectsAllowed(circularRedirectsEnabled)//
				.setConnectionRequestTimeout(timeout)//
				.setConnectTimeout(timeout)//
				.setSocketTimeout(timeout)//
				.setExpectContinueEnabled(expectContinueEnabled)//
				.setRedirectsEnabled(redirectsEnabled)//
				// https://stackoverflow.com/questions/36473478/fixing-httpclient-warning-invalid-expires-attribute-using-fluent-api/40697322
				.setCookieSpec(cookieSpec)//
		// .setCookieSpec(org.apache.http.client.params.CookiePolicy.BROWSER_COMPATIBILITY)//
		;
		if (sharepoint) {
			requestConfigBuilder//
					.setTargetPreferredAuthSchemes(Arrays.asList(AuthSchemes.NTLM))//
					.setCookieSpec(cookieSpec)//
			;
		}
		requestConfigBuilder = requestConfigBuilder.setContentCompressionEnabled(compressed);
		return requestConfigBuilder;
	}

	public RedirectStrategy getRedirectStrategy() {
		if (redirectStrategy == null) {
			redirectStrategy = new LaxRedirectStrategy() {
				@Override
				public HttpUriRequest getRedirect(HttpRequest request, HttpResponse response, HttpContext context)
						throws ProtocolException {
					HttpUriRequest redirect = super.getRedirect(request, response, context);
					if (chain != null)
						chain.get().add(redirect.getURI());
					return redirect;
				}

				@Override
				public boolean isRedirected(HttpRequest request, HttpResponse response, HttpContext context)
						throws ProtocolException {
					if (response == null) {
						throw new IllegalArgumentException("HTTP response may not be null");
					}

					int statusCode = response.getStatusLine().getStatusCode();
					String method = request.getRequestLine().getMethod();
					Header locationHeader = response.getFirstHeader("location");

					switch (statusCode) {
					case HttpStatus.SC_MOVED_TEMPORARILY:
						return (method.equalsIgnoreCase(HttpGet.METHOD_NAME)
								|| method.equalsIgnoreCase(HttpPost.METHOD_NAME)
								|| method.equalsIgnoreCase(HttpHead.METHOD_NAME)) && (locationHeader != null);
					case HttpStatus.SC_MOVED_PERMANENTLY:
					case HttpStatus.SC_TEMPORARY_REDIRECT:
						return method.equalsIgnoreCase(HttpGet.METHOD_NAME)
								|| method.equalsIgnoreCase(HttpPost.METHOD_NAME)
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

	public synchronized CloseableHttpClient getHttpClient() {
		if (httpClient == null) {
			httpClient = getHttpClientBuilder().build();//
		}
		return httpClient;
	}

	protected HttpClientBuilder getHttpClientBuilder() {
		HttpClientBuilder httpClientBuilder = HttpClientBuilder.create()//
				.setDefaultCookieStore(getCookieStore()) //
				.setUserAgent(getUserAgent())//
				.setRedirectStrategy(getRedirectStrategy())//
				.setDefaultRequestConfig(getRequestConfig())//
				.setConnectionManager(getConnectionManager())//
				.setDefaultCredentialsProvider(getDefaultCredentialsProvider())//
		;
		if (sharepoint) {
			httpClientBuilder.setDefaultAuthSchemeRegistry(//
					RegistryBuilder.<AuthSchemeProvider>create()//
							.register(AuthSchemes.NTLM, new JCIFSNTLMSchemeFactory())//
							.build()//
			);
		}
		if (!compressed) {
			httpClientBuilder.disableContentCompression();
		}
		return httpClientBuilder;
	}

	protected HttpClientConnectionManager getConnectionManager() {
		if (connectionManager == null) {
			// replaces ThreadSafeClientConnManager
			PoolingHttpClientConnectionManager poolingConnectionManager = new PoolingHttpClientConnectionManager(//
					RegistryBuilder.<ConnectionSocketFactory>create()//
							.register("http", PlainConnectionSocketFactory.getSocketFactory())//
							.register("https", getSSLConnectionSocketFactory())//
							.build()//
			);
			poolingConnectionManager.setMaxTotal(20);
			poolingConnectionManager.setDefaultMaxPerRoute(2);
			connectionManager = poolingConnectionManager;
		}
		return connectionManager;
	}

	private static String getProperty(final String key) {
		return AccessController.doPrivileged(new PrivilegedAction<String>() {
			@Override
			public String run() {
				return System.getProperty(key);
			}
		});
	}

	public static String[] getSystemCipherSuits() {
		return split(getProperty("https.cipherSuites"));
	}

	private static String[] split(final String s) {
		if (TextUtils.isBlank(s)) {
			return null;
		}
		return s.split(" *, *");
	}

	protected SSLConnectionSocketFactory getSSLConnectionSocketFactory() {
		return new SSLConnectionSocketFactory(//
				getSSLContext()//
				, tlsVersions//
				, getSystemCipherSuits()//
				, getHostnameVerifier()//
		);
	}

	protected HostnameVerifier getHostnameVerifier() {
		if (disableSSLCheck) {
			return NoopHostnameVerifier.INSTANCE;
		}
		return HttpsSupport.getDefaultHostnameVerifier();
	}

	protected SSLContext getSSLContext() {
		if (disableSSLCheck) {
			return noOpSsl();
		}
		return SSLContexts.createSystemDefault();
	}

	public Response execute(AbstractRequest<? extends AbstractRequest<?>> params, HttpUriRequest req) {
		return execute(params, req, null, null);
	}

	public Response execute(AbstractRequest<? extends AbstractRequest<?>> params, HttpUriRequest req, OutputStream out,
			RequestListener requestListener) {
		if (params != null) {
			prepareRequest(params, req);
		}

		chain.get().clear();

		URI uri = req.getURI();
		logger.trace("{}", uri);

		if (StringUtils.isNotBlank(params.getReferer())) {
			req.addHeader("Referer", params.getReferer());
		} else {
			req.addHeader("Referer", uri.toASCIIString());
		}

		HttpHost targetHost = new HttpHost(uri.getHost(), uri.getPort(), uri.getScheme());
		Response response = null;
		if (requestListener == null)
			requestListener = new RequestListenerAdapter();
		try (CloseableHttpResponse httpResponse = getHttpClient().execute(targetHost, req, getContext(uri))) {
			response = buildResponse(req, httpResponse, out, requestListener);
			if (throwException && 500 <= response.getStatusCode() && response.getStatusCode() <= 599) {
				throw new HttpException(response, response.getStatusCode(), response.getStatusText());
			}
			EntityUtils.consumeQuietly(httpResponse.getEntity());
		} catch (IOException ioex) {
			throw new UncheckedIOException(ioex);
		}

		return response;
	}

	protected Response buildResponse(HttpUriRequest req, CloseableHttpResponse httpResponse, OutputStream out,
			RequestListener requestListener) throws IOException {
		Response response = new Response();
		EnhancedList<URI> uris = chain.get();
		URI _uri_ = req.getURI();
		uris.add(0, _uri_);
		response.setChain(uris);
		response.setUri(_uri_);
		for (Header header : httpResponse.getAllHeaders()) {
			response.addHeader(header.getName(), header.getValue());
		}
		response.setStatusCode(httpResponse.getStatusLine().getStatusCode());
		response.setStatusText(httpResponse.getStatusLine().getReasonPhrase());
		response.setLocale(httpResponse.getLocale());
		HttpEntity entity = httpResponse.getEntity();
		if (entity != null) {
			{
				boolean buffer = false;
				if (out == null) {
					out = new ByteArrayOutputStream();
					buffer = true;
				}
				requestListener.start(_uri_, entity.getContentLength());
				try (RequestOutputStreamWrapper cout = new RequestOutputStreamWrapper(out, requestListener)) {
					IOUtils.copyLarge(entity.getContent(), cout);
					downloaded += cout.getBytesWritten();
				}
				requestListener.end();
				if (buffer) {
					response.setContent(ByteArrayOutputStream.class.cast(out).toByteArray());
				}
			}
			response.setContentLength(entity.getContentLength());
			response.setContentEncoding(
					entity.getContentEncoding() == null ? null : entity.getContentEncoding().getValue());
			Header contentTypeHeader = entity.getContentType();
			if (contentTypeHeader != null) {
				String contentType = contentTypeHeader.getValue();
				response.setContentType(contentType);
				if (StringUtils.isNotBlank(contentType)) {
					int charsetIndex = contentType.indexOf("charset=");
					if (charsetIndex != -1) {
						response.setCharset(contentType.substring("charset=".length() + charsetIndex));
					}
				}
			}
			response.setFilename(getName(uris.get(uris.size() - 1)));
		}
		try {
			Header dateHeader = httpResponse.getFirstHeader("Date");
			if (dateHeader != null && StringUtils.isNotBlank(String.valueOf(dateHeader))) {
				response.setDate(parseDate(dateHeader.getValue()));
			}
		} catch (ParseException ex) {
			//
		}
		return response;
	}

	public Response get(GetRequest get) {
		return execute(get, createGet(get), get.getOut(), get.getRequestListener());
	}

	public GetRequest createGet(Form form) {
		URI uri = isBlank(form.getAction()) ? form.getUrl() : URIUtils.resolve(form.getUrl(), form.getAction());
		GetRequest get = new GetRequest(uri);
		form.getInputElements().forEach(e -> get.addFormValue(e.getName(), e.getValue()));
		return get;
	}

	public HttpGet createGet(GetRequest get) {
		return new HttpGet(toFullUri(get));
	}

	public Response delete(DeleteRequest delete) {
		return execute(delete, createDelete(delete), delete.getOut(), delete.getRequestListener());
	}

	public HttpDelete createDelete(DeleteRequest delete) {
		return new HttpDelete(toFullUri(delete));
	}

	public Response put(PutRequest put) {
		return execute(put, createPut(put), put.getOut(), put.getRequestListener());
	}

	public HttpUriRequest createPut(PutRequest put) {
		RequestBuilder builder = RequestBuilder.put().setUri(put.getUri());
		if (put.getBody() != null) {
			builder.setEntity(new StringEntity(put.getBody(), ContentType.create(put.getMime(), charSet)));
		}
		if (!put.getFormValues().isEmpty()) {
			put.getFormValues().entrySet().stream().forEach(entry -> {
				entry.getValue().stream().filter(Objects::nonNull).forEach(value -> {
					builder.addParameter(entry.getKey(), value);
				});
			});
		}
		HttpUriRequest req = builder.build();
		return req;
	}

	public Response post(PostRequest post) {
		return execute(post, createPost(post), post.getOut(), post.getRequestListener());
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
			post.getAttachments().entrySet()
					.forEach(entry -> mb.addBinaryBody(entry.getKey(), entry.getValue().toFile()));
			post.getFormValues().entrySet().forEach(
					entry -> entry.getValue().stream().forEach(element -> mb.addTextBody(entry.getKey(), element)));
			req = new HttpPost(post.getUri());
			HttpEntity body = mb.build();
			HttpPost.class.cast(req).setEntity(body);
		} else if (post.getStream() != null) {
			req = new HttpPost(post.getUri());
			HttpEntity body = new InputStreamEntity(post.getStream().get());
			HttpPost.class.cast(req).setEntity(body);
		} else if (post.isUrlEncodedFormEntity()) {
			RequestBuilder builder = RequestBuilder.post().setUri(post.getUri());
			EnhancedList<NameValuePair> nvps = new EnhancedArrayList<>();
			post.getFormValues().entrySet().stream().filter(kv -> kv.getKey() != null)
					.forEach(kv -> kv.getValue().forEach(v -> nvps.add(new BasicNameValuePair(kv.getKey(), v))));
			try {
				builder.setEntity(new UrlEncodedFormEntity(nvps, "UTF-8"));
			} catch (UnsupportedEncodingException ex) {
				throw new RuntimeException(ex);
			}
			req = builder.build();
		} else {
			RequestBuilder builder = RequestBuilder.post().setUri(post.getUri());
			post.getFormValues().entrySet().stream().filter(kv -> kv.getKey() != null)
					.forEach(kv -> kv.getValue().forEach(v -> builder.addParameter(kv.getKey(), v)));
			req = builder.build();
		}
		return req;
	}

	public Response head(String head) {
		return head(new HeadRequest(head));
	}

	public Response head(HeadRequest head) {
		return execute(head, createHead(head), head.getOut(), head.getRequestListener());
	}

	public HttpHead createHead(HeadRequest head) {
		return new HttpHead(head.getUri());
	}

	public Response options(OptionsRequest options) {
		return execute(options, createOptions(options), options.getOut(), options.getRequestListener());
	}

	public HttpOptions createOptions(OptionsRequest options) {
		return new HttpOptions(options.getUri());
	}

	public Response trace(TraceRequest trace) {
		return execute(trace, createTrace(trace), trace.getOut(), trace.getRequestListener());
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
		URI uri = isBlank(form.getAction()) ? form.getUrl() : URIUtils.resolve(form.getUrl(), form.getAction());
		PostRequest post = new PostRequest(uri);
		form.getInputElements().stream().filter(e -> !(e instanceof FileInput))
				.forEach(e -> post.addFormValue(e.getName(), e.getValue()));
		form.getInputElements().stream().filter(e -> isNotBlank(e.getName())).filter(e -> isNotBlank(e.getValue()))
				.filter(e -> e instanceof FileInput).map(e -> FileInput.class.cast(e))
				.forEach(e -> post.addAttachment(e.getName(), new FilePath(e.getFile())));
		post.setName(form.getId());
		return post;
	}

	public Response get(String url) {
		return get(URI.create(url));
	}

	public String getContentString(String url) {
		return get(url).getContentString();
	}

	public String getContentString(URI url) {
		return get(url).getContentString();
	}

	public Response get(URI url) {
		return get(new GetRequest(url));
	}

	public Response get(URL url) {
		try {
			return get(url.toURI());
		} catch (URISyntaxException ex) {
			throw new IllegalArgumentException(String.valueOf(url));
		}
	}

	public Response get(Form form) {
		return get(createGet(form));
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
		redirectStrategy = null;
		requestConfig = null;
		authentication.clear();
	}

	protected void prepareRequest(AbstractRequest<? extends AbstractRequest<?>> params, HttpUriRequest req) {
		prepareRequest_accept(params, req);
		prepareRequest_additionalHeaders(params, req);
	}

	protected void prepareRequest_accept(AbstractRequest<? extends AbstractRequest<?>> params, HttpUriRequest req) {
		if (params != null && StringUtils.isNotBlank(params.getAccept())) {
			req.setHeader(HttpHeaders.ACCEPT, params.getAccept());
		}
		if (params != null && StringUtils.isNotBlank(params.getAcceptEncoding())) {
			req.setHeader(HttpHeaders.ACCEPT_ENCODING, params.getAcceptEncoding());
		}
		if (StringUtils.isNotBlank(acceptLanguage)) {
			req.setHeader(HttpHeaders.ACCEPT_LANGUAGE, acceptLanguage);
		} else {
			req.removeHeaders(HttpHeaders.ACCEPT_LANGUAGE);
		}
	}

	protected void prepareRequest_additionalHeaders(AbstractRequest<? extends AbstractRequest<?>> params,
			HttpUriRequest req) {
		if (params != null) {
			params//
					.getHeaders()//
					.entrySet()//
					.stream()//
					.filter(h -> h.getValue() != null)//
					.forEach(h -> req.setHeader(h.getKey(), String.valueOf(h.getValue())));
		}
	}

	public CookieStore getCookieStore() {
		if (cookieStore == null) {
			cookieStore = new DefaultCookieStore();
		}
		return this.cookieStore;
	}

	public DefaultCookieStore getCustomCookieStore() {
		return DefaultCookieStore.class.cast(getCookieStore());
	}

	public HTTPClient setCookieStore(DefaultCookieStore cookieStore) {
		this.cookieStore = cookieStore;
		return this;
	}

	public HTTPClient setRequestConfig(RequestConfig requestConfig) {
		this.requestConfig = requestConfig;
		return this;
	}

	public HTTPClient setRedirectStrategy(RedirectStrategy redirectStrategy) {
		this.redirectStrategy = redirectStrategy;
		return this;
	}

	public HTTPClient setConnectionManager(HttpClientConnectionManager connectionManager) {
		this.connectionManager = connectionManager;
		return this;
	}

	public void addAuthentication(HTTPClientAuth httpClientAuth) {
		this.authentication.add(httpClientAuth);
	}

	public HTTPClient resetAuthentication() {
		this.authentication.clear();
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
				defaultCredentialsProvider.setCredentials(authscope, createCredentials(auth));
			}
			if (!init) {
				defaultCredentialsProvider = new SystemDefaultCredentialsProvider();
			}
		}
		return this.defaultCredentialsProvider;
	}

	public HTTPClient setDefaultCredentialsProvider(CredentialsProvider credentialsProvider) {
		this.defaultCredentialsProvider = credentialsProvider;
		return this;
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
				preemptiveCredentialsProvider.setCredentials(authscope, createCredentials(auth));
			}
			if (!init) {
				preemptiveCredentialsProvider = null;
			}
		}
		return this.preemptiveCredentialsProvider;
	}

	protected Credentials createCredentials(HTTPClientAuth auth) {
		if (sharepoint) {
			return new NTCredentials(auth.getUser(), auth.getPass(), null, null);
		}
		return new UsernamePasswordCredentials(auth.getUser(), auth.getPass());
	}

	public HTTPClient setPreemptiveCredentialsProvider(CredentialsProvider credentialsProvider) {
		this.preemptiveCredentialsProvider = credentialsProvider;
		return this;
	}

	public HttpClientContext getContext(URI preemptiveCPBaseUrl) {
		this.preemptiveCPBaseUrl.set(preemptiveCPBaseUrl);
		return context.get();
	}

	public HTTPClient setHttpClient(CloseableHttpClient httpClient) {
		this.httpClient = httpClient;
		return this;
	}
}
