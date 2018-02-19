package org.tools.hqlbuilder.webservice.jquery.ui.owl_carousel;

import org.tools.hqlbuilder.webservice.jquery.ui.jqueryui.JQueryUI;
import org.tools.hqlbuilder.webservice.wicket.CssResourceReference;
import org.tools.hqlbuilder.webservice.wicket.JavaScriptResourceReference;

/**
 * @author http://owlgraphic.com/owlcarousel/#demo
 */
public class OwlCarousel {
    public static final JavaScriptResourceReference JS = new JavaScriptResourceReference(OwlCarousel.class, "owl.carousel.js");

    static {
        try {
            OwlCarousel.JS.addJavaScriptResourceReferenceDependency(JQueryUI.getJQueryUIReference());
        } catch (Exception ex) {
            //
        }
    }

    public static final CssResourceReference CSS1 = new CssResourceReference(OwlCarousel.class, "owl.carousel.css");

    public static final CssResourceReference CSS2 = new CssResourceReference(OwlCarousel.class, "owl.theme.css");

    public static final CssResourceReference CSS3 = new CssResourceReference(OwlCarousel.class, "owl.transitions.css");
}
