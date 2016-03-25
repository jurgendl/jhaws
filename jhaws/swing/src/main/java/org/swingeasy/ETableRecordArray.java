package org.swingeasy;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.lang3.builder.EqualsBuilder;

/**
 * @author Jurgen
 */
public class ETableRecordArray<E> implements ETableRecord<E[]> {
    protected E[] array;

    protected final Map<Integer, E> originalValues = new HashMap<Integer, E>();

    public ETableRecordArray(@SuppressWarnings("unchecked") E... o) {
        this.array = o;
    }

    /**
     *
     * @see org.swingeasy.ETableRecord#get(int)
     */
    @Override
    public Object get(int column) {
        return this.array[column];
    }

    /**
     *
     * @see org.swingeasy.ETableRecord#getBean()
     */
    @Override
    public E[] getBean() {
        return this.array;
    }

    /**
     *
     * @see org.swingeasy.ETableRecord#getStringValue(int)
     */
    @Override
    public String getStringValue(int column) {
        Object value = this.get(column);
        return value == null ? null : "" + value; //$NON-NLS-1$
    }

    /**
     *
     * @see org.swingeasy.ETableRecord#getTooltip(int)
     */
    @Override
    public String getTooltip(int column) {
        return this.getStringValue(column);
    }

    /**
     *
     * @see org.swingeasy.ETableRecord#hasChanged(int)
     */
    @Override
    public boolean hasChanged(int column) {
        Object ov = this.originalValues.get(column);
        Object nv = this.get(column);
        return !new EqualsBuilder().append(ov, nv).isEquals();
    }

    /**
     *
     * @see java.lang.Iterable#iterator()
     */
    @Override
    public Iterator<?> iterator() {
        return Arrays.asList(this.array).iterator();
    }

    /**
     *
     * @see org.swingeasy.ETableRecord#set(int, java.lang.Object)
     */
    @SuppressWarnings("unchecked")
    @Override
    public void set(int column, Object newValue) {
        if (this.originalValues.get(column) == null) {
            Object ov = this.get(column);
            this.originalValues.put(column, (E) (ov == null ? Void.TYPE : ov));
        }
        this.array[column] = (E) newValue;
    }

    /**
     *
     * @see org.swingeasy.ETableRecord#size()
     */
    @Override
    public int size() {
        return this.array.length;
    }

    /**
     *
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return Arrays.toString(this.array);
    }
}
