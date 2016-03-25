package org.swingeasy;

import java.awt.Component;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

/**
 * @author Jurgen
 */
public interface OptionPaneCustomizer {
    void customize(final Component parentComponent, final MessageType messageType, final OptionType optionType, final JOptionPane pane,
            final JDialog dialog);
}