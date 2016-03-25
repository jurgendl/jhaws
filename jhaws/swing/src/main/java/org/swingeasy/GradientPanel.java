package org.swingeasy;

import java.awt.Graphics;

import javax.swing.JPanel;

/**
 * @author Jurgen
 */
public class GradientPanel extends JPanel {
    private static final long serialVersionUID = -3019198419458872568L;

    protected EComponentGradientRenderer gradientRenderer = new EComponentGradientRenderer();

    public EComponentGradientRenderer getGradientRenderer() {
        return this.gradientRenderer;
    }

    /**
     * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        this.gradientRenderer.render(this, g);
    }

    public void setGradientRenderer(EComponentGradientRenderer gradientRenderer) {
        this.gradientRenderer = gradientRenderer;
    }
}