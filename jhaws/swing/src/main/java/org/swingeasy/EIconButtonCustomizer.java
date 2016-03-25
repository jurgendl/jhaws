package org.swingeasy;

import java.awt.Dimension;

import javax.swing.AbstractButton;
import javax.swing.Icon;

/**
 * @author Jurgen
 */
public class EIconButtonCustomizer extends EToolBarButtonCustomizer {
    protected Icon icon = null;

    public EIconButtonCustomizer() {
        super();
    }

    public EIconButtonCustomizer(Dimension defaultDimension) {
        super(defaultDimension);
    }

    public EIconButtonCustomizer(Icon icon) {
        super(new Dimension(icon.getIconWidth(), icon.getIconHeight()));
        this.icon = icon;
    }

    /**
     * 
     * @see org.swingeasy.EToolBarButtonCustomizer#customize(javax.swing.AbstractButton, java.awt.Dimension)
     */
    @Override
    public void customize(AbstractButton button) {
        super.customize(button);
        button.setBorderPainted(false);
        button.setOpaque(false);
        button.setContentAreaFilled(false);
        if (this.icon != null) {
            button.setIcon(this.icon);
        }
    }
}
