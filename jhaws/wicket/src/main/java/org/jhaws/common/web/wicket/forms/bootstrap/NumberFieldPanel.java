package org.jhaws.common.web.wicket.forms.bootstrap;

import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.form.NumberTextField;
import org.apache.wicket.model.IModel;
import org.jhaws.common.lambda.LambdaPath;
import org.jhaws.common.web.wicket.forms.common.FormSettings;
import org.jhaws.common.web.wicket.forms.common.NumberFieldSettings;

import static org.jhaws.common.web.wicket.WebHelper.tag;

@SuppressWarnings("serial")
public class NumberFieldPanel<N extends Number & Comparable<N>> extends DefaultFormRowPanel<N, NumberTextField<N>, NumberFieldSettings<N>> {
    public NumberFieldPanel(IModel<?> model, LambdaPath<?, N> propertyPath, FormSettings formSettings, NumberFieldSettings<N> componentSettings) {
        super(model, propertyPath, formSettings, componentSettings);
    }

    @Override
    protected NumberTextField<N> createComponent(IModel<N> model, Class<N> valueType) {
        return new NumberTextField<N>(VALUE, model, valueType) {
            @Override
            protected void onComponentTag(ComponentTag tag) {
                super.onComponentTag(tag);
                onFormComponentTag(tag);
                NumberFieldSettings<N> settings = getComponentSettings();
                tag(tag, "min", settings.getMinimum());
                tag(tag, "max", settings.getMaximum());
                tag(tag, "step", settings.getStep());
                tag(tag, "size", String.valueOf(settings.getMaximum()).length() + 2);
            }
        };
    }

    @Override
    protected void setupPlaceholder(ComponentTag tag) {
        //
    }
}
