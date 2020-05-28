package org.jhaws.common.web.wicket.typeahead;

import org.jhaws.common.web.wicket.Bootstrap4;
import org.jhaws.common.web.wicket.JavaScriptResourceReference;

// https://github.com/bassjobsen/Bootstrap-3-Typeahead
// !!!!!!!!!!! IMPORTANT FIX !!!!!!!!!!!
// search for ...
// https://github.com/bootstrap-tagsinput/bootstrap-tagsinput/issues/331
public class TypeAhead {
    public static JavaScriptResourceReference JS = new JavaScriptResourceReference(TypeAhead.class, "bootstrap3-typeahead.js");

    static {
        // JS.addJavaScriptResourceReferenceDependency(org.tools.hqlbuilder.webservice.jquery.ui.typeahead.TypeAhead.JS);
        JS.addJavaScriptResourceReferenceDependency(Bootstrap4.JS);
    }
}
