package org.jhaws.common.web.wicket.slider;

import org.jhaws.common.web.wicket.Bootstrap4;
import org.jhaws.common.web.wicket.CssResourceReference;
import org.jhaws.common.web.wicket.JavaScriptResourceReference;

// http://seiyria.com/bootstrap-slider/
// https://github.com/seiyria/bootstrap-slider
// 10.6.1
public class BootstrapSlider {
    public static final JavaScriptResourceReference JS = new JavaScriptResourceReference(BootstrapSlider.class, "js/bootstrap-slider.js");

    public static final CssResourceReference CSS = new CssResourceReference(BootstrapSlider.class, "css/bootstrap-slider.css");

    static {
        JS.addJavaScriptResourceReferenceDependency(Bootstrap4.JS);
        // CSS.addCssResourceReferenceDependency(Bootstrap4.getCSS());
    }
}
