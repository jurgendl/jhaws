package org.jhaws.common.web.wicket.qtip;

import org.jhaws.common.web.wicket.CssResourceReference;
import org.jhaws.common.web.wicket.JavaScriptResourceReference;
import org.jhaws.common.web.wicket.WicketApplication;

// v3.0.3
/**
 * @see http://qtip2.com
 */
public class QTip {
    public static JavaScriptResourceReference JS = new JavaScriptResourceReference(QTip.class, "jquery.qtip.js")
            .addJavaScriptResourceReferenceDependency(WicketApplication.get().getJavaScriptLibrarySettings().getJQueryReference());

    public static CssResourceReference CSS = new CssResourceReference(QTip.class, "jquery.qtip.css");
}
