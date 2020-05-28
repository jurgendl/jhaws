package org.jhaws.common.web.wicket.forms.common;

@SuppressWarnings("serial")
public class MultiSelectSettings extends AbstractSelectSettings<MultiSelectSettings> {
    public MultiSelectSettings() {
        super();
    }

    public MultiSelectSettings(MultiSelectSettings other) {
        super(other);
    }

    public MultiSelectSettings(boolean required) {
        super(required);
    }

    @Override
    public boolean isNullValid() {
        return this.nullValid;
    }

    @Override
    public MultiSelectSettings setNullValid(boolean nullValid) {
        this.nullValid = nullValid;
        return this;
    }
}
