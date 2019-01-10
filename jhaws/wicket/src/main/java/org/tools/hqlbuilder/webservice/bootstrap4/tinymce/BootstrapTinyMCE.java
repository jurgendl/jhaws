package org.tools.hqlbuilder.webservice.bootstrap4.tinymce;

import java.io.IOException;
import java.io.UncheckedIOException;

import org.jhaws.common.io.FilePath;
import org.tools.hqlbuilder.webservice.jquery.ui.jquery.JQuery;
import org.tools.hqlbuilder.webservice.wicket.JavaScriptResourceReference;

// https://www.tinymce.com/
// https://www.tinymce.com/docs/integrations/bootstrap/
// 4.9.2 (2018-12-17) community
public class BootstrapTinyMCE {
    public static final String CLASS = "tinymce";

    public static final JavaScriptResourceReference JS = new JavaScriptResourceReference(BootstrapTinyMCE.class, "js/tinymce/tinymce.js");

    public static final JavaScriptResourceReference JS_JQUERY = new JavaScriptResourceReference(BootstrapTinyMCE.class,
            "js/tinymce/jquery.tinymce.min.js");

    public static final JavaScriptResourceReference JS_PLUGIN_LINK = new JavaScriptResourceReference(BootstrapTinyMCE.class,
            "js/tinymce/plugins/link/plugin.js");

    public static final JavaScriptResourceReference JS_PLUGIN_CODE = new JavaScriptResourceReference(BootstrapTinyMCE.class,
            "js/tinymce/plugins/code/plugin.js");

    public static final JavaScriptResourceReference JS_PLUGIN_LISTS = new JavaScriptResourceReference(BootstrapTinyMCE.class,
            "js/tinymce/plugins/lists/plugin.js");

    public static final JavaScriptResourceReference JS_PLUGIN_AUTOLINK = new JavaScriptResourceReference(BootstrapTinyMCE.class,
            "js/tinymce/plugins/autolink/plugin.js");

    public static final JavaScriptResourceReference JS_PLUGIN_PRINT = new JavaScriptResourceReference(BootstrapTinyMCE.class,
            "js/tinymce/plugins/print/plugin.js");

    public static final JavaScriptResourceReference JS_PLUGIN_SEARCHREPLACE = new JavaScriptResourceReference(BootstrapTinyMCE.class,
            "js/tinymce/plugins/searchreplace/plugin.js");

    public static final JavaScriptResourceReference JS_PLUGIN_TABLE = new JavaScriptResourceReference(BootstrapTinyMCE.class,
            "js/tinymce/plugins/table/plugin.js");

    public static final JavaScriptResourceReference JS_PLUGIN_VISUALCHARS = new JavaScriptResourceReference(BootstrapTinyMCE.class,
            "js/tinymce/plugins/visualchars/plugin.js");

    public static final JavaScriptResourceReference JS_PLUGIN_PASTE = new JavaScriptResourceReference(BootstrapTinyMCE.class,
            "js/tinymce/plugins/paste/plugin.js");

    // Prevent Bootstrap dialog from blocking focusin
    public static final String FIX_SCRIPT = " ; $(document).on('focusin', function(e) { if ($(e.target).closest(\".mce-window\").length) { e.stopImmediatePropagation(); } }); ";

    static {
        JS_JQUERY.addJavaScriptResourceReferenceDependency(JQuery.getJQueryReference());
        JS_JQUERY.addJavaScriptResourceReferenceDependency(JS);
        JS_PLUGIN_LINK.addJavaScriptResourceReferenceDependency(JS);
        JS_PLUGIN_CODE.addJavaScriptResourceReferenceDependency(JS);
        JS_PLUGIN_LISTS.addJavaScriptResourceReferenceDependency(JS);
        JS_PLUGIN_AUTOLINK.addJavaScriptResourceReferenceDependency(JS);
        JS_PLUGIN_PRINT.addJavaScriptResourceReferenceDependency(JS);
        JS_PLUGIN_SEARCHREPLACE.addJavaScriptResourceReferenceDependency(JS);
        JS_PLUGIN_TABLE.addJavaScriptResourceReferenceDependency(JS);
        JS_PLUGIN_VISUALCHARS.addJavaScriptResourceReferenceDependency(JS);
        JS_PLUGIN_PASTE.addJavaScriptResourceReferenceDependency(JS);
    }

    public static String factory() {
        try {
            String factory = new String(new FilePath(BootstrapTinyMCE.class, "tinymce-facory.js").readAllBytes(), "utf-8");
            return factory;
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }
}
