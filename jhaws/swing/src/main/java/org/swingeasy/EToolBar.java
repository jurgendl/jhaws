package org.swingeasy;

import java.awt.Component;

import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JToolBar;

/**
 * @author Jurgen
 */
public class EToolBar extends JToolBar {
    private static final long serialVersionUID = -1257874888846884947L;

    public EToolBar(JPopupMenu popup) {
        super(popup.getName());
        for (int i = 0; i < popup.getComponentCount(); i++) {
            Component component = popup.getComponent(i);
            if (component instanceof JMenuItem) {
                Action action = JMenuItem.class.cast(component).getAction();
                JButton button = this.add(action);
                button.setRequestFocusEnabled(false);
            } else {
                this.addSeparator();
            }
        }
    }
}
