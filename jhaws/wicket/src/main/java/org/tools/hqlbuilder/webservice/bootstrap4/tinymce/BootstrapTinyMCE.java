package org.tools.hqlbuilder.webservice.bootstrap4.tinymce;

import org.tools.hqlbuilder.webservice.jquery.ui.jquery.JQuery;
import org.tools.hqlbuilder.webservice.wicket.JavaScriptResourceReference;

// https://www.tinymce.com/
// https://www.tinymce.com/docs/integrations/bootstrap/
// 4.7.1 community
public class BootstrapTinyMCE {
    public static final JavaScriptResourceReference JS = new JavaScriptResourceReference(BootstrapTinyMCE.class, "js/tinymce/tinymce.min.js");

    public static final JavaScriptResourceReference JS_JQUERY = new JavaScriptResourceReference(BootstrapTinyMCE.class,
            "js/tinymce/jquery.tinymce.min.js");

    static {
        JS_JQUERY.addJavaScriptResourceReferenceDependency(JQuery.getJQueryReference());
        JS_JQUERY.addJavaScriptResourceReferenceDependency(JS);
    }
}
