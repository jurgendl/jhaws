package org.jhaws.common.web.resteasy;

import org.jboss.resteasy.plugins.providers.jackson.ResteasyJackson2Provider;

// extends and add these annotations for auto configuring
// @Provider
// @Consumes({"application/json", "application/*+json", "text/json"})
// @Produces({"application/json", "application/*+json", "text/json"})
public class CustomResteasyJackson2Provider extends ResteasyJackson2Provider {
	public CustomResteasyJackson2Provider() {
		setMapper(new CustomObjectMapper());
	}
}
