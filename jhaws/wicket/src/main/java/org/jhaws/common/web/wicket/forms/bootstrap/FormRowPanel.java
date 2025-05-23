package org.jhaws.common.web.wicket.forms.bootstrap;

import org.apache.wicket.feedback.FeedbackMessage;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.model.IModel;
import org.jhaws.common.lambda.LambdaPath;
import org.jhaws.common.web.wicket.forms.common.AbstractFormElementSettings;
import org.jhaws.common.web.wicket.forms.common.FormRowPanelParent;
import org.jhaws.common.web.wicket.forms.common.FormSettings;

@SuppressWarnings("serial")
public abstract class FormRowPanel<P, T, C extends FormComponent<T>, ElementSettings extends AbstractFormElementSettings<ElementSettings>> extends FormRowPanelParent<P, T, C, ElementSettings> {
    public FormRowPanel(IModel<?> model, LambdaPath<?, P> propertyPath, FormSettings formSettings, ElementSettings componentSettings) {
        super(model, propertyPath, formSettings, componentSettings);
    }

    public FormRowPanel(LambdaPath<?, P> propertyPath, IModel<T> valueModel, FormSettings formSettings, ElementSettings componentSettings) {
        super(propertyPath, valueModel, formSettings, componentSettings);
    }

    @Override
    public String getLabelClass(ElementSettings settings) {
        if (formSettings.getColumns() == null) {
            return settings.getLabelClass();
        }
        if (formSettings.getColumns() >= 5) {
            return "col-1";
        }
        return "col-2";
    }

    @Override
    public String getComponentClass(ElementSettings settings) {
        if (formSettings.getColumns() == null) {
            return settings.getComponentClass();
        }
        if (formSettings.getColumns() == 1) {
            return "col-10";
        }
        if (formSettings.getColumns() == 2) {
            return "col-4";
        }
        if (formSettings.getColumns() == 3) {
            return "col-2";
        }
        return "col-1";
    }

    protected String getFeedbackMessageCSSClass(final FeedbackMessage message) {
        switch (message.getLevel()) {
            case FeedbackMessage.DEBUG:
                return "alert pl-1 pr-1 pt-1 pb-1 alert-info";
            case FeedbackMessage.INFO:
                return "alert pl-1 pr-1 pt-1 pb-1 alert-info";
            case FeedbackMessage.SUCCESS:
                return "alert pl-1 pr-1 pt-1 pb-1 alert-success";
            case FeedbackMessage.WARNING:
                return "alert pl-1 pr-1 pt-1 pb-1 alert-warning";
            case FeedbackMessage.ERROR:
                return "alert pl-1 pr-1 pt-1 pb-1 alert-danger";
            case FeedbackMessage.FATAL:
                return "alert pl-1 pr-1 pt-1 pb-1 alert-danger";
        }
        return "alert pl-1 pr-1 pt-1 pb-1 alert-info";
    }
}
