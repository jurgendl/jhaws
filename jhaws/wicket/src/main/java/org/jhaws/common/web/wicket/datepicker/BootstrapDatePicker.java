package org.jhaws.common.web.wicket.datepicker;

import org.jhaws.common.web.wicket.Bootstrap4;
import org.jhaws.common.web.wicket.CssResourceReference;
import org.jhaws.common.web.wicket.JavaScriptResourceReference;

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

	public static final JavaScriptResourceReference JS = new JavaScriptResourceReference(BootstrapDatePicker.class,
			"js/bootstrap-datepicker.js");

	public static final CssResourceReference CSS = new CssResourceReference(BootstrapDatePicker.class,
			"css/bootstrap-datepicker3.css");

	public static JavaScriptResourceReference JS_I18N = new JavaScriptResourceReference(BootstrapDatePicker.class,
			RESOURCE_I18N_PATH + "bootstrap-datepicker.js").addJavaScriptResourceReferenceDependency(JS);

	static {
		JS.addJavaScriptResourceReferenceDependency(Bootstrap4.JS);
		// CSS.addCssResourceReferenceDependency(Bootstrap4.getCSS());
	}
}
