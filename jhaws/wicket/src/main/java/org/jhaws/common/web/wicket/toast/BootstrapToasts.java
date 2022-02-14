package org.jhaws.common.web.wicket.toast;

import org.apache.commons.lang3.StringUtils;
import org.jhaws.common.web.wicket.Bootstrap4;
import org.jhaws.common.web.wicket.CssResourceReference;
import org.jhaws.common.web.wicket.JavaScriptResourceReference;

// $.toast({});
// title: alert title
// subtitle: subtitle
// content: alert message
// type: 'info', 'success', 'warning', 'error'
// delay: auto dismiss after this timeout
// img: an object containging image information: {src class title alt}
//
public class BootstrapToasts {
    public static final JavaScriptResourceReference JS = new JavaScriptResourceReference(BootstrapToasts.class, "toast.js");

    public static final CssResourceReference CSS = new CssResourceReference(BootstrapToasts.class, "toast.css");

    static {
        JS.addJavaScriptResourceReferenceDependency(Bootstrap4.JS);
        // CSS.addCssResourceReferenceDependency(Bootstrap4.getCSS());
    }

    public static enum ToastType {
        info, success, warning, error;
    }

    public static String script(ToastType type, String title, String subtitle, String content, Long delay) {
        if (type == null) throw new NullPointerException();
        return ";$.toast({"//
                + "type:'" + type + "'"//
                + ",title:'" + title.toString() + "'"//
                + ",content:'" + content.toString() + "'" //
                + (StringUtils.isBlank(subtitle) ? "" : ",subtitle:'" + subtitle + "'")//
                + (delay == null ? "" : ",delay:" + delay)//
                + "});";
    }
}
