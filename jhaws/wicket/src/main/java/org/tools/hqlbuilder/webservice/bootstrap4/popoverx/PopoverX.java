package org.tools.hqlbuilder.webservice.bootstrap4.popoverx;

import org.jhaws.common.web.wicket.CssResourceReference;
import org.jhaws.common.web.wicket.JavaScriptResourceReference;
import org.tools.hqlbuilder.webservice.bootstrap4.Bootstrap4;

public class PopoverX {
    public static final JavaScriptResourceReference JS = new JavaScriptResourceReference(PopoverX.class, "js/bootstrap-popover-x.js");

    public static CssResourceReference CSS = new CssResourceReference(PopoverX.class, "css/bootstrap-popover-x.css");

    static {
        JS.addJavaScriptResourceReferenceDependency(Bootstrap4.JS);
        // CSS.addCssResourceReferenceDependency(Bootstrap4.getCSS());
    }
}
