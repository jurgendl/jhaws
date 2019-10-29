package org.jhaws.common.net.client5;

import java.io.ByteArrayInputStream;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;

import org.jhaws.common.io.FilePath;
import org.jhaws.common.io.jaxb.JAXBMarshalling;
import org.jhaws.common.lang.StringUtils;
import org.jhaws.common.net.client.DeleteRequest;
import org.jhaws.common.net.client.FileInput;
import org.jhaws.common.net.client.Form;
import org.jhaws.common.net.client.GetRequest;
import org.jhaws.common.net.client.HeadRequest;
import org.jhaws.common.net.client.PostRequest;
import org.jhaws.common.net.client.PutRequest;
import org.jhaws.common.net.client.Response;
import org.jhaws.common.net.client.TestBody;
import org.jhaws.common.net.client.TestResource;
import org.jhaws.common.net.client.TestResourceI;
import org.jhaws.common.net.client.TestRestServer;
import org.jhaws.common.net.resteasy.client.RestEasyClient;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @see http://www.mastertheboss.com/jboss-frameworks/resteasy/resteasy-tutorial
 * -part-two-web-parameters
 * @see https://dzone.com/articles/how-test-rest-api-junit
 * @see https://docs.jboss.org/resteasy/docs/3.0.9.Final/userguide/pdf/resteasy-
 * reference-guide-en-US.pdf
 * @see http://www.baeldung.com/httpclient-multipart-upload
 */
public class HttpClientTest {
	private static TestRestServer server;

	private static HTTPClient hc;

	private static TestResource testResource;

	private static JAXBMarshalling xmlMarshalling;

	private static TestResourceI proxy;

	private UriBuilder getBase() {
		return UriBuilder.fromPath(server.baseUri()).path(TestResourceI.PATH);
	}

	@BeforeClass
	public static void beforeClass() throws Exception {
		try {
			hc = new HTTPClient();
			testResource = new TestResource();
			server = TestRestServer.create(testResource);
			xmlMarshalling = new JAXBMarshalling(TestBody.class);
			proxy = new RestEasyClient<>(server.baseUri(), TestResourceI.class).proxy();
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
	public void test_get() {
		try {
			URI uri = getBase().path(TestResourceI.GET).build();
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
			URI uri = getBase().path(TestResourceI.GET_BODY).build();
			TestBody rec = xmlMarshalling.unmarshall(TestBody.class,
					server.resteasyClient.target(uri).request().get(String.class), StringUtils.UTF8);
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
			URI uri = getBase().path(TestResourceI.GET_WITH_PARAMS).build("pathValue");
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
			URI uri = getBase().path(TestResourceI.GET_WITH_QUERY).queryParam(TestResourceI.QUERY_PARAM, "queryValue")
					.build();
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
		URI uri = getBase().path(TestResourceI.DELETE).build("deleteId");
		server.resteasyClient.target(uri).request().delete();
		String rec = String.class.cast(testResource.delete);
		testResource.delete = null;
		hc.delete(new DeleteRequest(uri));
		String hcr = String.class.cast(testResource.delete);
		Assert.assertEquals(rec, hcr);
	}

	@Test
	public void test_put() {
		URI uri = getBase().path(TestResourceI.PUT).build("putId");
		TestBody entity = new TestBody("putBody");
		Entity<TestBody> xmle = Entity.xml(entity);
		System.out.println(xmle);
		server.resteasyClient.target(uri).request().put(xmle);
		String rec1 = String.class.cast(testResource.put);
		TestBody rec2 = TestBody.class.cast(testResource.putBody);
		testResource.put = null;
		testResource.putBody = null;
		String xml = xmlMarshalling.marshall(entity, StringUtils.UTF8);
		System.out.println(xml);
		hc.put(new PutRequest(uri, xml, MediaType.TEXT_XML));
		String hcr1 = String.class.cast(testResource.put);
		TestBody hcr2 = TestBody.class.cast(testResource.putBody);
		Assert.assertEquals(rec1, hcr1);
		Assert.assertEquals(rec2, hcr2);
	}

	@Test
	public void test_head() {
		URI uri = getBase().path(TestResourceI.GET).build();

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
		URI uri = getBase().path(TestResourceI.POST).build();
		Form form = new Form("formid");
		form.setMethod("POST");
		form.setUrl(uri);
		String value = "formValue";
		form.setValue(TestResourceI.FORM_PARAM, value);
		Assert.assertEquals(value, hc.submit(form).getContentString());
	}

	@Test
	public void test_post_multi() {
		URI uri = getBase().path(TestResourceI.POST_MULTI).build();
		Form form = new Form("formid");
		FileInput f = new FileInput("fileinput");
		FilePath tmp = FilePath.createTempFile();
		String bytes = "newTmpFile";
		tmp.write(bytes);
		f.setFile(tmp);
		form.addInputElements(f.getName(), f);
		form.setMethod("POST");
		form.setUrl(uri);
		String value = "formValue";
		form.setValue(TestResourceI.FORM_PARAM, value);
		Assert.assertEquals(bytes, hc.submit(form).getContentString());
	}

	@Test
	public void test_stream_out() {
		FilePath tmp1 = FilePath.getTempDirectory().child("hctest_1_.txt");
		tmp1.delete();
		tmp1.write(hc.get(new GetRequest(getBase().path(TestResourceI.STREAM_OUT).build())).getContent());

		FilePath tmp2 = FilePath.getTempDirectory().child("hctest_2_.txt");
		tmp2.delete();
		hc.get(new GetRequest(getBase().path(TestResourceI.STREAM_OUT).build()).setOut(tmp2.newBufferedOutputStream()));

		System.out.println(tmp2);
		System.out.println(tmp2.getFileSize());

		Assert.assertEquals(tmp1.getFileSize(), tmp2.getFileSize());
		Assert.assertEquals(tmp1.readAll(), tmp2.readAll());
	}

	@Test
	public void test_stream_in() {
		URI uri = getBase().path(TestResourceI.STREAM_IN).build();
		PostRequest post = new PostRequest(uri);
		post.addHeader("filename", "hctest_3_.txt");
		post.setStream(() -> new ByteArrayInputStream("file text data".getBytes()));
		hc.execute(post, hc.createPost(post));
	}

	@Test
	public void test_matrix_1() {
		URI uri = getBase().path(TestResourceI.MATRIX_PATH).matrixParam("key1", "value1a")
				.matrixParam("key1", "value1b").matrixParam("key2", "value2").build();
		GetRequest get = new GetRequest(uri);
		System.out.println(uri);
		System.out.println(hc.get(get).getContentString());
	}

	// @Test
	// public void test_matrix_2() {
	// URI uri = getBase().path(TestResource.GET_MATRIXBEAN)
	// .matrixParam("key1", "value1a")
	// .matrixParam("key1", "value1b")
	// .matrixParam("key2", "value2")
	// .build();
	// GetRequest get = new GetRequest(uri);
	// System.out.println(uri);
	// System.out.println(hc.get(get).getContentString());
	// }

	// @Test
	// public void test_matrix_3() {
	// URI uri = getBase().path(TestResource.GET_MATRIXBEANI)
	// .matrixParam("key1", "value1a")
	// .matrixParam("key1", "value1b")
	// .matrixParam("key2", "value2")
	// .build();
	// GetRequest get = new GetRequest(uri);
	// System.out.println(uri);
	// System.out.println(hc.get(get).getContentString());
	// }

	// @Test
	// public void test_headers_info() {
	// URI uri = getBase().path(TestResourceI.GET_HEADERINFO).build();
	// GetRequest get = new GetRequest(uri);
	// System.out.println(hc.get(get).getContentString());
	// }

	@Test
	public void test_cookie_info() {
		URI uri = getBase().path(TestResourceI.GET_COOKIEINFO).build();
		GetRequest get = new GetRequest(uri);
		System.out.println(hc.get(get).getContentString());
	}

	@Test
	public void test_client_get() {
		try {
			Assert.assertEquals(testResource.get(), proxy.get());
		} catch (Exception e) {
			e.printStackTrace(System.out);
			Assert.fail(String.valueOf(e));
		}
	}
}
