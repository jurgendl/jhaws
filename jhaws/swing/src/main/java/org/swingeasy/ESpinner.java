package org.swingeasy;

import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * @author Jurgen
 */
public class ESpinner<T> extends JSpinner {
    private static final long serialVersionUID = -5205530967336536976L;

    protected String customTooltip = null;

    public ESpinner(SpinnerModel model) {
        super(model);
        this.init();
    }

    @SuppressWarnings("unchecked")
    public T get() {
        return (T) this.getValue();
    }

    /**
     * 
     * @see javax.swing.JComponent#getToolTipText()
     */
    @Override
    public String getToolTipText() {
        if (this.customTooltip != null) {
            return this.customTooltip;
        }
        return super.getToolTipText();
    }

    public T gotoNextValue() {
        @SuppressWarnings("unchecked")
        T nextValue = (T) ESpinner.this.getNextValue();
        if (nextValue == null) {
            return null;
        }
        ESpinner.this.getModel().setValue(nextValue);
        return nextValue;
    }

    public T gotoPreviousValue() {
        @SuppressWarnings("unchecked")
        T nextValue = (T) ESpinner.this.getPreviousValue();
        if (nextValue == null) {
            return null;
        }
        ESpinner.this.getModel().setValue(nextValue);
        return nextValue;
    }

    protected void init() {
        this.addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                if (e.getWheelRotation() < 0) {
                    ESpinner.this.gotoPreviousValue();
                } else {
                    ESpinner.this.gotoNextValue();
                }
            }
        });

        this.getModel().addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                ESpinner.this.super_setToolTipText(String.valueOf(ESpinner.this.getModel().getValue()));
            }
        });
        ESpinner.this.super_setToolTipText(String.valueOf(ESpinner.this.getModel().getValue()));
    }

    /**
     * 
     * @see javax.swing.JComponent#setToolTipText(java.lang.String)
     */
    @Override
    public void setToolTipText(String text) {
        super.setToolTipText(text);
        this.customTooltip = text;
    }

    private void super_setToolTipText(String text) {
        super.setToolTipText(text);
    }
}
