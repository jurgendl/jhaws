package org.swingeasy;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;

import javax.swing.plaf.TextUI;
import javax.swing.text.BadLocationException;
import javax.swing.text.JTextComponent;
import javax.swing.text.LayeredHighlighter;
import javax.swing.text.Position;
import javax.swing.text.View;

/**
 * @author Jurgen
 */
public abstract class ETextAreaAbstractHighlightPainter extends LayeredHighlighter.LayerPainter implements ETextAreaHighlightPainter {
    public ETextAreaAbstractHighlightPainter() {
        super();
    }

    /**
     * 
     * @see javax.swing.text.Highlighter.HighlightPainter#paint(java.awt.Graphics, int, int, java.awt.Shape, javax.swing.text.JTextComponent)
     */
    @Override
    public void paint(Graphics g, int offs0, int offs1, Shape bounds, JTextComponent c) {
        Rectangle alloc = bounds.getBounds();
        try {
            // --- determine locations ---
            TextUI mapper = c.getUI();
            Rectangle p0 = mapper.modelToView(c, offs0);
            Rectangle p1 = mapper.modelToView(c, offs1);

            // --- render ---
            Color color1 = this.getColor();

            if (color1 == null) {
                g.setColor(c.getSelectionColor());
            } else {
                g.setColor(color1);
            }

            if (p0.y == p1.y) {
                // same line, render a rectangle
                Rectangle r = p0.union(p1);
                g.fillRect(r.x, r.y, r.width, r.height);
            } else {
                // different lines
                int p0ToMarginWidth = (alloc.x + alloc.width) - p0.x;
                g.fillRect(p0.x, p0.y, p0ToMarginWidth, p0.height);
                if ((p0.y + p0.height) != p1.y) {
                    g.fillRect(alloc.x, p0.y + p0.height, alloc.width, p1.y - (p0.y + p0.height));
                }
                g.fillRect(alloc.x, p1.y, (p1.x - alloc.x), p1.height);
            }
        } catch (BadLocationException e) {
            // can't render
        }
    }

    protected abstract void paintHighlight(Graphics2D g2d, Rectangle alloc);

    /**
     * 
     * @see javax.swing.text.LayeredHighlighter.LayerPainter#paintLayer(java.awt.Graphics, int, int, java.awt.Shape, javax.swing.text.JTextComponent,
     *      javax.swing.text.View)
     */
    @Override
    public Shape paintLayer(Graphics g, int offs0, int offs1, Shape bounds, JTextComponent c, View view) {
        Color color1 = this.getColor();

        if (color1 == null) {
            g.setColor(c.getSelectionColor());
        } else {
            g.setColor(color1);
        }

        Graphics2D g2d = (Graphics2D) g;

        if ((offs0 == view.getStartOffset()) && (offs1 == view.getEndOffset())) {
            // Contained in view, can just use bounds.
            Rectangle alloc;
            if (bounds instanceof Rectangle) {
                alloc = (Rectangle) bounds;
            } else {
                alloc = bounds.getBounds();
            }

            this.paintHighlight(g2d, alloc);

            return alloc;
        }
        // Should only render part of View.
        try {
            // --- determine locations ---
            Shape shape = view.modelToView(offs0, Position.Bias.Forward, offs1, Position.Bias.Backward, bounds);
            Rectangle alloc = (shape instanceof Rectangle) ? (Rectangle) shape : shape.getBounds();

            this.paintHighlight(g2d, alloc);

            return alloc;
        } catch (BadLocationException e) {
            // can't render
        }
        // Only if exception
        return null;
    }
}
