package org.jhaws.common.web.wicket.forms.bootstrap;

import java.io.Serializable;

import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.jhaws.common.web.wicket.WebHelper;
import org.jhaws.common.web.wicket.forms.common.AbstractFormElementSettings;
import org.jhaws.common.web.wicket.forms.common.FormRowPanelParent;
import org.jhaws.common.web.wicket.forms.common.FormSettings;

@SuppressWarnings("serial")
public abstract class DefaultFormRowPanel<T, C extends FormComponent<T>, S extends AbstractFormElementSettings<S>> extends FormRowPanel<T, T, C, S> {
    public DefaultFormRowPanel(IModel<?> model, T propertyPath, FormSettings formSettings, S componentSettings) {
        super(model, propertyPath, formSettings, componentSettings);
    }

    public DefaultFormRowPanel(T propertyPath, IModel<T> valueModel, FormSettings formSettings, S componentSettings) {
        super(propertyPath, valueModel, formSettings, componentSettings);
    }

    @Override
    public FormRowPanelParent<T, T, C, S> setValueModel(IModel<T> model) {
        getComponent().setModel(model);
        return super.setValueModel(model);
    }

    @Override
    public IModel<T> getValueModel() {
        if (valueModel == null) {
            String property = getPropertyName();
            valueModel = property == null ? null : new PropertyModel<>(getDefaultModel(), property);
        }
        return valueModel;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Class<T> getPropertyType() {
        if (propertyType == null) {
            try {
                this.propertyType = WebHelper.type(propertyPath);
            } catch (ch.lambdaj.function.argument.ArgumentConversionException ex) {
                try {
                    this.propertyType = WebHelper.<T>getImplementation(this, DefaultFormRowPanel.class);
                } catch (IllegalArgumentException ex2) {
                    this.propertyType = (Class<T>) Serializable.class;
                }
            }
        }
        return this.propertyType;
    }
}