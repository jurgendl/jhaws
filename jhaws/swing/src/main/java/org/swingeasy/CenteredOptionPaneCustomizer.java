package org.swingeasy;

import java.awt.Component;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

/**
 * center dialog on screen
 * 
 * @author Jurgen
 */
public class CenteredOptionPaneCustomizer implements OptionPaneCustomizer {
    /**
     * 
     * @see org.swingeasy.OptionPaneCustomizer#customize(java.awt.Component, org.swingeasy.MessageType, org.swingeasy.OptionType,
     *      javax.swing.JOptionPane, javax.swing.JDialog)
     */
    @Override
    public void customize(Component parentComponent, MessageType messageType, OptionType optionType, JOptionPane pane, JDialog dialog) {
        dialog.setLocationRelativeTo(null);
    }
}
