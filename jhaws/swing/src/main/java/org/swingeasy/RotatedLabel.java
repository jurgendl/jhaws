package org.swingeasy;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * @author Jurgen
 */
public class RotatedLabel extends JPanel implements Icon {
    
    private static final long serialVersionUID = 8025460362121403426L;

    private static final double NINETY_DEGREES = Math.toRadians(90.0);

    private JLabel label = new JLabel() {
        private static final long serialVersionUID = -2386180924411172809L;

        @Override
        public void setIcon(Icon icon) {
            super.setIcon(icon);
            super.setDisabledIcon(icon);
        };
    };

    private boolean clockwise = false;

    public RotatedLabel(String value, Icon icon, boolean clockwise) {
        this.label.setText(value);
        this.label.setIcon(icon);
        this.clockwise = clockwise;
    }

    public Icon getIcon() {
        return this.label.getIcon();
    }

    /**
     * Implementation of Icon interface (especially useful with side tabs on a JTabbedPane)
     * 
     * @return
     */
    @Override
    public int getIconHeight() {
        return this.getPreferredSize().height;
    }

    /**
     * 
     * @see javax.swing.Icon#getIconWidth()
     */
    @Override
    public int getIconWidth() {
        return this.getPreferredSize().width;
    }

    /**
     * 
     * @see javax.swing.JComponent#getPreferredSize()
     */
    @Override
    public Dimension getPreferredSize() {
        Dimension d = this.label.getPreferredSize();

        return new Dimension(d.height, d.width);
    }

    public String getText() {
        return this.label.getText();
    }

    public boolean isClockwise() {
        return this.clockwise;
    }

    /**
     * 
     * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
     */
    @Override
    public void paintComponent(Graphics g) {
        Dimension d = this.getSize();
        this.paintHere(this, g, 0, 0, d.width, d.height);
    }

    /**
     * doet effectieve rendering (painting)
     * 
     * @param c
     * @param g
     * @param x
     * @param y
     * @param width
     * @param height
     */
    public void paintHere( Component c, Graphics g, int x, int y, int width, int height) {
        if ((height <= 0) || (width <= 0)) {
            return;
        }

        this.label.updateUI();

        // Paint the JLabel into an image buffer...
        BufferedImage buffer = new BufferedImage(height, width, BufferedImage.TYPE_INT_ARGB_PRE);
        Graphics2D g2 = buffer.createGraphics();
        this.label.setSize(new Dimension(height, width));
        this.label.paint(g2);

        // ...then apply a transform while painting the buffer into the component
        AffineTransform af = AffineTransform.getTranslateInstance(this.clockwise ? x + width : x, this.clockwise ? y : y + height);
        AffineTransform af2 = AffineTransform.getRotateInstance(this.clockwise ? RotatedLabel.NINETY_DEGREES : -RotatedLabel.NINETY_DEGREES);
        af.concatenate(af2);

        ((Graphics2D) g).drawImage(buffer, af, this);
    }

    /**
     * 
     * @see javax.swing.Icon#paintIcon(java.awt.Component, java.awt.Graphics, int, int)
     */
    @Override
    public void paintIcon(Component c, Graphics g, int x, int y) {
        Dimension d = this.getPreferredSize();
        this.paintHere(c, g, x, y, d.width, d.height);
    }

    public void setClockwise(boolean clockwise) {
        this.clockwise = clockwise;
    }

    public void setHorizontalAlignment(int alignment) {
        this.label.setHorizontalAlignment(alignment);
    }

    public void setIcon(Icon icon) {
        this.label.setIcon(icon);
    }

    public void setText(String value) {
        this.label.setText(value);
    }

    public void setVerticalAlignment(int alignment) {
        this.label.setVerticalAlignment(alignment);
    }
}
