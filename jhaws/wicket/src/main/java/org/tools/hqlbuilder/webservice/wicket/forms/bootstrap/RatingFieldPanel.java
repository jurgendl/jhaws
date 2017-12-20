package org.tools.hqlbuilder.webservice.wicket.forms.bootstrap;

import static org.tools.hqlbuilder.webservice.wicket.WebHelper.tag;

import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.markup.html.form.NumberTextField;
import org.apache.wicket.model.IModel;
import org.jhaws.common.io.FilePath;
import org.tools.hqlbuilder.webservice.wicket.CssResourceReference;
import org.tools.hqlbuilder.webservice.wicket.forms.common.FormSettings;
import org.tools.hqlbuilder.webservice.wicket.forms.common.NumberFieldSettings;

@SuppressWarnings("serial")
public class RatingFieldPanel<N extends Number & Comparable<N>> extends DefaultFormRowPanel<N, NumberTextField<N>, NumberFieldSettings<N>> {
    public RatingFieldPanel(IModel<?> model, N propertyPath, FormSettings formSettings, NumberFieldSettings<N> componentSettings) {
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
                tag(tag, "min", 1);
                tag(tag, "max", settings.getMaximum());
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
        response.render(CssHeaderItem.forReference(new CssResourceReference(getClass(), getClass().getSimpleName() + ".css")));
        response.render(OnDomReadyHeaderItem.forScript(new FilePath(getClass(), getClass().getSimpleName() + ".js").readAll()));
    }
}
