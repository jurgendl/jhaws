package org.jhaws.common.web.wicket.velocity;

import org.jhaws.common.web.wicket.JavaScriptResourceReference;
import org.jhaws.common.web.wicket.jquery.JQuery;

/**
 * Velocity animation framework version 0.11.9
 *
 * @see http://css-tricks.com/improving-ui-animation-workflow-velocity-js/
 * @see http://julian.com/research/velocity/
 */
public class Velocity {
    public static JavaScriptResourceReference VELOCITY_UI_JS = new JavaScriptResourceReference(Velocity.class, "velocity.ui.js");

    public static JavaScriptResourceReference VELOCITY_JS = new JavaScriptResourceReference(Velocity.class, "velocity.js");

    static {
        try {
            VELOCITY_JS.addJavaScriptResourceReferenceDependency(JQuery.getJQueryReference());
        } catch (Exception ex) {
            //
        }
    }
}
