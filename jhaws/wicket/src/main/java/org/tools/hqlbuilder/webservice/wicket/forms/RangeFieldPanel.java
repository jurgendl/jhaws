package org.tools.hqlbuilder.webservice.wicket.forms;

import static org.tools.hqlbuilder.webservice.wicket.WebHelper.tag;

import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.head.OnLoadHeaderItem;
import org.apache.wicket.markup.html.form.RangeTextField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.resource.JavaScriptResourceReference;
import org.tools.hqlbuilder.webservice.jquery.ui.primeui.PrimeUI;

import de.agilecoders.wicket.core.markup.html.bootstrap.behavior.CssClassNameAppender;

/**
 * @see http://demosthenes.info/blog/757/Playing-With-The-HTML5-range-Slider-Input
 */
public class RangeFieldPanel<N extends Number & Comparable<N>> extends DefaultFormRowPanel<N, RangeTextField<N>, RangeFieldSettings<N>> {
    public static final String STEP = "step";

    public static final String MAX = "max";

    public static final String MIN = "min";

    public static final String LIST = "list";

    private static final long serialVersionUID = 317764716316092786L;

    public static final JavaScriptResourceReference RANGEFIELD_REFERENCE = new JavaScriptResourceReference(RangeFieldPanel.class, "RangeField.js");

    protected Output output;

    public RangeFieldPanel(IModel<?> model, N propertyPath, FormSettings formSettings, RangeFieldSettings<N> rangeFieldSettings) {
        super(model, propertyPath, formSettings, rangeFieldSettings);
    }

    @Override
    protected FormRowPanel<N, N, RangeTextField<N>, RangeFieldSettings<N>> addComponents() {
        output = new Output(VALUE_OUTPUT, getValueModel()) {
            private static final long serialVersionUID = 2019925551669937151L;

            @Override
            protected void onComponentTag(ComponentTag tag) {
                super.onComponentTag(tag);
                tag.getAttributes().put(FOR, getPropertyName());
            }
        };
        add(output.add(new CssClassNameAppender(PrimeUI.puiinputtext)));
        super.addComponents();
        return this;
    }

    @Override
    protected RangeTextField<N> createComponent(IModel<N> model, Class<N> valueType) {
        RangeTextField<N> rangeTextField = new RangeTextField<N>(VALUE, model, valueType) {
            private static final long serialVersionUID = 5507304679724490593L;

            @Override
            protected void onComponentTag(ComponentTag tag) {
                super.onComponentTag(tag);
                onFormComponentTag(tag);
                RangeFieldSettings<N> settings = getComponentSettings();
                tag(tag, MIN, settings.getMinimum());
                tag(tag, MAX, settings.getMaximum());
                tag(tag, STEP, settings.getStep());
                // should be "onchange"
                tag.getAttributes().put("oninput", "outputUpdate('" + output.getMarkupId() + "',value)");
                if (tag.getAttributes().containsKey(LIST)) {
                    tag.getAttributes().put(LIST, getMarkupId() + "_" + tag.getAttributes().get(LIST));
                }
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
        response.render(JavaScriptHeaderItem.forReference(RANGEFIELD_REFERENCE));
        RangeFieldSettings<N> settings = getComponentSettings();
        if (settings.getTickStep() != null) {
            response.render(OnLoadHeaderItem.forScript("ticks('" + getComponent().getMarkupId() + "'," + settings.getTickStep() + ")"));
        }
    }
}
