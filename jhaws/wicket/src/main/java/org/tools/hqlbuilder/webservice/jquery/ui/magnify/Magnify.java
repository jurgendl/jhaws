package org.tools.hqlbuilder.webservice.jquery.ui.magnify;

import org.tools.hqlbuilder.webservice.wicket.CssResourceReference;
import org.tools.hqlbuilder.webservice.wicket.JavaScriptResourceReference;
import org.tools.hqlbuilder.webservice.wicket.WicketApplication;

/**
 * @see http://dimsemenov.com/plugins/magnific-popup/documentation.html
 */
public class Magnify {
    public static JavaScriptResourceReference MAGNIFY_JS = new JavaScriptResourceReference(Magnify.class, "magnific-popup.js")
    .addJavaScriptResourceReferenceDependency(WicketApplication.get().getJavaScriptLibrarySettings().getJQueryReference());

    public static CssResourceReference MAGNIFY_CSS = new CssResourceReference(Magnify.class, "magnific-popup.css");

    public static final String MAGNIFY_CLASS = "mfp-hide";
}
