package org.jhaws.common.net.client.tests;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.URI;
import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.UriBuilder;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.jboss.resteasy.plugins.server.embedded.SecurityDomain;
import org.jboss.resteasy.plugins.server.tjws.TJWSEmbeddedJaxrsServer;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class NewHttpClientTest {
    private static InMemoryRestServer server;

    private static TestResource testResource = new TestResource();

    @BeforeClass
    public static void beforeClass() throws Exception {
        server = InMemoryRestServer.create(testResource);
    }

    @AfterClass
    public static void afterClass() throws Exception {
        server.close();
    }

    @Test
    public void test() {
        try {
            URI uri = UriBuilder.fromPath(server.baseUri()).path(TestResource.PATH).path(TestResource.GET).build();
            System.out.println(new ResteasyClientBuilder().build().target(uri).request().get().getEntity());

            NewHttpClient hc = new NewHttpClient();
            HttpResponse httpResponse = hc.get(uri);
            System.out.println(httpResponse.getStatusLine());
            System.out.println(new String(IOUtils.toByteArray(httpResponse.getEntity().getContent())));
        } catch (Exception e) {
            e.printStackTrace(System.out);
            Assert.fail(String.valueOf(e));
        }
    }

    @Path(TestResource.PATH)
    public static class TestResource {
        public static final String PATH = "testresource";

        public static final String GET = "get";

        @GET
        @Produces("text/plain")
        @Path(GET)
        public String get() {
            System.out.println("request get");
            return "get ok";
        }
    }

    public static class InMemoryRestServer implements AutoCloseable {
        private int port;

        private Set<Object> objects = new HashSet<Object>();

        private Set<Class<?>> classes = new HashSet<Class<?>>();

        private TJWSEmbeddedJaxrsServer server;

        private SecurityDomain securityDomain;

        private ResteasyClient resteasyClient;

        private String bindAddress = "localhost";

        private InMemoryRestServer(Object... objects) {
            append(objects);
        }

        public static InMemoryRestServer create(Object... objects) throws IOException {
            return create(null, objects);
        }

        public static InMemoryRestServer create(SecurityDomain securityDomain, Object... objects) throws IOException {
            InMemoryRestServer inMemoryRestServer = new InMemoryRestServer(objects);
            inMemoryRestServer.withDefaults(securityDomain);
            inMemoryRestServer.start();
            return inMemoryRestServer;
        }

        private void append(Object... objects) {
            for (Object object : objects) {
                if (object instanceof Class) {
                    classes.add((Class<?>) object);
                } else {
                    this.objects.add(object);
                }
            }
        }

        private void withDefaults(SecurityDomain securityDomain) {
            this.securityDomain = securityDomain;
            this.resteasyClient = new ResteasyClientBuilder().build();
        }

        private void start() throws IOException {
            port = findFreePort();
            server = new TJWSEmbeddedJaxrsServer();
            server.setPort(port);
            server.setBindAddress(bindAddress);
            server.setSecurityDomain(securityDomain);
            for (Object object : objects) {
                if (object instanceof Application) {
                    server.getDeployment().setApplication((Application) object);
                } else {
                    server.getDeployment().getResources().add(object);
                }
            }
            for (Class<?> resourceOrProvider : classes) {
                if (Application.class.isAssignableFrom(resourceOrProvider)) {
                    server.getDeployment().setApplicationClass(resourceOrProvider.getName());
                } else {
                    server.getDeployment().getProviderClasses().add(resourceOrProvider.getName());
                }
            }
            server.start();
        }

        public String baseUri() {
            return "http://" + bindAddress + ":" + port;
        }

        public ResteasyWebTarget newRequest(String uriTemplate) {
            return resteasyClient.target(baseUri() + uriTemplate);
        }

        public static int findFreePort() throws IOException {
            ServerSocket server = new ServerSocket(0);
            int port = server.getLocalPort();
            server.close();
            return port;
        }

        @Override
        public void close() {
            if (server != null) {
                server.stop();
                server = null;
            }
        }
    }
}
