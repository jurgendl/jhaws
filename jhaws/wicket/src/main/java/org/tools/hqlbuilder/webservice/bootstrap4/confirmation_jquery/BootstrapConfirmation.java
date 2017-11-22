package org.tools.hqlbuilder.webservice.bootstrap4.confirmation_jquery;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.attributes.AjaxCallListener;
import org.apache.wicket.ajax.attributes.AjaxRequestAttributes;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.markup.html.form.Form;
import org.tools.hqlbuilder.webservice.bootstrap4.Bootstrap4;
import org.tools.hqlbuilder.webservice.wicket.JavaScriptResourceReference;

// https://ci.apache.org/projects/wicket/guide/6.x/guide/ajax.html
/**
 * https://myclabs.github.io/jquery.confirm/<br>
 * https://github.com/myclabs/jquery.confirm<br>
 * version 2.7.0<br>
 * <style>.modal-header .close { margin: 0px !important; padding: 0px !important; } .modal-header { display: block !important; }</style>
 */
public class BootstrapConfirmation {
    public static final JavaScriptResourceReference JS = new JavaScriptResourceReference(BootstrapConfirmation.class, "jquery.confirm.js");

    static {
        JS.addJavaScriptResourceReferenceDependency(Bootstrap4.JS);
    }

    public static final OnDomReadyHeaderItem JS_FACTORY = OnDomReadyHeaderItem.forScript("var _BootstrapConfirmation = false;");

    @SuppressWarnings("serial")
    public static org.apache.wicket.markup.html.form.SubmitLink confirmation(org.apache.wicket.markup.html.form.SubmitLink link, String title,
            String message) {
        link.add(new AttributeModifier("onclick", "") {
            @Override
            protected String newValue(String currentValue, String replacementValue) {
                return "$.confirm({title:'" + title + "',text:'" + message
                        + "',modalOptionsBackdrop:'static',modalOptionsKeyboard:false,confirm:function(){" + currentValue
                        + "},cancel:function(){;}});return false;";
            }
        });
        return link;
    }

    @SuppressWarnings("serial")
    public static org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink confirmation(String id, Form<?> form, String title, String message) {
        org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink link = new org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink(id, form) {
            @Override
            protected void updateAjaxAttributes(AjaxRequestAttributes attributes) {
                super.updateAjaxAttributes(attributes);

                // // https://stackoverflow.com/questions/6962301/change-the-asynchronous-jquery-dialog-to-be-synchronous
                // String script = "";
                // script += "var defer=$.Deferred();";
                // script += "$.confirm({" //
                // + "title:'" + title + "'"//
                // + ",text:'" + message + "'"//
                // + ",modalOptionsBackdrop:'static'"//
                // + ",modalOptionsKeyboard:false"//
                // + ",confirm:function(){"//
                // + "defer.resolve(true);"//
                // // + "attrs.event.target.form.submit();"//
                // + "console.log('confirmed');"//
                // + "}"//
                // + ",cancel:function(){"//
                // + "defer.resolve(false);"//
                // + "console.log('cancelled');"//
                // + "}"//
                // + "});";
                // script += "defer.promise().then(function(promised){"//
                // + "console.log(promised);"//
                // + "if(!promised){"//
                // + "attrs.event.preventDefault();"//
                // + "console.log('cancelled');"//
                // + "}"//
                // + "});";//
                // // script += "attrs.event.preventDefault();";
                // script += "console.log('returned');";
                // script += "return false;";
                //
                // // String _script = script;
                // // attributes.getAjaxCallListeners().add(new AjaxCallListener() {
                // // @Override
                // // public CharSequence getBeforeHandler(Component component) {
                // // return _script;
                // // }
                // // });
                //
                // attributes.getAjaxCallListeners().add(new AjaxCallListener().onPrecondition(script));
                //
                // // String _script = script;
                // // attributes.getAjaxCallListeners().add(new AjaxCallListener() {
                // // @Override
                // // public CharSequence getBeforeHandler(Component component) {
                // // return _script;
                // // }
                // // });

                attributes.getAjaxCallListeners().add(new AjaxCallListener().onPrecondition("if (!confirm('"
                        + StringEscapeUtils.escapeJavaScript(title) + ": " + StringEscapeUtils.escapeJavaScript(message) + "')) return false; "));
            }
        };

        // https://mysticcoders.com/blog/wicket-ajax-confirmation-modal-window

        return link;
    }
}
