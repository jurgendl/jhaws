package org.swingeasy.list.renderer;

import java.awt.Component;
import java.awt.Graphics;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JComponent;
import javax.swing.JList;

import org.swingeasy.EComponentI;
import org.swingeasy.EComponentRenderer;

/**
 * @author Jurgen
 */
public class EListCellRenderer<T> extends DefaultListCellRenderer.UIResource implements EComponentI {
    private static final long serialVersionUID = -4058183204571727427L;

    protected EComponentRenderer backgroundRenderer;

    public EComponentRenderer getBackgroundRenderer() {
        return this.backgroundRenderer;
    }

    /**
     * @see javax.swing.DefaultListCellRenderer#getListCellRendererComponent(javax.swing.JList, java.lang.Object, int, boolean, boolean)
     */
    @SuppressWarnings("unchecked")
    @Override
    final public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        return this.render(list, (T) value, index, isSelected, cellHasFocus);
    }

    /**
     * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
     */
    @Override
    protected void paintComponent(Graphics g) {
        this.renderBackground(this, g);
        super.paintComponent(g);
    }

    protected Component render(JList<?> list, T value, int index, boolean isSelected, boolean cellHasFocus) {
        return this.super_getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
    }

    protected void renderBackground(JComponent c, Graphics g) {
        if (this.backgroundRenderer != null) {
            this.backgroundRenderer.render(c, g);
        }
    }

    public EListCellRenderer<T> setBackgroundRenderer(EComponentRenderer backgroundRenderer) {
        this.backgroundRenderer = backgroundRenderer;
        return this;
    }

    protected Component super_getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
    }
}
