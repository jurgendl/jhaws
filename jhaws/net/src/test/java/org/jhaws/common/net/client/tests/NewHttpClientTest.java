package org.jhaws.common.net.client.tests;

import java.net.URI;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @see https://dzone.com/articles/how-test-rest-api-junit
 * @see https://docs.jboss.org/resteasy/docs/3.0.9.Final/userguide/pdf/resteasy-reference-guide-en-US.pdf
 */
public class NewHttpClientTest {
    private static InMemoryRestServer server;

    private static HTTPClient hc;

    private static TestResource testResource;

    @BeforeClass
    public static void beforeClass() throws Exception {
        hc = new HTTPClient();
        testResource = new TestResource();
        server = InMemoryRestServer.create(testResource);
    }

    @AfterClass
    public static void afterClass() throws Exception {
        server.close();
    }

    @Test
    public void test_get() {
        try {
            URI uri = getBase().path(TestResource.GET).build();
            String rec = server.resteasyClient.target(uri).request().get(String.class);
            String hcr = new String(hc.get(uri).getResponse());
            Assert.assertEquals(rec, hcr);
        } catch (Exception e) {
            e.printStackTrace(System.out);
            Assert.fail(String.valueOf(e));
        }
    }

    @Test
    public void test_getWithParams() {
        try {
            URI uri = getBase().path(TestResource.GET_WITH_PARAMS).build("pathValue");
            String rec = server.resteasyClient.target(uri).request().get(String.class);
            String hcr = new String(hc.get(uri).getResponse());
            Assert.assertEquals(rec, hcr);
        } catch (Exception e) {
            e.printStackTrace(System.out);
            Assert.fail(String.valueOf(e));
        }
    }

    @Test
    public void test_getWithQuery() {
        try {
            URI uri = getBase().path(TestResource.GET_WITH_QUERY).queryParam(TestResource.QUERY_PARAM, "queryValue").build();
            String rec = server.resteasyClient.target(uri).request().get(String.class);
            String hcr = new String(hc.get(uri).getResponse());
            Assert.assertEquals(rec, hcr);
        } catch (Exception e) {
            e.printStackTrace(System.out);
            Assert.fail(String.valueOf(e));
        }
    }

    @Test
    public void test_delete() {
        URI uri = getBase().path(TestResource.DELETE).build("deleteId");
        server.resteasyClient.target(uri).request().delete();
        Object rec = testResource.delete;
        testResource.delete = null;
        hc.delete(uri);
        Object hcr = testResource.delete;
        Assert.assertEquals(rec, hcr);
    }

    @Test
    public void test_put() {
        URI uri = getBase().path(TestResource.PUT).build("putId");
        // server.resteasyClient.target(uri).request().put(Entity.xml(new TestBody("putBody")));
        Object rec1 = testResource.put;
        Object rec2 = testResource.putBody;
        testResource.put = null;
        testResource.putBody = null;
        hc.put(uri, HTTPClientUtils.marshall(new TestBody("putBody"), "UTF-8"), MediaType.TEXT_XML, "UTF-8");
        Object hcr1 = testResource.put;
        Object hcr2 = testResource.putBody;
        Assert.assertEquals(rec1, hcr1);
        Assert.assertEquals(rec2, hcr2);
    }

    private UriBuilder getBase() {
        return UriBuilder.fromPath(server.baseUri()).path(TestResource.PATH);
    }
}
