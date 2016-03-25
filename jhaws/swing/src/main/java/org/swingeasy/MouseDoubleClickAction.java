package org.swingeasy;

import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.Action;
import javax.swing.JComponent;

/**
 * @author Jurgen
 */
public class MouseDoubleClickAction extends MouseAdapter {
    protected final Action action;

    public MouseDoubleClickAction(Action action) {
        this.action = action;
    }

    public MouseDoubleClickAction inject(JComponent component) {
        component.addMouseListener(this);
        component.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return this;
    }

    /**
     * @see java.awt.event.MouseAdapter#mouseClicked(java.awt.event.MouseEvent)
     */
    @Override
    public void mouseClicked(java.awt.event.MouseEvent e) {
        if ((e.getClickCount() == 2) && (e.getButton() == MouseEvent.BUTTON1)) {
            ActionEvent ev = new ActionEvent(e.getComponent(), ActionEvent.ACTION_PERFORMED, (String) this.action.getValue(Action.NAME));
            this.action.actionPerformed(ev);
        }
    };
}
