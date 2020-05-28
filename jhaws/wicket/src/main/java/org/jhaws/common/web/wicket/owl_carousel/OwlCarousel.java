package org.jhaws.common.web.wicket.owl_carousel;

import org.jhaws.common.web.wicket.CssResourceReference;
import org.jhaws.common.web.wicket.JavaScriptResourceReference;

/**
 * @author http://owlgraphic.com/owlcarousel/#demo
 */
public class OwlCarousel {
    public static final JavaScriptResourceReference JS = new JavaScriptResourceReference(OwlCarousel.class, "owl.carousel.js");

    public static final CssResourceReference CSS1 = new CssResourceReference(OwlCarousel.class, "owl.carousel.css");

    public static final CssResourceReference CSS2 = new CssResourceReference(OwlCarousel.class, "owl.theme.css");

    public static final CssResourceReference CSS3 = new CssResourceReference(OwlCarousel.class, "owl.transitions.css");
}
