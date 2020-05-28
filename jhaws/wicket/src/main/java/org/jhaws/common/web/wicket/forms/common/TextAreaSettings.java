package org.jhaws.common.web.wicket.forms.common;

@SuppressWarnings("serial")
public class TextAreaSettings extends AbstractTextAreaSettings<TextAreaSettings> {
    protected Integer minlength;

    protected Integer maxlength;

    public TextAreaSettings() {
        super();
    }

    public Integer getMinlength() {
        return this.minlength;
    }

    public void setMinlength(Integer minlength) {
        this.minlength = minlength;
    }

    public Integer getMaxlength() {
        return this.maxlength;
    }

    public void setMaxlength(Integer maxlength) {
        this.maxlength = maxlength;
    }
}
