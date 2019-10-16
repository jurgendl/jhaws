package org.jhaws.common.net.resteasy.client;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.ext.Provider;

import org.jhaws.common.web.resteasy.CustomResteasyJackson2Provider;

@Provider
@Consumes({ "application/json", "application/*+json", "text/json" })
@Produces({ "application/json", "application/*+json", "text/json" })
// @javax.annotation.Priority(0)
public class JsonProvider extends CustomResteasyJackson2Provider {
	//
}
