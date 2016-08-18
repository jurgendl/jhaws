package org.swingeasy;

/**
 * @author Jurgen
 */
public interface HasValue<T> {
    public void addValueChangeListener(ValueChangeListener<T> listener);

    public void clearValueChangeListeners();

    public abstract T getValue();

    public void removeValueChangeListener(ValueChangeListener<T> listener);
}
