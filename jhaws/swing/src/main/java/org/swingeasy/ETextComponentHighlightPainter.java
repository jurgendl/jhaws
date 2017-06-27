package org.swingeasy;

import java.awt.Color;

import javax.swing.text.Highlighter.HighlightPainter;

/**
 * @author Jurgen
 */
public interface ETextComponentHighlightPainter extends HighlightPainter {
    public Color getColor();

    public void setColor(Color color);
}
