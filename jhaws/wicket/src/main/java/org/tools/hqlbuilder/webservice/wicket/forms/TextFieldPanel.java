package org.tools.hqlbuilder.webservice.wicket.forms;

import java.io.Serializable;

import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;
import org.tools.hqlbuilder.webservice.jquery.ui.primeui.PrimeUI;

import de.agilecoders.wicket.core.markup.html.bootstrap.behavior.CssClassNameAppender;

/**
 * @see http://www.primefaces.org/primeui/inputtext.html
 */
public class TextFieldPanel<T extends Serializable> extends DefaultFormRowPanel<T, TextField<T>, FormElementSettings> {
    private static final long serialVersionUID = -7993592150932306594L;

    public TextFieldPanel(final IModel<?> model, final T propertyPath, FormSettings formSettings, FormElementSettings componentSettings) {
        super(model, propertyPath, formSettings, componentSettings);
    }

    @Override
    protected TextField<T> createComponent(IModel<T> model, Class<T> valueType) {
        TextField<T> textField = new TextField<T>(VALUE, model, valueType) {
            private static final long serialVersionUID = -3231896888772971388L;

            @Override
            protected void onComponentTag(ComponentTag tag) {
                super.onComponentTag(tag);
                onFormComponentTag(tag);
            }
        };
        textField.add(new CssClassNameAppender(PrimeUI.puiinputtext));
        return textField;
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
