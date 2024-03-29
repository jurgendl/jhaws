package org.swingeasy;

import java.awt.event.ActionEvent;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.ToolTipManager;

import org.swingeasy.EComponentPopupMenu.ReadableComponent;

/**
 * @author Jurgen
 */
public class ELabel extends JLabel implements EComponentI, ReadableComponent {
    private static final long serialVersionUID = 8880462529209952297L;

    protected final ELabelConfig cfg;

    public ELabel() {
        this(new ELabelConfig(""));
    }

    public ELabel(ELabelConfig cfg) {
        init(this.cfg = cfg.lock());
    }

    public ELabel(String text) {
        this(new ELabelConfig(text));
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

    /**
     * @see javax.swing.JComponent#getToolTipText()
     */
    @Override
    public String getToolTipText() {
        String toolTipText = super.getToolTipText();
        if (toolTipText == null) {
            String text = getText();
            if (text.trim().length() == 0) {
                return null;
            }
            return text.trim();
        }
        return toolTipText;
    }

    protected void init(ELabelConfig config) {
        if (config.getText() != null) {
            setText(config.getText());
        }

        if (config.getIcon() != null) {
            setIcon(config.getIcon());
        }

        if (config.getHorizontalAlignment() != null) {
            setHorizontalAlignment(config.getHorizontalAlignment());
        }

        if (config.isTooltips()) {
            ToolTipManager.sharedInstance().registerComponent(this);
        }

        if (config.isLocalized()) {
            UIUtils.registerLocaleChangeListener((EComponentI) this);
        }

        if (config.isDefaultPopupMenu()) {
            installPopupMenuAction(EComponentPopupMenu.installPopupMenu(this));
        }

        setFocusable(false);
    }

    protected void installPopupMenuAction(EComponentPopupMenu menu) {
        //
    }
}
