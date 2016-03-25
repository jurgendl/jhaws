package org.swingeasy;

import java.awt.Dimension;

import javax.swing.AbstractButton;
import javax.swing.Action;
import javax.swing.KeyStroke;

/**
 * @author Jurgen
 */
public class EToolBarButtonCustomizer extends EButtonCustomizer {
    protected Dimension defaultDimension;

    public EToolBarButtonCustomizer() {
        super();
    }

    public EToolBarButtonCustomizer(Dimension defaultDimension) {
        super();
        this.defaultDimension = defaultDimension;
    }

    /**
     * 
     * @see org.swingeasy.EButtonCustomizer#customize(javax.swing.AbstractButton, java.awt.Dimension)
     */
    @Override
    public void customize(AbstractButton button) {
        if (this.defaultDimension != null) {
            button.setMaximumSize(this.defaultDimension);
            button.setPreferredSize(this.defaultDimension);
            button.setSize(this.defaultDimension);
        }
        button.setFocusable(false);
        button.setHideActionText(true);

        if (button.getAction() != null) {
            KeyStroke key = KeyStroke.class.cast(button.getAction().getValue(Action.ACCELERATOR_KEY));
            String kst = EComponentPopupMenu.keyStroke2String(key).trim();
            Object description = button.getAction().getValue(Action.SHORT_DESCRIPTION);
            if (description == null) {
                description = button.getAction().getValue(Action.LONG_DESCRIPTION);
            }
            if (kst.length() > 0) {
                button.setToolTipText(String.valueOf(description) + " (" + kst + ")");
            } else {
                button.setToolTipText(String.valueOf(description));
            }
        }
    }

    public Dimension getDefaultDimension() {
        return this.defaultDimension;
    }

    public void setDefaultDimension(Dimension defaultDimension) {
        this.defaultDimension = defaultDimension;
    }
}
