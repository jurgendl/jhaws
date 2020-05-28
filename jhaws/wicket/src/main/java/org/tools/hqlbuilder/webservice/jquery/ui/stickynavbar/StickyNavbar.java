package org.tools.hqlbuilder.webservice.jquery.ui.stickynavbar;

import org.jhaws.common.web.wicket.JavaScriptResourceReference;
import org.jhaws.common.web.wicket.WicketApplication;
import org.tools.hqlbuilder.webservice.css.WicketCSSRoot;
import org.tools.hqlbuilder.webservice.jquery.ui.easing.Easing;

/**
 * @see https://github.com/jbutko/stickyNavbar.js
 */
public class StickyNavbar {
    public static JavaScriptResourceReference STICKY_NAVBAR_JS = new JavaScriptResourceReference(StickyNavbar.class, "jquery.stickyNavbar.js");

    static {
        try {
            STICKY_NAVBAR_JS.addCssResourceReferenceDependency(WicketCSSRoot.ANIMATE);
            STICKY_NAVBAR_JS.addJavaScriptResourceReferenceDependency(Easing.EASING_JS);
            STICKY_NAVBAR_JS.addJavaScriptResourceReferenceDependency(WicketApplication.get().getJavaScriptLibrarySettings().getJQueryReference());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
