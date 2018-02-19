package org.tools.hqlbuilder.webservice.jquery.ui.qtip;

import org.tools.hqlbuilder.webservice.wicket.CssResourceReference;
import org.tools.hqlbuilder.webservice.wicket.JavaScriptResourceReference;
import org.tools.hqlbuilder.webservice.wicket.WicketApplication;

/**
 * @see http://qtip2.com
 */
public class QTip {
    public static JavaScriptResourceReference QTIP_JS = new JavaScriptResourceReference(QTip.class, "jquery.qtip.js")
            .addJavaScriptResourceReferenceDependency(WicketApplication.get().getJavaScriptLibrarySettings().getJQueryReference());

    public static CssResourceReference QTIP_CSS = new CssResourceReference(QTip.class, "jquery.qtip.css");
}
