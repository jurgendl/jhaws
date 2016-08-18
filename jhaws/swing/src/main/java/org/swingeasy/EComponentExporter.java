package org.swingeasy;

import javax.swing.Icon;
import javax.swing.JComponent;

/**
 * @author Jurgen
 */
public interface EComponentExporter<T extends JComponent & EComponentI> {
    public void export(T component);

    public String getAction();

    public String getFileExtension();

    public Icon getIcon();
}
