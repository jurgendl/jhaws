package org.jhaws.common.net.client.latest;

import java.io.Serializable;
import java.net.URI;

public abstract class AbstractParams<T extends AbstractParams<? super T>> implements Serializable {
    private static final long serialVersionUID = -8834915649537196310L;

    protected URI uri;

    protected String accept;

    public AbstractParams() {
        super();
    }

    public AbstractParams(URI uri) {
        setUri(uri);
    }

    public AbstractParams(String uri) {
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
}