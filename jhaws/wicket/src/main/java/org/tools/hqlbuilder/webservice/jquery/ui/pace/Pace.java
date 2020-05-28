package org.tools.hqlbuilder.webservice.jquery.ui.pace;

import org.jhaws.common.web.wicket.CssResourceReference;
import org.jhaws.common.web.wicket.JavaScriptResourceReference;

/**
 * @see http://github.hubspot.com/pace/docs/welcome/
 * @see https://github.com/HubSpot/pace/
 */
public class Pace {
    public static JavaScriptResourceReference JS = new JavaScriptResourceReference(Pace.class, "pace.js");

    public static CssResourceReference SS = new CssResourceReference(Pace.class, "themes/blue/pace-theme-flash.css");
}
