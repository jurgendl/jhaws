package org.tools.hqlbuilder.webservice.wicket.forms.bootstrap;

import java.util.List;

import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;
import org.jhaws.common.io.FilePath;
import org.jhaws.common.lang.StringUtils;
import org.tools.hqlbuilder.webservice.bootstrap4.tags.BootstrapTags;
import org.tools.hqlbuilder.webservice.wicket.forms.common.FormConstants;
import org.tools.hqlbuilder.webservice.wicket.forms.common.FormSettings;
import org.tools.hqlbuilder.webservice.wicket.forms.common.TagItTextFieldSettings;

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

    public TagItTextFieldPanel(final IModel<?> model, final String propertyPath, FormSettings formSettings, TagItTextFieldSettings componentSettings,
            IModel<? extends List<String>> choices) {
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
        //
        response.render(JavaScriptHeaderItem.forReference(org.tools.hqlbuilder.webservice.jquery.ui.typeahead.TypeAhead.JS));
        response.render(JavaScriptHeaderItem.forReference(org.tools.hqlbuilder.webservice.jquery.ui.typeahead.TypeAhead.JS_BLOODHOUND));
        response.render(JavaScriptHeaderItem.forReference(org.tools.hqlbuilder.webservice.bootstrap4.typeahead.TypeAhead.JS));
        //
        response.render(CssHeaderItem.forReference(BootstrapTags.CSS));
        response.render(JavaScriptHeaderItem.forReference(BootstrapTags.JS));
        //
        if (StringUtils.isNotBlank(getComponentSettings().getRemote())) {
            response.render(OnDomReadyHeaderItem.forScript(new FilePath(TagItTextFieldPanel.class, "TagItTextFieldPanel-remote-factory.js").readAll()//
                    .replace("$URL$", getComponentSettings().getRemote())//
                    .replace("$ID$", getComponent().getMarkupId())//
                    .replace("$DELAY$", String.valueOf(getComponentSettings().getDelay()))//
                    .replace("$MIN$", String.valueOf(getComponentSettings().getMinLength()))//
                    .replace("$FREE$", String.valueOf(getComponentSettings().isFree()))//
                    .replace("$DELIMITER$", String.valueOf(getComponentSettings().getFieldDelimiter()))//
            ));
        } else if (StringUtils.isNotBlank(getComponentSettings().getLocal())) {
            response.render(OnDomReadyHeaderItem.forScript(new FilePath(TagItTextFieldPanel.class, "TagItTextFieldPanel-local-factory.js").readAll()//
                    .replace("$OPTIONS$", getComponentSettings().getLocal())//
                    .replace("$ID$", getComponent().getMarkupId())//
                    .replace("$DELAY$", String.valueOf(getComponentSettings().getDelay()))//
                    .replace("$MIN$", String.valueOf(getComponentSettings().getMinLength()))//
                    .replace("$FREE$", String.valueOf(getComponentSettings().isFree()))//
                    .replace("$DELIMITER$", String.valueOf(getComponentSettings().getFieldDelimiter()))//
            ));
        } else if (choices != null && choices.getObject() != null && !choices.getObject().isEmpty()) {
            response.render(OnDomReadyHeaderItem.forScript(new FilePath(TagItTextFieldPanel.class, "TagItTextFieldPanel-local-factory.js").readAll()//
                    .replace("$OPTIONS$", tagItChoices(choices))//
                    .replace("$ID$", getComponent().getMarkupId())//
                    .replace("$DELAY$", String.valueOf(getComponentSettings().getDelay()))//
                    .replace("$MIN$", String.valueOf(getComponentSettings().getMinLength()))//
                    .replace("$FREE$", String.valueOf(getComponentSettings().isFree()))//
                    .replace("$DELIMITER$", String.valueOf(getComponentSettings().getFieldDelimiter()))//
            ));
        } else {
            response.render(OnDomReadyHeaderItem.forScript(new FilePath(TagItTextFieldPanel.class, "TagItTextFieldPanel-factory.js").readAll()//
                    .replace("$ID$", getComponent().getMarkupId())//
                    .replace("$FREE$", String.valueOf(getComponentSettings().isFree()))//
                    .replace("$DELIMITER$", String.valueOf(getComponentSettings().getFieldDelimiter()))//
            ));
        }
        if (getComponentSettings().isReadOnly()) {
            response.render(OnDomReadyHeaderItem.forScript(";$('#" + getComponent().getMarkupId()
                    + "').on('beforeItemAdd',function(event){event.cancel=true;}).on('beforeItemRemove',function(event){event.cancel=true;});"));
        }
    }
}
