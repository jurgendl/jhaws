package org.jhaws.common.web.wicket.confirmation;

import org.jhaws.common.web.wicket.Bootstrap4;
import org.jhaws.common.web.wicket.JavaScriptResourceReference;

public class Bootstrap4Confirmation {
    public static final JavaScriptResourceReference JS = new JavaScriptResourceReference(Bootstrap4Confirmation.class, "bootstrap-confirmation.js");

    static {
        JS.addJavaScriptResourceReferenceDependency(Bootstrap4.JS);
    }
}
