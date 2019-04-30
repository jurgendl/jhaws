package org.tools.hqlbuilder.webservice.jquery.ui.spin;

import org.tools.hqlbuilder.webservice.wicket.CssResourceReference;

// http://www.raphaelfabeni.com.br/css-loader/
// https://github.com/raphaelfabeni/css-loader/blob/master/dist/loader-bouncing.css
public class Spin {
	public static final String HTML = "<span id=\"spinnercontainer\" class=\"visible\"><div class=\"loader loader-bouncing is-active\"></div></span>";

	public static final String HIDE = ";$('#spinnercontainer').removeClass('visible').addClass('invisible');";

	public static final String SHOW = ";$('#spinnercontainer').removeClass('invisible').addClass('visible');";

	public static CssResourceReference SPIN_CSS = new CssResourceReference(Spin.class, "spin.css");

	public static String show() {
		return SHOW;
	}

	public static String hide() {
		return HIDE;
	}

	public static String html() {
		return HTML;
	}
}
