package org.tools.hqlbuilder.webservice.wicket.forms;

import static org.jhaws.common.lang.StringUtils.sortable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;
import org.tools.hqlbuilder.webservice.jquery.ui.primeui.PrimeUI;

import com.googlecode.wicket.jquery.core.renderer.ITextRenderer;
import com.googlecode.wicket.jquery.ui.form.autocomplete.AutoCompleteTextField;

import de.agilecoders.wicket.core.markup.html.bootstrap.behavior.CssClassNameAppender;

/**
 * @see http://www.primefaces.org/primeui/inputtext.html
 */
public class AutoCompleteTextFieldPanel<T extends Serializable> extends DefaultFormRowPanel<T, TextField<T>, AutoCompleteTextFieldSettings> {
    private static final long serialVersionUID = -7993592150932306594L;

    protected IModel<List<T>> choices = null;

    protected ITextRenderer<T> renderer = null;

    public AutoCompleteTextFieldPanel(final IModel<?> model, final T propertyPath, FormSettings formSettings,
            AutoCompleteTextFieldSettings componentSettings, IModel<List<T>> choices, ITextRenderer<T> renderer) {
        super(model, propertyPath, formSettings, componentSettings);
        this.choices = choices;
        this.renderer = renderer;
    }

    @Override
    protected TextField<T> createComponent(IModel<T> model, Class<T> valueType) {
        AutoCompleteTextField<T> textField = new AutoCompleteTextField<T>(VALUE, model, renderer, valueType) {
            private static final long serialVersionUID = -3401872343978963488L;

            @Override
            protected void onComponentTag(ComponentTag tag) {
                super.onComponentTag(tag);
                onFormComponentTag(tag);
            }

            @Override
            protected List<T> getChoices(String input) {
                List<T> show = new ArrayList<T>();
				if (org.apache.commons.lang3.StringUtils.isBlank(input)
						|| input.length() < getComponentSettings().getMinLenght()) {
                    return show;
                }
				String search = getComponentSettings().isNormalize() ? sortable(input) : input;
                for (T choice : choices.getObject()) {
                    String text = renderer.getText(choice);
                    if (getComponentSettings().isNormalize()) {
						text = sortable(text);
                    }
                    if (getComponentSettings().isContains()) {
                        if (text.contains(search)) {
                            show.add(choice);
                        }
                    } else {
                        if (text.startsWith(search)) {
                            show.add(choice);
                        }
                    }
                    if (show.size() >= getComponentSettings().getMaxResults()) {
                        break;
                    }
                }
                return show;
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
