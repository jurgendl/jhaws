package org.jhaws.common.web.wicket.scrollator;

import org.jhaws.common.web.wicket.CssResourceReference;
import org.jhaws.common.web.wicket.JavaScriptResourceReference;
import org.jhaws.common.web.wicket.WicketApplication;

/**
 * @see https://github.com/FaroeMedia/scrollator
 */
public class Scrollator {
    public static JavaScriptResourceReference SCROLLATOR_JS = new JavaScriptResourceReference(Scrollator.class, "fm.scrollator.jquery.js").addJavaScriptResourceReferenceDependency(WicketApplication.get().getJavaScriptLibrarySettings().getJQueryReference());

    public static CssResourceReference SCROLLATOR_CSS = new CssResourceReference(Scrollator.class, "fm.scrollator.jquery.css");

    public static final String SCROLLATOR_CLASS = "scrollator";

    public static final String SCROLLATOR_FACTORY_JS = "$(document.body).scrollator();$('." + SCROLLATOR_CLASS + "').scrollator();$('textarea').scrollator();";
}
