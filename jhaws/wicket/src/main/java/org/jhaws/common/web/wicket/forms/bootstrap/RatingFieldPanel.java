package org.jhaws.common.web.wicket.forms.bootstrap;

import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.markup.html.form.NumberTextField;
import org.apache.wicket.model.IModel;
import org.jhaws.common.io.FilePath;
import org.jhaws.common.lambda.LambdaPath;
import org.jhaws.common.web.wicket.CssResourceReference;
import org.jhaws.common.web.wicket.forms.common.FormSettings;
import org.jhaws.common.web.wicket.forms.common.RatingFieldSettings;

import static org.jhaws.common.web.wicket.WebHelper.tag;

@SuppressWarnings("serial")
public class RatingFieldPanel extends DefaultFormRowPanel<Integer, NumberTextField<Integer>, RatingFieldSettings> {
    public RatingFieldPanel(IModel<?> model, LambdaPath<?, Integer> propertyPath, FormSettings formSettings, RatingFieldSettings componentSettings) {
        super(model, propertyPath, formSettings, componentSettings);
    }

    @Override
    protected NumberTextField<Integer> createComponent(IModel<Integer> model, Class<Integer> valueType) {
        return new NumberTextField<Integer>(VALUE, model, valueType) {
            @Override
            protected void onComponentTag(ComponentTag tag) {
                super.onComponentTag(tag);
                onFormComponentTag(tag);
                RatingFieldSettings settings = getComponentSettings();
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
