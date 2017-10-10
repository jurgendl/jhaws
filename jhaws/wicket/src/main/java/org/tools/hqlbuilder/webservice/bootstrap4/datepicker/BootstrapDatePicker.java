package org.tools.hqlbuilder.webservice.bootstrap4.datepicker;

import org.tools.hqlbuilder.webservice.bootstrap4.Bootstrap4;
import org.tools.hqlbuilder.webservice.wicket.CssResourceReference;
import org.tools.hqlbuilder.webservice.wicket.JavaScriptResourceReference;

// https://eonasdan.github.io/bootstrap-datetimepicker/
// https://bootstrap-datepicker.readthedocs.io/en/latest/
// https://uxsolutions.github.io/bootstrap-datepicker/?markup=input&format=&weekStart=&startDate=&endDate=&startView=0&minViewMode=0&maxViewMode=4&todayBtn=false&clearBtn=false&language=en&orientation=auto&multidate=&multidateSeparator=&keyboardNavigation=on&forceParse=on#
public class BootstrapDatePicker {
    public static final String RESOURCE_I18N_PATH = "locales/";

    public static final JavaScriptResourceReference JS = new JavaScriptResourceReference(BootstrapDatePicker.class, "js/bootstrap-datepicker.js");

    public static final CssResourceReference CSS = new CssResourceReference(BootstrapDatePicker.class, "css/bootstrap-datepicker3.css");

    public static JavaScriptResourceReference JS_I18N = new JavaScriptResourceReference(BootstrapDatePicker.class,
            RESOURCE_I18N_PATH + "bootstrap-datepicker.js").addJavaScriptResourceReferenceDependency(JS);

    static {
        JS.addJavaScriptResourceReferenceDependency(Bootstrap4.JS);
        CSS.addCssResourceReferenceDependency(Bootstrap4.CSS);
    }
}
