package org.swingeasy;

/**
 * @author Jurgen
 */
public class ETreeTableConfig extends EComponentConfig<ETreeTableConfig> {
    protected boolean editable;

    public boolean isEditable() {
        return this.editable;
    }

    public ETreeTableConfig setEditable(boolean editable) {
        this.editable = editable;
        return this;
    }
}
