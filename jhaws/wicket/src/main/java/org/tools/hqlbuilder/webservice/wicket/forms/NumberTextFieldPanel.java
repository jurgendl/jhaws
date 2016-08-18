package org.tools.hqlbuilder.webservice.wicket.forms;

import static org.tools.hqlbuilder.webservice.wicket.WebHelper.tag;

import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;
import org.tools.hqlbuilder.webservice.jquery.ui.primeui.PrimeUI;

import de.agilecoders.wicket.core.markup.html.bootstrap.behavior.CssClassNameAppender;

/**
 * @see http://www.primefaces.org/primeui/spinner.html
 */
public class NumberTextFieldPanel<N extends Number & Comparable<N>> extends DefaultFormRowPanel<N, TextField<N>, NumberFieldSettings<N>> {
    private static final long serialVersionUID = 2490571767214451220L;

    public NumberTextFieldPanel(IModel<?> model, N propertyPath, FormSettings formSettings, NumberFieldSettings<N> componentSettings) {
        super(model, propertyPath, formSettings, componentSettings);
    }

    @Override
    protected TextField<N> createComponent(IModel<N> model, Class<N> valueType) {
        TextField<N> textField = new TextField<N>(VALUE, model, valueType) {
            private static final long serialVersionUID = -8892429029495702023L;

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
        textField.add(new CssClassNameAppender(PrimeUI.puispinner));
        return textField;
    }

    @Override
    protected void setupPlaceholder(ComponentTag tag) {
        //
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
