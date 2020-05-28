package org.tools.hqlbuilder.webservice.jquery.ui.jquery;

import org.jhaws.common.web.wicket.WicketApplication;

/**
 * version 1.12.4
 */
public class JQuery {
	public static org.apache.wicket.request.resource.JavaScriptResourceReference getJQueryReference() {
		return (org.apache.wicket.request.resource.JavaScriptResourceReference) WicketApplication.get()
				.getJavaScriptLibrarySettings().getJQueryReference();
	}
}
