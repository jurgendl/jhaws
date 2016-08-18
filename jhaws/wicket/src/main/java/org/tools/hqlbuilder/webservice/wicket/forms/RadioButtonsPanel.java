package org.tools.hqlbuilder.webservice.wicket.forms;

import java.io.Serializable;
import java.util.List;

import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.form.RadioChoice;
import org.apache.wicket.model.IModel;

/**
 * @see http://jqueryui.com/button/
 */
public class RadioButtonsPanel<T extends Serializable> extends DefaultFormRowPanel<T, RadioChoice<T>, FormElementSettings> {
    private static final long serialVersionUID = 1409542083276399035L;

    protected IModel<List<T>> choices;

    protected IChoiceRenderer<T> renderer;

    public RadioButtonsPanel(IModel<?> model, T propertyPath, FormSettings formSettings, FormElementSettings componentSettings,
            IModel<List<T>> choices, IChoiceRenderer<T> renderer) {
        super(model, propertyPath, formSettings, componentSettings);
        this.choices = choices;
        this.renderer = renderer;
    }

    @Override
    protected RadioChoice<T> createComponent(IModel<T> model, Class<T> valueType) {
        RadioChoice<T> radioChoice = new RadioChoice<T>(VALUE, model, choices, renderer);
        radioChoice.setPrefix("<span class=\"jquibuttonset\">");
        radioChoice.setSuffix("</span>");
        return radioChoice;
    }
}
