package org.tools.hqlbuilder.webservice.wicket.forms.bootstrap;

import java.io.Serializable;
import java.util.List;

import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.model.IModel;
import org.tools.hqlbuilder.webservice.wicket.forms.common.FormElementSettings;
import org.tools.hqlbuilder.webservice.wicket.forms.common.FormSettings;

@SuppressWarnings("serial")
public class RadioButtonsAltPanel<T extends Serializable> extends DefaultFormRowPanel<T, BootstrapRadioChoice<T>, FormElementSettings> {
    protected IModel<List<T>> choices;

    protected IChoiceRenderer<T> renderer;

    public RadioButtonsAltPanel(IModel<?> model, T propertyPath, FormSettings formSettings, FormElementSettings componentSettings,
            IModel<List<T>> choices, IChoiceRenderer<T> renderer) {
        super(model, propertyPath, formSettings, componentSettings);
        this.choices = choices;
        this.renderer = renderer;
    }

    @Override
    protected BootstrapRadioChoice<T> createComponent(IModel<T> model, Class<T> valueType) {
        BootstrapRadioChoice<T> radioChoice = new BootstrapRadioChoice<>(VALUE, model, choices, renderer);
        return radioChoice;
    }
}
