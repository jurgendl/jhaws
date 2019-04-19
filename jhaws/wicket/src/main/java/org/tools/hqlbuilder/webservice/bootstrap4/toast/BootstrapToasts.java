package org.tools.hqlbuilder.webservice.bootstrap4.toast;

import org.tools.hqlbuilder.webservice.bootstrap4.Bootstrap4;
import org.tools.hqlbuilder.webservice.wicket.CssResourceReference;
import org.tools.hqlbuilder.webservice.wicket.JavaScriptResourceReference;

public class BootstrapToasts {
    public static final JavaScriptResourceReference JS = new JavaScriptResourceReference(BootstrapToasts.class, "toast.js");

    public static final CssResourceReference CSS = new CssResourceReference(BootstrapToasts.class, "toast.css");

    static {
        JS.addJavaScriptResourceReferenceDependency(Bootstrap4.JS);
        CSS.addCssResourceReferenceDependency(Bootstrap4.CSS);
    }
}
