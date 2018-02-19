package org.tools.hqlbuilder.webservice.jquery.ui.offcanvaspush;

import org.tools.hqlbuilder.webservice.jquery.ui.jquery.JQuery;
import org.tools.hqlbuilder.webservice.wicket.CssResourceReference;
import org.tools.hqlbuilder.webservice.wicket.JavaScriptResourceReference;

/**
 * @see https://github.com/marc-andrew/off-canvas-infinity-push/releases
 * @see http://www.marcandrew.net/off-canvas-infinity-push/
 */
public class OffCanvasInfinityPush {
    public static JavaScriptResourceReference JS = new JavaScriptResourceReference(OffCanvasInfinityPush.class, "jquery.ma.infinitypush.js");

    static {
        try {
            JS.addJavaScriptResourceReferenceDependency(JQuery.getJQueryReference());
        } catch (Exception ex) {
            //
        }
    }

    public static CssResourceReference CSS = new CssResourceReference(OffCanvasInfinityPush.class, "jquery.ma.infinitypush.css");
}
