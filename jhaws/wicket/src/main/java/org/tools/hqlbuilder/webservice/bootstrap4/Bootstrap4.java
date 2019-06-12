package org.tools.hqlbuilder.webservice.bootstrap4;

import org.tools.hqlbuilder.webservice.jquery.ui.jquery.JQuery;
import org.tools.hqlbuilder.webservice.wicket.CssResourceReference;
import org.tools.hqlbuilder.webservice.wicket.JavaScriptResourceReference;

// v4.3.1 (previous: v4.2.1-v4.1.3)
// https://blog.getbootstrap.com/2018/12/21/bootstrap-4-2-1/
//
// http://getbootstrap.com/
// https://hackerthemes.com/bootstrap-cheatsheet/
// https://bootsnipp.com/tags/4.0.0
//
//
// https://bootsnipp.com/snippets/gv6Pe
// https://bootsnipp.com/snippets/7KGEV
// https://bootsnipp.com/snippets/nrQWa
// https://bootsnipp.com/snippets/orPaP
// https://bootsnipp.com/snippets/denxZ
// https://bootsnipp.com/snippets/rleW6
// https://getbootstrap.com/docs/4.0/components/modal/
//
//
// http://davidstutz.github.io/bootstrap-multiselect/
// http://formvalidation.io/examples/bootstrap-multiselect/
//
// https://codepen.io/Funstarter/pen/pJZooM
//
// https://startbootstrap.com/bootstrap-resources
//
// https://codexui.com/components/
public class Bootstrap4 {
	public static final JavaScriptResourceReference JS = new JavaScriptResourceReference(Bootstrap4.class,
			"js/bootstrap.js");

	// IE10 viewport hack for Surface/desktop Windows 8 bug
	public static final JavaScriptResourceReference JS_IE10FIX = new JavaScriptResourceReference(Bootstrap4.class,
			"js/ie10-viewport-bug-workaround.js");

	public static final JavaScriptResourceReference JS_POPPER = new JavaScriptResourceReference(Bootstrap4.class,
			"js/popper.min.js");

	public static CssResourceReference CSS = new CssResourceReference(Bootstrap4.class, "css/bootstrap.css");

	// public static final CssResourceReference CSS_GRID = new
	// CssResourceReference(Bootstrap4.class,
	// "css/bootstrap-grid.css");
	//
	// public static final CssResourceReference CSS_REBOOT = new
	// CssResourceReference(Bootstrap4.class,
	// "css/bootstrap-reboot.css");

	static {
		JS.addJavaScriptResourceReferenceDependency(JQuery.getJQueryReference());
		JS_IE10FIX.addJavaScriptResourceReferenceDependency(JS);
		JS.addJavaScriptResourceReferenceDependency(JS_POPPER);
		// CSS_GRID.addCssResourceReferenceDependency(CSS);
		// CSS_REBOOT.addCssResourceReferenceDependency(CSS);
	}

	public static final CssResourceReference MENLO = new CssResourceReference(Bootstrap4.class, "theme/menlo.css");

	public static CssResourceReference theme(String name) {
		return new CssResourceReference(Bootstrap4.class, "theme/" + name + "/bootstrap.css");
	}
}
