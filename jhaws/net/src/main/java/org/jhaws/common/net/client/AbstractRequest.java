package org.jhaws.common.net.client;

import java.io.OutputStream;
import java.io.Serializable;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractRequest<T extends AbstractRequest<? super T>> implements Serializable {
	private static final long serialVersionUID = -8834915649537196310L;

	protected URI uri;

	protected String accept;

	protected String acceptEncoding;

	protected Map<String, Object> headers = new HashMap<>();

	protected transient OutputStream out;

	protected transient RequestListener requestListener;

	protected String referer;

	public AbstractRequest() {
		super();
	}

	public AbstractRequest(URI uri) {
		setUri(uri);
	}

	public AbstractRequest(String uri) {
		setUri(uri);
	}

	@SuppressWarnings("unchecked")
	protected T cast() {
		return (T) this;
	}

	public URI getUri() {
		return uri;
	}

	public T setUri(URI uri) {
		this.uri = uri;
		return cast();
	}

	public T setUri(String uri) {
		this.uri = URI.create(uri);
		return cast();
	}

	public String getAccept() {
		return accept;
	}

	public T setAccept(String accept) {
		this.accept = accept;
		return cast();
	}

	public T addHeader(String key, Object value) {
		headers.put(key, value);
		return cast();
	}

	public T removeHeader(String key) {
		headers.remove(key);
		return cast();
	}

	@Override
	public String toString() {
		return "AbstractParams [uri=" + this.uri + ", accept=" + this.accept + "]";
	}

	public Map<String, Object> getHeaders() {
		return this.headers;
	}

	public T setHeaders(Map<String, Object> headers) {
		this.headers = headers;
		return cast();
	}

	public String getAcceptEncoding() {
		return this.acceptEncoding;
	}

	public T setAcceptEncoding(String acceptEncoding) {
		this.acceptEncoding = acceptEncoding;
		return cast();
	}

	public OutputStream getOut() {
		return this.out;
	}

	public T setOut(OutputStream out) {
		this.out = out;
		return cast();
	}

	public String getReferer() {
		return this.referer;
	}

	public T setReferer(String referer) {
		this.referer = referer;
		return cast();
	}

	public RequestListener getRequestListener() {
		return this.requestListener;
	}

	public T setRequestListener(RequestListener requestListener) {
		this.requestListener = requestListener;
		return cast();
	}
}