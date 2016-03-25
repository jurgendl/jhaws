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
        return this.horizontalAlignment;
    }

    public Icon getIcon() {
        return this.icon;
    }

    public String getText() {
        return this.text;
    }

    public ELabelConfig setHorizontalAlignment(int horizontalAlignment) {
        this.lockCheck();
        this.horizontalAlignment = horizontalAlignment;
        return this;
    }

    public ELabelConfig setIcon(Icon icon) {
        this.lockCheck();
        this.icon = icon;
        return this;
    }

    public ELabelConfig setText(String text) {
        this.lockCheck();
        this.text = text;
        return this;
    }
}
