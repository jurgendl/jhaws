package org.tools.hqlbuilder.webservice.wicket.forms.bootstrap;

import java.io.Serializable;

import org.apache.commons.lang3.StringUtils;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;
import org.tools.hqlbuilder.webservice.wicket.WebHelper;
import org.tools.hqlbuilder.webservice.wicket.forms.common.FormSettings;
import org.tools.hqlbuilder.webservice.wicket.forms.common.TextFieldSettings;

@SuppressWarnings("serial")
public class TextFieldPanel<T extends Serializable> extends DefaultFormRowPanel<T, TextField<T>, TextFieldSettings> {
	public TextFieldPanel(final IModel<?> model, final T propertyPath, FormSettings formSettings,
			TextFieldSettings componentSettings) {
		super(model, propertyPath, formSettings, componentSettings);
	}

	@Override
	protected void onFormComponentTag(ComponentTag tag) {
		super.onFormComponentTag(tag);
		if (getComponentSettings().getMinlength() != null) {
			WebHelper.tag(tag, "minlength", String.valueOf(getComponentSettings().getMinlength()));
		}
		if (getComponentSettings().getMinlength() != null) {
			WebHelper.tag(tag, "maxlength", String.valueOf(getComponentSettings().getMaxlength()));
		}
		if (Boolean.FALSE.equals(getComponentSettings().getAutocomplete())) {
			WebHelper.tag(tag, "autocomplete", "off");
		}
		if (StringUtils.isNotBlank(getComponentSettings().getPattern())) {
			WebHelper.tag(tag, "pattern", getComponentSettings().getPattern());
		}
		if (Boolean.TRUE.equals(getComponentSettings().getReplaceAllOnDrop())) {
			WebHelper.tag(tag, "ondrop",
					";event.preventDefault();$(event.target).val(event.dataTransfer.getData('text'));");
		}
		if (Boolean.TRUE.equals(getComponentSettings().getSelectAllOnFocus())) {
			WebHelper.tag(tag, "onClick", ";this.setSelectionRange(0,this.value.length);");
		}
		if (StringUtils.isNotBlank(getComponentSettings().getPlaceholder())) {
			WebHelper.tag(tag, "placeholder", getComponentSettings().getPlaceholder());
		}
	}

	@Override
	protected TextField<T> createComponent(IModel<T> model, Class<T> valueType) {
		TextField<T> textField = new TextField<T>(VALUE, model, valueType) {
			@Override
			protected void onComponentTag(ComponentTag tag) {
				super.onComponentTag(tag);
				onFormComponentTag(tag);
			}
		};
		return textField;
	}
}
