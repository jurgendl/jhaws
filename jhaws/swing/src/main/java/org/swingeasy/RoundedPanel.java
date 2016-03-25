package org.swingeasy;

import java.awt.Graphics;
import java.awt.LayoutManager;

import javax.swing.JPanel;

/**
 * @author Jurgen
 */
public class RoundedPanel extends JPanel {
    
    private static final long serialVersionUID = -4175531991430641355L;

    private final RoundedComponentDelegate delegate;

    public RoundedPanel() {
        super();
        this.delegate = new RoundedComponentDelegate(this);
    }

    public RoundedPanel(boolean isDoubleBuffered) {
        super(isDoubleBuffered);
        this.delegate = new RoundedComponentDelegate(this);
    }

    public RoundedPanel(LayoutManager layout) {
        super(layout);
        this.delegate = new RoundedComponentDelegate(this);
    }

    public RoundedPanel(LayoutManager layout, boolean isDoubleBuffered) {
        super(layout, isDoubleBuffered);
        this.delegate = new RoundedComponentDelegate(this);
    }

    public RoundedComponentDelegate getDelegate() {
        return this.delegate;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        this.delegate.paintComponent(g);
    }
}
