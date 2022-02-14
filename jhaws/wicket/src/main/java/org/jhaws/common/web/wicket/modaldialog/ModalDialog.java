package org.jhaws.common.web.wicket.modaldialog;

import org.jhaws.common.web.wicket.Bootstrap4;
import org.jhaws.common.web.wicket.CssResourceReference;
import org.jhaws.common.web.wicket.JavaScriptResourceReference;

// https://github.com/nakupanda/bootstrap3-dialog
// https://nakupanda.github.io/bootstrap3-dialog/
// v1.35.4
//
// type:
// BootstrapDialog.TYPE_DEFAULT,
// BootstrapDialog.TYPE_INFO,
// BootstrapDialog.TYPE_PRIMARY,
// BootstrapDialog.TYPE_SUCCESS,
// BootstrapDialog.TYPE_WARNING,
// BootstrapDialog.TYPE_DANGER
// BootstrapDialog.show({type:...,title:'...',message:'...',buttons:[{{label:'Close',action:function(dialogItself){dialogItself.close();}}]});
public class ModalDialog {
    public static final JavaScriptResourceReference JS = new JavaScriptResourceReference(ModalDialog.class, "js/bootstrap-dialog.js");

    public static final CssResourceReference CSS = new CssResourceReference(ModalDialog.class, "css/bootstrap-dialog.css");

    static {
        JS.addJavaScriptResourceReferenceDependency(Bootstrap4.JS);
        // CSS.addCssResourceReferenceDependency(Bootstrap4.getCSS());
    }
}
