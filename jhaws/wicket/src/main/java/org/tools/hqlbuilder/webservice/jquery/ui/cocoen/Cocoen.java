package org.tools.hqlbuilder.webservice.jquery.ui.cocoen;

import org.tools.hqlbuilder.webservice.jquery.ui.jquery.JQuery;
import org.tools.hqlbuilder.webservice.wicket.CssResourceReference;
import org.tools.hqlbuilder.webservice.wicket.JavaScriptResourceReference;

// $('.cocoen').cocoen();
//
// <div class="container p-0">
// <div class="cocoen" wicket:id="compare">
// <img wicket:id="first" src="" />
// <img wicket:id="last" src="" />
// </div>
// </div>
//
// https://github.com/koenoe/cocoen
// 2.0.5
public class Cocoen {
    public static JavaScriptResourceReference JS = new JavaScriptResourceReference(Cocoen.class, "js/cocoen.min.js");

    public static JavaScriptResourceReference JS_JQUERY = new JavaScriptResourceReference(Cocoen.class, "js/cocoen-jquery.min.js");

    public static CssResourceReference CSS = new CssResourceReference(Cocoen.class, "css/cocoen.min.css");

    static {
        try {
            JS_JQUERY.addJavaScriptResourceReferenceDependency(JS);
            JS_JQUERY.addJavaScriptResourceReferenceDependency(JQuery.getJQueryReference());
        } catch (Exception ex) {
            //
        }
    }
}
