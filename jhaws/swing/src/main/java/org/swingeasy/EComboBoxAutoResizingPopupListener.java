package org.swingeasy;

import java.awt.Dimension;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JPopupMenu;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

public class EComboBoxAutoResizingPopupListener implements PopupMenuListener {
    private Dimension preferredSize;

    @Override
    public void popupMenuCanceled(PopupMenuEvent e) {
        JComboBox<?> _box = (JComboBox<?>) e.getSource();
        _box.setPreferredSize(this.preferredSize);
    }

    @Override
    public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
        JComboBox<?> _box = (JComboBox<?>) e.getSource();
        _box.setPreferredSize(this.preferredSize);
    }

    @Override
    public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
        JComboBox<?> _box = (JComboBox<?>) e.getSource();
        this.preferredSize = _box.getPreferredSize();

        Object comp = _box.getUI().getAccessibleChild(_box, 0);
        if (!(comp instanceof JPopupMenu)) {
            return;
        }

        _box.setPreferredSize(null);
        Dimension preferredSizeCalc = _box.getPreferredSize();

        JComponent scrollPane = (JComponent) ((JPopupMenu) comp).getComponent(0);
        Dimension size = new Dimension();
        size.width = preferredSizeCalc.width;
        size.height = scrollPane.getPreferredSize().height;
        scrollPane.setPreferredSize(size);
    }
}
