package org.tools.hqlbuilder.webservice.wicket.forms;

import static org.tools.hqlbuilder.webservice.wicket.WebHelper.tag;

import java.io.Serializable;
import java.util.List;

import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.extensions.markup.html.form.select.IOptionRenderer;
import org.apache.wicket.extensions.markup.html.form.select.Select;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.model.IModel;
import org.tools.hqlbuilder.webservice.jquery.ui.primeui.PrimeUI;
import org.tools.hqlbuilder.webservice.wicket.forms.common.FormSettings;
import org.tools.hqlbuilder.webservice.wicket.forms.common.ListSettings;

/**
 * @see http://www.primefaces.org/primeui/listbox.html
 */
public class ListPanel<T extends Serializable> extends SelectPanel<T, Select<T>, ListSettings> {
    private static final long serialVersionUID = 6519523561212631975L;

    public ListPanel(IModel<?> model, T propertyPath, FormSettings formSettings, ListSettings componentSettings, IOptionRenderer<T> renderer,
            IModel<List<T>> choices) {
        super(model, propertyPath, formSettings, componentSettings, renderer, choices);
    }

    public ListPanel(IModel<?> model, T propertyPath, FormSettings formSettings, ListSettings componentSettings, IOptionRenderer<T> renderer,
            IModel<List<T>>[] choices, IModel<String>[] groupLabels) {
        super(model, propertyPath, formSettings, componentSettings, renderer, choices, groupLabels);
    }

    @Override
    protected Select<T> createComponent(IModel<T> model, int size) {
        if (getComponentSettings().getSize() <= 1) {
            throw new IllegalArgumentException("getComponentSettings().getSize()<=1");
        }
        Select<T> select = new Select<T>(VALUE, model) {
            private static final long serialVersionUID = 6509470567166194399L;

            @Override
            protected void onComponentTag(ComponentTag tag) {
                super.onComponentTag(tag);
                onFormComponentTag(tag);
                tag(tag, "size", Math.min(size, getComponentSettings().getSize()));
                tag(tag, "style", "height: auto");
            }
        };
        select.add(AttributeAppender.append("class", PrimeUI.puilistbox));
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
