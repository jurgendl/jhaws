package org.tools.hqlbuilder.webservice.bootstrap4.confirmation_jquery;

import java.util.List;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.attributes.AjaxCallListener;
import org.apache.wicket.ajax.attributes.AjaxRequestAttributes;
import org.apache.wicket.ajax.attributes.IAjaxCallListener;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.markup.html.form.Form;
import org.tools.hqlbuilder.webservice.bootstrap4.Bootstrap4;
import org.tools.hqlbuilder.webservice.wicket.JavaScriptResourceReference;

/**
 * https://myclabs.github.io/jquery.confirm/<br>
 * https://github.com/myclabs/jquery.confirm<br>
 * version after 2.7.0 <br>
 * <style>.modal-header .close { margin: 0px !important; padding: 0px !important; } .modal-header { display: block !important; }</style>
 */
public class BootstrapConfirmation {
    public static final JavaScriptResourceReference JS = new JavaScriptResourceReference(BootstrapConfirmation.class, "jquery.confirm.js");

    static {
        JS.addJavaScriptResourceReferenceDependency(Bootstrap4.JS);
    }

    public static final OnDomReadyHeaderItem JS_FACTORY = OnDomReadyHeaderItem.forScript("var _BootstrapConfirmation = false;");

    @SuppressWarnings("serial")
    public static <C extends org.apache.wicket.markup.html.link.AbstractLink> C confirmation(C link, String title, String message) {
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
    public static AjaxSubmitLink confirmation(String id, Form<?> form, String title, String message) {
        AjaxSubmitLink link = new AjaxSubmitLink(id, form) {
            @Override
            protected void updateAjaxAttributes(AjaxRequestAttributes attributes) {
                super.updateAjaxAttributes(attributes);
                List<IAjaxCallListener> listeners = attributes.getAjaxCallListeners();
                IAjaxCallListener listener = new AjaxCallListener() {
                    @Override
                    public CharSequence getBeforeHandler(Component component) {
                        return "$.confirm({title:'" + title + "',text:'" + message
                                + "',modalOptionsBackdrop:'static',modalOptionsKeyboard:false,confirm:function(){console.log(attrs.event);/*attrs.event.target.form.submit();*/},cancel:function(){;}});"
                                + "attrs.event.preventDefault();";
                    }
                };
                listeners.add(listener);
            }
        };
        return link;
    }
}
