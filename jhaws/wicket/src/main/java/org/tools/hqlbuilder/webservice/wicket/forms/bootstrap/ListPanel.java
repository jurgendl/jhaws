package org.tools.hqlbuilder.webservice.wicket.forms.bootstrap;

import static org.tools.hqlbuilder.webservice.wicket.WebHelper.tag;

import java.io.Serializable;
import java.util.List;

import org.apache.wicket.extensions.markup.html.form.select.IOptionRenderer;
import org.apache.wicket.extensions.markup.html.form.select.Select;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.model.IModel;
import org.tools.hqlbuilder.webservice.wicket.forms.common.FormSettings;
import org.tools.hqlbuilder.webservice.wicket.forms.common.ListSettings;

@SuppressWarnings("serial")
public class ListPanel<T extends Serializable> extends SelectPanel<T, Select<T>, ListSettings> {
    public ListPanel(IModel<?> model, T propertyPath, FormSettings formSettings, ListSettings componentSettings, IOptionRenderer<T> renderer,
            IModel<? extends List<? extends T>> choices) {
        super(model, propertyPath, formSettings, componentSettings, renderer, choices);
    }

    public ListPanel(IModel<?> model, T propertyPath, FormSettings formSettings, ListSettings componentSettings, IOptionRenderer<T> renderer,
            IModel<? extends List<? extends T>>[] choices, IModel<String>[] groupLabels) {
        super(model, propertyPath, formSettings, componentSettings, renderer, choices, groupLabels);
    }

    @Override
    protected Select<T> createComponent(IModel<T> model, int size) {
        if (getComponentSettings().getSize() <= 1) {
            throw new IllegalArgumentException("getComponentSettings().getSize()<=1");
        }
        Select<T> select = new Select<T>(VALUE, model) {
            @Override
            protected void onComponentTag(ComponentTag tag) {
                super.onComponentTag(tag);
                onFormComponentTag(tag);
                tag(tag, "size", Math.min(size, getComponentSettings().getSize()));
                tag(tag, "style", "height: auto");
            }
        };
        return select;
    }

    @Override
    protected boolean isNullValid() {
        return getComponentSettings().isNullValid();
    }
}
