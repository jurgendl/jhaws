package org.swingeasy;

/**
 * @author Jurgen
 */
@SuppressWarnings("rawtypes")
public interface ETableRecord<T> extends Iterable {
    public abstract Object get(int column);

    public abstract T getBean();

    public abstract String getStringValue(int column);

    public abstract String getTooltip(int column);

    public abstract boolean hasChanged(int column);

    public abstract void set(int column, Object newValue);

    public abstract int size();
}
