package org.swingeasy;

import java.awt.Component;

import javax.swing.JDialog;
import javax.swing.JFileChooser;

/**
 * @author Jurgen
 */
public interface FileChooserCustomizer {
    void customize(final Component parentComponent, final JDialog dialog);

    void customize(final JFileChooser fileChooser);
}