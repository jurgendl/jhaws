package org.tools.hqlbuilder.webservice.wicket.forms.bootstrap;

import java.util.List;

import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;
import org.jhaws.common.io.FilePath;
import org.jhaws.common.lang.StringUtils;
import org.tools.hqlbuilder.webservice.wicket.forms.common.FormConstants;
import org.tools.hqlbuilder.webservice.wicket.forms.common.FormSettings;
import org.tools.hqlbuilder.webservice.wicket.forms.common.TypeAheadTextFieldSettings;

// https://www.jqueryscript.net/form/jQuery-Bootstrap-4-Typeahead-Plugin.html
@SuppressWarnings("serial")
public class TypeAheadTextFieldPanel
		extends DefaultFormRowPanel<String, TextField<String>, TypeAheadTextFieldSettings> {
	public static String typeAheadChoices(IModel<? extends List<String>> choices) {
		StringBuilder availableTags = new StringBuilder("[");
		if (!choices.getObject().isEmpty()) {
			for (String choice : choices.getObject()) {
				availableTags.append("\"").append(choice).append("\"").append(',');
			}
			availableTags.deleteCharAt(availableTags.length() - 1);
		}
		availableTags.append("]");
		return availableTags.toString();
	}

	protected IModel<? extends List<String>> choices;

	public TypeAheadTextFieldPanel(final IModel<?> model, final String propertyPath, FormSettings formSettings,
			TypeAheadTextFieldSettings componentSettings, IModel<? extends List<String>> choices) {
		super(model, propertyPath, formSettings, componentSettings);
		this.choices = choices;
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

		response.render(
				JavaScriptHeaderItem.forReference(org.tools.hqlbuilder.webservice.jquery.ui.typeahead.TypeAhead.JS));
		response.render(JavaScriptHeaderItem
				.forReference(org.tools.hqlbuilder.webservice.jquery.ui.typeahead.TypeAhead.JS_BLOODHOUND));

		if (StringUtils.isNotBlank(getComponentSettings().getRemote())) {
			String script = new FilePath(TagItTextFieldPanel.class, "TypeAheadTextFieldPanel-remote-factory.js")
					.readAll();
			response.render(OnDomReadyHeaderItem.forScript(
					replace(script, getComponentSettings()).replace("$URL$", getComponentSettings().getRemote())));
		} else if (StringUtils.isNotBlank(getComponentSettings().getLocal())) {
			String script = new FilePath(TagItTextFieldPanel.class, "TypeAheadTextFieldPanel-local-factory.js")
					.readAll();
			response.render(OnDomReadyHeaderItem.forScript(
					replace(script, getComponentSettings()).replace("$OPTIONS$", getComponentSettings().getLocal())));
		} else if (choices != null && choices.getObject() != null && !choices.getObject().isEmpty()) {
			String script = new FilePath(TagItTextFieldPanel.class, "TypeAheadTextFieldPanel-local-factory.js")
					.readAll();
			response.render(OnDomReadyHeaderItem.forScript(
					replace(script, getComponentSettings()).replace("$OPTIONS$", typeAheadChoices(choices))));
		} else {
			throw new IllegalArgumentException();
		}

//		if (getComponentSettings().isReadOnly()) {
//			response.render(OnDomReadyHeaderItem.forScript(";$('#" + getComponent().getMarkupId()
//					+ "').on('beforeItemAdd',function(event){event.cancel=true;}).on('beforeItemRemove',function(event){event.cancel=true;});"));
//		}
	}

	protected String replace(String script, TypeAheadTextFieldSettings componentSettings) {
		return script//
				.replace("$ID$", getComponent().getMarkupId())//
				.replace("$DELAY$", String.valueOf(getComponentSettings().getDelay()))//
				.replace("$MIN$", String.valueOf(getComponentSettings().getMinLength()))//
				.replace("$FREE$", String.valueOf(getComponentSettings().isFree()))//
				.replace("$MAX$", String.valueOf(getComponentSettings().getMax()))//
		;
	}

	public IModel<? extends List<String>> getChoices() {
		return this.choices;
	}

	public void setChoices(IModel<? extends List<String>> choices) {
		this.choices = choices;
	}
}
