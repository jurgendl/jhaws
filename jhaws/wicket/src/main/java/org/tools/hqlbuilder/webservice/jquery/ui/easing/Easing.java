package org.tools.hqlbuilder.webservice.jquery.ui.easing;

import org.apache.wicket.request.Url;
import org.tools.hqlbuilder.webservice.wicket.JavaScriptResourceReference;
import org.tools.hqlbuilder.webservice.wicket.UrlResourceReference;
import org.tools.hqlbuilder.webservice.wicket.WicketApplication;

/**
 * @see http://gsgd.co.uk/sandbox/jquery/easing/
 */
public class Easing {
    public static JavaScriptResourceReference EASING_JS = new JavaScriptResourceReference(Easing.class, "jquery.easing.1.3.js")
            .addJavaScriptResourceReferenceDependency(WicketApplication.get().getJavaScriptLibrarySettings().getJQueryReference());

    public static UrlResourceReference CDN_EASING_JS = new UrlResourceReference(
            Url.parse("http://cdnjs.cloudflare.com/ajax/libs/jquery-easing/1.3/jquery.easing.min.js"));
}
