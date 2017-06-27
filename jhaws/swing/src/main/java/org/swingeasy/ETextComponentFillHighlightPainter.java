package org.swingeasy;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Rectangle;

/**
 * @author Jurgen
 */
public class ETextComponentFillHighlightPainter extends ETextComponentAbstractHighlightPainter {
    protected Color color;

    protected Color gradientColor;

    protected boolean verticalGradient = false;

    public ETextComponentFillHighlightPainter(Color color) {
        setColor(color);
    }

    /**
     * 
     * @see org.swingeasy.ETextComponentHighlightPainter#getColor()
     */
    @Override
    public Color getColor() {
        return color;
    }

    public Color getGradientColor() {
        return gradientColor;
    }

    public boolean isVerticalGradient() {
        return verticalGradient;
    }

    /**
     * 
     * @see org.swingeasy.ETextComponentAbstractHighlightPainter#paintHighlight(java.awt.Graphics2D, java.awt.Rectangle)
     */
    @Override
    protected void paintHighlight(Graphics2D g2d, Rectangle alloc) {
        Paint originalPaint = g2d.getPaint();
        if (gradientColor != null) {
            int x1;
            int y1;
            int x2;
            int y2;
            int w = alloc.width;
            int h = alloc.height;
            if (verticalGradient) {
                x1 = alloc.x + w / 2;
                y1 = alloc.y;
                x2 = x1;
                y2 = y1 + h / 2;
            } else {
                x1 = alloc.x;
                y1 = alloc.y;
                x2 = x1 + w / 2;
                y2 = y1 + h / 2;
            }
            g2d.setPaint(new GradientPaint(x1, y1, color, x2, y2, gradientColor, true));
        }
        g2d.fillRect(alloc.x, alloc.y, alloc.width, alloc.height);
        g2d.setPaint(originalPaint);
    }

    /**
     * 
     * @see org.swingeasy.ETextComponentHighlightPainter#setColor(java.awt.Color)
     */
    @Override
    public void setColor(Color color) {
        this.color = color;
    }

    public void setGradientColor(Color gradientColor) {
        this.gradientColor = gradientColor;
    }

    public void setVerticalGradient(boolean verticalGradient) {
        this.verticalGradient = verticalGradient;
    }
}
