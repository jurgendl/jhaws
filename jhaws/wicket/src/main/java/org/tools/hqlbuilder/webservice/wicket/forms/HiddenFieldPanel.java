package org.tools.hqlbuilder.webservice.wicket.forms;

import java.io.Serializable;

import org.apache.wicket.markup.html.form.HiddenField;
import org.apache.wicket.model.IModel;
import org.tools.hqlbuilder.webservice.wicket.forms.common.FormElementSettings;
import org.tools.hqlbuilder.webservice.wicket.forms.common.FormSettings;

public class HiddenFieldPanel<T extends Serializable>
		extends DefaultFormRowPanel<T, HiddenField<T>, FormElementSettings> {
	private static final long serialVersionUID = -7993592150932306594L;

	public HiddenFieldPanel(final IModel<?> model, final T propertyPath) {
		super(model, propertyPath, new FormSettings(), new FormElementSettings());
	}

	@Override
	protected HiddenField<T> createComponent(IModel<T> model, Class<T> valueType) {
		return new HiddenField<>(VALUE, model, valueType);
	}

	@Override
	public FormRowPanel<T, T, HiddenField<T>, FormElementSettings> addComponents() {
		HiddenField<T> comp = getComponent();
		this.add(comp);
		return this;
	}

	@Override
	protected void setupRequired(HiddenField<T> component) {
		//
	}

	@Override
	public boolean takesUpSpace() {
		return false;
	}
}
