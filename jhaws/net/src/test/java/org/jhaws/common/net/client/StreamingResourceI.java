package org.jhaws.common.net.client;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;
import java.io.InputStream;
import java.util.List;

@Path(StreamingResourceI.PATH)
// @Pretty
// @GZIP
public interface StreamingResourceI {
    static final String PATH = "/files";

    static final String LIST = "/list";

    @Deprecated
    static final String DOWNLOAD_STREAM = "/downloadstream";

    static final String DOWNLOAD_STREAM_IN_RESPONSE = "/downloadstreaminresponse";

    static final String UPLOAD_FORM = "/uploadform";

    static final String DOWNLOAD_GET = "/downloadget";

    static final String DOWNLOAD_FORM = "/downloadform";

    static final String UPLOAD_STREAM = "/uploadstream";

    @POST
    @Path(UPLOAD_FORM)
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.TEXT_PLAIN)
    Response uploadForm(@Context HttpServletRequest request, MultipartFormDataInput input);

    @POST
    @Path(DOWNLOAD_FORM)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    Response downloadForm(@FormParam("file") String file);

    @GET
    @Path(DOWNLOAD_GET)
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    Response downloadGet(@QueryParam("file") String file);

    @GET
    @Path(DOWNLOAD_STREAM)
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    @Deprecated
    StreamingOutput downloadStream(@Context HttpServletResponse response, @QueryParam("file") String file);

    @GET
    @Path(DOWNLOAD_STREAM_IN_RESPONSE)
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    Response downloadStreamInResponse(@QueryParam("file") String file);

    @GET
    @Path(LIST)
    @Produces(MediaType.APPLICATION_JSON)
    List<String> list();

    @POST
    @Path(UPLOAD_STREAM)
    @Consumes(MediaType.APPLICATION_OCTET_STREAM)
    @Produces(MediaType.TEXT_PLAIN)
    String uploadStream(@HeaderParam("file") String fileName, InputStream data);
}
