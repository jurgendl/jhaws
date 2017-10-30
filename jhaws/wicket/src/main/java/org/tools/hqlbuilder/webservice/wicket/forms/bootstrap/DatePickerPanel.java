package org.tools.hqlbuilder.webservice.wicket.forms.bootstrap;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.tools.hqlbuilder.webservice.wicket.converter.Converter;
import org.tools.hqlbuilder.webservice.wicket.converter.ModelConverter;
import org.tools.hqlbuilder.webservice.wicket.forms.common.AbstractFormElementSettings;
import org.tools.hqlbuilder.webservice.wicket.forms.common.FormRowPanelParent;
import org.tools.hqlbuilder.webservice.wicket.forms.common.FormSettings;

import com.googlecode.wicket.jquery.core.utils.LocaleUtils;

@SuppressWarnings({ "unchecked", "rawtypes", "serial" })
public class DatePickerPanel<X extends Serializable> extends DefaultFormRowPanel {
    protected String dateFormat;

    protected Converter<X, Date> dateConverter;

    public DatePickerPanel(IModel<?> model, Date propertyPath, FormSettings formSettings, AbstractFormElementSettings componentSettings) {
        super(model, propertyPath, formSettings, componentSettings);
        this.dateConverter = null;
    }

    public DatePickerPanel(IModel<?> model, X propertyPath, Converter<X, Date> dateConverter, FormSettings formSettings,
            AbstractFormElementSettings componentSettings) {
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
        Locale locale = getLocale();
        dateFormat = dateformat(locale);
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
    public FormRowPanelParent addComponents() {
        this.add(this.getLabel());
        WebMarkupContainer tempusdominuspicker = new WebMarkupContainer("tempusdominuspicker");
        WebMarkupContainer tempusdominuspickergroup = new WebMarkupContainer("tempusdominuspickergroup");
        tempusdominuspicker.add(tempusdominuspickergroup);
        FormComponent _component = getComponent();
        tempusdominuspicker.add(_component);
        getComponentContainer().add(tempusdominuspicker);
        getComponentContainer().add(getRequiredMarker());
        getComponentContainer().add(getFeedback());
        this.add(getComponentContainer());
        tempusdominuspickergroup.add(new AttributeModifier("data-target", "#" + tempusdominuspicker.getMarkupId()));
        _component.add(new AttributeModifier("data-target", "#" + tempusdominuspicker.getMarkupId()));
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
}
