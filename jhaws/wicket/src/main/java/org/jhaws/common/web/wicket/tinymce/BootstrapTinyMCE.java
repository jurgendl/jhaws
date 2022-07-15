package org.jhaws.common.web.wicket.tinymce;

import org.jhaws.common.io.FilePath;
import org.jhaws.common.web.wicket.JavaScriptResourceReference;
import org.jhaws.common.web.wicket.jquery.JQuery;

// TODO https://www.tiny.cloud/docs/demo/local-upload/
// https://www.tinymce.com/
// https://www.tiny.cloud/docs/plugins/
// https://www.tinymce.com/docs/integrations/bootstrap/
// 4.9.2 (2018-12-17) community
public class BootstrapTinyMCE {
    public static final String CLASS = "tinymce";

    public static final JavaScriptResourceReference JS = new JavaScriptResourceReference(BootstrapTinyMCE.class, "v5/tinymce.js");

    public static final JavaScriptResourceReference JS_JQUERY = new JavaScriptResourceReference(BootstrapTinyMCE.class, "v5/jquery.tinymce.min.js");

    public static final JavaScriptResourceReference JS_PLUGIN_LINK = new JavaScriptResourceReference(BootstrapTinyMCE.class, "v5/plugins/link/plugin.js");

    public static final JavaScriptResourceReference JS_PLUGIN_CODE = new JavaScriptResourceReference(BootstrapTinyMCE.class, "v5/plugins/code/plugin.js");

    public static final JavaScriptResourceReference JS_PLUGIN_LISTS = new JavaScriptResourceReference(BootstrapTinyMCE.class, "v5/plugins/lists/plugin.js");

    public static final JavaScriptResourceReference JS_PLUGIN_ADVLIST = new JavaScriptResourceReference(BootstrapTinyMCE.class, "v5/plugins/advlist/plugin.js");

    public static final JavaScriptResourceReference JS_PLUGIN_AUTOLINK = new JavaScriptResourceReference(BootstrapTinyMCE.class, "v5/plugins/autolink/plugin.js");

    public static final JavaScriptResourceReference JS_PLUGIN_PRINT = new JavaScriptResourceReference(BootstrapTinyMCE.class, "v5/plugins/print/plugin.js");

    public static final JavaScriptResourceReference JS_PLUGIN_SEARCHREPLACE = new JavaScriptResourceReference(BootstrapTinyMCE.class, "v5/plugins/searchreplace/plugin.js");

    public static final JavaScriptResourceReference JS_PLUGIN_TABLE = new JavaScriptResourceReference(BootstrapTinyMCE.class, "v5/plugins/table/plugin.js");

    public static final JavaScriptResourceReference JS_PLUGIN_VISUALCHARS = new JavaScriptResourceReference(BootstrapTinyMCE.class, "v5/plugins/visualchars/plugin.js");

    public static final JavaScriptResourceReference JS_PLUGIN_PASTE = new JavaScriptResourceReference(BootstrapTinyMCE.class, "v5/plugins/paste/plugin.js");

    public static final JavaScriptResourceReference JS_PLUGIN_WORDCOUNT = new JavaScriptResourceReference(BootstrapTinyMCE.class, "v5/plugins/wordcount/plugin.js");

    public static final JavaScriptResourceReference JS_PLUGIN_ANCHOR = new JavaScriptResourceReference(BootstrapTinyMCE.class, "v5/plugins/anchor/plugin.js");

    public static final JavaScriptResourceReference JS_PLUGIN_CHARMAP = new JavaScriptResourceReference(BootstrapTinyMCE.class, "v5/plugins/charmap/plugin.js");

    public static final JavaScriptResourceReference JS_PLUGIN_TEXTCOLOR = new JavaScriptResourceReference(BootstrapTinyMCE.class, "v5/plugins/textcolor/plugin.js");

    public static final JavaScriptResourceReference JS_PLUGIN_COLORPICKER = new JavaScriptResourceReference(BootstrapTinyMCE.class, "v5/plugins/colorpicker/plugin.js");

    public static final JavaScriptResourceReference JS_PLUGIN_MEDIA = new JavaScriptResourceReference(BootstrapTinyMCE.class, "v5/plugins/media/plugin.js");

    public static final JavaScriptResourceReference JS_PLUGIN_HR = new JavaScriptResourceReference(BootstrapTinyMCE.class, "v5/plugins/hr/plugin.js");

    public static final JavaScriptResourceReference JS_PLUGIN_IMAGE = new JavaScriptResourceReference(BootstrapTinyMCE.class, "v5/plugins/image/plugin.js");

    public static final JavaScriptResourceReference JS_PLUGIN_INSERTDATETIME = new JavaScriptResourceReference(BootstrapTinyMCE.class, "v5/plugins/insertdatetime/plugin.js");

    public static final JavaScriptResourceReference JS_PLUGIN_HELP = new JavaScriptResourceReference(BootstrapTinyMCE.class, "v5/plugins/help/plugin.js");

    public static final JavaScriptResourceReference JS_PLUGIN_PREVIEW = new JavaScriptResourceReference(BootstrapTinyMCE.class, "v5/plugins/preview/plugin.js");

    // Prevent Bootstrap dialog from blocking focusin
    public static final String FIX_SCRIPT = " ; $(document).on('focusin', function(e) { if ($(e.target).closest(\".mce-window\").length) { e.stopImmediatePropagation(); } }); ";

    static {
        JS_JQUERY.addJavaScriptResourceReferenceDependency(JQuery.getJQueryReference());
        JS_JQUERY.addJavaScriptResourceReferenceDependency(JS);
        JS_PLUGIN_LINK.addJavaScriptResourceReferenceDependency(JS);
        JS_PLUGIN_CODE.addJavaScriptResourceReferenceDependency(JS);
        JS_PLUGIN_LISTS.addJavaScriptResourceReferenceDependency(JS);
        JS_PLUGIN_ADVLIST.addJavaScriptResourceReferenceDependency(JS_PLUGIN_LISTS);
        JS_PLUGIN_AUTOLINK.addJavaScriptResourceReferenceDependency(JS);
        JS_PLUGIN_PRINT.addJavaScriptResourceReferenceDependency(JS);
        JS_PLUGIN_SEARCHREPLACE.addJavaScriptResourceReferenceDependency(JS);
        JS_PLUGIN_TABLE.addJavaScriptResourceReferenceDependency(JS);
        JS_PLUGIN_VISUALCHARS.addJavaScriptResourceReferenceDependency(JS);
        JS_PLUGIN_PASTE.addJavaScriptResourceReferenceDependency(JS);
        JS_PLUGIN_WORDCOUNT.addJavaScriptResourceReferenceDependency(JS);
        JS_PLUGIN_ANCHOR.addJavaScriptResourceReferenceDependency(JS);
        JS_PLUGIN_CHARMAP.addJavaScriptResourceReferenceDependency(JS);
        JS_PLUGIN_TEXTCOLOR.addJavaScriptResourceReferenceDependency(JS);
        JS_PLUGIN_COLORPICKER.addJavaScriptResourceReferenceDependency(JS_PLUGIN_TEXTCOLOR);
        JS_PLUGIN_MEDIA.addJavaScriptResourceReferenceDependency(JS);
        JS_PLUGIN_HR.addJavaScriptResourceReferenceDependency(JS);
        JS_PLUGIN_IMAGE.addJavaScriptResourceReferenceDependency(JS);
        JS_PLUGIN_INSERTDATETIME.addJavaScriptResourceReferenceDependency(JS);
        JS_PLUGIN_HELP.addJavaScriptResourceReferenceDependency(JS);
        JS_PLUGIN_PREVIEW.addJavaScriptResourceReferenceDependency(JS);
    }

    public static final String FACTORY = new FilePath(BootstrapTinyMCE.class, "tinymce-facory.js").readAll();
}
