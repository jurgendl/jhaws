package org.tools.hqlbuilder.webservice.bootstrap4.colorpicker;

import org.jhaws.common.web.wicket.CssResourceReference;
import org.jhaws.common.web.wicket.JavaScriptResourceReference;
import org.tools.hqlbuilder.webservice.bootstrap4.Bootstrap4;

// response.render(CssHeaderItem.forReference(BootstrapColorPicker.CSS));
// response.render(JavaScriptHeaderItem.forReference(BootstrapColorPicker.JS));
// response.render(BootstrapColorPicker.FACTORY);
//
// https://farbelous.github.io/bootstrap-colorpicker/
// 3.1.1
public class BootstrapColorPicker {
	public static final JavaScriptResourceReference JS = new JavaScriptResourceReference(BootstrapColorPicker.class,
			"js/bootstrap-colorpicker.js");

	public static final CssResourceReference CSS = new CssResourceReference(BootstrapColorPicker.class,
			"css/bootstrap-colorpicker.css");

	public static final String FACTORY = ";$('.colorpicker-component').colorpicker();";

	static {
		JS.addJavaScriptResourceReferenceDependency(Bootstrap4.JS);
		// CSS.addCssResourceReferenceDependency(Bootstrap4.getCSS());
	}
}
