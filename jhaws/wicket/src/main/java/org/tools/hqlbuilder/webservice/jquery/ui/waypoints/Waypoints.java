package org.tools.hqlbuilder.webservice.jquery.ui.waypoints;

import org.tools.hqlbuilder.webservice.jquery.ui.jquery.JQuery;
import org.tools.hqlbuilder.webservice.wicket.JavaScriptResourceReference;

// var waypoint = new Waypoint({
// element: document.getElementById('basic-waypoint'),
// handler: function() {
// notify('Basic waypoint triggered')
// }
// })
public class Waypoints {
    public static JavaScriptResourceReference JS = new JavaScriptResourceReference(Waypoints.class, "jquery.waypoints.js");

    static {
        try {
            JS.addJavaScriptResourceReferenceDependency(JQuery.getJQueryReference());
        } catch (Exception ex) {
            //
        }
    }
}
