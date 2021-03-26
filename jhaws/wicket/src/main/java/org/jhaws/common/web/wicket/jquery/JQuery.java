package org.jhaws.common.web.wicket.jquery;

import org.jhaws.common.web.wicket.JavaScriptResourceReference;

public class JQuery {
	public static final JavaScriptResourceReference JS = new JavaScriptResourceReference(JQuery.class, "jquery.js");

	public static org.apache.wicket.request.resource.JavaScriptResourceReference getJQueryReference() {
		return JS;
	}

//	public static org.apache.wicket.request.resource.JavaScriptResourceReference getJQueryReference() {
//		return (org.apache.wicket.request.resource.JavaScriptResourceReference) WicketApplication.get()
//				.getJavaScriptLibrarySettings().getJQueryReference();
//	}
}
