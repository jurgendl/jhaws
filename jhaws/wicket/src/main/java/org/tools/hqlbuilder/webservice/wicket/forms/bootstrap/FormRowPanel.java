package org.tools.hqlbuilder.webservice.wicket.forms.bootstrap;

import org.apache.wicket.feedback.FeedbackMessage;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.model.IModel;
import org.tools.hqlbuilder.webservice.wicket.forms.common.AbstractFormElementSettings;
import org.tools.hqlbuilder.webservice.wicket.forms.common.FormRowPanelParent;
import org.tools.hqlbuilder.webservice.wicket.forms.common.FormSettings;

@SuppressWarnings("serial")
public abstract class FormRowPanel<P, T, C extends FormComponent<T>, ElementSettings extends AbstractFormElementSettings<ElementSettings>>
		extends FormRowPanelParent<P, T, C, ElementSettings> {
	public FormRowPanel(IModel<?> model, P propertyPath, FormSettings formSettings, ElementSettings componentSettings) {
		super(true, model, propertyPath, formSettings, componentSettings);
	}

	public FormRowPanel(P propertyPath, IModel<T> valueModel, FormSettings formSettings,
			ElementSettings componentSettings) {
		super(true, propertyPath, valueModel, formSettings, componentSettings);
	}

	public String getLabelClass() {
		if (formSettings.getColumns() >= 5) {
			return "col-sm-1";
		}
		return "col-sm-2";
	}

	public String getComponentClass() {
		if (formSettings.getColumns() == 1) {
			return "col-sm-10";
		}
		if (formSettings.getColumns() == 2) {
			return "col-sm-4";
		}
		if (formSettings.getColumns() == 3) {
			return "col-sm-2";
		}
		return "col-sm-1";
	}

	protected String getFeedbackMessageCSSClass(final FeedbackMessage message) {
		switch (message.getLevel()) {
		case FeedbackMessage.DEBUG:
			return "alert alert-info";
		case FeedbackMessage.INFO:
			return "alert alert-info";
		case FeedbackMessage.SUCCESS:
			return "alert alert-success";
		case FeedbackMessage.WARNING:
			return "alert alert-warning";
		case FeedbackMessage.ERROR:
			return "alert alert-danger";
		case FeedbackMessage.FATAL:
			return "alert alert-danger";
		}
		return "alert alert-info";
	}
}
