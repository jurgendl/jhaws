package org.swingeasy;

import javax.swing.Action;
import javax.swing.Icon;

/**
 * @author Jurgen
 */
public class EButtonConfig extends EComponentConfig<EButtonConfig> {
    protected EButtonCustomizer buttonCustomizer;

    protected Action action;

    protected Icon icon;

    protected String text;

    public EButtonConfig() {
        super();
    }

    public EButtonConfig(Action a) {
        this.action = a;
    }

    public EButtonConfig(EButtonCustomizer ebc) {
        this.buttonCustomizer = ebc;
    }

    public EButtonConfig(EButtonCustomizer ebc, Action a) {
        this.buttonCustomizer = ebc;
        this.action = a;
    }

    public EButtonConfig(EButtonCustomizer ebc, Icon icon) {
        this.buttonCustomizer = ebc;
        this.icon = icon;
    }

    public EButtonConfig(EButtonCustomizer ebc, String text) {
        this.buttonCustomizer = ebc;
        this.text = text;
    }

    public EButtonConfig(EButtonCustomizer ebc, String text, Icon icon) {
        this.buttonCustomizer = ebc;
        this.text = text;
        this.icon = icon;
    }

    public EButtonConfig(Icon icon) {
        this.icon = icon;
    }

    public EButtonConfig(String text) {
        this.text = text;
    }

    public EButtonConfig(String text, Icon icon) {
        this.text = text;
        this.icon = icon;
    }

    public Action getAction() {
        return this.action;
    }

    public EButtonCustomizer getButtonCustomizer() {
        return this.buttonCustomizer;
    }

    public Icon getIcon() {
        return this.icon;
    }

    public String getText() {
        return this.text;
    }

    public EButtonConfig setAction(Action action) {
        this.lockCheck();
        this.action = action;
        return this;
    }

    public EButtonConfig setButtonCustomizer(EButtonCustomizer buttonCustomizer) {
        this.lockCheck();
        this.buttonCustomizer = buttonCustomizer;
        return this;
    }

    public EButtonConfig setIcon(Icon icon) {
        this.lockCheck();
        this.icon = icon;
        return this;
    }

    public EButtonConfig setText(String text) {
        this.lockCheck();
        this.text = text;
        return this;
    }
}
