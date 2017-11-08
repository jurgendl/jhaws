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
    protected String tagItChoices(IModel<List<String>> choices) {
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

    protected IModel<List<String>> choices;

    public TagItTextFieldPanel(final IModel<?> model, final String propertyPath, FormSettings formSettings, TagItTextFieldSettings componentSettings,
            IModel<List<String>> choices) {
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
            ));
        } else {
            response.render(OnDomReadyHeaderItem.forScript(new FilePath(TagItTextFieldPanel.class, "TagItTextFieldPanel-local-factory.js").readAll()//
                    .replace("$OPTIONS$", tagItChoices(choices))//
                    .replace("$ID$", getComponent().getMarkupId())//
            ));
        }
    }
}
