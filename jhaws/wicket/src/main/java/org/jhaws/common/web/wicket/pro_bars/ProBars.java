package org.jhaws.common.web.wicket.pro_bars;

import org.jhaws.common.web.wicket.CssResourceReference;
import org.jhaws.common.web.wicket.JavaScriptResourceReference;
import org.jhaws.common.web.wicket.WicketApplication;
import org.jhaws.common.web.wicket.jquery_appear.JqueryAppear;

/**
 * @see https://github.com/joemottershaw/pro-bars
 */
public class ProBars {
    public static JavaScriptResourceReference ProBars_JS = new JavaScriptResourceReference(ProBars.class, "pro-bars.js")
            .addJavaScriptResourceReferenceDependency(JqueryAppear.JqueryAppear_JS)
            .addJavaScriptResourceReferenceDependency(WicketApplication.get().getJavaScriptLibrarySettings().getJQueryReference());

    public static CssResourceReference ProBars_CSS = new CssResourceReference(ProBars.class, "pro-bars.css");
}
