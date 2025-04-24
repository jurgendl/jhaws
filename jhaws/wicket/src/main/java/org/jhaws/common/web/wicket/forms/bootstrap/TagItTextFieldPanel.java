package org.jhaws.common.web.wicket.forms.bootstrap;

import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;
import org.jhaws.common.io.FilePath;
import org.jhaws.common.lambda.LambdaPath;
import org.jhaws.common.lang.StringUtils;
import org.jhaws.common.web.wicket.forms.common.FormConstants;
import org.jhaws.common.web.wicket.forms.common.FormSettings;
import org.jhaws.common.web.wicket.forms.common.TagItTextFieldSettings;

import java.util.List;

@SuppressWarnings("serial")
public class TagItTextFieldPanel extends DefaultFormRowPanel<String, TextField<String>, TagItTextFieldSettings> {
    public static String tagItChoices(IModel<? extends List<String>> choices) {
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

    public TagItTextFieldPanel(final IModel<?> model, final LambdaPath<?, String> propertyPath, FormSettings formSettings, TagItTextFieldSettings componentSettings, IModel<? extends List<String>> choices) {
        super(model, propertyPath, formSettings, componentSettings);
        this.choices = choices;
    }

    @Override
    protected TextField<String> createComponent(IModel<String> model, Class<String> valueType) {
        TextField<String> textField = new TextField<String>(FormConstants.VALUE, model, valueType) {
            @Override
            protected void onComponentTag(ComponentTag tag) {
                super.onComponentTag(tag);
                TagItTextFieldPanel.this.onFormComponentTag(tag);
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

        // response.render(
        // JavaScriptHeaderItem.forReference(org.tools.hqlbuilder.webservice.jquery.ui.typeahead.TypeAhead.JS));
        // response.render(JavaScriptHeaderItem
        // .forReference(org.tools.hqlbuilder.webservice.bootstrap4.typeahead.TypeAhead.JS));
        // response.render(
        // JavaScriptHeaderItem.forReference(org.tools.hqlbuilder.webservice.bootstrap4.typeahead.TypeAhead.JS));

        // response.render(CssHeaderItem.forReference(BootstrapTags.CSS));
        // response.render(JavaScriptHeaderItem.forReference(BootstrapTags.JS));

        if (StringUtils.isNotBlank(getComponentSettings().getRemote())) {
            String script = new FilePath(TagItTextFieldPanel.class, "TagItTextFieldPanel-remote-factory.js").readAll();
            response.render(OnDomReadyHeaderItem.forScript(replace(script, getComponentSettings()).replace("$URL$", getComponentSettings().getRemote())));
        } else if (StringUtils.isNotBlank(getComponentSettings().getLocal())) {
            String script = new FilePath(TagItTextFieldPanel.class, "TagItTextFieldPanel-local-factory.js").readAll();
            response.render(OnDomReadyHeaderItem.forScript(replace(script, getComponentSettings()).replace("$OPTIONS$", getComponentSettings().getLocal())));
        } else if (choices != null && choices.getObject() != null && !choices.getObject().isEmpty()) {
            String script = new FilePath(TagItTextFieldPanel.class, "TagItTextFieldPanel-local-factory.js").readAll();
            response.render(OnDomReadyHeaderItem.forScript(replace(script, getComponentSettings()).replace("$OPTIONS$", tagItChoices(choices))));
        } else {
            String script = new FilePath(TagItTextFieldPanel.class, "TagItTextFieldPanel-factory.js").readAll();
            response.render(OnDomReadyHeaderItem.forScript(replace(script, getComponentSettings())));
        }

        if (getComponentSettings().isReadOnly()) {
            response.render(OnDomReadyHeaderItem.forScript(";$('#" + getComponent().getMarkupId() + "').on('beforeItemAdd',function(event){event.cancel=true;}).on('beforeItemRemove',function(event){event.cancel=true;});"));
        }
    }

    protected String replace(String script, TagItTextFieldSettings componentSettings) {
        return script//
                .replace("$ID$", getComponent().getMarkupId())//
                .replace("$DELAY$", String.valueOf(getComponentSettings().getDelay()))//
                .replace("$MIN$", String.valueOf(getComponentSettings().getMinLength()))//
                .replace("$FREE$", String.valueOf(getComponentSettings().isFree()))//
                .replace("$DELIMITER$", String.valueOf(getComponentSettings().getFieldDelimiter()))//
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
