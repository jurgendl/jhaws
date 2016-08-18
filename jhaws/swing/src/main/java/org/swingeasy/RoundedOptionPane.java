package org.swingeasy;

import java.awt.Graphics;

import javax.swing.Icon;
import javax.swing.JOptionPane;

/**
 * @author Jurgen
 */
public class RoundedOptionPane extends JOptionPane {

    private static final long serialVersionUID = -4223179632092759961L;

    private final RoundedComponentDelegate delegate;

    public RoundedOptionPane() {
        super();
        delegate = new RoundedComponentDelegate(this);
    }

    public RoundedOptionPane(Object message) {
        super(message);
        delegate = new RoundedComponentDelegate(this);
    }

    public RoundedOptionPane(Object message, int messageType) {
        super(message, messageType);
        delegate = new RoundedComponentDelegate(this);
    }

    public RoundedOptionPane(Object message, int messageType, int optionType) {
        super(message, messageType, optionType);
        delegate = new RoundedComponentDelegate(this);
    }

    public RoundedOptionPane(Object message, int messageType, int optionType, Icon icon) {
        super(message, messageType, optionType, icon);
        delegate = new RoundedComponentDelegate(this);
    }

    public RoundedOptionPane(Object message, int messageType, int optionType, Icon icon, Object[] options) {
        super(message, messageType, optionType, icon, options);
        delegate = new RoundedComponentDelegate(this);
    }

    public RoundedOptionPane(Object message, int messageType, int optionType, Icon icon, Object[] options, Object initialValue) {
        super(message, messageType, optionType, icon, options, initialValue);
        delegate = new RoundedComponentDelegate(this);
    }

    public RoundedComponentDelegate getDelegate() {
        return delegate;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        delegate.paintComponent(g);
    }
}