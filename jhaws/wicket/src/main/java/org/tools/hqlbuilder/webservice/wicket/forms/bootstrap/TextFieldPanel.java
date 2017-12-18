package org.tools.hqlbuilder.webservice.wicket.forms.bootstrap;

import java.io.Serializable;

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
	protected TextField<T> createComponent(IModel<T> model, Class<T> valueType) {
		TextField<T> textField = new TextField<T>(VALUE, model, valueType) {
			@Override
			protected void onComponentTag(ComponentTag tag) {
				super.onComponentTag(tag);
				onFormComponentTag(tag);
				if (Boolean.TRUE.equals(getComponentSettings().getReplaceAllOnDrop())) {
					WebHelper.tag(tag, "ondrop",
							";event.preventDefault();$(event.target).val(event.dataTransfer.getData('text'));");
				}
				if (Boolean.TRUE.equals(getComponentSettings().getSelectAllOnFocus())) {
					WebHelper.tag(tag, "onClick", ";this.setSelectionRange(0,this.value.length);");
				}
			}
		};
		return textField;
	}
}
