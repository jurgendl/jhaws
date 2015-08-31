package org.jhaws.common.net.client.tests;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HEAD;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

@Path(TestResource.PATH)
public class TestResource {
    private static final String IS = "=";

    public static final String PATH_PARAM = "pathParam";

    public static final String QUERY_PARAM = "queryParam";

    public static final String PATH = "testresource";

    public static final String GET = "get";

    public static final String GET_BODY = "getbody";

    public static final String PUT = "put/{" + PATH_PARAM + "}";

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
}
