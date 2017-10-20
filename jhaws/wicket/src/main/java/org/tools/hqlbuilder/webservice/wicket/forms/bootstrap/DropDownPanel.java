package org.tools.hqlbuilder.webservice.wicket.forms.bootstrap;

import java.io.Serializable;
import java.util.List;

import org.apache.wicket.extensions.markup.html.form.select.IOptionRenderer;
import org.apache.wicket.extensions.markup.html.form.select.Select;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.model.IModel;
import org.tools.hqlbuilder.webservice.wicket.forms.common.DropDownSettings;
import org.tools.hqlbuilder.webservice.wicket.forms.common.FormSettings;

@SuppressWarnings("serial")
public class DropDownPanel<T extends Serializable> extends SelectPanel<T, Select<T>, DropDownSettings> {
    public DropDownPanel(IModel<?> model, T propertyPath, FormSettings formSettings, DropDownSettings componentSettings, IOptionRenderer<T> renderer,
            IModel<List<T>> choices) {
        super(model, propertyPath, formSettings, componentSettings, renderer, choices);
    }

    public DropDownPanel(IModel<?> model, T propertyPath, FormSettings formSettings, DropDownSettings componentSettings, IOptionRenderer<T> renderer,
            IModel<List<T>>[] choices, IModel<String>[] groupLabels) {
        super(model, propertyPath, formSettings, componentSettings, renderer, choices, groupLabels);
    }

    @Override
    protected Select<T> createComponent(IModel<T> model, int size) {
        Select<T> select = new Select<T>(VALUE, model) {
            @Override
            protected void onComponentTag(ComponentTag tag) {
                super.onComponentTag(tag);
                onFormComponentTag(tag);
            }
        };
        return select;
    }

    @Override
    protected boolean isNullValid() {
        return getComponentSettings().isNullValid();
    }
}