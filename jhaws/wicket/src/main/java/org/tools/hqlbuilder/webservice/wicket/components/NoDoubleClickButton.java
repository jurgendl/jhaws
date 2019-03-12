package org.tools.hqlbuilder.webservice.wicket.components;

import org.apache.wicket.markup.html.form.Button;

@SuppressWarnings("serial")
public class NoDoubleClickButton extends Button {

    public NoDoubleClickButton(String id) {
        super(id);
    }

    @Override
    protected String getOnClickScript() {
        return PreventDoubleClickBehaviorButtons.getEnableDisableJavascript(NoDoubleClickButton.this, true);
    }

}