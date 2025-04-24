package org.jhaws.common.web.wicket.forms.bootstrap;

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
import org.jhaws.common.lambda.LambdaPath;
import org.jhaws.common.web.wicket.forms.common.FormConstants;
import org.jhaws.common.web.wicket.forms.common.FormRowPanelParent;
import org.jhaws.common.web.wicket.forms.common.FormSettings;
import org.jhaws.common.web.wicket.forms.common.TypeAheadTextFieldSettings;
import org.jhaws.common.web.wicket.jquery_typeahead.JqueryTypeAhead;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

// //https://github.com/lipis/flag-icon-css
@SuppressWarnings("serial")
public class TypeAheadTextFieldPanel extends DefaultFormRowPanel<String, TextField<String>, TypeAheadTextFieldSettings> {
    public static String typeAheadChoices(IModel<? extends List<Map<String, String>>> choices) {
        return "[" + choices.getObject().stream().map(map -> "{" + map.entrySet().stream().map(e -> "\"" + e.getKey() + "\":\"" + e.getValue() + "\"").collect(Collectors.joining(",")) + "}").collect(Collectors.joining(",")) + "]";
    }

    protected IModel<? extends List<Map<String, String>>> choices;

    protected Component results;

    public TypeAheadTextFieldPanel(final IModel<?> model, final LambdaPath<?, String> propertyPath, FormSettings formSettings, TypeAheadTextFieldSettings componentSettings, IModel<? extends List<Map<String, String>>> choices) {
        super(model, propertyPath, formSettings, componentSettings);
        this.choices = choices;
    }

    @Override
    public FormRowPanelParent<String, String, TextField<String>, TypeAheadTextFieldSettings> addComponents(TypeAheadTextFieldSettings settings) {
        super.addComponents(settings);
        getComponentContainer(settings).add(results = new WebMarkupContainer("result-container").setOutputMarkupId(true));
        return this;
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

        response.render(CssHeaderItem.forReference(JqueryTypeAhead.CSS));
        response.render(JavaScriptHeaderItem.forReference(JqueryTypeAhead.JS));

        if (!Boolean.TRUE.equals(getComponentSettings().getCustom())) {
            if (StringUtils.isNotBlank(getComponentSettings().getRemote())) {
                if (getComponentSettings().isRemoteFilters()) {
                    String script = new FilePath(TypeAheadTextFieldPanel.class, TypeAheadTextFieldPanel.class.getSimpleName() + "-remote-filter-factory.js").readAll();
                    response.render(OnDomReadyHeaderItem.forScript(replace(script, getComponentSettings())//
                            .replace("$URL$", getComponentSettings().getRemote())//
                            .replace("$QUERY$", getComponentSettings().getQueryParam())//
                    ));
                } else {
                    String script = new FilePath(TypeAheadTextFieldPanel.class, TypeAheadTextFieldPanel.class.getSimpleName() + "-remote-factory.js").readAll();
                    response.render(OnDomReadyHeaderItem.forScript(replace(script, getComponentSettings())//
                            .replace("$URL$", getComponentSettings().getRemote())//
                            .replace("$QUERY$", getComponentSettings().getQueryParam())//
                    ));
                }
            } else if (StringUtils.isNotBlank(getComponentSettings().getLocal())) {
                String script = new FilePath(TypeAheadTextFieldPanel.class, TypeAheadTextFieldPanel.class.getSimpleName() + "-local-factory.js").readAll();
                response.render(OnDomReadyHeaderItem.forScript(replace(script, getComponentSettings()).replace("$OPTIONS$", getComponentSettings().getLocal())));
            } else if (choices != null && choices.getObject() != null && !choices.getObject().isEmpty()) {
                String script = new FilePath(TypeAheadTextFieldPanel.class, TypeAheadTextFieldPanel.class.getSimpleName() + "-local-factory.js").readAll();
                response.render(OnDomReadyHeaderItem.forScript(replace(script, getComponentSettings()).replace("$OPTIONS$", typeAheadChoices(choices))));
            } else {
                throw new IllegalArgumentException();
            }
        }
    }

    protected String replace(String script, TypeAheadTextFieldSettings componentSettings) {
        return script//
                .replace("$ID$", getComponent().getMarkupId())//
                .replace("$RESULTS_ID$", results.getMarkupId())//
                .replace("$TEMPLATE$", getComponentSettings().getTemplate())//
                .replace("$PROPERTIES$", getComponentSettings().getProperties().stream().map(p -> "'" + p + "'").collect(Collectors.joining(",")))//
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
