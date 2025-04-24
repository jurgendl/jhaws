package org.jhaws.common.web.wicket.forms.bootstrap;

import org.apache.wicket.extensions.markup.html.form.select.IOptionRenderer;
import org.apache.wicket.extensions.markup.html.form.select.Select;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.model.IModel;
import org.jhaws.common.lambda.LambdaPath;
import org.jhaws.common.web.wicket.forms.common.DropDownSettings;
import org.jhaws.common.web.wicket.forms.common.FormSettings;

import java.io.Serializable;
import java.util.List;

@SuppressWarnings("serial")
public class DropDownPanel<T extends Serializable> extends SelectPanel<T, Select<T>, DropDownSettings> {
    public DropDownPanel(IModel<?> model, LambdaPath<?, T> propertyPath, FormSettings formSettings, DropDownSettings componentSettings, IOptionRenderer<T> renderer, IModel<? extends List<? extends T>> choices) {
        super(model, propertyPath, formSettings, componentSettings, renderer, choices);
    }

    public DropDownPanel(IModel<?> model, LambdaPath<?, T> propertyPath, FormSettings formSettings, DropDownSettings componentSettings, IOptionRenderer<T> renderer, IModel<? extends List<? extends T>>[] choices, IModel<String>[] groupLabels) {
        super(model, propertyPath, formSettings, componentSettings, renderer, choices, groupLabels);
    }

    public DropDownPanel(LambdaPath<?, T> propertyPath, IModel<T> valueModel, FormSettings formSettings, DropDownSettings componentSettings, IOptionRenderer<T> renderer, IModel<? extends List<? extends T>> choices) {
        super(propertyPath, valueModel, formSettings, componentSettings, renderer, choices);
    }

    public DropDownPanel(LambdaPath<?, T> propertyPath, IModel<T> valueModel, FormSettings formSettings, DropDownSettings componentSettings, IOptionRenderer<T> renderer, IModel<? extends List<? extends T>>[] choices, IModel<String>[] groupLabels) {
        super(propertyPath, valueModel, formSettings, componentSettings, renderer, choices, groupLabels);
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
