package org.tools.hqlbuilder.webservice.wicket.forms;

import org.apache.wicket.extensions.validation.validator.RfcCompliantEmailAddressValidator;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.html.form.EmailTextField;
import org.apache.wicket.model.IModel;
import org.tools.hqlbuilder.webservice.jquery.ui.primeui.PrimeUI;

import de.agilecoders.wicket.core.markup.html.bootstrap.behavior.CssClassNameAppender;

/**
 * @see http://www.primefaces.org/primeui/inputtext.html
 */
public class EmailTextFieldPanel extends DefaultFormRowPanel<String, EmailTextField, FormElementSettings> {
    private static final long serialVersionUID = -7993592150932306594L;

    public EmailTextFieldPanel(final IModel<?> model, final String propertyPath, FormSettings formSettings, FormElementSettings componentSettings) {
        super(model, propertyPath, formSettings, componentSettings);
    }

    @Override
    protected EmailTextField createComponent(IModel<String> model, Class<String> valueType) {
        EmailTextField emailTextField = new EmailTextField(VALUE, model, RfcCompliantEmailAddressValidator.getInstance()) {
            private static final long serialVersionUID = -8333366326586690978L;

            @Override
            protected void onComponentTag(ComponentTag tag) {
                super.onComponentTag(tag);
                onFormComponentTag(tag);
            }
        };
        emailTextField.add(new CssClassNameAppender(PrimeUI.puiinputtext));
        return emailTextField;
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        if (!isEnabledInHierarchy()) {
            return;
        }
        response.render(JavaScriptHeaderItem.forReference(PrimeUI.PRIME_UI_FACTORY_JS));
    }
}
