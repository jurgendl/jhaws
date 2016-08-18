package org.tools.hqlbuilder.webservice.wicket.forms;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import org.apache.wicket.extensions.markup.html.form.select.IOptionRenderer;
import org.apache.wicket.extensions.markup.html.form.select.Select;
import org.apache.wicket.extensions.markup.html.form.select.SelectOptions;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.tools.hqlbuilder.webservice.jquery.ui.primeui.PrimeUI;
import org.tools.hqlbuilder.webservice.wicket.WebHelper;
import org.tools.hqlbuilder.webservice.wicket.components.DefaultOptionRenderer;

import de.agilecoders.wicket.core.markup.html.bootstrap.behavior.CssClassNameAppender;

/**
 * @see http://www.primefaces.org/primeui/listbox.html
 */
public class MultiListPanel<O extends Serializable, T extends Collection<O>> extends FormRowPanel<T, T, Select<T>, MultiListSettings> {
    private static final long serialVersionUID = 167148397490967806L;

    protected IOptionRenderer<O> renderer;

    protected IModel<List<O>> choices;

    protected Class<O> type;

    public MultiListPanel(IModel<?> model, T propertyPath, FormSettings formSettings, MultiListSettings componentSettings,
            IOptionRenderer<O> renderer, IModel<List<O>> choices) {
        super(model, propertyPath, formSettings, componentSettings);
        this.renderer = this.fallback(renderer);
        this.choices = choices;
    }

    @Override
    protected Select<T> createComponent(IModel<T> model, Class<T> valueType) {
        if (this.getComponentSettings().getSize() <= 1) {
            throw new IllegalArgumentException("getComponentSettings().getSize()<=1");
        }
        if (model.getObject() == null) {
            throw new NullPointerException("model object");
        }
        Select<T> select = new Select<T>(FormConstants.VALUE, model) {
            private static final long serialVersionUID = -3408379598404381390L;

            @Override
            protected void onComponentTag(ComponentTag tag) {
                super.onComponentTag(tag);
                MultiListPanel.this.onFormComponentTag(tag);
                WebHelper.tag(tag, "multiple", "multiple");
                WebHelper.tag(tag, "size", MultiListPanel.this.getComponentSettings().getSize());
            }
        };
        SelectOptions<O> options = new SelectOptions<O>("options", this.choices, this.renderer);
        select.add(options);
        select.add(new CssClassNameAppender(PrimeUI.puilistbox));
        return select;
    }

    protected IOptionRenderer<O> fallback(IOptionRenderer<O> r) {
        if (r == null) {
            r = new DefaultOptionRenderer<O>();
        }
        return r;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public Class<T> getPropertyType() {
        Class c = Collection.class;
        return c;
    }

    @Override
    public IModel<T> getValueModel() {
        if (this.valueModel == null) {
            String property = this.getPropertyName();
            this.valueModel = property == null ? null : new PropertyModel<T>(this.getDefaultModel(), property);
        }
        return this.valueModel;
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        if (!this.isEnabledInHierarchy()) {
            return;
        }
        response.render(JavaScriptHeaderItem.forReference(PrimeUI.PRIME_UI_FACTORY_JS));
    }

    @Override
    protected void setupPlaceholder(ComponentTag tag) {
        //
    }

    @Override
    public FormRowPanel<T, T, Select<T>, MultiListSettings> setValueModel(IModel<T> model) {
        this.getComponent().setModel(model);
        return super.setValueModel(model);
    }
}
