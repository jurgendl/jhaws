package org.tools.hqlbuilder.webservice.jquery.ui.spin;

import org.tools.hqlbuilder.webservice.jquery.ui.jquery.JQuery;
import org.tools.hqlbuilder.webservice.wicket.CssResourceReference;
import org.tools.hqlbuilder.webservice.wicket.JavaScriptResourceReference;

// 3.1.0
// https://github.com/fgnass/spin.js/releases
// http://spin.js.org
/**
 * add <span id="spinnercontainer"><span id="spinner" style="width:160px;height:160px;"/></span> to your page and give it a place and width/height,
 * use startSpinner() to start and stopSpinner() to stop
 *
 * @see http://spin.js.org/#?lines=20&length=0&width=4&radius=20&scale=1.3&corners=1&opacity=0&rotate=0&direction=1&speed=0.8&trail=32&color=%23000000&fadeColor=transparent&top=50&left=50&shadow=false
 */
public class Spin {
    public static CssResourceReference SPIN_CSS = new CssResourceReference(Spin.class, "spin.css");

    public static JavaScriptResourceReference SPIN_JS = new JavaScriptResourceReference(Spin.class, "spin.js");

    public static JavaScriptResourceReference SPIN_FACTORY_JS = new JavaScriptResourceReference(Spin.class, "spin.factory.js")
            .addJavaScriptResourceReferenceDependency(JQuery.getJQueryReference()).addJavaScriptResourceReferenceDependency(Spin.SPIN_JS);
}
