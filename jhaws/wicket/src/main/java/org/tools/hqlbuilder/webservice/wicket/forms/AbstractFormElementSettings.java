package org.tools.hqlbuilder.webservice.wicket.forms;

import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.PropertyUtilsBean;
import org.apache.commons.lang3.StringUtils;

public abstract class AbstractFormElementSettings<T extends AbstractFormElementSettings<T>> implements Serializable, Cloneable {
    private static final long serialVersionUID = -2716372832273804363L;

    protected boolean required = false;

    protected boolean readOnly = false;

    protected boolean showPlaceholder = false;

    protected boolean inheritId = false;

    public AbstractFormElementSettings() {
        super();
    }

    public AbstractFormElementSettings(boolean required) {
        this.required = required;
    }

    public AbstractFormElementSettings(T other) {
        try {
            BeanUtils.copyProperties(this, other);
        } catch (IllegalAccessException ex) {
            throw new RuntimeException(new CloneNotSupportedException(String.valueOf(ex)));
        } catch (InvocationTargetException ex) {
            throw new RuntimeException(new CloneNotSupportedException(String.valueOf(ex)));
        }
    }

    @SuppressWarnings("unchecked")
    protected T castThis() {
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    @Override
    public T clone() {
        try {
            return (T) this.getClass().cast(BeanUtils.cloneBean(this));
        } catch (IllegalAccessException ex) {
            throw new RuntimeException(new CloneNotSupportedException(String.valueOf(ex)));
        } catch (InstantiationException ex) {
            throw new RuntimeException(new CloneNotSupportedException(String.valueOf(ex)));
        } catch (InvocationTargetException ex) {
            throw new RuntimeException(new CloneNotSupportedException(String.valueOf(ex)));
        } catch (NoSuchMethodException ex) {
            throw new RuntimeException(new CloneNotSupportedException(String.valueOf(ex)));
        }
    }

    public boolean isInheritId() {
        return this.inheritId;
    }

    public boolean isReadOnly() {
        return this.readOnly;
    }

    public boolean isRequired() {
        return this.required;
    }

    public boolean isShowPlaceholder() {
        return this.showPlaceholder;
    }

    public T setInheritId(boolean inheritId) {
        this.inheritId = inheritId;
        return this.castThis();
    }

    public T setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
        return this.castThis();
    }

    public T setRequired(boolean required) {
        this.required = required;
        return this.castThis();
    }

    public T setShowPlaceholder(boolean showPlaceholder) {
        this.showPlaceholder = showPlaceholder;
        return this.castThis();
    }

    protected boolean skipForExport(String propertyName) {
        return "required".equals(propertyName) || "showPlaceholder".equals(propertyName) || "inheritId".equals(propertyName);
    }

    @Override
    public String toString() {
        try {
            StringBuilder builder = new StringBuilder("{");
            PropertyUtilsBean propertyUtils = BeanUtilsBean.getInstance().getPropertyUtils();
            for (PropertyDescriptor desc : propertyUtils.getPropertyDescriptors(this)) {
                String name = desc.getName();
                if ("class".equals(name) || this.skipForExport(name)) {
                    continue;
                }
                Object value = propertyUtils.getSimpleProperty(this, name);
                if (value == null) {
                    continue;
                }
                if (String.class.equals(desc.getPropertyType())) {
                    if (StringUtils.isBlank(String.class.cast(value))) {
                        continue;
                    }
                    builder.append(name).append(":").append("\"").append(value).append("\"").append(",");
                } else if (Enum.class.isAssignableFrom(desc.getPropertyType())) {
                    builder.append(name).append(":").append("\"").append(value).append("\"").append(",");
                } else {
                    builder.append(name).append(":").append(value).append(",");
                }
            }
            if (builder.length() == 1) {
                return "{}";
            }
            return builder.deleteCharAt(builder.length() - 1).append("}").toString();
        } catch (IllegalAccessException ex) {
            throw new RuntimeException(ex);
        } catch (InvocationTargetException ex) {
            throw new RuntimeException(ex);
        } catch (NoSuchMethodException ex) {
            throw new RuntimeException(ex);
        }
    }
}