package org.tools.hqlbuilder.webservice.bootstrap4.tinymce;

import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.tools.hqlbuilder.webservice.jquery.ui.jquery.JQuery;
import org.tools.hqlbuilder.webservice.wicket.JavaScriptResourceReference;

// https://www.tinymce.com/
// https://www.tinymce.com/docs/integrations/bootstrap/
// 4.8.3 community
public class BootstrapTinyMCE {
    public static final JavaScriptResourceReference JS = new JavaScriptResourceReference(BootstrapTinyMCE.class, "js/tinymce/tinymce.min.js");

    public static final JavaScriptResourceReference JS_JQUERY = new JavaScriptResourceReference(BootstrapTinyMCE.class,
            "js/tinymce/jquery.tinymce.min.js");

    // Prevent Bootstrap dialog from blocking focusin
    public static final OnDomReadyHeaderItem FACTORY = OnDomReadyHeaderItem.forScript(
            " ; $(document).on('focusin', function(e) { if ($(e.target).closest(\".mce-window\").length) { e.stopImmediatePropagation(); } }); ; tinymce.init({ selector : '.tinymce' }) ; ");

    static {
        JS_JQUERY.addJavaScriptResourceReferenceDependency(JQuery.getJQueryReference());
        JS_JQUERY.addJavaScriptResourceReferenceDependency(JS);
    }
}
