package org.tools.hqlbuilder.webservice.bootstrap4.slider;

import org.tools.hqlbuilder.webservice.bootstrap4.Bootstrap4;
import org.tools.hqlbuilder.webservice.wicket.CssResourceReference;
import org.tools.hqlbuilder.webservice.wicket.JavaScriptResourceReference;

// http://seiyria.com/bootstrap-slider/
// https://github.com/seiyria/bootstrap-slider
// 10.6.1
public class BootstrapSlider {
    public static final JavaScriptResourceReference JS = new JavaScriptResourceReference(BootstrapSlider.class, "js/bootstrap-slider.js");

    public static final CssResourceReference CSS = new CssResourceReference(BootstrapSlider.class, "css/bootstrap-slider.css");

    static {
        JS.addJavaScriptResourceReferenceDependency(Bootstrap4.JS);
        CSS.addCssResourceReferenceDependency(Bootstrap4.CSS);
    }
}
