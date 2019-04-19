package org.tools.hqlbuilder.webservice.bootstrap4.colorpicker;

import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.tools.hqlbuilder.webservice.bootstrap4.Bootstrap4;
import org.tools.hqlbuilder.webservice.wicket.CssResourceReference;
import org.tools.hqlbuilder.webservice.wicket.JavaScriptResourceReference;

// response.render(CssHeaderItem.forReference(BootstrapColorPicker.CSS));
// response.render(JavaScriptHeaderItem.forReference(BootstrapColorPicker.JS));
// response.render(BootstrapColorPicker.FACTORY);
//
// https://farbelous.github.io/bootstrap-colorpicker/
// 3.1.1
public class BootstrapColorPicker {
    public static final JavaScriptResourceReference JS = new JavaScriptResourceReference(BootstrapColorPicker.class, "js/bootstrap-colorpicker.js");

    public static final CssResourceReference CSS = new CssResourceReference(BootstrapColorPicker.class, "css/bootstrap-colorpicker.css");

    public static final OnDomReadyHeaderItem FACTORY = OnDomReadyHeaderItem.forScript(";$('.colorpicker-component').colorpicker();");

    static {
        JS.addJavaScriptResourceReferenceDependency(Bootstrap4.JS);
        CSS.addCssResourceReferenceDependency(Bootstrap4.CSS);
    }
}
