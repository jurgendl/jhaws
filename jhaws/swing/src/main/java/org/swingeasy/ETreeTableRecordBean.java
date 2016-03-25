package org.swingeasy;

import org.apache.commons.lang3.builder.CompareToBuilder;

/**
 * @author Jurgen
 */
public class ETreeTableRecordBean<T> extends ETableRecordBean<T> implements ETreeTableRecord<T> {
    protected ETreeTableRecord<T> parent = null;

    public ETreeTableRecordBean(ETreeTableRecord<T> parent, T o, String... orderedFields) {
        super(o, orderedFields);
        this.parent = parent;
    }

    /**
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    @Override
    public int compareTo(ETreeTableRecord<T> o) {
        CompareToBuilder cb = new CompareToBuilder();
        cb.append(String.valueOf(this.object), String.valueOf(((ETreeTableRecordBean<?>) o).object));
        return cb.toComparison();
    }

    /**
     * @see org.swingeasy.ETreeTableRecord#getParent()
     */
    @Override
    public ETreeTableRecord<T> getParent() {
        return this.parent;
    }

    public void setParent(ETreeTableRecord<T> parent) {
        this.parent = parent;
    }
}
