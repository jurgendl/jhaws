package org.tools.hqlbuilder.webservice.jquery.ui.spin;

import org.tools.hqlbuilder.webservice.wicket.CssResourceReference;
import org.tools.hqlbuilder.webservice.wicket.JavaScriptResourceReference;
import org.tools.hqlbuilder.webservice.wicket.WicketApplication;

/**
 * add <span id="spinnercontainer"><span id="spinner" style="width:160px;height:160px;"/></span> to your page and give it a place and width/height, use startSpinner() to start and
 * stopSpinner() to stop
 *
 * @see http ://fgnass.github.io/spin.js/#?lines=13&length=14&width=5&radius=19&corners=1.0&rotate=0&trail=60&speed=1.0&direction=1&shadow=on&hwaccel=on
 */
public class Spin {
	public static CssResourceReference SPIN_CSS = new CssResourceReference(Spin.class, "spin.css");

	public static JavaScriptResourceReference SPIN_JS = new JavaScriptResourceReference(Spin.class, "spin.js");

	public static JavaScriptResourceReference SPIN_JQUERY_JS = new JavaScriptResourceReference(Spin.class, "jquery.spin.js").addJavaScriptResourceReferenceDependency(Spin.SPIN_JS)
			.addJavaScriptResourceReferenceDependency(WicketApplication.get().getJavaScriptLibrarySettings().getJQueryReference());

	public static JavaScriptResourceReference SPIN_FACTORY_JS = new JavaScriptResourceReference(Spin.class, "spin.factory.js")
			.addJavaScriptResourceReferenceDependency(Spin.SPIN_JQUERY_JS);
}
