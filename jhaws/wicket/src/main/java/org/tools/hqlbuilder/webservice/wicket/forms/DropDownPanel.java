package org.tools.hqlbuilder.webservice.wicket.forms;

import java.io.Serializable;
import java.util.List;

import org.apache.wicket.extensions.markup.html.form.select.IOptionRenderer;
import org.apache.wicket.extensions.markup.html.form.select.Select;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.model.IModel;
import org.tools.hqlbuilder.webservice.jquery.ui.primeui.PrimeUI;

import de.agilecoders.wicket.core.markup.html.bootstrap.behavior.CssClassNameAppender;

/**
 * @see http://www.primefaces.org/primeui/dropdown.html
 */
public class DropDownPanel<T extends Serializable> extends SelectPanel<T, Select<T>, DropDownSettings> {
    private static final long serialVersionUID = -4693793144091792295L;

    public DropDownPanel(IModel<?> model, T propertyPath, FormSettings formSettings, DropDownSettings componentSettings, IOptionRenderer<T> renderer,
            IModel<List<T>> choices) {
        super(model, propertyPath, formSettings, componentSettings, renderer, choices);
    }

    public DropDownPanel(IModel<?> model, T propertyPath, FormSettings formSettings, DropDownSettings componentSettings, IOptionRenderer<T> renderer,
            IModel<List<T>>[] choices, IModel<String>[] groupLabels) {
        super(model, propertyPath, formSettings, componentSettings, renderer, choices, groupLabels);
    }

    @Override
    protected Select<T> createComponent(IModel<T> model) {
        Select<T> select = new Select<T>(VALUE, model) {
            private static final long serialVersionUID = 1143647284311142999L;

            @Override
            protected void onComponentTag(ComponentTag tag) {
                super.onComponentTag(tag);
                onFormComponentTag(tag);
            }
        };
        select.add(new CssClassNameAppender(PrimeUI.puidropdown));
        return select;
    }

    @Override
    protected boolean isNullValid() {
        return getComponentSettings().isNullValid();
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
