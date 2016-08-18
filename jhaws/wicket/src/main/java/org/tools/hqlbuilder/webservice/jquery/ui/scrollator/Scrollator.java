package org.tools.hqlbuilder.webservice.jquery.ui.scrollator;

import org.tools.hqlbuilder.webservice.wicket.CssResourceReference;
import org.tools.hqlbuilder.webservice.wicket.JavaScriptResourceReference;
import org.tools.hqlbuilder.webservice.wicket.WicketApplication;

/**
 * @see https://github.com/FaroeMedia/scrollator
 */
public class Scrollator {
    public static JavaScriptResourceReference SCROLLATOR_JS = new JavaScriptResourceReference(Scrollator.class, "fm.scrollator.jquery.js")
    .addJavaScriptResourceReferenceDependency(WicketApplication.get().getJavaScriptLibrarySettings().getJQueryReference());

    public static CssResourceReference SCROLLATOR_CSS = new CssResourceReference(Scrollator.class, "fm.scrollator.jquery.css");

    public static final String SCROLLATOR_CLASS = "scrollator";

    public static final String SCROLLATOR_FACTORY_JS = "$(document.body).scrollator();$('." + SCROLLATOR_CLASS
            + "').scrollator();$('textarea').scrollator();";
}
