package org.tools.hqlbuilder.webservice.wicket.forms;

public class CheckBoxSettings extends AbstractFormElementSettings<CheckBoxSettings> {
    private static final long serialVersionUID = 8680530201586036946L;

    protected boolean nice = true;

    protected boolean labelBehind = true;

    public CheckBoxSettings() {
        super();
    }

    public CheckBoxSettings(boolean required) {
        super(required);
    }

    public CheckBoxSettings(CheckBoxSettings other) {
        super(other);
    }

    public boolean isNice() {
        return this.nice;
    }

    public CheckBoxSettings setNice(boolean nice) {
        this.nice = nice;
        return castThis();
    }

    public boolean isLabelBehind() {
        return labelBehind;
    }

    public CheckBoxSettings setLabelBehind(boolean labelBehind) {
        this.labelBehind = labelBehind;
        return castThis();
    }
}
