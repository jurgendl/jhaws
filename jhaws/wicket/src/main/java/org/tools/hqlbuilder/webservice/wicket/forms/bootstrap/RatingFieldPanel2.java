package org.tools.hqlbuilder.webservice.wicket.forms.bootstrap;

import static org.tools.hqlbuilder.webservice.wicket.WebHelper.tag;

import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.html.form.NumberTextField;
import org.apache.wicket.model.IModel;
import org.tools.hqlbuilder.webservice.jquery.ui.jquery.JQuery;
import org.tools.hqlbuilder.webservice.wicket.JavaScriptResourceReference;
import org.tools.hqlbuilder.webservice.wicket.forms.common.FormSettings;
import org.tools.hqlbuilder.webservice.wicket.forms.common.NumberFieldSettings;

/** https://github.com/javiertoledo/bootstrap-rating-input */
@SuppressWarnings("serial")
public class RatingFieldPanel2<N extends Number & Comparable<N>> extends DefaultFormRowPanel<N, NumberTextField<N>, NumberFieldSettings<N>> {
    public RatingFieldPanel2(IModel<?> model, N propertyPath, FormSettings formSettings, NumberFieldSettings<N> componentSettings) {
        super(model, propertyPath, formSettings, componentSettings);
    }

    @Override
    protected NumberTextField<N> createComponent(IModel<N> model, Class<N> valueType) {
        return new NumberTextField<N>(VALUE, model, valueType) {
            @Override
            protected void onComponentTag(ComponentTag tag) {
                super.onComponentTag(tag);
                onFormComponentTag(tag);
                NumberFieldSettings<N> settings = getComponentSettings();
                // data-inline
                if (getComponentSettings().isReadOnly()) tag(tag, "data-readonly", "");
                tag(tag, "data-icon-lib", "fa");
                tag(tag, "data-active-icon", "fa-star");
                tag(tag, "data-inactive-icon", "fa-star-o");
                tag(tag, "data-clearable-icon", "text-danger fa-remove");
                tag(tag, "data-clearable", " remove");
                tag(tag, "min", 1);
                tag(tag, "max", settings.getMaximum());
                tag(tag, "data-min", 1);
                tag(tag, "data-max", settings.getMaximum());
                tag(tag, "step", 1);
            }
        };
    }

    @Override
    protected void setupPlaceholder(ComponentTag tag) {
        //
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.render(JavaScriptHeaderItem.forReference(new JavaScriptResourceReference(getClass(), "bootstrap-rating-input.js")
                .addJavaScriptResourceReferenceDependency(JQuery.getJQueryReference())));
    }
}
