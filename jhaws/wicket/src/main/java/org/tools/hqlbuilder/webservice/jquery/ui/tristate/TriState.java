package org.tools.hqlbuilder.webservice.jquery.ui.tristate;

import org.tools.hqlbuilder.webservice.jquery.ui.jqueryui.JQueryUI;
import org.tools.hqlbuilder.webservice.wicket.JavaScriptResourceReference;

/**
 * @see https://github.com/vanderlee/tristate
 * @see http://vanderlee.github.io/tristate/
 */
public class TriState {
    public static JavaScriptResourceReference TRISTATE_JS = new JavaScriptResourceReference(TriState.class, "jquery.tristate.js");

    static {
        try {
            TRISTATE_JS.addJavaScriptResourceReferenceDependency(JQueryUI.getJQueryUIReference());
        } catch (Exception ex) {
            //
        }
    }

    public static JavaScriptResourceReference TRISTATE_FACTORY_JS = new JavaScriptResourceReference(TriState.class, "tristate_factory.js")
            .addJavaScriptResourceReferenceDependency(TRISTATE_JS);
}
