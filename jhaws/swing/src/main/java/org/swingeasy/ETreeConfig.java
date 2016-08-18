package org.swingeasy;

import java.awt.Color;

/**
 * @author Jurgen
 */
public class ETreeConfig extends EComponentConfig<ETreeConfig> {
    protected boolean editable = true;

    protected Color focusColor;

    public ETreeConfig() {
        super();
    }

    public Color getFocusColor() {
        return focusColor;
    }

    public boolean isEditable() {
        return editable;
    }

    public ETreeConfig setEditable(boolean editable) {
        lockCheck();
        this.editable = editable;
        return this;
    }

    public ETreeConfig setFocusColor(Color focusColor) {
        lockCheck();
        this.focusColor = focusColor;
        return this;
    }
}
