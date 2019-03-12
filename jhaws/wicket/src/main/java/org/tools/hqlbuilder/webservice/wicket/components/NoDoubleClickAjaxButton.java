package org.tools.hqlbuilder.webservice.wicket.components;

import org.apache.wicket.ajax.attributes.AjaxRequestAttributes;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;

@SuppressWarnings("serial")
public class NoDoubleClickAjaxButton extends AjaxButton {

    public NoDoubleClickAjaxButton(String id) {
        super(id);
    }

    @Override
    protected void updateAjaxAttributes(AjaxRequestAttributes attributes) {
        super.updateAjaxAttributes(attributes);

        attributes.getAjaxCallListeners().add(new PreventDoubleClickBehaviorButtons());
    }
}