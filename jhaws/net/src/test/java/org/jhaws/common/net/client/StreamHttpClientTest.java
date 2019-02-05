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
import org.junit.Test;

public class StreamHttpClientTest {
    private static TestRestServer server;

    private static HTTPClient hc;

    private static StreamingResource testResource;

    @SuppressWarnings("unused")
    private static StreamingResourceI proxy;

    private static FilePath file;

    static {
        try {
            file = new FilePath("pom.xml");
            hc = new HTTPClient();
            testResource = new StreamingResource();
            testResource.data.put(file.getFileNameString(), file.readAllBytes());
            testResource.len.put(file.getFileNameString(), file.getFileSize());
            server = TestRestServer.create(testResource);
            proxy = new RestEasyClient<>(getBaseExclResourcePath().build(), StreamingResourceI.class).proxy();
        } catch (Exception ex) {
            ex.printStackTrace();
            file = null;
            hc = null;
            testResource = null;
            server = null;
            proxy = null;
        }
    }

    private static UriBuilder getBaseInclResourcePath() {
        return getBaseExclResourcePath().path(StreamingResourceI.PATH);
    }

    private static UriBuilder getBaseExclResourcePath() {
        return UriBuilder.fromPath(server.baseUri());
    }

    @AfterClass
    static public void afterClass() throws Exception {
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
    public void test_UploadForm() {
        try {
            // FIXME only 1024
            MultipartFormDataOutput mdo = new MultipartFormDataOutput();
            mdo.addFormData("attachment", file.newBufferedInputStream(), MediaType.APPLICATION_OCTET_STREAM_TYPE, file.getFileNameString());
            Response response = target().path(StreamingResource.UPLOAD_FORM).request().post(MultipartFormDataOutputEntity.entity(mdo));
            Assert.assertEquals(200, response.getStatus());
            System.out.println(response.readEntity(String.class));
        } catch (Exception ex) {
            ex.printStackTrace();
            Assert.fail();
        }
    }

    @Test
    public void proxy_List() {
        try {
            StreamingResourceI simple = client().target(server.baseUri()).proxy(StreamingResourceI.class);
            System.out.println(simple.list());
        } catch (Exception ex) {
            ex.printStackTrace();
            Assert.fail();
        }
    }

    @Test
    public void test_List() {
        try {
            String response = target().path(StreamingResource.LIST).request().get(String.class);
            System.out.println(response);
            JsonArray a = Json.createReader(new InputStreamReader(new ByteArrayInputStream(response.getBytes()))).read().asJsonArray();
            Assert.assertEquals(1, a.size());
            Assert.assertTrue(JsonString.class.cast(a.get(0)).getString().contains(file.getFileNameString()));
        } catch (Exception ex) {
            ex.printStackTrace();
            Assert.fail();
        }
    }

    @Test
    public void test_DownloadGet() {
        try {
            Builder request = target().path(StreamingResource.DOWNLOAD_GET).queryParam("file", file.getFileNameString()).request();
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
    public void proxy_DownloadGet() {
        try {
            StreamingResourceI simple = client().target(server.baseUri()).proxy(StreamingResourceI.class);
            Response response = simple.downloadGet(file.getFileNameString());
            Assert.assertEquals(200, response.getStatus());
            InputStream readEntity = response.readEntity(InputStream.class);
            System.out.println(new String(IOUtils.readFully(readEntity, response.getLength())));
        } catch (Exception ex) {
            ex.printStackTrace();
            Assert.fail();
        }
    }

    @Test
    public void test_DownloadForm() {
        try {
            Builder request = target().path(StreamingResource.DOWNLOAD_FORM).request();
            MultivaluedMap<String, String> formData = new MultivaluedHashMap<>();
            formData.add("file", file.getFileNameString());
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
    public void proxy_DownloadForm() {
        try {
            StreamingResourceI simple = client().target(server.baseUri()).proxy(StreamingResourceI.class);
            Response response = simple.downloadForm(file.getFileNameString());
            Assert.assertEquals(200, response.getStatus());
            InputStream readEntity = response.readEntity(InputStream.class);
            System.out.println(new String(IOUtils.readFully(readEntity, response.getLength())));
        } catch (Exception ex) {
            ex.printStackTrace();
            Assert.fail();
        }
    }

    @Test
    public void test_DownloadStream() {
        try {
            Builder request = target().path(StreamingResource.DOWNLOAD_STREAM).queryParam("file", file.getFileNameString()).request();
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

    private ResteasyClient client;

    private ResteasyClient client() {
        if (client == null) client = new ResteasyClientBuilder().httpEngine(new ApacheHttpClient43Engine(HttpClientBuilder.create().build())).build();
        return client;
    }

    private ResteasyWebTarget target;

    private ResteasyWebTarget target() {
        if (target == null) target = client().target(getBaseInclResourcePath());
        return target;
    }

    @Test
    public void proxy_UploadStream() {
        try {
            StreamingResourceI simple = client().target(server.baseUri()).proxy(StreamingResourceI.class);
            String reponse = simple.uploadStream(file.getFileNameString(), file.newBufferedInputStream());
            System.out.println(reponse);
        } catch (Exception ex) {
            ex.printStackTrace();
            Assert.fail();
        }
    }

    @Test
    public void test_DownloadStreamInResponse() {
        try {
            Builder request = target().path(StreamingResource.DOWNLOAD_STREAM_IN_RESPONSE).queryParam("file", file.getFileNameString()).request();
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
}
