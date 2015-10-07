package org.jhaws.common.net.client.v45;

import java.io.Serializable;
import java.net.URI;

public abstract class AbstractRequest<T extends AbstractRequest<? super T>> implements Serializable {
	private static final long serialVersionUID = -8834915649537196310L;

	protected URI uri;

	protected String accept;

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

	@Override
	public String toString() {
		return "AbstractParams [uri=" + this.uri + ", accept=" + this.accept + "]";
	}
}