package org.tools.hqlbuilder.webservice.bootstrap4;

import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.jhaws.common.io.FilePath;
import org.tools.hqlbuilder.webservice.jquery.ui.jquery.JQuery;
import org.tools.hqlbuilder.webservice.wicket.CssResourceReference;
import org.tools.hqlbuilder.webservice.wicket.JavaScriptResourceReference;

// http://getbootstrap.com/
// https://hackerthemes.com/bootstrap-cheatsheet/
public class Bootstrap4 {
    public static final JavaScriptResourceReference JS = new JavaScriptResourceReference(Bootstrap4.class, "js/bootstrap.js");

    // IE10 viewport hack for Surface/desktop Windows 8 bug
    public static final JavaScriptResourceReference JS_IE10FIX = new JavaScriptResourceReference(Bootstrap4.class,
            "js/ie10-viewport-bug-workaround.js");

    public static final JavaScriptResourceReference JS_POPPER = new JavaScriptResourceReference(Bootstrap4.class, "js/popper.min.js");

    public static final CssResourceReference CSS = new CssResourceReference(Bootstrap4.class, "css/bootstrap.css");

    public static final CssResourceReference CSS_GRID = new CssResourceReference(Bootstrap4.class, "css/bootstrap-grid.css");

    public static final CssResourceReference CSS_REBOOT = new CssResourceReference(Bootstrap4.class, "css/bootstrap-reboot.css");

    public static final OnDomReadyHeaderItem FACTORY = OnDomReadyHeaderItem
            .forScript(new FilePath(Bootstrap4.class, "js/bootstrap.factory.js").readAll());

    {
        JS.addJavaScriptResourceReferenceDependency(JQuery.getJQueryReference());
        JS_IE10FIX.addJavaScriptResourceReferenceDependency(JS);
        JS.addJavaScriptResourceReferenceDependency(JS_POPPER);
        CSS_GRID.addCssResourceReferenceDependency(CSS);
        CSS_REBOOT.addCssResourceReferenceDependency(CSS);
    }
}
