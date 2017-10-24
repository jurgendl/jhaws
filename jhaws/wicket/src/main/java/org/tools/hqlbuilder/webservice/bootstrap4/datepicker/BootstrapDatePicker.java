package org.tools.hqlbuilder.webservice.bootstrap4.datepicker;

import org.tools.hqlbuilder.webservice.bootstrap4.Bootstrap4;
import org.tools.hqlbuilder.webservice.wicket.CssResourceReference;
import org.tools.hqlbuilder.webservice.wicket.JavaScriptResourceReference;

// <div class="form-group form-row">
// <label class="col-form-label col-sm-2" for="cd">Custom Date</label>
// <div class="col-sm-10" style="padding-left: 0px !important; padding-right: 0px !important">
// <div class="input-group input-group-sm bootstrap-datetimepicker">
// <input id="cd" class="form-control form-control-sm" type="text" /> <span title="Select Date" class="input-group-addon"><span
// class="fontawesome-calendar"></span></span>
// </div>
// </div>
// </div>
//
// response.render(CssHeaderItem.forReference(BootstrapDatePicker.CSS));
// response.render(JavaScriptHeaderItem.forReference(BootstrapDatePicker.JS));
// response.render(JavaScriptHeaderItem.forReference(BootstrapDatePicker.JS_I18N));
//
//
// $('[data-provide=datepicker]').datepicker({
// language : "nl",
// calendarWeeks : true,
// autoclose : true,
// todayHighlight : true
// });
//
// https://bootstrap-datepicker.readthedocs.io/en/latest/
// https://uxsolutions.github.io/bootstrap-datepicker/?markup=input&format=&weekStart=&startDate=&endDate=&startView=0&minViewMode=0&maxViewMode=4&todayBtn=false&clearBtn=false&language=en&orientation=auto&multidate=&multidateSeparator=&keyboardNavigation=on&forceParse=on#
// 1.6.4
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
