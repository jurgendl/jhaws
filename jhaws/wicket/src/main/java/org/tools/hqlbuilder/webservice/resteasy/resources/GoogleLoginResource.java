package org.tools.hqlbuilder.webservice.resteasy.resources;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.jboss.resteasy.annotations.GZIP;
import org.jhaws.common.web.resteasy.Pretty;
import org.jhaws.common.web.resteasy.RestResource;

@Path("/googlelogin")
@Pretty
@GZIP
public interface GoogleLoginResource extends RestResource {
    @GET
	@Path("/ping" + D + TEXT_EXTENSION)
    @Produces(TEXT)
    public String ping();

    @POST
    @Path("/tokensignin")
    @Consumes(FORM_URLENCODED)
    @Produces(HTML)
    public Response tokensignin(@FormParam("idtoken") String id_token);
}
