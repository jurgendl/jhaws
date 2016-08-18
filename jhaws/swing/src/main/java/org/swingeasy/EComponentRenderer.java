package org.swingeasy;

import java.awt.Graphics;

import javax.swing.JComponent;

/**
 * @author Jurgen
 */
public interface EComponentRenderer {
    public void render(JComponent c, Graphics g);
}
