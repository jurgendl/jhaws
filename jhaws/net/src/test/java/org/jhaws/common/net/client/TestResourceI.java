package org.jhaws.common.net.client;

import java.io.InputStream;
import java.util.Date;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
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
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.PathSegment;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.StreamingOutput;
import javax.ws.rs.core.UriInfo;

import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

@Path(TestResource.PATH)
public interface TestResourceI /* extends RestResource */ {
    String IS = "=";

    String GET_MATRIXBEAN = "getmatrixbean";

    String GET_MATRIXBEANI = "getmatrixbeani";

    String GET_HEADERINFO = "headerinfo";

    String GET_COOKIEINFO = "cookieinfo";

    String PATH_PARAM = "pathParam";

    String QUERY_PARAM = "queryParam";

    String FORM_PARAM = "formParam";

    String PATH = "testresource";

    String GET = "get";

    String GET_DOUBLE = "getdouble";

    String MATRIX_PARAMS = "matrixparams";

    String MATRIX_PATH = "matrix/";

    String MATRIX = MATRIX_PATH + "{" + MATRIX_PARAMS + "}";

    String GET_BODY = "getbody";

    String PUT = "put/{" + PATH_PARAM + "}";

    String POST = "post";

    String POST_MULTI = "postmulti";

    String DELETE = "delete/{" + PATH_PARAM + "}";

    String GET_WITH_PARAMS = "getwithparams/{" + PATH_PARAM + "}";

    String GET_WITH_QUERY = "getwithquery";

    String STREAM_IN = "streamin";

    String STREAM_OUT = "streamout";

    @GET
    @HEAD
    @Produces(MediaType.TEXT_PLAIN)
    @Path("ping")
    String ping();

    @GET
    @Produces({ MediaType.TEXT_XML, MediaType.APPLICATION_XML })
    @Path(GET_BODY)
    TestBody getBody();

    @GET
    @HEAD
    @Produces(MediaType.TEXT_PLAIN)
    @Path(GET)
    String get();

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path(GET_WITH_PARAMS)
    String getWithParams(@PathParam(PATH_PARAM) String pathParam);

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path(GET_WITH_QUERY)
    String getWithQuery(@QueryParam(QUERY_PARAM) String queryParam);

    @PUT
    @Path(PUT)
    @Consumes({ MediaType.TEXT_XML, MediaType.APPLICATION_XML })
    void put(@PathParam(PATH_PARAM) String pathParam, TestBody testBody);

    @DELETE
    @Path(DELETE)
    void delete(@PathParam(PATH_PARAM) String pathParam);

    @POST
    @Path(POST)
    String post(@FormParam(FORM_PARAM) String formParam);

    @POST
    @Path(POST_MULTI)
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    String uploadFile(MultipartFormDataInput input);

    @POST
    @Path(STREAM_IN)
    void stream(@HeaderParam("filename") String filename, InputStream fileinput);

    @GET
    @Path(STREAM_OUT)
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    StreamingOutput stream();

    @Path(GET_DOUBLE)
    @Produces(MediaType.TEXT_XML)
    String getDoubleXml();

    @Path(GET_DOUBLE)
    @Produces(MediaType.TEXT_XML)
    String getDoublePlain();

    @GET
    @Path(GET_HEADERINFO)
    @Produces(MediaType.TEXT_PLAIN)
    String headers(//
            String Accept, //
            String Accept_Charset, //
            String Accept_Encoding, //
            String Accept_Language, //
            String Accept_Datetime, //
            // Authorization
            String Cache_Control, //
            String Connection, //
            Long Content_Length, //
            String Content_Type, //
            Date Date, //
            String Expect, //
            String From_email, //
            Date If_Modified_Since, //
            String Origin, //
            String Range, //
            String Referer, //
            String User_Agent//
    );

    @GET
    @Path(GET_COOKIEINFO)
    @Produces(MediaType.TEXT_PLAIN)
    String cookies(@HeaderParam("JSESSIONID") String JSESSIONID);

    @GET
    @Path("a")
    String get(@Context HttpHeaders hh);

    @GET
    @Path("b")
    String get(@Context UriInfo ui);

    @GET
    @Path("c")
    String get(@Context Request req);

    @GET
    @Path("d")
    String get(@Context HttpServletRequest req);

    @GET
    @Path("e")
    String get(@Context ServletConfig req);

    @GET
    @Path("f")
    String get(@Context ServletContext req);

    @GET
    @Path("g")
    String get(@Context SecurityContext req);

    @GET
    @Path(MATRIX)
    String matrixParams(@PathParam(MATRIX_PARAMS) PathSegment matrix);
}