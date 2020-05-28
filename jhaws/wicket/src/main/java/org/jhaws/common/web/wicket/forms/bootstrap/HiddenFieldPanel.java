package org.jhaws.common.web.wicket.forms.bootstrap;

import java.io.Serializable;

import org.apache.wicket.markup.html.form.HiddenField;
import org.apache.wicket.model.IModel;
import org.jhaws.common.web.wicket.forms.common.FormElementSettings;
import org.jhaws.common.web.wicket.forms.common.FormSettings;

@SuppressWarnings("serial")
public class HiddenFieldPanel<T extends Serializable> extends DefaultFormRowPanel<T, HiddenField<T>, FormElementSettings> {
    public HiddenFieldPanel(final IModel<?> model, final T propertyPath) {
        super(model, propertyPath, new FormSettings(), new FormElementSettings());
    }

    @Override
    protected HiddenField<T> createComponent(IModel<T> model, Class<T> valueType) {
        return new HiddenField<>(VALUE, model, valueType);
    }

    @Override
    public FormRowPanel<T, T, HiddenField<T>, FormElementSettings> addComponents(FormElementSettings settings) {
        HiddenField<T> comp = getComponent();
        this.add(comp);
        return this;
    }

    @Override
    protected void setupRequired(HiddenField<T> component) {
        //
    }

    @Override
    public boolean takesUpSpace() {
        return false;
    }
}
