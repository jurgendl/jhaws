package org.jhaws.common.web.wicket.offcanvaspush;

import org.jhaws.common.web.wicket.CssResourceReference;
import org.jhaws.common.web.wicket.JavaScriptResourceReference;
import org.jhaws.common.web.wicket.jquery.JQuery;

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
