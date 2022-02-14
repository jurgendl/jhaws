package org.swingeasy;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.ToolTipManager;

import org.swingeasy.EComponentPopupMenu.ReadableComponent;

/**
 * @author Jurgen
 */
public class ECheckBox extends JCheckBox implements EComponentI, HasValue<Boolean>, ReadableComponent {
    private static final long serialVersionUID = -7050606626337213461L;

    protected final List<ValueChangeListener<Boolean>> valueChangeListeners = new ArrayList<>();

    protected ECheckBoxConfig cfg;

    protected ECheckBox() {
        cfg = null;
    }

    public ECheckBox(ECheckBoxConfig cfg) {
        super(cfg.getText(), cfg.getIcon(), cfg.isSelected());
        this.init(this.cfg = cfg.lock());
    }

    /**
     * @see org.swingeasy.HasValue#addValueChangeListener(org.swingeasy.ValueChangeListener)
     */
    @Override
    public void addValueChangeListener(ValueChangeListener<Boolean> listener) {
        valueChangeListeners.add(listener);
    }

    /**
     * @see org.swingeasy.HasValue#clearValueChangeListeners()
     */
    @Override
    public void clearValueChangeListeners() {
        valueChangeListeners.clear();
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

    /**
     * @see org.swingeasy.HasValue#getValue()
     */
    @Override
    public Boolean getValue() {
        return isSelected();
    }

    protected void init(ECheckBoxConfig config) {
        if (config.isTooltips()) {
            ToolTipManager.sharedInstance().registerComponent(this);
        }

        addActionListener(e -> {
            Boolean value = ECheckBox.this.getValue();
            for (ValueChangeListener<Boolean> valueChangeListener : valueChangeListeners) {
                valueChangeListener.valueChanged(value);
            }
        });

        if (cfg.isDefaultPopupMenu()) {
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

    /**
     * @see org.swingeasy.HasValue#removeValueChangeListener(org.swingeasy.ValueChangeListener)
     */
    @Override
    public void removeValueChangeListener(ValueChangeListener<Boolean> listener) {
        valueChangeListeners.remove(listener);
    }
}
