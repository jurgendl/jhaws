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
import javax.ws.rs.HeaderParam;
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

@SuppressWarnings("unused")
public class TestResource implements TestResourceI {
    protected Object put;

    protected Object putBody;

    protected Object delete;

    private List<String> written = new ArrayList<>();

    protected Boolean streamBusy = null;

    /**
     * @see org.jhaws.common.net.client.TestResourceI#ping()
     */
    @Override
    public String ping() {
        return String.valueOf(new Date());
    }

    /**
     * @see org.jhaws.common.net.client.TestResourceI#getBody()
     */
    @Override
    public TestBody getBody() {
        return new TestBody("getbody");
    }

    /**
     * @see org.jhaws.common.net.client.TestResourceI#get()
     */
    @Override
    public String get() {
        return "get ok";
    }

    /**
     * @see org.jhaws.common.net.client.TestResourceI#getWithParams(java.lang.String)
     */
    @Override
    public String getWithParams(String pathParam) {
        return PATH_PARAM + IS + pathParam;
    }

    /**
     * @see org.jhaws.common.net.client.TestResourceI#getWithQuery(java.lang.String)
     */
    @Override
    public String getWithQuery(String queryParam) {
        return QUERY_PARAM + IS + queryParam;
    }

    /**
     * @see org.jhaws.common.net.client.TestResourceI#put(java.lang.String, org.jhaws.common.net.client.TestBody)
     */
    @Override
    public void put(String pathParam, TestBody testBody) {
        put = pathParam;
        putBody = testBody;
    }

    /**
     * @see org.jhaws.common.net.client.TestResourceI#delete(java.lang.String)
     */
    @Override
    public void delete(String pathParam) {
        delete = pathParam;
    }

    /**
     * @see org.jhaws.common.net.client.TestResourceI#post(java.lang.String)
     */
    @Override
    public String post(String formParam) {
        return formParam;
    }

    /**
     * @see org.jhaws.common.net.client.TestResourceI#uploadFile(org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput)
     */
    @Override
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
                        content.put(fileName, p.getBody(new javax.ws.rs.core.GenericType<byte[]>() {}));
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
     * header sample { Content-Type=[image/png], Content-Disposition=[form-data; name="file"; filename="filename.extension"] }
     **/
    // get uploaded filename, is there a easy way in RESTEasy?
    private String getFileName(MultivaluedMap<String, String> header) {
        String[] contentDisposition = header.getFirst("Content-Disposition").split(";");
        for (String filename : contentDisposition) {
            if ((filename.trim().startsWith("filename"))) {
                String[] name = filename.split("=");
                String finalFileName = name[1].trim().replace("\"", "");
                return finalFileName;
            }
        }
        return "unknown";
    }

    /**
     * @see org.jhaws.common.net.client.TestResourceI#stream(java.lang.String, java.io.InputStream)
     */
    @Override
    public void stream(String filename, InputStream fileinput) {
        try {
            String data = new String(IOUtils.toByteArray(fileinput));
            System.out.println("> filename = " + filename);
            System.out.println("> data = " + data);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * @see org.jhaws.common.net.client.TestResourceI#stream()
     */
    @Override
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

    /**
     * @see org.jhaws.common.net.client.TestResourceI#getDoubleXml()
     */
    @Override
    public String getDoubleXml() {
        return "<xmlcontent></xmlcontent>";
    }

    /**
     * @see org.jhaws.common.net.client.TestResourceI#getDoublePlain()
     */
    @Override
    public String getDoublePlain() {
        return "textcontent";
    }

    /**
     * @see org.jhaws.common.net.client.TestResourceI#headers(java.lang.String, java.lang.String, java.lang.String, java.lang.String,
     *      java.lang.String, java.lang.String, java.lang.String, java.lang.Long, java.lang.String, java.util.Date, java.lang.String,
     *      java.lang.String, java.util.Date, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
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

    /**
     * @see org.jhaws.common.net.client.TestResourceI#cookies(java.lang.String)
     */
    @Override
    public String cookies(String JSESSIONID) {
        return JSESSIONID;
    }

    /**
     * @see org.jhaws.common.net.client.TestResourceI#get(javax.ws.rs.core.HttpHeaders)
     */
    @Override
    public String get(HttpHeaders hh) {
        MultivaluedMap<String, String> headerParameters = hh.getRequestHeaders();
        Map<String, Cookie> params = hh.getCookies();
        return null;
    }

    /**
     * @see org.jhaws.common.net.client.TestResourceI#get(javax.ws.rs.core.UriInfo)
     */
    @Override
    public String get(UriInfo ui) {
        MultivaluedMap<String, String> queryParameters = ui.getQueryParameters();
        MultivaluedMap<String, String> pathParameters = ui.getPathParameters();
        return null;
    }

    /**
     * @see org.jhaws.common.net.client.TestResourceI#get(javax.ws.rs.core.Request)
     */
    @Override
    public String get(Request req) {
        return null;
    }

    /**
     * @see org.jhaws.common.net.client.TestResourceI#get(javax.servlet.http.HttpServletRequest)
     */
    @Override
    public String get(HttpServletRequest req) {
        return null;
    }

    /**
     * @see org.jhaws.common.net.client.TestResourceI#get(javax.servlet.ServletConfig)
     */
    @Override
    public String get(ServletConfig req) {
        return null;
    }

    /**
     * @see org.jhaws.common.net.client.TestResourceI#get(javax.servlet.ServletContext)
     */
    @Override
    public String get(ServletContext req) {
        return null;
    }

    /**
     * @see org.jhaws.common.net.client.TestResourceI#get(javax.ws.rs.core.SecurityContext)
     */
    @Override
    public String get(SecurityContext req) {
        return null;
    }

    /**
     * @see org.jhaws.common.net.client.TestResourceI#matrixParams(javax.ws.rs.core.PathSegment)
     */
    @Override
    public String matrixParams(PathSegment matrix) {
        return "*" + matrix.getMatrixParameters();
    }

    // @GET
    // @Path(GET_MATRIXBEAN)
    // public String matrixBean(@BeanParam MatrixTestBean matrix) {
    // return "*" + matrix;
    // }
    //
    // @GET
    // @Path(GET_MATRIXBEANI)
    // public String matrixBeani(@PathParam(MATRIX_PARAMS) MatrixTestBeanI matrix) {
    // return "*" + matrix;
    // }
}
