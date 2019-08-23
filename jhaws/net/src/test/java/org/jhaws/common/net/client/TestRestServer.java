package org.jhaws.common.net.client;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.core.Application;

import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.jboss.resteasy.plugins.server.embedded.SecurityDomain;
import org.jboss.resteasy.spi.ResteasyDeployment;

public class TestRestServer implements AutoCloseable {
    int port;

    Set<Object> objects = new HashSet<>();

    Set<Class<?>> classes = new HashSet<>();

    @SuppressWarnings("deprecation")
    org.jboss.resteasy.plugins.server.tjws.TJWSEmbeddedJaxrsServer server;

    SecurityDomain securityDomain;

    ResteasyClient resteasyClient;

    String bindAddress = "localhost";

    private TestRestServer(Object... objects) {
        append(objects);
    }

    public static TestRestServer create(Object... objects) throws IOException {
        return create(null, objects);
    }

    public static TestRestServer create(SecurityDomain securityDomain, Object... objects) throws IOException {
        TestRestServer inMemoryRestServer = new TestRestServer(objects);
        inMemoryRestServer.withDefaults(securityDomain);
        inMemoryRestServer.start();
        return inMemoryRestServer;
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

    private void withDefaults(SecurityDomain _securityDomain) {
        this.securityDomain = _securityDomain;
        this.resteasyClient = new ResteasyClientBuilder().build();
    }

    @SuppressWarnings("deprecation")
    private void start() throws IOException {
        port = findFreePort();
        server = new org.jboss.resteasy.plugins.server.tjws.TJWSEmbeddedJaxrsServer();
        server.setPort(port);
        server.setBindAddress(bindAddress);
        server.setSecurityDomain(securityDomain);
        System.out.println(bindAddress + ":" + port);
        ResteasyDeployment deployment = server.getDeployment();
        for (Object object : objects) {
            if (object instanceof Application) {
                deployment.setApplication((Application) object);
            } else {
                deployment.getResources().add(object);
            }
        }
        for (Class<?> resourceOrProvider : classes) {
            if (Application.class.isAssignableFrom(resourceOrProvider)) {
                deployment.setApplicationClass(resourceOrProvider.getName());
            } else {
                deployment.getProviderClasses().add(resourceOrProvider.getName());
            }
        }
        deployment.getProviderClasses().add(MatrixTestBeanMessageBodyReader.class.getName());
        server.start();
    }

    public String baseUri() {
        return "http://" + bindAddress + ":" + port;
    }

    public ResteasyWebTarget newRequest(String uriTemplate) {
        return resteasyClient.target(baseUri() + uriTemplate);
    }

    public static int findFreePort() throws IOException {
        try (ServerSocket server = new ServerSocket(0)) {
            int port = server.getLocalPort();
            server.close();
            return port;
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public void close() {
        if (server != null) {
            server.stop();
            server = null;
        }
    }
}
