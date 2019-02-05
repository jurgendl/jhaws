package org.jhaws.common.net.client;

import java.io.InputStream;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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

import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

@Path(StreamingResourceI.PATH)
// @Pretty
// @GZIP
public interface StreamingResourceI {
    static final String LIST = "/list";

    static final String DOWNLOAD_FILE = "/downloadfile";

    static final String DOWNLOAD_FILE_ALT = "/downloadfilealt";

    static final String UPLOAD_FORM = "/uploadform";

    static final String DOWNLOAD_GET = "/downloadget";

    static final String DOWNLOAD_FORM = "/downloadform";

    static final String UPLOAD_BINARY = "/uploadbin";

    static final String PATH = "/files";

    @POST
    @Path(UPLOAD_FORM)
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    Response uploadFileForm(@Context HttpServletRequest request, MultipartFormDataInput input);

    @POST
    @Path(DOWNLOAD_FORM)
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    Response downloadFileForm(@FormParam("file") String file);

    @GET
    @Path(DOWNLOAD_GET)
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    Response downloadFileGet(@QueryParam("file") String file);

    @GET
    @Path(DOWNLOAD_FILE)
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    StreamingOutput downloadFileBin(@Context HttpServletResponse response, @QueryParam("file") String file);

    @GET
    @Path(DOWNLOAD_FILE_ALT)
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    Response downloadFileBinAlt(@QueryParam("file") String file);

    @GET
    @Path(LIST)
    @Produces(MediaType.APPLICATION_JSON)
    List<String> list();

    @POST
    @Consumes(MediaType.APPLICATION_OCTET_STREAM)
    @Path(UPLOAD_BINARY)
    void uploadFileBin(@HeaderParam("file") String fileName, InputStream data);
}
