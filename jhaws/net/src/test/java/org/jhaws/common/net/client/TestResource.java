package org.jhaws.common.net.client;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.BeanParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.HEAD;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.PathSegment;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.StreamingOutput;
import javax.ws.rs.core.UriInfo;

import org.apache.commons.io.IOUtils;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

@Path(TestResource.PATH)
@SuppressWarnings("unused")
public class TestResource /* extends RestResource */ {
	public static final String GET_MATRIXBEAN = "getmatrixbean";

	public static final String GET_MATRIXBEANI = "getmatrixbeani";

	public static final String GET_HEADERINFO = "headerinfo";

	public static final String GET_COOKIEINFO = "cookieinfo";

	private static final String IS = "=";

	public static final String PATH_PARAM = "pathParam";

	public static final String QUERY_PARAM = "queryParam";

	public static final String FORM_PARAM = "formParam";

	public static final String PATH = "testresource";

	public static final String GET = "get";

	public static final String GET_DOUBLE = "getdouble";

	public static final String MATRIX_PARAMS = "matrixparams";

	public static final String MATRIX_PATH = "matrix/";

	public static final String MATRIX = MATRIX_PATH + "{" + MATRIX_PARAMS + "}";

	public static final String GET_BODY = "getbody";

	public static final String PUT = "put/{" + PATH_PARAM + "}";

	public static final String POST = "post";

	public static final String POST_MULTI = "postmulti";

	public static final String DELETE = "delete/{" + PATH_PARAM + "}";

	public static final String GET_WITH_PARAMS = "getwithparams/{" + PATH_PARAM + "}";

	public static final String GET_WITH_QUERY = "getwithquery";

	public static final String STREAM_IN = "streamin";

	public static final String STREAM_OUT = "streamout";

	protected Object put;

	protected Object putBody;

	protected Object delete;

	private List<String> written = new ArrayList<>();

	protected Boolean streamBusy = null;

	@GET
	@HEAD
	@Produces(MediaType.TEXT_PLAIN)
	@Path("ping")
	public String ping() {
		return String.valueOf(new Date());
	}

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
	@Consumes(MediaType.MULTIPART_FORM_DATA)
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
					if (MediaType.APPLICATION_OCTET_STREAM_TYPE.getSubtype().equals(p.getMediaType().getSubtype())) {
						String fileName = getFileName(p.getHeaders());
						list.add(fileName);
						content.put(fileName, p.getBody(new javax.ws.rs.core.GenericType<byte[]>() {
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

	@POST
	@Path(STREAM_IN)
	public void stream(@HeaderParam("filename") String filename, InputStream fileinput) {
		try {
			String data = new String(IOUtils.toByteArray(fileinput));
			System.out.println("> filename = " + filename);
			System.out.println("> data = " + data);
		} catch (IOException ex) {
			throw new RuntimeException(ex);
		}
	}

	@GET
	@Path(STREAM_OUT)
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	public StreamingOutput stream() {
		return output -> {
			written.clear();
			streamBusy = true;
			int w = 0;
			for (int i = 0; i < 10; i++) {
				for (int j = 0; j < 4; j++) {
					for (int k = 0; k < 20; k++) {
						output.write((char) ('a' + k/* r.nextInt(25) */));
					}
				}
				long currentTimeMillis = 0;// System.currentTimeMillis();
				w += 100 + 1 + 9 + 1 + ("" + currentTimeMillis).length() + 1;
				String log = String.format("%09d", w) + "," + currentTimeMillis;
				// System.out.println("> " + log);
				written.add(log);
				output.write('\n');
				output.write(log.getBytes());
				output.write('\n');
				// try {
				// Thread.sleep(3000l);
				// } catch (Exception e) {
				// //
				// }
			}
			streamBusy = false;
		};
	}

	@Path(GET_DOUBLE)
	@Produces(MediaType.TEXT_XML)
	public String getDoubleXml() {
		return "<xmlcontent></xmlcontent>";
	}

	@Path(GET_DOUBLE)
	@Produces(MediaType.TEXT_XML)
	public String getDoublePlain() {
		return "textcontent";
	}

	@GET
	@Path(GET_HEADERINFO)
	@Produces(MediaType.TEXT_PLAIN)
	public String headers(//
			@HeaderParam("Accept") String Accept, //
			@HeaderParam("Accept-Charset") String Accept_Charset, //
			@HeaderParam("Accept-Encoding") String Accept_Encoding, //
			@HeaderParam("Accept-Language") String Accept_Language, //
			@HeaderParam("Accept-Datetime") String Accept_Datetime, //
			// Authorization
			@HeaderParam("Cache-Control") String Cache_Control, //
			@HeaderParam("Connection") String Connection, //
			@HeaderParam("Content-Length") Long Content_Length, //
			@HeaderParam("Content-Type") String Content_Type, //
			@HeaderParam("Date") Date Date, //
			@HeaderParam("Expect") String Expect, //
			@HeaderParam("From") String From_email, //
			@HeaderParam("If-Modified-Since") Date If_Modified_Since, //
			@HeaderParam("Origint") String Origin, //
			@HeaderParam("Range") String Range, //
			@HeaderParam("Referer") String Referer, //
			@HeaderParam("User-Agent") String User_Agent//
	) {
		return User_Agent;
	}

	@GET
	@Path(GET_COOKIEINFO)
	@Produces(MediaType.TEXT_PLAIN)
	public String cookies(@HeaderParam("JSESSIONID") String JSESSIONID) {
		return JSESSIONID;
	}

	@GET
	@Path("a")
	public String get(@Context HttpHeaders hh) {
		MultivaluedMap<String, String> headerParameters = hh.getRequestHeaders();
		Map<String, Cookie> params = hh.getCookies();
		return null;
	}

	@GET
	@Path("b")
	public String get(@Context UriInfo ui) {
		MultivaluedMap<String, String> queryParameters = ui.getQueryParameters();
		MultivaluedMap<String, String> pathParameters = ui.getPathParameters();
		return null;
	}

	@GET
	@Path("c")
	public String get(@Context Request req) {
		return null;
	}

	@GET
	@Path("d")
	public String get(@Context HttpServletRequest req) {
		return null;
	}

	@GET
	@Path("e")
	public String get(@Context ServletConfig req) {
		return null;
	}

	@GET
	@Path("f")
	public String get(@Context ServletContext req) {
		return null;
	}

	@GET
	@Path("g")
	public String get(@Context SecurityContext req) {
		return null;
	}

	@GET
	@Path(MATRIX)
	public String matrixParams(@PathParam(MATRIX_PARAMS) PathSegment matrix) {
		return "*" + matrix.getMatrixParameters();
	}

	@GET
	@Path(GET_MATRIXBEAN)
	public String matrixBean(@BeanParam MatrixTestBean matrix) {
		return "*" + matrix;
	}

	@GET
	@Path(GET_MATRIXBEANI)
	public String matrixBeani(@PathParam(MATRIX_PARAMS) MatrixTestBeanI matrix) {
		return "*" + matrix;
	}
}
