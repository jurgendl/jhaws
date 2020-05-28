package org.jhaws.common.web.wicket.waypoints;

import org.jhaws.common.web.wicket.JavaScriptResourceReference;
import org.jhaws.common.web.wicket.jquery.JQuery;

// var waypoint = new Waypoint({
// element: document.getElementById('basic-waypoint'),
// handler: function() {
// notify('Basic waypoint triggered')
// }
// })
//
// http://imakewebthings.com/waypoints/guides/getting-started/
// v4.0.1
public class Waypoints {
    public static JavaScriptResourceReference JS = new JavaScriptResourceReference(Waypoints.class, "jquery.waypoints.js");

    public static JavaScriptResourceReference JS_STICKY = new JavaScriptResourceReference(Waypoints.class, "shortcuts/sticky.js");

    public static JavaScriptResourceReference JS_INVIEW = new JavaScriptResourceReference(Waypoints.class, "shortcuts/inview.js");

    public static JavaScriptResourceReference JS_INFINITE = new JavaScriptResourceReference(Waypoints.class, "shortcuts/infinite.js");

    static {
        try {
            JS.addJavaScriptResourceReferenceDependency(JQuery.getJQueryReference());
        } catch (Exception ex) {
            //
        }
        JS_STICKY.addJavaScriptResourceReferenceDependency(JS);
        JS_INVIEW.addJavaScriptResourceReferenceDependency(JS);
        JS_INFINITE.addJavaScriptResourceReferenceDependency(JS);
    }
}
