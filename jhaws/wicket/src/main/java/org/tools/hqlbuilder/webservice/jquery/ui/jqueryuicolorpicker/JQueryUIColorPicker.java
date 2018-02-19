package org.tools.hqlbuilder.webservice.jquery.ui.jqueryuicolorpicker;

import org.tools.hqlbuilder.webservice.jquery.ui.jqueryui.JQueryUI;
import org.tools.hqlbuilder.webservice.wicket.CssResourceReference;
import org.tools.hqlbuilder.webservice.wicket.JavaScriptResourceReference;

/**
 * version 1.0.9
 *
 * @see http://vanderlee.github.io/colorpicker/
 */
public class JQueryUIColorPicker {
    public static final String RESOURCE_I18N_PATH = "i18n/";

    public static JavaScriptResourceReference JQUERY_COLORPICKER_JS = new JavaScriptResourceReference(JQueryUIColorPicker.class,
            "jquery.colorpicker.js");

    static {
        try {
            JQUERY_COLORPICKER_JS.addJavaScriptResourceReferenceDependency(JQueryUI.getJQueryUIReference());
        } catch (Exception ex) {
            //
        }
    }

    public static CssResourceReference JQUERY_COLORPICKER_CSS = new CssResourceReference(JQueryUIColorPicker.class, "jquery.colorpicker.css");

    public static JavaScriptResourceReference JQUERY_COLORPICKER_I18N_JS = new JavaScriptResourceReference(JQueryUIColorPicker.class,
            RESOURCE_I18N_PATH + "jquery.ui.colorpicker.js").addJavaScriptResourceReferenceDependency(JQUERY_COLORPICKER_JS);

    public static JavaScriptResourceReference JQUERY_COLORPICKER_FACTORY_JS = new JavaScriptResourceReference(JQueryUIColorPicker.class,
            "jquery.colorpicker.factory.js").addJavaScriptResourceReferenceDependency(JQUERY_COLORPICKER_JS);

    public static final String jquicolorpicker = "jquicolorpicker";
}
