package org.tools.hqlbuilder.webservice.bootstrap4.confirmation;

import org.apache.wicket.markup.head.OnLoadHeaderItem;
import org.tools.hqlbuilder.webservice.bootstrap4.Bootstrap4;
import org.tools.hqlbuilder.webservice.wicket.JavaScriptResourceReference;

// http://bootstrap-confirmation.js.org/
// https://github.com/mistic100/Bootstrap-Confirmation/releases
// 2.4.0
// !!!! doesn't work with Bootstrap 4 !!!!
// https://github.com/mistic100/Bootstrap-Confirmation/issues/51
public class BootstrapConfirmation {
    public static final JavaScriptResourceReference JS = new JavaScriptResourceReference(BootstrapConfirmation.class, "bootstrap-confirmation.js");

    static {
        JS.addJavaScriptResourceReferenceDependency(Bootstrap4.JS);
    }

    public static final OnLoadHeaderItem JS_FACTOR = OnLoadHeaderItem
            .forScript(";$('[data-toggle=confirmation]').confirmation({rootSelector:'[data-toggle=confirmation]'});");
}
