package org.jhaws.common.web.wicket.datetimepicker.tempusdominus;

import org.jhaws.common.io.FilePath;
import org.jhaws.common.web.wicket.Bootstrap4;
import org.jhaws.common.web.wicket.CssResourceReference;
import org.jhaws.common.web.wicket.JavaScriptResourceReference;
import org.jhaws.common.web.wicket.moment.MomentJs;

// https://github.com/tempusdominus/bootstrap-4
// https://tempusdominus.github.io/bootstrap-4/
// 5.1.2
public class BootstrapTempusDominusDateTimePicker {
	public static final JavaScriptResourceReference JS = new JavaScriptResourceReference(
			BootstrapTempusDominusDateTimePicker.class, "js/tempusdominus-bootstrap-4.js");

	public static final CssResourceReference CSS = new CssResourceReference(BootstrapTempusDominusDateTimePicker.class,
			"css/tempusdominus-bootstrap-4.css");

	public static final String FACTORY = new FilePath(BootstrapTempusDominusDateTimePicker.class,
			"js/tempusdominus-bootstrap-4-factory.js").readAll();

	static {
		JS.addJavaScriptResourceReferenceDependency(Bootstrap4.JS);
		JS.addJavaScriptResourceReferenceDependency(MomentJs.JS);
		// CSS.addCssResourceReferenceDependency(Bootstrap4.getCSS());
		// CSS.addCssResourceReferenceDependency(FontAwesome.CSS4);
	}
}
