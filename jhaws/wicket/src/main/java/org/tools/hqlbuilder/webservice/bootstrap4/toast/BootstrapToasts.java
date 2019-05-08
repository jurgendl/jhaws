package org.tools.hqlbuilder.webservice.bootstrap4.toast;

import org.apache.commons.lang3.StringUtils;
import org.tools.hqlbuilder.webservice.bootstrap4.Bootstrap4;
import org.tools.hqlbuilder.webservice.wicket.CssResourceReference;
import org.tools.hqlbuilder.webservice.wicket.JavaScriptResourceReference;

// $.toast({});
// title: alert title
// subtitle: subtitle
// content: alert message
// type: 'info', 'success', 'warning', 'error'
// delay: auto dismiss after this timeout
// img: an object containging image information: {src class title alt}
//
public class BootstrapToasts {
	public static final JavaScriptResourceReference JS = new JavaScriptResourceReference(BootstrapToasts.class,
			"toast.js");

	public static final CssResourceReference CSS = new CssResourceReference(BootstrapToasts.class, "toast.css");

	static {
		JS.addJavaScriptResourceReferenceDependency(Bootstrap4.JS);
		CSS.addCssResourceReferenceDependency(Bootstrap4.CSS);
	}

	public static enum ToastType {
		info, success, warning, error;
	}

	public static String script(ToastType type, String title, String subtitle, String content, Long delay) {
		if (type == null)
			throw new NullPointerException();
		return ";$.toast({"//
				+ "type:'" + type + "'"//
				+ ",title:'" + title.toString() + "'"//
				+ ",content:'" + content.toString() + "'" //
				+ (StringUtils.isBlank(subtitle) ? "" : ",subtitle:'" + subtitle + "'")//
				+ (delay == null ? "" : ",delay:" + delay)//
				+ "});";
	}
}
