package org.tools.hqlbuilder.webservice.jquery.ui.velocity;

import org.tools.hqlbuilder.webservice.jquery.ui.jqueryui.JQueryUI;
import org.tools.hqlbuilder.webservice.wicket.JavaScriptResourceReference;

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
            VELOCITY_JS.addJavaScriptResourceReferenceDependency(JQueryUI.getJQueryUIReference());
        } catch (Exception ex) {
            //
        }
    }
}
