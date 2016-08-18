package org.swingeasy;

import java.awt.event.ActionEvent;

import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.ToolTipManager;

import org.swingeasy.EComponentPopupMenu.ReadableComponent;

/**
 * @author Jurgen
 */
public class EButton extends JButton implements EComponentI, ReadableComponent {
    private static final long serialVersionUID = -6193067407274776197L;

    protected EButtonConfig cfg;

    protected EButton() {
        cfg = null;
    }

    public EButton(EButtonConfig cfg) {
        this.init(cfg = cfg.lock());
    }

    /**
     * @see org.swingeasy.EComponentPopupMenu.ReadableComponent#copy(java.awt.event.ActionEvent)
     */
    @Override
    public void copy(ActionEvent e) {
        EComponentPopupMenu.copyToClipboard(getText());
    }

    /**
     * @see org.swingeasy.HasParentComponent#getParentComponent()
     */
    @Override
    public JComponent getParentComponent() {
        return this;
    }

    protected void init(EButtonConfig config) {
        if (config.getAction() != null) {
            setAction(config.getAction());
            setName(String.valueOf(config.getAction().getValue(Action.NAME)));
        }

        if (config.getText() != null) {
            setText(config.getText());
        }

        if (config.getIcon() != null) {
            setIcon(config.getIcon());
        }

        if (config.getButtonCustomizer() != null) {
            config.getButtonCustomizer().customize(this);
        }

        if (config.isDefaultPopupMenu()) {
            installPopupMenuAction(EComponentPopupMenu.installPopupMenu(this));
        }

        if (config.isLocalized()) {
            UIUtils.registerLocaleChangeListener((EComponentI) this);
        }

        if (config.isTooltips()) {
            ToolTipManager.sharedInstance().registerComponent(this);
        }
    }

    protected void installPopupMenuAction(EComponentPopupMenu menu) {
        //
    }
}
