package org.jhaws.common.web.wicket.forms.bootstrap;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.wicket.Component;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;
import org.jhaws.common.io.FilePath;
import org.jhaws.common.web.wicket.forms.common.FormConstants;
import org.jhaws.common.web.wicket.forms.common.FormRowPanelParent;
import org.jhaws.common.web.wicket.forms.common.FormSettings;
import org.jhaws.common.web.wicket.forms.common.TypeAheadTextFieldAltSettings;
import org.tools.hqlbuilder.webservice.jquery.ui.jquery_typeahead.JqueryTypeAhead;

// //https://github.com/lipis/flag-icon-css
@SuppressWarnings("serial")
public class TypeAheadTextFieldAltPanel
		extends DefaultFormRowPanel<String, TextField<String>, TypeAheadTextFieldAltSettings> {
	public static String typeAheadChoices(IModel<? extends List<Map<String, String>>> choices) {
		return "[" + choices.getObject().stream().map(map -> "{" + map.entrySet().stream()
				.map(e -> "\"" + e.getKey() + "\":\"" + e.getValue() + "\"").collect(Collectors.joining(",")) + "}")
				.collect(Collectors.joining(",")) + "]";
	}

	protected IModel<? extends List<Map<String, String>>> choices;

	protected Component results;

	public TypeAheadTextFieldAltPanel(final IModel<?> model, final String propertyPath, FormSettings formSettings,
			TypeAheadTextFieldAltSettings componentSettings, IModel<? extends List<Map<String, String>>> choices) {
		super(model, propertyPath, formSettings, componentSettings);
		this.choices = choices;
	}

	@Override
	public FormRowPanelParent<String, String, TextField<String>, TypeAheadTextFieldAltSettings> addComponents(
			TypeAheadTextFieldAltSettings settings) {
		super.addComponents(settings);
		getComponentContainer(settings)
				.add(results = new WebMarkupContainer("result-container").setOutputMarkupId(true));
		return this;
	}

	@Override
	protected TextField<String> createComponent(IModel<String> model, Class<String> valueType) {
		TextField<String> textField = new TextField<String>(FormConstants.VALUE, model, valueType) {
			@Override
			protected void onComponentTag(ComponentTag tag) {
				super.onComponentTag(tag);
				TypeAheadTextFieldAltPanel.this.onFormComponentTag(tag);
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

		response.render(CssHeaderItem.forReference(JqueryTypeAhead.CSS));
		response.render(JavaScriptHeaderItem.forReference(JqueryTypeAhead.JS));

		if (!Boolean.TRUE.equals(getComponentSettings().getCustom())) {
			if (StringUtils.isNotBlank(getComponentSettings().getRemote())) {
				if (getComponentSettings().isRemoteFilters()) {
					String script = new FilePath(TypeAheadTextFieldAltPanel.class,
							TypeAheadTextFieldAltPanel.class.getSimpleName() + "-remote-filter-factory.js").readAll();
					response.render(OnDomReadyHeaderItem.forScript(replace(script, getComponentSettings())//
							.replace("$URL$", getComponentSettings().getRemote())//
							.replace("$QUERY$", getComponentSettings().getQueryParam())//
					));
				} else {
					String script = new FilePath(TypeAheadTextFieldAltPanel.class,
							TypeAheadTextFieldAltPanel.class.getSimpleName() + "-remote-factory.js").readAll();
					response.render(OnDomReadyHeaderItem.forScript(replace(script, getComponentSettings())//
							.replace("$URL$", getComponentSettings().getRemote())//
							.replace("$QUERY$", getComponentSettings().getQueryParam())//
					));
				}
			} else if (StringUtils.isNotBlank(getComponentSettings().getLocal())) {
				String script = new FilePath(TypeAheadTextFieldAltPanel.class,
						TypeAheadTextFieldAltPanel.class.getSimpleName() + "-local-factory.js").readAll();
				response.render(OnDomReadyHeaderItem.forScript(replace(script, getComponentSettings())
						.replace("$OPTIONS$", getComponentSettings().getLocal())));
			} else if (choices != null && choices.getObject() != null && !choices.getObject().isEmpty()) {
				String script = new FilePath(TypeAheadTextFieldAltPanel.class,
						TypeAheadTextFieldAltPanel.class.getSimpleName() + "-local-factory.js").readAll();
				response.render(OnDomReadyHeaderItem.forScript(
						replace(script, getComponentSettings()).replace("$OPTIONS$", typeAheadChoices(choices))));
			} else {
				throw new IllegalArgumentException();
			}
		}
	}

	protected String replace(String script, TypeAheadTextFieldAltSettings componentSettings) {
		return script//
				.replace("$ID$", getComponent().getMarkupId())//
				.replace("$RESULTS_ID$", results.getMarkupId())//
				.replace("$TEMPLATE$", getComponentSettings().getTemplate())//
				.replace("$PROPERTIES$",
						getComponentSettings().getProperties().stream().map(p -> "'" + p + "'")
								.collect(Collectors.joining(",")))//
				.replace("$PROPERTY$", getComponentSettings().getProperties().get(0))//
				.replace("$DELAY$", String.valueOf(getComponentSettings().getDelay()))//
				.replace("$MIN$", String.valueOf(getComponentSettings().getMinLength()))//
				.replace("$FREE$", String.valueOf(getComponentSettings().isFree()))//
				.replace("$MAX$", String.valueOf(getComponentSettings().getMax()))//
		;
	}

	public IModel<? extends List<Map<String, String>>> getChoices() {
		return this.choices;
	}

	public void setChoices(IModel<? extends List<Map<String, String>>> choices) {
		this.choices = choices;
	}
}
