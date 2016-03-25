package org.swingeasy;

import javax.swing.Action;
import javax.swing.Icon;

/**
 * @author Jurgen
 */
public class ERadioButtonConfig extends EComponentConfig<ERadioButtonConfig> {
    protected String text;

    protected Icon icon;

    protected boolean selected;

    protected Action action;

    public ERadioButtonConfig() {
        super();
    }

    public ERadioButtonConfig(Action a) {
        this.action = a;
    }

    public ERadioButtonConfig(Icon icon) {
        this.icon = icon;
    }

    public ERadioButtonConfig(Icon icon, boolean selected) {
        this.icon = icon;
        this.selected = selected;
    }

    public ERadioButtonConfig(String text) {
        this.text = text;
    }

    public ERadioButtonConfig(String text, boolean selected) {
        this.text = text;
        this.selected = selected;
    }

    public ERadioButtonConfig(String text, Icon icon) {
        this.text = text;
        this.icon = icon;
    }

    public ERadioButtonConfig(String text, Icon icon, boolean selected) {
        this.text = text;
        this.icon = icon;
        this.selected = selected;
    }

    public Action getAction() {
        return this.action;
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

    public ERadioButtonConfig setAction(Action action) {
        this.lockCheck();
        this.action = action;
        return this;
    }

    public ERadioButtonConfig setIcon(Icon icon) {
        this.lockCheck();
        this.icon = icon;
        return this;
    }

    public ERadioButtonConfig setSelected(boolean selected) {
        this.lockCheck();
        this.selected = selected;
        return this;
    }

    public ERadioButtonConfig setText(String text) {
        this.lockCheck();
        this.text = text;
        return this;
    }
}
