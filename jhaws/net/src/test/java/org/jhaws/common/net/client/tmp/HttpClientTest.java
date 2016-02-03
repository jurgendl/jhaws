package org.jhaws.common.net.client.tmp;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;

import org.jhaws.common.io.FilePath;
import org.jhaws.common.net.client.HTTPClientDefaults;
import org.jhaws.common.net.client.v45.DeleteRequest;
import org.jhaws.common.net.client.v45.FileInput;
import org.jhaws.common.net.client.v45.Form;
import org.jhaws.common.net.client.v45.GetRequest;
import org.jhaws.common.net.client.v45.HTTPClient;
import org.jhaws.common.net.client.v45.HeadRequest;
import org.jhaws.common.net.client.v45.PutRequest;
import org.jhaws.common.net.client.v45.Response;
import org.jhaws.common.net.client.v45.XmlMarshalling;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @see http://www.mastertheboss.com/jboss-frameworks/resteasy/resteasy-tutorial-part-two-web-parameters
 * @see https://dzone.com/articles/how-test-rest-api-junit
 * @see https://docs.jboss.org/resteasy/docs/3.0.9.Final/userguide/pdf/resteasy-reference-guide-en-US.pdf
 * @see http://www.baeldung.com/httpclient-multipart-upload
 */
public class HttpClientTest {
	private static final String UTF_8 = "UTF-8";

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
		hc.close();
	}

	@Test
	public void test_get() {
		try {
			URI uri = getBase().path(TestResource.GET).build();
			String rec = server.resteasyClient.target(uri).request().get(String.class);
			String hcr = hc.get(new GetRequest(uri)).getContentString();
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
			TestBody rec = xmlMarshalling.unmarshall(TestBody.class, server.resteasyClient.target(uri).request().get(String.class), UTF_8);
			TestBody hcr = xmlMarshalling.unmarshall(TestBody.class, hc.get(new GetRequest(uri)).getContent());
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
			String hcr = hc.get(new GetRequest(uri)).getContentString();
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
			String hcr = hc.get(new GetRequest(uri)).getContentString();
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
		hc.delete(new DeleteRequest(uri));
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
		String xml = xmlMarshalling.marshall(entity, UTF_8);
		System.out.println(xml);
		hc.put(new PutRequest(uri, xml, MediaType.TEXT_XML));
		String hcr1 = String.class.cast(testResource.put);
		TestBody hcr2 = TestBody.class.cast(testResource.putBody);
		Assert.assertEquals(rec1, hcr1);
		Assert.assertEquals(rec2, hcr2);
	}

	@Test
	public void test_head() {
		URI uri = getBase().path(TestResource.GET).build();

		Map<String, List<Object>> mg1 = new TreeMap<>(hc.get(new GetRequest(uri)).getHeaders());
		javax.ws.rs.core.Response response = server.resteasyClient.target(uri).request().get();
		Map<String, List<Object>> mg2 = new TreeMap<>(response.getHeaders());
		response.close();

		Response head = hc.head(new HeadRequest(uri));
		Assert.assertNull(head.getContent());

		Map<String, List<Object>> mh1 = new TreeMap<>(head.getHeaders());
		response = server.resteasyClient.target(uri).request().head();
		Map<String, List<Object>> mh2 = new TreeMap<>(response.getHeaders());
		response.close();

		// transfer-encoding=[chunked] missing in other
		mg1.remove("transfer-encoding");
		mg2.remove("transfer-encoding");

		Assert.assertEquals(mg2, mh2);
		Assert.assertEquals(mg1, mh1);
		Assert.assertEquals(mh1, mh2);
	}

	@Test
	public void test_form() {
		try (HTTPClient hcl = new HTTPClient()) {
			Form form = hcl.get(new GetRequest("http://www.google.com")).getForms().get(0);
			form.setValue("q", "java");
			form.setValue("source", "hp");
			Assert.assertEquals(200, hcl.submit(form).getStatusCode());
		}
	}

	@Test
	public void test_post() {
		URI uri = getBase().path(TestResource.POST).build();
		Form form = new Form("formid");
		form.setMethod(HTTPClientDefaults.POST);
		form.setUrl(uri);
		String value = "formValue";
		form.setValue(TestResource.FORM_PARAM, value);
		Assert.assertEquals(value, hc.submit(form).getContentString());
	}

	@Test
	public void test_post_multi() {
		URI uri = getBase().path(TestResource.POST_MULTI).build();
		Form form = new Form("formid");
		FileInput f = new FileInput("fileinput");
		FilePath tmp = FilePath.createDefaultTempFile();
		String bytes = "newTmpFile";
		tmp.write(bytes);
		f.setFile(tmp);
		form.addInputElements(f.getName(), f);
		form.setMethod(HTTPClientDefaults.POST);
		form.setUrl(uri);
		String value = "formValue";
		form.setValue(TestResource.FORM_PARAM, value);
		Assert.assertEquals(bytes, hc.submit(form).getContentString());
	}

	@Test
	public void test_stream() {
		URI uri = getBase().path(TestResource.STREAM).build();
		GetRequest get = new GetRequest(uri);

		FilePath tmp1 = FilePath.getTempDirectory().child("hctest_1_.txt");
		tmp1.deleteIfExists();
		tmp1.write(hc.get(get).getContent());

		FilePath tmp2 = FilePath.getTempDirectory().child("hctest_2_.txt");
		tmp2.deleteIfExists();
		hc.execute(null, hc.createGet(get), tmp2.newBufferedOutputStream());

		Assert.assertEquals(tmp2.getFileSize(), tmp1.getFileSize());
		Assert.assertEquals(tmp2.readAll(), tmp1.readAll());
	}
}