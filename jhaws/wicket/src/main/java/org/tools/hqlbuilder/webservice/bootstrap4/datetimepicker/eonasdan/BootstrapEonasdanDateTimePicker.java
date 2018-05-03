package org.tools.hqlbuilder.webservice.bootstrap4.datetimepicker.eonasdan;

import org.tools.hqlbuilder.webservice.bootstrap4.Bootstrap4;
import org.tools.hqlbuilder.webservice.jquery.ui.moment.MomentJs;
import org.tools.hqlbuilder.webservice.wicket.CssResourceReference;
import org.tools.hqlbuilder.webservice.wicket.JavaScriptResourceReference;

// http://eonasdan.github.io/bootstrap-datetimepicker/Installing/#manual
// https://github.com/Eonasdan/bootstrap-datetimepicker
// 4.17.47
public class BootstrapEonasdanDateTimePicker {
	public static final JavaScriptResourceReference JS = new JavaScriptResourceReference(
			BootstrapEonasdanDateTimePicker.class, "js/bootstrap-datetimepicker.min.js");

	public static final CssResourceReference CSS = new CssResourceReference(BootstrapEonasdanDateTimePicker.class,
			"css/bootstrap-datetimepicker.css");

	static {
		JS.addJavaScriptResourceReferenceDependency(Bootstrap4.JS);
		JS.addJavaScriptResourceReferenceDependency(MomentJs.JS);
		CSS.addCssResourceReferenceDependency(Bootstrap4.CSS);
		// CSS.addCssResourceReferenceDependency(FontAwesome.CSS4);
	}
}
