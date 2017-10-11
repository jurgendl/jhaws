package org.tools.hqlbuilder.webservice.bootstrap4.datetimepicker.tempusdominus;

import java.util.Locale;

import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.tools.hqlbuilder.webservice.bootstrap4.Bootstrap4;
import org.tools.hqlbuilder.webservice.jquery.ui.moment.MomentJs;
import org.tools.hqlbuilder.webservice.jquery.ui.weloveicons.fontawesome.FontAwesome;
import org.tools.hqlbuilder.webservice.wicket.CssResourceReference;
import org.tools.hqlbuilder.webservice.wicket.JavaScriptResourceReference;

// https://github.com/tempusdominus/bootstrap-4
// https://tempusdominus.github.io/bootstrap-4/
// 5.0.0
public class BootstrapTempusDominusDateTimePicker {
    public static final JavaScriptResourceReference JS = new JavaScriptResourceReference(BootstrapTempusDominusDateTimePicker.class,
            "js/tempusdominus-bootstrap-4.js");

    public static final CssResourceReference CSS = new CssResourceReference(BootstrapTempusDominusDateTimePicker.class,
            "css/tempusdominus-bootstrap-4.css");

    static {
        JS.addJavaScriptResourceReferenceDependency(Bootstrap4.JS);
        JS.addJavaScriptResourceReferenceDependency(MomentJs.JS);
        CSS.addCssResourceReferenceDependency(Bootstrap4.CSS);
        CSS.addCssResourceReferenceDependency(FontAwesome.CSS);
    }

    public static enum Type {
        DATETIME, DATE, TIME;
    }

    public static OnDomReadyHeaderItem factory(Type type, Locale locale, boolean debug) {
        String typeOpt = "";
        switch (type) {
            case DATE:
                typeOpt = ",format:'L'";
                break;
            case TIME:
                typeOpt = ",format:'LT'";
                break;
            case DATETIME:
            default:
                typeOpt = "";
                break;
        }
        String string = ";$('.tempusdominus').datetimepicker({" + //
                "locale:'" + locale.getLanguage() + "'" + //
                typeOpt + //
                ",sideBySide:true" + //
                (debug ? ",debug:true" : "") + //
                "});";
        return OnDomReadyHeaderItem.forScript(string);
    }
}
