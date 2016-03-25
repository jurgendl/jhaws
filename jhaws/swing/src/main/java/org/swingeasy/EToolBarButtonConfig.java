package org.swingeasy;

import javax.swing.Action;
import javax.swing.Icon;

/**
 * @author Jurgen
 */
public class EToolBarButtonConfig extends EComponentConfig<EToolBarButtonConfig> {
    protected EToolBarButtonCustomizer buttonCustomizer;

    protected Action action;

    protected Icon icon;

    public EToolBarButtonConfig() {
        super();
    }

    public EToolBarButtonConfig(Action a) {
        this.action = a;
    }

    public EToolBarButtonConfig(EToolBarButtonCustomizer ebc) {
        this.buttonCustomizer = ebc;
    }

    public EToolBarButtonConfig(EToolBarButtonCustomizer ebc, Action a) {
        this.buttonCustomizer = ebc;
        this.action = a;
    }

    public EToolBarButtonConfig(EToolBarButtonCustomizer ebc, Icon icon) {
        this.buttonCustomizer = ebc;
        this.icon = icon;
    }

    public EToolBarButtonConfig(Icon icon) {
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

    public EToolBarButtonConfig setAction(Action action) {
        this.lockCheck();
        this.action = action;
        return this;
    }

    public EToolBarButtonConfig setButtonCustomizer(EToolBarButtonCustomizer buttonCustomizer) {
        this.lockCheck();
        this.buttonCustomizer = buttonCustomizer;
        return this;
    }

    public EToolBarButtonConfig setIcon(Icon icon) {
        this.lockCheck();
        this.icon = icon;
        return this;
    }
}
