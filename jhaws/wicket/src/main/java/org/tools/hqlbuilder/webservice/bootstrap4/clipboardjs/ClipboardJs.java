package org.tools.hqlbuilder.webservice.bootstrap4.clipboardjs;

import org.tools.hqlbuilder.webservice.bootstrap4.Bootstrap4;
import org.tools.hqlbuilder.webservice.wicket.JavaScriptResourceReference;

public class ClipboardJs {
    public static final JavaScriptResourceReference JS = new JavaScriptResourceReference(ClipboardJs.class, "clipboard.js");

    static {
        JS.addJavaScriptResourceReferenceDependency(Bootstrap4.JS);
    }

    public static final String CLASS = "clipboard";

    public static String factory() {
        return ";new ClipboardJS('." + CLASS + "');";
    }
}
