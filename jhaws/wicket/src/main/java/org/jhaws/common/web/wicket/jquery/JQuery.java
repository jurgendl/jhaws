package org.jhaws.common.web.wicket.jquery;

import org.jhaws.common.web.wicket.JavaScriptResourceReference;
import org.jhaws.common.web.wicket.WicketApplication;

/**
 * version 1.12.4
 */
public class JQuery {
	public static final JavaScriptResourceReference JS = new JavaScriptResourceReference(JQuery.class,
			"jquery-2.2.4.js");

	public static org.apache.wicket.request.resource.JavaScriptResourceReference getJQueryReference() {
		return (org.apache.wicket.request.resource.JavaScriptResourceReference) WicketApplication.get()
				.getJavaScriptLibrarySettings().getJQueryReference();
	}
}
