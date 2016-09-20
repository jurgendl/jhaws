package org.tools.hqlbuilder.webservice.jquery.ui.moment;

import org.tools.hqlbuilder.webservice.wicket.JavaScriptResourceReference;

/**
 * @see http://momentjs.com/
 */
public class MomentJs {
	public static JavaScriptResourceReference JS = new JavaScriptResourceReference(MomentJs.class, "moment.js");

	public static JavaScriptResourceReference JS_LOCALE = new JavaScriptResourceReference(MomentJs.class, "moment-with-locales.js");

	public static JavaScriptResourceReference JS_PRECISE_RANGE = new JavaScriptResourceReference(MomentJs.class, "moment-precise-range.js");

	public static JavaScriptResourceReference JS_JAVA_FORMAT = new JavaScriptResourceReference(MomentJs.class, "moment-jdateformatparser");
}
