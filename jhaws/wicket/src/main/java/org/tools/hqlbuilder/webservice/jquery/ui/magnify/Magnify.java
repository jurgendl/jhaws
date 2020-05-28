package org.tools.hqlbuilder.webservice.jquery.ui.magnify;

import org.jhaws.common.web.wicket.CssResourceReference;
import org.jhaws.common.web.wicket.JavaScriptResourceReference;
import org.tools.hqlbuilder.webservice.jquery.ui.jquery.JQuery;

/**
 * @see http://dimsemenov.com/plugins/magnific-popup/documentation.html
 */
public class Magnify {
    public static JavaScriptResourceReference JS = new JavaScriptResourceReference(Magnify.class, "magnific-popup.js")
            .addJavaScriptResourceReferenceDependency(JQuery.getJQueryReference());

    public static CssResourceReference CSS = new CssResourceReference(Magnify.class, "magnific-popup.css");

    public static final String MAGNIFY_CLASS = "mfp-hide";
}
