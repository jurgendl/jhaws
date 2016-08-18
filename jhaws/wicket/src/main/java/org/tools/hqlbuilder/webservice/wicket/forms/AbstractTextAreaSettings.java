package org.tools.hqlbuilder.webservice.wicket.forms;

public abstract class AbstractTextAreaSettings<T extends AbstractTextAreaSettings<T>> extends AbstractFormElementSettings<T> {
    private static final long serialVersionUID = 6279323396756540215L;

    private Integer rows;

    private Integer cols;

    private boolean resizable = true;

    public Integer getRows() {
        return this.rows;
    }

    public Integer getCols() {
        return this.cols;
    }

    public T setRows(Integer rows) {
        this.rows = rows;
        return castThis();
    }

    public T setCols(Integer cols) {
        this.cols = cols;
        return castThis();
    }

    public boolean isResizable() {
        return this.resizable;
    }

    public T setResizable(boolean resizable) {
        this.resizable = resizable;
        return castThis();
    }
}
