package org.jhaws.common.web.wicket.filestyle;

import org.jhaws.common.web.wicket.JavaScriptResourceReference;
import org.jhaws.common.web.wicket.WicketApplication;

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
