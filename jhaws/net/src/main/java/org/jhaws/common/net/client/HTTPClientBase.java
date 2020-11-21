package org.jhaws.common.net.client;

import static java.util.stream.StreamSupport.stream;

import java.io.Closeable;
import java.io.IOException;
import java.io.Serializable;
import java.io.UncheckedIOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;

import org.apache.http.client.utils.URIBuilder;
import org.jhaws.common.lang.EnhancedArrayList;
import org.jhaws.common.lang.EnhancedList;
import org.jhaws.common.lang.StringUtils;

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
@SuppressWarnings("serial")
public abstract class HTTPClientBase<X extends HTTPClientBase<? super X>> implements Closeable, Serializable {
	static {
		// fix javax.net.ssl.SSLException: SSL peer shut down incorrectly
		System.setProperty("https.protocols", "TLSv1,TLSv1.1,TLSv1.2");
	}

	public static final String CONTENT_LENGTH = "Content-Length";

	public static final String CONTENT_TYPE = "Content-Type";

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

	public static String getName(URI url) {
		String tmp = url.toString();
		int beginIndex = tmp.lastIndexOf("/") + 1;
		int endIndex = tmp.indexOf("?", beginIndex);
		if (endIndex == -1)
			endIndex = tmp.length();
		String n = tmp.substring(beginIndex, endIndex);
		if (n.length() == 0)
			n = null;
		return n;
	}

	/**
	 * Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:60.0) Gecko/20100101
	 * Firefox/60.0
	 */
	public static String FIREFOX = "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:60.0) Gecko/20100101 Firefox/60.0";

	/**
	 * Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like
	 * Gecko) Chrome/75.0.3770.80 Safari/537.36
	 */
	public static String CHROME = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.80 Safari/537.36";

	/**
	 * Mozilla/5.0 (Windows NT 10.0; WOW64; Trident/7.0; rv:11.0) like Gecko
	 */
	public static String IEXPLORER = "Mozilla/5.0 (Windows NT 10.0; WOW64; Trident/7.0; rv:11.0) like Gecko";

	/**
	 * Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like
	 * Gecko) Chrome/51.0.2704.79 Safari/537.36 Edge/14.14393
	 */
	public static String EDGE = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.79 Safari/537.36 Edge/14.14393";

	/**
	 * Mozilla/5.0 (Linux; U; Android 4.0.4; en-gb; GT-I9300 Build/IMM76D)
	 * AppleWebKit/534.30 (KHTML, like Gecko) Version/4.0 Mobile Safari/534.30
	 */
	public static String ANDROID_S3 = "Mozilla/5.0 (Linux; U; Android 4.0.4; en-gb; GT-I9300 Build/IMM76D) AppleWebKit/534.30 (KHTML, like Gecko) Version/4.0 Mobile Safari/534.30";

	// https://deviceatlas.com/blog/samsung-phones-user-agent-strings-list
	/**
	 * Mozilla/5.0 (Linux; Android 6.0.1; SM-G935S Build/MMB29K; wv)
	 * AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/55.0.2883.91
	 * Mobile Safari/537.36
	 */
	public static String ANDROID_S7E = "Mozilla/5.0 (Linux; Android 6.0.1; SM-G935S Build/MMB29K; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/55.0.2883.91 Mobile Safari/537.36";

	protected String charSet = StringUtils.UTF8;

	protected String userAgent = CHROME;

	protected ThreadLocal<URI> preemptiveCPBaseUrl = new ThreadLocal<>();

	protected boolean throwException = true;

	protected int timeout = 60 * 1000;

	protected boolean ntlm = false;

	// https://stackoverflow.com/questions/24752485/httpclient-javax-net-ssl-sslpeerunverifiedexception-peer-not-authenticated-when
	protected boolean disableSSLCheck = false;

	protected transient long downloaded;

	protected String acceptLanguage = "en, en-gb;q=0.9";

	protected boolean compressed = true;

	protected String cookieSpec;

	protected int maxRedirects = 5;

	protected boolean redirectsEnabled = true;

	protected boolean expectContinueEnabled = true;

	protected boolean circularRedirectsEnabled = true;

	protected String[] tlsVersions = { "TLSv1.3", "TLSv1.2" };

	protected final ThreadLocal<EnhancedList<URI>> chain = new ThreadLocal<EnhancedList<URI>>() {
		@Override
		protected EnhancedList<URI> initialValue() {
			return new EnhancedArrayList<>();
		};
	};

	public HTTPClientBase() {
		super();
	}

	public ThreadLocal<EnhancedList<URI>> getChain() {
		return this.chain;
	}

	@SuppressWarnings("unchecked")
	protected X castThis() {
		return (X) this;
	}

	public String getCharSet() {
		return this.charSet;
	}

	public X setCharSet(String charSet) {
		this.charSet = charSet;
		return castThis();
	}

	public String getUserAgent() {
		return this.userAgent;
	}

	public X setUserAgent(String userAgent) {
		this.userAgent = userAgent;
		return castThis();
	}

	public boolean isThrowException() {
		return this.throwException;
	}

	public X setThrowException(boolean throwException) {
		this.throwException = throwException;
		return castThis();
	}

	public int getTimeout() {
		return this.timeout;
	}

	public X setTimeout(int timeout) {
		this.timeout = timeout;
		return castThis();
	}

	public boolean isNTLM() {
		return this.ntlm;
	}

	public X setNTLM(boolean sharepoint) {
		this.ntlm = sharepoint;
		return castThis();
	}

	public boolean isDisableSSLCheck() {
		return this.disableSSLCheck;
	}

	public X setDisableSSLCheck(boolean disableSSLCheck) {
		this.disableSSLCheck = disableSSLCheck;
		return castThis();
	}

	public String getAcceptLanguage() {
		return this.acceptLanguage;
	}

	public X setAcceptLanguage(String acceptLanguage) {
		this.acceptLanguage = acceptLanguage;
		return castThis();
	}

	public boolean isCompressed() {
		return this.compressed;
	}

	public X setCompressed(boolean compressed) {
		this.compressed = compressed;
		return castThis();
	}

	public long getDownloaded() {
		return this.downloaded;
	}

	public String getCookieSpec() {
		return this.cookieSpec;
	}

	public X setCookieSpec(String cookieSpec) {
		this.cookieSpec = cookieSpec;
		return castThis();
	}

	protected SSLContext noOpSsl() {
		SSLContext sslcontext;
		try {
			sslcontext = SSLContext.getInstance("SSL");
		} catch (NoSuchAlgorithmException ex) {
			throw new UnsupportedOperationException(ex);
		}
		try {
			sslcontext.init(null, new TrustManager[] { new NoOpX509TrustManager() }, new SecureRandom());
		} catch (KeyManagementException ex) {
			throw new UnsupportedOperationException(ex);
		}
		return sslcontext;
	}

	protected void wrap(Throwable cause) throws UncheckedIOException, RuntimeException {
		if (cause instanceof RuntimeException) {
			throw RuntimeException.class.cast(cause);
		}
		if (cause instanceof IOException) {
			throw new UncheckedIOException(IOException.class.cast(cause));
		}
		throw new RuntimeException(cause);
	}

	public int getMaxRedirects() {
		return this.maxRedirects;
	}

	public X setMaxRedirects(int maxRedirects) {
		this.maxRedirects = maxRedirects;
		return castThis();
	}

	public boolean isRedirectsEnabled() {
		return this.redirectsEnabled;
	}

	public X setRedirectsEnabled(boolean redirectsEnabled) {
		this.redirectsEnabled = redirectsEnabled;
		return castThis();
	}

	public boolean isExpectContinueEnabled() {
		return this.expectContinueEnabled;
	}

	public X setExpectContinueEnabled(boolean expectContinueEnabled) {
		this.expectContinueEnabled = expectContinueEnabled;
		return castThis();
	}

	public boolean isCircularRedirectsEnabled() {
		return this.circularRedirectsEnabled;
	}

	public X setCircularRedirectsEnabled(boolean circularRedirectsEnabled) {
		this.circularRedirectsEnabled = circularRedirectsEnabled;
		return castThis();
	}

	public String[] getTlsVersions() {
		return this.tlsVersions;
	}

	public X setTlsVersions(String[] tlsVersions) {
		this.tlsVersions = tlsVersions;
		return castThis();
	}
}
