package org.jhaws.common.net.resteasy.client;

import java.net.URI;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScheme;
import org.apache.http.client.AuthCache;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.protocol.BasicHttpContext;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.jboss.resteasy.client.jaxrs.engines.ApacheHttpClient4Engine;
import org.jboss.resteasy.plugins.providers.RegisterBuiltin;
import org.jboss.resteasy.spi.ResteasyProviderFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NewRestEasyClient<R> {
    protected Logger logger = LoggerFactory.getLogger(RestEasyClient.class);

    private String serviceUrl;

    private final Class<R> resourceClass;

    private ResteasyWebTarget clientExecutor;

    private ResteasyProviderFactory resteasyProvider;

    public NewRestEasyClient(URI serviceUrl, Class<R> resourceClass) {
        this(serviceUrl.toASCIIString(), resourceClass);
    }

    public NewRestEasyClient(String serviceUrl, Class<R> resourceClass) {
        this(resourceClass);
        this.serviceUrl = serviceUrl;
    }

    public NewRestEasyClient(Class<R> resourceClass) {
        this.resourceClass = resourceClass;
    }

    public R proxy() {
        if (!getResourceClass().isInterface()) {
            throw new RuntimeException("must be an interface");
        }
        return getClientExecutor().proxy(resourceClass);
    }

    public String getServiceUrl() {
        return this.serviceUrl;
    }

    public void setServiceUrl(String serviceUrl) {
        this.serviceUrl = serviceUrl;
    }

    public Class<R> getResourceClass() {
        return this.resourceClass;
    }

    public ResteasyWebTarget getClientExecutor() {
        if (clientExecutor == null) {
            AuthCache authCache = new BasicAuthCache();
            AuthScheme basicAuth = new BasicScheme();
            authCache.put(new HttpHost(getServiceUrl()), basicAuth);
            BasicHttpContext localContext = new BasicHttpContext();
            localContext.setAttribute(URI.create(getServiceUrl()).getHost(), authCache);
            HttpClient httpClient = HttpClientBuilder.create().build();
            ResteasyClient client = new ResteasyClientBuilder()//
                    .httpEngine(new ApacheHttpClient4Engine(httpClient, true))//
                    .providerFactory(getResteasyProvider())//
                    .build();
            ResteasyWebTarget target = client.target(getServiceUrl());
            return target;
        }
        return this.clientExecutor;
    }

    public ResteasyProviderFactory getResteasyProvider() {
        if (resteasyProvider == null) {
            resteasyProvider = new ResteasyProviderFactory();
            resteasyProvider.registerProviderInstance(new JsonProvider());
            RegisterBuiltin.register(resteasyProvider);
        }
        return this.resteasyProvider;
    }

    public void setResteasyProvider(ResteasyProviderFactory resteasyProvider) {
        this.resteasyProvider = resteasyProvider;
    }

    public static <T> String getFilename(Response response) {
        try {
            String disposition = response.getHeaders().get(HttpHeaders.CONTENT_DISPOSITION).get(0).toString();
            return disposition.split(";")[1].split("=")[1].replace("\"", "").trim();
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static <T> Long getFileSize(Response response) {
        try {
            return Long.parseLong(response.getHeaders().get(HttpHeaders.CONTENT_LENGTH).get(0).toString());
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public void setClientExecutor(ResteasyWebTarget clientExecutor) {
        this.clientExecutor = clientExecutor;
    }
}
