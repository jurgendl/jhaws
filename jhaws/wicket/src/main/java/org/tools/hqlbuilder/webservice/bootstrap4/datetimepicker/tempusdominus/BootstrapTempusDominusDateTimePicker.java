package org.tools.hqlbuilder.webservice.bootstrap4.datetimepicker.tempusdominus;

import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.jhaws.common.io.FilePath;
import org.tools.hqlbuilder.webservice.bootstrap4.Bootstrap4;
import org.tools.hqlbuilder.webservice.jquery.ui.moment.MomentJs;
import org.tools.hqlbuilder.webservice.wicket.CssResourceReference;
import org.tools.hqlbuilder.webservice.wicket.JavaScriptResourceReference;

// https://github.com/tempusdominus/bootstrap-4
// https://tempusdominus.github.io/bootstrap-4/
// 5.0.0
public class BootstrapTempusDominusDateTimePicker {
	public static final JavaScriptResourceReference JS = new JavaScriptResourceReference(
			BootstrapTempusDominusDateTimePicker.class, "js/tempusdominus-bootstrap-4.js");

	public static final CssResourceReference CSS = new CssResourceReference(BootstrapTempusDominusDateTimePicker.class,
			"css/tempusdominus-bootstrap-4.css");

	public static final OnDomReadyHeaderItem FACTORY = OnDomReadyHeaderItem.forScript(
			new FilePath(BootstrapTempusDominusDateTimePicker.class, "js/tempusdominus-bootstrap-4-factory.js")
					.readAll());

	static {
		JS.addJavaScriptResourceReferenceDependency(Bootstrap4.JS);
		JS.addJavaScriptResourceReferenceDependency(MomentJs.JS);
		CSS.addCssResourceReferenceDependency(Bootstrap4.CSS);
		// CSS.addCssResourceReferenceDependency(FontAwesome.CSS4);
	}
}
