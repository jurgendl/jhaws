package org.tools.hqlbuilder.webservice.resteasy.resources;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.StreamingOutput;

import org.jboss.resteasy.annotations.GZIP;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
import org.jhaws.common.web.resteasy.Pretty;
import org.jhaws.common.web.resteasy.RestResource;
import org.tools.hqlbuilder.webservice.jquery.ui.jquery_file_upload_alt.DeleteResult;
import org.tools.hqlbuilder.webservice.jquery.ui.jquery_file_upload_alt.UploadResult;

@Path(JQueryFileUploadRestImpl.PATH)
@Pretty
@GZIP
public interface JQueryFileUploadRest extends RestResource {
	public static final String PATH = "/jqupload";

	public static final String PATH_DELETE = "/delete";

	public static final String PATH_GET = "/get";

	public static final String PATH_UPLOAD = "/upload";

	public static final String PARAM_NAME = "name";

	public static String getFileName(MultivaluedMap<String, String> header) {
		String[] contentDisposition = header.getFirst("Content-Disposition").split(";");
		for (String filename : contentDisposition) {
			if (filename.trim().startsWith("filename")) {
				String[] name = filename.split("=");
				String finalFileName = name[1].trim().replaceAll("\"", "");
				return finalFileName;
			}
		}
		return "unknown";
	}

	@Path(JQueryFileUploadRestImpl.PATH_DELETE)
	@DELETE
	@Produces(JSON)
	public abstract DeleteResult deleteUpload(@QueryParam(JQueryFileUploadRestImpl.PARAM_NAME) String name);

	@Path(JQueryFileUploadRestImpl.PATH_GET)
	@GET
	@Produces(BINARY)
	public abstract StreamingOutput download(@QueryParam(JQueryFileUploadRestImpl.PARAM_NAME) String name);

	@Path(JQueryFileUploadRestImpl.PATH_UPLOAD)
	@GET
	@Produces(JSON)
	public abstract UploadResult getUploads();

	@Path(JQueryFileUploadRestImpl.PATH_UPLOAD)
	@POST
	@Consumes(FORM_MULTIPART)
	@Produces(JSON)
	public abstract UploadResult upload(@Context HttpServletRequest request, MultipartFormDataInput input);

}