package org.tools.hqlbuilder.webservice.jquery.ui.jquery;

import org.tools.hqlbuilder.webservice.wicket.JavaScriptResourceReference;
import org.tools.hqlbuilder.webservice.wicket.WicketApplication;

/**
 * version 1.11.1
 */
public class JQuery {
    public static org.apache.wicket.request.resource.JavaScriptResourceReference getJQueryReference() {
        return (org.apache.wicket.request.resource.JavaScriptResourceReference) WicketApplication.get().getJavaScriptLibrarySettings()
                .getJQueryReference();
    }

    /**
     * fix errors like "$.browser.msie"
     *
     * @see https://github.com/jquery/jquery-migrate/#readme
     */
    public static JavaScriptResourceReference JQUERY_MIGRATE_JS = new JavaScriptResourceReference(JQuery.class, "jquery-migrate-1.2.1.js");

    static {
        try {
            JQuery.JQUERY_MIGRATE_JS.addJavaScriptResourceReferenceDependency(JQuery.getJQueryReference());
        } catch (Exception ex) {
            //
        }
    }
}
