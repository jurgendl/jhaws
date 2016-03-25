package org.swingeasy;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.builder.EqualsBuilder;

/**
 * @author Jurgen
 */

public class ETableRecordCollection<E> implements ETableRecord<List<E>> {
    protected List<E> collection;

    protected final Map<Integer, Object> originalValues = new HashMap<Integer, Object>();

    public ETableRecordCollection() {
        this.collection = new ArrayList<E>();
    }

    public ETableRecordCollection(Collection<E> o) {
        this.collection = new ArrayList<E>(o);
    }

    public ETableRecordCollection(List<E> o) {
        this.collection = o;
    }

    public void add(E item) {
        this.collection.add(item);
    }

    /**
     *
     * @see org.swingeasy.ETableRecord#get(int)
     */
    @Override
    public Object get(int column) {
        return this.collection.get(column);
    }

    /**
     *
     * @see org.swingeasy.ETableRecord#getBean()
     */
    @Override
    public List<E> getBean() {
        return this.collection;
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
        return Collections.unmodifiableList(this.collection).iterator();
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
            this.originalValues.put(column, ov == null ? Void.TYPE : ov);
        }
        this.collection.set(column, (E) newValue);
    }

    /**
     *
     * @see org.swingeasy.ETableRecord#size()
     */
    @Override
    public int size() {
        return this.collection.size();
    }

    /**
     *
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return this.collection.toString();
    }
}
