package org.swingeasy;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;

/**
 * requires commons-beanutils-core
 *
 * @author Jurgen
 */
public class ETableRecordBean<T> implements ETableRecord<T> {
    protected T object;

    protected List<String> orderedFields;

    protected final Map<String, Object> originalValues = new HashMap<String, Object>();

    public ETableRecordBean(List<String> orderedFields, T o) {
        this.object = o;
        this.orderedFields = orderedFields;
    }

    public ETableRecordBean(T o, String... orderedFields) {
        this.object = o;
        this.orderedFields = Arrays.asList(orderedFields);
    }

    /**
     *
     * @see org.swingeasy.ETableRecord#get(int)
     */
    @Override
    public Object get(int column) {
        try {
            Object property = PropertyUtils.getProperty(this.object, this.orderedFields.get(column));
            return property;
        } catch (IllegalAccessException ex) {
            ex.printStackTrace();
            return null;
        } catch (InvocationTargetException ex) {
            ex.printStackTrace();
            return null;
        } catch (NoSuchMethodException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    /**
     *
     * @see org.swingeasy.ETableRecord#getBean()
     */
    @Override
    public T getBean() {
        return this.object;
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
        try {
            String property = this.orderedFields.get(column);
            Object ov = this.originalValues.get(property);
            Object nv = PropertyUtils.getProperty(this.object, property);
            return !new EqualsBuilder().append(ov, nv).isEquals();
        } catch (IllegalAccessException ex) {
            throw new RuntimeException(ex);
        } catch (InvocationTargetException ex) {
            throw new RuntimeException(ex);
        } catch (NoSuchMethodException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     *
     * @see java.lang.Iterable#iterator()
     */
    @SuppressWarnings("rawtypes")
    @Override
    public Iterator<?> iterator() {
        return new Iterator() {
            int i = 0;

            @Override
            public boolean hasNext() {
                return this.i < ETableRecordBean.this.orderedFields.size();
            }

            @Override
            public Object next() {
                return ETableRecordBean.this.get(this.i++);
            }

            @Override
            public void remove() {
                //
            }
        };
    }

    /**
     *
     * @see org.swingeasy.ETableRecord#set(int, java.lang.Object)
     */
    @Override
    public void set(int column, Object newValue) {
        try {
            String property = this.orderedFields.get(column);
            if (this.originalValues.get(property) == null) {
                Object ov = PropertyUtils.getProperty(this.object, property);
                this.originalValues.put(property, ov == null ? Void.TYPE : ov);
            }
            PropertyUtils.setProperty(this.object, property, newValue);
        } catch (IllegalAccessException ex) {
            throw new RuntimeException(ex);
        } catch (InvocationTargetException ex) {
            throw new RuntimeException(ex);
        } catch (NoSuchMethodException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     *
     * @see org.swingeasy.ETableRecord#size()
     */
    @Override
    public int size() {
        return this.orderedFields.size();
    }

    /**
     *
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return String.valueOf(this.object);
    }
}
