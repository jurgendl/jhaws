package org.jhaws.common.web.wicket.toggle;

import org.jhaws.common.web.wicket.Bootstrap4;
import org.jhaws.common.web.wicket.CssResourceReference;
import org.jhaws.common.web.wicket.JavaScriptResourceReference;

// https://gitbrent.github.io/bootstrap4-toggle/
public class Toggle {
	public static final JavaScriptResourceReference JS = new JavaScriptResourceReference(Toggle.class,
			"js/bootstrap4-toggle.js");

	public static final CssResourceReference CSS = new CssResourceReference(Toggle.class, "css/bootstrap4-toggle.css");

	static {
		JS.addJavaScriptResourceReferenceDependency(Bootstrap4.JS);
		// CSS.addCssResourceReferenceDependency(Bootstrap4.getCSS());
	}
}
