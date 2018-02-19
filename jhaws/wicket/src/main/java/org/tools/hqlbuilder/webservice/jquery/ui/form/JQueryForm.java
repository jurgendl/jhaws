package org.tools.hqlbuilder.webservice.jquery.ui.form;

import org.apache.wicket.request.Url;
import org.tools.hqlbuilder.webservice.wicket.JavaScriptResourceReference;
import org.tools.hqlbuilder.webservice.wicket.UrlResourceReference;
import org.tools.hqlbuilder.webservice.wicket.WicketApplication;

/**
 * used version 3.51.0-2014.06.20
 *
 * @see http://malsup.com/jquery/form/
 */
public class JQueryForm {
    public static final String URL = "http://cdnjs.cloudflare.com/ajax/libs/jquery.form/3.50/";

    public static final String PATH_JQUERY_FORM_JS = "jquery.form.js";

    public static JavaScriptResourceReference FORM_JS = new JavaScriptResourceReference(JQueryForm.class, PATH_JQUERY_FORM_JS);

    public static UrlResourceReference CDN_FORM_JS = new UrlResourceReference(Url.parse(URL + PATH_JQUERY_FORM_JS));

    static {
        try {
            FORM_JS.addJavaScriptResourceReferenceDependency(WicketApplication.get().getJavaScriptLibrarySettings().getJQueryReference());
            CDN_FORM_JS.addJavaScriptResourceReferenceDependency(WicketApplication.get().getJavaScriptLibrarySettings().getJQueryReference());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
