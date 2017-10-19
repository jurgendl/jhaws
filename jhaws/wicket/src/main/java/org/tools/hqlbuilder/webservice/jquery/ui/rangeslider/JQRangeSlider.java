package org.tools.hqlbuilder.webservice.jquery.ui.rangeslider;

import org.tools.hqlbuilder.webservice.jquery.ui.jqueryui.JQueryUI;
import org.tools.hqlbuilder.webservice.wicket.CssResourceReference;
import org.tools.hqlbuilder.webservice.wicket.JavaScriptResourceReference;

// 5.7.2
/**
 * @see http://ghusse.github.io/jQRangeSlider/documentation.html#dateSliderQuickStart
 */
public class JQRangeSlider {
	public static JavaScriptResourceReference JS = new JavaScriptResourceReference(JQRangeSlider.class,
			"jQAllRangeSliders-withRuler-min.js");

	static {
		try {
			JS.addJavaScriptResourceReferenceDependency(JQueryUI.getJQueryUIReference());
		} catch (Exception ex) {
			//
		}
	}

	public static CssResourceReference CSS = new CssResourceReference(JQRangeSlider.class, "css/iThing.css");
}
