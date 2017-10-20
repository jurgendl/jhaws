package org.tools.hqlbuilder.webservice.wicket.forms.bootstrap;

import java.io.Serializable;
import java.util.Date;

import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;
import org.tools.hqlbuilder.webservice.wicket.converter.Converter;
import org.tools.hqlbuilder.webservice.wicket.forms.common.AbstractFormElementSettings;
import org.tools.hqlbuilder.webservice.wicket.forms.common.FormSettings;

@SuppressWarnings({ "unchecked", "rawtypes", "serial" })
public class DatePickerPanel<X extends Serializable> extends DefaultFormRowPanel {
	public DatePickerPanel(IModel<?> model, Date propertyPath, FormSettings formSettings,
			AbstractFormElementSettings componentSettings) {
		super(model, propertyPath, formSettings, componentSettings);
	}

	public DatePickerPanel(IModel<?> model, X propertyPath, Converter<X, Date> dateConverter, FormSettings formSettings,
			AbstractFormElementSettings componentSettings) {
		super(model, propertyPath, formSettings, componentSettings);
	}

	@Override
	protected FormComponent createComponent(IModel model, Class valueType) {
		TextField textField = new TextField(VALUE, model, valueType) {
			@Override
			protected void onComponentTag(ComponentTag tag) {
				super.onComponentTag(tag);
				onFormComponentTag(tag);
			}
		};
		return textField;
	}
}
