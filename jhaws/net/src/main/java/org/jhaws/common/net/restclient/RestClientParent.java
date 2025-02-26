package org.jhaws.common.net.restclient;

import java.net.MalformedURLException;
import java.net.URI;
import java.util.Properties;
import java.util.function.Function;

import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.web.util.UriBuilder;

public abstract class RestClientParent {
    protected String base;

    protected String password;

    protected String user;

    private DefaultUriBuilderFactory defaultUriBuilderFactory = new DefaultUriBuilderFactory();

    public RestClientParent() {
        super();
    }

    public RestClientParent(Properties p) {
        init(p);
    }

    protected abstract void init(Properties p);

    public void setBase(String base) {
        this.base = base;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUser(String user) {
        this.user = user;
    }

    protected URI uri(Function<UriBuilder, URI> uriFunction) {
        java.net.URL b;
        try {
            b = new java.net.URL(base);
        } catch (MalformedURLException ex) {
            throw new RuntimeException(ex);
        }
        URI uri = uriFunction.apply(defaultUriBuilderFactory.builder().scheme(b.getProtocol()).host(b.getHost()).port(b.getPort()).path(b.getPath()));
        return uri;
    }

    protected abstract <B, R> R get(Function<UriBuilder, URI> uriFunction, RestConverter<R> elementTypeRef);

    protected abstract <B, R> R post(Function<UriBuilder, URI> uriFunction, B bodyValue, RestConverter<R> elementTypeRef);

    protected abstract <B, R> R put(Function<UriBuilder, URI> uriFunction, B bodyValue, RestConverter<R> elementTypeRef);

    protected abstract <B, R> R delete(Function<UriBuilder, URI> uriFunction, RestConverter<R> elementTypeRef);
}
