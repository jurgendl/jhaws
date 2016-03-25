package org.swingeasy;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Enumeration;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.ButtonModel;

/**
 * @author Jurgen
 */
public class EButtonGroup extends ButtonGroup implements ItemListener {
    private static final long serialVersionUID = 6087976164994858164L;

    public static final String SELECTION = "selection"; //$NON-NLS-1$

    protected final PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);

    protected ButtonModel previousSelection = null;

    /**
     * 
     * @see javax.swing.ButtonGroup#add(javax.swing.AbstractButton)
     */
    @Override
    public void add(AbstractButton b) {
        if (b.getModel().getActionCommand() == null) {
            b.setActionCommand(b.getText());
        }
        super.add(b);
        b.addItemListener(this);
    }

    /**
     * 
     * @see java.beans.PropertyChangeSupport#addPropertyChangeListener(java.beans.PropertyChangeListener)
     */
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        this.propertyChangeSupport.addPropertyChangeListener(listener);
    }

    /**
     * 
     * @see java.beans.PropertyChangeSupport#addPropertyChangeListener(java.lang.String, java.beans.PropertyChangeListener)
     */
    public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
        this.propertyChangeSupport.addPropertyChangeListener(propertyName, listener);
    }

    /**
     * 
     * @see java.beans.PropertyChangeSupport#getPropertyChangeListeners()
     */
    public PropertyChangeListener[] getPropertyChangeListeners() {
        return this.propertyChangeSupport.getPropertyChangeListeners();
    }

    /**
     * 
     * @see java.beans.PropertyChangeSupport#getPropertyChangeListeners(java.lang.String)
     */
    public PropertyChangeListener[] getPropertyChangeListeners(String propertyName) {
        return this.propertyChangeSupport.getPropertyChangeListeners(propertyName);
    }

    public String getSelectedButtonText() {
        for (Enumeration<AbstractButton> b = this.getElements(); b.hasMoreElements();) {
            AbstractButton button = b.nextElement();
            if (button.isSelected()) {
                return button.getText();
            }
        }
        return null;
    }

    /**
     * 
     * @see java.awt.event.ItemListener#itemStateChanged(java.awt.event.ItemEvent)
     */
    @Override
    public void itemStateChanged(ItemEvent e) {
        if (e.getStateChange() == java.awt.event.ItemEvent.SELECTED) {
            ButtonModel newSelection = this.getSelection();
            this.propertyChangeSupport.firePropertyChange(EButtonGroup.SELECTION,
                    this.previousSelection == null ? null : this.previousSelection.getActionCommand(),
                    newSelection == null ? null : newSelection.getActionCommand());
            this.previousSelection = newSelection;
        }
    }

    /**
     * 
     * @see javax.swing.ButtonGroup#remove(javax.swing.AbstractButton)
     */
    @Override
    public void remove(AbstractButton b) {
        super.remove(b);
        b.removeItemListener(this);
    }

    /**
     * 
     * @see java.beans.PropertyChangeSupport#removePropertyChangeListener(java.beans.PropertyChangeListener)
     */
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        this.propertyChangeSupport.removePropertyChangeListener(listener);
    }

    /**
     * 
     * @see java.beans.PropertyChangeSupport#removePropertyChangeListener(java.lang.String, java.beans.PropertyChangeListener)
     */
    public void removePropertyChangeListener(String propertyName, PropertyChangeListener listener) {
        this.propertyChangeSupport.removePropertyChangeListener(propertyName, listener);
    }
}
