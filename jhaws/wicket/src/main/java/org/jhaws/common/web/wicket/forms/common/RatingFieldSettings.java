package org.jhaws.common.web.wicket.forms.common;

@SuppressWarnings("serial")
public class RatingFieldSettings extends AbstractNumberFieldSettings<Integer, RatingFieldSettings> {
    protected Boolean clearable = Boolean.TRUE;

    protected Boolean inline;

    public RatingFieldSettings(boolean required, Integer maximum) {
        super(required, null, maximum, null);
    }

    public RatingFieldSettings(Integer maximum) {
        super(null, maximum, null);
    }

    public Boolean getClearable() {
        return this.clearable;
    }

    public RatingFieldSettings setClearable(Boolean clearable) {
        this.clearable = clearable;
        return this;
    }

    public Boolean getInline() {
        return this.inline;
    }

    public RatingFieldSettings setInline(Boolean inline) {
        this.inline = inline;
        return this;
    }
}
