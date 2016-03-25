package org.swingeasy;


/**
 * @author Jurgen
 */
public interface ETreeTableRecord<T> extends ETableRecord<T>, Comparable<ETreeTableRecord<T>> {
    public abstract ETreeTableRecord<T> getParent();
}
