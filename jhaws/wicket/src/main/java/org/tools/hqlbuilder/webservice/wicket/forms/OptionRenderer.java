package org.tools.hqlbuilder.webservice.wicket.forms;

import java.io.Serializable;

import org.apache.wicket.extensions.markup.html.form.select.IOptionRenderer;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

public class OptionRenderer<T extends Serializable> implements IOptionRenderer<T> {
	private static final long serialVersionUID = -585868546428887624L;

	@Override
	public String getDisplayValue(T object) {
		return object == null ? null : String.valueOf(object);
	}

	@Override
	public IModel<T> getModel(T value) {
		return Model.of(value);
	}
}
