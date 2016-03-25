package org.swingeasy;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * @author Jurgen
 */
public class PropertyChangeParent {
    protected PropertyChangeSupport propertyChangeSupport;

    protected PropertyChangeParent() {
        this.propertyChangeSupport = new PropertyChangeSupport(this);
    }

    /**
     * @see java.beans.PropertyChangeSupport#addPropertyChangeListener(java.beans.PropertyChangeListener)
     */
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        this.propertyChangeSupport.addPropertyChangeListener(listener);
    }

    /**
     * @see java.beans.PropertyChangeSupport#addPropertyChangeListener(java.lang.String, java.beans.PropertyChangeListener)
     */
    public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
        this.propertyChangeSupport.addPropertyChangeListener(propertyName, listener);
    }

    /**
     * @see java.beans.PropertyChangeSupport#fireIndexedPropertyChange(java.lang.String, int, boolean, boolean)
     */
    public void fireIndexedPropertyChange(String propertyName, int index, boolean oldValue, boolean newValue) {
        this.propertyChangeSupport.fireIndexedPropertyChange(propertyName, index, oldValue, newValue);
    }

    /**
     * @see java.beans.PropertyChangeSupport#fireIndexedPropertyChange(java.lang.String, int, int, int)
     */
    public void fireIndexedPropertyChange(String propertyName, int index, int oldValue, int newValue) {
        this.propertyChangeSupport.fireIndexedPropertyChange(propertyName, index, oldValue, newValue);
    }

    /**
     * @see java.beans.PropertyChangeSupport#fireIndexedPropertyChange(java.lang.String, int, java.lang.Object, java.lang.Object)
     */
    public void fireIndexedPropertyChange(String propertyName, int index, Object oldValue, Object newValue) {
        this.propertyChangeSupport.fireIndexedPropertyChange(propertyName, index, oldValue, newValue);
    }

    /**
     * @see java.beans.PropertyChangeSupport#firePropertyChange(java.beans.PropertyChangeEvent)
     */
    public void firePropertyChange(PropertyChangeEvent evt) {
        this.propertyChangeSupport.firePropertyChange(evt);
    }

    /**
     * @see java.beans.PropertyChangeSupport#firePropertyChange(java.lang.String, boolean, boolean)
     */
    public void firePropertyChange(String propertyName, boolean oldValue, boolean newValue) {
        this.propertyChangeSupport.firePropertyChange(propertyName, oldValue, newValue);
    }

    /**
     * @see java.beans.PropertyChangeSupport#firePropertyChange(java.lang.String, int, int)
     */
    public void firePropertyChange(String propertyName, int oldValue, int newValue) {
        this.propertyChangeSupport.firePropertyChange(propertyName, oldValue, newValue);
    }

    /**
     * @see java.beans.PropertyChangeSupport#firePropertyChange(java.lang.String, java.lang.Object, java.lang.Object)
     */
    public void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
        this.propertyChangeSupport.firePropertyChange(propertyName, oldValue, newValue);
    }

    /**
     * @see java.beans.PropertyChangeSupport#getPropertyChangeListeners()
     */
    public PropertyChangeListener[] getPropertyChangeListeners() {
        return this.propertyChangeSupport.getPropertyChangeListeners();
    }

    /**
     * @see java.beans.PropertyChangeSupport#getPropertyChangeListeners(java.lang.String)
     */
    public PropertyChangeListener[] getPropertyChangeListeners(String propertyName) {
        return this.propertyChangeSupport.getPropertyChangeListeners(propertyName);
    }

    /**
     * @see java.beans.PropertyChangeSupport#hasListeners(java.lang.String)
     */
    public boolean hasListeners(String propertyName) {
        return this.propertyChangeSupport.hasListeners(propertyName);
    }

    /**
     * @see java.beans.PropertyChangeSupport#removePropertyChangeListener(java.beans.PropertyChangeListener)
     */
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        this.propertyChangeSupport.removePropertyChangeListener(listener);
    }

    /**
     * @see java.beans.PropertyChangeSupport#removePropertyChangeListener(java.lang.String, java.beans.PropertyChangeListener)
     */
    public void removePropertyChangeListener(String propertyName, PropertyChangeListener listener) {
        this.propertyChangeSupport.removePropertyChangeListener(propertyName, listener);
    }
}
