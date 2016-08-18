package org.tools.hqlbuilder.webservice.jquery.ui.pro_bars;

import org.tools.hqlbuilder.webservice.jquery.ui.jquery_appear.JqueryAppear;
import org.tools.hqlbuilder.webservice.wicket.CssResourceReference;
import org.tools.hqlbuilder.webservice.wicket.JavaScriptResourceReference;
import org.tools.hqlbuilder.webservice.wicket.WicketApplication;

/**
 * @see https://github.com/joemottershaw/pro-bars
 */
public class ProBars {
    public static JavaScriptResourceReference ProBars_JS = new JavaScriptResourceReference(ProBars.class, "pro-bars.js")
            .addJavaScriptResourceReferenceDependency(JqueryAppear.JqueryAppear_JS).addJavaScriptResourceReferenceDependency(
                    WicketApplication.get().getJavaScriptLibrarySettings().getJQueryReference());

    public static CssResourceReference ProBars_CSS = new CssResourceReference(ProBars.class, "pro-bars.css");
}
