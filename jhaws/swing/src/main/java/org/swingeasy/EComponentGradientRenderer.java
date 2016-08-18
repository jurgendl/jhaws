package org.swingeasy;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JComponent;

/**
 * @author Jurgen
 */
public class EComponentGradientRenderer implements EComponentRenderer {
    public static enum GradientOrientation {
        HORIZONTAL, VERTICAL, DIAGONAL, OFF;
    }

    private Color gradientColorStart = Color.WHITE;

    private Color gradientColorEnd = Color.LIGHT_GRAY;

    private GradientOrientation orientation = GradientOrientation.DIAGONAL;

    public EComponentGradientRenderer() {
        super();
    }

    public EComponentGradientRenderer(GradientOrientation orientation, Color gradientColorStart, Color gradientColorEnd) {
        super();
        this.orientation = orientation;
        this.gradientColorStart = gradientColorStart;
        this.gradientColorEnd = gradientColorEnd;
    }

    public Color getGradientColorEnd() {
        return gradientColorEnd;
    }

    public Color getGradientColorStart() {
        return gradientColorStart;
    }

    public GradientOrientation getOrientation() {
        return orientation;
    }

    /**
     * @see org.swingeasy.EComponentRenderer#render(javax.swing.JComponent, java.awt.Graphics)
     */
    @Override
    public void render(JComponent c, Graphics g) {
        if (orientation != GradientOrientation.OFF) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            int w = c.getWidth();
            int h = c.getHeight();
            GradientPaint gp;
            switch (orientation) {
                case HORIZONTAL:
                    gp = new GradientPaint(0, 0, gradientColorStart, w, 0, gradientColorEnd);
                    break;
                case VERTICAL:
                    gp = new GradientPaint(0, 0, gradientColorStart, 0, h, gradientColorEnd);
                    break;
                default:
                case DIAGONAL:
                    gp = new GradientPaint(0, 0, gradientColorStart, w, h, gradientColorEnd);
                    break;
                case OFF:
                    gp = null;
                    break;
            }
            g2d.setPaint(gp);
            g2d.fillRect(0, 0, w, h);
        }
    }

    public EComponentGradientRenderer setGradientColorEnd(Color gradientColorEnd) {
        this.gradientColorEnd = gradientColorEnd;
        return this;
    }

    public EComponentGradientRenderer setGradientColorStart(Color gradientColorStart) {
        this.gradientColorStart = gradientColorStart;
        return this;
    }

    public EComponentGradientRenderer setOrientation(GradientOrientation orientation) {
        this.orientation = orientation;
        return this;
    }
}
