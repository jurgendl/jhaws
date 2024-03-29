package org.swingeasy;

import java.awt.event.ActionEvent;

import javax.swing.JComponent;
import javax.swing.JRadioButton;
import javax.swing.ToolTipManager;

import org.apache.commons.lang3.StringUtils;
import org.swingeasy.EComponentPopupMenu.ReadableComponent;

/**
 * @author Jurgen
 */
public class ERadioButton extends JRadioButton implements EComponentI, ReadableComponent {
    private static final long serialVersionUID = -1359464901174268318L;

    protected final ERadioButtonConfig cfg;

    protected ERadioButton() {
        cfg = null;
    }

    public ERadioButton(ERadioButtonConfig cfg) {
        this.init(this.cfg = cfg.lock());
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
                text = null;
            }
            return text;
        }
        return toolTipText;
    }

    protected void init(ERadioButtonConfig config) {
        if (config.isTooltips()) {
            ToolTipManager.sharedInstance().registerComponent(this);
        }

        if (config.getAction() != null) {
            setAction(config.getAction());
        }

        if (StringUtils.isNotBlank(config.getText())) {
            setText(config.getText());
        }

        if (config.getIcon() != null) {
            setIcon(config.getIcon());
        }

        setSelected(config.isSelected());

        if (config.isDefaultPopupMenu()) {
            installPopupMenuAction(EComponentPopupMenu.installPopupMenu(this));
        }

        if (config.isLocalized()) {
            UIUtils.registerLocaleChangeListener((EComponentI) this);
        }
    }

    /**
     * JDOC
     */
    protected void installPopupMenuAction(EComponentPopupMenu menu) {
        //
    }
}
