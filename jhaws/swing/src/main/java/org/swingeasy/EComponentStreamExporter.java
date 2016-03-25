package org.swingeasy;

import java.io.IOException;
import java.io.OutputStream;

import javax.swing.JComponent;

/**
 * @author Jurgen
 */
public interface EComponentStreamExporter<T extends JComponent & EComponentI> {
    public void exportStream(T component, OutputStream out) throws IOException;
}
