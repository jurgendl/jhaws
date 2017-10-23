package org.tools.hqlbuilder.webservice.jquery.ui.typeahead;

import org.tools.hqlbuilder.webservice.jquery.ui.jquery.JQuery;
import org.tools.hqlbuilder.webservice.wicket.JavaScriptResourceReference;

// http://twitter.github.io/typeahead.js/examples/
public class TypeAhead {
    public static JavaScriptResourceReference JS = new JavaScriptResourceReference(TypeAhead.class, "typeahead.jquery.js");

    public static JavaScriptResourceReference JS_BLOODHOUND = new JavaScriptResourceReference(TypeAhead.class, "bloodhound.js");

    static {
        try {
            JS.addJavaScriptResourceReferenceDependency(JQuery.getJQueryReference());
        } catch (Exception ex) {
            //
        }
        JS_BLOODHOUND.addJavaScriptResourceReferenceDependency(JS);
    }
}