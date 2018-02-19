package org.jhaws.common.lang;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class Property<T, P> {
    protected final PropertyDescriptor descriptor;

    public Property(PropertyDescriptor descriptor) {
        this.descriptor = descriptor;
    }

    @SuppressWarnings("unchecked")
    public Class<P> type() {
        return (Class<P>) this.descriptor.getPropertyType();
    }

    public String name() {
        return this.descriptor.getName();
    }

    public void write(T obj, P value) throws PropertyException {
        try {
            descriptor.getWriteMethod().invoke(obj, new Object[] { value });
        } catch (InvocationTargetException ex) {
            throw new PropertyException(descriptor.getPropertyType().getName() + " " + descriptor.getName(), ex.getTargetException());
        } catch (IllegalAccessException | IllegalArgumentException | ClassCastException ex) {
            throw new PropertyException(descriptor.getPropertyType().getName() + " " + descriptor.getName(), ex);
        }
    }

    @SuppressWarnings("unchecked")
    public P read(T obj) throws PropertyException {
        try {
            return (P) descriptor.getReadMethod().invoke(obj);
        } catch (InvocationTargetException ex) {
            throw new PropertyException(descriptor.getPropertyType().getName() + " " + descriptor.getName(), ex.getTargetException());
        } catch (IllegalAccessException | IllegalArgumentException | ClassCastException ex) {
            throw new PropertyException(descriptor.getPropertyType().getName() + " " + descriptor.getName(), ex);
        }
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).appendSuper(super.toString()).append("descriptor", descriptor).toString();
    }
}
