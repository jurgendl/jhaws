package org.tools.hqlbuilder.webservice.wicket.forms;

import static org.tools.hqlbuilder.webservice.wicket.WebHelper.tag;

import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.html.form.RangeTextField;
import org.apache.wicket.model.IModel;
import org.tools.hqlbuilder.webservice.jquery.ui.jqueryui.JQueryUI;
import org.tools.hqlbuilder.webservice.wicket.JavaScriptResourceReference;

public class SliderPanel<N extends Number & Comparable<N>> extends DefaultFormRowPanel<N, RangeTextField<N>, NumberFieldSettings<N>> {
    private static final long serialVersionUID = 1636673516673656939L;

    public static final JavaScriptResourceReference SLIDER_REFERENCE = new JavaScriptResourceReference(SliderPanel.class, "slider.js")
            .addJavaScriptResourceReferenceDependency(JQueryUI.getJQueryUIReference());

    public static final String STEP = "step";

    public static final String MAX = "max";

    public static final String MIN = "min";

    public SliderPanel(IModel<?> model, N propertyPath, FormSettings formSettings, NumberFieldSettings<N> NumberFieldSettings) {
        super(model, propertyPath, formSettings, NumberFieldSettings);
    }

    @Override
    protected RangeTextField<N> createComponent(IModel<N> model, Class<N> valueType) {
        RangeTextField<N> rangeTextField = new RangeTextField<N>(VALUE, model, valueType) {
            private static final long serialVersionUID = 5507304679724490593L;

            @Override
            protected void onComponentTag(ComponentTag tag) {
                super.onComponentTag(tag);
                onFormComponentTag(tag);
                NumberFieldSettings<N> settings = getComponentSettings();
                tag(tag, MIN, settings.getMinimum());
                tag(tag, MAX, settings.getMaximum());
                tag(tag, STEP, settings.getStep());
            }
        };
        return rangeTextField;
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
        response.render(JavaScriptHeaderItem.forReference(SLIDER_REFERENCE));
        response.render(JavaScriptHeaderItem.forScript("$( document ).ready(function() { sliderWidget('" + getComponent().getMarkupId() + "'); }); ",
                "js_slider_" + getComponent().getMarkupId()));
    }
}
