package org.jhaws.common.web.wicket.eventmove;

import org.jhaws.common.web.wicket.JavaScriptResourceReference;
import org.jhaws.common.web.wicket.jquery.JQuery;

// https://github.com/stephband/jquery.event.move
public class JQueryEventMove {
    public static JavaScriptResourceReference JS_EVENT_MOVE = new JavaScriptResourceReference(JQueryEventMove.class, "jquery.event.move.js");

    static {
        try {
            JS_EVENT_MOVE.addJavaScriptResourceReferenceDependency(JQuery.getJQueryReference());
        } catch (Exception ex) {
            //
        }
    }
}
