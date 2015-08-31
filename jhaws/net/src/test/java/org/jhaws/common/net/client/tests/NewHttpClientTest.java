package org.jhaws.common.net.client.tests;

import java.net.URI;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;

import org.jhaws.common.net.client.tests.HTTPClient.Response;
import org.jhaws.common.net.client.xml.XmlMarshalling;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @see https://dzone.com/articles/how-test-rest-api-junit
 * @see https://docs.jboss.org/resteasy/docs/3.0.9.Final/userguide/pdf/resteasy-reference-guide-en-US.pdf
 */
public class NewHttpClientTest {
    private static TestRestServer server;

    private static HTTPClient hc;

    private static TestResource testResource;
    
    private static XmlMarshalling xmlMarshalling;

    private UriBuilder getBase() {
        return UriBuilder.fromPath(server.baseUri()).path(TestResource.PATH);
    }

    @BeforeClass
    public static void beforeClass() throws Exception {
        hc = new HTTPClient();
        testResource = new TestResource();
        server = TestRestServer.create(testResource);
        xmlMarshalling = new XmlMarshalling(TestBody.class);
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
    public void test_getBody() {
        try {
            URI uri = getBase().path(TestResource.GET_BODY).build();
            TestBody rec = xmlMarshalling.unmarshall(TestBody.class, server.resteasyClient.target(uri).request().get(String.class), "UTF-8");
            TestBody hcr = xmlMarshalling.unmarshall(TestBody.class, hc.get(uri).getResponse());
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
        String rec = String.class.cast(testResource.delete);
        testResource.delete = null;
        hc.delete(uri);
        String hcr = String.class.cast(testResource.delete);
        Assert.assertEquals(rec, hcr);
    }

    @Test
    public void test_put() {
        URI uri = getBase().path(TestResource.PUT).build("putId");
        TestBody entity = new TestBody("putBody");
        Entity<TestBody> xmle = Entity.xml(entity);
        System.out.println(xmle);
        server.resteasyClient.target(uri).request().put(xmle);
        String rec1 = String.class.cast(testResource.put);
        TestBody rec2 = TestBody.class.cast(testResource.putBody);
        testResource.put = null;
        testResource.putBody = null;
        String xml = xmlMarshalling.marshall(entity, "UTF-8");
        System.out.println(xml);
        hc.put(uri, xml, MediaType.TEXT_XML, "UTF-8");
        String hcr1 = String.class.cast(testResource.put);
        TestBody hcr2 = TestBody.class.cast(testResource.putBody);
        Assert.assertEquals(rec1, hcr1);
        Assert.assertEquals(rec2, hcr2);
    }

    @Test
    public void test_head() {
        URI uri = getBase().path(TestResource.GET).build();
        Response head = hc.head(uri);
        Assert.assertNull(head.getResponse());
    }
}
