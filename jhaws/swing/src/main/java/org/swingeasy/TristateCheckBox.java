package org.swingeasy;

import java.awt.AWTEvent;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.ButtonModel;
import javax.swing.Icon;
import javax.swing.JCheckBox;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.ActionMapUIResource;

public final class TristateCheckBox extends JCheckBox {
    private static final long serialVersionUID = -7488108042920772251L;

    // Listener on model changes to maintain correct focusability
    private final ChangeListener enableListener = new ChangeListener() {
        @Override
        public void stateChanged(ChangeEvent e) {
            TristateCheckBox.this.setFocusable(TristateCheckBox.this.getModel().isEnabled());
        }
    };

    public TristateCheckBox(String text) {
        this(text, null, TristateState.DESELECTED);
    }

    public TristateCheckBox(String text, Icon icon, TristateState initial) {
        super(text, icon);

        // Set default single model
        this.setModel(new TristateButtonModel(initial));

        // override action behaviour
        super.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                TristateCheckBox.this.iterateState();
            }
        });
        ActionMap actions = new ActionMapUIResource();
        actions.put("pressed", new AbstractAction() { //$NON-NLS-1$
                    private static final long serialVersionUID = -4444403699594853761L;

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        TristateCheckBox.this.iterateState();
                    }
                });
        actions.put("released", null); //$NON-NLS-1$
        SwingUtilities.replaceUIActionMap(this, actions);
    }

    // Empty override of superclass method
    @Override
    public synchronized void addMouseListener(MouseListener l) {
        //
    }

    public TristateState getState() {
        return this.getTristateModel().getState();
    }

    public Boolean getStateValue() {
        switch (this.getState()) {
            case SELECTED:
                return Boolean.TRUE;
            case DESELECTED:
                return Boolean.FALSE;
            default:
            case INDETERMINATE:
                return null;
        }
    }

    // Convenience cast
    public TristateButtonModel getTristateModel() {
        return (TristateButtonModel) super.getModel();
    }

    public boolean isIndeterminate() {
        return this.getTristateModel().isIndeterminate();
    }

    // Mostly delegates to model
    private void iterateState() {
        // Maybe do nothing at all?
        if (!this.getModel().isEnabled()) {
            return;
        }

        this.grabFocus();

        // Iterate state
        this.getTristateModel().iterateState();

        // Fire ActionEvent
        int modifiers = 0;
        AWTEvent currentEvent = EventQueue.getCurrentEvent();
        if (currentEvent instanceof InputEvent) {
            modifiers = ((InputEvent) currentEvent).getModifiers();
        } else if (currentEvent instanceof ActionEvent) {
            modifiers = ((ActionEvent) currentEvent).getModifiers();
        }
        this.fireActionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, this.getText(), System.currentTimeMillis(), modifiers));
    }

    // Next two methods implement new API by delegation to model
    public void setIndeterminate() {
        this.getTristateModel().setIndeterminate();
    }

    // Overrides superclass method
    @Override
    public void setModel(ButtonModel newModel) {
        super.setModel(newModel);

        // Listen for enable changes
        if (this.model instanceof TristateButtonModel) {
            this.model.addChangeListener(this.enableListener);
        }
    }
}
