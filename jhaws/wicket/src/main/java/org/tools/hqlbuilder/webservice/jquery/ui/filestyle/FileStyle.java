package org.tools.hqlbuilder.webservice.jquery.ui.filestyle;

import org.tools.hqlbuilder.webservice.wicket.JavaScriptResourceReference;
import org.tools.hqlbuilder.webservice.wicket.WicketApplication;

/**
 * version 1.1.0<br>
 * requires twitter bootstrap
 *
 * @see http://dev.tudosobreweb.com.br/bootstrap-filestyle/
 */
public class FileStyle {
    public static JavaScriptResourceReference FILESTYLE_JS = new JavaScriptResourceReference(FileStyle.class, "bootstrap-filestyle.js")
            .addJavaScriptResourceReferenceDependency(WicketApplication.get().getJavaScriptLibrarySettings().getJQueryReference());
}
