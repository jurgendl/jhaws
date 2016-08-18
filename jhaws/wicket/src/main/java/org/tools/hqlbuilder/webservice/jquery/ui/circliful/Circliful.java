package org.tools.hqlbuilder.webservice.jquery.ui.circliful;

import org.tools.hqlbuilder.webservice.jquery.ui.jquery.JQuery;
import org.tools.hqlbuilder.webservice.wicket.CssResourceReference;
import org.tools.hqlbuilder.webservice.wicket.JavaScriptResourceReference;

/**
 * @see http://circliful.gutersohn.biz/
 */
public class Circliful {
    public static final JavaScriptResourceReference CIRCLIFUL_JS = new JavaScriptResourceReference(Circliful.class, "js/jquery.circliful.js");

    static {
        try {
            CIRCLIFUL_JS.addJavaScriptResourceReferenceDependency(JQuery.getJQueryReference());
        } catch (Exception ex) {
            //
        }
    }

    public static final CssResourceReference CIRCLIFUL_CSS = new CssResourceReference(Circliful.class, "css/jquery.circliful.css");
}
