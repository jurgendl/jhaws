package org.tools.hqlbuilder.webservice.wicket.forms.bootstrap;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.tools.hqlbuilder.webservice.jquery.ui.moment.MomentJs;
import org.tools.hqlbuilder.webservice.wicket.converter.Converter;
import org.tools.hqlbuilder.webservice.wicket.converter.ModelConverter;
import org.tools.hqlbuilder.webservice.wicket.forms.common.AbstractFormElementSettings;
import org.tools.hqlbuilder.webservice.wicket.forms.common.DatePickerSettings;
import org.tools.hqlbuilder.webservice.wicket.forms.common.FormRowPanelParent;
import org.tools.hqlbuilder.webservice.wicket.forms.common.FormSettings;

import com.googlecode.wicket.jquery.core.utils.LocaleUtils;

import de.agilecoders.wicket.core.markup.html.bootstrap.behavior.CssClassNameAppender;
import de.agilecoders.wicket.core.markup.html.bootstrap.behavior.CssClassNameRemover;

@SuppressWarnings({ "unchecked", "rawtypes", "serial" })
public class DatePickerPanel<X extends Serializable> extends DefaultFormRowPanel {
    protected String dateFormat;

    protected Converter<X, Date> dateConverter;

    public DatePickerPanel(IModel<?> model, Date propertyPath, FormSettings formSettings, DatePickerSettings componentSettings) {
        super(model, propertyPath, formSettings, componentSettings);
        this.dateConverter = null;
    }

    public DatePickerPanel(IModel<?> model, X propertyPath, Converter<X, Date> dateConverter, FormSettings formSettings,
            DatePickerSettings componentSettings) {
        super(model, propertyPath, formSettings, componentSettings);
        this.dateConverter = dateConverter;
    }

    @Override
    public IModel<Date> getValueModel() {
        if (dateConverter == null) {
            return super.getValueModel();
        }
        IModel<X> backingModel = new PropertyModel<>(getDefaultModel(), getPropertyName());
        return new ModelConverter<>(backingModel, dateConverter);
    }

    @Override
    protected FormComponent createComponent(IModel model, Class valueType) {
        Map<String, String> dateFormats = MomentJs.dateFormats(getLocale());
        dateFormat = dateFormats.get("L");
        if (DatePickerSettings.class.cast(getComponentSettings()).getType() != null) {
            switch (DatePickerSettings.class.cast(getComponentSettings()).getType()) {
                case datetime:
                    dateFormat = dateFormats.get("L") + " " + dateFormats.get("LT");
                    break;
                case time:
                    dateFormat = dateFormats.get("LT");
                    break;
                default:
                case date:
                    break;
            }
        }
        org.apache.wicket.extensions.markup.html.form.DateTextField textField = new org.apache.wicket.extensions.markup.html.form.DateTextField(VALUE,
                model, dateFormat) {
            @Override
            protected void onComponentTag(ComponentTag tag) {
                super.onComponentTag(tag);
                onFormComponentTag(tag);
            }
        };
        return textField;
    }

    @Override
    public FormRowPanelParent addComponents(AbstractFormElementSettings settings) {
        this.add(this.getLabel(settings));
        WebMarkupContainer tempusdominuspicker = new WebMarkupContainer("tempusdominuspicker");
        if (DatePickerSettings.class.cast(getComponentSettings()).getType() != null) {
            switch (DatePickerSettings.class.cast(getComponentSettings()).getType()) {
                case datetime:
                    tempusdominuspicker.add(new CssClassNameRemover("tempusdominusdate")); // remove default
                    tempusdominuspicker.add(new CssClassNameAppender("tempusdominusdatetime")); // add correct one
                    break;
                case time:
                    tempusdominuspicker.add(new CssClassNameRemover("tempusdominusdate")); // remove default
                    tempusdominuspicker.add(new CssClassNameAppender("tempusdominustime")); // add correct one
                    break;
                default:
                case date:
                    // tempusdominusdate is default, css class is ok
                    break;
            }
        }
        WebMarkupContainer tempusdominuspickergroup = new WebMarkupContainer("tempusdominuspickergroup");
        tempusdominuspicker.add(tempusdominuspickergroup);
        FormComponent _component = getComponent();
        tempusdominuspicker.add(_component);
        getComponentContainer(settings).add(tempusdominuspicker);
        getComponentContainer(settings).add(getRequiredMarker());
        getComponentContainer(settings).add(getFeedback());
        this.add(getComponentContainer(settings));
        tempusdominuspickergroup.add(new AttributeModifier("data-target", "#" + tempusdominuspicker.getMarkupId()));
        _component.add(new AttributeModifier("data-target", "#" + tempusdominuspicker.getMarkupId()));
        WebMarkupContainer tempusdominuspickericon = new WebMarkupContainer("tempusdominuspickericon");
        tempusdominuspickergroup.add(tempusdominuspickericon);
        return this;
    }

    public static String dateformat(Locale locale) {
        return LocaleUtils.getLocaleDatePattern(locale, DateFormat.SHORT);
    }

    @Override
    protected void setupPlaceholder(ComponentTag tag) {
        if (this.componentSettings.isShowPlaceholder()) {
            tag.getAttributes().put(PLACEHOLDER, "<" + new SimpleDateFormat(dateFormat, getLocale()).format(new Date()) + ">");
        }
    }

    // @Override
    // public void renderHead(IHeaderResponse response) {
    // super.renderHead(response);
    // if (!isEnabledInHierarchy()) {
    // return;
    // }
    // response.render(CssHeaderItem.forReference(BootstrapTempusDominusDateTimePicker.CSS));
    // response.render(JavaScriptHeaderItem.forReference(BootstrapTempusDominusDateTimePicker.JS));
    // response.render(BootstrapTempusDominusDateTimePicker.FACTORY);
    // }
}
