package org.tools.hqlbuilder.webservice.wicket.forms;

import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.model.IModel;
import org.tools.hqlbuilder.webservice.jquery.ui.primeui.PrimeUI;
import org.tools.hqlbuilder.webservice.wicket.forms.common.AbstractFormElementSettings;
import org.tools.hqlbuilder.webservice.wicket.forms.common.FormRowPanelParent;
import org.tools.hqlbuilder.webservice.wicket.forms.common.FormSettings;

@SuppressWarnings("serial")
public abstract class FormRowPanel<P, T, C extends FormComponent<T>, ElementSettings extends AbstractFormElementSettings<ElementSettings>>
        extends FormRowPanelParent<P, T, C, ElementSettings> {
    public FormRowPanel(IModel<?> model, P propertyPath, FormSettings formSettings, ElementSettings componentSettings) {
        super(false, model, propertyPath, formSettings, componentSettings);
    }

    public FormRowPanel(P propertyPath, IModel<T> valueModel, FormSettings formSettings, ElementSettings componentSettings) {
        super(false, propertyPath, valueModel, formSettings, componentSettings);
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        if (!this.isEnabledInHierarchy()) {
            return;
        }
        response.render(JavaScriptHeaderItem.forReference(PrimeUI.PRIME_UI_JS));
    }
}
