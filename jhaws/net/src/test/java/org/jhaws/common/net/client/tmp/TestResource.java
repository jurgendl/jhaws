package org.jhaws.common.net.client.tmp;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.HEAD;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import org.jboss.resteasy.util.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;

import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

@Path(TestResource.PATH)
public class TestResource {
	private static final String IS = "=";

	public static final String PATH_PARAM = "pathParam";

	public static final String QUERY_PARAM = "queryParam";

	public static final String FORM_PARAM = "formParam";

	public static final String PATH = "testresource";

	public static final String GET = "get";

	public static final String GET_BODY = "getbody";

	public static final String PUT = "put/{" + PATH_PARAM + "}";

	public static final String POST = "post";

	public static final String POST_MULTI = "postmulti";

	public static final String DELETE = "delete/{" + PATH_PARAM + "}";

	public static final String GET_WITH_PARAMS = "getwithparams/{" + PATH_PARAM + "}";

	public static final String GET_WITH_QUERY = "getwithquery";

	Object put;

	Object putBody;

	Object delete;

	@GET
	@Produces({ MediaType.TEXT_XML, MediaType.APPLICATION_XML })
	@Path(GET_BODY)
	public TestBody getBody() {
		return new TestBody("getbody");
	}

	@GET
	@HEAD
	@Produces(MediaType.TEXT_PLAIN)
	@Path(GET)
	public String get() {
		return "get ok";
	}

	@GET
	@Produces(MediaType.TEXT_PLAIN)
	@Path(GET_WITH_PARAMS)
	public String getWithParams(@PathParam(PATH_PARAM) String pathParam) {
		return PATH_PARAM + IS + pathParam;
	}

	@GET
	@Produces(MediaType.TEXT_PLAIN)
	@Path(GET_WITH_QUERY)
	public String getWithQuery(@QueryParam(QUERY_PARAM) String queryParam) {
		return QUERY_PARAM + IS + queryParam;
	}

	@PUT
	@Path(PUT)
	@Consumes({ MediaType.TEXT_XML, MediaType.APPLICATION_XML })
	public void put(@PathParam(PATH_PARAM) String pathParam, TestBody testBody) {
		put = pathParam;
		putBody = testBody;
	}

	@DELETE
	@Path(DELETE)
	public void delete(@PathParam(PATH_PARAM) String pathParam) {
		delete = pathParam;
	}

	@POST
	@Path(POST)
	public String post(@FormParam(FORM_PARAM) String formParam) {
		return formParam;
	}

	@POST
	@Path(POST_MULTI)
	@Consumes("multipart/form-data")
	public String uploadFile(MultipartFormDataInput input) {
		Map<String, List<String>> values = new HashMap<>();
		Map<String, byte[]> content = new HashMap<>();
		for (Map.Entry<String, List<InputPart>> entry : input.getFormDataMap().entrySet()) {
			entry.getValue().stream().forEach(p -> {
				List<String> list = values.get(entry.getKey());
				if (list == null) {
					list = new ArrayList<>();
					values.put(entry.getKey(), list);
				}
				try {
					if ("octet-stream".equals(p.getMediaType().getSubtype())) {
						String fileName = getFileName(p.getHeaders());
						list.add(fileName);
						content.put(fileName, p.getBody(new GenericType<byte[]>() {
						}));
					} else {
						list.add(p.getBodyAsString());
					}
				} catch (IOException e) {
					throw new UncheckedIOException(e);
				}
			});
		}
		return new String(content.get(values.get("fileinput").get(0)));
	}

	/**
	 * header sample { Content-Type=[image/png], Content-Disposition=[form-data;
	 * name="file"; filename="filename.extension"] }
	 **/
	// get uploaded filename, is there a easy way in RESTEasy?
	private String getFileName(MultivaluedMap<String, String> header) {
		String[] contentDisposition = header.getFirst("Content-Disposition").split(";");
		for (String filename : contentDisposition) {
			if ((filename.trim().startsWith("filename"))) {
				String[] name = filename.split("=");
				String finalFileName = name[1].trim().replaceAll("\"", "");
				return finalFileName;
			}
		}
		return "unknown";
	}
}
