package org.jhaws.common.net.client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.util.HashSet;
import java.util.Set;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.jboss.resteasy.client.jaxrs.engines.ApacheHttpClient43Engine;
import org.jboss.resteasy.client.jaxrs.internal.LocalResteasyProviderFactory;
import org.jboss.resteasy.plugins.providers.RegisterBuiltin;
import org.jboss.resteasy.plugins.server.embedded.SecurityDomain;
import org.jboss.resteasy.plugins.server.sun.http.HttpContextBuilder;
import org.jboss.resteasy.spi.ResteasyProviderFactory;
import org.jhaws.common.net.resteasy.client.JsonProvider;

import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpServer;

public class TestRestServer implements AutoCloseable {
    public int port;

    public final String bindAddress = "localhost";

    public boolean chunked = true;

    public ResteasyClient resteasyClient;

    public ResteasyWebTarget resteasyWebTarget;

    public Set<Object> objects = new HashSet<>();

    public Set<Class<?>> classes = new HashSet<>();

    public HttpContextBuilder contextBuilder;

    public HttpServer httpServer;

    public SecurityDomain securityDomain;

    public HttpContext httpContext;

    private TestRestServer(Object... objects) {
        append(objects);
    }

    public static TestRestServer create(SecurityDomain securityDomain, Object... objects) throws IOException {
        TestRestServer inMemoryRestServer = new TestRestServer(objects);
        inMemoryRestServer.withDefaults(securityDomain);
        inMemoryRestServer.start();
        return inMemoryRestServer;
    }

    public static TestRestServer create(Object... objects) throws IOException {
        return create(null, objects);
    }

    private void append(Object... _objects) {
        for (Object object : _objects) {
            if (object instanceof Class) {
                classes.add((Class<?>) object);
            } else {
                this.objects.add(object);
            }
        }
    }

    private void start() throws IOException {
        port = findFreePort();
        httpServer = HttpServer.create(new InetSocketAddress(port), 10);
        contextBuilder = new HttpContextBuilder();
        for (Object object : objects) {
            contextBuilder.getDeployment().getResources().add(object);
        }
        for (Class<?> resourceClass : classes) {
            contextBuilder.getDeployment().getActualResourceClasses().add(resourceClass);
        }
        contextBuilder.setSecurityDomain(securityDomain);
        httpContext = contextBuilder.bind(httpServer);
        // context.getAttributes().put("some.config.info", "42");
        httpServer.start();
    }

    private void withDefaults(SecurityDomain _securityDomain) {
        this.securityDomain = _securityDomain;

        HttpClient httpClient = HttpClientBuilder.create().build();
        ResteasyProviderFactory resteasyProvider = new LocalResteasyProviderFactory();
        resteasyProvider.registerProviderInstance(new JsonProvider());
        RegisterBuiltin.register(resteasyProvider);
        resteasyClient = ((ResteasyClientBuilder) ResteasyClientBuilder.newBuilder())//
                .httpEngine(new ApacheHttpClient43Engine(httpClient, true))//
                .providerFactory(resteasyProvider)//
                .build();
        resteasyWebTarget = resteasyClient.target(baseUri());
        resteasyWebTarget.setChunked(chunked);
    }

    public String baseUri() {
        return "http://" + bindAddress + ":" + port;
    }

    public static int findFreePort() throws IOException {
        try (ServerSocket server = new ServerSocket(0)) {
            int port = server.getLocalPort();
            server.close();
            return port;
        }
    }

    @Override
    public void close() {
        contextBuilder.cleanup();
        httpServer.stop(0);
    }
}
