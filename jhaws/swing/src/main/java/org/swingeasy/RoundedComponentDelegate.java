package org.swingeasy;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JComponent;

/**
 * licensed under Apache License, Version 2.0 (file 'apache.license-2.0.txt' found in folder 'legal' or see link)<br>
 * adapted to paint on any {@link JComponent} (see link for code)<br>
 * 
 * @see http://www.codeproject.com/KB/java/rounded-jpanel.aspx
 * @see http://www.apache.org/licenses/LICENSE-2.0.html
 */
public class RoundedComponentDelegate {
    /** Double values for Horizzontal and Vertical radius of corner arcs */
    protected Dimension arcs = new Dimension(20, 20);

    /** Sets if it has an High Quality view */
    protected boolean highQuality = true;

    /** The transparency value of shadow. ( 0 - 255) */
    protected int shadowAlpha = 150;

    /** Color of shadow */
    protected Color shadowColor = Color.black;

    /** Distance between border of shadow and border of opaque panel */
    protected int shadowGap = 5;

    /** The offset of shadow. */
    protected int shadowOffset = 4;

    /** Sets if it drops shadow */
    protected boolean shady = true;

    /** Stroke size. it is recommended to set it to 1 for better view */
    protected int strokeSize = 1;

    protected final JComponent owner;

    public RoundedComponentDelegate(JComponent owner) {
        this.owner = owner;
    }

    /**
     * Get the value of arcs
     * 
     * @return the value of arcs
     */
    public Dimension getArcs() {
        return this.arcs;
    }

    /**
     * Get the value of shadowAlpha
     * 
     * @return the value of shadowAlpha
     */
    public int getShadowAlpha() {
        return this.shadowAlpha;
    }

    /**
     * Returns the Color of shadow.
     * 
     * @return a Color object.
     */
    public Color getShadowColor() {
        return this.shadowColor;
    }

    /**
     * Get the value of shadowGap
     * 
     * @return the value of shadowGap
     */
    public int getShadowGap() {
        return this.shadowGap;
    }

    /**
     * Get the value of shadowOffset
     * 
     * @return the value of shadowOffset
     */
    public int getShadowOffset() {
        return this.shadowOffset;
    }

    /**
     * Returns the size of strokes.
     * 
     * @return the value of size.
     */
    public float getStrokeSize() {
        return this.strokeSize;
    }

    /**
     * Check if component has High Quality enabled.
     * 
     * @return <b>TRUE</b> if it is HQ ; <b>FALSE</b> Otherwise
     */
    public boolean isHighQuality() {
        return this.highQuality;
    }

    /**
     * Check if component drops shadow.
     * 
     * @return <b>TRUE</b> if it drops shadow ; <b>FALSE</b> Otherwise
     */
    public boolean isShady() {
        return this.shady;
    }

    public void paintComponent(Graphics g) {
        int width = this.owner.getWidth();
        int height = this.owner.getHeight();
        @SuppressWarnings("hiding")
        int shadowGap = this.shadowGap;
        Color shadowColorA = new Color(this.shadowColor.getRed(), this.shadowColor.getGreen(), this.shadowColor.getBlue(), this.shadowAlpha);
        Graphics2D graphics = (Graphics2D) g;

        // Sets antialiasing if HQ.
        if (this.highQuality) {
            graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        }

        // Draws shadow borders if any.
        if (this.shady) {
            graphics.setColor(shadowColorA);
            graphics.fillRoundRect(this.shadowOffset,// X position
                    this.shadowOffset,// Y position
                    width - this.strokeSize - this.shadowOffset, // width
                    height - this.strokeSize - this.shadowOffset, // height
                    this.arcs.width, this.arcs.height);// arc Dimension
        } else {
            shadowGap = 1;
        }

        // Draws the rounded opaque panel with borders.
        graphics.setColor(this.owner.getBackground());
        graphics.fillRoundRect(0, 0, width - shadowGap, height - shadowGap, this.arcs.width, this.arcs.height);
        graphics.setColor(this.owner.getForeground());
        graphics.setStroke(new BasicStroke(this.strokeSize));
        graphics.drawRoundRect(0, 0, width - shadowGap, height - shadowGap, this.arcs.width, this.arcs.height);

        // Sets strokes to default, is better.
        graphics.setStroke(new BasicStroke());
    }

    /**
     * Set the value of arcs
     * 
     * @param arcs new value of arcs
     */
    public void setArcs(Dimension arcs) {
        this.arcs = arcs;
    }

    /**
     * Sets whether this component has High Quality or not
     * 
     * @param highQuality if <b>TRUE</b>, set this component to HQ
     */
    public void setHighQuality(boolean highQuality) {
        this.highQuality = highQuality;
    }

    /**
     * Set the value of shadowAlpha
     * 
     * @param shadowAlpha new value of shadowAlpha
     */
    public void setShadowAlpha(int shadowAlpha) {
        if ((shadowAlpha >= 0) && (shadowAlpha <= 255)) {
            this.shadowAlpha = shadowAlpha;
        } else {
            this.shadowAlpha = 255;
        }
    }

    /**
     * Sets the Color of shadow
     * 
     * @param shadowColor Color of shadow
     */
    public void setShadowColor(Color shadowColor) {
        this.shadowColor = shadowColor;
    }

    /**
     * Set the value of shadowGap
     * 
     * @param shadowGap new value of shadowGap
     */
    public void setShadowGap(int shadowGap) {
        if (shadowGap >= 1) {
            this.shadowGap = shadowGap;
        } else {
            this.shadowGap = 1;
        }
    }

    /**
     * Set the value of shadowOffset
     * 
     * @param shadowOffset new value of shadowOffset
     */
    public void setShadowOffset(int shadowOffset) {
        if (shadowOffset >= 1) {
            this.shadowOffset = shadowOffset;
        } else {
            this.shadowOffset = 1;
        }
    }

    /**
     * Sets whether this component drops shadow
     * 
     * @param shady if <b>TRUE</b>, it draws shadow
     */
    public void setShady(boolean shady) {
        this.shady = shady;
    }

    /**
     * Sets the stroke size value.
     * 
     * @param strokeSize stroke size value
     */
    public void setStrokeSize(int strokeSize) {
        this.strokeSize = strokeSize;
    }
}
