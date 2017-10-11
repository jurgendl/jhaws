package org.tools.hqlbuilder.webservice.jquery.ui.moment;

import java.util.Locale;

import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.tools.hqlbuilder.webservice.wicket.JavaScriptResourceReference;

// http://momentjs.com/
// https://github.com/moment/moment/tree/master
// 2.19.0
public class MomentJs {
    public static JavaScriptResourceReference JS = new JavaScriptResourceReference(MomentJs.class, "moment.js");

    public static JavaScriptResourceReference JS_LOCALE = new JavaScriptResourceReference(MomentJs.class, "moment-with-locales.js");

    public static JavaScriptResourceReference JS_I18N = new JavaScriptResourceReference(MomentJs.class, "locales.js")
            .addJavaScriptResourceReferenceDependency(JS);

    public static OnDomReadyHeaderItem factory(Locale locale) {
        return OnDomReadyHeaderItem.forScript(";moment.locale('" + locale.getLanguage() + "');");
    }

    // https://codebox.net/pages/moment-date-range-plugin
    // https://github.com/codebox/moment-precise-range/releases
    // 1.2.4
    public static JavaScriptResourceReference JS_PLUGIN_PRECISE_RANGE = new JavaScriptResourceReference(MomentJs.class,
            "plugins/preciserange/moment-precise-range.js");

    // public static JavaScriptResourceReference JS_PLUGIN_JAVA_DATE_FORMAT = new JavaScriptResourceReference(MomentJs.class,
    // "moment-jdateformatparser.js");
    //
    // public static JavaScriptResourceReference JS_PLUGIN_DURACTION_FORMAT = new JavaScriptResourceReference(MomentJs.class,
    // "moment-duration-format.js");
    //
    // public static JavaScriptResourceReference JS_PLUGIN_TAIWAN = new JavaScriptResourceReference(MomentJs.class, "moment-taiwan.js");
    //
    // public static JavaScriptResourceReference JS_PLUGIN_TRANSFORM = new JavaScriptResourceReference(MomentJs.class, "moment-transform.js");
    //
    // public static JavaScriptResourceReference JS_PLUGIN_ROUND = new JavaScriptResourceReference(MomentJs.class, "moment-round.js");
    //
    // public static JavaScriptResourceReference JS_PLUGIN_PARSE = new JavaScriptResourceReference(MomentJs.class, "parseformat.js");
    //
    // public static JavaScriptResourceReference JS_PLUGIN_FISCAL_QUARTERS = new JavaScriptResourceReference(MomentJs.class, "moment-fquarter.js");
    //
    // public static JavaScriptResourceReference JS_PLUGIN_TWITTER = new JavaScriptResourceReference(MomentJs.class, "moment-twitter.js");
    //
    // public static JavaScriptResourceReference JS_PLUGIN_RECUR = new JavaScriptResourceReference(MomentJs.class, "moment-recur.js");
    //
    // public static JavaScriptResourceReference JS_PLUGIN_HIJRI_CALENDAR = new JavaScriptResourceReference(MomentJs.class, "moment-hijri.js");
    //
    // public static JavaScriptResourceReference JS_PLUGIN_JAALALI_CALENDAR = new JavaScriptResourceReference(MomentJs.class, "moment-jalaali.js");
    //
    // public static JavaScriptResourceReference JS_PLUGIN_ISO_CALENDAR = new JavaScriptResourceReference(MomentJs.class, "moment.isocalendar.js");
    //
    // public static JavaScriptResourceReference JS_PLUGIN_TWIX = new JavaScriptResourceReference(MomentJs.class, "twix.js");
    //
    // public static JavaScriptResourceReference JS_PLUGIN_RANGE = new JavaScriptResourceReference(MomentJs.class, "moment-range.js");
    //
    // public static JavaScriptResourceReference JS_PLUGIN_MSDATE = new JavaScriptResourceReference(MomentJs.class, "moment-msdate.js");
    //
    // public static JavaScriptResourceReference JS_PLUGIN_STRFTIME = new JavaScriptResourceReference(MomentJs.class, "moment-strftime.js");
}
