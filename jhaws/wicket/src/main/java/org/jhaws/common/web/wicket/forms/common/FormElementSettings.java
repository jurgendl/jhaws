package org.jhaws.common.web.wicket.forms.common;

@SuppressWarnings("serial")
public class FormElementSettings extends AbstractFormElementSettings<FormElementSettings> {
    public FormElementSettings() {
        super();
    }

    public FormElementSettings(boolean required) {
        super(required);
    }

    public FormElementSettings(FormElementSettings other) {
        super(other);
    }
}
