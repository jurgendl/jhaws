package org.tools.hqlbuilder.webservice.bootstrap4.typeahead;

import org.tools.hqlbuilder.webservice.bootstrap4.Bootstrap4;
import org.tools.hqlbuilder.webservice.wicket.JavaScriptResourceReference;

// https://github.com/bassjobsen/Bootstrap-3-Typeahead
public class TypeAhead {
    public static JavaScriptResourceReference JS = new JavaScriptResourceReference(TypeAhead.class, "bootstrap3-typeahead.js");

    static {
        JS.addJavaScriptResourceReferenceDependency(org.tools.hqlbuilder.webservice.jquery.ui.typeahead.TypeAhead.JS);
        JS.addJavaScriptResourceReferenceDependency(Bootstrap4.JS);
    }
}
