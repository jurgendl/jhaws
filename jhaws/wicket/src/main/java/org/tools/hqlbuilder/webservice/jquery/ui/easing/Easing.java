package org.tools.hqlbuilder.webservice.jquery.ui.easing;

import org.apache.wicket.request.Url;
import org.jhaws.common.web.wicket.JavaScriptResourceReference;
import org.jhaws.common.web.wicket.UrlResourceReference;
import org.jhaws.common.web.wicket.WicketApplication;

/**
 * @see http://gsgd.co.uk/sandbox/jquery/easing/
 */
public class Easing {
    public static JavaScriptResourceReference EASING_JS = new JavaScriptResourceReference(Easing.class, "jquery.easing.1.3.js")
            .addJavaScriptResourceReferenceDependency(WicketApplication.get().getJavaScriptLibrarySettings().getJQueryReference());

    public static UrlResourceReference CDN_EASING_JS = new UrlResourceReference(
            Url.parse("http://cdnjs.cloudflare.com/ajax/libs/jquery-easing/1.3/jquery.easing.min.js"));
}
