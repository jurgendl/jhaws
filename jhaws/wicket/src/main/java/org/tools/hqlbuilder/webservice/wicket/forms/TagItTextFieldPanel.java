package org.tools.hqlbuilder.webservice.wicket.forms;

import java.util.List;

import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;
import org.tools.hqlbuilder.webservice.jquery.ui.tagit.TagIt;

public class TagItTextFieldPanel extends DefaultFormRowPanel<String, TextField<String>, TagItTextFieldSettings> {
    public static String tagIt(String id, TagItTextFieldSettings tagItTextFieldSettings, IModel<List<String>> choices) {
        return TagItTextFieldPanel.tagIt(id, tagItTextFieldSettings, TagItTextFieldPanel.tagItChoices(choices));
    }

    public static String tagIt(String id, TagItTextFieldSettings tagItTextFieldSettings, String choices) {
        return (";$('#" + id + "').tagIt(" + choices + ", " + tagItTextFieldSettings.getDelay() + ", " + tagItTextFieldSettings.getMinLength() + ", "
                + tagItTextFieldSettings.isAllowSpaces() + ", " + tagItTextFieldSettings.isCaseSensitive() + ", "
                + tagItTextFieldSettings.isSingleField() + ", '" + tagItTextFieldSettings.getFieldDelimiter() + "');");
        // return (";$('#" + id + "').hide().tagit({"//
        // + "autocomplete:"//
        // + "{\n"//
        // + "source:function(request,response){\n"//
        // + "var availableTags=" + choices + ";\n"//
        // + "var matcherStr=request.term.replace(new RegExp('\\\\*', 'g'), 'A1B2C3');\n"//
        // + "matcherStr=matcherStr.replace(/\\W/g, '');\n"//
        // + "matcherStr=$.ui.autocomplete.escapeRegex(matcherStr);\n"//
        // + "matcherStr=matcherStr.replace(new RegExp('A1B2C3', 'g'), '.*');\n" //
        // + "console.log('matcherStr='+matcherStr);\n"//
        // + "var matcher=new RegExp(matcherStr,'i');\n"//
        // + "response($.grep(availableTags,function(item){\n"//
        // + "return matcher.test(item.replace(/\\W/g, ''));\n"//
        // + "}));\n"//
        // + "}\n"//
        // + ",delay:" + tagItTextFieldSettings.getDelay()//
        // + ",minLength:" + tagItTextFieldSettings.getMinLength()//
        // + "}"//
        // + ",allowSpaces:" + tagItTextFieldSettings.isAllowSpaces()//
        // + ",caseSensitive:" + tagItTextFieldSettings.isCaseSensitive()//
        // + ",allowDuplicates:false"//
        // // + ",availableTags:" + availableTags //
        // + ",singleField:" + tagItTextFieldSettings.isSingleField()//
        // + ",singleFieldDelimiter:'" + tagItTextFieldSettings.getFieldDelimiter() + "'"//
        // + "})._hoverable()._focusable();");//
    }

    public static String tagItChoices(IModel<List<String>> choices) {
        StringBuilder availableTags = new StringBuilder("[");
        for (String choice : choices.getObject()) {
            availableTags.append("\"").append(choice).append("\"").append(',');
        }
        availableTags.deleteCharAt(availableTags.length() - 1).append("]");
        return availableTags.toString();
    }

    private static final long serialVersionUID = -3317709333874063112L;

    protected IModel<List<String>> choices;

    public TagItTextFieldPanel(final IModel<?> model, final String propertyPath, FormSettings formSettings, TagItTextFieldSettings componentSettings,
            IModel<List<String>> choices) {
        super(model, propertyPath, formSettings, componentSettings);
        this.choices = choices;
    }

    @Override
    protected TextField<String> createComponent(IModel<String> model, Class<String> valueType) {
        TextField<String> textField = new TextField<String>(FormConstants.VALUE, model, valueType) {
            private static final long serialVersionUID = 3940638846568679297L;

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
        response.render(JavaScriptHeaderItem.forReference(TagIt.TAG_IT_FACTORY_JS));
        response.render(CssHeaderItem.forReference(TagIt.TAG_IT_CSS));
        response.render(CssHeaderItem.forReference(TagIt.TAG_IT_ZEN_CSS));
        response.render(OnDomReadyHeaderItem.forScript(TagItTextFieldPanel.tagIt(this.getComponent().getMarkupId(), this.getComponentSettings(),
                this.choices)));
    }
}
