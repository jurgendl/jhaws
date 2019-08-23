package org.swingeasy;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Stroke;

/**
 * @see javax.swing.text.html.HTMLEditorKit.NavigateLinkAction.FocusHighlightPainter
 */
public class ETextComponentBorderHighlightPainter extends ETextComponentAbstractHighlightPainter {
    protected Color color;

    protected Stroke stroke;

    protected int offset = 1;

    public ETextComponentBorderHighlightPainter(Color color) {
        this.color = color;
    }

    /**
     *
     * @see org.swingeasy.ETextComponentHighlightPainter#getColor()
     */
    @Override
    public Color getColor() {
        return color;
    }

    public int getOffset() {
        return offset;
    }

    public Stroke getStroke() {
        return stroke;
    }

    /**
     *
     * @see org.swingeasy.ETextComponentAbstractHighlightPainter#paintHighlight(java.awt.Graphics2D, java.awt.Rectangle)
     */
    @Override
    protected void paintHighlight(Graphics2D g2d, Rectangle alloc) {
        Stroke originalStroke = g2d.getStroke();
        int x = alloc.x;
        int y = alloc.y + offset;
        int w = alloc.width - 1;
        int h = alloc.height - offset * 2;
        if (getStroke() != null) {
            g2d.setStroke(getStroke());
        }
        g2d.drawRect(x, y, w, h);
        g2d.setStroke(originalStroke);
    }

    /**
     *
     * @see org.swingeasy.ETextComponentHighlightPainter#setColor(java.awt.Color)
     */
    @Override
    public void setColor(Color color) {
        this.color = color;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public void setStroke(Stroke stroke) {
        this.stroke = stroke;
    }
}