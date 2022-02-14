package org.jhaws.common.web.wicket.jquery_typeahead;

import org.jhaws.common.web.wicket.CssResourceReference;
import org.jhaws.common.web.wicket.JavaScriptResourceReference;
import org.jhaws.common.web.wicket.jquery.JQuery;

// v2.11.0
//
// http://www.runningcoder.org/jquerytypeahead/overview/
// http://www.runningcoder.org/jquerytypeahead/version/
// http://www.runningcoder.org/jquerytypeahead/demo/
// http://www.runningcoder.org/jquerytypeahead/documentation/
// https://github.com/running-coder/jquery-typeahead
// http://upgrade-bootstrap.bootply.com/
public class JqueryTypeAhead {
    public static final JavaScriptResourceReference JS = new JavaScriptResourceReference(JqueryTypeAhead.class, "jquery.typeahead.js");
    public static final CssResourceReference CSS = new CssResourceReference(JqueryTypeAhead.class, "jquery.typeahead.css");

    static {
        try {
            JS.addJavaScriptResourceReferenceDependency(JQuery.getJQueryReference());
        } catch (Exception e) {
            //
        }
    }
}
