package org.tools.hqlbuilder.webservice.wicket.forms;

public class MultiListSettings extends AbstractSelectSettings<MultiListSettings> {
    private static final long serialVersionUID = -7087528702318354068L;

    protected int size = 10;

    public MultiListSettings() {
        super();
    }

    public MultiListSettings(MultiListSettings other) {
        super(other);
    }

    public MultiListSettings(boolean required) {
        super(required);
    }

    public int getSize() {
        return this.size;
    }

    public MultiListSettings setSize(int size) {
        this.size = size;
        return this;
    }

    @Override
    public boolean isNullValid() {
        return this.nullValid;
    }

    @Override
    public MultiListSettings setNullValid(boolean nullValid) {
        this.nullValid = nullValid;
        return this;
    }
}
