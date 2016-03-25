package org.swingeasy;

/**
 * @author Jurgen
 */
public class ValueHolder<T> {
    protected T value;

    public ValueHolder() {
        super();
    }

    public ValueHolder(T value) {
        this.setValue(value);
    }

    public T getValue() {
        return this.value;
    }

    public void setValue(T value) {
        this.value = value;
    }
}