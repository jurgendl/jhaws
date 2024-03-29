package org.jhaws.common.web.wicket.owl_carousel_2;

import org.jhaws.common.web.wicket.CssResourceReference;
import org.jhaws.common.web.wicket.JavaScriptResourceReference;
import org.jhaws.common.web.wicket.jquery.JQuery;

/**
 * version 2.3.4
 *
 * @author https://owlcarousel2.github.io/OwlCarousel2/demos/responsive.html
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

    public static final CssResourceReference CSS = new CssResourceReference(OwlCarousel2.class, "assets/owl.carousel.css");

    public static final CssResourceReference CSS_THEME = new CssResourceReference(OwlCarousel2.class, "assets/owl.theme.default.css").addCssResourceReferenceDependency(OwlCarousel2.CSS);
}
