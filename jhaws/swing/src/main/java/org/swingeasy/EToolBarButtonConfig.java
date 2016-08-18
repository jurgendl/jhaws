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
        action = a;
    }

    public EToolBarButtonConfig(EToolBarButtonCustomizer ebc) {
        buttonCustomizer = ebc;
    }

    public EToolBarButtonConfig(EToolBarButtonCustomizer ebc, Action a) {
        buttonCustomizer = ebc;
        action = a;
    }

    public EToolBarButtonConfig(EToolBarButtonCustomizer ebc, Icon icon) {
        buttonCustomizer = ebc;
        this.icon = icon;
    }

    public EToolBarButtonConfig(Icon icon) {
        this.icon = icon;
    }

    public Action getAction() {
        return action;
    }

    public EButtonCustomizer getButtonCustomizer() {
        return buttonCustomizer;
    }

    public Icon getIcon() {
        return icon;
    }

    public EToolBarButtonConfig setAction(Action action) {
        lockCheck();
        this.action = action;
        return this;
    }

    public EToolBarButtonConfig setButtonCustomizer(EToolBarButtonCustomizer buttonCustomizer) {
        lockCheck();
        this.buttonCustomizer = buttonCustomizer;
        return this;
    }

    public EToolBarButtonConfig setIcon(Icon icon) {
        lockCheck();
        this.icon = icon;
        return this;
    }
}
