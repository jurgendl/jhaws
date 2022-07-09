package org.jhaws.common.web.wicket;

import org.apache.wicket.request.resource.CssResourceReference;
import org.jhaws.common.web.wicket.jquery.JQuery;

// TODO bootstrap-icons folder ; https://icons.getbootstrap.com/

// v4.5.0
//
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
			"js/bootstrap.bundle.js");

	// IE10 viewport hack for Surface/desktop Windows 8 bug
	public static final JavaScriptResourceReference JS_IE10FIX = new JavaScriptResourceReference(Bootstrap4.class,
			"js/ie10-viewport-bug-workaround.js");

	public static final CssResourceReference CSS = new CssResourceReference(Bootstrap4.class, "css/bootstrap.css");

	static {
		JS.addJavaScriptResourceReferenceDependency(JQuery.getJQueryReference());
		JS_IE10FIX.addJavaScriptResourceReferenceDependency(JS);
	}

	public static final CssResourceReference MENLO = new CssResourceReference(Bootstrap4.class, "theme/menlo.css");

	public static CssResourceReference theme(String name) {
		return new CssResourceReference(Bootstrap4.class, "theme/" + name + "/bootstrap.css");
	}

	public static final CssResourceReference BRANDS = new CssResourceReference(Bootstrap4.class, "social/brand.css");

}
