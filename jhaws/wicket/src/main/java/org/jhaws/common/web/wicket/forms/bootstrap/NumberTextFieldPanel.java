package org.jhaws.common.web.wicket.forms.bootstrap;

import static org.jhaws.common.web.wicket.WebHelper.tag;

import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;
import org.jhaws.common.web.wicket.forms.common.FormSettings;
import org.jhaws.common.web.wicket.forms.common.NumberFieldSettings;

@SuppressWarnings("serial")
public class NumberTextFieldPanel<N extends Number & Comparable<N>> extends DefaultFormRowPanel<N, TextField<N>, NumberFieldSettings<N>> {
    public NumberTextFieldPanel(IModel<?> model, N propertyPath, FormSettings formSettings, NumberFieldSettings<N> componentSettings) {
        super(model, propertyPath, formSettings, componentSettings);
    }

    @Override
    protected TextField<N> createComponent(IModel<N> model, Class<N> valueType) {
        TextField<N> textField = new TextField<N>(VALUE, model, valueType) {
            @Override
            protected void onComponentTag(ComponentTag tag) {
                super.onComponentTag(tag);
                onFormComponentTag(tag);
                NumberFieldSettings<N> settings = getComponentSettings();
                tag(tag, "min", settings.getMinimum());
                tag(tag, "max", settings.getMaximum());
                tag(tag, "step", settings.getStep());
            }
        };
        return textField;
    }

    @Override
    protected void setupPlaceholder(ComponentTag tag) {
        //
    }
}
