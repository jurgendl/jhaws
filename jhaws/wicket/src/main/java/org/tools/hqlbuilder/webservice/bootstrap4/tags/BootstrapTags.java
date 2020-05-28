package org.tools.hqlbuilder.webservice.bootstrap4.tags;

import org.jhaws.common.web.wicket.CssResourceReference;
import org.jhaws.common.web.wicket.JavaScriptResourceReference;
import org.tools.hqlbuilder.webservice.bootstrap4.Bootstrap4;

/**
 * bootstrap v3
 * http://bootstrap-tagsinput.github.io/bootstrap-tagsinput/examples/<br>
 * bootstrap v4 (modified from bootstrap v3)
 * https://github.com/Nodws/bootstrap4-tagsinput<br>
 * <style>.bootstrap-tagsinput .badge [data-role="remove"]::after {
 * background-color: rgba(0, 0, 0, 0); font-size: 11px; }</style>
 */
public class BootstrapTags {
	public static final JavaScriptResourceReference JS = new JavaScriptResourceReference(BootstrapTags.class,
			"tagsinput.js");

	public static final CssResourceReference CSS = new CssResourceReference(BootstrapTags.class, "tagsinput.css");

	static {
		JS.addJavaScriptResourceReferenceDependency(Bootstrap4.JS);
		// CSS.addCssResourceReferenceDependency(Bootstrap4.getCSS());
	}
}
