package org.tools.hqlbuilder.webservice.wicket.components;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.wicket.Session;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.head.OnLoadHeaderItem;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.util.ListModel;
import org.tools.hqlbuilder.webservice.jquery.ui.primeui.PrimeUI;
import org.tools.hqlbuilder.webservice.wicket.JavaScriptResourceReference;

public class LocalesDropDown extends DropDownChoice<Locale> {
    private static final long serialVersionUID = 7959562572371996732L;

    public static JavaScriptResourceReference FLAGS_CSS = new JavaScriptResourceReference(LocalesPanel.class, "flags/flags.css");

    public static class LocaleComparator implements Comparator<Locale> {
        @Override
        public int compare(Locale o1, Locale o2) {
            return new CompareToBuilder()//
                    .append(o1.getDisplayLanguage(o1).toLowerCase(), o2.getDisplayLanguage(o2).toLowerCase())//
                    .append(o1.getDisplayCountry(o1).toLowerCase(), o2.getDisplayCountry(o2).toLowerCase())//
                    .append(o1.getDisplayVariant(o1).toLowerCase(), o2.getDisplayVariant(o2).toLowerCase())//
                    .append(o1.getDisplayScript(o1).toLowerCase(), o2.getDisplayScript(o2).toLowerCase())//
                    .toComparison();//
        }
    }

    public LocalesDropDown(String id, IModel<List<Locale>> optionsModel, IModel<Locale> valueModel, IChoiceRenderer<Locale> renderer) {
        super(id, forDefault(valueModel), forChoicesDefault(optionsModel), forDefault(renderer));
    }

    public static IChoiceRenderer<Locale> forDefault(IChoiceRenderer<Locale> renderer) {
        if (renderer == null) {
            renderer = new IChoiceRenderer<Locale>() {
                private static final long serialVersionUID = -8111346182386696927L;

                @Override
                public String getIdValue(Locale object, int index) {
                    return getId(object);
                }

                @Override
                public Object getDisplayValue(Locale locale) {
                    return getLabel(locale);
                }
            };
        }
        return renderer;
    }

    public static IModel<List<Locale>> forChoicesDefault(IModel<List<Locale>> optionsModel) {
        if (optionsModel == null) {
            optionsModel = new ListModel<Locale>(getAvailableLocales());
        }
        return optionsModel;
    }

    public static IModel<Locale> forDefault(IModel<Locale> valueModel) {
        if (valueModel == null) {
            valueModel = new IModel<Locale>() {
                private static final long serialVersionUID = -676290942608903019L;

                @Override
                public void detach() {
                    //
                }

                @Override
                public Locale getObject() {
                    return Session.get().getLocale();
                }

                @Override
                public void setObject(Locale locale) {
                    Session.get().setLocale(locale);
                }
            };
        }
        return valueModel;
    }

    public static String getLabel(Locale object) {
        return object.getDisplayLanguage(object) + (StringUtils.isBlank(object.getCountry()) ? "" : ", " + object.getDisplayCountry(object))
                + (StringUtils.isBlank(object.getVariant()) ? "" : ", " + object.getDisplayVariant(object))
                + (StringUtils.isBlank(object.getScript()) ? "" : ", " + object.getDisplayScript(object));
    }

    public static String getId(Locale object) {
        return object.toString();
    }

    public static List<Locale> getAvailableLocales() {
        List<Locale> locales = new ArrayList<Locale>(Arrays.asList(Locale.getAvailableLocales()));
        for (Locale locale : locales.toArray(new Locale[locales.size()])) {
            if (StringUtils.isBlank(locale.getLanguage())) {
                locales.remove(locale);
            }
        }
        Collections.sort(locales, new LocaleComparator());
        return locales;
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        if (!isEnabledInHierarchy()) {
            return;
        }
        response.render(JavaScriptHeaderItem.forReference(PrimeUI.PRIME_UI_JS));
        response.render(CssHeaderItem.forReference(FLAGS_CSS));
        {
            StringBuilder script = new StringBuilder("var options_").append(getMarkupId()).append(" = new Array(\n");
            boolean add = false;
            for (Locale locale : getChoices()) {
                if (add) {
                    script.append(",");
                } else {
                    add = true;
                }
                script.append("{ label: '").append(getLabel(locale)).append("' , value: '").append(getId(locale)).append("' , country: '")
                        .append(locale.getCountry().toLowerCase()).append("' }");
                script.append("\n");
            }
            script.append(");\n");
            response.render(JavaScriptHeaderItem.forScript(script.toString(), "js_options_" + getMarkupId()));
        }
        {
            StringBuilder script = new StringBuilder();
            script.append("$('#").append(getMarkupId())
            .append("').text('').puidropdown({ width: 'auto', filter: true, filterMatchMode: 'contains', data: options_")
                    .append(getMarkupId())
                    .append(", content: function(option) { return '<img class=\"flag flag-' + option['country'] + '\"/> ' + option['label']; } ")
                    .append("}).puidropdown('selectValue', '" + getId(getModel().getObject()) + "');\n");
            response.render(OnLoadHeaderItem.forScript(script.toString()));
        }
    }
}
