package org.jhaws.common.net.client;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonString;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;
import javax.ws.rs.core.UriBuilder;

import org.apache.commons.io.IOUtils;
import org.apache.http.impl.client.HttpClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.jboss.resteasy.client.jaxrs.engines.ApacheHttpClient43Engine;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataOutput;
import org.jhaws.common.io.FilePath;
import org.jhaws.common.net.resteasy.MultipartFormDataOutputEntity;
import org.jhaws.common.net.resteasy.client.RestEasyClient;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class StreamHttpClientTest {
    private static TestRestServer server;

    private static HTTPClient hc;

    private static StreamingResource testResource;

    private static StreamingResourceI proxy;

    private UriBuilder getBase() {
        return UriBuilder.fromPath(server.baseUri()).path(StreamingResourceI.PATH);
    }

    @BeforeClass
    public static void beforeClass() throws Exception {
        try {
            hc = new HTTPClient();
            testResource = new StreamingResource();
            FilePath fp = new FilePath("pom.xml");
            testResource.data.put("pom.xml", fp.readAllBytes());
            testResource.len.put("pom.xml", fp.getFileSize());
            server = TestRestServer.create(testResource);
            proxy = new RestEasyClient<>(server.baseUri(), StreamingResourceI.class).proxy();
        } catch (RuntimeException ex) {
            ex.printStackTrace();
            throw ex;
        }
    }

    @AfterClass
    public static void afterClass() throws Exception {
        try {
            server.close();
        } catch (Exception ex) {
            //
        }
        try {
            hc.close();
        } catch (Exception ex) {
            //
        }
    }

    @Test
    public void testUploadForm() {
        try {
            String url = getBase().build().toASCIIString() + StreamingResource.UPLOAD_FORM;
            ResteasyClient client = client();
            ResteasyWebTarget target = client.target(url);
            MultipartFormDataOutput mdo = new MultipartFormDataOutput();
            FilePath fp = new FilePath("pom.xml");
            mdo.addFormData("attachment", fp.newBufferedInputStream(), MediaType.APPLICATION_OCTET_STREAM_TYPE, fp.getFileNameString());
            Response r = target.request().post(MultipartFormDataOutputEntity.entity(mdo));
            Assert.assertEquals(200, r.getStatus());
        } catch (Exception ex) {
            ex.printStackTrace();
            Assert.fail();
        }
    }

    @Test
    public void testListProxy() {
        try {
            ResteasyClient client = client();
            ResteasyWebTarget target = client.target(server.baseUri());
            StreamingResourceI simple = target.proxy(StreamingResourceI.class);
            System.out.println(simple.list());
        } catch (Exception ex) {
            ex.printStackTrace();
            Assert.fail();
        }
    }

    @Test
    public void testList() {
        try {
            String url = getBase().build().toASCIIString() + StreamingResource.LIST;
            ResteasyClient client = client();
            ResteasyWebTarget target = client.target(url);
            String response = target.request().get(String.class);
            System.out.println(response);
            JsonArray a = Json.createReader(new InputStreamReader(new ByteArrayInputStream(response.getBytes()))).read().asJsonArray();
            Assert.assertEquals(1, a.size());
            Assert.assertTrue(JsonString.class.cast(a.get(0)).getString().contains("pom.xml"));
        } catch (Exception ex) {
            ex.printStackTrace();
            Assert.fail();
        }
    }

    @Test
    public void testDownloadGet() {
        try {
            ResteasyClient client = client();
            ResteasyWebTarget target = client.target(getBase().build().toASCIIString());
            Builder request = target.path(StreamingResource.DOWNLOAD_GET).queryParam("file", "pom.xml").request();
            Invocation get = request.buildGet();
            Response response = get.invoke();
            Assert.assertEquals(200, response.getStatus());
            InputStream readEntity = response.readEntity(InputStream.class);
            System.out.println(new String(IOUtils.readFully(readEntity, response.getLength())));
        } catch (Exception ex) {
            ex.printStackTrace();
            Assert.fail();
        }
    }

    @Test
    public void testDownloadGetProxy() {
        try {
            ResteasyClient client = client();
            ResteasyWebTarget target = client.target(server.baseUri());
            StreamingResourceI simple = target.proxy(StreamingResourceI.class);
            Response response = simple.downloadFileGet("pom.xml");
            Assert.assertEquals(200, response.getStatus());
            InputStream readEntity = response.readEntity(InputStream.class);
            System.out.println(new String(IOUtils.readFully(readEntity, response.getLength())));
        } catch (Exception ex) {
            ex.printStackTrace();
            Assert.fail();
        }
    }

    @Test
    public void testDownloadForm() {
        try {
            ResteasyClient client = client();
            ResteasyWebTarget target = client.target(getBase().build().toASCIIString());
            Builder request = target.path(StreamingResource.DOWNLOAD_FORM).request();
            MultivaluedMap<String, String> formData = new MultivaluedHashMap<>();
            formData.add("file", "pom.xml");
            Invocation post = request.buildPost(Entity.form(formData));
            Response response = post.invoke();
            Assert.assertEquals(200, response.getStatus());
            InputStream readEntity = response.readEntity(InputStream.class);
            System.out.println(new String(IOUtils.readFully(readEntity, response.getLength())));
        } catch (Exception ex) {
            ex.printStackTrace();
            Assert.fail();
        }
    }

    @Test
    public void testDownloadFormProxy() {
        try {
            ResteasyClient client = client();
            ResteasyWebTarget target = client.target(server.baseUri());
            StreamingResourceI simple = target.proxy(StreamingResourceI.class);
            Response response = simple.downloadFileForm("pom.xml");
            Assert.assertEquals(200, response.getStatus());
            InputStream readEntity = response.readEntity(InputStream.class);
            System.out.println(new String(IOUtils.readFully(readEntity, response.getLength())));
        } catch (Exception ex) {
            ex.printStackTrace();
            Assert.fail();
        }
    }

    // FIXME
    // @Test
    // public void testDownloadFile() {
    // try {
    // ResteasyClient client = client();
    // ResteasyWebTarget target = client.target(getBase().build().toASCIIString());
    // Builder request = target.path(StreamingResource.DOWNLOAD_FILE).queryParam("file", "pom.xml").request();
    // Invocation get = request.buildGet();
    // Response response = get.invoke();
    // Assert.assertEquals(200, response.getStatus());
    // InputStream readEntity = response.readEntity(InputStream.class);
    // System.out.println(response.getLength());
    // System.out.println(readEntity.available());
    // System.out.println(new String(IOUtils.readFully(readEntity, response.getLength())));
    // } catch (Exception ex) {
    // ex.printStackTrace();
    // Assert.fail();
    // }
    // }

    @Test
    public void testDownloadFileProxy() {
        try {
            StreamingOutput result = proxy.downloadFileBin("pom.xml");
            System.out.println();
        } catch (Exception ex) {
            ex.printStackTrace();
            Assert.fail();
        }
    }

    private ResteasyClient client() {
        ResteasyClient client = new ResteasyClientBuilder().httpEngine(new ApacheHttpClient43Engine(HttpClientBuilder.create().build())).build();
        return client;
    }

    @Test
    public void testUploadBin() {
        try {
            ResteasyClient client = client();
            ResteasyWebTarget target = client.target(server.baseUri());
            StreamingResourceI simple = target.proxy(StreamingResourceI.class);
            simple.uploadFileBin("pom.xml", new FilePath("pom.xml").newBufferedInputStream());
        } catch (Exception ex) {
            ex.printStackTrace();
            Assert.fail();
        }
    }

    @Test
    public void testDownloadFileAlt() {
        try {
            ResteasyClient client = client();
            ResteasyWebTarget target = client.target(getBase().build().toASCIIString());
            Builder request = target.path(StreamingResource.DOWNLOAD_FILE_ALT).queryParam("file", "pom.xml").request();
            Invocation get = request.buildGet();
            Response response = get.invoke();
            Assert.assertEquals(200, response.getStatus());
            InputStream readEntity = response.readEntity(InputStream.class);
            System.out.println(response.getLength());
            System.out.println(readEntity.available());
            System.out.println(new String(IOUtils.readFully(readEntity, response.getLength())));
        } catch (Exception ex) {
            ex.printStackTrace();
            Assert.fail();
        }
    }
}
