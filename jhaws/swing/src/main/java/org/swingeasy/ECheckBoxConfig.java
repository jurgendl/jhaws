package org.swingeasy;

import javax.swing.Icon;

/**
 * @author Jurgen
 */
public class ECheckBoxConfig extends EComponentConfig<ECheckBoxConfig> {
    protected boolean selected = true;

    protected Icon icon = null;

    protected String text = null;

    public ECheckBoxConfig() {
        super();
    }

    public ECheckBoxConfig(String text) {
        this.text = text;
    }

    public ECheckBoxConfig(String text, boolean selected) {
        this.text = text;
        this.selected = selected;
    }

    public Icon getIcon() {
        return this.icon;
    }

    public String getText() {
        return this.text;
    }

    public boolean isSelected() {
        return this.selected;
    }

    public ECheckBoxConfig setIcon(Icon icon) {
        this.lockCheck();
        this.icon = icon;
        return this;
    }

    public ECheckBoxConfig setSelected(boolean selected) {
        this.lockCheck();
        this.selected = selected;
        return this;
    }

    public ECheckBoxConfig setText(String text) {
        this.lockCheck();
        this.text = text;
        return this;
    }
}
