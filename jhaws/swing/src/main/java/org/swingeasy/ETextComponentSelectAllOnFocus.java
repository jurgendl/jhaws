package org.swingeasy;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.text.JTextComponent;

/**
 * @author Jurgen
 */
public class ETextComponentSelectAllOnFocus implements FocusListener {
    /**
     * @see java.awt.event.FocusListener#focusGained(java.awt.event.FocusEvent)
     */
    @Override
    public void focusGained(FocusEvent e) {
        JTextComponent tf = (JTextComponent) e.getSource();
        tf.select(0, tf.getText().length());
    }

    /**
     * @see java.awt.event.FocusListener#focusLost(java.awt.event.FocusEvent)
     */
    @Override
    public void focusLost(FocusEvent e) {
        JTextComponent tf = (JTextComponent) e.getSource();
        tf.select(0, 0);
    }
}
