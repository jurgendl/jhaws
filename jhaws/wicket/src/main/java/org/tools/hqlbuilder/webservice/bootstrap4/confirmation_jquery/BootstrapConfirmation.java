package org.tools.hqlbuilder.webservice.bootstrap4.confirmation_jquery;

import org.tools.hqlbuilder.webservice.bootstrap4.Bootstrap4;
import org.tools.hqlbuilder.webservice.wicket.JavaScriptResourceReference;

public class BootstrapConfirmation {
    public static final JavaScriptResourceReference JS = new JavaScriptResourceReference(BootstrapConfirmation.class, "jquery.confirm.js");

    static {
        JS.addJavaScriptResourceReferenceDependency(Bootstrap4.JS);
    }
}
