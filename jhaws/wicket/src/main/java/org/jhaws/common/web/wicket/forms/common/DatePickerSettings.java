package org.jhaws.common.web.wicket.forms.common;

@SuppressWarnings("serial")
public class DatePickerSettings extends AbstractFormElementSettings<DatePickerSettings> {
    public static enum DatePickerType {
        date, time, datetime;
    }

    protected DatePickerType type;

    public DatePickerSettings() {
        super();
    }

    public DatePickerSettings(DatePickerSettings other) {
        super(other);
        this.type = other.type;
    }

    public DatePickerSettings(boolean required) {
        super(required);
    }

    public DatePickerType getType() {
        return this.type;
    }

    public DatePickerSettings setType(DatePickerType type) {
        this.type = type;
        return this;
    }
}
