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
        this.delegate = new RoundedComponentDelegate(this);
    }

    public RoundedOptionPane(Object message) {
        super(message);
        this.delegate = new RoundedComponentDelegate(this);
    }

    public RoundedOptionPane(Object message, int messageType) {
        super(message, messageType);
        this.delegate = new RoundedComponentDelegate(this);
    }

    public RoundedOptionPane(Object message, int messageType, int optionType) {
        super(message, messageType, optionType);
        this.delegate = new RoundedComponentDelegate(this);
    }

    public RoundedOptionPane(Object message, int messageType, int optionType, Icon icon) {
        super(message, messageType, optionType, icon);
        this.delegate = new RoundedComponentDelegate(this);
    }

    public RoundedOptionPane(Object message, int messageType, int optionType, Icon icon, Object[] options) {
        super(message, messageType, optionType, icon, options);
        this.delegate = new RoundedComponentDelegate(this);
    }

    public RoundedOptionPane(Object message, int messageType, int optionType, Icon icon, Object[] options, Object initialValue) {
        super(message, messageType, optionType, icon, options, initialValue);
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