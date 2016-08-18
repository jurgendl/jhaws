package org.tools.hqlbuilder.webservice.wicket.forms;

public class ListSettings extends AbstractSelectSettings<ListSettings> {
    private static final long serialVersionUID = -6574286191603464915L;

    protected int size = 10;

    public ListSettings() {
        super();
    }

    public ListSettings(ListSettings other) {
        super(other);
    }

    public ListSettings(boolean required) {
        super(required);
    }

    public int getSize() {
        return this.size;
    }

    public ListSettings setSize(int size) {
        this.size = size;
        return this;
    }

    @Override
    public boolean isNullValid() {
        return this.nullValid;
    }

    @Override
    public ListSettings setNullValid(boolean nullValid) {
        this.nullValid = nullValid;
        return this;
    }
}
