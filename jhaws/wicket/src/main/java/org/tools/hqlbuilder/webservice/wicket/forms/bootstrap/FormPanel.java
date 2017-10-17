package org.tools.hqlbuilder.webservice.wicket.forms.bootstrap;

import java.io.Serializable;

import org.apache.wicket.MarkupContainer;
import org.apache.wicket.markup.html.form.FormComponent;
import org.tools.hqlbuilder.webservice.wicket.forms.common.AbstractFormElementSettings;
import org.tools.hqlbuilder.webservice.wicket.forms.common.FormActions;
import org.tools.hqlbuilder.webservice.wicket.forms.common.FormElementSettings;
import org.tools.hqlbuilder.webservice.wicket.forms.common.FormPanelParent;
import org.tools.hqlbuilder.webservice.wicket.forms.common.FormSettings;

@SuppressWarnings("serial")
public class FormPanel<T extends Serializable> extends FormPanelParent<T> {
    public FormPanel(String id, FormActions<T> formActions, FormSettings formSettings) {
        super(id, formActions, formSettings);
        bootstrap = true;
    }

    public <PropertyType extends Serializable, ComponentType extends FormComponent<PropertyType>, ElementSettings extends AbstractFormElementSettings<ElementSettings>, RowPanel extends DefaultFormRowPanel<PropertyType, ComponentType, ElementSettings>> RowPanel addDefaultRow(
            RowPanel rowpanel) {
        return this.addRow(rowpanel);
    }

    protected MarkupContainer newEmptyPanel() {
        return new EmptyFormPanel();
    }

    public <F extends Serializable> TextFieldPanel<F> addTextField(F propertyPath, FormElementSettings componentSettings) {
        return this.addDefaultRow(new TextFieldPanel<>(this.getFormModel(), propertyPath, this.getFormSettings(), componentSettings));
    }
}
