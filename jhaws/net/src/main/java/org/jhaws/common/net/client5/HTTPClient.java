package org.jhaws.common.net.client5;

import static java.util.stream.StreamSupport.stream;
import static org.apache.commons.io.IOUtils.copyLarge;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.time.DateUtils.parseDate;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UncheckedIOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.text.ParseException;
import java.util.Arrays;

import javax.annotation.PreDestroy;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;

import org.apache.hc.client5.http.auth.AuthCache;
import org.apache.hc.client5.http.auth.AuthSchemeProvider;
import org.apache.hc.client5.http.auth.AuthSchemes;
import org.apache.hc.client5.http.auth.AuthScope;
import org.apache.hc.client5.http.auth.Credentials;
import org.apache.hc.client5.http.auth.CredentialsProvider;
import org.apache.hc.client5.http.auth.NTCredentials;
import org.apache.hc.client5.http.auth.UsernamePasswordCredentials;
import org.apache.hc.client5.http.classic.methods.HttpDelete;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpHead;
import org.apache.hc.client5.http.classic.methods.HttpOptions;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.classic.methods.HttpPut;
import org.apache.hc.client5.http.classic.methods.HttpTrace;
import org.apache.hc.client5.http.classic.methods.HttpUriRequest;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.cookie.CookieSpecs;
import org.apache.hc.client5.http.entity.UrlEncodedFormEntity;
import org.apache.hc.client5.http.entity.mime.HttpMultipartMode;
import org.apache.hc.client5.http.entity.mime.MultipartEntityBuilder;
import org.apache.hc.client5.http.impl.DefaultRedirectStrategy;
import org.apache.hc.client5.http.impl.auth.BasicAuthCache;
import org.apache.hc.client5.http.impl.auth.BasicCredentialsProvider;
import org.apache.hc.client5.http.impl.auth.BasicScheme;
import org.apache.hc.client5.http.impl.auth.SystemDefaultCredentialsProvider;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.client5.http.io.HttpClientConnectionManager;
import org.apache.hc.client5.http.protocol.HttpClientContext;
import org.apache.hc.client5.http.protocol.RedirectStrategy;
import org.apache.hc.client5.http.ssl.HttpsSupport;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactory;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactoryBuilder;
import org.apache.hc.client5.http.utils.URIUtils;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.Header;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.HttpHeaders;
import org.apache.hc.core5.http.HttpHost;
import org.apache.hc.core5.http.HttpRequest;
import org.apache.hc.core5.http.HttpResponse;
import org.apache.hc.core5.http.HttpStatus;
import org.apache.hc.core5.http.NameValuePair;
import org.apache.hc.core5.http.ProtocolException;
import org.apache.hc.core5.http.config.RegistryBuilder;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.InputStreamEntity;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.apache.hc.core5.http.message.BasicNameValuePair;
import org.apache.hc.core5.http.protocol.HttpContext;
import org.apache.hc.core5.net.URIBuilder;
import org.apache.hc.core5.util.Timeout;
import org.jhaws.common.io.FilePath;
import org.jhaws.common.lang.EnhancedArrayList;
import org.jhaws.common.lang.EnhancedList;
import org.jhaws.common.lang.StringUtils;
import org.jhaws.common.net.NetHelper;
import org.jhaws.common.net.client.AbstractGetRequest;
import org.jhaws.common.net.client.AbstractRequest;
import org.jhaws.common.net.client.DeleteRequest;
import org.jhaws.common.net.client.FileInput;
import org.jhaws.common.net.client.Form;
import org.jhaws.common.net.client.GetRequest;
import org.jhaws.common.net.client.HeadRequest;
import org.jhaws.common.net.client.HttpException;
import org.jhaws.common.net.client.NoOpX509TrustManager;
import org.jhaws.common.net.client.OptionsRequest;
import org.jhaws.common.net.client.PostRequest;
import org.jhaws.common.net.client.PutRequest;
import org.jhaws.common.net.client.RequestListener;
import org.jhaws.common.net.client.RequestListenerAdapter;
import org.jhaws.common.net.client.RequestOutputStreamWrapper;
import org.jhaws.common.net.client.Response;
import org.jhaws.common.net.client.TraceRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
	static {
		// fix javax.net.ssl.SSLException: SSL peer shut down incorrectly
		System.setProperty("https.protocols", "TLSv1,TLSv1.1,TLSv1.2");
	}

	protected static Logger logger = LoggerFactory.getLogger(HTTPClient.class);

	/**
	 * Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:60.0) Gecko/20100101
	 * Firefox/60.0
	 */
	public static final String FIREFOX = "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:60.0) Gecko/20100101 Firefox/60.0";

	/**
	 * Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like
	 * Gecko) Chrome/75.0.3770.80 Safari/537.36
	 */
	public static final String CHROME = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.80 Safari/537.36";

	/**
	 * Mozilla/5.0 (Windows NT 10.0; WOW64; Trident/7.0; rv:11.0) like Gecko
	 */
	public static final String IEXPLORER = "Mozilla/5.0 (Windows NT 10.0; WOW64; Trident/7.0; rv:11.0) like Gecko";

	/**
	 * Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like
	 * Gecko) Chrome/51.0.2704.79 Safari/537.36 Edge/14.14393
	 */
	public static final String EDGE = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.79 Safari/537.36 Edge/14.14393";

	/**
	 * Mozilla/5.0 (Linux; U; Android 4.0.4; en-gb; GT-I9300 Build/IMM76D)
	 * AppleWebKit/534.30 (KHTML, like Gecko) Version/4.0 Mobile Safari/534.30
	 */
	public static final String ANDROID_S3 = "Mozilla/5.0 (Linux; U; Android 4.0.4; en-gb; GT-I9300 Build/IMM76D) AppleWebKit/534.30 (KHTML, like Gecko) Version/4.0 Mobile Safari/534.30";

	// https://deviceatlas.com/blog/samsung-phones-user-agent-strings-list
	/**
	 * Mozilla/5.0 (Linux; Android 6.0.1; SM-G935S Build/MMB29K; wv)
	 * AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/55.0.2883.91
	 * Mobile Safari/537.36
	 */
	public static final String ANDROID_S7E = "Mozilla/5.0 (Linux; Android 6.0.1; SM-G935S Build/MMB29K; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/55.0.2883.91 Mobile Safari/537.36";

	protected String charSet = StringUtils.UTF8;

	protected transient CloseableHttpClient httpClient;

	// protected transient
	// org.apache.http.impl.nio.client.CloseableHttpAsyncClient asyncHttpclient;

	protected transient RequestConfig requestConfig;

	protected transient RedirectStrategy redirectStrategy;

	protected transient EnhancedList<HTTPClientAuth> authentication = new EnhancedArrayList<>();

	protected String userAgent = CHROME;

	protected org.jhaws.common.net.client5.CookieStore cookieStore;

	protected final ThreadLocal<URI> preemptiveCPBaseUrl = new ThreadLocal<>();

	protected boolean throwException = true;

	protected int timeout = 60 * 1000;

	protected boolean sharepoint = false;

	protected boolean disableSSLCheck = false;

	protected String cookieSpec = CookieSpecs.STANDARD.ident;

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
				HttpHost targetHost = new HttpHost(_preemptiveCPBaseUrl.getScheme(), _preemptiveCPBaseUrl.getHost(),
						_preemptiveCPBaseUrl.getPort());
				authCache.put(targetHost, basicAuth);
				_context.setAuthCache(authCache);
			}
			return _context;
		};
	};

	protected HttpClientConnectionManager connectionManager;

	protected transient long downloaded;

	protected String acceptLanguage = "en, en-gb;q=0.9";

	protected final boolean compressed;

	protected final boolean ssl;

	protected transient CredentialsProvider defaultCredentialsProvider;

	protected transient CredentialsProvider preemptiveCredentialsProvider;

	public HTTPClient() {
		this(false, true);
	}

	public HTTPClient(boolean ssl, boolean compressed) {
		this.ssl = ssl;
		this.compressed = compressed;
	}

	public String getUserAgent() {
		return userAgent;
	}

	public HTTPClient setUserAgent(String userAgent) {
		this.userAgent = userAgent;
		return this;
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
				.setMaxRedirects(5)//
				.setCircularRedirectsAllowed(true)//
				.setConnectionRequestTimeout(Timeout.ofMilliseconds(timeout))//
				.setConnectTimeout(Timeout.ofMilliseconds(timeout))//
				// .setSocketTimeout(Timeout.ofMilliseconds(timeout))//
				.setExpectContinueEnabled(true)//
				.setRedirectsEnabled(true)//
				// https://stackoverflow.com/questions/36473478/fixing-httpclient-warning-invalid-expires-attribute-using-fluent-api/40697322
				.setCookieSpec(cookieSpec)//
		// .setCookieSpec(org.apache.http.client.params.CookiePolicy.BROWSER_COMPATIBILITY)//
		;
		if (sharepoint) {
			requestConfigBuilder//
					.setTargetPreferredAuthSchemes(Arrays.asList(AuthSchemes.NTLM.ident))//
					.setCookieSpec(cookieSpec)//
			;
		}
		requestConfigBuilder = requestConfigBuilder.setContentCompressionEnabled(compressed);
		return requestConfigBuilder;
	}

	protected final ThreadLocal<EnhancedList<URI>> chain = new ThreadLocal<EnhancedList<URI>>() {
		@Override
		protected EnhancedList<URI> initialValue() {
			return new EnhancedArrayList<>();
		};
	};

	public RedirectStrategy getRedirectStrategy() {
		if (redirectStrategy == null) {
			redirectStrategy = new DefaultRedirectStrategy() {
				@Override
				public URI getLocationURI(HttpRequest request, HttpResponse response, HttpContext context)
						throws org.apache.hc.core5.http.HttpException {
					URI redirect = super.getLocationURI(request, response, context);
					if (chain != null)
						chain.get().add(redirect);
					return redirect;
				}

				@Override
				public boolean isRedirected(HttpRequest request, HttpResponse response, HttpContext context)
						throws ProtocolException {
					if (response == null) {
						throw new IllegalArgumentException("HTTP response may not be null");
					}

					int statusCode = response.getCode();
					String method = request.getMethod();
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

	public CloseableHttpClient getHttpClient() {
		if (httpClient == null) {
			httpClient = getHttpClientBuilder().build();//
		}
		return httpClient;
	}

	@SuppressWarnings("deprecation")
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
							.register(AuthSchemes.NTLM.ident, new JCIFSNTLMSchemeFactory())//
							.build()//
			);
		}
		if (!compressed) {
			httpClientBuilder.disableContentCompression();
		}
		if (ssl) {
			// Trust own CA and all self-signed certs
			SSLContext sslcontext;
			try {
				sslcontext = SSLContext.getDefault();
			} catch (NoSuchAlgorithmException ex) {
				throw new RuntimeException(ex);
			}
			// alt: SSLContext.getInstance("SSL")

			// https://stackoverflow.com/questions/24752485/httpclient-javax-net-ssl-sslpeerunverifiedexception-peer-not-authenticated-when
			if (disableSSLCheck) {
				try {
					sslcontext.init(null, new TrustManager[] { new NoOpX509TrustManager() }, new SecureRandom());
				} catch (KeyManagementException ex) {
					throw new RuntimeException(ex);
				}
			}

			SSLConnectionSocketFactory sslsf;
			if (disableSSLCheck) {
				sslsf = new SSLConnectionSocketFactory(//
						sslcontext, //
						new String[] { "TLSv1", "TLSv1.1", "TLSv1.2" }, // supportedProtocols
						null, // supportedCipherSuites
						// https://stackoverflow.com/questions/24752485/httpclient-javax-net-ssl-sslpeerunverifiedexception-peer-not-authenticated-when
						org.apache.http.conn.ssl.AllowAllHostnameVerifier.INSTANCE // hostnameVerifier
				);
			} else {
				// Allow TLSv1 protocol only
				// FIXME
				// new String[] { "TLSv1", "TLSv1.1", "TLSv1.2" }, //
				// supportedProtocols
				sslsf = SSLConnectionSocketFactoryBuilder.create()//
						.setSslContext(sslcontext)//
						.setHostnameVerifier(HttpsSupport.getDefaultHostnameVerifier())//
						.build();
			}
			// FIXME httpClientBuilder.setSSLSocketFactory(sslsf);
			throw new UnsupportedOperationException();
		}
		return httpClientBuilder;
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

	public static URI toFullUri(AbstractGetRequest<? extends AbstractGetRequest<?>> get) {
		URIBuilder uriBuilder = new URIBuilder(get.getUri());
		stream(get.getFormValues().entrySet().spliterator(), false)//
				.filter(e -> StringUtils.isNotBlank(e.getKey()))//
				.forEach(e -> e.getValue().forEach(i -> uriBuilder.addParameter(e.getKey(), i)));
		try {
			return uriBuilder.build();
		} catch (URISyntaxException e1) {
			throw new RuntimeException(e1);
		}
	}

	public Response execute(AbstractRequest<? extends AbstractRequest<?>> params, HttpUriRequest req) {
		return execute(params, req, null, null);
	}

	protected URI uri;

	public Response execute(AbstractRequest<? extends AbstractRequest<?>> params, HttpUriRequest req, OutputStream out,
			RequestListener requestListener) {
		if (params != null) {
			prepareRequest(params, req);
		}

		chain.get().clear();

		URI uri;
		try {
			uri = req.getUri();
		} catch (URISyntaxException ex) {
			throw new RuntimeException(ex);
		}
		logger.trace("{}", uri);

		if (StringUtils.isNotBlank(params.getReferer())) {
			req.addHeader("Referer", params.getReferer());
		} else {
			req.addHeader("Referer", uri.toASCIIString());
		}

		HttpHost targetHost = new HttpHost(uri.getScheme(), uri.getHost(), uri.getPort());
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

	protected Response buildResponse(HttpUriRequest req, CloseableHttpResponse httpResponse, OutputStream out,
			RequestListener requestListener) throws IOException {
		Response response = new Response();
		EnhancedList<URI> uris = chain.get();
		URI _uri_;
		try {
			_uri_ = req.getUri();
		} catch (URISyntaxException ex1) {
			throw new RuntimeException(ex1);
		}
		uris.add(0, _uri_);
		response.setChain(uris);
		response.setUri(_uri_);
		Arrays.stream(httpResponse.getHeaders())
				.forEach(header -> response.addHeader(header.getName(), header.getValue()));
		response.setStatusCode(httpResponse.getCode());
		response.setStatusText(httpResponse.getReasonPhrase());
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
					copyLarge(entity.getContent(), cout);
					downloaded += cout.getBytesWritten();
				}
				requestListener.end();
				if (buffer) {
					response.setContent(ByteArrayOutputStream.class.cast(out).toByteArray());
				}
			}
			response.setContentLength(entity.getContentLength());
			response.setContentEncoding(entity.getContentEncoding());
			String contentType = entity.getContentType();
			if (contentType != null) {
				response.setContentType(contentType);
				if (StringUtils.isNotBlank(contentType)) {
					int charsetIndex = contentType.indexOf("charset=");
					if (charsetIndex != -1) {
						response.setCharset(contentType.substring("charset=".length() + charsetIndex));
					}
				}
			}
			response.setFilename(NetHelper.getName(uris.get(uris.size() - 1)));
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

	public ThreadLocal<EnhancedList<URI>> getChain() {
		return this.chain;
	}

	public Response get(GetRequest get) {
		return execute(get, createGet(get), get.getOut(), get.getRequestListener());
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
		HttpPut httpPut = new HttpPut(put.getUri());
		if (put.getBody() != null) {
			httpPut.setEntity(new StringEntity(put.getBody(), ContentType.create(put.getMime(), charSet)));
		}
		// FIXME
		if (!put.getFormValues().isEmpty()) {
			EnhancedList<NameValuePair> nvps = new EnhancedArrayList<>();
			put.getFormValues().entrySet().stream().filter(kv -> kv.getKey() != null)
					.forEach(kv -> kv.getValue().forEach(v -> nvps.add(new BasicNameValuePair(kv.getKey(), v))));
			httpPut.setEntity(new UrlEncodedFormEntity(nvps));
		}
		return httpPut;
	}

	public Response post(PostRequest post) {
		return execute(post, createPost(post), post.getOut(), post.getRequestListener());
	}

	public HttpUriRequest createPost(PostRequest post) {
		HttpUriRequest req;
		if (post.getBody() != null) {
			req = new HttpPost(post.getUri());
			req.setEntity(new StringEntity(post.getBody(), ContentType.create(post.getMime(), charSet)));
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
			InputStream inputStream = post.getStream().get();
			HttpEntity body = new InputStreamEntity(inputStream, ContentType.APPLICATION_OCTET_STREAM); // FIXME
			HttpPost.class.cast(req).setEntity(body);
		} else if (post.isUrlEncodedFormEntity()) {
			HttpPost httpPost = new HttpPost(post.getUri());
			EnhancedList<NameValuePair> nvps = new EnhancedArrayList<>();
			post.getFormValues().entrySet().stream().filter(kv -> kv.getKey() != null)
					.forEach(kv -> kv.getValue().forEach(v -> nvps.add(new BasicNameValuePair(kv.getKey(), v))));
			httpPost.setEntity(new UrlEncodedFormEntity(nvps));
			req = httpPost;
		} else {
			// FIXME
			HttpPost httpPost = new HttpPost(post.getUri());
			EnhancedList<NameValuePair> nvps = new EnhancedArrayList<>();
			post.getFormValues().entrySet().stream().filter(kv -> kv.getKey() != null)
					.forEach(kv -> kv.getValue().forEach(v -> nvps.add(new BasicNameValuePair(kv.getKey(), v))));
			httpPost.setEntity(new UrlEncodedFormEntity(nvps));
			req = httpPost;
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
		form.getInputElements().stream().filter(e -> StringUtils.isNotBlank(e.getName()))
				.filter(e -> StringUtils.isNotBlank(e.getValue())).filter(e -> e instanceof FileInput)
				.map(e -> FileInput.class.cast(e))
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

	public GetRequest createGet(Form form) {
		URI uri = isBlank(form.getAction()) ? form.getUrl() : URIUtils.resolve(form.getUrl(), form.getAction());
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
		// prepareRequest_singleCookieHeader(params, req);
		prepareRequest_accept(params, req);
		prepareRequest_additionalHeaders(params, req);
	}

	// @SuppressWarnings("deprecation")
	// protected void prepareRequest_singleCookieHeader(AbstractRequest<?
	// extends
	// AbstractRequest<?>> params, HttpUriRequest req) {
	// req.setHeader(org.apache.http.cookie.params.CookieSpecPNames.SINGLE_COOKIE_HEADER,
	// Boolean.TRUE.toString());
	// }

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

	public org.jhaws.common.net.client5.CookieStore getCookieStore() {
		if (cookieStore == null) {
			cookieStore = new org.jhaws.common.net.client5.CookieStore();
		}
		return this.cookieStore;
	}

	public org.jhaws.common.net.client5.CookieStore getCustomCookieStore() {
		return org.jhaws.common.net.client5.CookieStore.class.cast(getCookieStore());
	}

	public HTTPClient setCookieStore(org.jhaws.common.net.client5.CookieStore cookieStore) {
		this.cookieStore = cookieStore;
		return this;
	}

	public long getDownloaded() {
		return downloaded;
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

	public String getAcceptLanguage() {
		return this.acceptLanguage;
	}

	public HTTPClient setAcceptLanguage(String acceptLanguage) {
		this.acceptLanguage = acceptLanguage;
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
					authscope = new AuthScope(new HttpHost(auth.getHost(), auth.getPort()), auth.getRealm(),
							auth.getScheme());
				} else if (StringUtils.isNotBlank(auth.getRealm())) {
					// FIXME
					// authscope = new AuthScope(new HttpHost(auth.getHost(),
					// auth.getPort()), auth.getRealm());
					throw new UnsupportedOperationException();
				} else {
					authscope = new AuthScope(auth.getHost(), auth.getPort());
				}
				BasicCredentialsProvider.class.cast(defaultCredentialsProvider).setCredentials(authscope,
						createCredentials(auth));
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
					authscope = new AuthScope(new HttpHost(auth.getHost(), auth.getPort()), auth.getRealm(),
							auth.getScheme());
				} else if (StringUtils.isNotBlank(auth.getRealm())) {
					// FIXME
					// authscope = new AuthScope(new HttpHost(auth.getHost(),
					// auth.getPort()), auth.getRealm());
					throw new UnsupportedOperationException();
				} else {
					authscope = new AuthScope(auth.getHost(), auth.getPort());
				}
				BasicCredentialsProvider.class.cast(preemptiveCredentialsProvider).setCredentials(authscope,
						createCredentials(auth));
			}
			if (!init) {
				preemptiveCredentialsProvider = null;
			}
		}
		return this.preemptiveCredentialsProvider;
	}

	protected Credentials createCredentials(HTTPClientAuth auth) {
		if (sharepoint) {
			return new NTCredentials(auth.getUser(), auth.getPass().toCharArray(), null, null);
		}
		return new UsernamePasswordCredentials(auth.getUser(), auth.getPass().toCharArray());
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

	public boolean isThrowException() {
		return this.throwException;
	}

	public HTTPClient setThrowException(boolean throwException) {
		this.throwException = throwException;
		return this;
	}

	public int getTimeout() {
		return this.timeout;
	}

	public HTTPClient setTimeout(int timeout) {
		this.timeout = timeout;
		return this;
	}

	public boolean isSharepoint() {
		return this.sharepoint;
	}

	public HTTPClient setSharepoint(boolean sharepoint) {
		this.sharepoint = sharepoint;
		return this;
	}

	public boolean isDisableSSLCheck() {
		return this.disableSSLCheck;
	}

	public HTTPClient setDisableSSLCheck(boolean disableSSLCheck) {
		this.disableSSLCheck = disableSSLCheck;
		return this;
	}

	public String getCookieSpec() {
		return this.cookieSpec;
	}

	public HTTPClient setCookieSpec(String cookieSpec) {
		this.cookieSpec = cookieSpec;
		return this;
	}
}
