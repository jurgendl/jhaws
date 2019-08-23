package org.jhaws.common.net.client;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonString;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import org.apache.commons.io.IOUtils;
import org.apache.http.impl.client.HttpClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.jboss.resteasy.client.jaxrs.engines.ApacheHttpClient43Engine;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataOutput;
import org.jhaws.common.io.FilePath;
import org.jhaws.common.net.resteasy.MultipartFormDataOutputEntity;
import org.jhaws.common.net.resteasy.client.RestEasyClient;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class StreamingResourceTest {
    private static TestRestServer server;

    private static HTTPClient hc;

    private static StreamingResource testResource;

    private static StreamingResourceI otherproxy;

    private static FilePath file;

    private boolean runProxy = true;

    private boolean runOtherProxy = true;

    private boolean runDirect = true;

    static {
        try {
            file = new FilePath("pom.xml");
            hc = new HTTPClient();
            testResource = new StreamingResource();
            testResource.data.put(file.getFileNameString(), file.readAllBytes());
            testResource.len.put(file.getFileNameString(), file.getFileSize());
            server = TestRestServer.create(testResource);
            otherproxy = new RestEasyClient<>(getBaseExclResourcePath().build(), StreamingResourceI.class).proxy();
        } catch (Exception ex) {
            ex.printStackTrace();
            file = null;
            hc = null;
            testResource = null;
            server = null;
            otherproxy = null;
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

    private static ResteasyWebTarget target;

    private static ResteasyWebTarget target() {
        if (target == null) target = new ResteasyClientBuilder().httpEngine(new ApacheHttpClient43Engine(HttpClientBuilder.create().build()))
                .build()
                .target(getBaseInclResourcePath());
        return target;
    }

    private static StreamingResourceI proxy;

    private static StreamingResourceI proxy() {
        if (proxy == null) proxy = new ResteasyClientBuilder().httpEngine(new ApacheHttpClient43Engine(/* HttpClientBuilder.create().build() */))
                .build()
                .target(server.baseUri())
                .proxy(StreamingResourceI.class);
        return proxy;
    }

    @Test
    public void test_UploadForm() {
        if (!runDirect) return;
        Response response = null;
        try {
            MultipartFormDataOutput mdo = new MultipartFormDataOutput();
            mdo.addFormData("attachment", file.newBufferedInputStream(), MediaType.APPLICATION_OCTET_STREAM_TYPE, file.getFileNameString());
            response = target().path(StreamingResourceI.UPLOAD_FORM).request().post(MultipartFormDataOutputEntity.entity(mdo));
            Assert.assertEquals(200, response.getStatus());
            System.out.println(response.readEntity(String.class));
        } catch (Exception ex) {
            ex.printStackTrace();
            Assert.fail("" + ex);
        } finally {
            if (response != null) response.close();
        }
    }

    @Test
    public void proxy_List() {
        if (!runProxy) return;
        try {
            System.out.println(proxy().list());
        } catch (Exception ex) {
            ex.printStackTrace();
            Assert.fail("" + ex);
        }
    }

    @Test
    public void oproxy_List() {
        if (!runOtherProxy) return;
        try {
            System.out.println(otherproxy.list());
        } catch (Exception ex) {
            ex.printStackTrace();
            Assert.fail("" + ex);
        }
    }

    @Test
    public void test_List() {
        if (!runDirect) return;
        try {
            String response = target().path(StreamingResourceI.LIST).request().get(String.class);
            System.out.println(response);
            JsonArray a = Json.createReader(new InputStreamReader(new ByteArrayInputStream(response.getBytes()))).read().asJsonArray();
            Assert.assertEquals(1, a.size());
            Assert.assertTrue(JsonString.class.cast(a.get(0)).getString().contains(file.getFileNameString()));
        } catch (Exception ex) {
            ex.printStackTrace();
            Assert.fail("" + ex);
        }
    }

    @Test
    public void test_DownloadGet() {
        if (!runDirect) return;
        Response response = null;
        try {
            response = target().path(StreamingResourceI.DOWNLOAD_GET).queryParam("file", file.getFileNameString()).request().buildGet().invoke();
            Assert.assertEquals(200, response.getStatus());
            InputStream readEntity = response.readEntity(InputStream.class);
            System.out.println(new String(IOUtils.readFully(readEntity, response.getLength())));
        } catch (Exception ex) {
            ex.printStackTrace();
            Assert.fail("" + ex);
        } finally {
            if (response != null) response.close();
        }
    }

    @Test
    public void proxy_DownloadGet() {
        if (!runProxy) return;
        Response response = null;
        try {
            response = proxy().downloadGet(file.getFileNameString());
            Assert.assertEquals(200, response.getStatus());
            InputStream readEntity = response.readEntity(InputStream.class);
            System.out.println(new String(IOUtils.readFully(readEntity, response.getLength())));
        } catch (Exception ex) {
            ex.printStackTrace();
            Assert.fail("" + ex);
        } finally {
            if (response != null) response.close();
        }
    }

    @Test
    public void oproxy_DownloadGet() {
        if (!runOtherProxy) return;
        Response response = null;
        try {
            response = otherproxy.downloadGet(file.getFileNameString());
            Assert.assertEquals(200, response.getStatus());
            InputStream readEntity = (InputStream) response.getEntity();
            System.out.println(new String(IOUtils.readFully(readEntity, (int) (long) RestEasyClient.getFileSize(response))));
        } catch (Exception ex) {
            ex.printStackTrace();
            Assert.fail("" + ex);
        } finally {
            if (response != null) response.close();
        }
    }

    @Test
    public void test_DownloadForm() {
        if (!runDirect) return;
        Response response = null;
        try {
            MultivaluedMap<String, String> formData = new MultivaluedHashMap<>();
            formData.add("file", file.getFileNameString());
            response = target().path(StreamingResourceI.DOWNLOAD_FORM).request().buildPost(Entity.form(formData)).invoke();
            Assert.assertEquals(200, response.getStatus());
            InputStream readEntity = response.readEntity(InputStream.class);
            System.out.println(new String(IOUtils.readFully(readEntity, response.getLength())));
        } catch (Exception ex) {
            ex.printStackTrace();
            Assert.fail("" + ex);
        } finally {
            if (response != null) response.close();
        }
    }

    @Test
    public void proxy_DownloadForm() {
        if (!runProxy) return;
        Response response = null;
        try {
            response = proxy().downloadForm(file.getFileNameString());
            Assert.assertEquals(200, response.getStatus());
            InputStream readEntity = response.readEntity(InputStream.class);
            System.out.println(new String(IOUtils.readFully(readEntity, response.getLength())));
        } catch (Exception ex) {
            ex.printStackTrace();
            Assert.fail("" + ex);
        } finally {
            if (response != null) response.close();
        }
    }

    @Test
    public void oproxy_DownloadForm() {
        if (!runOtherProxy) return;
        Response response = null;
        try {
            response = otherproxy.downloadForm(file.getFileNameString());
            Assert.assertEquals(200, response.getStatus());
            InputStream readEntity = (InputStream) response.getEntity();
            System.out.println(new String(IOUtils.readFully(readEntity, (int) (long) RestEasyClient.getFileSize(response))));
        } catch (Exception ex) {
            ex.printStackTrace();
            Assert.fail("" + ex);
        } finally {
            if (response != null) response.close();
        }
    }

    @Test
    public void test_DownloadStream() {
        if (!runDirect) return;
        Response response = null;
        try {
            response = target().path(StreamingResourceI.DOWNLOAD_STREAM)
                    .queryParam("file", file.getFileName().toString())
                    .request()
                    .buildGet()
                    .invoke();
            Assert.assertEquals(200, response.getStatus());
            InputStream readEntity = response.readEntity(InputStream.class);
            System.out.println(new String(IOUtils.readFully(readEntity, response.getLength())));
        } catch (Exception ex) {
            ex.printStackTrace();
            Assert.fail("" + ex);
        } finally {
            if (response != null) response.close();
        }
    }

    @Test
    public void proxy_UploadStream() {
        if (!runProxy) return;
        try {
            String reponse = proxy().uploadStream(file.getFileNameString(), file.newBufferedInputStream());
            System.out.println(reponse);
        } catch (Exception ex) {
            ex.printStackTrace();
            Assert.fail("" + ex);
        }
    }

    @Test
    public void oproxy_UploadStream() {
        if (!runOtherProxy) return;
        try {
            String reponse = otherproxy.uploadStream(file.getFileNameString(), file.newBufferedInputStream());
            System.out.println(reponse);
        } catch (Exception ex) {
            ex.printStackTrace();
            Assert.fail("" + ex);
        }
    }

    @Test
    public void test_DownloadStreamInResponse() {
        if (!runDirect) return;
        Response response = null;
        try {
            response = target().path(StreamingResourceI.DOWNLOAD_STREAM_IN_RESPONSE)
                    .queryParam("file", file.getFileNameString())
                    .request()
                    .buildGet()
                    .invoke();
            Assert.assertEquals(200, response.getStatus());
            InputStream readEntity = response.readEntity(InputStream.class);
            System.out.println(new String(IOUtils.readFully(readEntity, response.getLength())));
        } catch (Exception ex) {
            ex.printStackTrace();
            Assert.fail("" + ex);
        } finally {
            if (response != null) response.close();
        }
    }

    @Test
    public void proxy_DownloadStreamInResponse() {
        if (!runProxy) return;
        Response response = null;
        try {
            response = proxy().downloadStreamInResponse(file.getFileName().toString());
            Assert.assertEquals(200, response.getStatus());
            InputStream readEntity = response.readEntity(InputStream.class);
            System.out.println(new String(IOUtils.readFully(readEntity, response.getLength())));
        } catch (Exception ex) {
            ex.printStackTrace();
            Assert.fail("" + ex);
        } finally {
            if (response != null) response.close();
        }
    }

    @Test
    public void oproxy_DownloadStreamInResponse() {
        if (!runOtherProxy) return;
        Response response = null;
        try {
            response = otherproxy.downloadStreamInResponse(file.getFileName().toString());
            Assert.assertEquals(200, response.getStatus());
            InputStream readEntity = (InputStream) response.getEntity();
            System.out.println(new String(IOUtils.readFully(readEntity, (int) (long) RestEasyClient.getFileSize(response))));
        } catch (Exception ex) {
            ex.printStackTrace();
            Assert.fail("" + ex);
        } finally {
            if (response != null) response.close();
        }
    }

    @Test
    public void test_UploadStream() {
        if (!runDirect) return;
        Response response = null;
        try {
            response = target().path(StreamingResourceI.UPLOAD_STREAM)
                    .request()
                    .header("file", file.getFileNameString())
                    .buildPost(Entity.entity(file.newBufferedInputStream(), MediaType.APPLICATION_OCTET_STREAM))
                    .invoke();
            Assert.assertEquals(200, response.getStatus());
            String s = response.readEntity(String.class);
            System.out.println(s);
        } catch (Exception ex) {
            ex.printStackTrace();
            Assert.fail("" + ex);
        } finally {
            if (response != null) response.close();
        }
    }
}
