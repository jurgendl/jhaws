package org.tools.hqlbuilder.webservice.wicket.forms.bootstrap;

import static org.tools.hqlbuilder.webservice.wicket.WebHelper.tag;

import java.io.Serializable;

import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.model.IModel;
import org.tools.hqlbuilder.webservice.wicket.forms.common.FormSettings;
import org.tools.hqlbuilder.webservice.wicket.forms.common.TextAreaSettings;

@SuppressWarnings("serial")
public class TextAreaPanel<T extends Serializable> extends DefaultFormRowPanel<T, TextArea<T>, TextAreaSettings> {
	public TextAreaPanel(final IModel<?> model, final T propertyPath, FormSettings formSettings,
			TextAreaSettings textAreaSettings) {
		super(model, propertyPath, formSettings, textAreaSettings);
	}

	@Override
	protected TextArea<T> createComponent(IModel<T> model, Class<T> valueType) {
		TextArea<T> textArea = new TextArea<T>(VALUE, model) {
			@Override
			protected void onComponentTag(ComponentTag tag) {
				super.onComponentTag(tag);
				tag(tag, "cols", getComponentSettings().getCols());
				tag(tag, "rows", getComponentSettings().getRows());
				/*
				 * http://brett.batie.com/website_development/no-resize-textarea-in-chrome-
				 * safari/
				 */
				if (!getComponentSettings().isResizable()) {
					tag(tag, "style", "resize: none; height: auto;");
				}
				onFormComponentTag(tag);
			}
		};
		return textArea;
	}
}
