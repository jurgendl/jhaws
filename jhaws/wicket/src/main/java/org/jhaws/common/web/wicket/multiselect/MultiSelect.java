package org.jhaws.common.web.wicket.multiselect;

import org.jhaws.common.io.FilePath;
import org.jhaws.common.web.wicket.Bootstrap4;
import org.jhaws.common.web.wicket.CssResourceReference;
import org.jhaws.common.web.wicket.JavaScriptResourceReference;

// http://davidstutz.github.io/bootstrap-multiselect/
// https://github.com/davidstutz/bootstrap-multiselect
// 0.9.13
public class MultiSelect {
    public static final JavaScriptResourceReference JS = new JavaScriptResourceReference(MultiSelect.class, "js/bootstrap-multiselect.js");

    public static final JavaScriptResourceReference JS_COLLAPSIBLE = new JavaScriptResourceReference(MultiSelect.class, "js/bootstrap-multiselect-collapsible-groups.js");

    public static final CssResourceReference CSS = new CssResourceReference(MultiSelect.class, "css/bootstrap-multiselect.css");

    public static final String FACTORY = new FilePath(MultiSelect.class, "MultiSelect-factory.js").readAll();

    static {
        JS.addJavaScriptResourceReferenceDependency(Bootstrap4.JS);
        // CSS.addCssResourceReferenceDependency(Bootstrap4.getCSS());
    }
}
