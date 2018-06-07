package org.tools.hqlbuilder.webservice.wicket.forms.common;

import org.tools.hqlbuilder.webservice.wicket.forms.AbstractTextAreaSettings;

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
