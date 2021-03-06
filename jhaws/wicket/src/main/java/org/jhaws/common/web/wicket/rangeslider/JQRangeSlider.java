package org.jhaws.common.web.wicket.rangeslider;

import org.jhaws.common.web.wicket.CssResourceReference;
import org.jhaws.common.web.wicket.JavaScriptResourceReference;
import org.jhaws.common.web.wicket.jquery.JQuery;

// 5.7.2
// needs jquery-ui or jquery-ui-1.10.4.custom.js
// see; response.render(JavaScriptHeaderItem.forReference(JQRangeSlider.JS));
/**
 * @see http://ghusse.github.io/jQRangeSlider/documentation.html#dateSliderQuickStart
 */
public class JQRangeSlider {
    public static JavaScriptResourceReference JS = new JavaScriptResourceReference(JQRangeSlider.class, "jQAllRangeSliders-withRuler-min.js");

    public static JavaScriptResourceReference JS_JQUI = new JavaScriptResourceReference(JQRangeSlider.class, "jquery-ui-1.10.4.custom.js");

    static {
        try {
            JS.addJavaScriptResourceReferenceDependency(JQuery.getJQueryReference());
        } catch (Exception ex) {
            //
        }
    }

    public static CssResourceReference CSS = new CssResourceReference(JQRangeSlider.class, "css/iThing.css");
}
