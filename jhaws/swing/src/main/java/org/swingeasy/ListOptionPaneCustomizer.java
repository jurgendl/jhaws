package org.swingeasy;

import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

/**
 * adds a listener to the list that confirms the dialog when the user doubleclicks an item
 * 
 * @author Jurgen
 */
public class ListOptionPaneCustomizer<T> implements OptionPaneCustomizer {
    protected final EList<T> list;

    public ListOptionPaneCustomizer(EList<T> list) {
        this.list = list;
    }

    /**
     * 
     * @see org.swingeasy.OptionPaneCustomizer#customize(java.awt.Component, org.swingeasy.MessageType, org.swingeasy.OptionType,
     *      javax.swing.JOptionPane, javax.swing.JDialog)
     */
    @Override
    public void customize(Component parentComponent, MessageType messageType, OptionType optionType, final JOptionPane pane, final JDialog dialog) {
        this.list.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if ((e.getClickCount() == 2) && (e.getButton() == MouseEvent.BUTTON1)) {
                    pane.setValue(new Integer(0));
                    dialog.dispose();
                }
            }
        });
    }
}
