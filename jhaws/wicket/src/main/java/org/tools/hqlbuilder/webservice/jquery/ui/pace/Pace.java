package org.tools.hqlbuilder.webservice.jquery.ui.pace;

import org.tools.hqlbuilder.webservice.wicket.CssResourceReference;
import org.tools.hqlbuilder.webservice.wicket.JavaScriptResourceReference;

/**
 * @see http://github.hubspot.com/pace/docs/welcome/
 * @see https://github.com/HubSpot/pace/
 */
public class Pace {
    public static JavaScriptResourceReference MAGNIFY_JS = new JavaScriptResourceReference(Pace.class, "pace.js");

    public static CssResourceReference MAGNIFY_CSS = new CssResourceReference(Pace.class, "themes/blue/pace-theme-flash.css");
}
