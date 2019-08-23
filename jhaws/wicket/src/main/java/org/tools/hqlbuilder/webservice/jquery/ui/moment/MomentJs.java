package org.tools.hqlbuilder.webservice.jquery.ui.moment;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.jhaws.common.io.FilePath;
import org.tools.hqlbuilder.webservice.wicket.JavaScriptResourceReference;

// http://momentjs.com/
// https://github.com/moment/moment/tree/master
// 2.19.0
public class MomentJs {
    public static String PROP_CURRENT_LANGUAGE = "currentLanguage";

    public static JavaScriptResourceReference JS = new JavaScriptResourceReference(MomentJs.class, "moment.js");

    public static JavaScriptResourceReference JS_LOCALE = new JavaScriptResourceReference(MomentJs.class, "moment-with-locales.js");

    public static JavaScriptResourceReference JS_I18N = new JavaScriptResourceReference(MomentJs.class, "locales.js")
            .addJavaScriptResourceReferenceDependency(JS);

    public static String FACTORY = ";moment.locale(" + PROP_CURRENT_LANGUAGE + ");";

    // https://codebox.net/pages/moment-date-range-plugin
    // https://github.com/codebox/moment-precise-range/releases
    // 1.2.4
    public static JavaScriptResourceReference JS_PLUGIN_PRECISE_RANGE = new JavaScriptResourceReference(MomentJs.class,
            "plugins/preciserange/moment-precise-range.js").addJavaScriptResourceReferenceDependency(JS);

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

    private static final Map<String, Map<String, String>> FORMATS = new HashMap<>();

    public static String dateFormat(Locale locale, String format) {
        Map<String, String> localemap = dateFormats(locale);
        return localemap.get(format);
    }

    /**
     * <pre>
     * Map<String, String> formats = MomentJs.dateFormats(getLocale());
     *
     * DateFormat dateFormat = new SimpleDateFormat(formats.get("L"));
     *
     * DateFormat timeFormat = new SimpleDateFormat(formats.get("LT"));
     *
     * DateFormat dateTimeFormat = new SimpleDateFormat(formats.get("L") + " " + formats.get("LT"));
     * </pre>
     */
    public static Map<String, String> dateFormats(Locale locale) {
        Map<String, String> localemap = FORMATS.get(locale.getLanguage());
        if (localemap == null) {
            localemap = new HashMap<>();
            FORMATS.put(locale.getLanguage(), localemap);
            String string = new FilePath(MomentJs.class, "/locale/nl.js").readAll();
            int p = string.indexOf("longDateFormat");
            String script = string.substring(p, 1 + string.indexOf("}", p));
            System.out.println(script);
            Map<String, String> _localemap = localemap;
            Arrays.stream(new String[] { "LT", "LTS", "L", "LL", "LLL", "LLLL" }).forEach(f -> {
                int p1 = script.indexOf(f + " ");
                p1 = script.indexOf("'", p1) + 1;
                int p2 = script.indexOf("'", p1);
                String momentformat = script.substring(p1, p2);
                _localemap.put(f, /* new SimpleDateFormat( */momentformat/* ) */);
            });
        }
        return localemap;
    }
}
