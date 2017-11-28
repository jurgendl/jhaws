package org.tools.hqlbuilder.webservice.jquery.ui.jstree;

import org.tools.hqlbuilder.webservice.jquery.ui.jquery.JQuery;
import org.tools.hqlbuilder.webservice.wicket.CssResourceReference;
import org.tools.hqlbuilder.webservice.wicket.JavaScriptResourceReference;

// https://www.jstree.com/
// https://github.com/vakata/jstree/
// 3.3.4
public class JsTree {
    public static JavaScriptResourceReference JS = new JavaScriptResourceReference(JsTree.class, "jstree.js");

    public static CssResourceReference CSS_THEME_DEFAULT = new CssResourceReference(JsTree.class, "themes/default/style.css");

    public static CssResourceReference CSS_THEME_DEFAULT_DARK = new CssResourceReference(JsTree.class, "themes/default-dark/style.css");

    static {
        try {
            JS.addJavaScriptResourceReferenceDependency(JQuery.getJQueryReference());
        } catch (Exception e) {
            //
        }
    }
}
