package org.tools.hqlbuilder.webservice.jquery.ui.magnify;

import org.tools.hqlbuilder.webservice.jquery.ui.jquery.JQuery;
import org.tools.hqlbuilder.webservice.wicket.CssResourceReference;
import org.tools.hqlbuilder.webservice.wicket.JavaScriptResourceReference;

/**
 * @see http://dimsemenov.com/plugins/magnific-popup/documentation.html
 */
public class Magnify {
    public static JavaScriptResourceReference JS = new JavaScriptResourceReference(Magnify.class, "magnific-popup.js")
            .addJavaScriptResourceReferenceDependency(JQuery.getJQueryReference());

    public static CssResourceReference CSS = new CssResourceReference(Magnify.class, "magnific-popup.css");

    public static final String MAGNIFY_CLASS = "mfp-hide";
}
