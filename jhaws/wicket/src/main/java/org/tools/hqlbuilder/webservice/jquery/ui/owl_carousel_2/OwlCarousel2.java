package org.tools.hqlbuilder.webservice.jquery.ui.owl_carousel_2;

import org.tools.hqlbuilder.webservice.jquery.ui.jquery.JQuery;
import org.tools.hqlbuilder.webservice.wicket.CssResourceReference;
import org.tools.hqlbuilder.webservice.wicket.JavaScriptResourceReference;

/**
 * version 2.0.0-beta.2.4
 *
 * @author https://owlcarousel2.github.io/OwlCarousel2/
 */
public class OwlCarousel2 {
    public static final JavaScriptResourceReference JS = new JavaScriptResourceReference(OwlCarousel2.class, "owl.carousel.js");

    static {
        try {
            OwlCarousel2.JS.addJavaScriptResourceReferenceDependency(JQuery.getJQueryReference());
        } catch (Exception ex) {
            //
        }
    }

    public static final CssResourceReference CSS = new CssResourceReference(OwlCarousel2.class, "owl.carousel.css");

    public static final CssResourceReference CSS_THEME = new CssResourceReference(OwlCarousel2.class, "owl.theme.default.css")
            .addCssResourceReferenceDependency(OwlCarousel2.CSS);
}
