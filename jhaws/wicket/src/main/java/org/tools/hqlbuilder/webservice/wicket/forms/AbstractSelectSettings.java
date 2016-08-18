package org.tools.hqlbuilder.webservice.wicket.forms;

public abstract class AbstractSelectSettings<T extends AbstractSelectSettings<T>> extends AbstractFormElementSettings<T> {
    private static final long serialVersionUID = -8517376888884835477L;

    protected boolean inheritValue = false;

    protected boolean nullValid = false;

    public AbstractSelectSettings() {
        super();
    }

    public AbstractSelectSettings(boolean required) {
        super(required);
    }

    public AbstractSelectSettings(T other) {
        super(other);
    }

    public boolean isNullValid() {
        return this.nullValid;
    }

    public T setNullValid(boolean nullValid) {
        this.nullValid = nullValid;
        return castThis();
    }

    public boolean isInheritValue() {
        return this.inheritValue;
    }

    public T setInheritValue(boolean inheritValue) {
        this.inheritValue = inheritValue;
        return castThis();
    }
}
