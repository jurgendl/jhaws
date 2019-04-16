package org.tools.hqlbuilder.webservice.bootstrap4.bootbox;

import org.tools.hqlbuilder.webservice.bootstrap4.Bootstrap4;
import org.tools.hqlbuilder.webservice.wicket.JavaScriptResourceReference;

// http://bootboxjs.com/examples.html
public class BootBox {
    public static final JavaScriptResourceReference JS = new JavaScriptResourceReference(BootBox.class, "bootbox.js");

    public static final JavaScriptResourceReference JS_LOCALE = new JavaScriptResourceReference(BootBox.class, "bootbox.locales.js");

    static {
        JS.addJavaScriptResourceReferenceDependency(Bootstrap4.JS);
        JS_LOCALE.addJavaScriptResourceReferenceDependency(JS);
    }
}
