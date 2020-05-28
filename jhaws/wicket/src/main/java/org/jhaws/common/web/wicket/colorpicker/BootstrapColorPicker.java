package org.jhaws.common.web.wicket.colorpicker;

import org.jhaws.common.web.wicket.Bootstrap4;
import org.jhaws.common.web.wicket.CssResourceReference;
import org.jhaws.common.web.wicket.JavaScriptResourceReference;

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
