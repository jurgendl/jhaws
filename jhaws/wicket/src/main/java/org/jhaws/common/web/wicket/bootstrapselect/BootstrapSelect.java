package org.jhaws.common.web.wicket.bootstrapselect;

import org.jhaws.common.web.wicket.CssResourceReference;
import org.jhaws.common.web.wicket.JavaScriptResourceReference;
import org.jhaws.common.web.wicket.jquery.JQuery;

// 1.13.14
// https://developer.snapappointments.com/bootstrap-select/
public class BootstrapSelect {
    public static final JavaScriptResourceReference JS = new JavaScriptResourceReference(BootstrapSelect.class, "js/bootstrap-select.js");

    public static final CssResourceReference CSS = new CssResourceReference(BootstrapSelect.class, "css/bootstrap-select.css");

    static {
        JS.addJavaScriptResourceReferenceDependency(JQuery.getJQueryReference());
        // CSS.addCssResourceReferenceDependency(Bootstrap4.getCSS());
    }
}
