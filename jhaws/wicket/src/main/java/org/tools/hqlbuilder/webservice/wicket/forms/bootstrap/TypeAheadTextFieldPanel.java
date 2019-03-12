package org.tools.hqlbuilder.webservice.wicket.forms.bootstrap;

import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;
import org.tools.hqlbuilder.webservice.wicket.forms.common.FormConstants;
import org.tools.hqlbuilder.webservice.wicket.forms.common.FormSettings;
import org.tools.hqlbuilder.webservice.wicket.forms.common.TypeAheadTextFieldSettings;

@SuppressWarnings("serial")
public class TypeAheadTextFieldPanel
		extends DefaultFormRowPanel<String, TextField<String>, TypeAheadTextFieldSettings> {
	public TypeAheadTextFieldPanel(final IModel<?> model, final String propertyPath, FormSettings formSettings,
			TypeAheadTextFieldSettings componentSettings) {
		super(model, propertyPath, formSettings, componentSettings);
	}

	@Override
	protected TextField<String> createComponent(IModel<String> model, Class<String> valueType) {
		TextField<String> textField = new TextField<String>(FormConstants.VALUE, model, valueType) {
			@Override
			protected void onComponentTag(ComponentTag tag) {
				super.onComponentTag(tag);
				TypeAheadTextFieldPanel.this.onFormComponentTag(tag);
			}
		};
		return textField;
	}

	@Override
	public void renderHead(IHeaderResponse response) {
		super.renderHead(response);
		if (!this.isEnabledInHierarchy()) {
			return;
		}
		//
		response.render(
				JavaScriptHeaderItem.forReference(org.tools.hqlbuilder.webservice.jquery.ui.typeahead.TypeAhead.JS));
		response.render(JavaScriptHeaderItem
				.forReference(org.tools.hqlbuilder.webservice.jquery.ui.typeahead.TypeAhead.JS_BLOODHOUND));
	}
}
