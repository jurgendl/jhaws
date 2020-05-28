package org.jhaws.common.web.wicket.forms.bootstrap;

import static org.jhaws.common.web.wicket.WebHelper.tag;

import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.markup.html.form.NumberTextField;
import org.apache.wicket.model.IModel;
import org.jhaws.common.web.wicket.JavaScriptResourceReference;
import org.jhaws.common.web.wicket.forms.common.FormSettings;
import org.jhaws.common.web.wicket.forms.common.RatingFieldSettings;
import org.jhaws.common.web.wicket.jquery.JQuery;

/**
 * https://github.com/javiertoledo/bootstrap-rating-input v0.4
 */
@SuppressWarnings("serial")
public class RatingFieldPanel2 extends DefaultFormRowPanel<Integer, NumberTextField<Integer>, RatingFieldSettings> {
	public RatingFieldPanel2(IModel<?> model, Integer propertyPath, FormSettings formSettings,
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
				if (getComponentSettings().isReadOnly()) {
					tag(tag, "data-readonly", "");
				}
				if (Boolean.TRUE.equals(getComponentSettings().getInline())) {
					tag(tag, "data-inline", "");
				}
				tag(tag, "data-icon-lib", "");
				tag(tag, "data-active-icon", "fas fa-star text-primary");
				tag(tag, "data-inactive-icon", "far fa-star text-secondary");
				if (Boolean.TRUE.equals(getComponentSettings().getClearable())) {
					tag(tag, "data-clearable-icon", "fas fa-times-circle text-danger");
					tag(tag, "data-clearable", ".");
					tag(tag, "data-clearable-remain", "true");
				}
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
		response.render(JavaScriptHeaderItem
				.forReference(new JavaScriptResourceReference(getClass(), "bootstrap-rating-input.js")
						.addJavaScriptResourceReferenceDependency(JQuery.getJQueryReference())));
		response.render(
				OnDomReadyHeaderItem.forScript(";$('input.rating[type=number]').each(function(){$(this).rating();});"));
	}
}
