/**
 * @(#)VerticalLabelUI.java 1.0 02/18/09
 */
package org.swingeasy;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicLabelUI;

/**
 * A UI delegate for JLabel that rotates the label 90 degrees
 * <P>
 * Extends {@link BasicLabelUI}.
 * <P>
 * The only difference between the appearance of labels in the Basic and Metal L&Fs is the manner in which diabled text is painted. As VerticalLabelUI
 * does not override the method paintDisabledText, this class can be adapted for Metal L&F by extending MetalLabelUI instead of BasicLabelUI.
 * <P>
 * No other changes are required.
 * 
 * @author Darryl
 */
public class VerticalLabelUI extends BasicLabelUI {

    private final static VerticalLabelUI SAFE_VERTICAL_LABEL_UI = new VerticalLabelUI();

    protected static VerticalLabelUI verticalLabelUI = new VerticalLabelUI();

    /**
     * @see ComponentUI#createUI(javax.swing.JComponent)
     */
    public static ComponentUI createUI(JComponent c) {
        if (System.getSecurityManager() != null) {
            return VerticalLabelUI.SAFE_VERTICAL_LABEL_UI;
        }
        return VerticalLabelUI.verticalLabelUI;
    }

    private boolean clockwise = false;

    Rectangle verticalIconR = new Rectangle();

    Rectangle verticalTextR = new Rectangle();

    // see comment in BasicLabelUI
    Rectangle verticalViewR = new Rectangle();

    /**
     * Constructs a <code>VerticalLabelUI</code> with the default anticlockwise rotation
     */
    public VerticalLabelUI() {
        super();
    }

    /**
     * Constructs a <code>VerticalLabelUI</code> with the desired rotation.
     * <P>
     * 
     * @param clockwise true to rotate clockwise, false for anticlockwise
     */
    public VerticalLabelUI(boolean clockwise) {
        this.clockwise = clockwise;
    }

    private Rectangle copyRectangle(Rectangle from, Rectangle to) {
        if (to == null) {
            to = new Rectangle();
        }
        to.x = from.x;
        to.y = from.y;
        to.width = from.width;
        to.height = from.height;
        return to;
    }

    /**
     * Overridden to always return -1, since a vertical label does not have a meaningful baseline.
     * 
     * @see ComponentUI#getBaseline(JComponent, int, int)
     */
    @Override
    public int getBaseline(JComponent c, int width, int height) {
        super.getBaseline(c, width, height);
        return -1;
    }

    /**
     * Overridden to always return Component.BaselineResizeBehavior.OTHER, since a vertical label does not have a meaningful baseline
     * 
     * @see ComponentUI#getBaselineResizeBehavior(javax.swing.JComponent)
     */
    @Override
    public Component.BaselineResizeBehavior getBaselineResizeBehavior(JComponent c) {
        super.getBaselineResizeBehavior(c);
        return Component.BaselineResizeBehavior.OTHER;
    }

    /**
     * Returns a Dimension appropriate for vertical rendering
     * 
     * @see ComponentUI#getMaximumSize(javax.swing.JComponent)
     */
    @Override
    public Dimension getMaximumSize(JComponent c) {
        return this.transposeDimension(super.getMaximumSize(c));
    }

    /**
     * Returns a Dimension appropriate for vertical rendering
     * 
     * @see ComponentUI#getMinimumSize(javax.swing.JComponent)
     */
    @Override
    public Dimension getMinimumSize(JComponent c) {
        return this.transposeDimension(super.getMinimumSize(c));
    }

    /**
     * Returns a Dimension appropriate for vertical rendering
     * 
     * @see ComponentUI#getPreferredSize(javax.swing.JComponent)
     */
    @Override
    public Dimension getPreferredSize(JComponent c) {
        return this.transposeDimension(super.getPreferredSize(c));
    }

    /**
     * Transposes the view rectangles as appropriate for a vertical view before invoking the super method and copies them after they have been altered
     * by {@link SwingUtilities#layoutCompoundLabel(FontMetrics, String, Icon, int, int, int, int, Rectangle, Rectangle, Rectangle, int)}
     */
    @Override
    protected String layoutCL(JLabel label, FontMetrics fontMetrics, String text, Icon icon, Rectangle viewR, Rectangle iconR, Rectangle textR) {

        this.verticalViewR = this.transposeRectangle(viewR, this.verticalViewR);
        this.verticalIconR = this.transposeRectangle(iconR, this.verticalIconR);
        this.verticalTextR = this.transposeRectangle(textR, this.verticalTextR);

        text = super.layoutCL(label, fontMetrics, text, icon, this.verticalViewR, this.verticalIconR, this.verticalTextR);

        /* viewR = */this.copyRectangle(this.verticalViewR, viewR);
        /* iconR = */this.copyRectangle(this.verticalIconR, iconR);
        /* textR = */this.copyRectangle(this.verticalTextR, textR);
        return text;
    }

    /**
     * Transforms the Graphics for vertical rendering and invokes the super method.
     */
    @Override
    public void paint(Graphics g, JComponent c) {
        Graphics2D g2 = (Graphics2D) g.create();
        if (this.clockwise) {
            g2.rotate(Math.PI / 2, c.getSize().width / 2, c.getSize().width / 2);
        } else {
            g2.rotate(-Math.PI / 2, c.getSize().height / 2, c.getSize().height / 2);
        }
        super.paint(g2, c);
    }

    private Dimension transposeDimension(Dimension from) {
        return new Dimension(from.height, from.width + 2);
    }

    private Rectangle transposeRectangle(Rectangle from, Rectangle to) {
        if (to == null) {
            to = new Rectangle();
        }
        to.x = from.y;
        to.y = from.x;
        to.width = from.height;
        to.height = from.width;
        return to;
    }
}
