package org.tools.hqlbuilder.webservice.jquery.ui.spectrum;

import org.tools.hqlbuilder.webservice.wicket.CssResourceReference;
import org.tools.hqlbuilder.webservice.wicket.JavaScriptResourceReference;
import org.tools.hqlbuilder.webservice.wicket.WicketApplication;

/**
 * @see http://bgrins.github.io/spectrum/#why
 */
public class Spectrum {
    public static final String RESOURCE_I18N_PATH = "i18n/";

    public static JavaScriptResourceReference SPECTRUM_JS = new JavaScriptResourceReference(Spectrum.class, "spectrum.js");

    public static CssResourceReference SPECTRUM_CSS = new CssResourceReference(Spectrum.class, "spectrum.css");

    public static JavaScriptResourceReference SPECTRUM_I18N_JS = new JavaScriptResourceReference(Spectrum.class, RESOURCE_I18N_PATH
            + "jquery.spectrum.js").addJavaScriptResourceReferenceDependency(SPECTRUM_JS);

    static {
        try {
            SPECTRUM_JS.addJavaScriptResourceReferenceDependency(WicketApplication.get().getJavaScriptLibrarySettings().getJQueryReference());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
