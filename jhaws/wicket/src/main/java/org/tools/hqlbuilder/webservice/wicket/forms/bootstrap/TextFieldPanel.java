package org.tools.hqlbuilder.webservice.wicket.forms.bootstrap;

import java.io.Serializable;

import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;
import org.tools.hqlbuilder.webservice.wicket.forms.common.FormElementSettings;
import org.tools.hqlbuilder.webservice.wicket.forms.common.FormSettings;

@SuppressWarnings("serial")
public class TextFieldPanel<T extends Serializable> extends DefaultFormRowPanel<T, TextField<T>, FormElementSettings> {
    public TextFieldPanel(final IModel<?> model, final T propertyPath, FormSettings formSettings, FormElementSettings componentSettings) {
        super(model, propertyPath, formSettings, componentSettings);
    }

    @Override
    protected TextField<T> createComponent(IModel<T> model, Class<T> valueType) {
        TextField<T> textField = new TextField<T>(VALUE, model, valueType) {
            @Override
            protected void onComponentTag(ComponentTag tag) {
                super.onComponentTag(tag);
                onFormComponentTag(tag);
            }
        };
        return textField;
    }
}
