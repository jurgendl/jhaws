package org.jhaws.common.web.wicket.forms.bootstrap;

import static org.jhaws.common.web.wicket.WebHelper.tag;

import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.markup.html.form.NumberTextField;
import org.apache.wicket.model.IModel;
import org.jhaws.common.web.wicket.forms.common.FormSettings;
import org.jhaws.common.web.wicket.forms.common.RatingFieldSettings;
import org.jhaws.common.web.wicket.rating.Rating;

// TODO value => data-stars=value
@SuppressWarnings("serial")
public class RatingFieldPanelAlt extends DefaultFormRowPanel<Integer, NumberTextField<Integer>, RatingFieldSettings> {
	public RatingFieldPanelAlt(IModel<?> model, Integer propertyPath, FormSettings formSettings,
			RatingFieldSettings componentSettings) {
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
				tag(tag, "min", settings.getMinimum());
				tag(tag, "max", settings.getMaximum());
				tag(tag, "step", settings.getStep());
				tag(tag, "data-min", settings.getMinimum());
				tag(tag, "data-max", settings.getMaximum());
				tag(tag, "data-step", settings.getStep());
				tag(tag, "data-size", "xs");
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
		response.render(JavaScriptHeaderItem.forReference(Rating.JS));
		response.render(CssHeaderItem.forReference(Rating.CSS));
		response.render(
				OnDomReadyHeaderItem.forScript(";$('input.rating[type=number]').each(function(){$(this).rating({"//
						+ ",'showCaption':true"//
						+ ",'size':'xs'"//
						+ ",min:0"//
						+ ",max:10"//
						+ ",step:1"//
						+ "});});"));
	}
}
