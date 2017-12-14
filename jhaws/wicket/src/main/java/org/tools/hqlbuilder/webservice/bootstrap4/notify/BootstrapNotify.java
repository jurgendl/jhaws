package org.tools.hqlbuilder.webservice.bootstrap4.notify;

import org.tools.hqlbuilder.webservice.bootstrap4.Bootstrap4;
import org.tools.hqlbuilder.webservice.wicket.JavaScriptResourceReference;

// https://github.com/mouse0270/bootstrap-notify/releases
// http://bootstrap-notify.remabledesigns.com/
// 3.1.3
public class BootstrapNotify {
    public static final JavaScriptResourceReference JS = new JavaScriptResourceReference(BootstrapNotify.class, "bootstrap-notify.js")
            .addJavaScriptResourceReferenceDependency(Bootstrap4.JS)
            .addCssResourceReferenceDependency(org.tools.hqlbuilder.webservice.css.WicketCSSRoot.ANIMATE);
}
