package org.jhaws.common.web.wicket.typeahead;

import org.jhaws.common.web.wicket.JavaScriptResourceReference;
import org.jhaws.common.web.wicket.jquery.JQuery;

// http://twitter.github.io/typeahead.js/examples/
public class TypeAhead {
    public static JavaScriptResourceReference JS = new JavaScriptResourceReference(TypeAhead.class, "typeahead.jquery.js");

    public static JavaScriptResourceReference JS_BLOODHOUND = new JavaScriptResourceReference(TypeAhead.class, "bloodhound.js");

    static {
        JS.addJavaScriptResourceReferenceDependency(JQuery.getJQueryReference());
        JS_BLOODHOUND.addJavaScriptResourceReferenceDependency(JQuery.getJQueryReference());
    }
}
