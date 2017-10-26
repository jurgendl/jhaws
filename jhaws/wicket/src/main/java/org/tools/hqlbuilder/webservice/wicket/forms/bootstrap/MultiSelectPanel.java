package org.tools.hqlbuilder.webservice.wicket.forms.bootstrap;

import java.io.Serializable;
import java.util.List;

import org.apache.wicket.extensions.markup.html.form.select.IOptionRenderer;
import org.apache.wicket.extensions.markup.html.form.select.Select;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.head.OnLoadHeaderItem;
import org.apache.wicket.model.IModel;
import org.tools.hqlbuilder.webservice.bootstrap4.multiselect.MultiSelect;
import org.tools.hqlbuilder.webservice.wicket.forms.common.FormSettings;
import org.tools.hqlbuilder.webservice.wicket.forms.common.ListSettings;

@SuppressWarnings("serial")
public class MultiSelectPanel<T extends Serializable> extends SelectPanel<T, Select<T>, ListSettings> {
    public MultiSelectPanel(IModel<?> model, T propertyPath, FormSettings formSettings, ListSettings componentSettings, IOptionRenderer<T> renderer,
            IModel<List<T>> choices) {
        super(model, propertyPath, formSettings, componentSettings, renderer, choices);
    }

    public MultiSelectPanel(IModel<?> model, T propertyPath, FormSettings formSettings, ListSettings componentSettings, IOptionRenderer<T> renderer,
            IModel<List<T>>[] choices, IModel<String>[] groupLabels) {
        super(model, propertyPath, formSettings, componentSettings, renderer, choices, groupLabels);
    }

    @Override
    protected boolean isNullValid() {
        return false;
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
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.render(CssHeaderItem.forReference(MultiSelect.CSS));
        response.render(JavaScriptHeaderItem.forReference(MultiSelect.JS));
        response.render(OnLoadHeaderItem.forScript(MultiSelect.JS_FACTORY));
    }
}
