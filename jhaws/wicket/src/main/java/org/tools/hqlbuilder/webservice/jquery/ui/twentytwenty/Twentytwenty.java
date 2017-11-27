package org.tools.hqlbuilder.webservice.jquery.ui.twentytwenty;

import org.tools.hqlbuilder.webservice.jquery.ui.jquery.JQuery;
import org.tools.hqlbuilder.webservice.wicket.CssResourceReference;
import org.tools.hqlbuilder.webservice.wicket.JavaScriptResourceReference;

// $(".twentytwenty").twentytwenty({
// default_offset_pct: 0.7, // How much of the before image is visible when the page loads
// orientation: 'vertical', // Orientation of the before and after images ('horizontal' or 'vertical')
// before_label: 'January 2017', // Set a custom before label
// after_label: 'March 2017', // Set a custom after label
// no_overlay: true //Do not show the overlay with before and after
// move_slider_on_hover: true // Move slider on mouse hover?
// move_with_handle_only: true, // Allow a user to swipe anywhere on the image to control slider movement.
// click_to_move: false // Allow a user to click (or tap) anywhere on the image to move the slider to that location.
// });
//
// <div class="container p-0">
// <div class="compare" wicket:id="compare">
// <img wicket:id="first" src="" />
// <img wicket:id="last" src="" />
// </div>
// </div>
//
//.twentytwenty-overlay:hover { #disable gray overlay on hover
// background: rgba(0, 0, 0, 0) !important;
//}
//
// https://zurb.com/playground/twentytwenty
// https://github.com/zurb/twentytwenty/
public class Twentytwenty {
	public static JavaScriptResourceReference JS_EVENT = new JavaScriptResourceReference(Twentytwenty.class,
			"js/jquery.event.move.js");

	public static JavaScriptResourceReference JS_MOVE = new JavaScriptResourceReference(Twentytwenty.class,
			"js/jquery.twentytwenty.js");

	public static CssResourceReference CSS = new CssResourceReference(Twentytwenty.class, "css/twentytwenty.css");

	public static CssResourceReference CSS_NO_COMPASS = new CssResourceReference(Twentytwenty.class,
			"css/twentytwenty-no-compass.css");

	static {
		try {
			JS_EVENT.addJavaScriptResourceReferenceDependency(JQuery.getJQueryReference());
			JS_MOVE.addJavaScriptResourceReferenceDependency(JS_EVENT);
		} catch (Exception ex) {
			//
		}
	}
}
