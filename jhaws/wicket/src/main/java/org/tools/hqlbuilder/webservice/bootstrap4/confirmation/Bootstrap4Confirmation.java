package org.tools.hqlbuilder.webservice.bootstrap4.confirmation;

import org.jhaws.common.web.wicket.JavaScriptResourceReference;
import org.tools.hqlbuilder.webservice.bootstrap4.Bootstrap4;

public class Bootstrap4Confirmation {
    public static final JavaScriptResourceReference JS = new JavaScriptResourceReference(Bootstrap4Confirmation.class, "bootstrap-confirmation.js");

    static {
        JS.addJavaScriptResourceReferenceDependency(Bootstrap4.JS);
    }
}
