package org.tools.hqlbuilder.webservice.wicket.components;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxCheckBox;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.tools.hqlbuilder.webservice.wicket.actions.OnAction;

public class CheckBoxPanel extends Panel {
    private static final long serialVersionUID = 3691695913774240085L;

    private CheckBox field;

    private Label checkboxlabel;

    public CheckBoxPanel(String id) {
        this(id, new Model<Boolean>(), null);
    }

    public CheckBoxPanel(String id, IModel<Boolean> model, final OnAction ajax) {
        super(id);
        this.field = ajax != null ? new AjaxCheckBox("checkBox", model) {
            private static final long serialVersionUID = 8913702086739100178L;

            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                ajax.doAction(target);
            }

        } : new CheckBox("checkBox", model);
        this.add(this.field);
        this.checkboxlabel = new Label("checkboxlabel", Model.of(""));
        this.add(this.checkboxlabel);
    }

    public Label getCheckboxlabel() {
        return this.checkboxlabel;
    }

    public CheckBox getField() {
        return this.field;
    }
}