package org.swingeasy;

import javax.swing.Icon;

/**
 * @author Jurgen
 */
public class ELabelConfig extends EComponentConfig<ELabelConfig> {
    protected String text;

    protected Icon icon;

    protected Integer horizontalAlignment;

    public ELabelConfig(Icon icon) {
        this.icon = icon;
    }

    public ELabelConfig(Icon icon, int horizontalAlignment) {
        this.icon = icon;
        this.horizontalAlignment = horizontalAlignment;
    }

    public ELabelConfig(String text) {
        this.text = text;
    }

    public ELabelConfig(String text, Icon icon, int horizontalAlignment) {
        this.icon = icon;
        this.horizontalAlignment = horizontalAlignment;
        this.text = text;
    }

    public ELabelConfig(String text, int horizontalAlignment) {
        this.horizontalAlignment = horizontalAlignment;
        this.text = text;
    }

    public Integer getHorizontalAlignment() {
        return horizontalAlignment;
    }

    public Icon getIcon() {
        return icon;
    }

    public String getText() {
        return text;
    }

    public ELabelConfig setHorizontalAlignment(int horizontalAlignment) {
        lockCheck();
        this.horizontalAlignment = horizontalAlignment;
        return this;
    }

    public ELabelConfig setIcon(Icon icon) {
        lockCheck();
        this.icon = icon;
        return this;
    }

    public ELabelConfig setText(String text) {
        lockCheck();
        this.text = text;
        return this;
    }
}
